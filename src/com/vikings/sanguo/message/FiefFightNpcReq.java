package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqFiefFightNpc;

public class FiefFightNpcReq extends BaseReq {

	private MsgReqFiefFightNpc req;

	public FiefFightNpcReq(long fiefid, int cost) {
		req = new MsgReqFiefFightNpc().setFiefid(fiefid).setCost(cost);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_FIEF_FIGHT_NPC
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
