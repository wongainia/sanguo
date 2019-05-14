package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HolyBattleState;

public class HolyBattleStateClient {

	private long id;// fiefId，也可以认为是battleId
	private int num;// 战场人数
	private int state;// 战场状态
	private int time;// 战场重置时间
	private HolyProp hProp;
	private BriefFiefInfoClient bFiefInfoClient;

	// 获取 重镇 、神迹、名城、圣都 的战斗状态
	public static List<HolyBattleStateClient> getHbscsByType(int HolyType,
			List<HolyBattleStateClient> hbsc) {
		List<HolyBattleStateClient> hbscs = new ArrayList<HolyBattleStateClient>();
		for (HolyBattleStateClient holyBattleStateClient : hbsc) {
			if (holyBattleStateClient.gethProp().getCategory() == HolyType) {
				if (!hbscs.contains(holyBattleStateClient)) {
					hbscs.add(holyBattleStateClient);
				}
			}
		}
		return hbscs;
	}

	// 获取 重镇 、名城、圣都 的战斗情况 除去神迹;服务器已经排好序
	public static List<HolyCategory> fillHolycByhbsc(List<HolyCategory> hcs,
			List<HolyBattleStateClient> hbsc) {
		List<HolyCategory> hcCategories = new ArrayList<HolyCategory>();
		// 移除神迹
		for (HolyCategory hc : hcs) {
			if (hc.getCategory() != HolyCategory.SHENJI
					&& hc.getCategory() != HolyCategory.SHENGDU) {
				hcCategories.add(hc);
			}
		}

		for (HolyCategory hc : hcCategories) {
			for (HolyBattleStateClient holyBattleStateClient : hbsc) {
				if (hc.getCategory() == holyBattleStateClient.gethProp()
						.getCategory()) {
					hc.setState(holyBattleStateClient.getState());
					hc.setTime(holyBattleStateClient.getTime());
					break;
				}
			}
		}

		return hcCategories;

	}

	// 读取配置中的圣都 并且从服务器获取战争状态
	public static List<HolyProp> getBattleHolyProp(
			List<HolyBattleStateClient> hbscBattleStateClients, HolyCategory hc) {
		// 读取配置 取对应的领地
		List<HolyProp> hps = CacheMgr.holyPropCache.getHolyPropsByCategory(hc
				.getCategory());

		// 需要展现的内容
		List<HolyProp> resultFiefs = new ArrayList<HolyProp>();

		for (HolyBattleStateClient holyBattleStateClient : hbscBattleStateClients) {
			for (HolyProp holyProp : hps) {
				if (holyBattleStateClient.getId() == holyProp.getFiefId()) {
					holyProp.setCdTime(holyBattleStateClient.getTime());
					holyProp.setNum(holyBattleStateClient.getNum());
					holyProp.setState(holyBattleStateClient.getState());
					resultFiefs.add(holyProp);
					hps.remove(holyProp);
					break;
				}
			}
		}

		resultFiefs.addAll(hps);
		return resultFiefs;
	}

	public BriefFiefInfoClient getbFiefInfoClient() {
		return bFiefInfoClient;
	}

	public void setbFiefInfoClient(BriefFiefInfoClient bFiefInfoClient) {
		this.bFiefInfoClient = bFiefInfoClient;
	}

	public HolyProp gethProp() {
		return hProp;
	}

	public void sethProp(HolyProp hProp) {
		this.hProp = hProp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public static HolyBattleStateClient convert(HolyBattleState state)
			throws GameException {
		if (state == null)
			return null;
		HolyBattleStateClient hbsc = new HolyBattleStateClient();
		hbsc.setId(state.getId());
		hbsc.setNum(state.getNum());
		hbsc.setState(state.getState());
		hbsc.setTime(state.getTime());
		return hbsc;
	}

	public static List<HolyBattleStateClient> convert2List(
			List<HolyBattleState> states) throws GameException {
		List<HolyBattleStateClient> hbscs = new ArrayList<HolyBattleStateClient>();
		if (null != states && !states.isEmpty()) {
			for (HolyBattleState state : states) {
				HolyBattleStateClient hbsc = convert(state);
				if (null != hbsc) {
					hbsc.sethProp((HolyProp) CacheMgr.holyPropCache.get(hbsc
							.getId()));
					hbscs.add(hbsc);
				}

			}
		}
		return hbscs;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HolyBattleStateClient other = (HolyBattleStateClient) obj;
		if (bFiefInfoClient == null) {
			if (other.bFiefInfoClient != null)
				return false;
		} else if (!bFiefInfoClient.equals(other.bFiefInfoClient))
			return false;
		if (hProp == null) {
			if (other.hProp != null)
				return false;
		} else if (!hProp.equals(other.hProp))
			return false;
		if (id != other.id)
			return false;
		if (num != other.num)
			return false;
		if (state != other.state)
			return false;
		if (time != other.time)
			return false;
		return true;
	}
}
