package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspDungeonDivine;

/**
 * 
 * @author susong
 * 
 */
public class BattleDungeonDivineResp extends BaseResp {
	
	private int divineResult;
	
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonDivine rsp = new MsgRspDungeonDivine();
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
