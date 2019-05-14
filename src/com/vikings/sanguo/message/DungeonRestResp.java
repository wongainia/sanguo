package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.protos.MsgRspDungeonReset;

/**
 * 
 * @author susong
 * 
 */
public class DungeonRestResp extends BaseResp {

	private ReturnInfoClient ri;
	
	private CampaignInfo ci;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonReset resp = new MsgRspDungeonReset();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		ci = resp.getInfo();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
	
	public CampaignInfo getCi() {
		return ci;
	}
}

