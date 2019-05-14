package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.protos.BriefGuildInfo;
import com.vikings.sanguo.protos.MsgRspBriefGuildInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class BriefGuildInfoQueryResp extends BaseResp {
	private List<BriefGuildInfoClient> infos = new ArrayList<BriefGuildInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBriefGuildInfoQuery rsp = new MsgRspBriefGuildInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.getInfosCount() > 0) {
			for (BriefGuildInfo info : rsp.getInfosList()) {
				infos.add(BriefGuildInfoClient.convert(info));
			}
		}
	}

	public List<BriefGuildInfoClient> getInfos() {
		return infos;
	}

}
