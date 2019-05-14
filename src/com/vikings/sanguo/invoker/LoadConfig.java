package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.ui.Home;

public class LoadConfig extends BaseInvoker {

	private Home home;

	public LoadConfig(Home home) {
		this.home = home;
	}

	@Override
	protected void beforeFire() {
	}

	@Override
	protected String failMsg() {
		return "";
	}

	@Override
	protected void fire() throws GameException {
		CacheMgr.init();
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.LoadConfig_loadingMsg);
	}

	@Override
	protected void onOK() {
		new LogInvoker("完成载入配置").start();
		home.login();
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		home.showMenu();
	}

}
