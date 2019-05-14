package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class GuildProp {
	private int level; // 等级
	private int reqMinExp; // 最低要求经验
	private int maxMemberCnt; // 最大族员数量
	private int money4LvUp; // 升级消耗金币
	private int rmb4LvUp;// 升级消耗的元宝数
	private int maxInviteCnt; // 最大邀请数量
	private int reqMinVip;// 最低要求的VIP

	public int getReqMinVip() {
		return reqMinVip;
	}

	public void setReqMinVip(int reqMinVip) {
		this.reqMinVip = reqMinVip;
	}

	public int getRmb4LvUp() {
		return rmb4LvUp;
	}

	public void setRmb4LvUp(int rmb4LvUp) {
		this.rmb4LvUp = rmb4LvUp;
	}

	public int getMaxInviteCnt() {
		return maxInviteCnt;
	}

	public void setMaxInviteCnt(int maxInviteCnt) {
		this.maxInviteCnt = maxInviteCnt;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getReqMinExp() {
		return reqMinExp;
	}

	public void setReqMinExp(int reqMinExp) {
		this.reqMinExp = reqMinExp;
	}

	public int getMaxMemberCnt() {
		return maxMemberCnt;
	}

	public void setMaxMemberCnt(int maxMemberCnt) {
		this.maxMemberCnt = maxMemberCnt;
	}

	public int getMoney4LvUp() {
		return money4LvUp;
	}

	public void setMoney4LvUp(int money4LvUp) {
		this.money4LvUp = money4LvUp;
	}

	public static GuildProp fromString(String csv) {
		GuildProp prop = new GuildProp();
		StringBuilder buf = new StringBuilder(csv);
		prop.setLevel(StringUtil.removeCsvInt(buf));
		prop.setReqMinExp(StringUtil.removeCsvInt(buf));
		prop.setMaxMemberCnt(StringUtil.removeCsvInt(buf));
		prop.setMoney4LvUp(StringUtil.removeCsvInt(buf));
		prop.setRmb4LvUp(StringUtil.removeCsvInt(buf));
		prop.setMaxInviteCnt(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		prop.setReqMinVip(StringUtil.removeCsvInt(buf));

		return prop;
	}
}
