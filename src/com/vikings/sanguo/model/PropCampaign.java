package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropCampaign {
	private int id; // 战役ID（ID分段，普通战役为100001-200000，活动战役为1001-2000，其他战役为2001-10000）
	private int actId; // 所属章节ID
	private String name; // 战役名称
	private String icon; // 战役图标
	private String desc; // 战役描述
	private int openLevel; // 开启等级

	public void setActId(int actId) {
		this.actId = actId;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getActId() {
		return actId;
	}

	public String getDesc() {
		return desc;
	}

	public String getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public boolean isNormalCampaign() {
		return id >= 100001 && id <= 200000;
	}

	public static PropCampaign fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropCampaign campaign = new PropCampaign();
		campaign.setId(StringUtil.removeCsvInt(buf));
		campaign.setActId(StringUtil.removeCsvInt(buf));
		campaign.setName(StringUtil.removeCsv(buf));
		campaign.setIcon(StringUtil.removeCsv(buf));
		campaign.setDesc(StringUtil.removeCsv(buf));
		campaign.setOpenLevel(StringUtil.removeCsvInt(buf));
		return campaign;
	}
}
