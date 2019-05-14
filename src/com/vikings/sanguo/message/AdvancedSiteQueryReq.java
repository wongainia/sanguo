/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-28 下午4:17:00
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;
import com.vikings.sanguo.protos.MsgReqAdvancedSiteQuery;

public class AdvancedSiteQueryReq extends BaseReq{

	private MsgReqAdvancedSiteQuery req;

	public AdvancedSiteQueryReq(int propId, int start, int count) {
		req = new MsgReqAdvancedSiteQuery().setPropid(propId).setStart(start).setCount(count);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_ADVANCED_SITE_QUERY
				.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
