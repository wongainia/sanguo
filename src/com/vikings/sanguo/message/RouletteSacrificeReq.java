package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.protos.MsgReqRouletteSacrifice;

public class RouletteSacrificeReq extends BaseReq {
	private MsgReqRouletteSacrifice req;

	public RouletteSacrificeReq(List<KeyValue> list) {
		req = new MsgReqRouletteSacrifice();
		if (list != null && !list.isEmpty()) {
			req.setInfosList(list);
		}
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ROULETTE_SACRIFICE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
