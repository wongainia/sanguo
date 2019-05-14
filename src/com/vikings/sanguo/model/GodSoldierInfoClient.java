package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GodSoldierInfoClient implements Serializable {

	private static final long serialVersionUID = -6764827055092438569L;

	private long battleId;
	private long uniqueId;

	private List<BaseGodSoldierInfoClient> infos;
	private int times;// 增援次数

	public GodSoldierInfoClient() {
		infos = new ArrayList<BaseGodSoldierInfoClient>();
	}

	public void setBattleId(long battleId) {
		this.battleId = battleId;
	}

	public long getBattleId() {
		return battleId;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<BaseGodSoldierInfoClient> getInfos() {
		return infos;
	}

	public void setInfos(List<BaseGodSoldierInfoClient> infos) {
		this.infos = infos;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void addInfo(BaseGodSoldierInfoClient info) {
		infos.add(0, info);
	}

	// 花费元宝总数
	public int getTotalCost() {
		int total = 0;
		for (BaseGodSoldierInfoClient info : infos) {
			total += info.getCost();
		}
		return total;
	}

	// 神兵总数
	public int getTotalTroop() {
		int total = 0;
		for (BaseGodSoldierInfoClient info : infos) {
			MoveTroopInfoClient mtic = info.getMtic();
			if (null != mtic) {
				for (ArmInfoClient armInfo : mtic.getTroopInfo()) {
					total += armInfo.getCount();
				}
			}
		}
		return total;
	}

	public void clear() {
		infos.clear();
		times = 0;
		uniqueId = 0;
	}

}
