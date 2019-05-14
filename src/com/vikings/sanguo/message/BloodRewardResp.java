package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.protos.MsgRspBloodReward;

public class BloodRewardResp extends BaseResp {
	private LordInfoClient lic;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBloodReward resp = new MsgRspBloodReward();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		if (resp.hasLi())
			Account.myLordInfo = LordInfoClient.convert(resp.getLi());
	}

	public LordInfoClient getLic() {
		return lic;
	}
}
