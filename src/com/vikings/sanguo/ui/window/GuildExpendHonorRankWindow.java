package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.ui.adapter.GuildExpendHonorRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildExpendHonorRankWindow extends HonorRankWindow {

	@Override
	protected String getTitle() {
		return "威震江湖";
	}

	@Override
	protected String getHonorRule() {
		return CacheMgr.uiTextCache.getTxt(UITextProp.GUILD_EXPEND_HONOR_RANK);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new GuildExpendHonorRankAdapter();
	}

	@Override
	protected void setTopDesc(MsgRspHonorRankInfo rsp) {
		StringBuilder buf = new StringBuilder();
		if (rsp.hasSelfInfo())
			buf.append("你的家族昨日消耗元宝").append(
					rsp.getSelfInfo().getRankData() + ",");
		else
			buf.append("你的家族昨日没有消耗元宝,");

		setTopDescSuffix(buf);

		ViewUtil.setRichText(findViewById(R.id.reward_spec),
				StringUtil.color(buf.toString(), R.color.color11));
	}

	@Override
	protected HonorRankType getHonorRankType() {
		return HonorRankType.HONOR_RANK_GUILD_COST;
	}

}
