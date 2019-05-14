package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.protos.HonorRankType;
import com.vikings.sanguo.protos.MsgRspHonorRankInfo;
import com.vikings.sanguo.ui.adapter.HeroHonorRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroHonorRankWindow extends HonorRankWindow {

	@Override
	protected String getTitle() {
		return "神将荣耀";
	}

	@Override
	protected String getHonorRule() {
		return "";
//		return CacheMgr.uiTextCache.getTxt(UITextProp.HERO_HONOR_RANK);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new HeroHonorRankAdapter();
	}

	@Override
	protected void setTopDesc(MsgRspHonorRankInfo rsp) {
		StringBuilder buf = new StringBuilder();
		if (rsp.hasSelfInfo() && null != rsp.getSelfInfo()
				&& rsp.getSelfInfo().hasHeroPropId()) {
			try {
				HeroProp hp = (HeroProp) CacheMgr.heroPropCache.get(rsp
						.getSelfInfo().getHeroPropId());
				// TODO 配置talent
				// HeroQuality hq = (HeroQuality)
				// CacheMgr.heroQualityCache.get(hp
				// .getType());
				HeroQuality hq = (HeroQuality) CacheMgr.heroQualityCache.get(1);
				buf.append("你的").append(StringUtil.getHeroTypeName(hq))
						.append(StringUtil.getHeroName(hp, hq))
						.append("统率提升了")
						.append(rsp.getSelfInfo().getRankData() + ",");
			} catch (GameException e) {
				e.printStackTrace();
			}
		} else {
			buf.append("昨日你的将领没有提高统率,");
		}

		setTopDescSuffix(buf);

		ViewUtil.setRichText(findViewById(R.id.topDesc), buf.toString());
	}

	@Override
	protected HonorRankType getHonorRankType() {
		return null;
		// return HonorRankType.HONOR_RANK_HERO;
	}

}
