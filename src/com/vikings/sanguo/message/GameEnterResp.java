package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.ReadLogCache;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgGameEnterRsp;

public class GameEnterResp extends BaseResp {

	private int time;
	private int worldLevel;
	private int worldLevelProcess;
	private int worldLevelProcessTotal;

	private ReturnInfoClient rs;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgGameEnterRsp resp = new MsgGameEnterRsp();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		time = resp.getTime();
		rs = ReturnInfoClient.convert2Client(resp.getRi());

		worldLevel = resp.getWorldLevel();
		worldLevelProcess = resp.getWorldLevelProcess();
		worldLevelProcessTotal = resp.getWorldLevelProcessTotal();

		// 保存距离下次免费时间和剩余次数
		if (null == Account.readLog)
			Account.readLog = ReadLogCache.getInstance();

		Account.readLog.FREE_RESET_TIME = resp.getMpResetTime();
		Account.readLog.FREE_TIMES = resp.getMpFreeCount();
		Account.readLog.save();

	}

	public int getTime() {
		return time;
	}

	public ReturnInfoClient getRs() {
		return rs;
	}

	public int getWorldLevel() {
		return worldLevel;
	}

	public int getWorldLevelProcess() {
		return worldLevelProcess;
	}

	public int getWorldLevelProcessTotal() {
		return worldLevelProcessTotal;
	}
}
