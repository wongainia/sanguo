package com.vikings.sanguo.model;

public class RechargeCardInfo {
	private BriefUserInfoClient target; // 充值对象
	private int amount; // 充值金额
	private String serial; // 充值卡序列号
	private String pswd; // 充值卡密码
	private byte channel; // 充值渠道

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setChannel(byte channel) {
		this.channel = channel;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public void setTarget(BriefUserInfoClient target) {
		this.target = target;
	}

	// 单位：元
	public int getAmount() {
		return amount;
	}

	public byte getChannel() {
		return channel;
	}

	public String getChannelStr() {
		switch (channel) {
		case RechargeState.ID_CHINA_MOBILE_CARD:
			return "SZX";
		case RechargeState.ID_CHINA_UNICOM_CARD:
			return "UNICOM";
		case RechargeState.ID_CHINA_TELECOM_CARD:
			return "TELECOM";
		default:
			return "unknown";
		}
	}

	public String getCardChannelDesc() {
		switch (channel) {
		case RechargeState.ID_CHINA_MOBILE_CARD:
			return "移动充值卡";
		case RechargeState.ID_CHINA_UNICOM_CARD:
			return "联通充值卡";
		case RechargeState.ID_CHINA_TELECOM_CARD:
			return "电信充值卡";
		default:
			return "未知充值卡";
		}
	}

	public String getPswd() {
		return pswd;
	}

	public String getSerial() {
		return serial;
	}

	public BriefUserInfoClient getTarget() {
		return target;
	}
}
