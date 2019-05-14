package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFavoriteFiefDel;

public class FavoriteFiefDelReq extends BaseReq {

	private MsgReqFavoriteFiefDel req;

	public FavoriteFiefDelReq(long fiefid) {
		req = new MsgReqFavoriteFiefDel().setFiefid(fiefid);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FAVORITE_FIEF_DEL.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
