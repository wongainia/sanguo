package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.protos.MsgReqMachinePlay;

public class MachinePlayReq extends BaseReq {
	MsgReqMachinePlay req;

	public MachinePlayReq(MachinePlayType type) {
		req = new MsgReqMachinePlay().setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MACHINE_PLAY.number;
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
