package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspDuelAttack;

/**
 * 
 * @author susong
 * 
 */
public class DuelAttackResp extends BaseResp {
	private BattleLogInfo bli;
	private ReturnInfoClient ri;

	private FiefInfoClient fiefInfo;
	private LordFiefInfoClient lordFiefInfo;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDuelAttack rsp = new MsgRspDuelAttack();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		bli = rsp.getBattleLogInfo();
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		fiefInfo = FiefInfoClient.convert(rsp.getFiefInfo());
		lordFiefInfo = LordFiefInfoClient.convert(rsp.getLordFiefInfo());

		Account.myLordInfo.battleClean(bli);
		Account.heroInfoCache.updateReturnHero(bli.getRhisList());
		Account.richFiefCache.update(lordFiefInfo, fiefInfo);
		// 如果服务器返回了最新的领地信息，更新地图上缓存
		Config.getController().getBattleMap().updateFief(fiefInfo);
	}

	public BattleLogInfo getBli() {
		return bli;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
