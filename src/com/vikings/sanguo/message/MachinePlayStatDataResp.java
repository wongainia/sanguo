package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.MachinePlayStatInfo;
import com.vikings.sanguo.protos.MsgRspMachinePlayStatData;

public class MachinePlayStatDataResp extends BaseResp {

	private List<MachinePlayStatInfo> gambleStatInfo = new ArrayList<MachinePlayStatInfo>();

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspMachinePlayStatData resp = new MsgRspMachinePlayStatData();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		gambleStatInfo = new ArrayList<MachinePlayStatInfo>();
		gambleStatInfo.addAll(resp.getMachinePlayStatInfosList());
	}

	public List<MachinePlayStatInfo> getGambleStatInfo() {
		return gambleStatInfo;
	}
}
