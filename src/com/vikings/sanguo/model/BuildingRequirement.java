package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//建筑是否解锁判断条件
public class BuildingRequirement {
	public static final int TYPE_RESOURCES = 1;
	public static final int TYPE_TOOLS = 2; // 道具
	public static final int TYPE_BUILDING = 3; // 建筑
	public static final int TYPE_DUPLICATE = 8;// 副本章节
	public static final int TYPE_VIPS = 9;// vip等级
	public static final int TYPE_WORLD_LEVEL = 10;// 世界等级

	private int buildingId;
	/*
	 * (type, value, extension) 1 2 玩家经验 ;1 3 功勋; 1 4 成就值; 1 6 玩家等级; 1 7 元宝 ;1 8
	 * 家族当前贡献值; 1 9 累计家族贡献值; 1 20 金币; 1 21 粮草; 1 22 木材; 1 23 镔铁; 1 24 皮革;
	 * 
	 * 2 道具id 数量; 3 建筑type 建筑level;
	 * 
	 * 8 副本章节id 无意义填0; 9 VIP等级 无意义填0
	 */
	private int type; // type（1属性、2建筑、3道具、8副本章节、9Vip、10世界等级）
	private int value; // 此value对应User里面的AttrType
	private int extension;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getExtension() {
		return extension;
	}

	public void setExtension(int extension) {
		this.extension = extension;
	}

	public static BuildingRequirement fromString(String csv) {
		BuildingRequirement br = new BuildingRequirement();
		StringBuilder buf = new StringBuilder(csv);
		br.setBuildingId(StringUtil.removeCsvInt(buf));
		br.setType(StringUtil.removeCsvInt(buf));
		br.setValue(StringUtil.removeCsvInt(buf));
		br.setExtension(StringUtil.removeCsvInt(buf));
		return br;
	}
}
