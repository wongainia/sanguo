package com.vikings.sanguo.widget;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class SlowOnClick implements OnClickListener {

	private long lastClickTime = 0;

	@Override
	public void onClick(View v) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime > getTime()) {
			lastClickTime = time;
			doOnClick(v);
		}
	}

	abstract public void doOnClick(View v);

	protected int getTime() {
		return 1500;
	}
}
