package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.MoveTroopInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspBuyBattleUnit;

/**
 * 
 * @author susong
 * 
 */
public class BuyBattleUnitResp extends BaseResp {
	private ReturnInfoClient ri;
	private MoveTroopInfoClient moveTroopInfo; // 获得的兵力
	private int pokeResult; // 翻牌奖励结果(对应服务器奖励)

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspBuyBattleUnit resp = new MsgRspBuyBattleUnit();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		moveTroopInfo = MoveTroopInfoClient.convert(resp.getMoveTroopInfo());
		pokeResult = resp.getPokerResult();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public MoveTroopInfoClient getMoveTroopInfo() {
		return moveTroopInfo;
	}

	public int getPokeResult() {
		return pokeResult;
	}

}
