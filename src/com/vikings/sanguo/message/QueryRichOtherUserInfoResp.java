package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.protos.MsgQueryRichOtherUserInfoRsp;

/**
 * 
 * @author susong
 * 
 */
public class QueryRichOtherUserInfoResp extends BaseResp {

	private OtherUserClient other;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgQueryRichOtherUserInfoRsp rsp = new MsgQueryRichOtherUserInfoRsp();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		other = OtherUserClient.convert(rsp.getUi(), rsp.getLi(), rsp.getMi(),
				rsp.getTroopEffectInfo());
		CacheMgr.userCache.updateCache(other.bref());
	}

	public OtherUserClient getOther() {
		return other;
	}

}
