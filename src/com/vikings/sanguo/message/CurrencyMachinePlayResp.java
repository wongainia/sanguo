package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspCurrencyMachinePlay;
import com.vikings.sanguo.protos.RoleInfo;

public class CurrencyMachinePlayResp extends BaseResp {
	private RoleInfo roleInfo;
	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspCurrencyMachinePlay resp = new MsgRspCurrencyMachinePlay();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		roleInfo = resp.getRoleInfo();
		Account.user.update(roleInfo);
		Account.readLog.GOD_WEALTH_TIMES = roleInfo.getPart3()
				.getCurrencyMachineTimes();

	}

	public RoleInfo getRoleInfo() {
		return roleInfo;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
