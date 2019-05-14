package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGuildJoinAsk;

/**
 * 
 * @author susong
 * 
 */
public class GuildJoinAskResp extends BaseResp {
	private ReturnInfoClient ri;
	private boolean autoJoin;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildJoinAsk resp = new MsgRspGuildJoinAsk();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		autoJoin = resp.getAutoJoin();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

}
