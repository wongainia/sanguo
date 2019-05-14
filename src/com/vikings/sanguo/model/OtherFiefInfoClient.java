package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.OtherFiefInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;

public class OtherFiefInfoClient {

	private long id; // 领地id

	private FiefProp prop;

	private int userId; // 所属领主

	private BuildingInfoClient building;

	private int battleState; // 战场状态（0：没有战争 1：战斗准备,行军 2:围城中，防守方可以突围
								// 3：围城结束，攻击方可以发生战斗 4：战斗结束）
	private int battleTime; // 战场进入下阶段时间

	private List<ArmInfoClient> info; // 兵力情况

	private List<HeroIdInfoClient> heroInfos; // 将领信息

	private List<HeroIdInfoClient> secondHeroInfos; // 待命将领信息

	private UserTroopEffectInfo troopEffectInfo;

	private int attackerId;

	private int nextExtraBattleTime; // 下次圣城副本可进时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public List<ArmInfoClient> getInfo() {
		return info == null ? new ArrayList<ArmInfoClient>() : info;
	}

	public void setInfo(List<ArmInfoClient> info) {
		this.info = info;
	}

	public List<HeroIdInfoClient> getHeroInfos() {
		return heroInfos == null ? new ArrayList<HeroIdInfoClient>()
				: heroInfos;
	}

	public void setHeroInfos(List<HeroIdInfoClient> heroInfos) {
		this.heroInfos = heroInfos;
	}

	public List<HeroIdInfoClient> getSecondHeroInfos() {
		return secondHeroInfos == null ? new ArrayList<HeroIdInfoClient>()
				: secondHeroInfos;
	}

	public void setSecondHeroInfos(List<HeroIdInfoClient> secondHeroInfos) {
		this.secondHeroInfos = secondHeroInfos;
	}

	public int getSecondHeroInfosCount() {
		if (null == secondHeroInfos || secondHeroInfos.isEmpty())
			return 0;
		else
			return secondHeroInfos.size();
	}

	public BuildingInfoClient getBuilding() {
		return building;
	}

	public FiefProp getProp() {
		return prop;
	}

	public void setBuilding(BuildingInfoClient building) {
		this.building = building;
	}

	public void setProp(FiefProp prop) {
		this.prop = prop;
	}

	public int getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}

	public void setTroopEffectInfo(UserTroopEffectInfo troopEffectInfo) {
		this.troopEffectInfo = troopEffectInfo;
	}

	public UserTroopEffectInfo getTroopEffectInfo() {
		return troopEffectInfo;
	}

	public int getNextExtraBattleTime() {
		return nextExtraBattleTime;
	}

	public void setNextExtraBattleTime(int nextExtraBattleTime) {
		this.nextExtraBattleTime = nextExtraBattleTime;
	}

	public static OtherFiefInfoClient convert(OtherFiefInfo info)
			throws GameException {
		if (null == info)
			return null;
		OtherFiefInfoClient ofic = new OtherFiefInfoClient();
		ofic.setId(info.getId());

		// ofic.prop = (FiefProp) CacheMgr.fiefPropCache.get(info.getPropid());

		ofic.setUserId(info.getUserid());
		ofic.building = BuildingInfoClient.convert(info.getBuilding());
		ofic.setBattleState(info.getBattleState());
		ofic.setBattleTime(info.getBattleTime());
		ofic.setAttackerId(info.getAttacker());
		if (info.hasInfo())
			ofic.setInfo(ArmInfoClient.convertList(info.getInfo()));
		if (info.hasFirstHeroInfos())
			ofic.setHeroInfos(HeroIdInfoClient.convert2List(info
					.getFirstHeroInfosList()));
		if (info.hasSecondHeroInfos())
			ofic.setSecondHeroInfos(HeroIdInfoClient.convert2List(info
					.getSecondHeroInfosList()));
		if (info.hasTroopEffectInfo())
			ofic.troopEffectInfo = info.getTroopEffectInfo();

		ofic.setNextExtraBattleTime(info.getNextExtraBattleTime());
		return ofic;
	}
}
