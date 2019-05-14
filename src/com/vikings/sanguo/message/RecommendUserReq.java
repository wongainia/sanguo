package com.vikings.sanguo.message;

import java.io.OutputStream;

public class RecommendUserReq extends BaseReq {

	@Override
	public short cmd() {
		return 0;
//		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RECOMMEND_USER;
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
