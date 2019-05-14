package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropHeroFavour {

	private int id;// 宠幸方式
	private String name;// 宠幸名称
	private String image;// 道具图片

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static PropHeroFavour fromString(String csv) {
		PropHeroFavour hfs = new PropHeroFavour();
		StringBuilder buf = new StringBuilder(csv);
		hfs.setId(StringUtil.removeCsvInt(buf));
		hfs.setName(StringUtil.removeCsv(buf));
		hfs.setImage(StringUtil.removeCsv(buf));
		return hfs;
	}
}
