package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BUILDING_STATUS;
import com.vikings.sanguo.protos.BuildingInfo;
import com.vikings.sanguo.protos.BuildingStatusInfo;

/**
 * 
 * @author susong
 * 
 */
public class BuildingInfoClient implements Serializable {

	private static final long serialVersionUID = 3620571598118511844L;

	private int itemId;
	private List<BuildingStatusInfo> sis; // 建筑状态信息

	private BuildingProp prop;

	public BuildingInfoClient(int itemId) throws GameException {
		this.itemId = itemId;
		prop = (BuildingProp) CacheMgr.buildingPropCache.get(itemId);
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public List<BuildingStatusInfo> getSis() {
		return sis == null ? new ArrayList<BuildingStatusInfo>() : sis;
	}

	public void setSis(List<BuildingStatusInfo> sis) {
		this.sis = sis;
	}

	public BuildingProp getProp() {
		return prop;
	}

	public void setProp(BuildingProp prop) {
		this.prop = prop;
	}

	// 通过用户等级 和 Vip等级判断 是否挂牌
	public boolean canHungLicense(ManorLocation ml) {
		return (Account.user.getLevel() >= ml.getUnlockLevel())
				&& (Account.getCurVip().getLevel() >= ml.getVip());
	}

	public boolean isLocked(ManorLocation ml) {
		return (Account.user.getLevel() < ml.getUnlockLevel())
				|| (Account.getCurVip().getLevel() < ml.getVip());
	}

	// 当前产量（value为产出速度，单位为 :每1秒）
	public int produce(BUILDING_STATUS status, int lastLoginTime) {
		int count = 0;
		for (BuildingStatusInfo info : getSis()) {
			if (info.getId().intValue() == status.getNumber()) {
				count += info.getStartValue()
						+ (int) ((info.getValue().intValue() / 3600f) * getPeriod(
								lastLoginTime, info.getStart()));
			}
		}

		int max = maxStore();
		if (max > 0) {
			count = Math.min(count, max);
		}
		return count;
	}

	public int getPeriod(int lastLoginTime, int startTime) {
		int period = 0;

		// 资源点停止增长的时间
		int seconds = CacheMgr.dictCache.getDictInt(Dict.TYPE_RESOURCE_PRODUCE,
				2);

		if (lastLoginTime == 0) {
			period = (int) (Config.serverTime() / 1000) - startTime;
		} else {
			int login2Now = (int) (Config.serverTime() / 1000) - lastLoginTime;
			if (lastLoginTime > startTime) {
				int start2Login = lastLoginTime - startTime;
				if (login2Now > seconds)
					period = start2Login + seconds;
				else
					period = start2Login + login2Now;
			} else {
				int login2Start = startTime - lastLoginTime;
				if (login2Now > seconds) {
					period = seconds - login2Start;
				} else {
					period = (int) (Config.serverTime() / 1000) - startTime;
				}
			}

		}

		if (period < 0)
			period = 0;

		return period;
	}

	public int produce(int lastLoginTime) {
		return produce(BUILDING_STATUS.valueOf(getResourceStatus()),
				lastLoginTime);
	}

	public int maxStore() {
		try {
			BuildingStore bs = (BuildingStore) CacheMgr.buildingStoreCache
					.get(getItemId());
			return bs.getMaxCount();
		} catch (GameException e) {
			return 0;
		}
	}

	/**
	 * 返回0表示人口增长速度值，其他都表示资源产出增长值
	 */
	public int getResourceStatus() {

		for (BuildingStatusInfo info : getSis()) {
			switch (info.getId().intValue()) {
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				return info.getId().intValue();
			default:
			}
		}
		return 0;

	}

	public String getResourceStatusImg(int statusId) {
		return ReturnInfoClient.getAttrTypeIconName(statusId);
	}

	public String getResourceName(int statusId) {
		return ReturnInfoClient.getAttrTypeName(statusId);
	}

	public boolean isResourceBuilding() {
		if (getResourceStatus() > 0)
			return true;
		else
			return false;
	}

	public BuildingStatusInfo getStatusInfo(BUILDING_STATUS status) {
		if (status == null)
			return null;
		for (BuildingStatusInfo info : getSis()) {
			if (info.getId().intValue() == status.getNumber())
				return info;
		}
		return null;
	}

	// 每小时的产量
	public int producePerHour(BUILDING_STATUS status) {
		for (BuildingStatusInfo info : getSis()) {
			if (info.getId().intValue() == status.getNumber()) {
				return info.getValue().intValue();
			}
		}
		return 0;
	}

	// 取征兵cd
	public BuildingStatusInfo getDraftCdStatus() {
		return getStatusInfo(BUILDING_STATUS.BUILDING_STATUS_DRAFT_CD);
	}

	public int getDraftCd() {
		BuildingStatusInfo info = getDraftCdStatus();
		if (null == info) {
			return 0;
		} else {
			return info.getStart() - (int) (Config.serverTime() / 1000);
		}
	}

	// 取资源加速cd
	public BuildingStatusInfo getResetCdStatus() {
		return getStatusInfo(BUILDING_STATUS.BUILDING_STATUS_RESET_CD);
	}

	public int getResetCd() {
		BuildingStatusInfo info = getResetCdStatus();
		if (null == info) {
			return 0;
		} else {
			return info.getStart() - (int) (Config.serverTime() / 1000);
		}
	}

	/**
	 * 得到资源加速剩余秒数
	 * 
	 * @return
	 */
	public int getResSpeedupTime() {
		BuildingStatusInfo bs = getResSpeedup();
		if (bs == null)
			return -1;
		return bs.getStart() - Config.serverTimeSS();
	}

	public BuildingStatusInfo getResSpeedup() {
		if (!isResourceBuilding())
			return null;
		return getStatusInfo(BUILDING_STATUS.valueOf(getResourceStatus() + 200));
	}

	public BuildingStatusInfo getResSpeed() {
		if (!isResourceBuilding())
			return null;
		return getStatusInfo(BUILDING_STATUS.valueOf(getResourceStatus()));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingInfoClient other = (BuildingInfoClient) obj;
		return itemId == other.getItemId();
	}

	public static BuildingInfoClient convert(BuildingInfo info)
			throws GameException {
		if (null == info)
			return null;
		if (info.getItemid() <= 0)
			return null;
		BuildingInfoClient bic = new BuildingInfoClient(info.getItemid());
		bic.setSis(info.getSisList());
		return bic;
	}

	public static List<BuildingInfoClient> convertList(List<BuildingInfo> infos)
			throws GameException {
		List<BuildingInfoClient> bics = new ArrayList<BuildingInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (BuildingInfo info : infos) {
				bics.add(convert(info));
			}
		}
		return bics;
	}
}
