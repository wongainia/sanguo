package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqItemUse;

public class ItemUseReq extends BaseReq {
	MsgReqItemUse req;

	public ItemUseReq(int targetId, long bagId, int itemId, int scheme,
			int count) {
		req = new MsgReqItemUse().setTargetid(targetId).setBagid(bagId)
				.setItemid(itemId).setScheme(scheme).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ITEM_USE
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
