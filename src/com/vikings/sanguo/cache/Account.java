package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.CheckVersion;
import com.vikings.sanguo.invoker.EndGuider;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.AssistGodSoldierLogCache;
import com.vikings.sanguo.model.BattlePropDefine;
import com.vikings.sanguo.model.BonusProp;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.EquipmentCache;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.Storehouse;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserStatDataClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;

/**
 * 用户帐户相关信息
 * 
 * @author Brad.Chen
 * 
 */
public class Account {

	// 本机玩家
	public static UserAccountClient user;

	// 本机玩家好友
	public static List<Integer> friends;

	public static List<Integer> blackList;

	public static List<QuestInfoClient> myQuestInfo;

	private static List<UserStatDataClient> myFrinedsRank;

	// 城堡 建筑缓存
	public static ManorInfoClient manorInfoClient;

	// 玩家仓库
	public static Storehouse store = new Storehouse();
	// 玩家装备
	public static EquipmentCache equipmentCache = new EquipmentCache();

	// 聊天记录
	public static MessageInfoClientCache msgInfoCache;
	public static UserNotifyInfoClientCache notifyInfoCache;
	public static UnReadChatMsgCntCache unReadCntCache;

	public static int notifyVer;

	public static ReadLogCache readLog;

	public static long maxIdRealLog;

	public static long lastCheckQuestTime = 0;

	// 领主信息
	public static LordInfoClient myLordInfo;

	// 领主的领地信息
	public static RichFiefCache richFiefCache;

	// 缓存战争日志
	public static BattleLogInfoCache battleLogCache;

	// 缓存战争
	public static BriefBattleInfoCache briefBattleInfoCache;

	public static AssistGodSoldierLogCache assistCache;

	public static RichGuildInfoClientCache guildCache;

	public static ActInfoCache actInfoCache;

	public static ArmEnhanceCache armEnhanceCache;

	private static ArrayList<Long> favoriteFief = new ArrayList<Long>();

	public static boolean isfavoriteFiefFetched = false;

	public static boolean isfavoriteFiefError = false;

	// 本家族族长信息
	public static BriefUserInfoClient myGuildLeader = null;

	public static BriefFiefInfoClient guildAltar = null;

	// 将领相关
	public static HeroInfoCache heroInfoCache = new HeroInfoCache();

	public static PlunderedCache plunderedCache;

	public static void initAccout() {
		maxIdRealLog = PrefAccess.getMaxIdRealLog();
		msgInfoCache = MessageInfoClientCache.getInstance(user.getId());
		notifyInfoCache = UserNotifyInfoClientCache.getInstance(user.getId());
		unReadCntCache = new UnReadChatMsgCntCache();
		readLog = ReadLogCache.getInstance();
		plunderedCache = PlunderedCache.getInstance(user.getId());
	}

	// 好友相关

	public static void setFriendList(List<Integer> ids) {
		friends = ids;
	}

	public static boolean isFriend(BriefUserInfoClient u) {
		return friends.contains(u.getId().intValue());
	}

	public static void addFriend(BriefUserInfoClient u) {
		synchronized (user) {
			if (!friends.contains(u.getId()))
				friends.add(u.getId());
		}
	}

	public static void addFriend(List<Integer> ids) {
		synchronized (user) {
			for (int i = 0; i < ids.size(); i++) {
				if (!friends.contains(ids.get(i)))
					friends.add(ids.get(i));
			}
		}
	}

	public static void deleteFriend(BriefUserInfoClient u) {
		synchronized (user) {
			friends.remove((Integer) u.getId());
			if (myFrinedsRank != null && !myFrinedsRank.isEmpty()) {
				for (Iterator<UserStatDataClient> iter = myFrinedsRank
						.iterator(); iter.hasNext();) {
					UserStatDataClient usdc = iter.next();
					if (usdc.getUserId() == u.getId().intValue()) {
						iter.remove();
						break;
					}
				}
			}
		}
	}

	public static List<GuildUserData> getFriend(ResultPage resultPage)
			throws GameException {
		synchronized (user) {
			int start = resultPage.getCurIndex();
			int end = start + resultPage.getPageSize();
			if (end > friends.size())
				end = friends.size();
			if (start > end) {
				resultPage.setResult(new ArrayList<Integer>());
				resultPage.setTotal(friends.size());
				return new ArrayList<GuildUserData>();
			}

			// 取User
			List<Integer> userids = friends.subList(start, end);
			List<BriefUserInfoClient> userTemps = CacheMgr.getUser(userids);

			sortUsers(userTemps);

			// 组装数据
			List<GuildUserData> datas = new ArrayList<GuildUserData>();
			List<GuildUserData> dataTemps = packGuildUserDatas(userTemps);
			// 保持顺序
			for (int i = 0; i < userids.size(); i++) {
				for (GuildUserData data : dataTemps) {
					if (data.getUser().getId().intValue() == userids.get(i)) {
						datas.add(data);
						break;
					}
				}
			}

			resultPage.setResult(datas);
			resultPage.setTotal(friends.size());
			return datas;
		}
	}

