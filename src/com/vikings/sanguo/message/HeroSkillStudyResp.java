package com.vikings.sanguo.message;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.MsgRspHeroSkillStudy;

public class HeroSkillStudyResp extends BaseResp {
	private ReturnInfoClient ri;
	private int result; // 学习结果 1：成功 2：失败

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeroSkillStudy resp = new MsgRspHeroSkillStudy();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		ri = ReturnInfoClient.convert2Client(resp.getRi());
		result = resp.getStudyResult();
	}

	public ReturnInfoClient getRi() {
		return ri;
	}

	public boolean isSuccess() {
		return result == 1;
	}
}
