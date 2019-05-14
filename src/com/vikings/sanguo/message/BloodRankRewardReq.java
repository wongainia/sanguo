package com.vikings.sanguo.message;

import java.io.OutputStream;

public class BloodRankRewardReq extends BaseReq {
	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_BLOOD_RANK_REWARD
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
	}

}
