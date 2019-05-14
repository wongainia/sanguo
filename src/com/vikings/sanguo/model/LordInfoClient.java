package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.ManorReviveCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseLordInfo;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.BattleLogReturnInfo;
import com.vikings.sanguo.protos.BuildingStatusInfo;
import com.vikings.sanguo.protos.LordInfo;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.protos.ThingType;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 领主信息客户端结构
 * 
 * @author susong
 * 
 */
public class LordInfoClient {

	private long capital; // 首都 (庄园所在地)
	private List<ArmInfoClient> troopInfo;// 部队信息
	private LordBloodInfoClient lbic;// 血战信息
	private LordReviveInfoClient lric;// 医馆信息
	private LordArenaInfoClient laic;// 巅峰信息
	private HeroShopInfoClient hsic;// 将领商店信息
	private int shopCost;// 酒馆累计消耗元宝

	private BloodRankInfoClient selfBloodRankInfo;// 自己的巅峰排名（该数据每次进入巅峰荣耀的时候更新）

	private long lastTime;

	public void setCapital(long capital) {
		this.capital = capital;
	}

	public long getCapital() {
		return capital;
	}

	public LordBloodInfoClient getLbic() {
		return lbic;
	}

	public void setLbic(LordBloodInfoClient lbic) {
		this.lbic = lbic;
	}

	public LordReviveInfoClient getLric() {
		return lric;
	}

	public void setLric(LordReviveInfoClient lric) {
		this.lric = lric;
	}

	public LordArenaInfoClient getLaic() {
		return laic;
	}

	public void setLaic(LordArenaInfoClient laic) {
		this.laic = laic;
	}

	public int getShopCost() {
		return shopCost;
	}

	public void setShopCost(int shopCost) {
		this.shopCost = shopCost;
	}

	public BloodRankInfoClient getSelfBloodRankInfo() {
		return selfBloodRankInfo;
	}

	public void setSelfBloodRankInfo(BloodRankInfoClient selfBloodRankInfo) {
		this.selfBloodRankInfo = selfBloodRankInfo;
	}

	public List<ArmInfoClient> getTroopInfo() {
		if (null != troopInfo) {
			for (Iterator<ArmInfoClient> iter = troopInfo.iterator(); iter
					.hasNext();) {
				ArmInfoClient amrInfo = iter.next();
				if (amrInfo.getCount() <= 0) {
					iter.remove();
				}
			}
		}
		return troopInfo;
	}

	public void setTroopInfo(List<ArmInfoClient> troopInfo) {
		this.troopInfo = troopInfo;
	}

	// 全部兵力
	public int getArmCount() {
		int count = 0;
		if (null != troopInfo && !troopInfo.isEmpty()) {
			for (ArmInfoClient info : troopInfo) {
				count += info.getCount();
			}
		}
		if (count < 0)
			count = 0;
		return count;
	}

	public int getArmCountByType(int armId) {
		int count = 0;
		if (null != troopInfo && !troopInfo.isEmpty()) {
			for (ArmInfoClient info : troopInfo) {
				if (info.getId() == armId) {
					count = info.getCount();
					break;
				}

			}
		}
		return count;
	}

	// 酒馆相关
	public HeroShopInfoClient getHeroShopInfo() {
		if (null == hsic)
			hsic = new HeroShopInfoClient();
		return hsic;
	}

	public void setHeroShopInfo(HeroShopInfoClient heroShopInfo) {
		updateHeroShopInfoClient(heroShopInfo);
	}

	public List<HeroShopItemInfoClient> getHeroShopItemInfoClients() {
		return getHeroShopInfo().getInfos();
	}

	public void updateHeroShopInfoClient(HeroShopInfoClient heroShopInfoClient) {
		if (null == heroShopInfoClient)
			return;
		updateHeroShopItemInfoClient(heroShopInfoClient.getInfos());
	}

	public void updateHeroShopItemInfoClient(
			List<HeroShopItemInfoClient> heroShopItemInfoClients) {
		getHeroShopInfo().setInfos(heroShopItemInfoClients);
	}

	// 巅峰相关
	public int getArenaCount() {
		if (null == laic)
			return 0;
		else
			return laic.getArenaCount();
	}

