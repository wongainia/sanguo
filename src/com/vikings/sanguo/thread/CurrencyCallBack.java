/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-28 下午4:46:37
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.thread;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.ui.alert.ToActionTip;

public abstract class CurrencyCallBack implements CallBack{
	private int cost;
	private CallBack cb;
	
	public CurrencyCallBack(int cost) {
		this.cost = cost;
	}
	
	public CurrencyCallBack(int cost, CallBack cb) {
		this.cost = cost;
		this.cb = cb;
	}
	
	@Override
	public void onCall() {
		if (Account.user.getCurrency() < cost) {
			if (null != cb)
				cb.onCall();
			new ToActionTip(cost).show();
		} else
			handle();
	}

	public abstract void handle();
}
