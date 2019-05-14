/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-16 下午4:54:43
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.protos.MsgReqManorRevive;

public class ManorReviveReq extends BaseReq {
	public static final int TARGET_ARM = 1;
	public static final int TARGET_BOSS = 2;

	private MsgReqManorRevive req;

	public ManorReviveReq(int target, int buildingId, List<Integer> armIds,
			int bossArmId, int bossCount) {
		req = new MsgReqManorRevive().setTarget(target).setBuildingid(
				buildingId);
		if (null != armIds && !armIds.isEmpty())
			req.setArmidsList(armIds);
		if (bossArmId > 0 && bossCount > 0)
			req.setBossArmid(bossArmId).setBossCount(bossCount);
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_MANOR_REVIVE
				.getNumber();
	}

}
