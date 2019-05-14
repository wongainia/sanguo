package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspPlunderAttack;

/**
 * 
 * @author susong
 * 
 */
public class BattlePlunderResp extends BaseResp {

	private BattleLogInfo bli;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspPlunderAttack rsp = new MsgRspPlunderAttack();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		bli = rsp.getBattleLogInfo();
		ri = ReturnInfoClient.convert2Client(rsp.getRi());

		Account.myLordInfo.battleClean(bli);
		Account.heroInfoCache.updateReturnHero(bli.getRhisList());

		// 如果服务器返回了最新的领地信息，更新地图上缓存
		FiefInfoClient fiefInfo = FiefInfoClient.convert(rsp.getDstFiefInfo());
		Config.getController().getBattleMap().updateFief(fiefInfo);

	}

	public BattleLogInfo getBli() {
		return bli;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
