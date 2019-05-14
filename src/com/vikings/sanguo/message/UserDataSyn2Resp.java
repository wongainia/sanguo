package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.MsgRspUserDataSyn2;
import com.vikings.sanguo.protos.RichUserInfo;

/**
 * 
 * @author susong
 * 
 */
public class UserDataSyn2Resp extends BaseResp {
	private SyncDataSet syncDataSet;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspUserDataSyn2 rsp = new MsgRspUserDataSyn2();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		RichUserInfo info = rsp.getUserInfo();
		syncDataSet = new SyncDataSet();
		Decoder.decodeRichUserInfo(info, syncDataSet);
	}

	public SyncDataSet getSyncDataSet() {
		return syncDataSet;
	}
}
