/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-4 上午10:19:23
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqArenaQuery;

public class ArenaQueryLogReq extends BaseReq{
	private MsgReqArenaQuery req;
	
	public ArenaQueryLogReq() {
		req = new MsgReqArenaQuery();
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
