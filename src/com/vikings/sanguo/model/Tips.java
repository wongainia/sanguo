package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class Tips {

	private int tipId; //ID号
	private int percent; //概率
	private String desc;  //内容


	public int getTipId() {
		return tipId;
	}

	public void setTipId(int tipId) {
		this.tipId = tipId;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Tips fromString(String str) {
		Tips tip = new Tips();
		StringBuilder buf = new StringBuilder(str);
		tip.setTipId(Integer.parseInt(StringUtil.removeCsv(buf)));
		tip.setPercent(Integer.parseInt(StringUtil.removeCsv(buf)));
		tip.setDesc(StringUtil.removeCsv(buf));
		return tip;
	}

}
