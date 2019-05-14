package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqPlayerStaticUserDataQuery;
import com.vikings.sanguo.protos.StaticUserDataType;

public class PlayerStaticUserDataQueryReq extends BaseReq {
	private MsgReqPlayerStaticUserDataQuery req;

	public PlayerStaticUserDataQueryReq(int targertId,
			StaticUserDataType dataType, long id, int count) {
		req = new MsgReqPlayerStaticUserDataQuery().setTargetid(targertId)
				.setDataType(dataType).setId(id).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_PLAYER_STATIC_USER_DATA_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
