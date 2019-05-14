/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-10 下午5:52:53
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.thread.CallBack;

public class DelFavorateFief extends BaseInvoker {
	private long id;
	private CallBack callBack;

	public DelFavorateFief(long id, CallBack callBack) {
		this.id = id;
		this.callBack = callBack;
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.please_wait);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.DelFavorateFief_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		GameBiz.getInstance().favoriteFiefDel(id);
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		Account.isfavoriteFiefError = true;
	}

	@Override
	protected void onOK() {
		Config.getController().alert(
				ctr.getString(R.string.DelFavorateFief_onOK));
		Account.deleteFavoriteFief(id);
		if (null != callBack)
			callBack.onCall();
	}

}
