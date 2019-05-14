package com.vikings.sanguo.invoker;

import java.io.IOException;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.Home;

public class CheckServerConfigBeforeEnterInvoker extends BackgroundInvoker {
	
	private Home home;
	byte[] bytes;
	
	public CheckServerConfigBeforeEnterInvoker(Home home) {
		this.home = home;
	}

	@Override
	protected void fire() throws GameException {
		try {
			bytes = HttpConnector.getInstance().httpGetBytes(
					Config.serverURl + "?" + System.currentTimeMillis());
			
		} catch (IOException e) {
			
		}
	}

	@Override
	protected void onOK() {
		ctr.getServerFileCache().update(bytes);
		if (null != home)
			home.refreshData();
	}
}
