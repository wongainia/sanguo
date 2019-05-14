/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-3 下午5:55:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;

public class ArenaRewardReq extends BaseReq{
	public ArenaRewardReq() {
	}
	
	@Override
	protected void toBytes(OutputStream out) {
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ARENA_REWARD.getNumber();
	}


}
