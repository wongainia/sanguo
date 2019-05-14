package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.protos.MsgRspOtherLordTroopInfoQuery;

public class OtherLordTroopInfoQueryResp extends BaseResp {
	private List<ArmInfoClient> infos;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspOtherLordTroopInfoQuery resp = new MsgRspOtherLordTroopInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		infos = new ArrayList<ArmInfoClient>();
		if (resp.hasInfo()) {
			infos.addAll(ArmInfoClient.convertList(resp.getInfo()));
		}
	}

	public List<ArmInfoClient> getInfos() {
		return infos;
	}

}
