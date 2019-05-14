/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午10:31:28
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.MsgReqUserRankInfo;
import com.vikings.sanguo.protos.UserRankType;

public class UserRankInfoReq extends BaseReq{
	private MsgReqUserRankInfo req;
	
	public UserRankInfoReq(UserRankType type, ResultPage resultPage) {
		req = new MsgReqUserRankInfo().setType(type)
				.setStart(resultPage.getCurIndex())
				.setCount(Integer.valueOf(resultPage.getPageSize())); 
	}
	
	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_RANK_INFO.getNumber();
	}

}
