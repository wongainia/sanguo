package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.GuildInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGuildInfoUpdate;

/**
 * 
 * @author susong
 * 
 */
public class GuildInfoUpdateResp extends BaseResp {
	private GuildInfoClient gic;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildInfoUpdate resp = new MsgRspGuildInfoUpdate();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		gic = GuildInfoClient.convert(resp.getGi());
	}

	public GuildInfoClient getGic() {
		return gic;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
