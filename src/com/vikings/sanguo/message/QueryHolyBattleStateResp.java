package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.HolyBattleStateClient;
import com.vikings.sanguo.protos.MsgRspQueryHolyBattleState;

public class QueryHolyBattleStateResp extends BaseResp {
	private List<HolyBattleStateClient> list;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspQueryHolyBattleState rsp = new MsgRspQueryHolyBattleState();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		list = new ArrayList<HolyBattleStateClient>();
		if (rsp.hasInfos()) {
			list.addAll(HolyBattleStateClient.convert2List(rsp.getInfosList()));
			
		}
	}

	public List<HolyBattleStateClient> getList() {
		return list;
	}

}
