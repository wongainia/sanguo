package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgPlayerManorUpdateReq;
import com.vikings.sanguo.utils.StringUtil;

public class PlayerManorUpdateReq extends BaseReq {
	private MsgPlayerManorUpdateReq req;

	public PlayerManorUpdateReq(String name) {
		req = new MsgPlayerManorUpdateReq();
		if (!StringUtil.isNull(name))
			req.setName(name);
	}

	@Override
	public short cmd() {
		return 0;
//		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLAYER_MANOR_UPDATE;
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
