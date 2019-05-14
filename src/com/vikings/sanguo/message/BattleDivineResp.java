package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspBattleDivine;

/**
 * 
 * @author susong
 * 
 */
public class BattleDivineResp extends BaseResp {
	private int divineResult;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBattleDivine rsp = new MsgRspBattleDivine();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		divineResult = rsp.getDivineResult();
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
	}

	public int getDivineResult() {
		return divineResult;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
