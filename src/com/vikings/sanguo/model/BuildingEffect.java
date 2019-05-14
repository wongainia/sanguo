package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 建筑效果
 * 
 * @author susong
 * 
 */
public class BuildingEffect {

	private int buildingId;
	/*
	 * 51 增加人口上限; 53 驻兵数上限增加; 54 仓库储物总重量（具体根据资源重量相除获得，资源间不互斥） ; 55
	 * 地窖保护总重量（具体根据资源重量相除获得，资源间不互斥）; 56 拥有领地总数 ; 57 提供的规模值 ; 58 拥有资源点总数; 59
	 * 墓地容量上限; 60 士兵额外加血; 71 改变资源点金币产出效率（百分比） ; 72 改变资源点粮草产出效率（百分比） ; 73
	 * 改变资源点木材产出效率（百分比）; 74 改变资源点雪晶产出效率（百分比） ; 75 改变资源点魂晶产出效率（百分比） ;101 技能id
	 * 城防技能id; 102 士兵id 可招募士兵;
	 */
	private int effectId;
	private int value;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static BuildingEffect fromString(String csv) {
		BuildingEffect be = new BuildingEffect();
		StringBuilder buf = new StringBuilder(csv);
		be.setBuildingId(Integer.valueOf(StringUtil.removeCsv(buf)));
		be.setEffectId(Integer.valueOf(StringUtil.removeCsv(buf)));
		be.setValue(Integer.valueOf(StringUtil.removeCsv(buf)));
		return be;
	}
}
