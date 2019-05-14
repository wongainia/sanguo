package com.vikings.sanguo.message;

import java.io.OutputStream;

public class TrainingEndReq extends BaseReq {

	@Override
	public short cmd() {
		return 0;
//		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_TRAINING_COMPLETE;
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
