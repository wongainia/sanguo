package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqItemSell;

public class ItemSellReq extends BaseReq {

	private MsgReqItemSell req; 
	
	public ItemSellReq(long bagId, int amount) {
		req = new MsgReqItemSell().setBagid(bagId).setAmount(amount);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ITEM_SELL
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
