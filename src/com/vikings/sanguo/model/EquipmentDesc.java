package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 装备品质客户端显示相关配置
 * 
 * @author susong
 * 
 */
public class EquipmentDesc {
	private byte quality;
	private String name;
	private String color;
	private String image;

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static EquipmentDesc fromString(String line) {
		EquipmentDesc ed = new EquipmentDesc();
		StringBuilder buf = new StringBuilder(line);
		ed.setQuality(StringUtil.removeCsvByte(buf));
		ed.setName(StringUtil.removeCsv(buf));
		ed.setColor(StringUtil.removeCsv(buf));
		ed.setImage(StringUtil.removeCsv(buf));
		return ed;
	}
}
