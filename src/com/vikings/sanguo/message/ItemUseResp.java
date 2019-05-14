package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspItemUse;

public class ItemUseResp extends BaseResp {
	private ReturnInfoClient ri;

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspItemUse resp = new MsgRspItemUse();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		
//		更新武将
		if (resp.hasHis()) {
			for (int i = 0; i < resp.getHisCount(); i++) {
				if (resp.getHis(i).getBi().getId() > 0)
					Account.heroInfoCache.update(HeroInfoClient.convert(resp.getHis(i)));
			}
		}
		
//		更新建筑 和兵力 
		if(resp.hasMi()){
			Account.manorInfoClient.update(ManorInfoClient.convert(resp.getMi()));
		}
		
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		
	}

	public ReturnInfoClient getRi() {
		return ri;
	}
}
