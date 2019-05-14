package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspBattleLogInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class BattleLogInfoQueryResp extends BaseResp {
	private BattleLogInfo bli;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBattleLogInfoQuery rsp = new MsgRspBattleLogInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		bli = rsp.getInfo();
	}

	public BattleLogInfo getBli() {
		return bli;
	}

}
