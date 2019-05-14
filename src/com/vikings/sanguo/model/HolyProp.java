package com.vikings.sanguo.model;

import android.util.Log;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.StringUtil;

/**
 * holyprp现在对应holyfief 内置prop
 * 
 * @author chenqing
 * 
 */
public class HolyProp extends FiefScale {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HolyProp() {
	}

	/**
	 * 圣都
	 */
	public static final byte TYPE_CAPITAL = 1;

	/**
	 * 名城
	 */
	public static final byte TYPE_CITY = 2;
	/**
	 * 重镇
	 */
	public static final byte TYPE_TOWN = 3;

	private long fiefId;

	private byte type;

	private int province;

	private int city;

	private HolyPropCfg prop;

	// 非配置中的字段
	private int num = 0;// 战场人数
	private int state = BattleStatus.BATTLE_STATE_NONE;// 战场状态
	private int cdTime = 0;// 战场重置时间 cd 时间
	private BriefFiefInfoClient bFiefInfoClient;

	public BriefFiefInfoClient getbFiefInfoClient() {
		return bFiefInfoClient;
	}

	public void setbFiefInfoClient(BriefFiefInfoClient bFiefInfoClient) {
		this.bFiefInfoClient = bFiefInfoClient;
	}

	public int getCdTime() {
		return cdTime;
	}

	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getFiefId() {
		return fiefId;
	}

	public byte getType() {
		return type;
	}

	public String getAlertTitle() {
		return prop.getAlertTitle();
	}

	public void setAlertTitle(String alertTitle) {
		prop.setAlertTitle(alertTitle);
	}

	public int getPropId() {
		return prop.getPropId();
	}

	public int getCity() {
		return city;
	}

	public int getProvince() {
		return province;
	}

	public void setBonusDesc(String bonusDesc) {
		prop.setBonusDesc(bonusDesc);
	}

	public void setCd(int cd) {
		prop.setCd(cd);
	}

	public void setDesc(String desc) {
		prop.setDesc(desc);
	}

	public String getBonusDesc() {
		return prop.getBonusDesc();
	}

	public int getCd() {
		return prop.getCd();
	}

	public String getDesc() {
		return prop.getDesc();
	}

	public int getLordId() {
		return prop.getLordId();
	}

	public void setDefenseBuff(int defenseBuff) {
		prop.setDefenseBuff(defenseBuff);
	}

	public void setScaleDesc(String scaleDesc) {
		prop.setScaleDesc(scaleDesc);
	}

	public int getDefenseBuff() {
		return prop.getDefenseBuff();
	}

	public String getScaleDesc() {
		return prop.getScaleDesc();
	}

	public HolyPropCfg getProp() {
		return prop;
	}

	public void setProp(HolyPropCfg prop) {
		this.prop = prop;
	}

	public String getName() {
		String name = prop.getName();
		switch (type) {
		case TYPE_CAPITAL:
			break;
		case TYPE_CITY:
			name = Config.arrayValue(province, Config.province) + name;
			break;
		case TYPE_TOWN:
			// name = Config.cityValue(city, province) + name;
			// name = ((CityGeoInfo) CacheMgr.cityGeoInfoCache.search(province,
			// city)).getCityName() + name;
			CityGeoInfo cityGeoInfo = (CityGeoInfo) CacheMgr.cityGeoInfoCache
					.search(province, city);
			if (cityGeoInfo != null) {
				name = cityGeoInfo.getCityName() + name;
			} else {
				Log.e("CityGeoInfo", "province:" + province + "    city:"
						+ city + "   not found!");
			}
			break;
		default:
			break;
		}
		return name;
	}

	// 得到外敌入侵驻兵数量
	public int getTroopCnt() {
		return CacheMgr.holyTroopCache.getSoldierCnt(prop.getPropId());
	}

	// 得到外敌入侵驻将数量
	public int getGeneralCnt() {
		return CacheMgr.holyTroopCache.getGeneralCnt(prop.getPropId());
	}

	public int getDefendTroopCnt() {
		return prop.getTroopCount();
	}

	public int getDefendHeroCnt() {
		return prop.getHeroCount();
	}

	public Country getCountry() {
		int p = CacheMgr.zoneCache.getProvince(fiefId);
		return CacheMgr.countryCache.getCountryByProvice(p);
	}

	public void setName(String name) {
		prop.setName(name);
	}

	public String getIcon() {
		return prop.getIcon();
	}

	public void setIcon(String icon) {
		prop.setIcon(icon);
	}

	public int getLockTime() {
		return prop.getLockTime();
	}

	public void setLockTime(int lockTime) {
		prop.setLockTime(lockTime);
	}

	// public int getNeedFood() {
	// return prop.getNeedFood();
	// }
	//
	// public void setNeedFood(int needFood) {
	// prop.setNeedFood(needFood);
	// }
	//
	// public int getMinBlood() {
	// return prop.getMinBlood();
	// }
	//
	// public void setMinBlood(int minBlood) {
	// prop.setMinBlood(minBlood);
	// }

	public int getMaxReinforceCount() {
		return prop.getMaxReinforceCount();
	}

	public void setMaxReinforceCount(int maxReinforceCount) {
		prop.setMaxReinforceCount(maxReinforceCount);
	}

	public int getTroopRate() {
		return prop.getTroopRate();
	}

	public void setTroopRate(int troopRate) {
		prop.setTroopRate(troopRate);
	}

	public int getErrCode() {
		return prop.getErrCode();
	}

	public void setErrCode(int errCode) {
		prop.setErrCode(errCode);
	}

	public void setLordId(int lordId) {
		prop.setLordId(lordId);
	}

	public String getEvilDoorName() {
		return prop.getEvilDoorName();
	}

	public void setMaxReinforceUser(int maxReinforceUser) {
		prop.setMaxReinforceUser(maxReinforceUser);
	}

	public int getMaxReinforceUser() {
		return prop.getMaxReinforceUser();
	}

	public int getMinArmCountOpenDoor() {
		return prop.getMinArmCountOpenDoor();
	}

	public int getItemId() {
		return prop.getItemId();
	}

	public int getItemCost() {
		return prop.getItemCost();
	}

	public int getItemReinforceCost() {
		return prop.getItemReinforceCost();
	}

	public int getTime() {
		return prop.getTime();
	}

	public int getAttackCountPvP() {
		return prop.getAttackCountPvP();
	}

	public int getDefendCountPvP() {
		return prop.getDefendCountPvP();
	}

	public int getSequence() {
		return prop.getSequence();
	}

	public int getForceAttack() {
		return prop.getForceAttack();
	}

	public int getCategory() {
		return prop.getCategory();
	}

	// 可以占领
	public boolean canOccupied() {
		return (prop.getCanBePlayerOccupied() == 1);
	}

	static public HolyProp fromString(String line) {
		HolyProp hp = new HolyProp();
		StringBuilder buf = new StringBuilder(line);
		hp.fiefId = StringUtil.removeCsvLong(buf);
		try {
			hp.prop = (HolyPropCfg) CacheMgr.holyPropCfgCache.get(StringUtil
					.removeCsvInt(buf));
		} catch (GameException e) {
			e.printStackTrace();
		}

		hp.type = StringUtil.removeCsvByte(buf);
		hp.province = StringUtil.removeCsvInt(buf);
		hp.city = StringUtil.removeCsvInt(buf);
		return hp;
	}
}
