package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFavoriteFiefAdd;

public class FavoriteFiefAddReq extends BaseReq {

	private MsgReqFavoriteFiefAdd req;

	public FavoriteFiefAddReq(long fiefid) {
		req = new MsgReqFavoriteFiefAdd().setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FAVORITE_FIEF_ADD.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
