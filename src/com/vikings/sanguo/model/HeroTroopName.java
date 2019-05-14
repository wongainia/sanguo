package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroTroopName {
	private int type; // 统率类型（对应TroopProp中的type）
	private String slug;// 缩略名
	private String name;// 名称
	private String smallIcon;// 小图标

	public HeroTroopName() {
	}

	public HeroTroopName getThis() {
		return this;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSlug() {
		return slug;
	}

	public HeroTroopName setSlug(String slug) {
		this.slug = slug;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public static HeroTroopName fromString(String csv) {
		HeroTroopName heroTroopName = new HeroTroopName();
		StringBuilder buf = new StringBuilder(csv);
		heroTroopName.setType(StringUtil.removeCsvInt(buf));
		heroTroopName.setSlug(StringUtil.removeCsv(buf));
		heroTroopName.setName(StringUtil.removeCsv(buf));
		heroTroopName.setSmallIcon(StringUtil.removeCsv(buf));
		return heroTroopName;
	}
}
