package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspAccountRestore;

public class AccountRestoreResp extends BaseResp {
	// 剩余找回次数
	private int count;

	protected void fromBytes(byte[] buf, int index) {
		MsgRspAccountRestore resp = new MsgRspAccountRestore();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		count = resp.getRemainCount();
	}

	public int getCount() {
		return count;
	}
}
