package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqQuestFinish;

public class QuestFinishReq extends BaseReq {

	private MsgReqQuestFinish req;

	public QuestFinishReq(int questId) {
		req = new MsgReqQuestFinish().setQuestid(questId);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_QUEST_FINISH
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
