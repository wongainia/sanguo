package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BUILDING_EFFECT;
import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.protos.BaseManorInfo;
import com.vikings.sanguo.protos.BuildingEffectInfo;
import com.vikings.sanguo.protos.BuildingStatusInfo;
import com.vikings.sanguo.protos.ManorInfo;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ListUtil;

/**
 * 庄园对象
 * 
 * @author susong
 * 
 */
public class ManorInfoClient {

	public static final int POS_EMPTY = 0;

	private long pos; // 建筑位置
	private int scale; // 规模id
	private int curPop; // 当前人口值
	private int totPop; // 总人口值
	private long flag; // 状态buff
	private int update; // 状态上次更新时间
	private String name; // 庄园名字(最多9个汉字啊）
	private List<ArmInfoClient> troopInfo; // 驻兵信息

	// private FiefHeroInfoClient fhic; // 领地-将领信息

	private List<BuildingInfoClient> buildingInfos;// 建筑信息
	private List<BuildingEffectInfo> effectInfos;
	private List<BuildingStatusInfo> statusInfos;

	private FiefScale fiefScale;

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	private int getCurPop() {
		return curPop;
	}

	public void setCurPop(int curPop) {
		this.curPop = curPop;
	}

	public int getTotPop() {
		return totPop;
	}

