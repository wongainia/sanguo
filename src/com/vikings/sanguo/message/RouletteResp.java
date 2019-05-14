package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.protos.MsgRspRoulette;

public class RouletteResp extends BaseResp {
	private ReturnInfoClient ric;
	private List<KeyValue> list;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRoulette resp = new MsgRspRoulette();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		list = new ArrayList<KeyValue>();
		if (resp.hasInfos())
			list.addAll(resp.getInfosList());
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public List<KeyValue> getList() {
		return list;
	}

}
