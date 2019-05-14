package com.vikings.sanguo.model;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ConditionInfo;

public class QuestConditionInfoClient {
	private int type; // 任务目标类型
	private int target; // 任务目标值
	private int value; // 任务当前值

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static QuestConditionInfoClient convert(ConditionInfo info)
			throws GameException {
		QuestConditionInfoClient qcic = new QuestConditionInfoClient();
		if (null != info) {
			qcic.setType(info.getType());
			qcic.setTarget(info.getTarget());
			qcic.setValue(info.getValue());
		}
		return qcic;
	}
}
