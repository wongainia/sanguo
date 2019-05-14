package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqStaticUserDataQuery;
import com.vikings.sanguo.protos.StaticUserDataType;

public class StaticUserDataQueryReq extends BaseReq {
	private MsgReqStaticUserDataQuery req;

	public StaticUserDataQueryReq(StaticUserDataType dataType, long id,
			int count) {
		req = new MsgReqStaticUserDataQuery().setDataType(dataType).setId(id)
				.setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_STATIC_USER_DATA_QUERY.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