	public int getArenaCountResetTime() {
		if (null == laic)
			return 0;
		else
			return laic.getArenaCountResetTime();
	}

	public List<HeroIdInfoClient> getArenaHeroInfos() {
		if (null == laic)
			return new ArrayList<HeroIdInfoClient>();
		else
			return laic.getArenaHeroIdInfos();
	}

	public int getArenaRank() {
		if (null == laic)
			return 0;
		else
			return laic.getArenaRank();
	}

	public int getArenaRewardStart() {
		if (null == laic)
			return 0;
		else
			return laic.getArenaRewardStart();
	}

	public int getArenaRewardStartValue() {
		if (null == laic)
			return 0;
		else
			return laic.getArenaRewardStartValue();
	}

	public List<ArmInfoClient> getArenaTroopInfo() {
		if (null == laic)
			return new ArrayList<ArmInfoClient>();
		else
			return laic.getArenaTroopInfo();
	}

	public List<ArmInfoClient> getReviveInfo() {
		if (null == lric) {
			return new ArrayList<ArmInfoClient>();
		} else {
			return lric.getReviveInfo();
		}
	}

	public List<ArmInfoClient> getReviveBossInfo() {
		if (null == lric) {
			return new ArrayList<ArmInfoClient>();
		} else {
			return lric.getReviveBossInfo();
		}
	}

	public List<ArmInfoClient> getReviveInfo(int index) {
		if (null == lric) {
			return new ArrayList<ArmInfoClient>();
		} else if (ManorReviveCache.TYPE_ISARM == index) {
			return lric.getReviveInfo();
		} else if (ManorReviveCache.TYPE_ISBOSS == index) {
			return lric.getReviveBossInfo();
		} else {
			return new ArrayList<ArmInfoClient>();
		}
	}

	// 医馆上次复活士兵的时间
	public long getReviveLastTime() {
		long lastTime = 0;

		if (null == lric) {
			return 0;
		} else {
			lastTime = lric.getLastTime() * 1000l;
		}
		return lastTime;
	}

	// 医馆救治士兵的次数
	public int getCount() {
		int revivedCount = 0;
		if (null == lric) {
			return 0;
		} else {
			if (DateUtil.isToday(getReviveLastTime())) {
				revivedCount = lric.getCount();
			}
		}
		return revivedCount;
	}

	public boolean isHeroInArena(long hero) {
		if (null == laic)
			return false;
		else
			return laic.isHeroInArena(hero);
	}

	public boolean isArenaConfig() {
		if (null == laic)
			return false;
		else
			return laic.isArenaConfig();
	}

	// 血战相关

	public boolean isBloodConfig() { // 是否已经配置过血战信息
		return null != lbic && !lbic.getBloodTroopInfo().isEmpty();
	}

	public int getBloodAttackRecord() {// 当前打血战的关卡
		if (null != lbic)
			return lbic.getBloodRecord() + 1;
		return 1;
	}

	public int getBloodCount() { // 今天已经打过的血战次数
		if (null != lbic && lbic.isValide())
			return lbic.getBloodCount();
		return 0;
	}

	public int getMaxBloodCount() { // 每天最大
		return CacheMgr.bloodCommonCache.getMaxTimes();
	}

	public int getBloodFreeTime() {
		UserVip vip = Account.user.getCurVip();
		return vip.getBloodFreeCount();
	}

	public int getTodayBestRecord() { // 取今日最佳战绩
		if (null != lbic && lbic.isValide())
			return lbic.getBloodRecordMax();
		return 0;
	}

	public int getLastRecord() { // 取上次进度
		if (null != lbic)
			return lbic.getBloodRecordLast();
		return 0;
	}

	public boolean isBloodLoss() { // 是否失败
		if (null != lbic)
			return lbic.isLoss();
		return false;
	}

	public int getContinueRecord() {
		int continueRecord = 0;
		if (null != lbic)
			continueRecord = lbic.getBloodRecordLast()
					- CacheMgr.bloodCommonCache.getRecordReduce();
		if (continueRecord < 0)
			continueRecord = 0;
		return continueRecord + 1;
	}

	public boolean hasReward() {// 是否可以领奖
		if (null != lbic)
			return lbic.hasReward();
		return false;
	}

