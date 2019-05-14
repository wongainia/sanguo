package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqUserStatusUpdate;
import com.vikings.sanguo.protos.ROLE_STATUS;

public class UserStatusUpdateReq extends BaseReq {
	private MsgReqUserStatusUpdate req;

	public UserStatusUpdateReq(ROLE_STATUS status, int type) {
		req = new MsgReqUserStatusUpdate().setStatusid(status.getNumber())
				.setType(type);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_STATUS_UPDATE
				.getNumber();
	}

}
