package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspUserStatusUpdate;
import com.vikings.sanguo.protos.RoleInfo;

public class UserStatusUpdateResp extends BaseResp {
	private ReturnInfoClient ric;
	private RoleInfo roleInfo;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserStatusUpdate resp = new MsgRspUserStatusUpdate();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		roleInfo = resp.getRoleInfo();
		Account.user.update(roleInfo);
	}

	public RoleInfo getRoleInfo() {
		return roleInfo;
	}

	public ReturnInfoClient getRic() {
		return ric;
	}
}
