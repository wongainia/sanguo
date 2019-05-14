package com.vikings.sanguo.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.AESKeyCache;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.UserCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.message.HeartBeatResp;
import com.vikings.sanguo.message.StaticUserDataQueryResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserNotifyInfoClient;
import com.vikings.sanguo.network.GuildConnector;
import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.protos.ChatTimeInfo;
import com.vikings.sanguo.protos.MessageInfo;
import com.vikings.sanguo.protos.PlayerWantedInfo;
import com.vikings.sanguo.protos.RoleStatusInfo;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.protos.TroopInfo;
import com.vikings.sanguo.protos.TroopLogEvent;
import com.vikings.sanguo.protos.TroopLogInfo;
import com.vikings.sanguo.protos.UserNotifyInfo;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.ui.window.ChatWindow;
import com.vikings.sanguo.ui.window.PopupUI;
import com.vikings.sanguo.utils.PayUtil;

/**
 * 心跳通讯
 * 
 * @author Brad.Chen
 * 
 */
public class HeartBeat {

	public static final int QUERY_COUNT = 20;

	private int userVer;

	// 停止标识位
	private boolean stop = true;

	private int sleepTimeFast = Config.getIntConfig("heartBeatTime");

	private int sleepTimeSlow = Config.getIntConfig("heartBeatTimeSlow");

	private int sleepTime = sleepTimeSlow;

	private int seq = 0;

	private Handler handler = new Handler();

	private SyncDataSet syncData; // 登录时传入

	private boolean failedLatest = false;

	private List<Integer> chatIds = new ArrayList<Integer>();

	private List<ChatTimeInfo> chatTimeInfos = new ArrayList<ChatTimeInfo>();

	public static int guildChatMsgTime;// 在家族聊天界面取到的最新的聊天消息time，如果心跳取到的聊天消息time比记录的time小，说明已经看过了，不再提示

	public static long maxIdWantedPlayer;

	public HeartBeat(SyncDataSet syncData) {
		this.syncData = syncData;
		this.userVer = syncData.version;
	}

	public void updataChatIds() {
		synchronized (chatIds) {
			chatIds.clear();
			// 世界
			chatIds.add(0);
			// 国家
			if (null != Account.user && Account.user.hasCountry()) {
				chatIds.add(Account.user.getCountry());
			}
			// 家族
			if (null != Account.guildCache && Account.user.hasGuild()) {
				chatIds.add(Account.guildCache.getGuildid());
			}
		}
	}

	public List<Integer> getChatIds() {
		if (chatIds.isEmpty()) {
			updataChatIds();
		}
		return chatIds;
	}

	public void start() {
		if (!stop)
			return;
		stop = false;
		seq++;
		new Thread(new Worker(seq)).start();
	}

	public void stop() {
		stop = true;
	}

	private void speedUp() {
		this.sleepTime = sleepTimeFast;
	}

	private void speedDown() {
		this.sleepTime = sleepTimeSlow;
	}

	private void adjustSleep() {
		// 如果有网络异常标识，加速心跳
		if (failedLatest) {
			speedUp();
			return;
		}
		// 当前有战争 要加速同步
		if (Account.briefBattleInfoCache.countCurBattle() > 0) {
			speedUp();
			return;
		}
		// 当前有未清理领地 加速同步
		if (Account.richFiefCache.hasDitryFief()) {
			speedUp();
			return;
		}
		PopupUI cur = Config.getController().getCurPopupUI();
		if (cur != null && (cur instanceof ChatWindow)) {
			speedUp();
			return;
		}
		speedDown();
	}

	public void updateUI() throws GameException {
		if (null != syncData) {
			updateUI(syncData, false, false);
			syncData = null;
		}
	}

	/**
	 * 
	 * @param data
	 * @param diff
	 *            是否差量更新后的通知
	 * @param wanted
	 *            当前是否被追杀
	 * @throws GameException
	 */
	public void updateUI(SyncDataSet data, boolean diff, boolean wanted)
			throws GameException {
		if (data == null)
			return;

		if (diff) {
			if (data.userInfo != null) {
				UserAccountClient uac = data.userInfo.getData();
				if (!wanted && uac.isWanted())
					new FetchWantedUserNoticeInvoker().start();
			}
		}
	}

