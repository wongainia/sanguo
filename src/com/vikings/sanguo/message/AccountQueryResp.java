package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.MsgRspAccountQuery;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class AccountQueryResp extends BaseResp {

	private UserAccountClient user;

	@Override
	protected void fromBytes(byte[] buf, int index) {
		MsgRspAccountQuery rsp = new MsgRspAccountQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		user = new UserAccountClient(rsp.getUserid(),rsp.getPsw());
	}

	public UserAccountClient getUser() {
		return user;
	}

}
