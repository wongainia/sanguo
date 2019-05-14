package com.vikings.sanguo.model;

import java.io.Serializable;

import com.vikings.sanguo.utils.StringUtil;

//将领品质
public class HeroQuality implements Serializable {

	private static final long serialVersionUID = -3610395121491761831L;

	private byte talent; // 天赋
	private String color; // 文字颜色（品质名称+将领名字）
	private String attrDesc;// 属性及技能描述
	private String image; // 背景图片
	private String name; // 名称
	
	private String imageMajor; //战斗框(主)
	private String imageSecond; //战斗框(副)

	public byte getTalent() {
		return talent;
	}

	public void setTalent(byte talent) {
		this.talent = talent;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAttrDesc() {
		return attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String[] getProfAndSkill() {
		if (null == attrDesc)
			return null;

		String[] str = new String[2];
		int idx = attrDesc.indexOf("|");
		if (idx == -1) {
			str[0] = "";
			str[1] = "";
		} else {
			str[0] = attrDesc.substring(0, idx).trim();
			str[1] = attrDesc.substring(idx + 1).trim();
		}
		return str;
	}
	
	public String getImageMajor() {
		return imageMajor;
	}

	public void setImageMajor(String image) {
		this.imageMajor = image;
	}

	public String getImageSecond() {
		return imageSecond;
	}

	public void setImageSecond(String image) {
		this.imageSecond = image;
	}

	public static HeroQuality fromString(String csv) {
		HeroQuality heroQuality = new HeroQuality();
		StringBuilder buf = new StringBuilder(csv);
		heroQuality.setTalent(StringUtil.removeCsvByte(buf));
		heroQuality.setColor(StringUtil.removeCsv(buf));
		heroQuality.setAttrDesc(StringUtil.removeCsv(buf));
		heroQuality.setImage(StringUtil.removeCsv(buf));
		heroQuality.setName(StringUtil.removeCsv(buf));
		heroQuality.setImageMajor(StringUtil.removeCsv(buf));
		heroQuality.setImageSecond(StringUtil.removeCsv(buf));
		return heroQuality;
	}
}
