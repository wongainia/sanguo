package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.protos.MsgRspOtherRichGuildInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class OtherRichGuildInfoQueryResp extends BaseResp {
	private OtherRichGuildInfoClient orgic;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspOtherRichGuildInfoQuery resp = new MsgRspOtherRichGuildInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		orgic = OtherRichGuildInfoClient.convert(resp.getInfo());
	}

	public OtherRichGuildInfoClient getOrgic() {
		return orgic;
	}

}
