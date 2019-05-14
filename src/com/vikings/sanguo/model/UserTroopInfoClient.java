package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.UserTroopInfo;

public class UserTroopInfoClient {
	private int id; // 用户id
	private List<ArmInfoClient> infos;// 军队信息

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<ArmInfoClient> getInfos() {
		return infos == null ? new ArrayList<ArmInfoClient>() : infos;
	}

	public void setInfos(List<ArmInfoClient> infos) {
		this.infos = infos;
	}

	public static UserTroopInfoClient convert(UserTroopInfo info) throws GameException {
		if (null == info)
			return null;
		UserTroopInfoClient utic = new UserTroopInfoClient();
		utic.setId(info.getId());
		if (info.hasInfo()) {
			utic.setInfos(ArmInfoClient.convertList(info.getInfo()));
		}
		return utic;
	}
}
