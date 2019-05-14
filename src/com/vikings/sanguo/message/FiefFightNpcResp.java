package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspFiefFightNpc;

/**
 * 
 * @author susong
 * 
 */
public class FiefFightNpcResp extends BaseResp {
	private long battleId;
	private List<HeroInfoClient> heroInfoClients;

	private FiefInfoClient fiefInfo = null;
	private LordFiefInfoClient lordFiefInfo = null;
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefFightNpc resp = new MsgRspFiefFightNpc();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		battleId = resp.getBattleid();
		if (resp.hasRi())
			ri = ReturnInfoClient.convert2Client(resp.getRi());

		if (resp.hasHeroInfos()) {
			heroInfoClients = HeroInfoClient.convert2List(resp
					.getHeroInfosList());
			Account.heroInfoCache.update(heroInfoClients);
		}

		if (resp.hasFiefInfo())
			fiefInfo = FiefInfoClient.convert(resp.getFiefInfo());
		if (resp.hasLordFiefInfo())
			lordFiefInfo = LordFiefInfoClient.convert(resp.getLordFiefInfo());
		Account.richFiefCache.update(lordFiefInfo, fiefInfo);
	}

	/**
	 * @return 战场id
	 */
	public long getBattleId() {
		return battleId;
	}

	public List<HeroInfoClient> getHeroInfoClient() {
		return heroInfoClients;
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
