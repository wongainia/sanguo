package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspBloodAttack;

public class BloodAttackResp extends BaseResp {
	private LordInfoClient lic;
	private ReturnInfoClient ri;
	private BattleLogInfo log;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBloodAttack resp = new MsgRspBloodAttack();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		if (resp.hasLi())
			Account.myLordInfo = LordInfoClient.convert(resp.getLi());
		log = resp.getLog();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public LordInfoClient getLic() {
		return lic;
	}

	public BattleLogInfo getLog() {
		return log;
	}

}
