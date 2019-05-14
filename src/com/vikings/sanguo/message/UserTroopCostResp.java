package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspUserTroopCostUpdate;

/**
 * 
 * @author susong
 * 
 */
public class UserTroopCostResp extends BaseResp {
	
	private ReturnInfoClient ri;
//	private ManorInfoClient mic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserTroopCostUpdate resp = new MsgRspUserTroopCostUpdate();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
//		mic = ManorInfoClient.convert(resp.getMi());
//		Account.manorInfoClient.update(mic);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
