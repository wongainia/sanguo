package com.vikings.sanguo.model;

public class SyncCtrl {

	public static final byte DATA_CTRL_OP_NONE = 0;
	public static final byte DATA_CTRL_OP_ADD =1 ;
	public static final byte DATA_CTRL_OP_DEL = 2;
	public static final byte DATA_CTRL_OP_REP =3;	//replace
	
	private int ver;
	
	private byte op;

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public byte getOp() {
		return op;
	}
	
	public void setOp(byte op) {
		this.op = op;
	}
	
}
