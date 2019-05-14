package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.MsgReqPlayerUpdate;

public class PlayerUpdateReq extends BaseReq {

	private MsgReqPlayerUpdate req;

	public PlayerUpdateReq(UserAccountClient user) {
		req = new MsgReqPlayerUpdate().setBirthday(user.getBirthday())
				.setDesc(user.getDesc()).setImage(user.getImage())
				.setNick(user.getNick()).setSex(user.getSex()).setProvince(user.getProvince());
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLAYER_UPDATE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
