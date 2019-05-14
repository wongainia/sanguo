package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.GuildSearchCond;
import com.vikings.sanguo.protos.MsgReqGuildSearch;

public class GuildSearchReq extends BaseReq {

	private MsgReqGuildSearch req;

	public GuildSearchReq(ResultPage page, List<GuildSearchCond> conds) {
		req = new MsgReqGuildSearch().setStart(page.getCurIndex()).setCount(
				(int) page.getPageSize());
		if (null != conds && !conds.isEmpty())
			req.setCondsList(conds);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_GUILD_SEARCH
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
