package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.protos.GuildSearchInfo;
import com.vikings.sanguo.protos.MsgRspGuildSearch;

/**
 * 
 * @author susong
 * 
 */
public class GuildSearchResp extends BaseResp {
	private List<GuildSearchInfoClient> infos = new ArrayList<GuildSearchInfoClient>();

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspGuildSearch rsp = new MsgRspGuildSearch();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.hasInfos() && rsp.getInfosCount() > 0) {
			for (GuildSearchInfo info : rsp.getInfosList()) {
				infos.add(new GuildSearchInfoClient(info));
			}
		}
	}

	public List<GuildSearchInfoClient> getInfos() {
		return infos;
	}

}
