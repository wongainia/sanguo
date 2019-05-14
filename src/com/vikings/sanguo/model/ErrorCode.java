package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class ErrorCode {

	private short id;

	private String msg;

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static ErrorCode fromString(String str) {
		ErrorCode ec = new ErrorCode();
		StringBuilder buf = new StringBuilder(str);
		ec.setId(StringUtil.removeCsvShort(buf));
		ec.setMsg(StringUtil.removeCsv(buf));
		return ec;
	}

}
