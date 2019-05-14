package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.LordFiefInfo;
import com.vikings.sanguo.protos.MsgRspManorReceiveAll;

/**
 * 
 * @author susong
 * 
 */
public class ManorReceiveAllResp extends BaseResp {
	private ReturnInfoClient ri;
	private ManorInfoClient mic;

	private List<Integer> lastAttrId;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorReceiveAll resp = new MsgRspManorReceiveAll();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		mic = ManorInfoClient.convert(resp.getMi());
		Account.manorInfoClient.update(mic);
		for (LordFiefInfo li : resp.getLfisList()) {
			Account.richFiefCache.update(LordFiefInfoClient.convert(li), null);
		}
		lastAttrId = resp.getLastAttridsList();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public List<Integer> getLastAttrId() {
		return lastAttrId;
	}

}
