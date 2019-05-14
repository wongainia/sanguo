package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentDisarm;

public class EquipmentDisarmResp extends BaseResp {
	private HeroInfoClient hic;
	private EquipmentInfoClient eic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentDisarm rsp = new MsgRspEquipmentDisarm();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		hic = HeroInfoClient.convert(rsp.getHeroInfo());
		eic = EquipmentInfoClient.convert(rsp.getReplacedEquipmentInfo());
		// TODO 跟新自己的装备数据
	}

	public HeroInfoClient getHic() {
		return hic;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

}
