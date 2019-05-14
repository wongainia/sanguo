/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-3 下午5:16:19
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;
import com.vikings.sanguo.protos.MsgReqArenaAttack;

public class ArenaAttackReq extends BaseReq{
	private MsgReqArenaAttack req;
	
	public ArenaAttackReq(int target, int targetPos, int selfPos) {
		req = new MsgReqArenaAttack().setTarget(target).setTargetPos(targetPos)
				.setSelfPos(selfPos);
	}
	
	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ARENA_ATTACK.getNumber();
	}


}
