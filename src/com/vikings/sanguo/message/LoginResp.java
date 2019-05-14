package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.MsgLoginRsp;
import com.vikings.sanguo.utils.AesUtil;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class LoginResp extends BaseResp {

	private UserAccountClient user;

	public LoginResp(UserAccountClient user) {
		this.user = user;
	}

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgLoginRsp resp = new MsgLoginRsp();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		Config.aesKey = AesUtil.loadKey(resp.getAesKey().toByteArray());
		Config.sessionId = resp.getSessionid();
		if(resp.hasInfo()){
			if(resp.getInfo().hasPart1())
				user.setAccountInfoPart1(resp.getInfo().getPart1());
			if(resp.getInfo().hasPart3())
				user.setAccountInfoPart3(resp.getInfo().getPart3());
		}
	}

	public UserAccountClient getUser() {
		return user;
	}
}
