package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.protos.MsgRspDungeonActReset;

/**
 * 
 * @author susong
 * 
 */
public class DungeonActResetResp extends BaseResp {

	private ReturnInfoClient ri;

	private List<CampaignInfo> infos = new ArrayList<CampaignInfo>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonActReset rsp = new MsgRspDungeonActReset();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		if (rsp.hasInfos())
			infos.addAll(rsp.getInfosList());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public List<CampaignInfo> getInfos() {
		return infos;
	}

}
