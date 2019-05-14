package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.protos.BriefBattleInfo;
import com.vikings.sanguo.protos.MsgRspBriefBattleInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class BriefBattleInfoQueryResp extends BaseResp {
	
	private List<BriefBattleInfoClient> infos = new ArrayList<BriefBattleInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBriefBattleInfoQuery rsp = new MsgRspBriefBattleInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		for (BriefBattleInfo info : rsp.getInfosList()) {
			infos.add(BriefBattleInfoClient.convert(info));
		}
	}

	public List<BriefBattleInfoClient> getInfos() {
		return infos;
	}

}
