package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BuildingEffectInfo;
import com.vikings.sanguo.protos.LordFiefInfo;

public class LordFiefInfoClient {

	private long fiefId;

	private List<ArmInfoClient> troopInfo; // 驻兵信息
	private BuildingInfoClient buildingInfo;// 建筑信息
	private List<BuildingEffectInfo> effectInfos;

	private LordFiefInfoClient() {
	}

	public LordFiefInfoClient(long id) {
		this.fiefId = id;
	}

	public long getFiefId() {
		return fiefId;
	}

	public List<ArmInfoClient> getTroopInfo() {
		return troopInfo;
	}

	public void setTroopInfo(List<ArmInfoClient> troopInfo) {
		this.troopInfo = troopInfo;
	}

	public BuildingInfoClient getBuildingInfo() {
		return buildingInfo;
	}

	public List<BuildingEffectInfo> getEffectInfos() {
		return effectInfos;
	}

	public static LordFiefInfoClient convert(LordFiefInfo lordFiefInfo)
			throws GameException {
		if (lordFiefInfo == null)
			return null;
		LordFiefInfoClient c = new LordFiefInfoClient();
		c.fiefId = lordFiefInfo.getBi().getFiefid();
		c.troopInfo = ArmInfoClient.convertList(lordFiefInfo.getBi().getTroopInfo());
		if (lordFiefInfo.hasBi() && lordFiefInfo.getBi().hasBuildingInfos())
			c.buildingInfo = BuildingInfoClient.convert(lordFiefInfo.getBi()
					.getBuildingInfos());
		c.effectInfos = lordFiefInfo.getBi().getEffectInfosList();
		return c;
	}

}
