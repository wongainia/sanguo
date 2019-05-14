package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroRecruitExchange {
	public static final byte EXCHANGE_JIA = 1;
	public static final byte EXCHANGE_YI = 2;
	public static final byte EXCHANGE_BING = 3;
	public static final byte EXCHANGE_DING = 4;

	private int itemId; // 所需将魂ID

	private int schemaId;// 武将生成方案号

	private int amount;

	private byte vipLevel;// 条件(Vip)

	private byte tab;// 位置(1、甲； 2、乙；3、丙；4、丁)

	private int sequence;// 排序

	private String soulSource;// 将魂来源

	public String getSoulSource() {
		return soulSource;
	}

	public void setSoulSource(String soulSource) {
		this.soulSource = soulSource;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(int schemaId) {
		this.schemaId = schemaId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public byte getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(byte vipLevel) {
		this.vipLevel = vipLevel;
	}

	public byte getTab() {
		return tab;
	}

	public void setTab(byte tab) {
		this.tab = tab;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public static HeroRecruitExchange fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		HeroRecruitExchange exchange = new HeroRecruitExchange();
		exchange.setSchemaId(StringUtil.removeCsvInt(buf));
		exchange.setItemId(StringUtil.removeCsvInt(buf));
		exchange.setAmount(StringUtil.removeCsvInt(buf));
		exchange.setVipLevel(StringUtil.removeCsvByte(buf));
		exchange.setTab(StringUtil.removeCsvByte(buf));
		exchange.setSequence(StringUtil.removeCsvInt(buf));
		exchange.setSoulSource(StringUtil.removeCsv(buf));
		return exchange;
	}
}
