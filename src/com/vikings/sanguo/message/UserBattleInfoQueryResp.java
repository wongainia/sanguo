package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.OtherUserBattleIdInfoClient;
import com.vikings.sanguo.protos.MsgRspUserBattleInfoQuery;
import com.vikings.sanguo.protos.OtherUserBattleIdInfo;

/**
 * 
 * @author susong
 * 
 */
public class UserBattleInfoQueryResp extends BaseResp {
	private List<OtherUserBattleIdInfoClient> infos = new ArrayList<OtherUserBattleIdInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserBattleInfoQuery rsp = new MsgRspUserBattleInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		for (OtherUserBattleIdInfo info : rsp.getInfosList()) {
			OtherUserBattleIdInfoClient oubiic = OtherUserBattleIdInfoClient
					.convert(info);
			if (null != oubiic.getInfos() && !oubiic.getInfos().isEmpty())
				infos.add(oubiic);
		}
	}

	public List<OtherUserBattleIdInfoClient> getInfos() {
		return infos;
	}
}
