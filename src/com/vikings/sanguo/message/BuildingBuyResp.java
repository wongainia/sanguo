package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorBuildingBuy;

/**
 * 
 * @author susong
 * 
 */
public class BuildingBuyResp extends BaseResp {
	private ReturnInfoClient ri;
	private ManorInfoClient mic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorBuildingBuy resp = new MsgRspManorBuildingBuy();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		mic = ManorInfoClient.convert(resp.getMi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public ManorInfoClient getMic() {
		return mic;
	}
}
