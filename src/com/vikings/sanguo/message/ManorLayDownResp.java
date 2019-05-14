package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorLayDown;

/**
 * 
 * @author susong
 * 
 */
public class ManorLayDownResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorLayDown resp = new MsgRspManorLayDown();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		ManorInfoClient mic = ManorInfoClient.convert(resp.getMi());
		LordFiefInfoClient lord = LordFiefInfoClient.convert(resp.getLfi());
		FiefInfoClient fief = FiefInfoClient.convert(resp.getFi());
		
//		删除主城旧位置
		Account.richFiefCache.deleteFief(Account.manorInfoClient.getPos());
		
		Account.richFiefCache.update(lord, fief);
		Account.manorInfoClient.update(mic);
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

}
