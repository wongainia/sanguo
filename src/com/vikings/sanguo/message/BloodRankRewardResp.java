package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspBloodRankReward;

public class BloodRankRewardResp extends BaseResp {
	private ReturnInfoClient ric;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBloodRankReward resp = new MsgRspBloodRankReward();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

}
