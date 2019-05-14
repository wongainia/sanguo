package com.vikings.sanguo.model;

import java.util.List;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.GuidePosition;
import com.vikings.sanguo.utils.StringUtil;

public class BuildingProp implements Comparable<BuildingProp> {
	public static final int BUILDING_TYPE_MAIN = 5000;
	public static final int BUILDING_TYPE_ARM_ENROLL = 5900; // 募兵所
	public static final int BUILDING_TYPE_BAR = 5901; // 酒馆 红楼
	public static final int BUILDING_TYPE_ARM_ENHANCE = 5902;// 校场
	public static final int BUILDING_TYPE_HERO_CENTER = 5903; // 英雄殿
	public static final int BUILDING_TYPE_SMITHY = 5904; // 铁匠铺
	public static final int BUILDING_TYPE_ARM_DUNWEI = 5420;
	public static final int BUILDING_TYPE_ARM_QISHI = 5430;
	public static final int BUILDING_TYPE_ARM_QIANGBIN = 5440;
	public static final int BUILDING_TYPE_ARM_JIANSHOU = 5450;// 集市
	public static final int BUILDING_TYPE_ARM_MUNIU = 5460;
	public static final int BUILDING_TYPE_JUNHUNJITAN = 5470;
	public static final int BUILDING_TYPE_SCIENCE = 5240;
	public static final int BUILDING_TYPE_REVIVE = 5650;// 医馆
	public static final int BUILDING_TYPE_GUARDIAN = 58001;// 盾兵招募所
	public static final int BUILDING_MINGJU = 5400;// 民居
	public static final int BUILDING_GUOHU = 5010;// 国库
	public static final int BUILDING_TUNTIAN = 5500;// 屯田
	public static final int BUILDING_FENGDI = 5550;// 封地
	public static final int BUILDING_GONGDIAN = 5000;// 宫殿
	public static final int BUILDING_JUNYING = 5600;// 军营

	// 巅峰战场id
	public static final int BUILDING_ID_ARENA = 59911;

	private int id;
	private short mainType;
	private String buildingName;
	private String desc; // 简单描述
	private short level;
	private int type;// 建筑类型
	private int price;// 元宝价格（和资源互斥），如果此字段为0，取building_buy_cost表中配置的资源
	private String image;
	private int sequence; // 列表排序（注：0标示隐藏）
	private String upgradeDesc; // 升级描述
	private int occupyCost;// pvp占领价格
	private int massacreCost;// 屠城价格
	private int checkLv;// 元宝升级是否有等级限制，1没有，0有
	private int currencyBuy;// 是否允许元宝购买，0不可以，1可以

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getMainType() {
		return mainType;
	}

	public void setMainType(short mainType) {
		this.mainType = mainType;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	// public String getFunDesc() {
	// return funDesc;
	// }
	//
	// public void setFunDesc(String funDesc) {
	// this.funDesc = funDesc;
	// }

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getUpgradeDesc() {
		return upgradeDesc;
	}

	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}

	public int getOccupyCost() {
		return occupyCost;
	}

	public void setOccupyCost(int occupyCost) {
		this.occupyCost = occupyCost;
	}

	public int getMassacreCost() {
		return massacreCost;
	}

	public void setMassacreCost(int massacreCost) {
		this.massacreCost = massacreCost;
	}

	public boolean isCheckLv() {
		return currencyBuy == 0 || checkLv != 1;
	}

	public boolean isNoCheckLv() {
		return currencyBuy == 1 && checkLv == 1;
	}

	public int getCheckLv() {
		return checkLv;
	}

	public void setCheckLv(int checkLv) {
		this.checkLv = checkLv;
	}

	public int getCurrencyBuy() {
		return currencyBuy;
	}

	public void setCurrencyBuy(int currencyBuy) {
		this.currencyBuy = currencyBuy;
	}

	public boolean isCurrencyBuy() {
		return currencyBuy != 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingProp other = (BuildingProp) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}

