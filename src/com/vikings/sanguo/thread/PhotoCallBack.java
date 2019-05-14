package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;

public class PhotoCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;

	private long time;

	public PhotoCallBack(String name, View v) {
		time = System.currentTimeMillis();
		this.v = v;
		this.v.setTag(key, time);
		set(name);
	}

	public PhotoCallBack(String name, String stub, View v) {
		if (null != Config.getController().getDrawable(stub))
			stubName = stub;
		time = System.currentTimeMillis();
		this.v = v;
		this.v.setTag(key, time);
		set(name);
	}

	@Override
	public void setImage(Drawable d) {
		long t = (Long) v.getTag(key);
		if (t != time)
			return;
		if (v.getBackground() != null)
			v.getBackground().setCallback(null);
		v.setBackgroundDrawable(d);
	}

	@Override
	public String getUrl() {
		return Config.iconUrl; 
	}
	
}
