package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.protos.MsgPlayerManorUpdateRsp;

/**
 * 
 * @author susong
 * 
 */
public class PlayerManorUpdateResp extends BaseResp {
	private ManorInfoClient mic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgPlayerManorUpdateRsp resp = new MsgPlayerManorUpdateRsp();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		mic = ManorInfoClient.convert(resp.getMi());
	}

	public ManorInfoClient getMic() {
		return mic;
	}
}
