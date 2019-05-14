package com.vikings.sanguo.message;

import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.MsgRspFiefDataSyn;
import com.vikings.sanguo.protos.RichFiefInfo;

/**
 * 
 * @author susong
 * 
 */
public class FiefDataSynResp extends BaseResp {

	List<RichFiefInfo> infos;
	
	SyncData<LordFiefInfoClient>[] datas;
	
	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspFiefDataSyn rsp = new MsgRspFiefDataSyn();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		infos = rsp.getInfosList();
		SyncDataSet set  = new SyncDataSet();
		Decoder.decodeLordFiefInfos(rsp.getLordFiefInfos(), set);
		datas = set.lordFiefInfos;
	}

	public List<RichFiefInfo> getInfos() {
		return infos;
	}
	
	public SyncData<LordFiefInfoClient>[] getDatas() {
		return datas;
	}

	public void setDatas(SyncData<LordFiefInfoClient>[] datas) {
		this.datas = datas;
	}
	
	public void setInfos(List<RichFiefInfo> infos) {
		this.infos = infos;
	}
}
