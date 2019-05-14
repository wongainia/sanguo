package com.vikings.sanguo.invoker;

import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.network.HttpConnector;

public class LogInvoker extends BackgroundInvoker {

	private String op;

	public LogInvoker(String op) {
		this.op = op;
	}

	@Override
	public void start() {
		// 不用记日志了
	}

	@Override
	protected void fire() throws GameException {
		UserAccountClient user = Config.getAccountClient();
		if (user.getId() == 0 && Config.useFor == 0)
			HttpConnector.getInstance().uploadString(
					Config.getChannel() + "," + Config.getImsi() + ","
							+ Config.getImei() + "," + op + ","
							+ Config.getClientVer() + "\r\n",
					"http://www.vk51.com:8080/common/CPSActiveLog");
	}

	@Override
	protected void onOK() {
	}

}
