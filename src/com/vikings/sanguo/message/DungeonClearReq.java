package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.protos.MsgReqDungeonClear;

public class DungeonClearReq extends BaseReq {

	private MsgReqDungeonClear req;

	public DungeonClearReq(int actId, List<HeroIdInfoClient> heros) {
		req = new MsgReqDungeonClear();
		req.setActid(actId);
		if (null != heros && !heros.isEmpty())
			req.setHeroInfosList(HeroIdInfoClient.convert2ServerList(heros));
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_DUNGEON_CLEAR
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
