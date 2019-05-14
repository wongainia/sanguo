package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspCurrencyBoxOpen;
import com.vikings.sanguo.protos.RoleDayInfo;

public class CurrencyBoxOpenResp extends BaseResp {
	private ReturnInfoClient ric;
	private RoleDayInfo roleDayInfo;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspCurrencyBoxOpen resp = new MsgRspCurrencyBoxOpen();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		roleDayInfo = resp.getDayInfo();
		Account.user.setRoleDayInfo(roleDayInfo);
		Account.readLog.WAR_LORD_BOX_TIMES = roleDayInfo.getBoxTimes();
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public RoleDayInfo getRoleDayInfo() {
		return roleDayInfo;
	}

}
