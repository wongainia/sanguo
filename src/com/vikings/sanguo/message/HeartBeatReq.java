package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqHeartbeat;

public class HeartBeatReq extends BaseReq {
	private MsgReqHeartbeat req;

	public HeartBeatReq(List<Integer> ids, int country) {
		req = new MsgReqHeartbeat().setChatIdsList(ids);
		if (country > 0)
			req.setCountry(country);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_HEARTBEAT
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
