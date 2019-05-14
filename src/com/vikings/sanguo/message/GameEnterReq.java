package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.MsgGameEnterReq;

public class GameEnterReq extends BaseReq {

	private MsgGameEnterReq req;

	public GameEnterReq(int clientType, int clientVer) {
		req = new MsgGameEnterReq().setClientType(clientType).setClientVer(
				clientVer);
		if (Config.getChannel() > 0)
			req.setChannelId(Config.getChannel());
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GAME_ENTER
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
