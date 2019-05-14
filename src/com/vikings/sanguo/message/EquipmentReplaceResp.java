package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentReplace;

public class EquipmentReplaceResp extends BaseResp {
	private HeroInfoClient targetHic;
	private HeroInfoClient srcHic; // 如果存在原使用者
	private EquipmentInfoClient eic; // 如果目标原来位置有装备

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentReplace rsp = new MsgRspEquipmentReplace();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		targetHic = HeroInfoClient.convert(rsp.getTargetHeroInfo());
		Account.heroInfoCache.update(targetHic);
		if (rsp.hasSrcHeroInfo()) {
			srcHic = HeroInfoClient.convert(rsp.getSrcHeroInfo());
			Account.heroInfoCache.update(srcHic);
		}
		if (rsp.hasReplacedEquipmentInfo()) {
			eic = EquipmentInfoClient.convert(rsp.getReplacedEquipmentInfo());
			Account.equipmentCache.add(eic);
		}
	}

	public HeroInfoClient getTargetHic() {
		return targetHic;
	}

	public HeroInfoClient getSrcHic() {
		return srcHic;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

}
