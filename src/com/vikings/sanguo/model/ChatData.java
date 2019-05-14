package com.vikings.sanguo.model;

public class ChatData {

	protected int userId;
	protected int time;
	protected String msg;

	protected BriefUserInfoClient user;

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

}
