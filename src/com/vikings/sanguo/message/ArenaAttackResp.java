package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspArenaAttack;

public class ArenaAttackResp extends BaseResp {
	private ReturnInfoClient ric;
	private BattleLogInfo bli;
	private boolean hasBattleLog;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspArenaAttack resp = new MsgRspArenaAttack();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		if (resp.hasLi())
			Account.myLordInfo = LordInfoClient.convert(resp.getLi());
		hasBattleLog = resp.hasBattleLog();
		bli = resp.getBattleLog();
	}

	public BattleLogInfo getLog() {
		return bli;
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public boolean isHasBattleLog() {
		return hasBattleLog;
	}

}
