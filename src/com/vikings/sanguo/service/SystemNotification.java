package com.vikings.sanguo.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.activity.MainActivity;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BriefBattleInfoCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.QueryServerReq;
import com.vikings.sanguo.message.QueryServerResp;
import com.vikings.sanguo.message.StaticUserDataQueryReq;
import com.vikings.sanguo.message.StaticUserDataQueryResp;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.network.SocketShortConnector;
import com.vikings.sanguo.protos.BriefBattleInfo;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.protos.TrayNotifyFlag;
import com.vikings.sanguo.protos.TrayNotifyInfo;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.PrefAccessUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class SystemNotification extends Service implements Runnable {
	private static final int wait_time = 5 * 60 * 1000; // 5
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	private static final int THREE = 3;
	private static final long THREE_DAYS = THREE * ONE_DAY;
	private static final long SEVEN_DAYS = 7 * ONE_DAY;
	private static final int HEART_BEAT_TIME = 20;
	private static final String icon = "icon";

	private boolean loginNotify = false;
	private boolean running = true;
	private SocketShortConnector socketConn;
	private long lastAskTime;
	private Map<Long, BriefBattleInfoClient> content;
	private FileAccess fileAccess;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		running = true;
		fileAccess = new FileAccess(this);
		new Thread(this).start();// 启动线程服务
	}

	@Override
	public void onDestroy() {
		this.running = false;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 判断游戏是否启动
	 * 
	 * @return
	 */
	private boolean isGameRunning() {
		return Config.getController() != null;
	}

	@Override
	public void run() {
		while (running) {
			// 仅在游戏未运行时，且开启离线提醒, 后台service才工作
			if (!isGameRunning() && PrefAccessUtil.hasOfflineNotify(this)
					&& isValidUser())
				check();
			setServiceSleepTime();
		}
	}

	private boolean isValidUser() {
		AccountPswInfoClient client = fileAccess.getLastUser();
		if (null != client) {
			Config.serverId = client.getSid();
			Account.user = new UserAccountClient(0, "");
			Account.user.setInfo(client);
			return true;
		}
		return false;
	}

	private void setServiceSleepTime() {
		try {
			// 超过7天未登陆，休眠时间改为1天
			Thread.sleep(PrefAccessUtil.notLoginFor(this, SEVEN_DAYS) ? ONE_DAY
					: wait_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void check() {
		Config.loadProperty(this);
		notifyNotLoginFor3Days();
		// 服务器取提醒消息
		List<TrayNotifyInfo> list = askServer();

		if (!ListUtil.isNull(list))
			notifyNotifyMsg(getLastestNotifyMsg(list));

		// 战争信息提示
		notifyBeAttackedMsg(list);
		notifySurroundEndMsg();
	}

	private void notifyNotLoginFor3Days() {
		// 三天未登录
		if (PrefAccessUtil.notLoginFor(this, THREE_DAYS) && !loginNotify) {
			notify(icon, getStr(R.string.SystemNotification_checkLogin_1),
					StringUtil.repParams(
							getStr(R.string.SystemNotification_checkLogin_2),
							String.valueOf(THREE)),
					com.vikings.sanguo.Constants.TYPE_LOGIN);
			loginNotify = true;
		}
	}

	private TrayNotifyInfo getLastestNotifyMsg(List<TrayNotifyInfo> rs) {
		int time = (int) (PrefAccess.getLoginTime(this) / 1000);
		time += 30;
		TrayNotifyInfo info = null;
		for (TrayNotifyInfo tni : rs) {
			if (!StringUtil.isNull(tni.getMessage())) {
				// 未过期的消息通知
				if (tni.getTime() > time && isNotifyMsg(tni))
					info = tni;
			}
		}
		return info;
	}

	private void notifyNotifyMsg(TrayNotifyInfo info) {
		if (null != info && isNotifyMsg(info)) {
			// TRAY_NOTIFY_MESSAGE消息包含||，表示既有标题又有内容
			if (info.getMessage().contains("||")) {
				int idx = info.getMessage().indexOf("||");
				notify(icon, info.getMessage().substring(0, idx), info
						.getMessage().substring(idx + 2),
						com.vikings.sanguo.Constants.TYPE_MSG);
			} else
				notify(icon, getStr(R.string.SystemNotification_checkMsg_1),
						info.getMessage(),
						com.vikings.sanguo.Constants.TYPE_MSG);
		}
	}

	private boolean isNotifyMsg(TrayNotifyInfo info) {
		return info.getFlag() == TrayNotifyFlag.TRAY_NOTIFY_MESSAGE.getNumber();
	}

	private void notifyBeAttackedMsg(List<TrayNotifyInfo> rs) {
		if (rs == null)
			return;

		int logoutTime = (int) (PrefAccess.getLoginTime(this) / 1000);

		for (TrayNotifyInfo tni : rs) {
			if (isOwnBattleMsg(logoutTime, tni)) {
				notify(icon,
						getStr(R.string.SystemNotification_checkBattleInfo_5),
						getStr(R.string.SystemNotification_checkBattleInfo_6),
						com.vikings.sanguo.Constants.TYPE_MSG);
			} else if (TrayNotifyFlag.TRAY_NOTIFY_BATTLE_INFO.getNumber() == tni
					.getFlag()
					&& null != tni.getBattleInfo()
					&& logoutTime - tni.getTime() < HEART_BEAT_TIME) {
				if (null == content)
					content = BriefBattleInfoCache.getInstance(false, this)
							.getContent();

				BriefBattleInfo info = tni.getBattleInfo();
				if (!content.containsKey(info.getId())
						&& info.getDefender() == Account.user.getId()
						&& BattleStatus.BATTLE_STATE_SURROUND == TroopUtil
								.getCurBattleState(info.getState(),
										info.getTime())) {
					try {
						BriefBattleInfoClient bbic = convert(info);
						bbic.setStateWhenSave(BattleStatus.BATTLE_STATE_NONE);
						content.put(info.getId(), bbic);
					} catch (GameException e) {

					}

				}
			}
		}
	}

	public BriefBattleInfoClient convert(BriefBattleInfo battleInfo)
			throws GameException {
		BriefBattleInfoClient info = new BriefBattleInfoClient();
		info.setBattleid(battleInfo.getId());
		info.setType(battleInfo.getType());
		info.setDefendFiefid(battleInfo.getDefendFiefid());
		info.setAttackFiefid(battleInfo.getAttackFiefid());
		info.setAttacker(battleInfo.getAttacker());
		info.setDefender(battleInfo.getDefender());
		info.setState(battleInfo.getState());
		info.setTime(battleInfo.getTime());
		info.setScale(battleInfo.getScale());
		info.setAttackUnit(battleInfo.getAttackUnit());
		info.setDefendUnit(battleInfo.getDefendUnit());
		return info;
	}

	private boolean isOwnBattleMsg(int time, TrayNotifyInfo tni) {
		return null != tni
				&& TrayNotifyFlag.TRAY_NOTIFY_BATTLE_INFO.getNumber() == tni
						.getFlag() && null != tni.getBattleInfo()
				&& tni.getTime() > time
				&& Account.user.getId() == tni.getBattleInfo().getDefender();
	}

	private void notifySurroundEndMsg() {
		if (null == content)
			content = BriefBattleInfoCache.getInstance(false, this)
					.getContent();

		int time = (int) (PrefAccess.getLoginTime(this) / 1000);

		Iterator iter = content.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			BriefBattleInfoClient bbic = (BriefBattleInfoClient) entry
					.getValue();

			if (bbic.isNoNeedToOfflineNotify())
				continue;

			if (bbic.isSurroundEndWhenAtk()) {// 我方进攻围城结束
				notify(icon,
						getStr(R.string.SystemNotification_checkBattleInfo_1),
						getStr(R.string.SystemNotification_checkBattleInfo_2),
						com.vikings.sanguo.Constants.TYPE_MSG);
				iter.remove();
			} else if (bbic.isMeDefender()) {
				// 敌方进攻，开始包围
				notify(icon,
						getStr(R.string.SystemNotification_checkBattleInfo_5),
						getStr(R.string.SystemNotification_checkBattleInfo_6),
						com.vikings.sanguo.Constants.TYPE_MSG);
				iter.remove();
			}
		}
	}

	/**
	 * 联机服务器拉新消息
	 * 
	 * @return
	 */
	private List<TrayNotifyInfo> askServer() {
		List<TrayNotifyInfo> rs = new ArrayList<TrayNotifyInfo>();
		JSONObject saveData = fileAccess.getUserData();
		try {
			if (null == socketConn)
				socketConn = new SocketShortConnector();
			// 当前时间与上次查询地址时间大于一天，就重新查地址
			if (System.currentTimeMillis() - lastAskTime > ONE_DAY) {
				socketConn.setAddr(
						InetAddress.getByName(saveData.getString("ip")),
						saveData.getInt("port"));
				QueryServerResp addrResp = new QueryServerResp();
				socketConn.send(new QueryServerReq(), addrResp);
				socketConn.setAddr(addrResp.getAddr(), addrResp.getPort());
				lastAskTime = System.currentTimeMillis();
			}
			StaticUserDataQueryReq req = new StaticUserDataQueryReq(
					StaticUserDataType.STATIC_USER_DATA_TYPE_TRAY, 0, 1);
			StaticUserDataQueryResp resp = new StaticUserDataQueryResp();
			socketConn.send(req, resp);
			rs = resp.getTrayInfos();
		} catch (Exception e) {

		}
		return rs;
	}

	private void notify(String imgName, String title, String text, int notifyId) {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = findResId(imgName);
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification_layout);
		Bitmap bitmap = drawableToBitmap(this.getResources().getDrawable(
				findResId(imgName)));
		contentView.setImageViewBitmap(R.id.notify_icon, bitmap);
		contentView.setTextViewText(R.id.notify_title, title);
		contentView.setTextViewText(R.id.notify_prompt, text);
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		contentView.setTextViewText(R.id.notify_tick, hour + ":" + minute);
		notification.contentView = contentView;

		notification.contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		nm.notify(notifyId, notification);
	}

	private int findResId(String name) {
		Resources res = getResources();
		String resName = name;
		int i = name.indexOf(".");
		if (i != -1) {
			resName = name.substring(0, i);
		}
		int icons = res.getIdentifier(this.getPackageName() + ":drawable/"
				+ resName, null, null);
		if (icons == 0) {
			icons = res.getIdentifier(this.getPackageName() + ":drawable/"
					+ icon, null, null);
		}
		return icons;
	}

	private String getStr(int resId) {
		return getResources().getString(resId);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
}
