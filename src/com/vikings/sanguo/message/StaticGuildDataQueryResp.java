package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.GuildLogInfoClient;
import com.vikings.sanguo.protos.GuildLogInfo;
import com.vikings.sanguo.protos.MsgRspStaticGuildDataQuery;
import com.vikings.sanguo.protos.StaticGuildDataType;

/**
 * 
 * @author susong
 * 
 */
public class StaticGuildDataQueryResp extends BaseResp {
	private StaticGuildDataType dataType;
	private List<GuildLogInfoClient> logs = new ArrayList<GuildLogInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspStaticGuildDataQuery rsp = new MsgRspStaticGuildDataQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		dataType = rsp.getDataType();
		logs=new ArrayList<GuildLogInfoClient>();
		if (rsp.getGuildLogInfosCount() > 0) {
			for (GuildLogInfo log : rsp.getGuildLogInfosList()) {
				logs.add(GuildLogInfoClient.convert(log));
			}
		}
	}

	public StaticGuildDataType getDataType() {
		return dataType;
	}

	public List<GuildLogInfoClient> getLogs() {
		return logs;
	}

}
