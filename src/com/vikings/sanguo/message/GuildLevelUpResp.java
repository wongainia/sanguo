package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.GuildInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGuildLevelUp;

public class GuildLevelUpResp extends BaseResp {
	private ReturnInfoClient ric;
	private GuildInfoClient gic;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildLevelUp resp = new MsgRspGuildLevelUp();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		gic = GuildInfoClient.convert(resp.getGi());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public GuildInfoClient getGic() {
		return gic;
	}

}
