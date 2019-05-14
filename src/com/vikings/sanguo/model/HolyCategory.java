/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-8 上午9:51:02
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.StringUtil;

public class HolyCategory {
	// 圣都
	public  static final int SHENGDU = 1;
	// 名城
	public static final int MINGCHENG = 2;
	// 重镇
	public static final int ZHONGZHEN = 3;
	// 神迹
	public static final int SHENJI = 4;

	private int id; // 按钮分类
	private String btnTxt; // 按钮文字
	private int holyType; // 服务器类型(holy_type.csv)
	private int category; // 客户端类型(prop_holy_AB列)
	private String name;// 名称-外敌入侵界面
	private String icon;// 图标-外敌入侵界面
	private int itemId;// 消耗物品id - 外敌入侵界面
	private int itemCount;// 消耗的物品数量
	private String desc;// 描述 - 外敌入侵界面

	// state 和 time 不是holy_category.csv里面的字段;用于统计外敌入侵的战斗状态
	private int state;// 战场状态
	private int time;// 战场重置时间

	public int getHolyTypeName() {
		if (this.category == SHENJI) {
			return R.drawable.mingzhou;
		}
		switch (this.category) {
		case SHENGDU:
			return R.drawable.shengdu;
		case MINGCHENG:
			return R.drawable.mingzhou;
		case ZHONGZHEN:
			return R.drawable.zhongju;
		default:
			return 0;
		}
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setBtnTxt(String btnTxt) {
		this.btnTxt = btnTxt;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setHolyType(int holyType) {
		this.holyType = holyType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBtnTxt() {
		return btnTxt;
	}

	public int getCategory() {
		return category;
	}

	public int getHolyType() {
		return holyType;
	}

	public int getId() {
		return id;
	}

	public static HolyCategory fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		HolyCategory hc = new HolyCategory();
		hc.setId(StringUtil.removeCsvInt(buf));
		hc.setBtnTxt(StringUtil.removeCsv(buf));
		hc.setHolyType(StringUtil.removeCsvInt(buf));
		hc.setCategory(StringUtil.removeCsvInt(buf));
		hc.setName(StringUtil.removeCsv(buf));
		hc.setIcon(StringUtil.removeCsv(buf));
		hc.setItemId(StringUtil.removeCsvInt(buf));
		hc.setItemCount(StringUtil.removeCsvInt(buf));
		hc.setDesc(StringUtil.removeCsv(buf));
		return hc;
	}
}
