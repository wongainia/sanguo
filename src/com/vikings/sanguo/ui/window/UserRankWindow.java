/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午11:08:17
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
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.model.UserRankInfoClient;
import com.vikings.sanguo.protos.MsgRspUserRankInfo;
import com.vikings.sanguo.protos.UserRankInfo;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.UserRankAdapter;
import com.vikings.sanguo.utils.ListUtil;

public class UserRankWindow extends RankWindow{
	@Override
	public void getServerData(ResultPage resultPage) throws GameException {
		MsgRspUserRankInfo rsp = GameBiz.getInstance().getUserRankInfo(
				rankProp.getUserRankType(), resultPage);

		resultPage.setTotal(rsp.getTotal());

		if (rsp.hasInfos()) {
			List<Integer> userIds = new ArrayList<Integer>();
			List<Integer> guilds = new ArrayList<Integer>();
			for (UserRankInfo it : rsp.getInfosList()) {
				if (!userIds.contains(it.getUserid()))
					userIds.add(it.getUserid());
				
				if (!guilds.contains(it.getGuildid()))
					guilds.add(it.getGuildid());
			}

			List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
			List<BriefGuildInfoClient> bgics = CacheMgr.bgicCache.get(guilds);
			List<UserRankInfoClient> urics = new ArrayList<UserRankInfoClient>();

			for (UserRankInfo it : rsp.getInfosList())
				urics.add(UserRankInfoClient.convert(it, ListUtil.getUser(users, it.getUserid()),
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
		return new UserRankAdapter(rankProp);
	}
}