	public static void sortFriend() throws GameException {
		synchronized (Account.user) {
			ArrayList<Integer> friends = new ArrayList<Integer>();
			List<UserStatDataClient> datas = Account.getFriendRank();
			if (datas != null) {
				for (UserStatDataClient ri : datas) {
					friends.add(ri.getUserId());
				}
				if (Account.friends != null) {
					for (int i = 0; i < Account.friends.size(); i++) {
						int id = Account.friends.get(i);
						if (!friends.contains(id))
							friends.add(id);
					}
				}
				Account.friends = friends;
			}
		}
	}

	private static void sortUsers(List<BriefUserInfoClient> userTemps) {
		Collections.sort(userTemps, new Comparator<BriefUserInfoClient>() {

			@Override
			public int compare(BriefUserInfoClient data1,
					BriefUserInfoClient data2) {
				return data2.getLevel() - data1.getLevel();
			}
		});
	}

	// 排行
	public static void updateFriendRank(BriefUserInfoClient u) {
		synchronized (user) {
			if (null == myFrinedsRank || u == null)
				return;
			for (UserStatDataClient usdc : myFrinedsRank) {
				if (usdc.getUserId() == u.getId().intValue()) {
					usdc.setLevel(u.getLevel().intValue());
					return;
				}
			}
			UserStatDataClient newData = new UserStatDataClient();
			newData.setUserId(u.getId().intValue());
			newData.setLevel(u.getLevel().intValue());
			myFrinedsRank.add(newData);
		}
	}

	public static void setFriendRank(List<UserStatDataClient> datas) {
		myFrinedsRank = datas;
	}

	public static List<UserStatDataClient> getFriendRank() {
		if (null != myFrinedsRank) {
			Collections.sort(myFrinedsRank,
					new Comparator<UserStatDataClient>() {

						@Override
						public int compare(UserStatDataClient data1,
								UserStatDataClient data2) {
							if (data1.getLevel() == data2.getLevel()) {
								return data2.getExp() - data1.getExp();
							} else {
								return data2.getLevel() - data1.getLevel();
							}
						}
					});
		}
		return myFrinedsRank;
	}

	// 黑名单相关

	public static void setBlackList(List<Integer> ids) {
		blackList = ids;
	}

	public static boolean isBlackList(BriefUserInfoClient u) {
		return blackList.contains(u.getId().intValue());
	}

	public static void addBlackList(BriefUserInfoClient u) {
		synchronized (user) {
			if (!blackList.contains(u.getId().intValue()))
				blackList.add(u.getId());
			if (friends.contains(u.getId().intValue()))
				friends.remove(u.getId());

			if (!ListUtil.isNull(myFrinedsRank)) {
				Iterator<UserStatDataClient> it = myFrinedsRank.iterator();
				while (it.hasNext()) {
					UserStatDataClient usdc = it.next();
					if (usdc.getUserId() == u.getId().intValue())
						it.remove();
				}
			}
		}
	}

	public static String getSubmitExtendData() {
		JSONObject json = new JSONObject();
		if (null != Account.user) {
			try {
				json.put("roleId", Account.user.getId());
				json.put("roleName", Account.user.getNick());
				json.put("roleLevel", Account.user.getLevel());
				json.put("zoneId", Config.serverId);
				json.put("balance", Account.user.getCurrency());
				json.put("gamerVip", Account.user.getCurVip().getLevel());
			} catch (JSONException e) {

			}
		}
		return json.toString();
	}

	public static void deleteBlackList(BriefUserInfoClient u) {
		synchronized (user) {
			blackList.remove((Integer) u.getId());
		}
	}

	/**
	 * 是否是领主 服务器（史炼）说没有fief，也可能有lordInfo，所以是否是领主只能通过fief的数量判断
	 * 
	 * @return
	 */
	public static boolean isLord() {
		if (null != richFiefCache)
			return richFiefCache.getFiefids().size() > 0;
		return false;
	}

	public static void getBlackList(ResultPage resultPage) throws GameException {
		synchronized (user) {
			int start = resultPage.getCurIndex();
			int end = start + resultPage.getPageSize();
			if (end > blackList.size())
				end = blackList.size();
			if (start > end) {
				resultPage.setResult(new ArrayList<Integer>());
				resultPage.setTotal(blackList.size());
				return;
			}
			List<BriefUserInfoClient> users = CacheMgr.getUser(blackList
					.subList(start, end));

			resultPage.setResult(packGuildUserDatas(users));
			resultPage.setTotal(blackList.size());
		}
	}

