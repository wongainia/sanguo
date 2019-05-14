package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;

public class AddrInvoker extends BaseInvoker {

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.network_anomalies);
	}

	@Override
	protected void onFail(GameException exception) {
	}

	@Override
	protected void fire() throws GameException {
		GameBiz.switchServer();
	}

	@Override
	protected void beforeFire() {
	}

	@Override
	protected String loadingMsg() {
		return null;
	}

	@Override
	protected void onOK() {
	}

}