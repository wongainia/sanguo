package com.vikings.sanguo.message;

import java.io.OutputStream;
import java.util.List;

import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.ConditionNum;
import com.vikings.sanguo.protos.ConditionStr;
import com.vikings.sanguo.protos.MsgReqUserSearch;

public class UserSearchReq extends BaseReq {
	MsgReqUserSearch req;

	public UserSearchReq(ResultPage resultPage, List<ConditionNum> nums,
			List<ConditionStr> strs) {
		req = new MsgReqUserSearch().setStart(resultPage.getCurIndex())
				.setCount(Integer.valueOf(resultPage.getPageSize()))
				.setConditionNumsList(nums).setConditionStrsList(strs);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_SEARCH.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}
}
