package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorReceive;

/**
 * 
 * @author susong
 * 
 */
public class ManorReceiveResp extends BaseResp {
	private ReturnInfoClient ri;
	private ManorInfoClient mic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorReceive resp = new MsgRspManorReceive();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		mic = ManorInfoClient.convert(resp.getMi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public ManorInfoClient getMic() {
		return mic;
	}

}
