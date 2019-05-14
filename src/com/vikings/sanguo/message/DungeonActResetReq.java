package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqDungeonActReset;

public class DungeonActResetReq extends BaseReq {

	private MsgReqDungeonActReset req;

	public DungeonActResetReq(int actId, List<Integer> campaignids, int type) {
		req = new MsgReqDungeonActReset();
		req.setActid(actId).setType(type);
		if (null != campaignids && !campaignids.isEmpty()) {
			req.setCampaignidsList(campaignids);
		}
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_ACT_RESET
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