	public static BuildingProp fromString(String csv) {
		BuildingProp building = new BuildingProp();
		StringBuilder buf = new StringBuilder(csv);
		building.setId(StringUtil.removeCsvInt(buf));
		building.setMainType(StringUtil.removeCsvShort(buf));
		building.setBuildingName(StringUtil.removeCsv(buf));
		building.setDesc(StringUtil.removeCsv(buf));
		building.setLevel(StringUtil.removeCsvShort(buf));
		building.setType(StringUtil.removeCsvInt(buf));
		building.setPrice(StringUtil.removeCsvInt(buf));
		building.setImage(StringUtil.removeCsv(buf));
		building.setSequence(StringUtil.removeCsvInt(buf));
		building.setUpgradeDesc(StringUtil.removeCsv(buf));
		building.setOccupyCost(StringUtil.removeCsvInt(buf));
		building.setMassacreCost(StringUtil.removeCsvInt(buf));
		building.setCheckLv(StringUtil.removeCsvInt(buf));
		building.setCurrencyBuy(StringUtil.removeCsvInt(buf));
		return building;
	}

	@Override
	public int compareTo(BuildingProp another) {
		if (another == null)
			return 1;
		if (type != another.type)
			return type - another.type;
		return level - another.level;
	}

	/**
	 * 取下一个等级的建筑，如果没有返回null
	 * 
	 * @return
	 */
	public BuildingProp getNextLevel() {
		BuildingProp next = null;
		List<BuildingProp> list = CacheMgr.buildingPropCache
				.getBuildingsByType(type);
		for (int i = 0; i < list.size(); i++) {
			BuildingProp building = list.get(i);
			if (building.getType() == type && building.getLevel() == level + 1) {
				next = building;
				break;
			}
		}
		return next;
	}

	public BuildingProp getPreLevel() {
		BuildingProp prop = null;
		List<BuildingProp> list = CacheMgr.buildingPropCache
				.getBuildingsByType(type);
		for (int i = 0; i < list.size(); i++) {
			BuildingProp building = list.get(i);
			if (building.getType() == type && building.getLevel() == level - 1) {
				prop = building;
				break;
			}
		}
		return prop;
	}

	public boolean isSameSeries(BuildingProp prop) {
		if (null == prop)
			return false;
		return type == prop.getType();
	}

	public boolean useMaterial() {
		List<BuildingBuyCost> list = CacheMgr.buildingBuyCostCache
				.search(getId());
		return !list.isEmpty();
	}

	// 主城挂牌的默认图片
	public static String getBuildingTypePic(int buildingType) {
		switch (buildingType) {
		case BUILDING_TYPE_ARM_ENROLL:// 募兵所
			return "arm_enroll_manor.png";
		case BUILDING_TYPE_BAR:// 酒馆
			return "bar_manor.png";
		case BUILDING_TYPE_ARM_ENHANCE:// 校场
			return "arm_enhance_manor.png";
		case BUILDING_TYPE_HERO_CENTER:// 英雄殿
			return "hero_center_manor.png";
		case BUILDING_TYPE_SMITHY:// 铁匠铺
			return "smithy_manor.png";
		case BUILDING_TYPE_ARM_JIANSHOU:// 集市
			return "arm_jianshou_manor.png";
		case BUILDING_TYPE_REVIVE:// 医馆
			return "guardian_manor.png";
		case BUILDING_MINGJU:// 民居
			return "mingju_manor.png";
		case BUILDING_GUOHU:// 国库
			return "guoku_manor.png";
		case BUILDING_TUNTIAN:// 屯田
			return "tuntian_manor.png";
		case BUILDING_FENGDI:// 封地
			return "fengdi_manor.png";
		case BUILDING_GONGDIAN:// 宫殿
			return "gongdian_manor.png";
		case BUILDING_JUNYING:// 军营
			return "junying_manor.png";
		default:
			return "null";
		}
	}

}
