package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

//不同战斗援助的玩家数量上限（恶魔之门除外）
public class BattleFief {
	// 领地的propId
	private int propId;
	// 掠夺战援军玩家上限
	private int maxPlunderUser;
	// 占领战援军玩家上限
	private int maxOccupyUser;

	public int getPropId() {
		return propId;
	}

	public void setPropId(int propId) {
		this.propId = propId;
	}

	public int getMaxPlunderUser() {
		return maxPlunderUser;
	}

	public void setMaxPlunderUser(int maxPlunderUser) {
		this.maxPlunderUser = maxPlunderUser;
	}

	public int getMaxOccupyUser() {
		return maxOccupyUser;
	}

	public void setMaxOccupyUser(int maxOccupyUser) {
		this.maxOccupyUser = maxOccupyUser;
	}

	public static BattleFief fromString(String line) {
		BattleFief battleFief = new BattleFief();
		StringBuilder buf = new StringBuilder(line);
		battleFief.setPropId(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		battleFief.setMaxPlunderUser(StringUtil.removeCsvInt(buf));
		battleFief.setMaxOccupyUser(StringUtil.removeCsvInt(buf));
		return battleFief;
	}
}
