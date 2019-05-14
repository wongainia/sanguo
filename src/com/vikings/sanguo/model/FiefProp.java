package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.utils.StringUtil;

public class FiefProp {
	/* 客户端资源类型 */

	public static final int TYPE_WILDERNESS = 0; // 荒野
	public static final int TYPE_CASTLE = 1; // 荒野

	public static final int TYPE_GOLD = 2;// 金矿
	public static final int TYPE_FARMLAND = 3;// 农田
	public static final int TYPE_FORESTRY = 4;// 林场
	public static final int TYPE_IRON = 5;// 铁矿
	public static final int TYPE_PARK = 6;// 猎园

	public static final int TYPE_RESOURCE = 2; // 《荒野》
	// public static final int TYPE_ALTAR = 3; // 荒野

	public static final int PROP_CASTLE = 1001; // 荒野

	private int id;
	private byte type; // 类型： 0：荒地 1：主城点 2：资源点
	private String name;
	private int productType; // 产出资源类型（0：不产出 1：金矿 2：粮草 3：木材 11-17：**脉
	private String icon;
	private int defenseSkill; // 城防（技能id）
	private int produceSpeed; // 产出速度（每10000秒的个数）
	private int scaleId; // 领地规模（1~9级）
	private int buildingType; // 对应野外建筑type
	private int level;// 领地等级
	private int occupyCost; // pvp占领价格
	private int resType;// 客户端类型（0：荒地 1：主城点 2：金矿 3：农田 4：林场 5：铁矿 6：猎园）
	private int surround;// 野地是否围城（客户端提示中显示，0不需，1需）

	public int getSurround() {
		return surround;
	}

	public boolean hasSurroundTime() {
		return surround == 1;
	}

	public void setSurround(int surround) {
		this.surround = surround;
	}

	public int getResType() {
		return resType;
	}

	public void setResType(int resType) {
		this.resType = resType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getProduceSpeed() {
		return produceSpeed;
	}

	public void setProduceSpeed(int produceSpeed) {
		this.produceSpeed = produceSpeed;
	}

	public int getDefenseSkill() {
		return defenseSkill;
	}

	public void setDefenseSkill(int defenseSkill) {
		this.defenseSkill = defenseSkill;
	}

	public int getScaleId() {
		return scaleId;
	}

	public void setScaleId(int scaleId) {
		this.scaleId = scaleId;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getOccupyCost() {
		return occupyCost;
	}

	public void setOccupyCost(int occupyCost) {
		this.occupyCost = occupyCost;
	}

	/**
	 * 是否主城
	 * 
	 * @return
	 */
	public boolean isCastle() {
		return type == TYPE_CASTLE;
	}

	public boolean isResource() {
		return type == TYPE_RESOURCE;
	}

	public boolean isAltar() {
		return false;
		// return type == TYPE_ALTAR;
	}

	/**
	 * 是否荒地
	 * 
	 * @return
	 */
	public boolean isWasteland() {
		return type == TYPE_WILDERNESS;
	}

	public static FiefProp fromString(String csv) {
		FiefProp fiefProp = new FiefProp();
		StringBuilder buf = new StringBuilder(csv);
		fiefProp.setId(StringUtil.removeCsvInt(buf));
		fiefProp.setType(StringUtil.removeCsvByte(buf));
		fiefProp.setName(StringUtil.removeCsv(buf));
		fiefProp.setProductType(StringUtil.removeCsvInt(buf));
		fiefProp.setIcon(StringUtil.removeCsv(buf));
		fiefProp.setDefenseSkill(StringUtil.removeCsvInt(buf));
		fiefProp.setProduceSpeed(StringUtil.removeCsvInt(buf));
		fiefProp.setScaleId(StringUtil.removeCsvInt(buf));
		fiefProp.setBuildingType(StringUtil.removeCsvInt(buf));
		fiefProp.setLevel(StringUtil.removeCsvInt(buf));
		fiefProp.setOccupyCost(StringUtil.removeCsvInt(buf));
		fiefProp.setResType(StringUtil.removeCsvInt(buf));
		fiefProp.setSurround(StringUtil.removeCsvInt(buf));
		return fiefProp;
	}

	public BUILDING_STATUS getBuildingStatus() {
		switch (productType) {
		case 20:
			return BUILDING_STATUS.BUILDING_STATUS_MONEY_ADD_SPEED;
		case 21:
			return BUILDING_STATUS.BUILDING_STATUS_FOOD_ADD_SPEED;
		case 22:
			return BUILDING_STATUS.BUILDING_STATUS_WOOD_ADD_SPEED;
		case 23:
			return BUILDING_STATUS.BUILDING_STATUS_MATERIAL0_ADD_SPEED;
		case 24:
			return BUILDING_STATUS.BUILDING_STATUS_MATERIAL1_ADD_SPEED;
		default:
			return null;
		}
	}

}