	public static List<GuildUserData> packGuildUserDatas(
			List<BriefUserInfoClient> briefUsers) throws GameException {
		List<GuildUserData> datas = new ArrayList<GuildUserData>();
		List<Integer> guildids = new ArrayList<Integer>();
		for (BriefUserInfoClient user : briefUsers) {
			if (user.hasGuild()
					&& !guildids.contains(user.getGuildid().intValue()))
				guildids.add(user.getGuildid());
		}

		List<BriefGuildInfoClient> bgics = CacheMgr
				.getBriefGuildInfoClient(guildids);

		for (BriefUserInfoClient user : briefUsers) {
			GuildUserData data = new GuildUserData();
			data.setUser(user);
			if (user.hasGuild()) {
				for (BriefGuildInfoClient bgic : bgics) {
					if (user.getGuildid() == bgic.getId()) {
						data.setBgic(bgic);
						break;
					}
				}
			}
			if (user.hasCountry())
				data.setCountry(CacheMgr.countryCache.getCountry(user
						.getCountry().intValue()));
			datas.add(data);
		}

		return datas;
	}

	/**
	 * 根据任务类型取任务列表
	 * 
	 * @param type
	 * @return
	 */
	public static List<QuestInfoClient> getQuestInfoByType(byte type) {
		synchronized (user) {
			List<QuestInfoClient> list = new ArrayList<QuestInfoClient>();
			for (QuestInfoClient questInfo : myQuestInfo) {
				if (questInfo.getQuest().getType() == type
						&& questInfo.isWithinTime())
					list.add(questInfo);
			}
			Collections.sort(list, new Comparator<QuestInfoClient>() {

				@Override
				public int compare(QuestInfoClient q1, QuestInfoClient q2) {
					if (q1.isComplete() && !q2.isComplete())
						return -1;
					else if (!q1.isComplete() && q2.isComplete()) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			return list;
		}
	}

	public static List<QuestInfoClient> getQuestInfoBySpecialType(int type) {
		synchronized (user) {
			List<QuestInfoClient> list = new ArrayList<QuestInfoClient>();
			for (QuestInfoClient questInfo : myQuestInfo) {
				if (questInfo.getQuest().getSpecialType() == type
						&& questInfo.isWithinTime())
					list.add(questInfo);
			}
			Collections.sort(list, new Comparator<QuestInfoClient>() {

				@Override
				public int compare(QuestInfoClient q1, QuestInfoClient q2) {
					if (q1.isComplete() && !q2.isComplete())
						return -1;
					else if (!q1.isComplete() && q2.isComplete()) {
						return 1;
					} else {
						return 0;
					}
				}

			});
			return list;
		}
	}

	public static List<QuestInfoClient> getNormalQuest() {
		synchronized (user) {
			List<QuestInfoClient> ls = getQuestInfoAll();
			List<QuestInfoClient> tmp = new ArrayList<QuestInfoClient>();

			for (QuestInfoClient it : ls) {
				if (it.isNormalType() && it.isWithinTime())
					tmp.add(it);
			}
			return tmp;
		}
	}

	public static boolean hasBonus(List<QuestInfoClient> ls) {
		for (QuestInfoClient it : ls) {
			if (it.isComplete()) {
				return true;
			}
		}
		return false;
	}

	public static List<QuestInfoClient> getArenaQuest() {
		synchronized (user) {
			List<QuestInfoClient> ls = getQuestInfoAll();
			List<QuestInfoClient> tmp = new ArrayList<QuestInfoClient>();

			for (QuestInfoClient it : ls) {
				if (it.isArenaType() && it.isWithinTime())
					tmp.add(it);
			}
			return tmp;
		}
	}

	public static QuestInfoClient getUpdateQuest() {
		synchronized (user) {
			List<QuestInfoClient> ls = getQuestInfoAll();

			for (QuestInfoClient it : ls) {
				if (it.isUpdate() && it.isWithinTime())
					return it;
			}
			return null;
		}
	}

	public static boolean hasQuestComplete() {
		synchronized (user) {
			if (ListUtil.isNull(myQuestInfo))
				return false;

			for (QuestInfoClient it : myQuestInfo) {
				if (it.isArenaType())
					continue;
				if (it.isWithinTime()) {
					if (it.getQuest().getSpecialType() == BonusProp.TYPE_UPDATE) {
						BonusProp prop = CacheMgr.bonusCache
								.getBySpecialType(BonusProp.TYPE_UPDATE);
						if (null != prop && !prop.isExpired()
								&& CheckVersion.isNewer(prop.getVerCode()))
							return true;
					} else if (it.getQuest().getSpecialType() == Quest.SPECIAL_TYPE_RECHARGE
							&& Config.isMainPak()) {
						if (it.isComplete())
							return true;
					} else {
						if (it.isComplete())
							return true;
					}
				}
			}

			return false;
		}
	}

	public static List<QuestInfoClient> getQuestInfoAll() {
		synchronized (user) {
			return new ArrayList<QuestInfoClient>(myQuestInfo);
		}
	}

	public static QuestInfoClient getQuestInfoById(int id) {
		synchronized (user) {
			for (QuestInfoClient questInfo : myQuestInfo) {
				if (questInfo.getQuestId() == id && questInfo.isWithinTime())
					return questInfo;
			}
			return null;
		}
	}

	// 我要变强专用 通过Id查询任务
	public static QuestInfoClient getStrongerQuestInfoById(int id) {
		QuestInfoClient questInfoClient = new QuestInfoClient();
		synchronized (user) {
			for (QuestInfoClient questInfo : myQuestInfo) {
				if (questInfo.getQuestId() == id) {
					questInfoClient.setQuestId(id);
					questInfoClient.setState(questInfo.getState());
					return questInfoClient;
				}
			}
			return null;
		}
	}

	public static void removeQuest(QuestInfoClient qi) {
		synchronized (user) {
			myQuestInfo.remove(qi);
		}
	}

	// 新手救济任务，null表示没有
	public static QuestInfoClient getNoviceHelpQuest() {
		if (user.getLevel() > CacheMgr.dictCache
				.getDictInt(Dict.TYPE_NOVICE, 1))
			return null;
		if (isVip())
			return null;
		List<QuestInfoClient> qics = getQuestInfoBySpecialType(Quest.SPECIAL_TYPE_NOVICE_HELP);
		if (qics.isEmpty())
			return null;
		return qics.get(0);
	}

	public static void saveAccount() {
		if (user == null)
			return;
		PrefAccess.saveNotifyVer(notifyVer);
		PrefAccess.saveMaxIdRealLog(maxIdRealLog);
		PrefAccess.saveQuit();
		if (null != Account.richFiefCache)
			PrefAccess.saveLastFiefCount(Account.richFiefCache.getFiefids()
					.size());

		AESKeyCache.save();
		Config.getController().getFileAccess()
				.updateUser(Config.serverId, user);
		if (null != msgInfoCache)
			msgInfoCache.save();
		if (null != notifyInfoCache)
			notifyInfoCache.save();
		if (null != readLog)
			readLog.save();
		if (null != CacheMgr.addrCache)
			CacheMgr.addrCache.save();
		if (null != briefBattleInfoCache)
			briefBattleInfoCache.save();
		if (null != assistCache)
			assistCache.save();

		// PrefAccess.saveUser(Account.user);
	}

	/**
	 * 用户数据全部覆盖
	 * 
	 * @param data
	 */
	public static void setSyncDataAll(SyncDataSet data) {
		notifyVer = data.notifyVer;
		if (data.userInfo != null)
			user.update(data.userInfo.getData());
		// 排行榜排好友临时解决方案
		friends = new ArrayList<Integer>();
		if (data.friendInfos != null) {
			for (int i = 0; i < data.friendInfos.length; i++) {
				Integer id = data.friendInfos[i].getData();
				if (!friends.contains(id))
					friends.add(id);
			}
		}

		// 黑名单
		blackList = new ArrayList<Integer>();
		if (null != data.blackListInfoClients) {
			for (int i = 0; i < data.blackListInfoClients.length; i++) {
				blackList.add(data.blackListInfoClients[i].getData().getBli()
						.getBi().getUserid());
			}
		}

		// 背包
		store = new Storehouse();
		if (data.bagInfos != null) {
			for (int i = 0; i < data.bagInfos.length; i++) {
				store.add(data.bagInfos[i].getData());
			}
		}

		equipmentCache = new EquipmentCache();
		if (data.equipmentInfos != null) {
			for (int i = 0; i < data.equipmentInfos.length; i++) {
				equipmentCache.add(data.equipmentInfos[i].getData());
			}
		}

		// 城堡信息
		if (null != data.manorInfoClient)
			manorInfoClient = data.manorInfoClient.getData();
		else
			manorInfoClient = new ManorInfoClient();

		// 任务
		myQuestInfo = new ArrayList<QuestInfoClient>();
		if (data.questInfos != null) {
			for (int i = 0; i < data.questInfos.length; i++) {
				myQuestInfo.add(data.questInfos[i].getData());
			}
		}

		// 领主信息
		if (null != data.lordInfoClient)
			myLordInfo = data.lordInfoClient.getData();

		// 领地信息,同步更改id,具体用到的地方再取RichFiefInfo
		richFiefCache = new RichFiefCache();
		if (null != data.lordFiefInfos) {
			richFiefCache.merge(data.lordFiefInfos);
		}
		richFiefCache.fixEmptyMaonor();

		// 战斗id信息
		briefBattleInfoCache = BriefBattleInfoCache.getInstance(true);
		briefBattleInfoCache.battleVer = data.battleVer;
		if (null != data.battleIds) {
			briefBattleInfoCache.mergeAll(data.battleIds);
		}

		battleLogCache = new BattleLogInfoCache();

		// 家族
		guildCache = new RichGuildInfoClientCache();
		if (null != data.guildId)
			guildCache.setGuildid(data.guildId.getData());
		// 将领
		if (data.heroInfos != null) {
			heroInfoCache.merge(data.heroInfos);
		}
		// 副本
		actInfoCache.updateDate(data);
		armEnhanceCache.updateDate(data);

		assistCache = AssistGodSoldierLogCache.getInstance();
	}

	public static void updateQuest(SyncDataSet data) {
		if (data.questInfos != null) {
			for (int i = 0; i < data.questInfos.length; i++) {
				data.questInfos[i].update2List(myQuestInfo);
			}
		}
	}

	public static void updateLordInfo(SyncDataSet data) {
		if (data.lordInfoClient != null) {
			myLordInfo = data.lordInfoClient.getData();
		}
	}

	/**
	 * 差量更新
	 * 
	 * @param data
	 */
	public static void updateSyncData(SyncDataSet data) {
		synchronized (user) {
			if (data.userInfo != null)
				user.update(data.userInfo.getData());
			// 更新好友
			if (data.friendInfos != null) {
				for (int i = 0; i < data.friendInfos.length; i++) {
					data.friendInfos[i].update2List(friends);
				}
			}
			if (data.bagInfos != null) {
				for (int i = 0; i < data.bagInfos.length; i++) {
					data.bagInfos[i].update2List(store.get(data.bagInfos[i]
							.getData().getItem().getClentType()));
				}
			}

			if (data.equipmentInfos != null) {
				equipmentCache.merge(data.equipmentInfos);
			}

			// 城堡
			if (data.manorInfoClient != null) {
				manorInfoClient = data.manorInfoClient.getData();
			}

			if (data.questInfos != null) {
				for (int i = 0; i < data.questInfos.length; i++) {
					data.questInfos[i].update2List(myQuestInfo);
				}
			}

			// 领主信息
			if (data.lordInfoClient != null) {
				myLordInfo = data.lordInfoClient.getData();
			}
			// 领地信息
			if (data.lordFiefInfos != null) {
				richFiefCache.merge(data.lordFiefInfos);
			}
			// 战斗id
			briefBattleInfoCache
					.mergeDiff(data.battleVer, data.battleIds, true);

			// 家族id
			if (data.guildId != null) {
				guildCache.merge(data.guildId);
			}

			// 将领
			if (data.heroInfos != null) {
				heroInfoCache.merge(data.heroInfos);
			}

			actInfoCache.updateDate(data);
			armEnhanceCache.updateDate(data);
		}
	}

	/**
	 * 是否完成新手教程
	 * 
	 * @return 1:新手且未完成新手教程 2:新手且已完成新手教程 3:非新手
	 */
	static public int finishedFlag() {
		if (user.isNew() && !readLog.FINISHED_FRESHMAN_STUDYING) {
			return 1;
		} else if (user.isNew() && readLog.FINISHED_FRESHMAN_STUDYING) {
			return 2;
		} else {
			return 3;
		}
	}

	// ReturnInfoClient 除了个人数值需更新数据 其他只做展示， 兵力武将建筑 都由返回接口中其他数据更新
	public static void updateData(ReturnInfoClient rsinfo) {
		if (null == rsinfo)
			return;
		synchronized (user) {
			user.update(rsinfo.getReturnInfo());
			List<ItemBag> ls = rsinfo.getItemPack();
			if (ls != null) {
				for (ItemBag it : ls) {
					Account.store.addItemPack(it);
				}
			}
			// 如果获得了军队信息,修改lord 用于显示
			if (null != rsinfo.getArmInfos() && !rsinfo.getArmInfos().isEmpty()) {
				if (null != Account.myLordInfo)
					Account.myLordInfo.addArmInfos(rsinfo.getArmInfos());
			}
		}
	}

	public static void setFavoriteFief(ArrayList<Long> favoriteFief) {
		Account.favoriteFief = favoriteFief;
	}

	public static ArrayList<Long> getFavoriteFief() {
		ArrayList<Long> fiefIds = new ArrayList<Long>();
		if (null != Account.favoriteFief && !Account.favoriteFief.isEmpty()) {
			for (Long id : Account.favoriteFief) {
				if (!Account.richFiefCache.isMyFief(id))
					fiefIds.add(id);
			}
		}
		return fiefIds;
	}

	public static boolean isFavoriteFief(Long id) {
		if (null == favoriteFief)
			return false;
		return favoriteFief.contains(id);
	}

	// 获取领地收藏夹
	public static void updateFavorateFief() throws GameException {
		if (!isfavoriteFiefFetched || (isfavoriteFiefError)) {
			ArrayList<Long> ls = (ArrayList<Long>) GameBiz.getInstance()
					.favoriteFiefQuery();
			if (null != ls && ls.size() > 0)
				setFavoriteFief(ls);
			isfavoriteFiefFetched = true;
			isfavoriteFiefError = false;
		}
	}

	public static void addFavoriteFief(Long id) {
		if (null == favoriteFief)
			favoriteFief = new ArrayList<Long>(1);
		if (!favoriteFief.contains(id))
			favoriteFief.add(id);
	}

	public static void deleteFavoriteFief(Long id) {
		if (null == favoriteFief)
			return;
		if (favoriteFief.contains(id))
			favoriteFief.remove(id);
	}

	public static boolean hasFavorateFief() {
		List<Long> favoriteFiefIds = getFavoriteFief();
		return (favoriteFiefIds.size() > 0);
	}

	public static boolean isRemoteFief(long id) {
		long[] neighbour = TileUtil.getNeighbourFiefId(id);
		boolean flag = false;
		for (long nerId : neighbour) {
			if (Account.richFiefCache.getFiefids().contains(nerId)) {
				flag = true;
			}
		}
		if (flag) // flag 为true的时候说明包含，说明为近攻
			return false;
		return true;
	}

	public static boolean isLogin() {
		return manorInfoClient != null;
	}

	// 获取当前vip等级
	public static UserVip getCurVip() {
		return user.getCurVip();
	}

	public static boolean isNotVip() {
		return getCurVip() == null || getCurVip().getLevel() <= 0;
	}

	public static int getAmountToVip(int vipLevel) {
		UserVip vipCfg = CacheMgr.userVipCache.getVipByLvl(vipLevel);
		int amount = vipCfg.getCharge() - user.getCharge();
		if (amount < 0)
			amount = 0;
		return amount;
	}

	public static int getVipExtraActTimes(ActInfoClient actClient) {
		int extra = 0;
		// if (actClient.isBattleAct())
		// extra = Account.getCurVip().getExActTimes();
		if (actClient.isServerActivityAct()) // isActivityAct() else
			extra = Account.getCurVip().getExActivityTimes();
		else
			extra = Account.getCurVip().getExActTimes();
		return extra;
	}

	public static boolean isVip() {
		return getCurVip().getLevel() > 0;
	}

	public static boolean isTopVip() {
		return getCurVip().getLevel() >= CacheMgr.userVipCache.getMaxLvl();
	}

	public static boolean isTodayChecked() {
		return DateUtil.isToday(user.getLastCheckinTime() * 1000L);
	}

	public static List<ArmInfoClient> getArmInfos() {
		List<ArmInfoClient> armList = null;
		if (null != myLordInfo)
			armList = myLordInfo.getTroopInfo();
		else
			armList = manorInfoClient.getTroopInfo();

		if (null != armList) {
			Collections.sort(armList, new Comparator<ArmInfoClient>() {
				@Override
				public int compare(ArmInfoClient object1, ArmInfoClient object2) {
					return object1.getId() - object2.getId();
				}
			});
		}

		return armList;
	}

	public static int getArmTotalCount() {
		int cnt = 0;

		List<ArmInfoClient> armList = getArmInfos();
		if (!ListUtil.isNull(armList)) {
			for (ArmInfoClient it : armList)
				cnt += it.getCount();
		}
		return cnt;
	}

	public static List<HeroInfoClient> getHeroInfos() {
		List<HeroInfoClient> heroList = heroInfoCache.get();
		Collections.sort(heroList);

		int position = -1;
		// 选一个没有穿满装备的英雄 放最前面 (选择除孔融以外的将领，实在没有就选择孔融)
		for (int i = 0; i < heroList.size(); i++) {
			if (!heroHasFullEquipment(heroList.get(i),
					PropEquipment.TYPE_WEAPON)) {
				position = i;
				if (heroList.get(i).getHeroId() == 10000/* 孔融 */) {
					continue;
				} else {
					break;
				}
			}
		}
		if (position != -1) {
			HeroInfoClient hic = heroList.get(position);
			heroList.remove(heroList.get(position));
			heroList.add(0, hic);
		}
		return heroList;
	}

	// 判断英雄是否全穿满装备
	public static boolean allHeroHasFullEquipment() {
		List<HeroInfoClient> heroInfoClients = getHeroInfos();
		if (ListUtil.isNull(heroInfoClients))
			return true/* 英雄为NULL 当作穿满 */;
		for (HeroInfoClient heroInfoClient : heroInfoClients) {
			if (!heroHasFullEquipment(heroInfoClient, PropEquipment.TYPE_WEAPON)) {
				return false;
			}
		}
		return true;
	}

	// 英雄是否穿指定的装备类型
	public static boolean heroHasFullEquipment(HeroInfoClient hic,
			int equipmentType) {
		if (hic == null)
			return true/* 英雄为NULL 当作穿满装备 */;
		List<EquipmentSlotInfoClient> esic = hic.getEquipmentSlotInfos();
		for (EquipmentSlotInfoClient equipmentSlotInfoClient : esic) {
			if (equipmentSlotInfoClient.hasEquipment()
					&& equipmentSlotInfoClient.getType() == PropEquipment.TYPE_WEAPON) {
				return true;
			}
		}
		return false;

	}

	public static List<HeroInfoClient> getCampaignHeros() {
		List<HeroInfoClient> list = new ArrayList<HeroInfoClient>();
		long heroId1 = Account.readLog.DEFAULT_CAMPAIGN_HERO_ID1;
		long heroId2 = Account.readLog.DEFAULT_CAMPAIGN_HERO_ID2;
		long heroId3 = Account.readLog.DEFAULT_CAMPAIGN_HERO_ID3;
		// 未选中 武将不存在 武将不在该领地 都需要设置默认选中
		HeroInfoClient hic1 = getDefaultHero(heroId1);
		list.add(hic1);
		Account.readLog.DEFAULT_CAMPAIGN_HERO_ID1 = hic1.getId();
		HeroInfoClient hic2 = getDefaultHero(heroId2);
		list.add(hic2);
		Account.readLog.DEFAULT_CAMPAIGN_HERO_ID2 = hic2.getId();
		HeroInfoClient hic3 = getDefaultHero(heroId3);
		list.add(hic3);
		Account.readLog.DEFAULT_CAMPAIGN_HERO_ID3 = hic3.getId();
		return list;
	}

	public static List<HeroInfoClient> getBloodHeros() {
		List<HeroInfoClient> list = new ArrayList<HeroInfoClient>();
		long heroId1 = Account.readLog.DEFAULT_BLOOD_HERO_ID1;
		long heroId2 = Account.readLog.DEFAULT_BLOOD_HERO_ID2;
		long heroId3 = Account.readLog.DEFAULT_BLOOD_HERO_ID3;
		// 未选中 武将不存在 武将不在该领地 都需要设置默认选中
		HeroInfoClient hic1 = getDefaultHero(heroId1);
		list.add(hic1);
		Account.readLog.DEFAULT_BLOOD_HERO_ID1 = hic1.getId();
		HeroInfoClient hic2 = getDefaultHero(heroId2);
		list.add(hic2);
		Account.readLog.DEFAULT_BLOOD_HERO_ID2 = hic2.getId();
		HeroInfoClient hic3 = getDefaultHero(heroId3);
		list.add(hic3);
		Account.readLog.DEFAULT_BLOOD_HERO_ID3 = hic3.getId();
		return list;
	}

	private static HeroInfoClient getDefaultHero(long heroId) {
		if (heroId > 0 && Account.heroInfoCache.get(heroId) != null) {
			return Account.heroInfoCache.get(heroId).copy();
		} else {
			return HeroInfoClient.newInstance();
		}
	}

	public static HeroInfoClient getDefaultHeroS(long pos) {
		HeroInfoClient heroInfoClient = null;
		long heroId = 0;// Account.readLog.DEFAULT_HERO_ID;
		// 未选中 武将不存在 武将不在改领地 都需要设置默认选中
		if (heroId == 0 || Account.heroInfoCache.get(heroId) == null
				|| Account.heroInfoCache.get(heroId).getFiefid() != pos) {
			List<HeroInfoClient> ls = Account.heroInfoCache.getHeroInMainCity();
			if (ls.size() > 0)
				heroInfoClient = ls.get(0).copy();
			else
				heroInfoClient = HeroInfoClient.newInstance();
		} else {
			heroInfoClient = Account.heroInfoCache.get(heroId).copy();
		}

		// Account.readLog.DEFAULT_HERO_ID = heroInfoClient.getId();
		return heroInfoClient;
	}

	public static boolean isCampaignHellEvilPassed() {
		ActInfoClient act = actInfoCache
				.getAct(ActInfoClient.GUILD_FIRST_ACT_ID);
		CampaignInfoClient campaign = act
				.getCampaign(CampaignInfoClient.CAMPAIGN_HELL_EVIL);
		return campaign.isComplete();
	}

	// 副本1_4是否完成
	public static boolean isCampain1_4Passed() {
		ActInfoClient act = actInfoCache
				.getAct(ActInfoClient.GUILD_FIRST_ACT_ID);
		CampaignInfoClient campaign = act
				.getCampaign(CampaignInfoClient.CAMPAIGN_1_4);
		return null != campaign && campaign.isComplete();
	}

	// 得到第四关的副本数据 引导用
	public static CampaignInfoClient getGuideCampaignInfoClient() {
		ActInfoClient act = actInfoCache
				.getAct(ActInfoClient.GUILD_FIRST_ACT_ID);
		CampaignInfoClient campaign = act
				.getCampaign(CampaignInfoClient.CAMPAIGN_1_4);
		return campaign;
	}

	// 是否进入过副本第四关战役
	public static boolean isEnterCampaingn4() {
		return BaseStep.isStep501Enter();
	}

	public static boolean isOwnAltar(int userId) {
		return null != Account.guildAltar
				&& guildCache.getRichInfoInCache().getGic().getLeader() == guildAltar
						.getUserId();
	}

	public static boolean isMeGuildLeader() {
		return null != Account.guildAltar
				&& Account.user.getId() == guildAltar.getUserId();
	}

	public static boolean isAltarInBattle() {
		return null != Account.guildAltar
				&& BattleStatus.isInBattle(TroopUtil.getCurBattleState(
						Account.guildAltar.getBattleState(),
						Account.guildAltar.getBattleTime()));
	}

	// 巅峰战场周期内剩余次数
	public static int getArenaLeftCount() {
		int count = 0;
		UserVip vipCfg = getCurVip();
		if (null != vipCfg && null != myLordInfo) {
			count = vipCfg.getFreeArenaCount() - myLordInfo.getArenaCount();
		}
		if (count < 0)
			count = 0;
		return count;
	}

	// 是否配置好部队
	public static boolean isTroopReady() {
		boolean ready = false;
		if (null != myLordInfo) {
			ready = !myLordInfo.getArenaHeroInfos().isEmpty();
		}
		return ready;
	}

	public static UserTroopEffectInfo getUserTroopEffectInfo() {
		UserTroopEffectInfo info = null;
		if (null != armEnhanceCache) {
			info = armEnhanceCache.getUserTroopEffectInfo();
		}
		if (info == null)
			info = new UserTroopEffectInfo();
		return info;
	}

	public static UserTroopEffectInfo getUserTroopEffectInfo(int armId) {
		UserTroopEffectInfo info = null;
		if (null != armEnhanceCache) {
			info = armEnhanceCache.getUserTroopEffectInfo(armId);
			// 合入血量加成
			if (null != manorInfoClient) {
				int value = manorInfoClient.getArmHpAdd();
				if (null != info) {
					List<TroopEffectInfo> troopEffectInfos = info
							.getInfosList();
					try {
						TroopProp troopProp = (TroopProp) CacheMgr.troopPropCache
								.get(armId);
						TroopEffectInfo troopEffectInfo = new TroopEffectInfo();
						troopEffectInfo.setArmid(armId)
								.setAttr(BattlePropDefine.PROP_LIFE)
								.setValue(value * troopProp.getHpModulus());
						troopEffectInfos.add(troopEffectInfo);
					} catch (GameException e) {

					}

					info.setInfosList(troopEffectInfos);
				}
			}
		}
		if (info == null)
			info = new UserTroopEffectInfo();
		return info;
	}

	public static void setFlag(int trainingId) {
		new EndGuider(trainingId).start();
	}

	public static boolean isMy(BriefUserInfoClient briefUserInfoClient) {
		if (briefUserInfoClient == null)
			return false;
		return Account.user.getId() == briefUserInfoClient.getId();
	}

	// 获取各榜单信息，并且判断是否有奖励 此方法必须线程中执行
	public static void obtainHonorReward() {
		int guildId = 0;
		if (Account.user.hasGuild()) {
			guildId = Account.user.getGuildId();
		}
		ResultPage rp = new ResultPage();
		rp.setCurIndex(0);
		rp.setPageSize((short) 1);
		for (int i = 0; i < UserAccountClient.hasHonorRankReward.length; i++) {
			try {
				MsgRspHonorRankInfo rsp = GameBiz.getInstance()
						.getHonorRankInfo(HonorRankType.valueOf(i + 1), rp,
								guildId);
				// 不是族长不加进去
				if ((HonorRankType.valueOf(i + 1) == HonorRankType.HONOR_RANK_GUILD_COST || HonorRankType
						.valueOf(i + 1) == HonorRankType.HONOR_RANK_GUILD)) {
					BriefGuildInfoClient bGuildInfoClient = null;
					if (rsp.getSelfInfo().getGuildid() > 0)
						bGuildInfoClient = CacheMgr.bgicCache.get(rsp
								.getSelfInfo().getGuildid());
					if (null != bGuildInfoClient
							&& Account.user.getId() == bGuildInfoClient
									.getLeader()) {
						if (rsp.hasSelfInfo()
								&& rsp.hasSelfPos()
								&& !CacheMgr.honorRankCache.isGtMaxRewardPos(
										i + 1, rsp.getSelfPos() + 1)) {
							Config.getController()
									.getFileAccess()
									.saveHonorRankReward(i + 1,
											rsp.getSelfInfo().getReceiveAward());
						}
					}
				} else {
					if (rsp.hasSelfInfo()
							&& rsp.hasSelfPos()
							&& !CacheMgr.honorRankCache.isGtMaxRewardPos(i + 1,
									rsp.getSelfPos() + 1)) {
						Config.getController()
								.getFileAccess()
								.saveHonorRankReward(i + 1,
										rsp.getSelfInfo().getReceiveAward());
					}
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		Account.honorRankRewardInit();
	}

	// 最好在线程里面调用，此方法有IO操作
	public static void honorRankRewardInit() {
		UserAccountClient.hasHonorRankReward[0] = Config.getController()
				.getFileAccess()
				.getHasHonorRankReward(HonorRankType.HONOR_RANK_MARS.number);
		UserAccountClient.hasHonorRankReward[1] = Config.getController()
				.getFileAccess()
				.getHasHonorRankReward(HonorRankType.HONOR_RANK_GUILD.number);
		UserAccountClient.hasHonorRankReward[2] = Config
				.getController()
				.getFileAccess()
				.getHasHonorRankReward(
						HonorRankType.HONOR_RANK_USER_COST.number);
		UserAccountClient.hasHonorRankReward[3] = Config
				.getController()
				.getFileAccess()
				.getHasHonorRankReward(
						HonorRankType.HONOR_RANK_GUILD_COST.number);
	}

	public static boolean hasHonorRankReward() {
		for (Boolean hasReward : UserAccountClient.hasHonorRankReward) {
			if (hasReward)
				return true;
		}
		return false;
	}
}
