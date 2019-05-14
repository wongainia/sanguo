package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BloodPokerClient;
import com.vikings.sanguo.protos.MsgRspBloodPoker;

public class BloodPokerResp extends BaseResp {
	private BloodPokerClient bpc;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBloodPoker rsp = new MsgRspBloodPoker();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);

		bpc = BloodPokerClient.convert(rsp.getInfo());

	}

	public BloodPokerClient getBpc() {
		return bpc;
	}

}
