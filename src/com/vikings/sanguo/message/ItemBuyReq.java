package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqItemBuy;

public class ItemBuyReq extends BaseReq {

	private MsgReqItemBuy req;

	public ItemBuyReq(int itemId, int amount, int currency) {
		req = new MsgReqItemBuy();
		req.setItemid(itemId).setAmount(amount).setCurrency(currency);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ITEM_BUY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
