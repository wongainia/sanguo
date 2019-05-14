package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.ActInfo;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.MsgRspDungeonClear;

/**
 * 
 * @author susong
 * 
 */
public class DungeonClearResp extends BaseResp {

	private ReturnInfoClient ri;
	private BattleLogInfo bli;
	private ActInfo ai;
	private List<HeroInfoClient> hics;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspDungeonClear rsp = new MsgRspDungeonClear();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ai = rsp.getActInfo();
		// 更新武将变化
		if (rsp.hasHeroInfos()) {
			hics = HeroInfoClient.convert2List(rsp.getHeroInfosList());
			Account.heroInfoCache.update(hics);
		}

		ri = ReturnInfoClient.convert2Client(rsp.getRi());
		bli = rsp.getLog();
		// 副本兵变化 是在主城里，主动主城设置兵力
		Account.manorInfoClient.updateArmFromReturnInfo(ri);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public ActInfo getAi() {
		return ai;
	}

	public BattleLogInfo getBattleLogInfo() {
		return bli;
	}

	public List<HeroInfoClient> getHeroInfoClient() {
		return hics;
	}
}
