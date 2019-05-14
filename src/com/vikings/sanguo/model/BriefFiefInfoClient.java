package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.protos.BriefFiefInfo;
import com.vikings.sanguo.protos.UserAttrScoreInfo;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class BriefFiefInfoClient implements Comparable<BriefFiefInfoClient> {

	public static final int REINFORCE_TYPE_INVALID = -1;
	public static final int REINFORCE_TYPE_SELF = 1;
	public static final int REINFORCE_TYPE_GUILD = 2;
	public static final int REINFORCE_TYPE_COUNTRY = 3;

	private long id; // 领地id

	private FiefProp prop;

	private int userId; // 所属领主

	private BuildingInfoClient building;

	private int unitCount; // 总兵力

	private int battleState; // 战场状态（0：没有战争 1：战斗准备,行军 2:围城中，防守方可以突围
								// 3：围城结束，攻击方可以发生战斗 4：战斗结束）
	private int battleTime; // 战场进入下阶段时间

	private List<HeroIdInfoClient> heroIdInfos; // 将领信息

	protected int secondHeroCount; // 待命将领总数

	private Country country; // 国家，有人跟人走

	private Country natureCountry;// 自然的地理位置

	private BriefUserInfoClient lord;

	private int attackerId;

	private BriefUserInfoClient attacker;

	private ManorInfoClient manor;

	private UserAttrScoreInfo userAttrScoreInfo;

	private int battleType;// 战场类型

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

	public List<HeroIdInfoClient> getHeroIdInfos() {
		return heroIdInfos == null ? new ArrayList<HeroIdInfoClient>()
				: heroIdInfos;
	}

	public void setHeroIdInfos(List<HeroIdInfoClient> heroIdInfos) {
		this.heroIdInfos = heroIdInfos;
	}

	// 取主将
	public HeroIdInfoClient getMainHero() {
		if (null != heroIdInfos)
			for (HeroIdInfoClient hic : heroIdInfos) {
				if (hic.isMainHero())
					return hic;
			}

		return null;
	}

	/**
	 * 避免主城兵力和brief不一致，修正brief的值
	 * 
	 * @return
	 */
	public int getUnitCount() {
		if (this.manor != null)
			return this.manor.getCurArmCount();
		else
			return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	public BuildingInfoClient getBuilding() {
		return building;
	}

	public FiefProp getProp() {
		return prop;
	}

	public ManorInfoClient getManor() {
		return manor;
	}

	public void setManor(ManorInfoClient manor) {
		this.manor = manor;
	}

	public UserAttrScoreInfo getUserAttrScoreInfo() {
		return userAttrScoreInfo;
	}

	public void setUserAttrScoreInfo(UserAttrScoreInfo userAttrScoreInfo) {
		this.userAttrScoreInfo = userAttrScoreInfo;
	}

	public int getBattleType() {
		return battleType;
	}

	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}

	// 是否是荒地
	public boolean isWasteland() {
		if (null == prop)
			return true;
		return prop.isWasteland();
	}

	public boolean isCastle() {
		if (null == prop)
			return false;
		return prop.isCastle();
	}

	public boolean isResource() {
		if (null == prop)
			return false;
		return prop.isResource();
	}

	public boolean isHoly() {
		return CacheMgr.holyPropCache.isHoly(id);
	}

	// id 是 holy 则有值
	public HolyProp getHolyProp() throws GameException {
		HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
		return hp;
	}

	public boolean canOccupied() {
		try {
			HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
			return hp.canOccupied();
		} catch (GameException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isOwnAltar() {
		if (null == prop)
			return false;
		return prop.isAltar() && Account.isOwnAltar(userId);
	}

	public boolean isOtherAltar() {
		if (null == prop)
			return false;
		return prop.isAltar() && !Account.isOwnAltar(userId);
	}

	public String getIcon() {
		if (CacheMgr.holyPropCache.isHoly(id)) {
			try {
				HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
				return hp.getIcon();
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		if (!isCastle()) {
			if (building != null)
				return building.getProp().getImage();
			else
				return prop.getIcon();
		} else {
			// 主城清理情况，由于有缓存，所以加入NPC判断
			if (manor == null || BriefUserInfoClient.isNPC(userId))
				return "";
			return getManorFiefIcon(manor.getScale(), id);
		}
	}

	public String getManorFiefIcon(int scale, long id) {
		FiefScale fc = CacheMgr.fiefScaleCache.getByScaleValue(scale, id);
		if (fc == null)
			return "";
		return fc.getIcon();
	}

	public String getManorFiefIconByScaleId(int scaleId) {
		FiefScale fc = CacheMgr.fiefScaleCache.getFiefScale(scaleId, id);
		if (fc == null)
			return "";
		return fc.getIcon();
	}

	public String getName() {
		if (isCastle()) {
			if (manor == null)
				return "主城";
			return getManorFiefName(manor.getScale(), id);
		} else {
			if (CacheMgr.holyPropCache.isHoly(id)) {
				try {
					HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
					return hp.getName();
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
			if (building != null)
				return building.getProp().getBuildingName();
			else
				return prop.getName();
		}
	}

	public String getManorFiefName(int scale, long id) {
		FiefScale fc = CacheMgr.fiefScaleCache.getByScaleValue(scale, id);
		if (fc == null)
			return "主城";
		return fc.getName();
	}

	public String getSimpleName() {
		if (isCastle())
			return "主城";
		else if (isResource())
			return "资源点";
		else if (isHoly()) {
			try {
				HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
				return hp.getScaleDesc();
			} catch (GameException e) {
				return "领地";
			}

		} else {
			return "领地";
		}

	}

	public String getManorFiefNameByScaleId(int scaleId) {
		FiefScale fc = CacheMgr.fiefScaleCache.getFiefScale(scaleId, id);
		if (fc == null)
			return "主城";
		return fc.getName();
	}

	public boolean hasBuilding() {
		return building != null;
	}

	public void setBuilding(BuildingInfoClient building) {
		this.building = building;
	}

	public void setProp(FiefProp prop) {
		this.prop = prop;
	}

	public BriefUserInfoClient getLord() {
		return userId == Account.user.getId() ? Account.user.bref() : lord;
	}

	public void setLord(BriefUserInfoClient lord) {
		this.lord = lord;
	}

	public int getHeroCount() {
		return getSecondHeroCount() + getHeroIdInfos().size();
	}

	public int getSecondHeroCount() {
		return secondHeroCount;
	}

	public void setSecondHeroCount(int secondHeroCount) {
		this.secondHeroCount = secondHeroCount;
	}

	public int getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}

	public BriefUserInfoClient getAttacker() {
		return attacker;
	}

	public void setAttacker(BriefUserInfoClient attacker) {
		this.attacker = attacker;
	}

	public BattleSkill getDefenceSkill() {
		int wallSkillId = getWallBattleSkillId();
		if (wallSkillId <= 0)
			return null;

		try {
			return (BattleSkill) CacheMgr.battleSkillCache.get(wallSkillId);
		} catch (GameException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getDefenceSkillName() {
		BattleSkill skill = getDefenceSkill();
		if (skill == null)
			return "无";
		else
			return skill.getName();
	}

	public BriefFiefInfoClient update(BriefFiefInfoClient info) {
		if (null != info) {
			setId(info.getId());
			setUserId(info.getUserId());
			setUnitCount(info.getUnitCount());
			setBattleState(info.getBattleState());
			setBattleTime(info.getBattleTime());
			setHeroIdInfos(info.getHeroIdInfos());
			setSecondHeroCount(info.getSecondHeroCount());
			setLord(info.getLord());
			setBuilding(info.getBuilding());
		}
		return this;
	}

	public String getCountry() {
		if (country == null || country.getName() == null)
			return "";
		else
			return country.getName();
	}

	public int getCountryId() {
		if (country == null)
			return 0;
		else
			return country.getCountryId();
	}

	public void setCountry() throws GameException {
		if (lord == null)
			return;
		if (BriefUserInfoClient.isNPC(lord.getId())) {
			int p = CacheMgr.zoneCache.getProvince(id);
			country = CacheMgr.countryCache.getCountryByProvice(p);
		} else
			country = CacheMgr.countryCache.getCountry(lord.getCountry());
	}

	public String getNatureCountryName() {
		if (natureCountry == null || natureCountry.getName() == null)
			return "未知区域";
		else
			return natureCountry.getName();
	}

	public Country getNatureCountry() {
		return natureCountry;
	}

	public void setNatureCountry() throws GameException {
		int p = CacheMgr.zoneCache.getProvince(id);
		natureCountry = CacheMgr.countryCache.getCountryByProvice(p);
	}

	// public String getStateString() {
	// int state = TroopUtil.getCurBattleState(getBattleState(),
	// getBattleTime());
	// if (state == BattleStatus.BATTLE_STATE_NONE)
	// return "<font size='11' color='blue'>"
	// + Config.getController().getString(
	// R.string.BaseBriefFiefInfoClient_getStateString_1)
	// + "</font>";
	// else {
	// if (state == BattleStatus.BATTLE_STATE_FINISH) {
	// return "<font size='11' color='red'>"
	// + Config.getController()
	// .getString(
	// R.string.BaseBriefFiefInfoClient_getStateString_2)
	// + "</font>";
	// } else if (state == BattleStatus.BATTLE_STATE_SURROUND) {
	// return "<font size='11' color='blue'>"
	// + Config.getController()
	// .getString(
	// R.string.BaseBriefFiefInfoClient_getStateString_4)
	// + "</font>";
	// } else if (state == BattleStatus.BATTLE_STATE_SURROUND_END) {
	// return "<font size='11' color='blue'>"
	// + Config.getController()
	// .getString(
	// R.string.BaseBriefFiefInfoClient_getStateString_5)
	// + "</font>";
	// }
	// }
	// return "";
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BriefFiefInfoClient other = (BriefFiefInfoClient) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public static BriefFiefInfoClient convert(BriefFiefInfo briefFiefInfo)
			throws GameException {
		BriefFiefInfoClient info = new BriefFiefInfoClient();
		info.setId(briefFiefInfo.getId());

		if (CacheMgr.holyPropCache.isHoly(info.getId())) {
			info.setProp((FiefProp) CacheMgr.fiefPropCache
					.get(FiefProp.TYPE_WILDERNESS));
		} else {
			info.setProp((FiefProp) CacheMgr.fiefPropCache.get(briefFiefInfo
					.getPropid()));
		}
		if (briefFiefInfo.hasBuilding())
			info.setBuilding(BuildingInfoClient.convert(briefFiefInfo
					.getBuilding()));
		info.setUserId(briefFiefInfo.getUserid());
		info.setUnitCount(briefFiefInfo.getUnitCount());
		info.setBattleState(briefFiefInfo.getBattleState());
		info.setBattleTime(briefFiefInfo.getBattleTime());
		if (briefFiefInfo.hasFirstHeroInfos()) {
			info.setHeroIdInfos(HeroIdInfoClient.convert2List(briefFiefInfo
					.getFirstHeroInfosList()));
		}
		info.setSecondHeroCount(briefFiefInfo.getSecondHeroCount());

		info.setAttackerId(briefFiefInfo.getAttacker());
		info.setBattleType(briefFiefInfo.getBattleType());
		return info;
	}

	public static List<BriefFiefInfoClient> convert2List(
			List<BriefFiefInfo> infos) throws GameException {
		List<BriefFiefInfoClient> list = new ArrayList<BriefFiefInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (BriefFiefInfo info : infos)
				list.add(convert(info));
		}
		return list;
	}

	public int compareTo(BriefFiefInfoClient another) {
		int x1 = TileUtil.tileId2x(getId());
		int x2 = TileUtil.tileId2x(another.getId());
		int y1 = TileUtil.tileId2y(getId());
		int y2 = TileUtil.tileId2y(another.getId());
		if (y1 == y2)
			return x1 - x2;
		else
			return y2 - y1;
	}

	public boolean isInBattle() {
		return BattleStatus.isInBattle(battleState);
	}

	private int getReinforceAttack() {
		if (!isInBattle())
			return REINFORCE_TYPE_INVALID;
		if (attackerId == Account.user.getId())
			return REINFORCE_TYPE_SELF;
		// 单挑
		if (battleType == BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber())
			return REINFORCE_TYPE_INVALID;
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();
		// 攻击者是家族成员 领主非家族成员 可以援助进攻
		if (g != null && g.isMember(attackerId) && !g.isMember(userId))
			return REINFORCE_TYPE_GUILD;
		// 攻击者是同国 并且攻击非同国领地 可以援助进攻
		if (attacker != null
				&& attacker.getCountry() == Account.user.getCountry()
				&& ((!BriefUserInfoClient.isNPC(lord.getId()) && (lord
						.getCountry().intValue() != Account.user.getCountry()
						.intValue())) || (isHoly())))
			return REINFORCE_TYPE_COUNTRY;
		return REINFORCE_TYPE_INVALID;
	}

	private int getReinforceDefend() {
		if (!isInBattle())
			return REINFORCE_TYPE_INVALID;
		if (userId == Account.user.getId())
			return REINFORCE_TYPE_SELF;
		// 单挑
		if (battleType == BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber())
			return REINFORCE_TYPE_INVALID;
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();
		// 攻击者非家族成员 领主家族成员 可以援助防守
		if (g != null && !g.isMember(attackerId) && g.isMember(userId))
			return REINFORCE_TYPE_GUILD;
		// 本国土地（主城/领地）受到别国玩家攻击     援助防守应该是防守方不是NPC
		if (attacker != null && !BriefUserInfoClient.isNPC(attackerId)
				&& attacker.getCountry() != Account.user.getCountry()
				&& (getCountryId() == Account.user.getCountry()))
			return REINFORCE_TYPE_COUNTRY;
		return REINFORCE_TYPE_INVALID;
	}

	public boolean canReinforceAttack() {
		return getReinforceAttack() != REINFORCE_TYPE_INVALID;
	}

	public boolean canReinforceDefend() {
		return getReinforceDefend() != REINFORCE_TYPE_INVALID;
	}

	public int getReinforceType() {
		if(isHoly() && canOccupied())   //恶魔之门    是Npc进攻是npc取useid  增援防守方
		{
			if(BriefUserInfoClient.isNPC(getAttackerId()))
			{	
				return getReinforceDefendInEvil();
			}
		}
		
		int type = getReinforceAttack();
		if (type != REINFORCE_TYPE_INVALID)
			return type;
		type = getReinforceDefend();
		if (type != REINFORCE_TYPE_INVALID)
			return type;
		return REINFORCE_TYPE_INVALID;
	}
	
	public int getReinforceTargetId() {
		if(isHoly() && canOccupied())   //恶魔之门    是Npc进攻是npc取useid
		{
			if(BriefUserInfoClient.isNPC(getAttackerId()))
			{			
				return userId;
			}
		}
		if (canReinforceAttack())
			return attackerId;
		if (canReinforceDefend())
			return userId;
		return 0;
	}
	
	//恶魔之门单独处理
	private int getReinforceDefendInEvil() {
		if (!isInBattle())
			return REINFORCE_TYPE_INVALID;
		if (userId == Account.user.getId())
			return REINFORCE_TYPE_SELF;
		// 单挑
		if (battleType == BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber())
			return REINFORCE_TYPE_INVALID;
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();
		// 攻击者非家族成员 领主家族成员 可以援助防守
		if (g != null && !g.isMember(attackerId) && g.isMember(userId))
			return REINFORCE_TYPE_GUILD;
		// 本国土地（主城/领地）受到别国玩家攻击
		if ( BriefUserInfoClient.isNPC(attackerId)
				&& (getCountryId() == Account.user.getCountry()))
			return REINFORCE_TYPE_COUNTRY;
		return REINFORCE_TYPE_INVALID;
	}

	// 是否新手间战争，新手间PVP不能增援
	public boolean isNewerBattle() {
		if (!isInBattle())
			return false;
		if (null == attacker || null == lord)
			return false;
		// 非NPC，新手之间
		if (!attacker.isNPC() && attacker.isNewerProtected() && !lord.isNPC()
				&& lord.isNewerProtected()) {
			return true;
		}
		return false;
	}

	// 领地上发生的战争，是否是自己的战争
	public boolean isMyBattle() {
		if (!isInBattle())
			return false;
		if (attackerId == Account.user.getId()
				|| userId == Account.user.getId())
			return true;

		return false;
	}

	/**
	 * 取领地scale
	 * 
	 * @return
	 */
	public FiefScale getFiefScale() {
		if (isCastle() && manor != null) {
			return CacheMgr.fiefScaleCache
					.getByScaleValue(manor.getScale(), id);
		}

		// // 2014年5月31日16:26:08确认过，保留建筑影响领地规模
		// if (this.building == null) {// 如果无建筑 是fiefporop里配置的scale
		return CacheMgr.fiefScaleCache.getFiefScale(prop.getScaleId(), id);
		// } else {// 有建筑由建筑提供的规模值来确定
		// int scaleValue = CacheMgr.buildingEffectCache
		// .getScaleValue(building.getItemId());
		// return CacheMgr.fiefScaleCache.getByScaleValue(scaleValue, id);
		// }
	}

	public int getFiefLockTime() {
		if (null == getFiefScale())
			return 0;
		return getFiefScale().getLockTime();
	}

	/**
	 * 取领地城防技能
	 * 
	 * @return
	 */
	private int getWallBattleSkillId() {
		if (isCastle() && manor != null) {
			return manor.getDefendSkillId();
		}
		if (CacheMgr.holyPropCache.isHoly(id)) {
			try {
				HolyProp hp = (HolyProp) CacheMgr.holyPropCache.get(id);
				return hp.getDefenseBuff();
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		// 如果无建筑 是fiefporop里配置的
		if (this.building == null) {
			return prop.getDefenseSkill();
		}
		// 有建筑由建筑提供的技能来确定
		else {
			int skillId = CacheMgr.buildingEffectCache
					.getBattleSkillId(building.getItemId());
			if (skillId > 0)
				return skillId;
			else
				return prop.getDefenseSkill();
		}
	}

	public boolean isMyFief() {
		return Account.user.getId() == userId && !isOwnAltar();
	}

	public String getStateIcon() { // BriefFiefInfoClient fief
		int state = TroopUtil.getCurBattleState(getBattleState(),
				getBattleTime());
		if (BattleStatus.isInBattle(state)) {
			if (getUserId() == Account.user.getId()) {
				return "battle_state_defend";
			} else if (getAttackerId() == Account.user.getId()) {
				return "battle_state_attack";
			} else if (canReinforceAttack()) {
				return "battle_state_reinforce_attack";
			} else if (canReinforceDefend()) {
				return "battle_state_reinforce_defend";
			} else {
				return "battle_state_attack";
			}
		} else
			return null;
	}

	public boolean drawFatSheep() {
		if (!isCastle())
			return false;
		if (isInBattle())
			return false;
		return isFatSheep();
	}

	private boolean isFatSheep() {
		if (userAttrScoreInfo != null && userAttrScoreInfo.getHot() != null
				&& userAttrScoreInfo.getHot())
			return true;
		else
			return false;
	}

	public int getFiefDefSkillId() {
		return (null != getDefenceSkill()) ? getDefenceSkill().getId() : -1;
	}

	public boolean isLordNotNPC() {
		return null != getLord()
				&& !BriefUserInfoClient.isNPC(getLord().getId());
	}

	public SiteSpecial getSiteSpecial() {
		SiteSpecial siteSpecial = null;
		if (null != prop && null != building) {
			siteSpecial = (SiteSpecial) CacheMgr.siteSpecialCache.search(
					prop.getId(), building.getItemId());
		}
		return siteSpecial;
	}

	// 已经满产的资源点
	public boolean isResourceFull() {
		if (!isResource())
			return false;
		if (null == building || null == lord || lord.isNPC())
			return false;
		int product = building.produce(lord.getLastLoginTime());
		return product == building.maxStore();
	}

	public boolean hasLord() {
		return null != getLord() && !getLord().isNPC();
	}

	public boolean isLordHasGuild() {
		return hasLord() && getLord().hasGuild();
	}

	public int getPlunderCount(AttrData attrData, AttrType attrType) {
		int plunderCount = 0;

		int count = attrData.getAttr(attrType);
		int weight = CacheMgr.weightCache.getWeight(attrType.getNumber());
		if (weight == 0)
			weight = 1;
		int totalWeight = count * weight;
		int protectWeight = getManor().getProtectedResourceWeight();

		int type = 0;

		if (!isMyFief()
				&& getCountryId() == Account.user.getCountry().intValue()) {
			type = Plunder.MANOR_SAME_COUNTRY;
		} else {
			type = Plunder.MANOR_DIF_COUNTRY;
		}
		Plunder plunder = null;
		try {
			plunder = (Plunder) CacheMgr.plunderCache.get(type);

			if (totalWeight > protectWeight)
				plunderCount = (int) ((plunder.getRate() / 100f)
						* (totalWeight - protectWeight) / weight);
		} catch (GameException e) {
			Log.e("getPlunderCount", e.getMessage());
		}

		return plunderCount;
	}

	public String getAttrDesc(AttrData attrData, AttrType attrType) {
		return "#"
				+ ReturnInfoClient.getAttrTypeIconName(attrType.getNumber())
				+ "#"
				+ (attrData == null ? 0 : CalcUtil
						.turnToTenThousand(getPlunderCount(attrData, attrType)));
	}

	public boolean hasMaxStore() {
		return getBuilding().produce(getLord().getLastLoginTime()) >= getBuilding()
				.maxStore();
	}

	public int getSpeedUpCost() {
		if (null == building)
			return 0;

		BUILDING_STATUS status = BUILDING_STATUS.valueOf(building
				.getResourceStatus());
		int product = building.produce(status, getLord().getLastLoginTime());
		float speed = building.producePerHour(status);
		int maxStore = building.maxStore();

		if (speed == 0 || product >= maxStore)
			return 0;
		else
			return CalcUtil.upNum((maxStore - product) / speed) * 10;
	}

	public void setDefenderHero() throws GameException {
		if (getHeroIdInfos().size() > 0 && getSecondHeroCount() > 0) {
			OtherFiefInfoClient ofic = GameBiz.getInstance()
					.otherFiefInfoQuery(getId());
			if (null != ofic.getHeroInfos()) {
				setHeroIdInfos(ofic.getHeroInfos());
				setSecondHeroCount(ofic.getSecondHeroInfosCount());
			}
		}
	}
}
