package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseGuildInfo;
import com.vikings.sanguo.protos.GuildAttrInfo;
import com.vikings.sanguo.protos.GuildInfo;
import com.vikings.sanguo.protos.GuildPositionInfo;

//家族信息(自己的家族信息)
public class GuildInfoClient extends BaseGuildInfoClient {
	public static final int POSITION_ELDER = 1;
	private String announcement; // 公告
	private List<GuildPositionInfo> positionInfos; // 职位信息
	private List<GuildAttrInfo> attrInfos; // 家族属性信息
	private List<BuildingInfoClient> buildingInfos; // 建筑信息

	public String getAnnouncement() {
		return announcement == null ? "" : announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

	public void setPositionInfos(List<GuildPositionInfo> positionInfos) {
		this.positionInfos = positionInfos;
	}

	public List<GuildPositionInfo> getPositionInfos() {
		return positionInfos == null ? new ArrayList<GuildPositionInfo>()
				: positionInfos;
	}

	public void setAttrInfos(List<GuildAttrInfo> attrInfos) {
		this.attrInfos = attrInfos;
	}

	public List<GuildAttrInfo> getAttrInfos() {
		return attrInfos == null ? new ArrayList<GuildAttrInfo>() : attrInfos;
	}

	public void setBuildingInfos(List<BuildingInfoClient> buildingInfos) {
		this.buildingInfos = buildingInfos;
	}

	public List<BuildingInfoClient> getBuildingInfos() {
		return buildingInfos == null ? new ArrayList<BuildingInfoClient>()
				: buildingInfos;
	}

	// 是否是长老
	public boolean isElder(int userId) {
		for (GuildPositionInfo info : getPositionInfos()) {
			if (userId == info.getUserid()
					&& info.getPosition() == POSITION_ELDER)
				return true;
		}
		return false;
	}

	// 废除长老
	public void removeElder(int userId) {
		GuildPositionInfo removeInfo = null;
		for (GuildPositionInfo info : getPositionInfos()) {
			if (userId == info.getUserid()) {
				removeInfo = info;
				break;
			}
		}
		if (null != removeInfo)
			getPositionInfos().remove(removeInfo);
	}

	// 提升长老
	public GuildPositionInfo addElder(int userId) {
		for (GuildPositionInfo info : getPositionInfos()) {
			if (userId == info.getUserid()) {
				info.setPosition(POSITION_ELDER);
				return info;
			}
		}
		GuildPositionInfo info = new GuildPositionInfo();
		info.setUserid(userId);
		info.setPosition(POSITION_ELDER);
		getPositionInfos().add(info);
		return info;
	}

	public static GuildInfoClient convert(GuildInfo info) throws GameException {
		if (null == info)
			return null;
		GuildInfoClient gic = new GuildInfoClient();
		BaseGuildInfo bi = info.getBi();
		gic.setId(bi.getId());
		gic.setName(bi.getName());
		gic.setImage(bi.getImage());
		gic.setLeader(bi.getLeader());
		gic.setLevel(bi.getLevel());
		gic.setFiefCount(bi.getFiefCount());
		gic.setDesc(bi.getDesc());
		gic.setAnnouncement(bi.getAnnouncement());
		gic.setCountryId(bi.getCountry());
		gic.setCountry(CacheMgr.countryCache.getCountry(bi.getCountry()));
		// gic.setAltarId(bi.getFiefid());
		gic.setPositionInfos(bi.getPositionInfosList());
		gic.setAutoJoin(bi.getAutoJoin());
		// gic.setAttrInfos(bi.getAttrInfosList());
		// gic.setBuildingInfos(BuildingInfoClient.convertList(bi
		// .getBuildingInfosList()));
		return gic;
	}
}
