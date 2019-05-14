package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 装备定义配置
 * 
 * @author susong
 * 
 */
public class PropEquipment {
	/** 武器 */
	public static final byte TYPE_WEAPON = 1;
	/** 饰品 */
	public static final byte TYPE_ACCESSORIES = 4;
	/** 战甲 */
	public static final byte TYPE_CLOTHES = 3;
	/** 頭盔 */
	public static final byte TYPE_ARMOR = 2;

	private int id;
	private byte type;// 类别1、武器；2、头盔；3、战甲；4、饰品
	private String name;
	private int sell; // 单价（卖出）
	private String icon;// 图标
	private String desc;// 描述
	private byte minQuality2OpenSlot;// 宝石槽开启最低品质
	private int qualityScheme;// 装备品质方案号（equipment_type）
	private int forgeScheme;// 锻造方案号（对应equipment_forge.csv）
	private int levelUpScheme;// 装备升级方案号（对应equipment_levelup.csv）
	private byte coordinates;// 是否套装（0不是、1是）
	private byte exclusive;// 是否将领专属（0不是、1是）
	private byte rating;// 评级
	private String suitName;// 套装名称

	public String getSuitName() {
		return suitName;
	}

	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}

	// 套装 或者 专属套装
	public boolean isSuitEq() {
		return (coordinates == 1 || exclusive == 1);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public static String getTypeName(byte type) {
		String typeName = "";
		switch (type) {
		case TYPE_WEAPON:
			typeName = "武器";
			break;
		case TYPE_ACCESSORIES:
			typeName = "饰品";
			break;
		case TYPE_CLOTHES:
			typeName = "战甲";
			break;
		case TYPE_ARMOR:
			typeName = "头盔";
			break;
		default:
			break;
		}
		return typeName;
	}

	public String getTypeName() {
		return getTypeName(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int sell) {
		this.sell = sell;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public byte getMinQuality2OpenSlot() {
		return minQuality2OpenSlot;
	}

	public void setMinQuality2OpenSlot(byte minQuality2OpenSlot) {
		this.minQuality2OpenSlot = minQuality2OpenSlot;
	}

	public int getQualityScheme() {
		return qualityScheme;
	}

	public void setQualityScheme(int qualityScheme) {
		this.qualityScheme = qualityScheme;
	}

	public int getForgeScheme() {
		return forgeScheme;
	}

	public void setForgeScheme(int forgeScheme) {
		this.forgeScheme = forgeScheme;
	}

	public int getLevelUpScheme() {
		return levelUpScheme;
	}

	public void setLevelUpScheme(int levelUpScheme) {
		this.levelUpScheme = levelUpScheme;
	}

	public boolean isCoordinates() {
		return coordinates == 1;
	}

	public byte getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(byte coordinates) {
		this.coordinates = coordinates;
	}

	public boolean isExclusive() {
		return exclusive == 1;
	}

	public byte getExclusive() {
		return exclusive;
	}

	public void setExclusive(byte exclusive) {
		this.exclusive = exclusive;
	}

	// 得到评定的图片
	public int getRatingPic() {
		switch (getRating()) {
		case 4:
			return R.drawable.icon_wan_mei;
		case 3:
			return R.drawable.icon_zhuo_yue;
		case 2:
			return R.drawable.icon_jing_liang;
		case 1:
			return R.drawable.icon_pu_tong;
		default:
			return R.drawable.icon_pu_tong;
		}
	}

	public byte getRating() {
		return rating;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	public static PropEquipment fromString(String line) {
		PropEquipment prop = new PropEquipment();
		StringBuilder buf = new StringBuilder(line);
		prop.setId(StringUtil.removeCsvInt(buf));
		prop.setType(StringUtil.removeCsvByte(buf));
		prop.setName(StringUtil.removeCsv(buf));
		prop.setSell(StringUtil.removeCsvInt(buf));
		prop.setIcon(StringUtil.removeCsv(buf));
		prop.setDesc(StringUtil.removeCsv(buf));
		prop.setMinQuality2OpenSlot(StringUtil.removeCsvByte(buf));
		prop.setQualityScheme(StringUtil.removeCsvInt(buf));
		prop.setForgeScheme(StringUtil.removeCsvInt(buf));
		prop.setLevelUpScheme(StringUtil.removeCsvInt(buf));
		prop.setCoordinates(StringUtil.removeCsvByte(buf));
		prop.setExclusive(StringUtil.removeCsvByte(buf));
		prop.setRating(StringUtil.removeCsvByte(buf));// TODO 设置评级
		prop.setSuitName(StringUtil.removeCsv(buf));
		return prop;
	}
}