	public void setTotPop(int totPop) {
		this.totPop = totPop;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		} else {
			troopInfo = new ArrayList<ArmInfoClient>();
		}
		return troopInfo;
	}

	public void setTroopInfo(List<ArmInfoClient> troopInfo) {
		this.troopInfo = troopInfo;
	}

	// 取当前兵力,此兵力为主城中的兵力
	public int getCurArmCount() {
		int curArmCount = 0;
		if (null != troopInfo && !troopInfo.isEmpty()) {
			for (ArmInfoClient info : troopInfo) {
				curArmCount += info.getCount();
			}
		}
		return curArmCount;
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

	public void updateArmFromReturnInfo(ReturnInfoClient rs) {
		List<ArmInfoClient> newArmInfos = new ArrayList<ArmInfoClient>();
		if (!troopInfo.isEmpty())
			for (ArmInfoClient info : rs.getArmInfos()) {
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
		else
			troopInfo.addAll(rs.getArmInfos());
		if (!newArmInfos.isEmpty()) {
			troopInfo.addAll(newArmInfos);
		}
	}

	public List<BuildingInfoClient> getBuildingInfos() {
		return buildingInfos == null ? new ArrayList<BuildingInfoClient>()
				: buildingInfos;
	}

	public BuildingInfoClient getBar() {
		for (BuildingInfoClient it : getBuildingInfos()) {
			if (it.getProp().getType() == BuildingProp.BUILDING_TYPE_BAR)
				return it;
		}
		return null;
	}

	public void setBuildingInfos(List<BuildingInfoClient> buildingInfos) {
		this.buildingInfos = buildingInfos;
	}

	public List<BuildingEffectInfo> getEffectInfos() {
		return effectInfos == null ? new ArrayList<BuildingEffectInfo>()
				: effectInfos;
	}

	public void setEffectInfos(List<BuildingEffectInfo> effectInfos) {
		this.effectInfos = effectInfos;
	}

	public List<BuildingStatusInfo> getStatusInfos() {
		return statusInfos == null ? new ArrayList<BuildingStatusInfo>()
				: statusInfos;
	}

	public void setStatusInfos(List<BuildingStatusInfo> statusInfos) {
		this.statusInfos = statusInfos;
	}

	public FiefScale getFiefScale() {
		return fiefScale;
	}

	public void setFiefScale(FiefScale fiefScale) {
		this.fiefScale = fiefScale;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManorInfoClient other = (ManorInfoClient) obj;
		return pos == other.getPos();
	}

	public static ManorInfoClient convert(ManorInfo manorInfo)
			throws GameException {
		if (null == manorInfo)
			return null;
		ManorInfoClient mic = new ManorInfoClient();
		BaseManorInfo bi = manorInfo.getBi();
		mic.setPos(bi.getPos());
		mic.setScale(bi.getScale());
		mic.setCurPop(bi.getCurPop());
		mic.setTotPop(bi.getTotPop());
		mic.setFlag(bi.getFlag());
		mic.setUpdate(bi.getUpdate());
		mic.setName(bi.getName());
		mic.setTroopInfo(ArmInfoClient.convertList(bi.getTroopInfo()));

		// 组装将领信息
		// FiefHeroInfo fiefHeroInfo = new FiefHeroInfo().setFirstHeroInfo(
		// bi.getFirstHeroInfo()).setSecondHeroInfosList(
		// bi.getSecondHeroInfosList());
		// mic.setFhic(FiefHeroInfoClient.convert(fiefHeroInfo));
		mic.setBuildingInfos(BuildingInfoClient.convertList(bi
				.getBuildingInfosList()));
		mic.setEffectInfos(bi.getEffectInfosList());
		mic.setStatusInfos(bi.getStatusInfosList());
		mic.setFiefScale(CacheMgr.fiefScaleCache.getByScaleValue(bi.getScale(),
				bi.getPos()));
		return mic;
	}

	public void update(ManorInfoClient mic) {
		if (mic == null)
			return;
		setPos(mic.getPos());
		setScale(mic.getScale());
		setCurPop(mic.getCurPop());
		setTotPop(mic.getTotPop());
		setFlag(mic.getFlag());
		setUpdate(mic.getUpdate());
		setName(mic.getName());
		setTroopInfo(mic.getTroopInfo());
		// setFhic(mic.getFhic());
		setBuildingInfos(mic.getBuildingInfos());
		setEffectInfos(mic.getEffectInfos());
		setStatusInfos(mic.getStatusInfos());
		setFiefScale(mic.getFiefScale());
		Account.richFiefCache.updateManor();
	}

	// 判断是否已经建了该种建筑
	public BuildingInfoClient getBuilding(BuildingProp prop) {
		if (null == prop)
			return null;
		for (BuildingInfoClient bic : getBuildingInfos()) {
			if (bic.getProp().getType() == prop.getType())
				return bic;
		}
		return null;

	}

	public BuildingInfoClient getBuilding(int type) {
		for (BuildingInfoClient bic : getBuildingInfos()) {
			if (bic.getProp().getType() == type)
				return bic;
		}
		return null;

	}

	public void updateBuildingInfoClient(BuildingInfoClient bic) {
		if (null == bic)
			return;
		BuildingInfoClient buildingInfoClient = getBuilding(bic.getProp());
		if (null != buildingInfoClient) {
			getBuildingInfos().remove(buildingInfoClient);
			getBuildingInfos().add(bic);
		} else {
			List<BuildingInfoClient> list = getBuildingInfos();
			list.add(bic);
			setBuildingInfos(list);
		}
	}

	/**
	 * 兵力上限
	 * 
	 * @return
	 */
	public int getToplimitArmCount() {
		int count = 0;
		if (null != effectInfos) {
			for (BuildingEffectInfo info : effectInfos) {
				if (info.getId().intValue() == BUILDING_EFFECT.BUILDING_EFFECT_ARM_TOTAL
						.getNumber()) {
					return count += info.getValue();
				}
			}
		}
		return 0;
	}

	public int getNextDraftTime() {
		if (null != statusInfos) {
			for (BuildingStatusInfo info : statusInfos) {
				if (info.getId().intValue() == BUILDING_STATUS.BUILDING_STATUS_DRAFT_CD
						.getNumber()) {
					return info.getStart();
				}
			}
		}
		return (int) (Config.serverTime() / 1000) - 1;
	}

	public int getManorReviveCDStart(int buildingId) {
		if (!ListUtil.isNull(buildingInfos)) {
			for (BuildingInfoClient info : buildingInfos) {
				if (info.getItemId() == buildingId) {
					for (BuildingStatusInfo bsiIt : info.getSis()) {
						if (bsiIt.getId() == BUILDING_STATUS.BUILDING_STATUS_REVIVE_CD
								.getNumber())
							return bsiIt.getStart();
					}
				}
			}
		}
		return 0;
	}

	public BuildingStatusInfo getPopAddStatus() {
		if (null != statusInfos) {
			for (BuildingStatusInfo info : statusInfos) {
				if (info.getId().intValue() == BUILDING_STATUS.BUILDING_STATUS_POP_ADD
						.getNumber()) {
					return info;
				}
			}
		}
		return null;
	}

	/**
	 * 当前人口实际值
	 * 
	 * @return
	 */
	public int getRealCurPop() {
		return getRealCurPop(Account.user.getFood());
	}

	public int getRealCurPop(OtherUserClient ouc) {
		return getRealCurPop(ouc.getFood());
	}

	private int getRealCurPop(int food) {
		int count = curPop;
		BuildingStatusInfo info = getPopAddStatus();
		// 策划需求，必须有粮草，人口才会自动增长。
		if (null != info && food > 0) {
			count += (int) ((((int) (Config.serverTime() / 1000) - info
					.getStart()) / 3600f) * info.getValue() + info
					.getStartValue());
		}
		if (count < 0)
			count = 0;
		return (count > totPop ? totPop : count);
	}

	public int getPopAddSpeed() {
		return getPopAddSpeed(Account.user.getFood());
	}

	public int getPopAddSpeed(OtherUserClient ouc) {
		return getPopAddSpeed(ouc.getFood());
	}

	private int getPopAddSpeed(int food) {
		BuildingStatusInfo info = getPopAddStatus();
		if (null != info && food > 0) {
			return info.getValue();
		} else {
			return 0;
		}
	}

	private int getEffectValue(BUILDING_EFFECT ef) {
		if (null != effectInfos) {
			for (BuildingEffectInfo info : effectInfos) {
				if (info.getId().intValue() == ef.getNumber())
					return info.getValue();
			}
		}
		return 0;
	}

	private List<Integer> getEffectValues(BUILDING_EFFECT ef) {
		List<Integer> list = new ArrayList<Integer>();
		if (null != effectInfos) {
			for (BuildingEffectInfo info : effectInfos) {
				if (info.getId().intValue() == ef.getNumber())
					list.add(info.getValue());
			}
		}
		return list;
	}

	/**
	 * 可以招募的士兵id
	 * 
	 * @return
	 */
	public List<Integer> getArmids() {
		return getEffectValues(BUILDING_EFFECT.BUILDING_EFFECT_ARMID);
	}

	/**
	 * 取城防技能
	 * 
	 * @return
	 */
	public int getDefendSkillId() {
		return getEffectValue(BUILDING_EFFECT.BUILDING_EFFECT_BATTLE_SKILLID);
	}

	/**
	 * 资源点总数
	 * 
	 * @return
	 */
	public int getMaxResource() {
		return getEffectValue(BUILDING_EFFECT.BUILDING_EFFECT_SITE_TOTAL);
	}

	public int getProtectedResourceWeight() {
		return getEffectValue(BUILDING_EFFECT.BUILDING_EFFECT_CELLAR_TOTAL);
	}

	public int getArmHpAdd() {
		return getEffectValue(BUILDING_EFFECT.BUILDING_EFFECT_ARM_HP_ADD);
	}

	// 是否落地
	public boolean isLaydown() {
		return getPos() != 0;
	}

	public BuildingInfoClient getResourceBuilding(BUILDING_STATUS bs) {
		for (BuildingInfoClient b : getBuildingInfos()) {
			if (b.getResourceStatus() == bs.number) {
				return b;
			}
		}
		return null;
	}

	public BuildingInfoClient getBuilding(BUILDING_STATUS bs) {
		for (BuildingInfoClient b : getBuildingInfos()) {
			if (null != b.getStatusInfo(bs))
				return b;
		}
		return null;
	}

	public BuildingInfoClient getSpeedUpBuilding(int attrId) {
		BUILDING_STATUS bs = BUILDING_STATUS.valueOf(attrId + 200);
		if (bs == null)
			return null;
		else
			return getBuilding(bs);
	}

	public BuildingInfoClient getFoodBuilding() {
		return getResourceBuilding(BUILDING_STATUS.BUILDING_STATUS_FOOD_ADD_SPEED);
	}

	public BattleSkill getDefenceSkill() {
		int defenceId = getDefendSkillId();
		if (defenceId <= 0)
			return null;

		try {
			return (BattleSkill) CacheMgr.battleSkillCache.get(defenceId);
		} catch (GameException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getDefenceSkillName() {
		BattleSkill skill = getDefenceSkill();
		if (skill == null)
			return "城防：无";
		else
			return "城防：" + skill.getName();
	}

	// 判断招兵建筑是否有cd (募兵所：BuildingProp.BUILDING_TYPE_ARM_ENROLL)
	public boolean hasDraftCD(int buildingType) {
		BuildingInfoClient bic = getBuilding(buildingType);
		if (null == bic)
			return false;

		return bic.getDraftCd() > 0 ? false : true;
	}

	// 判断是否能招兵
	@SuppressWarnings("unchecked")
	public boolean hasEnoughMaterial(int troopId) {
		try {
			ManorDraft manorDraft = null;
			BuildingProp buildingProp = null;
			List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(troopId);
			for (ManorDraft draft : drafts) {
				BuildingProp prop = (BuildingProp) CacheMgr.buildingPropCache
						.get(draft.getBuildingId());
				BuildingInfoClient bic = Account.manorInfoClient
						.getBuilding(prop);
				if (null != bic
						&& bic.getProp().getId() == draft.getBuildingId()) {
					manorDraft = draft;
					break;
				}
			}

			if (null == manorDraft)
				return false;

			buildingProp = (BuildingProp) CacheMgr.buildingPropCache
					.get(manorDraft.getBuildingId());
			if (null == buildingProp)
				return false;

			BuildingInfoClient bic = getBuilding(buildingProp);
			if (null == bic)
				return false;

			List<ManorDraftResource> resources = CacheMgr.manorDraftResourceCache
					.searchResourceList(troopId, bic.getItemId());

			int countByPop = (int) (Account.manorInfoClient.getRealCurPop() * (manorDraft
					.getResourceDraftRate() / 100f));

			int countByLimit = Account.manorInfoClient.getToplimitArmCount()
					- Account.myLordInfo.getArmCount();
			if (countByLimit < 0)
				countByLimit = 0;

			int count = Math.min(countByPop, countByLimit);

			ReturnInfoClient ric = new ReturnInfoClient();
			for (ManorDraftResource resource : resources) {
				ric.addCfg(
						resource.getResourceType(),
						resource.getValue(),
						(int) (1f * resource.getAmount() * count / Constants.PER_HUNDRED));
			}
			return ric.checkRequire().isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	// 根据人口计算可以训练的数量
	public int getDraftCount(float rate) {
		return (int) (getRealCurPop() * rate);
	}

	// 根据士兵上限计算可以训练的数量
	public int getTrainCountByLimit() {
		int count = getToplimitArmCount() - Account.myLordInfo.getArmCount();
		return (count < 0 ? 0 : count);
	}

	public int getDraftCountByRate(float rate) {
		return Math.min(getDraftCount(rate), getTrainCountByLimit());
	}

	public int getRecoverPopCost() {
		int count = 0;
		BuildingStatusInfo info = getPopAddStatus();
		if (null != info) {
			count = CalcUtil.upNum(getValidPop() / (info.getValue() * 1f))
					* CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 36);
		}
		return count;
	}

	public int getValidPop() {
		return getTotPop() - getRealCurPop();
	}
}
