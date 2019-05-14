package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentForge;

public class EquipmentForgeResp extends BaseResp {
	private ReturnInfoClient ric;
	private HeroInfoClient hic; // 如果装备被使用
	private EquipmentInfoClient eic; // 如果装备未使用

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentForge rsp = new MsgRspEquipmentForge();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
		if (rsp.hasHeroInfo()) {
			hic = HeroInfoClient.convert(rsp.getHeroInfo());
			Account.heroInfoCache.update(hic);
		}
		if (rsp.hasEquipmentInfo()) {
			eic = EquipmentInfoClient.convert(rsp.getEquipmentInfo());
			Account.equipmentCache.update(eic); // 跟新自己的装备数据
		}
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public HeroInfoClient getHic() {
		return hic;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

}
