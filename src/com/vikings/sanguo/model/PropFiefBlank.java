package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropFiefBlank {
	private byte id; // 编号
	private String img; // 美术资源

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public static PropFiefBlank fromString(String line) {
		PropFiefBlank prop = new PropFiefBlank();
		StringBuilder buf = new StringBuilder(line);
		prop.setId(StringUtil.removeCsvByte(buf));
		prop.setImg(StringUtil.removeCsv(buf));
		return prop;
	}
}
