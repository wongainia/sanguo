package com.vikings.sanguo.invoker;

import com.vikings.sanguo.exception.GameException;



abstract public class BackgroundInvoker extends BaseInvoker {

	
	@Override
	protected void beforeFire() {
	}

	@Override
	protected void afterFire() {
	}
	
	@Override
	protected String failMsg() {
		return null;
	}


	@Override
	protected String loadingMsg() {
		return null;
	}

	@Override
	protected void onFail(GameException exception) {
	}
	
}