	public int getBloodPokerOpenCount() { // 已经翻过的牌数量
		if (null != lbic)
			return lbic.getBloodPokerOpenCount();
		return 0;
	}

	public int getBloodPokerOpenLeftCount() { // 还可以翻牌数
		UserVip vip = Account.user.getCurVip();
		int count = vip.getBloodPokerCount();
		if (null != lbic)
			count = count - lbic.getBloodPokerOpenCount();
		if (count < 0)
			count = 0;
		return count;
	}

	public int getBloodCostCount() {
		int freeCount = getBloodFreeTime();
		int bloodCount = getBloodCount();
		if (freeCount >= bloodCount + 1)
			return 0;
		else {
			return CacheMgr.bloodCommonCache.getCostCount()
					+ (bloodCount - freeCount)
					* CacheMgr.bloodCommonCache.getCostAdd();
		}
	}

	// 医馆相关
	public boolean isReviveTroopChanged(List<ArmInfoClient> ls) {
		if (ListUtil.isNull(ls))
			return false;

		if (null == lric)
			return true;

		if (ListUtil.isNull(lric.getReviveInfo()))
			return true;

		if (ls.size() != lric.getReviveInfo().size())
			return true;

		for (ArmInfoClient it : lric.getReviveInfo()) {
			if (!ls.contains(it))
				return true;
		}

		return false;
	}

	// 战斗后结算；由于战斗后不能立即从服务器取到最新的lordinfo，所以需要手动根据log信息计算兵力
	public void battleClean(List<ArmInfoClient> list) {
		if (list == null || list.isEmpty())
			return;
		for (ArmInfoClient armInfo : list) {
			boolean has = true;
			for (int i = 0; i < troopInfo.size(); i++) {
				ArmInfoClient ai = troopInfo.get(i);
				if (armInfo.getId() == ai.getId()) {
					int count = ai.getCount() - armInfo.getCount();
					if (count < 0)
						count = 0;
					ai.setCount(count);
					break;
				}
				if (i == troopInfo.size() - 1) {
					has = false;
				}
			}
			if (!has)
				troopInfo.add(armInfo);
		}
	}

