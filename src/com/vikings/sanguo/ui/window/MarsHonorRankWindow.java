package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.ui.adapter.MarsHonorRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class MarsHonorRankWindow extends HonorRankWindow {

	@Override
	protected String getTitle() {
		return "铁血战神";
	}

	@Override
	protected String getHonorRule() {
		return CacheMgr.uiTextCache.getTxt(UITextProp.MARS_HONOR_RANK);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new MarsHonorRankAdapter();
	}

	@Override
	protected void setTopDesc(MsgRspHonorRankInfo rsp) {
		StringBuilder buf = new StringBuilder();
		if (rsp.hasSelfInfo())
			buf.append("你昨日击杀").append(rsp.getSelfInfo().getRankData())
					.append("名敌军,");
		else
			buf.append("你昨日没有击杀敌军,");

		setTopDescSuffix(buf);

		ViewUtil.setRichText(findViewById(R.id.reward_spec),
				StringUtil.color(buf.toString(), R.color.color11));
	}

	@Override
	protected HonorRankType getHonorRankType() {
		return HonorRankType.HONOR_RANK_MARS;
	}

}
