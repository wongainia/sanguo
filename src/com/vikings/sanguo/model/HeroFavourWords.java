package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 宠幸气泡对话框内容
 */
public class HeroFavourWords {

	public static final int FAVOURWORD_ONE = 1;
	public static final int FAVOURWORD_TWO = 2;
	public static final int FAVOURWORD_THREE = 3;

	private int id;// 宠幸气泡对话框
	private int state;// 宠幸条件(1、当前*无宠幸技能* 【进入主界面弹出】
						// 2、宠幸时【进行宠幸操作，兴奋值达100操作按条件3处理】
						// 3、获得宠幸技能【兴奋值到100触发宠幸技时操作】)
	private String favourDesc;// 挑逗性语句

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFavourDesc() {
		return favourDesc;
	}

	public void setFavourDesc(String favourDesc) {
		this.favourDesc = favourDesc;
	}

	public static HeroFavourWords fromString(String csv) {
		HeroFavourWords hfw = new HeroFavourWords();
		StringBuilder sb = new StringBuilder(csv);
		hfw.setId(StringUtil.removeCsvInt(sb));
		hfw.setState(StringUtil.removeCsvInt(sb));
		hfw.setFavourDesc(StringUtil.removeCsv(sb));
		return hfw;
	}

}
