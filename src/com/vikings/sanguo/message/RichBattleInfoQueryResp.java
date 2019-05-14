package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.protos.MsgRspRichBattleInfoQuery;

/**
 * 
 * @author susong
 * 
 */
public class RichBattleInfoQueryResp extends BaseResp {
	private RichBattleInfoClient info;
	private List<OtherHeroInfoClient> heroInfos;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspRichBattleInfoQuery rsp = new MsgRspRichBattleInfoQuery();
		ProtobufIOUtil.mergeFrom(buf, rsp, rsp);
		info = RichBattleInfoClient.convert(rsp.getInfo());
		heroInfos = new ArrayList<OtherHeroInfoClient>();
		if (rsp.hasHeroInfos()) {
			for (int i = 0; i < rsp.getHeroInfosCount(); i++) {
				heroInfos.add(OtherHeroInfoClient.convert(rsp.getHeroInfos(i)));
			}
		}
	}

	public RichBattleInfoClient getInfo() {
		return info;
	}

	public List<OtherHeroInfoClient> getHeroInfos() {
		return heroInfos;
	}

}
