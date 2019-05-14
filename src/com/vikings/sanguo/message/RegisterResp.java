package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.MsgRegisterRsp;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class RegisterResp extends BaseResp {

	private UserAccountClient user;

	@Override
	protected void fromBytes(byte[] buf, int index) {
		MsgRegisterRsp rsp = new MsgRegisterRsp();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		this.user = new UserAccountClient(rsp.getUserid(),rsp.getPsw());
	}

	public UserAccountClient getUser() {
		return user;
	}

}
