package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.LordReviveInfo;

public class LordReviveInfoClient {
	private List<ArmInfoClient> reviveInfo; // 墓地部队信息
	private List<ArmInfoClient> reviveBossInfo;// boss墓地信息
	private int lastTime;// 上次复活普通士兵时间
	private int count;// 上次复活普通士兵时累计次数

	public List<ArmInfoClient> getReviveInfo() {
		return reviveInfo;
	}

	public void setReviveInfo(List<ArmInfoClient> reviveInfo) {
		this.reviveInfo = reviveInfo;
	}

	public List<ArmInfoClient> getReviveBossInfo() {
		return reviveBossInfo;
	}

	public void setReviveBossInfo(List<ArmInfoClient> reviveBossInfo) {
		this.reviveBossInfo = reviveBossInfo;
	}

	public int getLastTime() {
		return lastTime;
	}

	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static LordReviveInfoClient convert(LordReviveInfo info)
			throws GameException {
		if (null == info)
			return null;
		LordReviveInfoClient lric = new LordReviveInfoClient();
		lric.setReviveInfo(ArmInfoClient.convertTidyList(info.getReviveInfo()));
		lric.setReviveBossInfo(ArmInfoClient.convertList(info
				.getReviveBossInfo()));
		lric.setLastTime(info.getLastTime());
		lric.setCount(info.getCount());
		return lric;
	}
}
