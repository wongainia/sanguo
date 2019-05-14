package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.BlacklistInfo;

/**
 * 
 * @author susong
 * 
 */
public class BlackListInfoClient {
	private BlacklistInfo bli;
	private BriefUserInfoClient user;

	public BlacklistInfo getBli() {
		return bli;
	}

	public void setBli(BlacklistInfo bli) {
		this.bli = bli;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

}
