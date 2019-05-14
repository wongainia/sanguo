package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.BaseBattleIdInfo;
import com.vikings.sanguo.protos.BattleIdInfo;
import com.vikings.sanguo.protos.OtherUserBattleIdInfo;

public class OtherUserBattleIdInfoClient {
	private int userId;
	private List<BaseBattleIdInfo> infos;

	private BriefUserInfoClient user;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<BaseBattleIdInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<BaseBattleIdInfo> infos) {
		this.infos = infos;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public static OtherUserBattleIdInfoClient convert(OtherUserBattleIdInfo info) {
		OtherUserBattleIdInfoClient oubiic = new OtherUserBattleIdInfoClient();
		if (info != null) {
			oubiic.setUserId(info.getUserid());
			List<BaseBattleIdInfo> list = new ArrayList<BaseBattleIdInfo>();
			for (BattleIdInfo battleIdInfo : info.getInfosList()) {
				list.add(battleIdInfo.getBi());
			}
			oubiic.setInfos(list);
		}
		return oubiic;

	}
}
