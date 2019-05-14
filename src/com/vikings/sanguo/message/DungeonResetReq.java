package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqDungeonReset;

public class DungeonResetReq extends BaseReq {

	private MsgReqDungeonReset req;

	public DungeonResetReq(int actid, int campaignid, int type) {
		req = new MsgReqDungeonReset().setActid(actid)
				.setCampaignid(campaignid).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_RESET
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
