package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqTrainingComplete;

public class TrainingCompleteReq extends BaseReq {

	private MsgReqTrainingComplete req;

	public TrainingCompleteReq(int trainingId) {
		req = new MsgReqTrainingComplete().setTrainingId(trainingId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_TRAINING_COMPLETE2.number;
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
