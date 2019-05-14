package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleBgProp;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.utils.ListUtil;

public class BattleBgPropCache extends ArrayFileCache {
	public static final String FILE_NAME = "prop_battle_bg.csv";

	public static final String DEFAULE_BG = "battle_map.jpg";

	@Override
	public long getSearchKey1(Object obj) {
		return ((BattleBgProp) obj).getFiefType();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((BattleBgProp) obj).getCountry();
	}

	@Override
	public Object fromString(String line) {
		return BattleBgProp.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	private String getWall(int type, int country, boolean isAtk) {
		List<BattleBgProp> b = search(type);

		if (ListUtil.isNull(b))
			return "";
		else {
			BattleBgProp propCommon = null; // country = 0，不限
			BattleBgProp prop = null;

			for (BattleBgProp it : b) {
				if (0 == it.getCountry())
					propCommon = it;
				if (country == it.getCountry())
					prop = it;
			}

			if (null != prop) {
				if (prop.getIsShowWall() == 1) {
					if (isAtk) {
						return prop.getBattleWallup();
					} else {
						return prop.getBattleWallDown();
					}
				}
			} else if (null != propCommon) {
				if (propCommon.getIsShowWall() == 1) {
					if (isAtk) {
						return propCommon.getBattleWallup();
					} else {
						return propCommon.getBattleWallDown();
					}
				}
			}
		}
		return "";
	}

	// 左边进攻 是否
	public String getBg(int type, int country, boolean isAtk) {
		List<BattleBgProp> b = search(type);

		if (ListUtil.isNull(b))
			return DEFAULE_BG;
		else {
			BattleBgProp propCommon = null; // country = 0，不限
			BattleBgProp prop = null;

			for (BattleBgProp it : b) {
				if (0 == it.getCountry())
					propCommon = it;
				if (country == it.getCountry())
					prop = it;
			}

			if (null != prop) {
				return prop.getImg();
			} else if (null != propCommon) {
				return propCommon.getImg();
			} else {
				return DEFAULE_BG;
			}
		}
	}

	public String getWall(long fiefId, int fiefPropId, boolean isAtk) {
		int type = 0;
		if (CacheMgr.holyPropCache.isHoly(fiefId)) {
			try {
				HolyProp h = (HolyProp) CacheMgr.holyPropCache.get(fiefId);
				type = h.getType() + 2; // 圣读 3 名称4 重镇 5
			} catch (GameException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FiefProp fp = (FiefProp) CacheMgr.fiefPropCache.get(fiefPropId);
				type = fp.getType();
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

		int p = CacheMgr.zoneCache.getProvince(fiefId);
		return getWall(type, CacheMgr.countryCache.getCountryByProvice(p)
				.getCountryId(), isAtk);
	}

	/**
	 * 取战斗背景
	 * 
	 * @return
	 */
	public String getBattleBg(long fiefId, int fiefPropId, boolean isAtk) {
		int type = 0;
		if (CacheMgr.holyPropCache.isHoly(fiefId)) {
			try {
				HolyProp h = (HolyProp) CacheMgr.holyPropCache.get(fiefId);
				type = h.getType() + 2; // 圣读 3 名称4 重镇 5
			} catch (GameException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FiefProp fp = (FiefProp) CacheMgr.fiefPropCache.get(fiefPropId);
				type = fp.getType();
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

		int p = CacheMgr.zoneCache.getProvince(fiefId);
		return getBg(type, CacheMgr.countryCache.getCountryByProvice(p)
				.getCountryId(), isAtk);
	}
}
