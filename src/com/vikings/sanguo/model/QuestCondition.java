package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class QuestCondition {
	public static final byte FLAG_SHOW = 1;
	public static final byte FLAG_HIDE = 0;

	private int questId;

	private int targetType;
	private int targetValue;
	private int targetCount;

	private String img;

	private byte show;// 界面是否显示图片和子目标（0：不显示，1：显示）
	private String desc;
	private int sequence;// 排序

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public int getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public byte getShow() {
		return show;
	}

	public void setShow(byte show) {
		this.show = show;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public static QuestCondition fromString(String csv) {
		QuestCondition qc = new QuestCondition();
		StringBuilder buf = new StringBuilder(csv);
		qc.setQuestId(StringUtil.removeCsvInt(buf));
		qc.setTargetType(StringUtil.removeCsvInt(buf));
		qc.setTargetValue(StringUtil.removeCsvInt(buf));
		qc.setTargetCount(StringUtil.removeCsvInt(buf));
		qc.setImg(StringUtil.removeCsv(buf));
		qc.setShow(StringUtil.removeCsvByte(buf));
		qc.setDesc(StringUtil.removeCsv(buf));
		qc.setSequence(StringUtil.removeCsvInt(buf));
		return qc;
	}
}
