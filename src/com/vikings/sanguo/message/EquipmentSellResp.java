package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspEquipmentSell;

public class EquipmentSellResp extends BaseResp {
	private long id;
	private ReturnInfoClient ric;

	public EquipmentSellResp(long id) {
		this.id = id;
	}

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspEquipmentSell rsp = new MsgRspEquipmentSell();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		ric = ReturnInfoClient.convert2Client(rsp.getRi());
		Account.equipmentCache.remove(id);
	}

	public ReturnInfoClient getRic() {
		return ric;
	}
}
