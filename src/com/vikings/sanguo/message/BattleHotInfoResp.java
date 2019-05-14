package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BattleHotInfoClient;
import com.vikings.sanguo.protos.MsgRspBattleHotInfo;

/**
 * 
 * @author susong
 * 
 */
public class BattleHotInfoResp extends BaseResp {
	List<BattleHotInfoClient> infos = new ArrayList<BattleHotInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBattleHotInfo rsp = new MsgRspBattleHotInfo();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		infos.addAll(BattleHotInfoClient.convertList(rsp.getInfosList()));
	}

	public List<BattleHotInfoClient> getInfos() {
		return infos;
	}

}
