package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.BriefBattleLogInfo;
import com.vikings.sanguo.protos.MsgRspBriefBattleLogQueryByFiefId;

/**
 * 
 * @author susong
 * 
 */
public class BriefBattleLogQueryByFiefIdResp extends BaseResp {
	private List<BriefBattleLogInfo> infos = new ArrayList<BriefBattleLogInfo>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBriefBattleLogQueryByFiefId rsp = new MsgRspBriefBattleLogQueryByFiefId();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		infos.addAll(rsp.getInfosList());
	}

	public List<BriefBattleLogInfo> getInfos() {
		return infos;
	}

}
