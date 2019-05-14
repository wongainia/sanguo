package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.protos.MsgRspPlayerStaticUserDataQuery;
import com.vikings.sanguo.protos.StaticUserDataType;

/**
 * 
 * @author susong
 * 
 */
public class PlayerStaticUserDataQueryResp extends BaseResp {
	private MsgRspPlayerStaticUserDataQuery resp;
	private StaticUserDataType dataType;
	// 只用于统计计数
	private int total;
	// 赠送日志
	private List<LogInfoClient> logInfos;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		resp = new MsgRspPlayerStaticUserDataQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		dataType = resp.getDataType();
//		if (dataType == StaticUserDataType.STATIC_USER_DATA_TYPE_HARVEST) {
//		
//			total = resp.getTotal();
//		}
	}

	public StaticUserDataType getDataType() {
		return dataType;
	}

	public List<LogInfoClient> getLogInfos() {
		return logInfos;
	}

	public int getTotal() {
		return total;
	}

}
