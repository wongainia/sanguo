package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.protos.ManorInfo;
import com.vikings.sanguo.protos.MsgRspBriefFiefInfoQuery;
import com.vikings.sanguo.protos.UserAttrScoreInfo;

/**
 * 
 * @author susong
 * 
 */
public class BriefFiefInfoQueryResp extends BaseResp {
	private List<BriefFiefInfoClient> infos = new ArrayList<BriefFiefInfoClient>();

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBriefFiefInfoQuery rsp = new MsgRspBriefFiefInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		infos.addAll(BriefFiefInfoClient.convert2List(rsp.getInfosList()));
		// 如果是主城 返回主城的manor 组装
		for (BriefFiefInfoClient b : infos) {
			if(!b.isCastle())continue;
			if (Account.richFiefCache.isMyFief(b.getId())) {
				b.setManor(Account.manorInfoClient);
			} else {
				for (ManorInfo m : rsp.getManorInfosList()) {
					if (m.getBi().getPos() == b.getId()) {
						b.setManor(ManorInfoClient.convert(m));
						break;
					}
				}
			}

			for (UserAttrScoreInfo info : rsp.getAttrInfosList()) {
				if (info.getUserid().intValue() == b.getUserId()) {
					b.setUserAttrScoreInfo(info);
				}
			}
		}
	}

	public List<BriefFiefInfoClient> getInfos() {
		return infos;
	}
}
