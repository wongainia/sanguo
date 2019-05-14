package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MsgCheckIfInTargetBlackListRsp;

/**
 * 
 * @author susong
 * 
 */
public class BlackListCheckResp extends BaseResp {

	// 0 : 不在 1：在对象黑名单
	private int exit;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgCheckIfInTargetBlackListRsp rsp = new MsgCheckIfInTargetBlackListRsp();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		exit = rsp.getExist();
	}

	public int getExit() {
		return exit;
	}

}
