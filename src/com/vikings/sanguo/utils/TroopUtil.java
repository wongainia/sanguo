package com.vikings.sanguo.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.ManorDraftResource;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;

public class TroopUtil {

	/**
	 * 行军所需要的粮草
	 * 
	 * @param dstFiefId
	 *            起点的fiefid
	 * @param tarFiefId
	 *            目的地的fifeid
	 * @param troops
	 *            军队<armid, count>
	 * @return
	 */
	public static int costFoodMove(long dstFiefId, long tarFiefId,
			List<ArmInfoClient> troops, List<HeroIdInfoClient> heroIds) {
		if (dstFiefId == tarFiefId
				|| ((null == troops || troops.isEmpty()) && (null == heroIds || heroIds
						.isEmpty())))
			return 0;
		return cost(troops, heroIds, null);

	}

	protected static int cost(List<ArmInfoClient> troops,
			List<HeroIdInfoClient> heroIds, FiefScale scale) {
		int count = 0;
		if (null != troops && !troops.isEmpty()) {
			for (ArmInfoClient armInfo : troops) {
				TroopProp prop = armInfo.getProp();
				if (null != prop)
					count += CalcUtil.upNum(armInfo.getCount()
							* (prop.getFood() / 1000f));

			}
		}

		if (null != heroIds && !heroIds.isEmpty()) {
			for (HeroIdInfoClient hiic : heroIds) {
				HeroProp prop = hiic.getHeroProp();
				if (prop != null)
					count += CalcUtil.upNum(prop.getFood());
			}

		}

		if (null != scale)
			count += CalcUtil.upNum(count * (scale.getNeedFood() / 100f));

		return modify(count);
	}

	// 士兵常规消耗
	public static int costFood(List<ArmInfoClient> troops) {
		int total = 0;
		if (null != troops)
			for (ArmInfoClient ai : troops) {
				total = total + ai.getProp().getFoodConsume() * ai.getCount()
						/ 1000;
			}
		return total;
	}

