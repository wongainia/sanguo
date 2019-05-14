package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspMessagePost;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class MessagePostResp extends BaseResp {
	private ReturnInfoClient ri;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspMessagePost resp = new MsgRspMessagePost();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
