package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EquipmentInsertItem {
	private int id; // 宝石id（即itemId）
	private int type;// 类型
	private byte level;// 等级
	private int itemId; // 拆出物品id
	private int count;// 拆出物品数量
	private int currency;// 拆除价格（元宝）
	private int maxCount;// 相同type能装备的最大数量
	private int upgradeItemId;// 升级需要物品id
	private int upgradeItemCount;// 升级需要物品数量
	private String desc; // 效果描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public int getUpgradeItemId() {
		return upgradeItemId;
	}

	public void setUpgradeItemId(int upgradeItemId) {
		this.upgradeItemId = upgradeItemId;
	}

	public int getUpgradeItemCount() {
		return upgradeItemCount;
	}

	public void setUpgradeItemCount(int upgradeItemCount) {
		this.upgradeItemCount = upgradeItemCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static EquipmentInsertItem fromString(String line) {
		EquipmentInsertItem eii = new EquipmentInsertItem();
		StringBuilder buf = new StringBuilder(line);
		eii.setId(StringUtil.removeCsvInt(buf));
		eii.setType(StringUtil.removeCsvInt(buf));
		eii.setLevel(StringUtil.removeCsvByte(buf));
		eii.setItemId(StringUtil.removeCsvInt(buf));
		eii.setCount(StringUtil.removeCsvInt(buf));
		eii.setCurrency(StringUtil.removeCsvInt(buf));
		eii.setMaxCount(StringUtil.removeCsvInt(buf));
		eii.setUpgradeItemId(StringUtil.removeCsvInt(buf));
		eii.setUpgradeItemCount(StringUtil.removeCsvInt(buf));
		eii.setDesc(StringUtil.removeCsv(buf));
		return eii;
	}
}
