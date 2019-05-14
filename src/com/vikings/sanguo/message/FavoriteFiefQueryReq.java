package com.vikings.sanguo.message;

import java.io.OutputStream;

public class FavoriteFiefQueryReq extends BaseReq {

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FAVORITE_FIEF_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {

	}

}
