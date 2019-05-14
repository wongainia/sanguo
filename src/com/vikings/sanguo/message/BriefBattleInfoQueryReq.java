package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqBriefBattleInfoQuery;

public class BriefBattleInfoQueryReq extends BaseReq {

	private MsgReqBriefBattleInfoQuery req;

	public BriefBattleInfoQueryReq(List<Long> battleIds) {
		req = new MsgReqBriefBattleInfoQuery().setBattleIdsList(new ArrayList<Long>());
		for(Long id:battleIds){
			req.getBattleIdsList().add(id);
		}
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BRIEF_BATTLE_INFO_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
