package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class TrainingReward {
	
	private int trainingId;// 引导 ID 数据重复
	private int thingType;// 物品类型
	private int thingId;// 物品ID
	private int amount;// 物品数量
	private int reward;// 引导结束 客户端是否显示的奖励（1显示，0不显示）

	public int getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(int trainingId) {
		this.trainingId = trainingId;
	}

	public int getThingType() {
		return thingType;
	}

	public void setThingType(int thingType) {
		this.thingType = thingType;
	}

	public int getThingId() {
		return thingId;
	}

	public void setThingId(int thingId) {
		this.thingId = thingId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public static TrainingReward fromString(String csv) {
		TrainingReward trainingReward = new TrainingReward();
		StringBuilder buf = new StringBuilder(csv);
		trainingReward.setTrainingId(StringUtil.removeCsvInt(buf));
		trainingReward.setThingType(StringUtil.removeCsvInt(buf));
		trainingReward.setThingId(StringUtil.removeCsvInt(buf));
		trainingReward.setAmount(StringUtil.removeCsvInt(buf));
		trainingReward.setReward(StringUtil.removeCsvInt(buf));
		return trainingReward;
	}

	// 客户端 引导结束是否显示奖励
	public boolean isShowReward() {
		return hasReward() && (reward == 1);
	}

	public boolean hasReward() {
		return (thingType != 0);
	}
}
