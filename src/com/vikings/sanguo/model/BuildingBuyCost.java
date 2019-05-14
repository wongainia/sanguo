package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//建筑资源条件判断
public class BuildingBuyCost {
	public static final int TYPE_RESOURCES = 1; // 资源
	public static final int TYPE_TOOLS = 2; // 道具

	public static final int ID_MONEY = 1; // 金币
	public static final int ID_FORAGE = 9;// 粮草
	public static final int ID_WOOD = 10;// 木材
	// 11:雪晶脉 12:魂晶脉 13:韵晶脉 14:光晶脉 15:沙晶脉 16:泪晶脉 17:水晶脉

	private int buildingId;
	private byte costType;
	private int costId;
	private int count;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public byte getCostType() {
		return costType;
	}

	public void setCostType(byte costType) {
		this.costType = costType;
	}

	public int getCostId() {
		return costId;
	}

	public void setCostId(int costId) {
		this.costId = costId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static BuildingBuyCost fromString(String str) {
		BuildingBuyCost cost = new BuildingBuyCost();
		StringBuilder buf = new StringBuilder(str);
		cost.setBuildingId(StringUtil.removeCsvInt(buf));
		cost.setCostType(StringUtil.removeCsvByte(buf));
		cost.setCostId(StringUtil.removeCsvInt(buf));
		cost.setCount(StringUtil.removeCsvInt(buf));
		return cost;
	}

}
