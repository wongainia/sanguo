package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 翻牌的配置
 * 
 * @author susong
 * 
 */
public class PokerConfig {
	private int id; // 奖品级别
	private int value;// 奖品价值
	private int troopId;// 兵种
	private int count; // 兵的数量
	private String backImg; // 卡牌的背面

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getTroopId() {
		return troopId;
	}

	public void setTroopId(int troopId) {
		this.troopId = troopId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public static PokerConfig fromString(String cvs) {
		PokerConfig config = new PokerConfig();
		StringBuilder buf = new StringBuilder(cvs);
		config.setId(Integer.valueOf(StringUtil.removeCsv(buf)));
		StringUtil.removeCsv(buf); // 初始概率 跳过
		StringUtil.removeCsv(buf); // 成功后概率 跳过
		config.setValue(Integer.valueOf(StringUtil.removeCsv(buf))); // 奖励价值
		config.setTroopId(Integer.valueOf(StringUtil.removeCsv(buf)));// 得到的兵种类，对应troopProp中的id
		config.setCount(Integer.valueOf(StringUtil.removeCsv(buf)));// 得到的兵数量
		config.setBackImg(StringUtil.removeCsv(buf));
		return config;
	}

}
