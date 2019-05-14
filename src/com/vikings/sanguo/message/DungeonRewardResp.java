package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspDungeonReward;

/**
 * 
 * @author susong
 * 
 */
public class DungeonRewardResp extends BaseResp {
	
	
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonReward  rsp = new MsgRspDungeonReward();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}
	
	public ReturnInfoClient getRi() {
		return ri;
	}
}
