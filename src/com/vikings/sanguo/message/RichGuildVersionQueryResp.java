package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.BattleIdInfo;
import com.vikings.sanguo.protos.MsgRspRichGuildVersionQuery;

/**
 * 
 * @author susong
 * 
 */
public class RichGuildVersionQueryResp extends BaseResp {
	private int version;
	private BattleIdInfo attackInfo;
	private BattleIdInfo defendInfo;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRichGuildVersionQuery rsp = new MsgRspRichGuildVersionQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		version = rsp.getVersion();
		// if (rsp.hasAttackBattleid())
		// attackInfo = rsp.getAttackBattleid();
		// if (rsp.hasDefendBattleid())
		// defendInfo = rsp.getDefendBattleid();
	}

	public int getVersion() {
		return version;
	}

	public BattleIdInfo getAttackInfo() {
		return attackInfo;
	}

	public BattleIdInfo getDefendInfo() {
		return defendInfo;
	}

}
