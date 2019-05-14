package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqPlayerWantedInfoQuery;

public class PlayerWantedInfoQueryReq extends BaseReq {

	private MsgReqPlayerWantedInfoQuery req;

	public PlayerWantedInfoQueryReq(int country, long id, int count) {
		req = new MsgReqPlayerWantedInfoQuery().setCountry(country).setId(id).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLAYER_WANTED_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
