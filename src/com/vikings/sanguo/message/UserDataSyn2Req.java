package com.vikings.sanguo.message;

import java.io.OutputStream;

import com.vikings.sanguo.protos.MsgReqUserDataSyn2;

public class UserDataSyn2Req extends BaseReq {

	private MsgReqUserDataSyn2 req;

	/**
	 * 
	 * @param synFlag
	 *            0 - 同步完整数据 1 - 同步变化数据
	 * @param dataType
	 *            数据类型标志(如:DATA_TYPE_ROLEINFO | DATA_TYPE_BAG, DATA_TYPE_ALL等)
	 */
	public UserDataSyn2Req(int synFlag, int dataType) {
		req = new MsgReqUserDataSyn2().setSynFlag(synFlag)
				.setDataType(dataType);
	}

	@Override
	public short cmd() {
		return (short) com.vikings.sanguo.protos.CMD.MSG_REQ_USER_DATA_SYN.getNumber();
	}

	@Override
	protected void toBytes(OutputStream out) {
		writeProbuf(req, out);
	}

}
