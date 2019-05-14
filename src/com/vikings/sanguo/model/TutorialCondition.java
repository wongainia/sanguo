package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 任务引导启动的条件
 * 
 * @author susong
 * 
 */
public class TutorialCondition {
	private short questId;
	private short type;

	private int value;
	// 数量、次数等
	private int extend;

	public short getQuestId() {
		return questId;
	}

	public void setQuestId(short questId) {
		this.questId = questId;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getExtend() {
		return extend;
	}

	public void setExtend(int extend) {
		this.extend = extend;
	}

	public static TutorialCondition fromString(String csv) {
		TutorialCondition tc = new TutorialCondition();
		StringBuilder buf = new StringBuilder(csv);
		tc.setQuestId(Short.valueOf(StringUtil.removeCsv(buf)));
		tc.setType(Short.valueOf(StringUtil.removeCsv(buf)));
		tc.setValue(Integer.valueOf(StringUtil.removeCsv(buf)));
		tc.setExtend(Integer.valueOf(StringUtil.removeCsv(buf)));
		return tc;
	}

}
