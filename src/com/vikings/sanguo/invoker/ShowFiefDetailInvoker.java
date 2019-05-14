/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-26 下午3:48:51
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
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.utils.ListUtil;

public class ShowFiefDetailInvoker extends BaseInvoker {
	private BriefFiefInfoClient bfic;
	private long fiefId;
	
	public ShowFiefDetailInvoker(long fiefId) {
		this.fiefId = fiefId;
	}
	
	@Override
	protected String loadingMsg() {
		return "正在加载数据";
	}

	@Override
	protected String failMsg() {
		return "获取数据失败";
	}

	@Override
	protected void fire() throws GameException {
		List<Long> ids = new ArrayList<Long>();
		ids.add(fiefId);
		List<BriefFiefInfoClient> ls = GameBiz.getInstance()
				.briefFiefInfoQuery(ids);
		if (!ListUtil.isNull(ls))
			bfic = ls.get(0);
	}

	@Override
	protected void onOK() {
		Config.getController().openFiefDetailWindow(bfic);
	}
}
