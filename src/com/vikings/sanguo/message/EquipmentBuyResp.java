package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentBuy;

public class EquipmentBuyResp extends BaseResp {
	private ReturnInfoClient ric;
	private EquipmentInfoClient eic;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentBuy rsp = new MsgRspEquipmentBuy();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
		eic = EquipmentInfoClient.convert(rsp.getEquipmentInfo());
		Account.equipmentCache.update(eic);
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public EquipmentInfoClient getEic() {
		return eic;
	}

}
