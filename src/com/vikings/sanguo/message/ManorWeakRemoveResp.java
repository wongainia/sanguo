package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorWeakRemove;

/**
 * 
 * @author susong
 * 
 */
public class ManorWeakRemoveResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorWeakRemove rsp = new MsgRspManorWeakRemove();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		ManorInfoClient mic = ManorInfoClient.convert(rsp.getMi());
		Account.manorInfoClient.update(mic);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
