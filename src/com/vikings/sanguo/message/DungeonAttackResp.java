package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.protos.MsgRspDungeonAttack;

/**
 * 
 * @author susong
 * 
 */
public class DungeonAttackResp extends BaseResp {

	private BattleLogInfo log;

	private ReturnInfoClient ri;

	private CampaignInfo ci;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonAttack rsp = new MsgRspDungeonAttack();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		log = rsp.getBattleLogInfo();
		ci = rsp.getCampaignInfo();
		// 更新武将变化
		if (rsp.hasHeroInfos())
			Account.heroInfoCache.update(HeroInfoClient.convert2List(rsp
					.getHeroInfosList()));
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		// 副本兵变化 是在主城里，主动主城设置兵力
		// Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}

	public BattleLogInfo getLog() {
		return log;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public CampaignInfo getCi() {
		return ci;
	}
}
