/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-29 下午6:04:18
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.thread.CallBack;

public class GetAltarInvoker extends BaseInvoker {
	private CallBack okCb;
	private CallBack failCb;
	
	public GetAltarInvoker(CallBack okCb, CallBack failCb) {
		this.okCb = okCb;
		this.failCb = failCb;
	}
	
	@Override
	protected String loadingMsg() {
		return "获取家族总坛信息中";
	}

	@Override
	protected String failMsg() {
		return "获取家族总坛失败";
	}

	@Override
	protected void fire() throws GameException {
		long fiefId = Account.guildCache.getRichGuildInfoClient().getGic().getAltarId();
		if (fiefId > 0) {
			List<Long> ids = new ArrayList<Long>();
			ids.add(fiefId);
			List<BriefFiefInfoClient> fiefs = GameBiz.getInstance()
					.briefFiefInfoQuery(ids);
			if (!ListUtil.isNull(fiefs))
				Account.guildAltar = fiefs.get(0);
		}
	}

	@Override
	protected void onOK() {
		if (null != okCb)
			okCb.onCall();
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		if (null != failCb)
			failCb.onCall();
	}
}
