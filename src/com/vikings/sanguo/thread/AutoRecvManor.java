package com.vikings.sanguo.thread;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;

public class AutoRecvManor {

	/**
	 * 自动收获间隔10min
	 */
	private static final int interval = 10 * 60;

	public static void check() {
		if (Account.user == null)
			return;
		if (Config.serverTimeSS() - Account.user.getLastRecvAllTime() < interval)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Account.user.setLastRecvAllTime(Config.serverTimeSS());
				try {
					GameBiz.getInstance().manorReceiveAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
