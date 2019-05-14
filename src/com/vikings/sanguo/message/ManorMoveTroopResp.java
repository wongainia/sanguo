package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspManorMoveTroop;

/**
 * 
 * @author susong
 * 
 */
public class ManorMoveTroopResp extends BaseResp {

	private ReturnInfoClient ri;
	
	private ManorInfoClient manorInfo;
	
	private LordFiefInfoClient lordFiefInfo;
	
	private FiefInfoClient fiefInfo;
	
	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspManorMoveTroop resp = new MsgRspManorMoveTroop();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		fiefInfo = FiefInfoClient.convert(resp.getFiefInfo());
		lordFiefInfo = LordFiefInfoClient.convert(resp.getLordFiefInfo());
		Account.richFiefCache.update(lordFiefInfo, fiefInfo);

		fiefInfo = FiefInfoClient.convert(resp.getSrcFiefInfo());
		Account.richFiefCache.update(null, fiefInfo);
		
		manorInfo = ManorInfoClient.convert(resp.getManorInfo());
		Account.manorInfoClient.update(manorInfo);
	}
	
	public ReturnInfoClient getRi() {
		return ri;
	}
}
