package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspCommonAttack;

/**
 * 
 * @author susong
 * 
 */
public class CommonAttackResp extends BaseResp {

	private BattleLogInfo bli;
	private ReturnInfoClient ri;

	private FiefInfoClient fiefInfo;
	private LordFiefInfoClient lordFiefInfo;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspCommonAttack rsp = new MsgRspCommonAttack();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		bli = rsp.getBattleLogInfo();
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		fiefInfo = FiefInfoClient.convert(rsp.getFiefInfo());
		lordFiefInfo = LordFiefInfoClient.convert(rsp.getLordFiefInfo());

		Account.myLordInfo.battleClean(bli);
		Account.heroInfoCache.updateReturnHero(bli.getRhisList());

		// 战斗结果的数据处理统一
		if (null != bli && Account.user.getId() == bli.getAttacker()) {
			if (bli.getBattleResult() == 1)
				Account.richFiefCache.update(lordFiefInfo, fiefInfo);
		}
		if (null != bli && (Account.user.getId() == bli.getDefender())) {
			if (bli.getBattleResult() == 1) {
				// 删除自己的领地
				Account.richFiefCache.deleteFief(bli.getDefendFiefid());
			}
		}
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
