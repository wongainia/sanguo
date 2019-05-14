package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspBuildingReset;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class BuildingResetResp extends BaseResp {

	private ReturnInfoClient ri;

	private BuildingInfoClient building;

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBuildingReset resp = new MsgRspBuildingReset();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		building = BuildingInfoClient.convert(resp.getBi());
		ri = ReturnInfoClient.convert2Client(resp.getRi());
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public BuildingInfoClient getBuilding() {
		return building;
	}

}
