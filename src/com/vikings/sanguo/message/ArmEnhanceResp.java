package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspArmEnhance;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class ArmEnhanceResp extends BaseResp {
	private ReturnInfoClient ri;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspArmEnhance  resp = new MsgRspArmEnhance ();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		Account.armEnhanceCache.update(resp.getArmInfo());
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
