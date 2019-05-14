package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspConfirmBuyBattleUnit;

/**
 * 
 * @author susong
 * 
 */
public class ConfirmBuyBattleUnitResp extends BaseResp {
	private int pokerUnit;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspConfirmBuyBattleUnit rsp = new MsgRspConfirmBuyBattleUnit();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		pokerUnit = rsp.getPokerUnit();
	}

	public int getPokerUnit() {
		return pokerUnit;
	}

}
