package com.vikings.sanguo.invoker;

import java.io.IOException;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.Home;

public class CheckServerConfigInvoker extends BaseInvoker {
	private Home home;

	public CheckServerConfigInvoker(Home home) {
		this.home = home;
	}

	@Override
	protected void beforeFire() {
	}

	@Override
	protected void fire() throws GameException {
		try {
			byte[] bytes = HttpConnector.getInstance().httpGetBytes(
					Config.serverURl + "?" + System.currentTimeMillis());
			ctr.getServerFileCache().update(bytes);
		} catch (IOException e) {

		}
	}

	@Override
	protected void onOK() {
		home.refreshData();
		home.checkVer();
	}

	@Override
	protected String loadingMsg() {
		return "更新服务器列表";
	}

	@Override
	protected String failMsg() {
		return "更新服务器列表失败";
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		home.showMenu();
	}
}
