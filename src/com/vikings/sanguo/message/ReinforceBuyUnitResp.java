/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-1 下午5:11:59
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.protos.MsgRspReinforceBuyUnit;

public class ReinforceBuyUnitResp extends BaseResp{
	private MoveTroopInfoClient mtic;
	private ReturnInfoClient ric;
	private KeyValue times = null;
	
	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspReinforceBuyUnit resp = new MsgRspReinforceBuyUnit();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		mtic = MoveTroopInfoClient.convert(resp.getMoveTroopInfo());
		ric = ReturnInfoClient.convert2Client(resp.getRi());
		if (resp.hasTimes())
			times = resp.getTimes();
	}

	public MoveTroopInfoClient getMoveTroopInfoClient() {
		return mtic;
	}
	
	public ReturnInfoClient getReturnInfoClient() {
		return ric;
	}
	
	public KeyValue getTimes() {
		return times;
	}
}
