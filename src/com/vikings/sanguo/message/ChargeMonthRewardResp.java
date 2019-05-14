package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspChargeMonthReward;
import com.vikings.sanguo.protos.RoleChargeInfo;

public class ChargeMonthRewardResp extends BaseResp {
	private ReturnInfoClient ric;
	private RoleChargeInfo chargeInfo;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspChargeMonthReward resp = new MsgRspChargeMonthReward();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		chargeInfo = resp.getChargeInfo();
		Account.user.setRoleChargeInfo(chargeInfo);

	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public RoleChargeInfo getChargeInfo() {
		return chargeInfo;
	}

}
