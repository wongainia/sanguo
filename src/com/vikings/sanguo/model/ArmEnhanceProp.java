package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ArmEnhanceProp {

	private int armId;

	private int level;

	private int propId;

	private int vaule;

	private int totalExp;

	private int sequence;

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

	public int getVaule() {
		return vaule;
	}

	public void setVaule(int vaule) {
		this.vaule = vaule;
	}

	public int getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(int totalExp) {
		this.totalExp = totalExp;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public static ArmEnhanceProp fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		ArmEnhanceProp ap = new ArmEnhanceProp();
		ap.armId = StringUtil.removeCsvInt(buf);
		ap.level = StringUtil.removeCsvInt(buf);
		ap.propId = StringUtil.removeCsvInt(buf);
		ap.vaule = StringUtil.removeCsvInt(buf);
		ap.totalExp = StringUtil.removeCsvInt(buf);
		ap.sequence = StringUtil.removeCsvInt(buf);
		return ap;
	}

}
