/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 下午5:34:52
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;
import com.vikings.sanguo.protos.MsgReqRankReward;

public class HonorRankRewardReq extends BaseReq{
	private MsgReqRankReward req;

	public HonorRankRewardReq(int type) {
		req = new MsgReqRankReward().setType(type);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_RANK_REWARD.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
