package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.protos.MsgRspRichGuildInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class RichGuildInfoQueryResp extends BaseResp {
	private RichGuildInfoClient rgic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRichGuildInfoQuery rsp = new MsgRspRichGuildInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		rgic = RichGuildInfoClient.convert(rsp.getInfo());
	}

	public RichGuildInfoClient getRgic() {
		return rgic;
	}

}
