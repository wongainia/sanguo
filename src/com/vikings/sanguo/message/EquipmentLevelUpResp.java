package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentLevelup;

public class EquipmentLevelUpResp extends BaseResp {
	private ReturnInfoClient ric;
	private HeroInfoClient hic;
	private EquipmentInfoClient eic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentLevelup rsp = new MsgRspEquipmentLevelup();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
		if (rsp.hasHeroInfo()) {
			hic = HeroInfoClient.convert(rsp.getHeroInfo());
			Account.heroInfoCache.update(hic);
		}
		if (rsp.hasEquipmentInfo()) {
			eic = EquipmentInfoClient.convert(rsp.getEquipmentInfo());
			Account.equipmentCache.update(eic);
		}
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

	public HeroInfoClient getHic() {
		return hic;
	}

}
