package com.vikings.sanguo.message;

import com.vikings.sanguo.utils.BytesUtil;

public class MsgHeader {

	private int len;

	private short cmd;

	private static int size = (2+4);
	
	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

	public int size(){
		return size;
	}
	
//	void writeBytes(OutputStream out){
//		BytesUtil.putInt(out, len);
//		BytesUtil.putShort(out, cmd);
//	}
	
	public void fromBytes(byte[] buf){
		this.len = BytesUtil.getInt(buf, 0);
		this.cmd = BytesUtil.getShort(buf, 4);
	}
}
