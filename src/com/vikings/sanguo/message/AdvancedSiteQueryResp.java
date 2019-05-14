/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-28 下午4:22:02
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgRspAdvancedSiteQuery;

public class AdvancedSiteQueryResp extends BaseResp{
	private List<Long> fiefIds = new ArrayList<Long>();;
	
	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspAdvancedSiteQuery rsp = new MsgRspAdvancedSiteQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.hasFiefids())
			fiefIds = rsp.getFiefidsList();
	}
	
	public List<Long> getFiefIds() {
		return fiefIds;
	}
}
