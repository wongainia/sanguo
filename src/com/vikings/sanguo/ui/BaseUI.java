package com.vikings.sanguo.ui;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.controller.GameController;

abstract public class BaseUI {

	protected GameController controller;

	public BaseUI() {
		this.controller = Config.getController();
		bindField();
	}

	public View bindButton(int resId, OnClickListener listener) {
		View b = controller.findViewById(resId);
		b.setOnClickListener(listener);
		return b;
	}

	public View bindButton(View v, int resId, OnClickListener listener) {
		View b = v.findViewById(resId);
		b.setOnClickListener(listener);
		return b;
	}

	protected void bindField() {
	}

	public void startThread(Runnable r) {
		new Thread(r).start();
	}
}
