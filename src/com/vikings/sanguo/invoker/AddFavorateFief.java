/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-1-10 下午4:13:57
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

public class AddFavorateFief extends BaseInvoker {
	private long id;
	private com.vikings.sanguo.thread.CallBack callBack;

	public AddFavorateFief(long id) {
		this.id = id;
	}

	public AddFavorateFief(long id,
			com.vikings.sanguo.thread.CallBack callBack) {
		this.id = id;
		this.callBack = callBack;
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.please_wait);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.AddFavorateFief_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		GameBiz.getInstance().favoriteFiefAdd(id);
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		Account.isfavoriteFiefError = true;
	}

	@Override
	protected void onOK() {
//		new AddFavorateFiefSucTip().show();
		Config.getController().alert("该领地已经成功添加至领地收藏夹");
		Account.addFavoriteFief(id);
		if (null != callBack)
			callBack.onCall();
	}

}
