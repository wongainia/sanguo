package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspGuildBuild;

/**
 * 
 * @author susong
 * 
 */
public class GuildBuildResp extends BaseResp {
	private int guildid;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildBuild resp = new MsgRspGuildBuild();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		guildid = resp.getGuildid();
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public int getGuildid() {
		return guildid;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
