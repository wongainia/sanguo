package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BloodRankInfoClient;
import com.vikings.sanguo.protos.MsgRspBloodRankQuery;

public class BloodRankQueryResp extends BaseResp {
	private List<BloodRankInfoClient> infos;
	private BloodRankInfoClient selfInfo;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBloodRankQuery rsp = new MsgRspBloodRankQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.hasInfos())
			infos = BloodRankInfoClient.convert2List(rsp.getInfosList());
		else
			infos = new ArrayList<BloodRankInfoClient>();
		if (rsp.hasSelfInfo())
			selfInfo = BloodRankInfoClient.convert(rsp.getSelfInfo());
	}

	public List<BloodRankInfoClient> getInfos() {
		return infos;
	}

	public BloodRankInfoClient getSelfInfo() {
		return selfInfo;
	}

}
