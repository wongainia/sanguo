package com.vikings.sanguo.model;

public abstract class UserTimeData {
	public static final int TYPE_GUILD_INVITE = 1;
	public static final int TYPE_GUILD_JOIN = 2;
	protected int userId;
	protected int time;
	protected BriefUserInfoClient briefUser;

	protected boolean approve = false;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	public BriefUserInfoClient getBriefUser() {
		return briefUser;
	}

	public void setBriefUser(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserTimeData))
			return false;
		UserTimeData other = (UserTimeData) obj;
		if (getType() != other.getType() || userId != other.userId)
			return false;
		return true;
	}

	public abstract int getType();

}
