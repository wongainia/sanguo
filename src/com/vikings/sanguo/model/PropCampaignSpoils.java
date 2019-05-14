package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//战役奖励信息 
public class PropCampaignSpoils {
	private int id; // 奖励方案（分段ID，副本奖励方案为1-10000，BOSS奖励方案为10001-20000）
	private int type; // 奖励类型（1：固定奖励，2：战绩奖励，3：意外奖励）
	private int assignType; // 分配方式：1、仅主战方获得 2、随机一人获得 3、按出兵比获得 4、每人复制获得
							// 5、按出兵比获得（主战优先）
	private int spoilType; // 掉落类型（1、金币 2、道具 3、玩家经验 4、将领经验 5、俘虏）
	private int spoilId; // 奖品ID（数值奖励填0，物品奖励填对应物品ID）
	private int spoilAmt; // 数量
	private int key; // 无意义，用作key

	public void setAssignType(int assignType) {
		this.assignType = assignType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSpoilAmt(int spoilAmt) {
		this.spoilAmt = spoilAmt;
	}

	public void setSpoilId(int spoilId) {
		this.spoilId = spoilId;
	}

	public void setSpoilType(int spoilType) {
		this.spoilType = spoilType;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAssignType() {
		return assignType;
	}

	public int getId() {
		return id;
	}

	public int getSpoilAmt() {
		return spoilAmt;
	}

	public int getSpoilId() {
		return spoilId;
	}

	public int getSpoilType() {
		return spoilType;
	}

	public int getType() {
		return type;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public static PropCampaignSpoils fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropCampaignSpoils cs = new PropCampaignSpoils();

		cs.setId(StringUtil.removeCsvInt(buf));
		cs.setType(StringUtil.removeCsvInt(buf));
		cs.setAssignType(StringUtil.removeCsvInt(buf));
		cs.setSpoilType(StringUtil.removeCsvInt(buf));
		cs.setSpoilId(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		cs.setSpoilAmt(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		cs.setKey(StringUtil.removeCsvInt(buf));
		return cs;
	}
}
