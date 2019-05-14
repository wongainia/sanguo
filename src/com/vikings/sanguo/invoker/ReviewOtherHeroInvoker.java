/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-12 下午2:16:38
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.ui.window.OthersHeroDetailWindow;
import com.vikings.sanguo.utils.ListUtil;

public class ReviewOtherHeroInvoker extends BaseInvoker {
	private int userId;
	private long heroId;
	private OtherHeroInfoClient hero;

	public ReviewOtherHeroInvoker(int userId, long heroId) {
		this.userId = userId;
		this.heroId = heroId;
	}

	@Override
	protected String loadingMsg() {
		return "获取信息中";
	}

	@Override
	protected String failMsg() {
		return "获取信息失败";
	}

	@Override
	protected void fire() throws GameException {
		List<Long> ids = new ArrayList<Long>();
		ids.add(heroId);
		List<OtherHeroInfoClient> heros = GameBiz.getInstance()
				.otherUserHeroInfoQuery(userId, ids);
		if (!ListUtil.isNull(heros))
			hero = heros.get(0);
	}

	@Override
	protected void onOK() {
		if (null != hero)
			new OthersHeroDetailWindow().open(hero);
		else
			Config.getController().alert("查看将领失败", "该将领已经被[吞噬],无法查看", null,
					true);
	}
}