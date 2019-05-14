package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqPlayerWanted;

public class PlayerWantedReq extends BaseReq {

	private MsgReqPlayerWanted req;

	public PlayerWantedReq(int target) {
		req = new MsgReqPlayerWanted().setTarget(target);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLAYER_WANTED
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
