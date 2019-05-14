package com.vikings.sanguo.thread;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.UserLocInvoker;

public class SetCountry implements Runnable {

	@Override
	public void run() {
		if (Account.user.getCountry() > 0) {
			if (null != Config.getController().getFiefMap())
				Config.getController().getFiefMap().getBattleMap()
						.stopLocaltionListener();
			return;
		} else {
			new UserLocInvoker().start();
		}
	}

}
