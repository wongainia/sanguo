/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-1 下午5:08:34
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;
import com.vikings.sanguo.protos.MsgReqReinforceBuyUnit;

public class ReinforceBuyUnitReq extends BaseReq{
	private MsgReqReinforceBuyUnit req;

	public ReinforceBuyUnitReq(long battleId, int cost, int type) {
		req = new MsgReqReinforceBuyUnit().setBattleid(battleId).setCost(cost).setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_REINFORCE_BUY_UNIT.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
