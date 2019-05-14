package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropTroopDesc {
	private int id;
	private String name;
	private String desc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static PropTroopDesc fromString(String csv) {
		PropTroopDesc propTroopDesc = new PropTroopDesc();
		StringBuilder buf = new StringBuilder(csv);
		propTroopDesc.setId(StringUtil.removeCsvInt(buf));
		propTroopDesc.setName(StringUtil.removeCsv(buf));
		propTroopDesc.setDesc(StringUtil.removeCsv(buf));
		return propTroopDesc;
	}
}
