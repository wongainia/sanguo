package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.protos.MsgRspUserSearch;

public class UserSearchResp extends BaseResp {

	private List<BriefUserInfoClient> users = new ArrayList<BriefUserInfoClient>();

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserSearch rsp = new MsgRspUserSearch();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		users = Decoder.decodeBriefUserInfos(rsp.getInfosList());
	}

	public List<BriefUserInfoClient> getUsers() {
		return users;
	}
}
