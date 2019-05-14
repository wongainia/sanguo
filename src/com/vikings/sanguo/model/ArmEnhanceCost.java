package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ArmEnhanceCost {

	private int armId;

	private int level;

	private int propId;

	private int type;

	private int costId; // （1对应effectid，2对应物品id）

	private int costAmt;
	
	private int costRmb;//一点强化消耗的元宝

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCostId() {
		return costId;
	}

	public void setCostId(int costId) {
		this.costId = costId;
	}

	public int getCostAmt() {
		return costAmt;
	}

	public void setCostAmt(int costAmt) {
		this.costAmt = costAmt;
	}

	
	
	public int getCostRmb() {
		return costRmb;
	}

	public void setCostRmb(int costRmb) {
		this.costRmb = costRmb;
	}

	public static ArmEnhanceCost fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ArmEnhanceCost ac = new ArmEnhanceCost();
		ac.armId = StringUtil.removeCsvInt(buf);
		ac.level = StringUtil.removeCsvInt(buf);
		ac.propId = StringUtil.removeCsvInt(buf);
		ac.type = StringUtil.removeCsvInt(buf);
		ac.costId = StringUtil.removeCsvInt(buf);
		ac.costAmt = StringUtil.removeCsvInt(buf);
		ac.costRmb=StringUtil.removeCsvInt(buf);
		return ac;
	}

}