	public void battleClean(BattleLogInfo info) {
		if (null == info || null == Account.myLordInfo)
			return;
		List<BattleLogReturnInfo> list = info.getRisList();
		if (list.isEmpty())
			return;
		List<ArmInfoClient> armInfos = new ArrayList<ArmInfoClient>();
		for (BattleLogReturnInfo logInfo : list) {
			ReturnInfo returnInfo = logInfo.getInfo();
			if (returnInfo.getUserid() == Account.user.getId()) {
				List<ReturnThingInfo> rtis = returnInfo.getRtisList();
				for (ReturnThingInfo rti : rtis) {
					if (rti.getType() == ThingType.THING_TYPE_DEAD.getNumber()) {
						try {
							ArmInfoClient armInfo = new ArmInfoClient(
									rti.getThingid(), rti.getCount());
							armInfos.add(armInfo);
						} catch (GameException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		battleClean(armInfos);
	}

	public void addArmInfo(int armId, int count) {
		for (ArmInfoClient armInfo : troopInfo) {
			if (armInfo.getId() == armId) {
				armInfo.setCount(armInfo.getCount() + count);
				return;
			}
		}
		try {
			ArmInfoClient armInfo = new ArmInfoClient(armId, count);
			troopInfo.add(armInfo);
		} catch (Exception e) {
			Log.e("LordInfoClient", e.getMessage());
		}

	}

	public void addArmInfos(List<ArmInfoClient> infos) {
		List<ArmInfoClient> newArmInfos = new ArrayList<ArmInfoClient>();
		if (!troopInfo.isEmpty())
			for (ArmInfoClient info : infos) {
				for (int i = 0; i < troopInfo.size(); i++) {
					ArmInfoClient armInfo = troopInfo.get(i);
					if (info.getId() == armInfo.getId()) {
						armInfo.setCount(armInfo.getCount() + info.getCount());
						break;
					}
					if (i == troopInfo.size() - 1)
						newArmInfos.add(info);
				}
			}
		else {
			troopInfo.addAll(infos);
		}
		if (!newArmInfos.isEmpty()) {
			troopInfo.addAll(newArmInfos);
		}
	}

	public static LordInfoClient convert(LordInfo lordInfo)
			throws GameException {
		LordInfoClient info = new LordInfoClient();
		BaseLordInfo bi = lordInfo.getBi();

		info.setCapital(bi.getCapital());

		if (bi.hasTroopInfo())
			info.setTroopInfo(ArmInfoClient.convertList(bi.getTroopInfo()));
		else
			info.setTroopInfo(new ArrayList<ArmInfoClient>());
		if (bi.hasBloodInfo())
			info.setLbic(LordBloodInfoClient.convert(bi.getBloodInfo()));
		if (bi.hasReviveInfo())
			info.setLric(LordReviveInfoClient.convert(bi.getReviveInfo()));
		if (bi.hasArenaInfo())
			info.setLaic(LordArenaInfoClient.convert(bi.getArenaInfo()));
		if (bi.hasShopInfo())
			info.setHeroShopInfo(HeroShopInfoClient.convert(bi.getShopInfo()));
		info.setShopCost(bi.getShopCost());
		return info;
	}

	/**
	 * 根据粮草增长和消耗计算出 粮草耗尽的剩余时间 分钟
	 * 
	 * @return
	 */
	private int calcFoodLeftTime() {
		// total 每小时消耗
		int total = 0;
		if (!ListUtil.isNull(troopInfo)) {
			for (ArmInfoClient ai : troopInfo) {
				total = total + ai.getProp().getFoodConsume() * ai.getCount()
						/ 1000;
			}
		}

		int speed1 = 0, remain = Account.user.getFood(), speed2 = 0, speedLeftTime = 0;
		// 计算增长
		BuildingInfoClient b = Account.manorInfoClient.getFoodBuilding();
		if (b != null) {
			BuildingStatusInfo bs = b.getResSpeed();
			if (bs != null) {
				speed1 = bs.getValue();
				speed2 = bs.getValue();
				remain = remain + bs.getStartValue()
						+ (Config.serverTimeSS() - bs.getStart())
						* bs.getValue() / 3600;
			}
			bs = b.getResSpeedup();
			if (bs != null) {
				speed2 = speed2 * bs.getValue() / 100;
				speedLeftTime = bs.getStart() - Config.serverTimeSS();
				if (speedLeftTime < 0)
					speedLeftTime = 0;
			}
		}
		if (total <= speed1)
			return Integer.MAX_VALUE;
		long min = (speedLeftTime * (speed2 - speed1) / 3600 + remain) * 60L
				/ (total - speed1);
		if (min > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		else
			return (int) min;
	}

	public void checkFood() {
		if (Config.serverTime() - lastTime < 30 * 60 * 1000)
			return;
		lastTime = Config.serverTime();

		int time = calcFoodLeftTime();

		if (time < 60) {
			Config.getController()
					.getNotifyMsg()
					.addMsg("粮草储量不够啦",
							"你的粮草储备不够了，请及时补充，否则"
									+ StringUtil.color(
											time + "分钟后你的士兵可能会逃亡",
											Config.getController()
													.getResourceColorText(
															R.color.k7_color4)),
							R.drawable.notify_troop);
		}
	}

	public String checkFoodOnExit() {
		int h = calcFoodLeftTime() / 60;
		if (h < 7 * 24) {
			if (h > 1) {

				return "你的粮草还够消耗"
						+ StringUtil.color(DateUtil.formatHourDesc(h), "red")
						+ "，无粮后士兵将会逃亡，请及时补充";
			} else
				return "你的粮草不够消耗" + StringUtil.color("1小时", "red")
						+ "啦，士兵很快就会逃亡，建议先补充粮草";
		}
		return "";
	}

	public int[] getCurArenaExploit() {
		if (null == laic)
			return null;
		else
			return laic.getCurArenaExploit();
	}

	public boolean isArenaGraded() {
		if (null == laic)
			return false;
		else
			return laic.getArenaRank() != 0;
	}

	public ArenaUserRankInfoClient getArenaUserRankInfoClient() {
		ArenaUserRankInfoClient info = new ArenaUserRankInfoClient();
		info.setArenaHeros(getArenaHeroInfos());
		info.setRank(getArenaRank());
		info.setUserId(Account.user.getId());
		info.setUser(Account.user.bref());
		info.setCanAttack(false);
		return info;
	}

}
