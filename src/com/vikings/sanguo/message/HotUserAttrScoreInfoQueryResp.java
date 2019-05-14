package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FatSheepData;
import com.vikings.sanguo.protos.BriefFiefInfo;
import com.vikings.sanguo.protos.HotUserScoreInfo;
import com.vikings.sanguo.protos.MsgRspHotUserAttrScoreInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class HotUserAttrScoreInfoQueryResp extends BaseResp {
	private List<FatSheepData> datas = new ArrayList<FatSheepData>();
	private int total;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHotUserAttrScoreInfoQuery rsp = new MsgRspHotUserAttrScoreInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		total = rsp.getTotal();
		List<BriefFiefInfo> fiefInfos = rsp.getInfosList();
		List<HotUserScoreInfo> userInfos = rsp.getHotInfosList();
		for (BriefFiefInfo fiefInfo : fiefInfos) {
			FatSheepData data = new FatSheepData();
			BriefFiefInfoClient bfic = BriefFiefInfoClient.convert(fiefInfo);
			data.setBfic(bfic);
			for (HotUserScoreInfo userInfo : userInfos) {
				if (userInfo.getUserid().intValue() == fiefInfo.getUserid()
						.intValue()) {
					data.setHusi(userInfo);
					break;
				}
			}
			datas.add(data);
		}
	}

	public List<FatSheepData> getDatas() {
		return datas;
	}

	public int getTotal() {
		return total;
	}

	public List<BriefFiefInfoClient> getBfics() {
		List<BriefFiefInfoClient> list = new ArrayList<BriefFiefInfoClient>();
		for (FatSheepData data : datas) {
			list.add(data.getBfic());
		}
		return list;
	}

}
