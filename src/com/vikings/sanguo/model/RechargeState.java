package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class RechargeState {
	public static final byte ID_ALIPAY = 0; // 支付宝
	public static final byte ID_CHINA_MOBILE_CARD = 1; // 移动充值卡
	public static final byte ID_CHINA_UNICOM_CARD = 2; // 联通充值卡
	public static final byte ID_CHINA_TELECOM_CARD = 3;// 电信充值卡
	public static final byte ID_CHINA_MOBILE_SM = 4;// 移动短信
	public static final byte ID_CHINA_TELCOM_SM = 5;// 电信短信
	public static final byte ID_UNIONPAY = 6; // 银联
	public static final byte ID_TRANSFER_PAY = 7; // 转账汇款
	public static final byte ID_CREDIT_CARD = 8;// 银联信用卡
	public static final byte ID_GAMECOMB = 126;// 移动短信

	private byte id; // 渠道id
	private String name;// 渠道名称
	private String icon;// 图标
	private byte type;// 充值类型（1、短信充值；2、有奖充值）
	private byte show;// 是否显示（0隐藏，1显示）
	private byte available;// 状态（0未开通，1开通）
	private String msg; // 点击提示文字，只有未开通时有效
	private String label; // 状态脚标
	private int rate;// 额外奖励 基数0
	private byte sequence;// 排序

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
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

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getShow() {
		return show;
	}

	public void setShow(byte show) {
		this.show = show;
	}

	public byte getAvailable() {
		return available;
	}

	public void setAvailable(byte available) {
		this.available = available;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public byte getSequence() {
		return sequence;
	}

	public void setSequence(byte sequence) {
		this.sequence = sequence;
	}

	public boolean isShow() {
		return show == 0;
	}

	public boolean isClose() {
		return available == 0;
	}

	public static RechargeState fromString(String line) {
		RechargeState rs = new RechargeState();
		StringBuilder buf = new StringBuilder(line);
		rs.setId(StringUtil.removeCsvByte(buf));
		rs.setName(StringUtil.removeCsv(buf));
		rs.setIcon(StringUtil.removeCsv(buf));
		rs.setType(StringUtil.removeCsvByte(buf));
		rs.setShow(StringUtil.removeCsvByte(buf));
		rs.setAvailable(StringUtil.removeCsvByte(buf));
		rs.setMsg(StringUtil.removeCsv(buf));
		rs.setLabel(StringUtil.removeCsv(buf));
		rs.setRate(StringUtil.removeCsvInt(buf));
		rs.setSequence(StringUtil.removeCsvByte(buf));
		return rs;
	}
}
