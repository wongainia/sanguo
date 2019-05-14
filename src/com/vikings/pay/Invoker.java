package com.vikings.pay;

import android.os.Handler;

abstract public class Invoker implements Runnable {

	private Handler handler = new Handler();

	@Override
	public void run() {
		work();
		handler.post(new Runnable() {

			@Override
			public void run() {
				onOK();
			}
		});
	}

	abstract void work();

	abstract void onOK();

	public void startJob() {
		new Thread(this).start();
	}
	
}
