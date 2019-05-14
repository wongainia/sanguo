package com.vikings.sanguo.message;

import com.vikings.sanguo.model.ReturnInfoClient;

public class ResultInfoResp extends BaseResp {

	private ReturnInfoClient rsinfo;

	@Override
	protected void fromBytes(byte[] buf, int index) {

		rsinfo = new ReturnInfoClient();
//		Decoder.decodeResultInfo(buf, index, rsinfo);
	}

	/**
	 * 服务器返回一个ResultInfo时调用
	 * 
	 * @return
	 */
	public ReturnInfoClient getRsinfo() {
		return rsinfo;
	}

}
