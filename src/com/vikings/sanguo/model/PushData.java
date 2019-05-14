package com.vikings.sanguo.model;

public class PushData {
	
	// 1:farm info 2:好友添加 3:动一下日志 4:擦肩
	private byte type;
	
	private int time;
	
	private String msg;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
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
	
	
	
}
