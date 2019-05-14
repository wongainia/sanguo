package com.vikings.sanguo.biz;

import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.*;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.MachinePlayStatInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.model.OtherUserBattleIdInfoClient;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.model.UserStatDataClient;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.network.SocketShortConnector;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.BindingType;
import com.vikings.sanguo.protos.ConditionNum;
import com.vikings.sanguo.protos.ConditionStr;
import com.vikings.sanguo.protos.GuildSearchCond;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.protos.MsgRspHeroRankInfo;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.protos.MsgRspUserRankInfo;
import com.vikings.sanguo.protos.ROLE_STATUS;
import com.vikings.sanguo.protos.StaticGuildDataType;
import com.vikings.sanguo.protos.StaticUserDataType;
import com.vikings.sanguo.protos.UserRankType;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 游戏逻辑处理。不包含UI
 * 
 * @author Brad.Chen
 * 
 */
// @SuppressWarnings("unchecked")
public class GameBiz {

	private static final GameBiz instance = new GameBiz();

	private SocketShortConnector socketConn = SocketShortConnector
			.getInstance();

	private GameBiz() {
	}

	public static void reset() {
		if (null != instance)
			instance.socketConn.reset();
	}

	synchronized public static GameBiz getInstance() throws GameException {
		if (instance.socketConn.isAddrInvalid()) {
			instance.queryAddr();
		}
		return instance;
	}

	synchronized public static void switchServer() throws GameException {
		instance.queryAddr();
	}

	public void testSetAddr(InetAddress gsAddr) {
		socketConn.setAddr(gsAddr);
	}

	private void queryAddr() throws GameException {
		// 询问服务器地址
		socketConn.reset();
		QueryServerResp addrResp = new QueryServerResp();
		try {
			socketConn.send(new QueryServerReq(), addrResp);
			socketConn.setAddr(addrResp.getAddr(), addrResp.getPort());
		} catch (GameException e) {
			Throwable t = e.getCause();
			if (t != null && (t instanceof SocketTimeoutException)) {
				socketConn.tryConn();
				socketConn.send(new QueryServerReq(), addrResp);
				socketConn.setPort(addrResp.getPort());
			} else {
				throw e;
			}
		}
	}