	// 消耗不超过上限
	private static int modify(int count) {
		int max = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 5);
		return (count > max ? max : count);
	}

	/**
	 * 粮草消耗，行军 +围城
	 * 
	 * @param dstFiefId
	 * @param tarFiefId
	 * @param troops
	 * @param scale
	 * @return
	 */
	public static int costFood(long dstFiefId, long tarFiefId,
			List<ArmInfoClient> troops, List<HeroIdInfoClient> heroIds,
			FiefScale scale) {
		if (dstFiefId == tarFiefId
				|| ((null == troops || troops.isEmpty()) && (null == heroIds || heroIds
						.isEmpty())))
			return 0;
		return cost(troops, heroIds, scale);
	}

	/**
	 * 取最新的战斗状态
	 * 
	 * @param state
	 * @param time
	 * @return
	 */
	public static int getCurBattleState(int state, int time) {
		if (state == BattleStatus.BATTLE_STATE_SURROUND) {
			if ((int) (Config.serverTime() / 1000) - time >= 0) {
				return BattleStatus.BATTLE_STATE_SURROUND_END;
			} else {
				return state;
			}
		} else if (state == BattleStatus.BATTLE_STATE_FINISH) {
			if ((int) (Config.serverTime() / 1000) - time >= 0) {
				return BattleStatus.BATTLE_STATE_NONE;
			} else {
				return state;
			}
		} else {
			return state;
		}
	}

	/**
	 * 战争到下一个阶段时间， 只有行军（1），和 围城（2）两个阶段有效
	 * 
	 * @param state
	 * @param time
	 * @param fiefScale
	 * @return
	 */
	public static int get2NextBattleStateTime(int state, int time,
			FiefScale fiefScale) {
		if (state == BattleStatus.BATTLE_STATE_SURROUND) {
			if ((int) (Config.serverTime() / 1000) - time >= 0) {
				return 0;
			} else {
				return time - (int) (Config.serverTime() / 1000);
			}
		} else if (state == BattleStatus.BATTLE_STATE_FINISH) {
			if ((int) (Config.serverTime() / 1000) - time >= 0) {
				return 0;
			} else {
				return time - (int) (Config.serverTime() / 1000);
			}
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param moveFood
	 * @return
	 */
	public static int costRmbMove(int moveFood) {
		int param = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 32);
		if (param == 0)
			param = 60;
		return CalcUtil.upNum(moveFood / (1f * param));
	}

	/**
	 * 判断进攻时的兵力是否满足数量比例 比例配置在dict（1409,8）
	 * 
	 * @param otherCount
	 * @return
	 */
	public static long minArmCntPVP(long otherCount) {
		return CalcUtil
				.upLongNum(otherCount
						* (CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST,
								8) / 100f));
	}

	public static long minArmCntPVE(long otherCount) {
		return CalcUtil
				.upLongNum(otherCount
						* (CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST,
								10) / 100f));
	}

	public static int troopBlood(List<ArmInfoClient> armInfos) {
		if (ListUtil.isNull(armInfos)) {
			return 0;
		}
		int total = 0;
		for (ArmInfoClient armInfo : armInfos) {
			total += armInfo.getProp().getHp() * armInfo.getCount();
		}
		return total;
	}

	public static String getPrice(int armId, int buildingId) {
		StringBuffer buf = new StringBuffer("单价: ");
		List<ShowItem> showItems = getShowRequire(armId, buildingId);

		DecimalFormat format = new DecimalFormat("#.#");

		for (ShowItem showItem : showItems) {
			buf.append("#"
					+ showItem.getImg()
					+ "#"
					+ format.format(showItem.getCount()
							/ Constants.PER_TEN_THOUSAND));
		}
		return buf.toString();
	}

	public static int getPrices(int armId, int buildingId) {
		List<ShowItem> showItems = getShowRequire(armId, buildingId);
		if (ListUtil.isNull(showItems)) {
			return 0;
		}
		return (int) (showItems.get(0).getCount() / Constants.PER_HUNDRED);
	}

	public static String getCost(int armId, int buildingId, int count) {
		StringBuffer buf = new StringBuffer("总价: ");
		List<ShowItem> showItems = getShowRequire(armId, buildingId);

		for (ShowItem showItem : showItems) {
			buf.append("#"
					+ showItem.getImg()
					+ "#"
					+ CalcUtil.upNum(showItem.getCount() * count
							/ Constants.PER_TEN_THOUSAND) + "   ");
		}
		return buf.toString();
	}

	private static List<ShowItem> getShowRequire(int armId, int buildingId) {
		List<ManorDraftResource> list = CacheMgr.manorDraftResourceCache
				.searchResourceList(armId, buildingId);
		ReturnInfoClient ric = new ReturnInfoClient();
		for (ManorDraftResource resource : list) {
			ric.addCfg(resource.getResourceType(), resource.getValue(),
					resource.getAmount());
		}
		List<ShowItem> showItems = ric.showRequire();
		return showItems;
	}

	public static int countArm(List<ArmInfoClient> ls) {
		int count = 0;
		if (ListUtil.isNull(ls))
			return 0;
		for (ArmInfoClient a : ls) {
			count += a.getCount();
		}
		return count;
	}

	public static int getMassacreCost(ManorInfoClient mic) {
		int cost = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_MASSACRE, 4);
		// 价格
		if (null != mic) {
			List<BuildingInfoClient> bics = mic.getBuildingInfos();
			for (BuildingInfoClient bic : bics) {
				if (null != bic.getProp())
					cost += bic.getProp().getMassacreCost();
			}
		}
		cost = Math.min(cost,
				CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_MASSACRE, 5));
		return cost;
	}

	public static <T> List<T> getList(T[] objects) {
		List<T> objects2 = new ArrayList<T>();
		if (objects == null)
			return objects2;
		for (T object : objects) {
			objects2.add(object);
		}
		return objects2;
	}
}
