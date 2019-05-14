/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 下午3:08:13
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.HeroRankInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.protos.HeroRankInfo;
import com.vikings.sanguo.protos.MsgRspHeroRankInfo;
import com.vikings.sanguo.ui.adapter.HeroRankAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ListUtil;

public class HeroRankWindow extends RankWindow{
	
	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		MsgRspHeroRankInfo rsp = GameBiz.getInstance().getHeroRankInfo(resultPage);

		resultPage.setTotal(rsp.getTotal());

		if (rsp.hasInfos()) {
			List<Integer> guilds = new ArrayList<Integer>();
			for (HeroRankInfo it : rsp.getInfosList()) {
				if (!guilds.contains(it.getGuildid()))
					guilds.add(it.getGuildid());
			}

			List<BriefGuildInfoClient> bgics = CacheMgr.bgicCache.get(guilds);
			List<HeroRankInfoClient> urics = new ArrayList<HeroRankInfoClient>();

			for (HeroRankInfo it : rsp.getInfosList())
				urics.add(HeroRankInfoClient.convert(it,
						getBriefGuildInfoClient(it.getGuildid(), bgics)));

			resultPage.setResult(urics);
		}
	}

	private BriefGuildInfoClient getBriefGuildInfoClient(int id, List<BriefGuildInfoClient> bgics) {
		if (ListUtil.isNull(bgics))
			return null;
		
		for (BriefGuildInfoClient it : bgics) {
			if (id == it.getId())
				return it;
		}
		
		return null;
	}
	
	@Override
	public void handleItem(Object o) {
		
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new HeroRankAdapter(rankProp);
	}
}