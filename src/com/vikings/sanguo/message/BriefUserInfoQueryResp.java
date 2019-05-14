package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.protos.BriefUserInfo;
import com.vikings.sanguo.protos.MsgRspBriefUserInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class BriefUserInfoQueryResp extends BaseResp {
	private List<BriefUserInfoClient> infos = new ArrayList<BriefUserInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBriefUserInfoQuery rsp = new MsgRspBriefUserInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if(!rsp.hasInfos())return;
		for(BriefUserInfo bu:rsp.getInfosList()){
			infos.add(BriefUserInfoClient.convert(bu));
		}
	}

	public List<BriefUserInfoClient> getInfos() {
		return infos;
	}

}
