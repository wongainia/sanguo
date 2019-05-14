package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.protos.MsgRspAccountRestore2;

public class AccountRestore2Resp extends BaseResp {
	private List<AccountPswInfoClient> infos = new ArrayList<AccountPswInfoClient>();

	@Override
	protected void fromBytes(byte[] buf, int index) {
		MsgRspAccountRestore2 rsp = new MsgRspAccountRestore2();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.hasInfos())
			infos.addAll(AccountPswInfoClient.convert2List(rsp.getInfosList()));
	}

	public List<AccountPswInfoClient> getInfos() {
		return infos;
	}
}
