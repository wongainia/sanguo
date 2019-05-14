package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspBattleReinfor;

/**
 * 
 * @author susong
 * 
 */
public class BattleReinforResp extends BaseResp {

	// 战争目的领地
	private BriefFiefInfoClient fiefInfo;
	private BriefBattleInfoClient battle;
	private ManorInfoClient mic;
	private ReturnInfoClient ri;
	private List<HeroInfoClient> heroInfoClients;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBattleReinfor rsp = new MsgRspBattleReinfor();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		fiefInfo = BriefFiefInfoClient.convert(rsp.getFiefInfo());
		battle = BriefBattleInfoClient.convert(rsp.getBattleInfo());
		CacheMgr.fillBattleResult(fiefInfo, battle);
		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		if (rsp.hasHeroInfos())
			heroInfoClients = HeroInfoClient.convert2List(rsp
					.getHeroInfosList());
		mic = ManorInfoClient.convert(rsp.getMi());
		Config.getController().getBattleMap().updateFief(fiefInfo);
		Account.briefBattleInfoCache.update(battle);
		Account.heroInfoCache.update(heroInfoClients);
		Account.manorInfoClient.update(mic);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public List<HeroInfoClient> getHeroInfoClients() {
		return heroInfoClients;
	}

	public ManorInfoClient getMic() {
		return mic;
	}

}
