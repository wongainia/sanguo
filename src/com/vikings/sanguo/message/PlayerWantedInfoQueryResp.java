package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.protos.MsgRspPlayerWantedInfoQuery;
import com.vikings.sanguo.protos.PlayerWantedInfo;

/**
 * 
 * @author susong
 * 
 */
public class PlayerWantedInfoQueryResp extends BaseResp {
	private List<PlayerWantedInfoClient> infos = new ArrayList<PlayerWantedInfoClient>();

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspPlayerWantedInfoQuery rsp = new MsgRspPlayerWantedInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		if (rsp.hasInfos() && rsp.getInfosCount() > 0) {
			for (PlayerWantedInfo info : rsp.getInfosList()) {
				infos.add(new PlayerWantedInfoClient(info));
			}
		}
	}

	public List<PlayerWantedInfoClient> getInfos() {
		return infos;
	}

}
