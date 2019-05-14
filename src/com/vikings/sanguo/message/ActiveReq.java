package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgChannelActivateReq;
import com.vikings.sanguo.utils.StringUtil;

public class ActiveReq extends RegisterReq {

	private MsgChannelActivateReq req;

	public ActiveReq() {
		super("", (byte) 0, 0, "");
		req = new MsgChannelActivateReq();
		req.setSim(imsi).setProgramid(Config.clientCode)
				.setChannel(Config.getChannel()).setExtend(extend);
		if (!StringUtil.isNull(pId))
			req.setPartnerId(pId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_CHANNEL_ACTIVATE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
