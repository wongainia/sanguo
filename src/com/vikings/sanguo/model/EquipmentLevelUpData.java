package com.vikings.sanguo.model;

//计算装备一件升级时用于存储数据
public class EquipmentLevelUpData {
	private short type;
	private int id;
	private int count;
	private String name;
	private String icon;

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
