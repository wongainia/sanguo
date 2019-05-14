package com.vikings.sanguo.invoker;

import com.vikings.sanguo.thread.CallBack;

public abstract class ConfirmInvoker extends BaseInvoker implements CallBack {

	abstract protected String confirmMsg();

	@Override
	public void onCall() {
		super.start();
	}

	@Override
	public void start() {
		ctr.confirm(confirmMsg(), this);
	}

}
