package com.vikings.sanguo.model;

import java.util.List;

import javax.xml.transform.Templates;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseFiefInfo;
import com.vikings.sanguo.protos.FiefInfo;

/**
 * 领地信息
 * 
 * @author susong
 * 
 */
public class FiefInfoClient {

	private long id; // 领地id

	private FiefProp prop;

	private int userid; // 所属领主

	private int battleState; // 战场状态（0：没有战争 1：战斗准备,行军 2:围城中，防守方可以突围
								// 3：围城结束，攻击方可以发生战斗 4：战斗结束）
	private int battleTime; // 战场进入下阶段时间

	private int nextExtraBattleTime; // 下次圣城副本可进时间

	private int attackerId;

	private int unitCount;

	private int battleType;

	private BuildingInfoClient building;

	private FiefHeroInfoClient fhic; // 领地-将领信息

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	/**
	 * 自己建筑信息在lordfief下
	 * 
	 * @return
	 */
	public BuildingInfoClient getBuilding() {
		return Account.richFiefCache.getBuildingInfo(id);
	}

	public FiefProp getProp() {
		return prop;
	}

	/**
	 * 自己资源点兵力信息在lordfief下 主城兵力在manor
	 * 
	 * @return
	 */
	public List<ArmInfoClient> getTroopInfo() {
		if (this.prop.isCastle())
			return Account.manorInfoClient.getTroopInfo();
		else
			return Account.richFiefCache.getTroopInfo(id);
	}

	/**
	 * 自己资源点兵力信息在lordfief下 主城兵力在manor
	 * 
	 * @return
	 */
	public void setTroopInfo(List<ArmInfoClient> troopInfo) {
		if (this.prop.isCastle())
			Account.manorInfoClient.setTroopInfo(troopInfo);
		else
			Account.richFiefCache.setTroopInfo(id, troopInfo);
	}

	public int getBattleState() {
		return battleState;
	}

	public void setBattleState(int battleState) {
		this.battleState = battleState;
	}

	public int getBattleTime() {
		return battleTime;
	}

	public void setBattleTime(int battleTime) {
		this.battleTime = battleTime;
	}

	public int getNextExtraBattleTime() {
		return nextExtraBattleTime;
	}

	public void setNextExtraBattleTime(int nextExtraBattleTime) {
		this.nextExtraBattleTime = nextExtraBattleTime;
	}

	public FiefHeroInfoClient getFhic() {
		return fhic;
	}

	public void setFhic(FiefHeroInfoClient fhic) {
		this.fhic = fhic;
	}

	public int getBattleType() {
		return battleType;
	}

	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}

	public int getUnitCount() {
		List<ArmInfoClient> troopInfo = getTroopInfo();
		int count = 0;
		if (null != troopInfo) {
			for (ArmInfoClient armInfo : troopInfo) {
				count += armInfo.getCount();
			}
		}
		return count;
	}

	public List<HeroIdInfoClient> getHeroIdInfos() {
		FiefHeroInfoClient tmp = getFhic();
		if (null != tmp)
			return tmp.getFirstHeroInfos();
		else
			return null;
	}

	// 通过id 查找HeroIdInfoClient
	public HeroIdInfoClient getHiicByID(long id) {
		FiefHeroInfoClient tmpClient = getFhic();
		if (null != tmpClient) {
			List<HeroIdInfoClient> hiics = tmpClient.getAllHeros();
			for (HeroIdInfoClient heroIdInfoClient : hiics) {
				if (heroIdInfoClient.getId() == id) {
					return heroIdInfoClient;
				}
			}
		} else {
			return null;
		}
		return null;
	}

	public int getSecondHeroCount() {
		FiefHeroInfoClient tmp = getFhic();
		if (null != tmp)
			return tmp.getSecondHeroInfos().size();
		else
			return 0;
	}

	public void fiefHeroDel(HeroIdInfoClient myHero) throws GameException {
		FiefHeroInfoClient tmp = getFhic();

		delHero(tmp.getFirstHeroInfos(), myHero);

		addHero(tmp.getSecondHeroInfos(), myHero);

	}

	public void fiefHeroAdd(HeroIdInfoClient myHero) throws GameException {

		// 添加将领到first中
		FiefHeroInfoClient tmp = getFhic();

		addHero(tmp.getFirstHeroInfos(), myHero);

		delHero(tmp.getSecondHeroInfos(), myHero);

	}

	// 添加将领
	public void addHero(List<HeroIdInfoClient> heroIdInfoClients,
			HeroIdInfoClient hic) {
		if (heroIdInfoClients != null) {
			boolean hasMyHero = false;
			for (HeroIdInfoClient hiic : heroIdInfoClients) {
				if (hiic.getId() == hic.getId()) {
					hasMyHero = true;
					break;
				}
			}
			if (!hasMyHero)
				heroIdInfoClients.add(hic);
		}
	}

	// 移除将领
	public void delHero(List<HeroIdInfoClient> heroIdInfoClients,
			HeroIdInfoClient hic) {
		if (heroIdInfoClients != null) {
			for (HeroIdInfoClient hiic : heroIdInfoClients) {
				if (hiic.getId() == hic.getId()) {
					heroIdInfoClients.remove(hiic);
					break;
				}
			}
		}
	}

	public int getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}
	
	public boolean isResource() {
		if (null == prop)
			return false;
		return prop.isResource();
	}

	public static FiefInfoClient convert(FiefInfo fiefInfo)
			throws GameException {
		FiefInfoClient info = new FiefInfoClient();
		BaseFiefInfo bi = fiefInfo.getBi();
		info.setId(bi.getId());
		info.setUserid(bi.getUserid());
		// 之后删除
		if (CacheMgr.holyPropCache.isHoly(bi.getId()))
			info.prop = (FiefProp) CacheMgr.fiefPropCache
					.get(FiefProp.TYPE_WILDERNESS);
		else
			info.prop = (FiefProp) CacheMgr.fiefPropCache.get(bi.getPropid());

		info.setBattleState(bi.getBattleState());
		info.setBattleTime(bi.getBattleTime());
		info.setFhic(FiefHeroInfoClient.convert(bi.getHeroInfo()));
		info.setAttackerId(bi.getAttacker());
		info.setNextExtraBattleTime(bi.getNextExtraBattleTime());
		info.setBattleType(bi.getBattleType());
		// 下面2个值 只有是别人领地才有效
		info.unitCount = bi.getUnitCount();
		info.building = BuildingInfoClient.convert(bi.getBuilding());
		return info;
	}

	/**
	 * 战斗返回数据更新缓存
	 * 
	 * @param info
	 * @throws GameException
	 */
	public void updateCacheFief(BriefFiefInfoClient info) throws GameException {
		if (info == null)
			return;
		info.setId(id);
		info.setProp(prop);
		info.setBuilding(building);
		info.setUserId(userid);
		info.setUnitCount(unitCount);
		info.setBattleState(getBattleState());
		info.setBattleTime(getBattleTime());
		info.setHeroIdInfos(getHeroIdInfos());
		info.setSecondHeroCount(getSecondHeroCount());
		info.setAttackerId(getAttackerId());
		info.setBattleType(getBattleType());
		CacheMgr.fillBriefFief(info);
	}
}
