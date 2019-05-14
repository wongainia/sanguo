package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGuildInviteAsk;

/**
 * 
 * @author susong
 * 
 */
public class GuildInviteAskResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildInviteAsk resp = new MsgRspGuildInviteAsk();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
