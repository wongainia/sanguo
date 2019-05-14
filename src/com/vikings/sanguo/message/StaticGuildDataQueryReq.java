package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqStaticGuildDataQuery;
import com.vikings.sanguo.protos.StaticGuildDataType;

public class StaticGuildDataQueryReq extends BaseReq {

	private MsgReqStaticGuildDataQuery req;

	public StaticGuildDataQueryReq(StaticGuildDataType dataType, int guildid,
			long id, int count) {
		req = new MsgReqStaticGuildDataQuery().setDataType(dataType)
				.setGuildid(guildid).setId(id).setCount(count);
	}

	@Override
	public short cmd() {
//		return 0;
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_STATIC_GUILD_DATA_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