	/**
	 * 渠道激活
	 * 
	 * @throws GameException
	 */
	private void active() throws GameException {
		ActiveReq req = new ActiveReq();
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	public UserAccountClient login(UserAccountClient user) throws GameException {
		UserAccountClient u = null;
		try {
			u = userLogin(user);
		} catch (GameException e) {
			if (e.getResult() == ResultCode.RESULT_FAILED_ACCOUNT_INVALID
					|| e.getResult() == ResultCode.RESULT_FAILED_ACCOUNT_VERIFY)
				Account.user = null;
			Config.getController().getFileAccess()
					.deleteUser(Config.serverId, user.getId());
			throw e;
		}
		gameEnter(Config.clientCode, Config.getClientVer());
		return u;

	}

	private UserAccountClient userLogin(UserAccountClient user)
			throws GameException {
		LoginReq req = new LoginReq(user);
		LoginResp resp = new LoginResp(user);
		socketConn.send(req, resp);
		return resp.getUser();
	}

	public void gameEnter(int clientType, int clientVer) throws GameException {
		GameEnterReq req = new GameEnterReq(clientType, clientVer);
		GameEnterResp resp = new GameEnterResp();
		socketConn.send(req, resp);
		Config.timeOffset = resp.getTime() * 1000L - System.currentTimeMillis();
		WorldLevelInfoClient.worldLevel = resp.getWorldLevel();
		WorldLevelInfoClient.worldLevelProcess = resp.getWorldLevelProcess();
		WorldLevelInfoClient.worldLevelProcessTotal = resp
				.getWorldLevelProcessTotal();
	}

	// 重新登录
	public void reLogin() throws GameException {
		UserAccountClient u = Config.getAccountClient();
		if (u == null || u.getId() != Account.user.getId())
			throw new GameException("帐号密码错误");
		Account.user.setPsw(u.getPsw());
		login(Account.user);
	}

	public UserAccountClient register(String nick, int sex, int province,
			String partnerId) throws GameException {
		RegisterReq req = new RegisterReq(nick, sex, province, partnerId);
		RegisterResp resp = new RegisterResp();
		socketConn.send(req, resp);
		return resp.getUser();
	}

	/**
	 * 通过imsi找回账号
	 * 
	 * @param imsi
	 * @return
	 * @throws GameException
	 */
	public UserAccountClient accountQuery(String imsi) throws GameException {
		AccountQueryReq imsiReq = new AccountQueryReq(imsi);
		AccountQueryResp imsiResp = new AccountQueryResp();
		socketConn.send(imsiReq, imsiResp);
		UserAccountClient user = imsiResp.getUser();
		return user;
	}

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	public UserAccountClient login(String imsi) throws GameException {
		UserAccountClient user = Config.getAccountClient();
		// 没有找到本地帐户，查询imsi
		if (user.getId() == 0 && StringUtil.isNull(user.getPsw())) {
			user = accountQuery(imsi);
			Config.getController()
					.getFileAccess()
					.saveUser(Config.serverId, user.getId(), user.getLevel(),
							user.getNick(), user.getPsw());
		}
		String password = user.getPsw();

		// 如果是老用户 登录
		if (user.getId() != 0) {
			Account.user = user;
			user = login(user);
		} else {
			// 记录激活操作
			active();
		}
		if (!StringUtil.isNull(password))
			user.setPsw(password);
		return user;
	}

	public void logout() {
		if (Account.user == null || Account.user.getId() == 0)
			return;
		LogoutReq req = new LogoutReq();
		socketConn.post(req);
	}

	/**
	 * 用户回复体力
	 * 
	 * @param currency
	 * @return
	 * @throws GameException
	 */
	public UserStaminaResetResp userStaminaReset(int currency)
			throws GameException {
		UserStaminaResetReq req = new UserStaminaResetReq(currency);
		UserStaminaResetResp resp = new UserStaminaResetResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param u
	 * @return
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient playerUpdate(UserAccountClient u)
			throws GameException {
		PlayerUpdateReq req = new PlayerUpdateReq(u);
		PlayerUpdateResp resp = new PlayerUpdateResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	/**
	 * 心跳通讯
	 * 
	 * @param userId
	 * @param update
	 * @return
	 * @throws GameException
	 */
	public HeartBeatResp heartbeat(List<Integer> ids, int country)
			throws GameException {
		HeartBeatReq req = new HeartBeatReq(ids, country);
		HeartBeatResp resp = new HeartBeatResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 玩家搜索
	 * 
	 * @param resultPage
	 * @param nums
	 * @param strs
	 * @throws GameException
	 */
	public List<BriefUserInfoClient> userSerarch(ResultPage resultPage,
			List<ConditionNum> nums, List<ConditionStr> strs)
			throws GameException {
		UserSearchReq req = new UserSearchReq(resultPage, nums, strs);
		UserSearchResp resp = new UserSearchResp();
		socketConn.send(req, resp);
		return resp.getUsers();
	}

	/**
	 * 添加好友
	 * 
	 * @param u
	 * @return
	 * @throws GameException
	 */
	public void addFriend(int targetid) throws GameException {
		List<Integer> ls = new ArrayList<Integer>(1);
		ls.add(targetid);
		ls = addFriends(ls);
		if (ls != null && ls.size() > 0)
			throw new GameException("添加失败，请重试");
	}

	/**
	 * 批量添加好友
	 * 
	 * @param ids
	 *            每次最多10个
	 * @return 添加失败的uiserId
	 * @throws GameException
	 */
	public List<Integer> addFriends(List<Integer> ids) throws GameException {
		FriendAddReq req = new FriendAddReq(ids);
		FriendAddResp resp = new FriendAddResp();
		socketConn.send(req, resp);
		return resp.getFailUserIds();
	}

	/**
	 * 删除好友
	 * 
	 * @param u
	 * @return
	 * @throws GameException
	 */
	public void deleteFriend(List<Integer> targetids) throws GameException {
		FriendDelReq req = new FriendDelReq(targetids);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 卖物品
	 * 
	 * @param u
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient sellItem(long bagId, int amount)
			throws GameException {
		ItemSellReq req = new ItemSellReq(bagId, amount);
		ItemSellResp resp = new ItemSellResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 查询他人基本信息
	 * 
	 * @param ids
	 *            单次最多支持10个人
	 * @return
	 * @throws GameException
	 */
	public List<BriefUserInfoClient> queryBriefUserList(List<Integer> ids)
			throws GameException {
		BriefUserInfoQueryReq req = new BriefUserInfoQueryReq(ids);
		BriefUserInfoQueryResp resp = new BriefUserInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getInfos();
	}

	/**
	 * 使用物品 目标人(protobuf)
	 * 
	 * @param targetId
	 * @param bagId
	 * @param itemId
	 * @param scheme
	 *            物品使用分方案 几种用法。。。 用于将魂 武将兑换， 高级魂魄兑换
	 * @param count
	 *            数量[default = 1]
	 * 
	 * @return
	 * @throws GameException
	 */
	public ItemUseResp itemUse(int targetId, long bagId, int itemId,
			int scheme, int count) throws GameException {
		ItemUseReq req = new ItemUseReq(targetId, bagId, itemId, scheme, count);
		ItemUseResp resp = new ItemUseResp();
		socketConn.send(req, resp);
		return resp;
	}

	public ItemUseResp itemUse(int targetId, long bagId, int itemId)
			throws GameException {
		return itemUse(targetId, bagId, itemId, 0, 1);
	}

	/**
	 * 买物品
	 * 
	 * @param u
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient itemBuy(int itemId, int amount)
			throws GameException {
		ItemBuyReq req = new ItemBuyReq(itemId, amount, 0);
		ItemBuyResp resp = new ItemBuyResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	public ReturnInfoClient itemBuy(int itemId, int amount, int currency)
			throws GameException {
		ItemBuyReq req = new ItemBuyReq(itemId, amount, currency);
		ItemBuyResp resp = new ItemBuyResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 数据同步新接口
	 * 
	 * @param synFlag
	 *            0 - 同步完整数据 1 - 同步变化数据
	 * @param dataType
	 *            数据类型标志(如:DATA_TYPE_ROLEINFO | DATA_TYPE_BAG, DATA_TYPE_ALL等)
	 * @return
	 * @throws GameException
	 */
	public SyncDataSet userDataSyn2(int synFlag, int dataType)
			throws GameException {
		UserDataSyn2Req req = new UserDataSyn2Req(synFlag, dataType);
		UserDataSyn2Resp resp = new UserDataSyn2Resp();
		socketConn.send(req, resp);
		CacheMgr.fillSyncDataSet(resp.getSyncDataSet());
		return resp.getSyncDataSet();
	}

	/**
	 * 新手教程结束。 传入绑定分配的渠道号，发放渠道奖励
	 * 
	 * @param channel
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient trainingComplete(int trainingId)
			throws GameException {
		TrainingCompleteReq req = new TrainingCompleteReq(trainingId);
		TrainingCompleteResp resp = new TrainingCompleteResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	// /**
	// * ping
	// *
	// * @throws GameException
	// */
	// public void ping() throws GameException {
	// PingReq req = new PingReq();
	// EmptyResp resp = new EmptyResp();
	// socketConn.send(req, resp);
	// }

	/**
	 * 排行榜信息查询
	 * 
	 * @param rankType
	 *            //0:等级 1:财富 2:成就 3:魅力 4:崇拜 5:人口 6:相册被赞值
	 * @param rangeType
	 *            // 0:同城 1:全国
	 * @param province
	 * @param city
	 * @param resultPage
	 * @throws GameException
	 */
	public void queryUserRank(byte rankType, byte rangeType, byte province,
			byte city, ResultPage resultPage) throws GameException {
		// UserRankReq req = new UserRankReq(rankType, rangeType, province,
		// city,
		// resultPage.getCurIndex(), resultPage.getPageSize());
		// UserRankResp resp = new UserRankResp(resultPage);
		// socketConn.send(req, resp);
	}

	/**
	 * 好友排名
	 * 
	 * @param userId
	 * @param ids
	 * @return
	 * @throws GameException
	 */
	public List<UserStatDataClient> queryFriendRank(List<Integer> ids)
			throws GameException {
		if (ids == null || ids.size() == 0)
			return new ArrayList<UserStatDataClient>();
		FriendRankReq req = new FriendRankReq(ids);
		FriendRankResp resp = new FriendRankResp();
		socketConn.send(req, resp);
		return resp.getRiList();
	}

	/**
	 * 根据手机或者邮箱找回 获取验证码
	 * 
	 * @param flag
	 *            (1手机对应Constants.RESTORE_OP_PHONE_INPUT，2邮箱对应Constant.
	 *            RESTORE_OP_MAIL_INPUT)
	 * @param value
	 * @return
	 * @throws GameException
	 */
	public AccountRestoreResp accountRestore(int flag, String value)
			throws GameException {
		AccountRestoreReq req = new AccountRestoreReq(flag, value);
		AccountRestoreResp resp = new AccountRestoreResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 由userid 和验证码 找回帐号
	 * 
	 * @param userId
	 * @param checkCode
	 * @return
	 * @throws GameException
	 */
	public AccountRestore2Resp accountRestore2(int flag, String value, int code)
			throws GameException {
		AccountRestore2Req req = new AccountRestore2Req(flag, value, code);
		AccountRestore2Resp resp = new AccountRestore2Resp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 账号找回第三步，服务器需要，客户端不做任何逻辑
	 * 
	 * @param userId
	 * @param psw
	 * @param sim
	 * @throws GameException
	 */
	public AccountRestore3Resp accountRestore3(int userId, String psw,
			String sim) throws GameException {
		AccountRestore3Req req = new AccountRestore3Req(userId, psw, sim);
		AccountRestore3Resp resp = new AccountRestore3Resp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 任务完成，领取奖励
	 * 
	 * @param questId
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient questFinish(int questId) throws GameException {
		QuestFinishReq req = new QuestFinishReq(questId);
		QuestFinishResp resp = new QuestFinishResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 同步任务信息
	 * 
	 * @param userId
	 * @throws GameException
	 */
	public SyncDataSet refreshQuest() throws GameException {
		SyncDataSet data = userDataSyn2(Constants.SYNC_TYPE_DIFF,
				Constants.DATA_TYPE_QUEST);
		// CacheMgr.fillSyncDataSet(data);
		Account.updateQuest(data);
		return data;
	}

	/**
	 * 同步战争id
	 * 
	 * @return
	 * @throws GameException
	 */
	public SyncDataSet refreshBattle() throws GameException {
		return userDataSyn2(Constants.SYNC_TYPE_DIFF,
				Constants.DATA_TYPE_BATTLE_ID);
	}

	/**
	 * 同步领主信息
	 * 
	 * @return
	 * @throws GameException
	 */
	public SyncDataSet refreshLordInfo() throws GameException {
		SyncDataSet data = userDataSyn2(Constants.SYNC_TYPE_ALL,
				Constants.DATA_TYPE_LORD);
		CacheMgr.fillSyncDataSet(data);
		Account.updateLordInfo(data);
		return data;
	}

	/**
	 * 同步将领信息
	 * 
	 * @return
	 * @throws GameException
	 */
	public SyncDataSet refreshHeroInfo() throws GameException {
		SyncDataSet data = userDataSyn2(Constants.SYNC_TYPE_DIFF,
				Constants.DATA_TYPE_HERO);
		CacheMgr.fillSyncDataSet(data);
		return data;
	}

	/**
	 * 同步装备信息
	 * 
	 * @return
	 * @throws GameException
	 */
	public SyncDataSet refreshEquipmentInfo() throws GameException {
		SyncDataSet data = userDataSyn2(Constants.SYNC_TYPE_DIFF,
				Constants.DATA_TYPE_EQUIPMENT);
		CacheMgr.fillSyncDataSet(data);
		return data;
	}

	// 城堡相关

	public ManorReceiveResp manorReceive(int buildingId) throws GameException {
		ManorReceiveReq req = new ManorReceiveReq(buildingId);
		ManorReceiveResp resp = new ManorReceiveResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 主城全收
	 * 
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient manorReceiveAll() throws GameException {
		ManorReceiveAllReq req = new ManorReceiveAllReq();
		ManorReceiveAllResp resp = new ManorReceiveAllResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	/**
	 * 主城虚弱状态解除
	 * 
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient manorWeakRemove() throws GameException {
		ManorWeakRemoveReq req = new ManorWeakRemoveReq();
		ManorWeakRemoveResp resp = new ManorWeakRemoveResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	/**
	 * 单挑和屠城
	 * 
	 * @param type
	 *            方式 1：攻城 2：突围
	 * @param target
	 *            战斗目标用户id
	 * @param fiefId
	 *            战争所在地
	 * @return
	 * @throws GameException
	 */
	public DuelAttackResp duelAttack(int type, int target, long fiefId)
			throws GameException {
		DuelAttackReq req = new DuelAttackReq(type, target, fiefId);
		DuelAttackResp resp = new DuelAttackResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 个人追杀令
	 * 
	 * @param target
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient playerWanted(int target) throws GameException {
		PlayerWantedReq req = new PlayerWantedReq(target);
		PlayerWantedResp resp = new PlayerWantedResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	/**
	 * 寻找空闲的城堡位置
	 * 
	 * @param pos
	 *            玩家当前所在位置
	 * @return 空闲的位置
	 * @throws GameException
	 */
	public ManorRecommendEmptySpaceResp manorRecommendEmptySpace(long pos)
			throws GameException {
		ManorRecommendEmptySpaceReq req = new ManorRecommendEmptySpaceReq(pos);
		ManorRecommendEmptySpaceResp resp = new ManorRecommendEmptySpaceResp();
		socketConn.send(req, resp);
		return resp;
	}

	public ManorLayDownResp manorLayDown(long zoneid) throws GameException {
		ManorLayDownReq req = new ManorLayDownReq(zoneid);
		ManorLayDownResp resp = new ManorLayDownResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 随机移动城堡
	 * 
	 * @return
	 * @throws GameException
	 */
	public ManorRandomMoveResp manorRandomMove() throws GameException {
		ManorRandomMoveReq req = new ManorRandomMoveReq();
		ManorRandomMoveResp resp = new ManorRandomMoveResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 更新城堡信息
	 * 
	 * @param name
	 *            城堡名称，如果不变传空串或null
	 * @param buildingId
	 *            城堡主建筑，如果不变传0
	 * @return
	 * @throws GameException
	 */
	public ManorInfoClient playerManorUpdate(String name) throws GameException {
		PlayerManorUpdateReq req = new PlayerManorUpdateReq(name);
		PlayerManorUpdateResp resp = new PlayerManorUpdateResp();
		socketConn.send(req, resp);
		return resp.getMic();
	}

	/**
	 * 恢复城堡人口
	 * 
	 * @return
	 * @throws GameException
	 */
	public PlayerManorPopRecoverResp playerManorPopRecover()
			throws GameException {
		PlayerManorPopRecoverReq req = new PlayerManorPopRecoverReq();
		PlayerManorPopRecoverResp resp = new PlayerManorPopRecoverResp();
		socketConn.send(req, resp);

		return resp;
	}

	// 建筑相关
	/**
	 * 购买建筑
	 * 
	 * @param pos
	 * @param itemId
	 * @param direction
	 * @param bag
	 * @param levelUpId
	 *            如果是升级，则需要升级建筑id
	 * @return
	 * @throws GameException
	 */
	public BuildingBuyResp buildingBuy(int type, int itemId)
			throws GameException {
		BuildingBuyReq req = new BuildingBuyReq(type, itemId);
		BuildingBuyResp resp = new BuildingBuyResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 添加黑名单
	 * 
	 * @param targetId
	 * @throws GameException
	 */
	public void blackListAdd(int targetId) throws GameException {
		BlackListAddReq req = new BlackListAddReq(targetId);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 删除黑名单
	 * 
	 * @param targetId
	 * @throws GameException
	 */
	public void blackListDel(int targetId) throws GameException {
		BlackListDelReq req = new BlackListDelReq(targetId);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * @param target
	 * @return true 在黑名单
	 * @throws GameException
	 */
	public boolean blackListCheck(int target) throws GameException {
		BlackListCheckReq req = new BlackListCheckReq(target);
		BlackListCheckResp resp = new BlackListCheckResp();
		socketConn.send(req, resp);
		return resp.getExit() == 1;
	}

	/**
	 * 查询静态数据
	 * 
	 * @param dataType
	 *            静态数据类型
	 * @param id
	 *            取比这个id小的数据。如果为0，则从最新数据开始取
	 * @param count
	 *            返回的数量
	 * @return
	 * @throws GameException
	 */
	public StaticUserDataQueryResp staticUserDataQuery(
			StaticUserDataType dataType, long id, int count)
			throws GameException {
		StaticUserDataQueryReq req = new StaticUserDataQueryReq(dataType, id,
				count);
		StaticUserDataQueryResp resp = new StaticUserDataQueryResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 查询别人静态数据(足迹、收获统计)
	 * 
	 * @param targerId
	 * @param dataType
	 * @param id
	 * @param count
	 * @return
	 * @throws GameException
	 */
	public PlayerStaticUserDataQueryResp playerStaticUserDataQuery(
			int targerId, StaticUserDataType dataType, long id, int count)
			throws GameException {
		PlayerStaticUserDataQueryReq req = new PlayerStaticUserDataQueryReq(
				targerId, dataType, id, count);
		PlayerStaticUserDataQueryResp resp = new PlayerStaticUserDataQueryResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 账号绑定，获取验证码
	 * 
	 * @param type
	 *            1: 手机 2：email 3: 身份证
	 * @param value
	 *            手机号码 OR email OR 身份证
	 * @throws GameException
	 */
	public void accountBinding(BindingType type, String value)
			throws GameException {
		AccountBindingReq req = new AccountBindingReq(type, value);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 
	 * @param type
	 *            1: 手机 2：email 3: 身份证
	 * @param value
	 *            手机号码 OR email OR 身份证
	 * @param code
	 *            验证码
	 * @param inviter
	 *            邀请者（可选，只生效一次）
	 * @return
	 * @throws GameException
	 */
	public AccountBinding2Resp accountBinding2(BindingType type, String value,
			int code, int inviter) throws GameException {
		AccountBinding2Req req = new AccountBinding2Req(type, value, code,
				inviter);
		AccountBinding2Resp resp = new AccountBinding2Resp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 查询其他人的富用户信息
	 * 
	 * @param tager
	 * @param flag
	 * @return
	 * @throws GameException
	 */
	public OtherUserClient queryRichOtherUserInfo(int target, long flag)
			throws GameException {
		// 一定查询richother 更新本地缓存
		QueryRichOtherUserInfoReq req = new QueryRichOtherUserInfoReq(target,
				Constants.DATA_TYPE_ACCOUNT_INFO
						| Constants.DATA_TYPE_ROLE_INFO | flag);
		QueryRichOtherUserInfoResp resp = new QueryRichOtherUserInfoResp();
		socketConn.send(req, resp);
		return resp.getOther();
	}

	public OtherUserClient otherLordInfoQuery(int target) throws GameException {
		return queryRichOtherUserInfo(target, Constants.DATA_TYPE_LORD);
	}

	// 充值
	/**
	 * 包月充值领奖
	 * 
	 * @return
	 * @throws GameException
	 */
	public ChargeMonthRewardResp chargeMonthReward() throws GameException {
		ChargeMonthRewardReq req = new ChargeMonthRewardReq();
		ChargeMonthRewardResp resp = new ChargeMonthRewardResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 战神宝箱
	 * 
	 * @param target
	 * @param type
	 *            1是道具 2是元宝
	 * 
	 * @return
	 * @throws GameException
	 */
	public CurrencyBoxOpenResp currencyBoxOpen(int target, int type)
			throws GameException {
		CurrencyBoxOpenReq req = new CurrencyBoxOpenReq(target, type);
		CurrencyBoxOpenResp resp = new CurrencyBoxOpenResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 轮盘
	 * 
	 * @param type
	 * @return
	 * @throws GameException
	 */
	public RouletteResp roulette(int type) throws GameException {
		RouletteReq req = new RouletteReq(type);
		RouletteResp resp = new RouletteResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 轮盘献祭
	 * 
	 * @param list
	 * @return
	 * @throws GameException
	 */
	public RouletteSacrificeResp rouletteSacrifice(List<KeyValue> list)
			throws GameException {
		RouletteSacrificeReq req = new RouletteSacrificeReq(list);
		RouletteSacrificeResp resp = new RouletteSacrificeResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 元宝老虎机
	 * 
	 * @return
	 * @throws GameException
	 */
	public CurrencyMachinePlayResp currencyMachinePlay() throws GameException {
		CurrencyMachinePlayReq req = new CurrencyMachinePlayReq();
		CurrencyMachinePlayResp resp = new CurrencyMachinePlayResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 幸运水果机
	 * 
	 * @param type
	 * @return
	 * @throws GameException
	 */
	public MachinePlayResp machinePlay(MachinePlayType type)
			throws GameException {
		MachinePlayReq req = new MachinePlayReq(type);
		MachinePlayResp resp = new MachinePlayResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 幸运水果机排行
	 * 
	 * @return
	 * @throws GameException
	 */
	public List<MachinePlayStatInfoClient> machinePlayStatData()
			throws GameException { // List<MachinePlayStatInfo>
		MachinePlayStatDataReq req = new MachinePlayStatDataReq();
		MachinePlayStatDataResp resp = new MachinePlayStatDataResp();
		socketConn.send(req, resp);
		return MachinePlayStatInfoClient.convert2List(resp.getGambleStatInfo());
	}

	/**
	 * 用户推荐
	 * 
	 * @return
	 * @throws GameException
	 */
	public List<Integer> recommendUser() throws GameException {
		RecommendUserReq req = new RecommendUserReq();
		RecommendUserResp resp = new RecommendUserResp();
		socketConn.send(req, resp);
		return resp.getUserIds();
	}

	// 土地相关
	/**
	 * Brief领地信息查询-byid
	 * 
	 * @param fiefids
	 *            每次最大支持25个
	 * @return
	 * @throws GameException
	 */
	public List<BriefFiefInfoClient> briefFiefInfoQuery(List<Long> fiefids)
			throws GameException {
		List<BriefFiefInfoClient> rs = new ArrayList<BriefFiefInfoClient>();
		for (int i = 0; i < fiefids.size(); i += com.vikings.sanguo.Constants.BRIEF_FIEF_INFO_QUERY_COUNT) {
			List<Long> ls = fiefids
					.subList(
							i,
							((i
									+ com.vikings.sanguo.Constants.BRIEF_FIEF_INFO_QUERY_COUNT > fiefids
									.size()) ? fiefids.size()
									: (i + com.vikings.sanguo.Constants.BRIEF_FIEF_INFO_QUERY_COUNT)));
			BriefFiefInfoQueryReq req = new BriefFiefInfoQueryReq(ls);
			BriefFiefInfoQueryResp resp = new BriefFiefInfoQueryResp();
			socketConn.send(req, resp);
			rs.addAll(resp.getInfos());
		}
		CacheMgr.fillBriefFiefInfoClients(rs);
		if (Config.getController().getBattleMap() != null) {
			Config.getController().getBattleMap().update(rs);
		}
		return rs;
	}

	/**
	 * 领地other信息查询-by other
	 * 
	 * @param fiefid
	 * @return
	 * @throws GameException
	 */
	public OtherFiefInfoClient otherFiefInfoQuery(long fiefid)
			throws GameException {
		OtherFiefInfoQueryReq req = new OtherFiefInfoQueryReq(fiefid);
		OtherFiefInfoQueryResp resp = new OtherFiefInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getInfo();
	}

	/**
	 * 领主领地id查询
	 * 
	 * @param target
	 * @return
	 * @throws GameException
	 */
	public List<Long> lordFiefIdQuery(int target) throws GameException {
		LordFiefIdQueryReq req = new LordFiefIdQueryReq(target);
		LordFiefIdQueryResp resp = new LordFiefIdQueryResp();
		socketConn.send(req, resp);
		return resp.getFiefIds();
	}

	/**
	 * 庄园招兵
	 * 
	 * @param buildingId
	 *            对应建筑id
	 * @param propid
	 *            兵种id
	 * @param type
	 *            征兵方式：1：普通征兵 2：元宝征兵
	 * @param count
	 *            征兵数量
	 * @return
	 * @throws GameException
	 */
	public ManorDraftResp manorDraft(int buildingId, int propid, int type,
			int count) throws GameException {
		ManorDraftReq req = new ManorDraftReq(buildingId, propid, type, count);
		ManorDraftResp resp = new ManorDraftResp();
		socketConn.send(req, resp);

		return resp;
	}

	// /**
	// * 领主修改名字
	// *
	// * @param title
	// * @return
	// * @throws GameException
	// */
	// public ReturnInfoClient lordTitle(String title) throws GameException {
	// LordTitleReq req = new LordTitleReq(title);
	// LordTitleResp resp = new LordTitleResp();
	// socketConn.send(req, resp);
	//
	// return resp.getRi();
	// }

	/**
	 * 领地调兵
	 * 
	 * @param src
	 *            源fiefid
	 * @param dst
	 *            目标fiefid
	 * @param type
	 *            方式（1：正常调兵 2：元宝调兵？）
	 * @param troops
	 *            部队信息<armId, count>
	 * @param heros
	 *            将领信息
	 * @return
	 * @throws GameException
	 */
	public FiefMoveTroopResp fiefMoveTroop(long src, int type,
			List<ArmInfoClient> troops, List<HeroIdInfoClient> heros)
			throws GameException {
		FiefMoveTroopReq req = new FiefMoveTroopReq(src, type, troops, heros);
		FiefMoveTroopResp resp = new FiefMoveTroopResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 主城调兵
	 * 
	 * @param dst
	 * @param type
	 * @param troops
	 * @param hero
	 * @return
	 * @throws GameException
	 */
	public ManorMoveTroopResp manorMoveTroop(long dst, int type,
			List<ArmInfoClient> troops, List<HeroIdInfoClient> heros)
			throws GameException {
		ManorMoveTroopReq req = new ManorMoveTroopReq(dst, troops, type, heros);
		ManorMoveTroopResp resp = new ManorMoveTroopResp();
		socketConn.send(req, resp);

		return resp;
	}

	// 战争相关
	/**
	 * //战争相关 //起兵
	 * 
	 * @param dstFiefId
	 *            //战争目的领地
	 * @param type
	 *            //方式 1：正常 2：元宝远征？
	 * @param targetUserId
	 *            //对方userid
	 * @param battleType
	 *            //类型 1：占领 2：掠夺 3：副本 4：单挑 5：屠城
	 * @param troops
	 *            //部队信息
	 * @param heros
	 *            //将领信息
	 * @param cost
	 *            //出征消耗
	 * @return
	 * @throws GameException
	 */
	public BattleRiseTroopResp battleRiseTroop(long dstFiefId, int type,
			int targetUserId, int battleType, List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heros, int cost) throws GameException {
		BattleRiseTroopReq req = new BattleRiseTroopReq(dstFiefId, type,
				targetUserId, battleType, troops, heros, cost);
		BattleRiseTroopResp resp = new BattleRiseTroopResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 副本开战
	 * 
	 * @param actId
	 * @param campaignId
	 * @param heros
	 *            英雄
	 * @param help
	 *            使用系统英雄
	 * @return
	 * @throws GameException
	 */
	public DungeonAttackResp dungeonAttack(int actId, int campaignId,
			List<HeroIdInfoClient> heros, boolean help) throws GameException {
		DungeonAttackReq req = new DungeonAttackReq(actId, campaignId, heros,
				help);
		DungeonAttackResp resp = new DungeonAttackResp();
		socketConn.send(req, resp);
		Account.actInfoCache.updateData(actId, resp.getCi());
		return resp;
	}

	/**
	 * 圣城打怪
	 * 
	 * @param fiefid
	 *            圣城所在领地id
	 * @param cost
	 *            恶魔之门开启消耗
	 * @return
	 * @throws GameException
	 */
	public FiefFightNpcResp fiefFightNpc(long fiefid, int cost)
			throws GameException {
		FiefFightNpcReq req = new FiefFightNpcReq(fiefid, cost);
		FiefFightNpcResp resp = new FiefFightNpcResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 领地增援
	 * 
	 * @return
	 * @throws GameException
	 */
	public BattleReinforResp battleReinfor(int targetId, long dst,
			int holyCost, int type, int targetType, List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heros) throws GameException {
		BattleReinforReq req = new BattleReinforReq(targetId, dst, holyCost,
				type, targetType, troops, heros);
		BattleReinforResp resp = new BattleReinforResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 购买神兵 - 兵力确认
	 * 
	 * @param battleid
	 * @return
	 * @throws GameException
	 */
	public int confirmBuyBattleUnit(long battleid) throws GameException {
		ConfirmBuyBattleUnitReq req = new ConfirmBuyBattleUnitReq(battleid);
		ConfirmBuyBattleUnitResp resp = new ConfirmBuyBattleUnitResp();
		socketConn.send(req, resp);
		return resp.getPokerUnit();
	}

	/**
	 * 购买神兵
	 * 
	 * @param battleId
	 *            战场id
	 * @param index
	 *            第几张牌
	 * @return
	 * @throws GameException
	 */
	public BuyBattleUnitResp buyBattleUnit(long battleId, int index)
			throws GameException {
		BuyBattleUnitReq req = new BuyBattleUnitReq(battleId, index);
		BuyBattleUnitResp resp = new BuyBattleUnitResp();
		socketConn.send(req, resp);

		return resp;

	}

	public ReinforceBuyUnitResp reinforceBuyUnit(long battleId, int cost,
			int type) throws GameException {
		ReinforceBuyUnitReq req = new ReinforceBuyUnitReq(battleId, cost, type);
		ReinforceBuyUnitResp resp = new ReinforceBuyUnitResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 占卜
	 * 
	 * @param fiefid
	 * @param battleId
	 * @param type
	 *            占卜类型(1 普通 进攻方占卜也填普通 2 出城应战 3 强攻)
	 * @return
	 * @throws GameException
	 */
	public BattleDivineResp battleDivine(long fiefid, int type)
			throws GameException {
		BattleDivineReq req = new BattleDivineReq(fiefid, type);
		BattleDivineResp resp = new BattleDivineResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 进攻/突围
	 * 
	 * @param type
	 *            方式 1：攻城 2：突围 3：强攻
	 * @param target
	 *            战斗目标用户id
	 * @param fiefid
	 *            战争所在地
	 * @param battleId
	 * @return
	 * @throws GameException
	 */
	public CommonAttackResp battleAttack(int type, int target, long fiefid)
			throws GameException {
		CommonAttackReq req = new CommonAttackReq(type, target, fiefid);
		CommonAttackResp resp = new CommonAttackResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 掠夺开战
	 * 
	 * @param type
	 * @param target
	 * @param fiefid
	 * @return
	 * @throws GameException
	 */
	public BattlePlunderResp plunderAttack(int type, int target, long fiefid)
			throws GameException {
		BattlePlunderReq req = new BattlePlunderReq(type, target, fiefid);
		BattlePlunderResp resp = new BattlePlunderResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * brief战争信息查询 注：自己的birefBattleInfo应调用BriefBattleInfoCache取
	 * 
	 * @param battleIds
	 * @return
	 * @throws GameException
	 */
	public List<BriefBattleInfoClient> briefBattleInfoQuery(List<Long> battleIds)
			throws GameException {
		List<BriefBattleInfoClient> rs = new ArrayList<BriefBattleInfoClient>();
		for (int i = 0; i < battleIds.size(); i += com.vikings.sanguo.Constants.BRIEF_BATTLE_INFO_QUERY_COUNT) {
			List<Long> ls = battleIds
					.subList(
							i,
							((i
									+ com.vikings.sanguo.Constants.BRIEF_BATTLE_INFO_QUERY_COUNT > battleIds
									.size()) ? battleIds.size()
									: (i + com.vikings.sanguo.Constants.BRIEF_BATTLE_INFO_QUERY_COUNT)));
			BriefBattleInfoQueryReq req = new BriefBattleInfoQueryReq(ls);
			BriefBattleInfoQueryResp resp = new BriefBattleInfoQueryResp();
			socketConn.send(req, resp);
			rs.addAll(resp.getInfos());
		}
		CacheMgr.fillBriefBattleInfoClient(rs);
		return rs;
	}

	/**
	 * 我的战役日志 注：所有的日志应该从BattleLogInfoCache中取，而不应该直接调用该接口
	 * 
	 * @param battleLogId
	 * @return
	 * @throws GameException
	 */
	public BattleLogInfo battleLogInfoQuery(long battleLogId)
			throws GameException {
		BattleLogInfoQueryReq req = new BattleLogInfoQueryReq(battleLogId);
		BattleLogInfoQueryResp resp = new BattleLogInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getBli();
	}

	/**
	 * 查询他人参与的战斗id信息
	 * 
	 * @param userIds
	 *            10个最多
	 * @param flag
	 *            (common.h文件中的enum EBattleFlag) 对应battleid的flag,
	 *            A&B只要为真就返回，0表示全取
	 * @return
	 * @throws GameException
	 */
	public List<OtherUserBattleIdInfoClient> userBattleInfoQuery(
			List<Integer> userIds) throws GameException {
		UserBattleInfoQueryReq req = new UserBattleInfoQueryReq(userIds,
				Constants.E_BATTLE_FLAG_GUILD_ASSIST);
		UserBattleInfoQueryResp resp = new UserBattleInfoQueryResp();
		socketConn.send(req, resp);
		CacheMgr.fillOtherUserBattleIdInfoClents(resp.getInfos());
		return resp.getInfos();
	}

	/**
	 * 请求最新战报
	 * 
	 * @return
	 * @throws GameException
	 */
	public List<BattleHotInfoClient> battleHotInfo() throws GameException {
		BattleHotInfoReq req = new BattleHotInfoReq();
		BattleHotInfoResp resp = new BattleHotInfoResp();
		socketConn.send(req, resp);
		return resp.getInfos();
	}

	/**
	 * 
	 * @param fiefid
	 *            注：暂不支持战争中的领地放弃
	 * @throws GameException
	 */
	public void fiefAbandon(long fiefid) throws GameException {
		FiefAbandonReq req = new FiefAbandonReq(fiefid);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * rich战争信息查询
	 * 
	 * @param battleId
	 * @param needHero
	 *            当前传的都是true true:返回将领信息
	 * @return
	 * @throws GameException
	 */
	public RichBattleInfoQueryResp richBattleInfoQuery(long battleId,
			boolean needHero) throws GameException {
		RichBattleInfoQueryReq req = new RichBattleInfoQueryReq(battleId,
				needHero);
		RichBattleInfoQueryResp resp = new RichBattleInfoQueryResp();
		socketConn.send(req, resp);
		CacheMgr.fillRichBattleInfoClient(resp.getInfo(), resp.getHeroInfos());
		return resp;
	}

	// /**
	// * 购买军衔
	// *
	// * @param rankid
	// * 目标军衔等级
	// * @param type
	// * 购买方式 （1：元宝 2：贡献值）
	// * @return
	// * @throws GameException
	// */
	// public MilitaryRankBuyResp militaryRankBuy(int rankid, int type)
	// throws GameException {
	// MilitaryRankBuyReq req = new MilitaryRankBuyReq(rankid, type);
	// MilitaryRankBuyResp resp = new MilitaryRankBuyResp();
	// socketConn.send(req, resp);
	//
	// return resp;
	// }

	/**
	 * 查询领地历史战斗日志
	 * 
	 * @param fiefid
	 * @param start
	 * @param count
	 *            单次最大10个
	 * @return
	 */
	public BriefBattleLogQueryByFiefIdResp briefBattleLogQueryByFiefId(
			long fiefid, int start, int count) throws GameException {
		BriefBattleLogQueryByFiefIdReq req = new BriefBattleLogQueryByFiefIdReq(
				fiefid, start, count);
		BriefBattleLogQueryByFiefIdResp resp = new BriefBattleLogQueryByFiefIdResp();
		socketConn.send(req, resp);
		return resp;
	}

	// 家族相关协议

	/**
	 * 创建家族
	 * 
	 * @param name
	 *            长度和玩家昵称一样
	 * @param type
	 *            创建方式 1：正常消耗 2：元宝
	 * @return
	 * @throws GameException
	 */
	public GuildBuildResp guildBuild(String name, int type)
			throws GameException {
		GuildBuildReq req = new GuildBuildReq(name, type);
		GuildBuildResp resp = new GuildBuildResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 家族升级
	 * 
	 * @param guildId
	 * @return
	 * @throws GameException
	 */
	public GuildLevelUpResp guildLevelUp(int guildId) throws GameException {
		GuildLevelUpReq req = new GuildLevelUpReq(guildId);
		GuildLevelUpResp resp = new GuildLevelUpResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 更新家族信息
	 * 
	 * @param guildid
	 * @param desc
	 * @param image
	 * @param announcement
	 * @return
	 * @throws GameException
	 */
	public GuildInfoUpdateResp guildInfoUpdate(int guildid, String desc,
			int image, String announcement, boolean autoJoin)
			throws GameException {
		GuildInfoUpdateReq req = new GuildInfoUpdateReq(guildid, desc, image,
				announcement, autoJoin);
		GuildInfoUpdateResp resp = new GuildInfoUpdateResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 查询rich家族信息
	 * 
	 * @param guildid
	 * @return
	 * @throws GameException
	 */
	public RichGuildInfoClient richGuildInfoQuery(int guildid)
			throws GameException {
		RichGuildInfoQueryReq req = new RichGuildInfoQueryReq(guildid);
		RichGuildInfoQueryResp resp = new RichGuildInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getRgic();
	}

	/**
	 * 查询otherrich家族信息
	 * 
	 * @param guildid
	 * @return
	 * @throws GameException
	 */
	public OtherRichGuildInfoClient otherRichGuildInfoClient(int guildid)
			throws GameException {
		OtherRichGuildInfoQueryReq req = new OtherRichGuildInfoQueryReq(guildid);
		OtherRichGuildInfoQueryResp resp = new OtherRichGuildInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getOrgic();
	}

	/**
	 * 查询brief家族信息
	 * 
	 * @param ids
	 * @return
	 * @throws GameException
	 */
	public List<BriefGuildInfoClient> briefGuildInfoQuery(List<Integer> ids)
			throws GameException {
		BriefGuildInfoQueryReq req = new BriefGuildInfoQueryReq(ids);
		BriefGuildInfoQueryResp resp = new BriefGuildInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getInfos();
	}

	/**
	 * 家族-邀请
	 * 
	 * @param target
	 * @param message
	 * @return
	 * @throws GameException
	 */
	public GuildInviteAskResp guildInviteAsk(int guildid, int target,
			String message) throws GameException {
		GuildInviteAskReq req = new GuildInviteAskReq(guildid, target, message);
		GuildInviteAskResp resp = new GuildInviteAskResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 家族-响应邀请
	 * 
	 * @param guildid
	 *            家族id
	 * @param answer
	 *            1: 同意 2：拒绝
	 * @throws GameException
	 */
	public void guildInviteApprove(int guildid, int answer)
			throws GameException {
		GuildInviteApproveReq req = new GuildInviteApproveReq(guildid, answer);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 邀请删除
	 * 
	 * @param guildid
	 * @param target
	 * @throws GameException
	 */
	public void guildInviteRemove(int guildid, int target) throws GameException {
		GuildInviteRemoveReq req = new GuildInviteRemoveReq(guildid, target);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 家族成员退出
	 * 
	 * @param guildid
	 * @throws GameException
	 */
	public void guildMemberQuit(int guildid) throws GameException {
		GuildMemberQuitReq req = new GuildMemberQuitReq(guildid);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 开除组员
	 * 
	 * @param guildid
	 * @param target
	 * @throws GameException
	 */
	public void guildMemberRemove(int guildid, int target) throws GameException {
		GuildMemberRemoveReq req = new GuildMemberRemoveReq(guildid, target);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 开除组员
	 * 
	 * @param guildid
	 * @param target
	 * @throws GameException
	 */
	public void guildLeaderAssign(int guildid, int target) throws GameException {
		GuildLeaderAssignReq req = new GuildLeaderAssignReq(guildid, target);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 获取RichGuildInfo的版本号
	 * 
	 * @param guildid
	 * @return
	 * @throws GameException
	 */
	public RichGuildVersionQueryResp richGuildVersionQuery(int guildid)
			throws GameException {
		RichGuildVersionQueryReq req = new RichGuildVersionQueryReq(guildid);
		RichGuildVersionQueryResp resp = new RichGuildVersionQueryResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 申请加入家族
	 * 
	 * @param guildId
	 * @param message
	 * @return
	 * @throws GameException
	 */
	public GuildJoinAskResp guildJoinAsk(int guildId, String message)
			throws GameException {
		GuildJoinAskReq req = new GuildJoinAskReq(guildId, message);
		GuildJoinAskResp resp = new GuildJoinAskResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 批准加入家族
	 * 
	 * @param guildId
	 * @param target
	 *            申请者id
	 * @param answer
	 *            1: 同意 2：拒绝
	 * @return
	 * @throws GameException
	 */
	public GuildJoinApproveResp guildJoinApprove(int guildId, int target,
			int answer) throws GameException {
		GuildJoinApproveReq req = new GuildJoinApproveReq(guildId, target,
				answer);
		GuildJoinApproveResp resp = new GuildJoinApproveResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * // 成员职位任命
	 * 
	 * @param guildId
	 * @param target
	 * @param position
	 * @throws GameException
	 */
	public void guildPositionAssign(int guildId, int target, int position)
			throws GameException {
		GuildPositionAssignReq req = new GuildPositionAssignReq(guildId,
				target, position);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 领地收藏夹 - 加入
	 * 
	 * @param fiefid
	 * @throws GameException
	 */
	public void favoriteFiefAdd(long fiefid) throws GameException {
		FavoriteFiefAddReq req = new FavoriteFiefAddReq(fiefid);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 领地收藏夹 - 删除
	 * 
	 * @param fiefid
	 * @throws GameException
	 */
	public void favoriteFiefDel(long fiefid) throws GameException {
		FavoriteFiefDelReq req = new FavoriteFiefDelReq(fiefid);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 领地收藏夹-查询
	 * 
	 * @return
	 * @throws GameException
	 */
	public List<Long> favoriteFiefQuery() throws GameException {
		FavoriteFiefQueryReq req = new FavoriteFiefQueryReq();
		FavoriteFiefQueryResp resp = new FavoriteFiefQueryResp();
		socketConn.send(req, resp);
		return resp.getFiefids();
	}

	/**
	 * 家族静态数据查询
	 * 
	 * @param dataType
	 *            静态数据类型
	 * @param guildid
	 *            家族id
	 * @param id
	 *            取比这个id小的数据。如果为0，则从最新数据开始取
	 * @param count
	 *            返回的数量
	 * @return
	 * @throws GameException
	 */
	public StaticGuildDataQueryResp staticGuildDataQuery(
			StaticGuildDataType dataType, int guildid, long id, int count)
			throws GameException {
		StaticGuildDataQueryReq req = new StaticGuildDataQueryReq(dataType,
				guildid, id, count);
		StaticGuildDataQueryResp resp = new StaticGuildDataQueryResp();
		socketConn.send(req, resp);
		return resp;
	}

	// 将领相关

	/**
	 * 刷新可招募将领
	 * 
	 * @param type
	 *            刷新方式 1:盲选 2：商店
	 * 
	 * @return
	 * @throws GameException
	 */
	public HeroRefreshResp heroRefresh(int type) throws GameException {
		HeroRefreshReq req = new HeroRefreshReq(type);
		HeroRefreshResp resp = new HeroRefreshResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 购买将领
	 * 
	 * @param id
	 *            商店编号
	 * @return
	 * @throws GameException
	 */
	public HeroBuyResp heroBuy(int id) throws GameException {
		HeroBuyReq req = new HeroBuyReq(id);
		HeroBuyResp resp = new HeroBuyResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 兑换将领
	 * 
	 * @param id
	 *            对应编号
	 * @return
	 * @throws GameException
	 */
	public HeroExchangeResp heroExchange(int id) throws GameException {
		HeroExchangeReq req = new HeroExchangeReq(id);
		HeroExchangeResp resp = new HeroExchangeResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 解雇将领
	 * 
	 * @param hero
	 * @throws GameException
	 */
	public HeroAbandonResp heroAbandon(List<Long> heros) throws GameException {
		HeroAbandonReq req = new HeroAbandonReq(heros);
		HeroAbandonResp resp = new HeroAbandonResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 查询其他玩家的将领信息
	 * 
	 * @param userId
	 *            对应将领所属userid
	 * @param ids
	 *            将领id（如果不传，默认全查）
	 * @return
	 * @throws GameException
	 */
	public List<OtherHeroInfoClient> otherUserHeroInfoQuery(int userId,
			List<Long> ids) throws GameException {
		OtherUserHeroInfoQueryReq req = new OtherUserHeroInfoQueryReq(userId,
				ids);
		OtherUserHeroInfoQueryResp resp = new OtherUserHeroInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getInfos();
	}

	/**
	 * 将领强化
	 * 
	 * @param id
	 *            对应id
	 * @param type
	 *            强化类型 1功勋强化 2元宝强化 3道具强化 4一键元宝强化
	 * @return
	 * @throws GameException
	 */
	public HeroEnhanceResp heroEnhance(long id, int type) throws GameException {
		HeroEnhanceReq req = new HeroEnhanceReq(id, type);
		HeroEnhanceResp resp = new HeroEnhanceResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 将领-技能学习
	 * 
	 * @param id
	 *            将领id
	 * @param slotId
	 *            技能槽id
	 * @param skillId
	 * @return
	 * @throws GameException
	 */
	public HeroSkillStudyResp heroSkillStudy(long id, int slotId, int skillId)
			throws GameException {
		HeroSkillStudyReq req = new HeroSkillStudyReq(id, slotId, skillId);
		HeroSkillStudyResp resp = new HeroSkillStudyResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 将领-技能放弃
	 * 
	 * @param id
	 *            将领id
	 * @param slotId
	 *            技能槽id
	 * @throws GameException
	 */
	public void heroSkillAbandon(long id, int slotId) throws GameException {
		HeroSkillAbandonReq req = new HeroSkillAbandonReq(id, slotId);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 将领 - 恢复体力
	 * 
	 * @param hero
	 * @param currency
	 * @return
	 * @throws GameException
	 */
	public HeroStaminaRecoveryResp heroStaminaRecovery(long hero, int currency)
			throws GameException {
		HeroStaminaRecoveryReq req = new HeroStaminaRecoveryReq(hero, currency);
		HeroStaminaRecoveryResp resp = new HeroStaminaRecoveryResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 领地-选择守城将领 将领全给
	 * 
	 * @param fiefId
	 * @param heros
	 * 
	 * @throws GameException
	 */
	public void fiefHeroSelect(long fiefId, List<HeroIdInfoClient> heros)
			throws GameException {
		FiefHeroSelectReq req = new FiefHeroSelectReq(fiefId, heros);
		EmptyResp resp = new EmptyResp();
		socketConn.send(req, resp);
	}

	/**
	 * 查询其他领主的兵力详情
	 * 
	 * @param target
	 *            目标玩家
	 * @return
	 * @throws GameException
	 */
	public List<ArmInfoClient> otherLordTroopInfoQuery(int target)
			throws GameException {
		OtherLordTroopInfoQueryReq req = new OtherLordTroopInfoQueryReq(target);
		OtherLordTroopInfoQueryResp resp = new OtherLordTroopInfoQueryResp();
		socketConn.send(req, resp);
		return resp.getInfos();
	}

	/**
	 * 领地同步 注：自己所有RichFiefInfo应该从RichFiefCache中取，不要调用该接口
	 * 
	 * @param allSynIds
	 * @param difSynIds
	 * @return
	 * @throws GameException
	 */
	public FiefDataSynResp fiefDataSyn(List<Long> allSynIds,
			List<Long> difSynIds) throws GameException {
		FiefDataSynReq req = new FiefDataSynReq(allSynIds, difSynIds);
		FiefDataSynResp resp = new FiefDataSynResp();
		socketConn.send(req, resp);

		// List<RichFiefInfo> ls = new ArrayList<RichFiefInfo>();
		// FiefDataSynResp resp = new FiefDataSynResp();
		// resp.setDatas(null);
		// resp.setInfos(ls);
		// for (Long id : allSynIds) {
		// ls.add(SelfFiefQuery(id));
		// }
		// for (Long id : difSynIds) {
		// ls.add(SelfFiefQuery(id));
		// }
		return resp;
	}

	/**
	 * 饿死兵
	 * 
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient userTroopCost() throws GameException {
		UserTroopCostReq req = new UserTroopCostReq();
		UserTroopCostResp resp = new UserTroopCostResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 资源点收获
	 * 
	 * @param fiefId
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient fiefReceive(long fiefId) throws GameException {
		FiefReceiveReq req = new FiefReceiveReq(fiefId);
		FiefReceiveResp resp = new FiefReceiveResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 资源点建筑购买/升级
	 * 
	 * @param type
	 * @param itemId
	 * @param fiefId
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient buildingFiefBuy(int type, int itemId, long fiefId)
			throws GameException {
		BuildingBuyFiefReq req = new BuildingBuyFiefReq(type, itemId, fiefId);
		BuildingBuyFiefResp resp = new BuildingBuyFiefResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 将领 进化
	 * 
	 * @param heroId
	 * @param type
	 *            1:金币 2:元宝
	 * @return
	 * @throws GameException
	 */
	public HeroEvolveResp heroEvolve(long heroId, int type)
			throws GameException {
		HeroEvolveReq req = new HeroEvolveReq(heroId, type);
		HeroEvolveResp resp = new HeroEvolveResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 将领 - 吞噬
	 * 
	 * @param heroId
	 * @param bagIds
	 *            道具
	 * @param type
	 *            1:金币 2：元宝
	 * @return
	 * @throws GameException
	 */
	public HeroDevourResp heroDevour(long heroId, List<Long> bagIds, int type)
			throws GameException {
		HeroDevourReq req = new HeroDevourReq(heroId, bagIds, type);
		HeroDevourResp resp = new HeroDevourResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 将领 - 宠幸
	 * 
	 * @param heroId
	 * @param slotId
	 * @return
	 * @throws GameException
	 */
	public HeroFavourResp heroFavour(long heroId, int slotId)
			throws GameException {
		HeroFavourReq req = new HeroFavourReq(heroId, slotId);
		HeroFavourResp resp = new HeroFavourResp();
		socketConn.send(req, resp);
		return resp;
	}

	// 副本相关

	/**
	 * 副本重置
	 * 
	 * @param actid
	 * @param campaignid
	 * @param mode
	 *            难度
	 * @param type
	 *            （1：正常消耗 2：元宝消耗）
	 * @return
	 * @throws GameException
	 */
	public DungeonRestResp dungeonReset(int actid, int campaignid, int type)
			throws GameException {
		DungeonResetReq req = new DungeonResetReq(actid, campaignid, type);
		DungeonRestResp resp = new DungeonRestResp();
		socketConn.send(req, resp);
		Account.actInfoCache.updateData(actid, resp.getCi());

		return resp;
	}

	/**
	 * 副本占卜
	 * 
	 * @param actId
	 * @param campaignId
	 * @param mode
	 * @param hero
	 * @param troops
	 * @return
	 * @throws GameException
	 */
	public BattleDungeonDivineResp dungeonDivine(int actId, int campaignId,
			int mode, long hero, List<ArmInfoClient> troops)
			throws GameException {
		BattleDungeonDivineReq req = new BattleDungeonDivineReq(actId,
				campaignId, mode, hero, troops);
		BattleDungeonDivineResp resp = new BattleDungeonDivineResp();
		socketConn.send(req, resp);

		return resp;
	}

	/**
	 * 领取章节奖励
	 * 
	 * @param actId
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient dungeonReward(int actId) throws GameException {
		DungeonRewardReq req = new DungeonRewardReq(actId);
		DungeonRewardResp resp = new DungeonRewardResp();
		socketConn.send(req, resp);
		// 设置战役全通奖励flag
		ActInfoClient act = Account.actInfoCache.getAct(actId);
		if (null != act)
			act.setAllPassBonus();
		return resp.getRi();
	}

	/**
	 * 副本扫荡
	 * 
	 * @param actId
	 * @param mode
	 * @param hero
	 * @param troops
	 * @param cost
	 * @return
	 * @throws GameException
	 */
	public DungeonClearResp dungeonClear(int actId, List<HeroIdInfoClient> heros)
			throws GameException {
		DungeonClearReq req = new DungeonClearReq(actId, heros);
		DungeonClearResp resp = new DungeonClearResp();
		socketConn.send(req, resp);
		Account.actInfoCache.updateDiffData(resp.getAi());
		return resp;
	}

	/**
	 * 副本重置
	 * 
	 * @param actId
	 * @param campaignids
	 * @param mode
	 * @param type
	 * @return
	 * @throws GameException
	 */
	public DungeonActResetResp dungeonActReset(int actId,
			List<Integer> campaignids, int type) throws GameException {
		DungeonActResetReq req = new DungeonActResetReq(actId, campaignids,
				type);
		DungeonActResetResp resp = new DungeonActResetResp();
		socketConn.send(req, resp);
		Account.actInfoCache.updateData(actId, resp.getInfos());
		return resp;
	}

	/**
	 * 
	 * @param type
	 *            1: 私聊 2：家族 3：国家 4：世界
	 * @param target
	 *            根据type选择目标
	 * @param context
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient messagePost(int type, int target, String context)
			throws GameException {
		MessagePostReq req = new MessagePostReq(type, target, context);
		MessagePostResp resp = new MessagePostResp();
		socketConn.send(req, resp);

		return resp.getRi();
	}

	/**
	 * 连续登录 签到领奖
	 * 
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient checkIn() throws GameException {
		UserCheckInReq req = new UserCheckInReq();
		UserCheckInResp resp = new UserCheckInResp();
		socketConn.send(req, resp);
		Account.user.checked();

		return resp.getRi();
	}

	/**
	 * 兵种强化
	 * 
	 * @param armId
	 * @param effectId
	 * @param type
	 *            1.资源强化 2.元宝强化
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient armEhance(int armId, int effectId, int type)
			throws GameException {
		ArmEnhanceReq req = new ArmEnhanceReq(armId, effectId, type);
		ArmEnhanceResp resp = new ArmEnhanceResp();
		socketConn.send(req, resp);
		return resp.getRi();
	}

	/**
	 * 资源建筑元宝 直接刷满产出资源。 如过是领地上的建筑传fief 主城里的传null 传入的bulding 会被更新到最新的sis状态
	 * 
	 * @param building
	 * @param fiefId
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient buildingReset(BuildingInfoClient building,
			BriefFiefInfoClient fief) throws GameException {
		BuildingResetReq req = new BuildingResetReq(building.getItemId(),
				fief == null ? 0 : fief.getId());
		BuildingResetResp resp = new BuildingResetResp();
		socketConn.send(req, resp);
		building.setSis(resp.getBuilding().getSis());
		return resp.getRi();
	}

	/**
	 * 肥羊
	 * 
	 * @return
	 * @throws GameException
	 */
	public HotUserAttrScoreInfoQueryResp hotUserAttrScoreInfoQuery(int level,
			int country, ResultPage resultPage, int type) throws GameException {
		HotUserAttrScoreInfoQueryReq req = new HotUserAttrScoreInfoQueryReq(
				level, country, resultPage, type);
		HotUserAttrScoreInfoQueryResp resp = new HotUserAttrScoreInfoQueryResp();
		socketConn.send(req, resp);
		CacheMgr.fillBriefFiefInfoClients(resp.getBfics());
		if (Config.getController().getBattleMap() != null) {
			Config.getController().getBattleMap().update(resp.getBfics());
		}
		return resp;
	}

	/**
	 * 查询追杀令信息
	 * 
	 * @param country
	 * @param id
	 *            取比这个id小的数据。如果为0，则从最新数据开始取
	 * @param count
	 *            返回的数量，最大10条
	 * @return
	 * @throws GameException
	 */
	public List<PlayerWantedInfoClient> playerWantedInfoQuery(int country,
			long id, int count) throws GameException {
		PlayerWantedInfoQueryReq req = new PlayerWantedInfoQueryReq(country,
				id, count);
		PlayerWantedInfoQueryResp resp = new PlayerWantedInfoQueryResp();
		socketConn.send(req, resp);
		CacheMgr.fillPlayerWantedInfoClients(resp.getInfos());
		return resp.getInfos();
	}

	/**
	 * 家族搜索
	 * 
	 * @param page
	 * @return
	 * @throws GameException
	 */
	public List<GuildSearchInfoClient> guildSearch(ResultPage page,
			List<GuildSearchCond> conds) throws GameException {
		GuildSearchReq req = new GuildSearchReq(page, conds);
		GuildSearchResp resp = new GuildSearchResp();
		socketConn.send(req, resp);
		CacheMgr.fillGuildSearchInfoClient(resp.getInfos());
		return resp.getInfos();
	}

	/**
	 * 富矿列表
	 * 
	 * @param
	 * @return
	 * @throws GameException
	 */
	public List<Long> advancedSiteQuery(int propId, int start, int count)
			throws GameException {
		AdvancedSiteQueryReq req = new AdvancedSiteQueryReq(propId, start,
				count);
		AdvancedSiteQueryResp resp = new AdvancedSiteQueryResp();
		socketConn.send(req, resp);
		return resp.getFiefIds();
	}

	/**
	 * 荣耀排行
	 * 
	 * @param guild
	 *            请求家族之光榜时带上
	 * @return
	 * @throws GameException
	 */
	public MsgRspHonorRankInfo getHonorRankInfo(HonorRankType type,
			ResultPage resultPage, int guild) throws GameException {
		HonorRankInfoReq req = new HonorRankInfoReq(type, resultPage, guild);
		HonorRankInfoResp resp = new HonorRankInfoResp();
		socketConn.send(req, resp);
		return resp.getMsgRspHonorRankInfo();
	}

	/**
	 * 荣耀领奖
	 * 
	 * @param
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient getHonorRankReward(int type) throws GameException {
		HonorRankRewardReq req = new HonorRankRewardReq(type);
		HonorRankRewardResp resp = new HonorRankRewardResp();
		socketConn.send(req, resp);
		return resp.getReturnInfoClient();
	}

	/**
	 * 玩家排行
	 * 
	 * @param
	 * @return
	 * @throws GameException
	 */
	public MsgRspUserRankInfo getUserRankInfo(UserRankType type,
			ResultPage resultPage) throws GameException {
		UserRankInfoReq req = new UserRankInfoReq(type, resultPage);
		UserRankInfoResp resp = new UserRankInfoResp();
		socketConn.send(req, resp);
		return resp.getMsgRspUserRankInfo();
	}

	/**
	 * 武将排行
	 * 
	 * @param
	 * @return
	 * @throws GameException
	 */
	public MsgRspHeroRankInfo getHeroRankInfo(ResultPage resultPage)
			throws GameException {
		HeroRankInfoReq req = new HeroRankInfoReq(resultPage);
		HeroRankInfoResp resp = new HeroRankInfoResp();
		socketConn.send(req, resp);
		return resp.getMsgRspHeroRankInfo();
	}

	/**
	 * 查询巅峰战场信息
	 * 
	 * @param pos
	 * @return
	 * @throws GameException
	 */
	public ArenaQueryResp arenaQuery(boolean needTopInfo) throws GameException {
		ArenaQueryReq req = new ArenaQueryReq(needTopInfo);
		ArenaQueryResp resp = new ArenaQueryResp();
		socketConn.send(req, resp);
		CacheMgr.fillArenaUserRankInfoClient(resp.getTopUsers());
		CacheMgr.fillArenaUserRankInfoClient(resp.getAttackableUsers());
		return resp;
	}

	/**
	 * 挑战巅峰战场
	 * 
	 * @param target
	 *            , targetPos, selfPos
	 * @return
	 * @throws GameException
	 */
	public ArenaAttackResp arenaAttack(int target, int targetPos, int selfPos)
			throws GameException {
		ArenaAttackReq req = new ArenaAttackReq(target, targetPos, selfPos);
		ArenaAttackResp resp = new ArenaAttackResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 巅峰战场领取奖励
	 * 
	 * @param pos
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient arenaAwrad() throws GameException {
		ArenaRewardReq req = new ArenaRewardReq();
		ArenaRewardResp resp = new ArenaRewardResp();
		socketConn.send(req, resp);
		return resp.getReturnInfoClient();
	}

	/**
	 * 保存巅峰部队配置
	 * 
	 * @param pos
	 * @return
	 * @throws GameException
	 */
	public void arenaConf(List<HeroIdInfoClient> heros,
			List<ArmInfoClient> troops) throws GameException {
		ArenaConfReq req = new ArenaConfReq(heros, troops);
		ArenaConfResp resp = new ArenaConfResp();
		socketConn.send(req, resp);
	}

	/**
	 * 墓地复活
	 * 
	 * @param target
	 *            1：普通士兵 2：boss
	 * @param buildingId
	 * @param armIds
	 * @param bossArmId
	 *            目标兵种
	 * @param bossCount
	 * @return
	 * @throws GameException
	 */
	public ReturnInfoClient manorRevive(int target, int buildingId,
			List<Integer> armIds, int bossArmId, int bossCount)
			throws GameException {
		ManorReviveReq req = new ManorReviveReq(target, buildingId, armIds,
				bossArmId, bossCount);
		ManorReviveResp resp = new ManorReviveResp();
		socketConn.send(req, resp);
		return resp.getRic();
	}

	/**
	 * 墓地复活-弃疗
	 * 
	 * @throws GameException
	 */
	public void manorReviveClean() throws GameException {
		ManorReviveCleanReq req = new ManorReviveCleanReq();
		ManorReviveCleanResp resp = new ManorReviveCleanResp();
		socketConn.send(req, resp);
	}

	// 装备相关
	/**
	 * 装备购买
	 * 
	 * @param scheme
	 *            方案号
	 * @return
	 * @throws GameException
	 */
	public EquipmentBuyResp equipmentBuy(int scheme) throws GameException {
		EquipmentBuyReq req = new EquipmentBuyReq(scheme);
		EquipmentBuyResp resp = new EquipmentBuyResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备使用
	 * 
	 * @param id
	 *            装备id
	 * @param srcHero
	 *            原使用者
	 * @param targetHero
	 * @return
	 * @throws GameException
	 */
	public EquipmentReplaceResp equipmentReplace(long id, long srcHero,
			long targetHero) throws GameException {
		EquipmentReplaceReq req = new EquipmentReplaceReq(id, srcHero,
				targetHero);
		EquipmentReplaceResp resp = new EquipmentReplaceResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备卸下
	 * 
	 * @param id
	 * @param targetHero
	 * @return
	 * @throws GameException
	 */
	public EquipmentDisarmResp equipmentDisarm(long id, long targetHero)
			throws GameException {
		EquipmentDisarmReq req = new EquipmentDisarmReq(id, targetHero);
		EquipmentDisarmResp resp = new EquipmentDisarmResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备卖出
	 * 
	 * @param id
	 * @return
	 * @throws GameException
	 */
	public EquipmentSellResp equipmentSell(long id) throws GameException {
		EquipmentSellReq req = new EquipmentSellReq(id);
		EquipmentSellResp resp = new EquipmentSellResp(id);
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备镶嵌宝石
	 * 
	 * @param id
	 *            装备id
	 * @param hero
	 *            如果装备使用，对应武将
	 * @param itemId
	 *            宝石id
	 * @return
	 * @throws GameException
	 */
	public EquipmentItemInsertResp equipmentItemInsert(long id, long hero,
			int itemId) throws GameException {
		EquipmentItemInsertReq req = new EquipmentItemInsertReq(id, hero,
				itemId);
		EquipmentItemInsertResp resp = new EquipmentItemInsertResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 宝石升级
	 * 
	 * @param id
	 *            装备id
	 * @param hero
	 *            如果装备使用，英雄id
	 * @return
	 * @throws GameException
	 */
	public EquipmentInsertItemLevelUpResp equipmentInsertItemLevelUp(long id,
			long hero) throws GameException {
		EquipmentInsertItemLevelUpReq req = new EquipmentInsertItemLevelUpReq(
				id, hero);
		EquipmentInsertItemLevelUpResp resp = new EquipmentInsertItemLevelUpResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备卸下宝石
	 * 
	 * @param id
	 *            装备id
	 * @param hero
	 *            如果装备使用，对应武将
	 * @return
	 * @throws GameException
	 */
	public EquipmentItemRemoveResp equipmentItemRemove(long id, long hero)
			throws GameException {
		EquipmentItemRemoveReq req = new EquipmentItemRemoveReq(id, hero);
		EquipmentItemRemoveResp resp = new EquipmentItemRemoveResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 装备锻造
	 * 
	 * @param id
	 * @param hero
	 *            如果装备使用，对应武将
	 * @param effectType
	 *            目标效果
	 * @param useCurrency
	 *            [default = false] 是否用元宝填补差价
	 * @return
	 * @throws GameException
	 */
	public EquipmentForgeResp equipmentForge(long id, long hero,
			int effectType, boolean useCurrency) throws GameException {
		EquipmentForgeReq req = new EquipmentForgeReq(id, hero, effectType,
				useCurrency);
		EquipmentForgeResp resp = new EquipmentForgeResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 
	 * @param id
	 *            装备id
	 * @param heroId
	 *            如果装备使用，对应武将
	 * @param type
	 *            1:道具升级 2：元宝补足道具 3：一键99级
	 * @return
	 * @throws GameException
	 */
	public EquipmentLevelUpResp equipmentLevelUp(long id, long heroId, int type)
			throws GameException {
		EquipmentLevelUpReq req = new EquipmentLevelUpReq(id, heroId, type);
		EquipmentLevelUpResp resp = new EquipmentLevelUpResp();
		socketConn.send(req, resp);
		return resp;
	}

	// 血战相关

	/**
	 * 血战开战
	 * 
	 * @param num
	 * @return
	 * @throws GameException
	 */
	public BloodAttackResp bloodAttack(int num) throws GameException {
		BloodAttackReq req = new BloodAttackReq(num);
		BloodAttackResp resp = new BloodAttackResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 保存血战部队配置
	 * 
	 * @param heros
	 * @param troops
	 * @throws GameException
	 */
	public void bloodConf(List<HeroIdInfoClient> heros,
			List<ArmInfoClient> troops) throws GameException {
		BloodConfReq req = new BloodConfReq(heros, troops);
		BloodConfResp resp = new BloodConfResp();
		socketConn.send(req, resp);
	}

	/**
	 * 血战翻牌
	 * 
	 * @param pos
	 *            翻牌的位置
	 * @return
	 * @throws GameException
	 */
	public BloodPokerResp bloodPoker(int pos) throws GameException {
		BloodPokerReq req = new BloodPokerReq(pos);
		BloodPokerResp resp = new BloodPokerResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 血战领取奖励
	 * 
	 * @return
	 * @throws GameException
	 */
	public BloodRewardResp bloodReward() throws GameException {
		BloodRewardReq req = new BloodRewardReq();
		BloodRewardResp resp = new BloodRewardResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 血战排行榜
	 * 
	 * @param resultPage
	 * @param self
	 * @return
	 * @throws GameException
	 */
	public BloodRankQueryResp bloodRankQuery(ResultPage resultPage, boolean self)
			throws GameException {
		BloodRankQueryReq req = new BloodRankQueryReq(resultPage, self);
		BloodRankQueryResp resp = new BloodRankQueryResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 血战排行榜领奖
	 * 
	 * @return
	 * @throws GameException
	 */
	public BloodRankRewardResp bloodRankReward() throws GameException {
		BloodRankRewardReq req = new BloodRankRewardReq();
		BloodRankRewardResp resp = new BloodRankRewardResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 获取圣都战争状态
	 * 
	 * @param resultPage
	 * @param country
	 * @return
	 * @throws GameException
	 */
	public QueryHolyBattleStateResp queryHolyBattleState(int country)
			throws GameException {
		QueryHolyBattleStateReq req = new QueryHolyBattleStateReq(country);
		QueryHolyBattleStateResp resp = new QueryHolyBattleStateResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 玩家状态更新
	 * 
	 * @param status
	 *            对应role_status
	 * @param type
	 *            1:正常开通 2：元宝开通
	 * @return
	 * @throws GameException
	 */
	public UserStatusUpdateResp userStatusUpdate(ROLE_STATUS status, int type)
			throws GameException {
		UserStatusUpdateReq req = new UserStatusUpdateReq(status, type);
		UserStatusUpdateResp resp = new UserStatusUpdateResp();
		socketConn.send(req, resp);
		return resp;
	}

	/**
	 * 获取救兵
	 * 
	 * @return
	 * @throws GameException
	 */
	public GetHelpArmResp getHelpArm() throws GameException {
		GetHelpArmReq req = new GetHelpArmReq();
		GetHelpArmResp resp = new GetHelpArmResp();
		socketConn.send(req, resp);
		return resp;
	}
}
