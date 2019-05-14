package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.protos.OtherGuildInfo;

//家族基本信息（别人的家族信息也是此结构）
public class BaseGuildInfoClient extends BriefGuildInfoClient {
	protected int fiefCount; // 总领地数
	protected String desc; // 家族宗旨 同聊天长度一样
	protected boolean autoJoin;// 是否默认自动申请加入

	public int getFiefCount() {
		return fiefCount;
	}

	public void setFiefCount(int fiefCount) {
		this.fiefCount = fiefCount;
	}

	public String getDesc() {
		return desc == null ? "" : desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin) {
		this.autoJoin = autoJoin;
	}

	public static BaseGuildInfoClient convert(OtherGuildInfo info) {
		if (null == info)
			return null;
		BaseGuildInfoClient gic = new BaseGuildInfoClient();
		gic.setId(info.getId());
		gic.setName(info.getName());
		gic.setImage(info.getImage());
		gic.setLeader(info.getLeader());
		gic.setLevel(info.getLevel());
		gic.setFiefCount(info.getFiefCount());
		gic.setDesc(info.getDesc());
		gic.setCountryId(info.getCountry());
		gic.setCountry(CacheMgr.countryCache.getCountry(info.getCountry()));
		// gic.setAltarId(info.getFiefId());
		gic.setAutoJoin(info.getAutoJoin());
		return gic;
	}
}