	// 判断是否需要进行心跳
	private boolean sleep() {
		long time = System.currentTimeMillis() - Config.lastUpdateTime;
		// 呆滞时间超过60s就不用进行心跳请求
		if (time > 1000 * 60 * 5)
			return true;
		else
			return false;
	}

	private class Worker implements Runnable {

		int threadSeq;

		public Worker(int threadSeq) {
			this.threadSeq = threadSeq;
		}

		@Override
		public void run() {
			while (!stop && threadSeq == seq) {
				adjustSleep();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (sleep() || stop || null == Account.user) // 添加判断，睡眠过程中停线程
					continue;
				try {
					HeartBeatResp resp = null;
					try {
						resp = GameBiz.getInstance().heartbeat(getChatIds(),
								Account.user.getCountry());
						Config.timeOffset = resp.getCurrentTime() * 1000L
								- System.currentTimeMillis();
					} catch (GameException e) {
						if (e.getResult() == ResultCode.RESULT_FAILED_TOKEN) {
							if (null != Account.user) {
								AESKeyCache.clear(Account.user.getId());
							}
						}
						throw e;
					}

					if (!resp.getChatTimeInfos().isEmpty()) {

						List<ChatTimeInfo> infos = resp.getChatTimeInfos();
						if (!chatTimeInfos.isEmpty()) {// 只有当之前有缓存数据时，才提醒
							for (ChatTimeInfo info : infos) {
								for (ChatTimeInfo oldInfo : chatTimeInfos) {
									if (info.getId().intValue() == oldInfo
											.getId().intValue()
											&& info.getTime().intValue() > oldInfo
													.getTime().intValue()) {
										// 起线程取最新的消息
										new FetchChatMsgInvoker(info).start();
									}
								}
							}
						}
						chatTimeInfos.clear();
						chatTimeInfos.addAll(resp.getChatTimeInfos());
					}

					if (maxIdWantedPlayer < resp.getMaxIdWantedPlayer()
							&& Account.user.getLevel() >= UIChecker.FUNC_BATTLE) {
						try {
							PlayerWantedInfoClient pwic = null;
							List<PlayerWantedInfoClient> list = GameBiz
									.getInstance().playerWantedInfoQuery(
											Account.user.getCountry(), 0, 1);
							if (!list.isEmpty()) {
								pwic = list.get(0);
								if (maxIdWantedPlayer != 0)
									Config.getController().getNotifyMsg()
											.addMsg(pwic);
								maxIdWantedPlayer = pwic.getInfo().getId();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (resp.getUserVer() != userVer) {
						// 加try-catch,如果此时由于网络异常，不影响后面的逻辑，同时加速心跳.在下次数据差量同步之前，全量同步constants中定义的敏感数据
						try {
							if (failedLatest) {
								SyncDataSet importantData = GameBiz
										.getInstance().userDataSyn2(
												Constants.SYNC_TYPE_ALL,
												Constants.DATA_TYPE_IMPORTANT);
								updateBattleCacheAll(importantData);
								failedLatest = false;
							}
							SyncDataSet data = GameBiz.getInstance()
									.userDataSyn2(Constants.SYNC_TYPE_DIFF,
											Constants.DATA_TYPE_ALL);
							userVer = data.version;
							if (userVer == 0)
								userVer = resp.getUserVer();
							handleVip(data);
							PayUtil.checkIfCharged(data.userInfo);
							boolean wanted = Account.user.isWanted();
							Account.updateSyncData(data);
							updateUI(data, true, wanted);
						} catch (GameException e) {
							Log.e("HeartBeat", e.getMessage(), e);
							failedLatest = true;
						}
					}

					// 判断是否有需要提示的消息
					if (resp.getMaxIdRealLog() > Account.maxIdRealLog) {
						// 提示消息，服务器忽略传入的第二个参数id和第三个参数count，返回所有的需要界面提示的日志
						StaticUserDataQueryResp rsp = GameBiz
								.getInstance()
								.staticUserDataQuery(
										StaticUserDataType.STATIC_USER_DATA_TYPE_REAL_LOG,
										0, 1);
						List<LogInfoClient> logInfos = rsp.getLogInfos();
						if (null != logInfos && !logInfos.isEmpty()) {
							// 暂存物品过期日志，提示过期物品时，需要将所有过期物品合并在一起提示。
							List<LogInfoClient> logs = new ArrayList<LogInfoClient>();
							CacheMgr.fillActionLog(logInfos);
							CacheMgr.fillLogUser(logInfos);
							CacheMgr.fillLogGuild(logInfos);
							for (LogInfoClient lic : logInfos) {
								int type = lic.getLogInfo().getType();
								if (type == LogInfoClient.LT_FRIEND_ADD) {
									Config.getController().getNotifyMsg()
											.addMsg(lic.getFromUser());
								} else if (type == LogInfoClient.LT_GUILD) {
									Config.getController().getNotifyMsg()
											.addMsg(lic);
								}
								if (type == LogInfoClient.LT_ITEM_EXPIRE) {
									logs.add(lic);
								}
							}
							if (!logs.isEmpty()) {
								handler.post(new StaleNotice(logs));
							}
						}
						if (rsp.getTroopLogInfos() != null) {
							TroopLogInfo merge = null;
							List<ArmInfo> troops = new ArrayList<ArmInfo>();
							for (TroopLogInfo t : rsp.getTroopLogInfos()) {
								if (t.getEvent() == TroopLogEvent.EVENT_TROOP_STARVATION
										.getNumber()) {
									merge = t;
									troops.addAll(t.getInfo().getInfosList());
								}
							}
							if (merge != null) {
								merge.setInfo(new TroopInfo()
										.setInfosList(troops));
								Config.getController().getNotifyMsg()
										.addMsg(merge);
							}
						}
						Account.maxIdRealLog = resp.getMaxIdRealLog();
					}
					// 聊天信息
					if (resp.getMaxIdMessage() > 0) {
						StaticUserDataQueryResp rsp = GameBiz
								.getInstance()
								.staticUserDataQuery(
										StaticUserDataType.STATIC_USER_DATA_TYPE_MESSAGE,
										0, 20);
						List<MessageInfo> mis = rsp.getMessageInfos();
						if (null != mis && !mis.isEmpty()) {
							List<MessageInfoClient> msgInfos = new ArrayList<MessageInfoClient>();
							Collections.sort(msgInfos);
							List<Integer> ids = new ArrayList<Integer>();

							// 加入消息是否已经读过的逻辑，修正问题单#5475
							BriefUserInfoClient briefUser = null;
							boolean read = false;
							PopupUI popupUI = Config.getController()
									.getCurPopupUI();
							if (null != popupUI
									&& popupUI instanceof ChatWindow) {
								briefUser = ((ChatWindow) popupUI)
										.getBriefUser();
							}
							for (int i = 0; i < mis.size(); i++) {
								MessageInfo info = mis.get(i);
								if (null != briefUser
										&& info.getFrom().intValue() == briefUser
												.getId().intValue())
									read = true;
								else
									read = false;
								MessageInfoClient msgInfo = MessageInfoClient
										.convert(info, read);
								msgInfos.add(msgInfo);
								// 聊天消息
								if (!ids.contains(msgInfo.getFrom()))
									ids.add(msgInfo.getFrom());
							}

							Account.msgInfoCache.addSyncMsg(msgInfos);

							if (!ids.isEmpty()) {
								List<BriefUserInfoClient> tempUsers = CacheMgr
										.getUser(ids);
								Config.getController()
										.addChatUser(
												UserCache.sequenceByIds(ids,
														tempUsers));
								handler.post(new ChatNotice(tempUsers));
							}

						}
					}
					// 系统公告有更新
					if (resp.getNotifyVer() > Account.notifyVer) {
						StaticUserDataQueryResp rsp = GameBiz
								.getInstance()
								.staticUserDataQuery(
										StaticUserDataType.STATIC_USER_DATA_TYPE_NOTIFY,
										0, 10);
						List<UserNotifyInfo> userNotifyInfos = rsp
								.getUserNotifyInfos();
						if (!userNotifyInfos.isEmpty()) {
							List<UserNotifyInfoClient> msgInfos = new ArrayList<UserNotifyInfoClient>();
							for (int i = 0; i < userNotifyInfos.size(); i++) {
								UserNotifyInfoClient msgInfo = UserNotifyInfoClient
										.convert(userNotifyInfos.get(i));
								if (null != msgInfo)
									msgInfos.add(msgInfo);
							}
							Account.notifyInfoCache.addSyncMsg(msgInfos);
						}
						Account.notifyVer = resp.getNotifyVer();
						Config.getController().getScrollText().fetchMsg();
					}

					if (null != Account.briefBattleInfoCache)
						Account.briefBattleInfoCache.checkData();

					// 更新家族信息
					if (null != Account.guildCache) {
						Account.guildCache.updata(false);
					}
					if (Account.myLordInfo != null)
						Account.myLordInfo.checkFood();
					AutoRecvManor.check();
				} catch (Exception e) {
					Log.e("HeartBeat", "HeartBeat" + e.getMessage(), e);
				}

			}
		}
	}

	private void updateBattleCacheAll(SyncDataSet data) {
		if (null != Account.briefBattleInfoCache) {
			Account.briefBattleInfoCache.battleVer = data.battleVer;
			Account.briefBattleInfoCache.mergeAll(data.battleIds);
		}
	}

	// 如果vip等级发生了变化，弹出vip对话框让玩家领奖
	private void handleVip(SyncDataSet data) {
		UserAccountClient user = data.userInfo.getData();
		if (null == user || !user.isRoleInfoPart3Valid())
			return;
		if (user.getCurVip().getLevel() > Account.user.getCurVip().getLevel()) {
			Account.updateSyncData(data);
			handler.post(new Runnable() {
				@Override
				public void run() {
					Config.getController().openVipListWindow();
				}
			});
		}
	}

	private class FetchChatMsgInvoker extends BackgroundInvoker {

		private ChatTimeInfo info;
		private GuildChatData data;

		public FetchChatMsgInvoker(ChatTimeInfo info) {
			this.info = info;
		}

		@Override
		protected void fire() throws GameException {
			data = GuildConnector.getLatestChatData(Config.snsUrl
					+ "/chat/guild/get", info.getId(), Account.user.getId());
			data.setUser(CacheMgr.userCache.getUser(data.getUserId()));
		}

		@Override
		protected void onOK() {
			if (data != null) {
				if (data.isGuildChatData()
						&& data.getUserId() != Account.user.getId()) { // 家族消息
					if (data.getTime() > guildChatMsgTime)
						handler.post(new ChatNotice(data));
				} else if (data.isWorldChatData() || data.isCountryChatData()) { // 国家,
																					// 世界消息
					// (2013年9月5日16:38:26，策划和测试一致要求不过滤自己的消息）
					Config.getController().getNotifyWorldChatMsg().addMsg(data);
				}
			}
		}
	}

	private class FetchWantedUserNoticeInvoker extends BackgroundInvoker {
		private PlayerWantedInfoClient pwic;

		@Override
		protected void fire() throws GameException {
			RoleStatusInfo statusInfo = Account.user.getWantedInfo();
			BriefUserInfoClient briefUser = CacheMgr.userCache
					.getUser(statusInfo.getValue());
			PlayerWantedInfo info = new PlayerWantedInfo().setTarget(
					Account.user.getId()).setUserid(statusInfo.getValue());
			pwic = new PlayerWantedInfoClient(info);
			pwic.setBriefUser(briefUser);
			pwic.setTargetUser(Account.user.bref());
		}

		@Override
		protected void onOK() {
			if (null != pwic)
				Config.getController().getNotifyMsg().addMsg(pwic);
		}

	}
}
