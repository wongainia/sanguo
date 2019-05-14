package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.protos.MsgRspOtherUserHeroInfoQuery;

public class OtherUserHeroInfoQueryResp extends BaseResp {
	private List<OtherHeroInfoClient> infos;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspOtherUserHeroInfoQuery resp = new MsgRspOtherUserHeroInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		infos = OtherHeroInfoClient.convert2List(resp.getInfosList());
	}

	public List<OtherHeroInfoClient> getInfos() {
		return null == infos?new ArrayList<OtherHeroInfoClient>():infos;
	}

}
