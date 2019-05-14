package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.R;

public class ViewImgCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;

	private long time;

	private CallBack callBack;

	public ViewImgCallBack(String name, String stubName, View v) {
		time = System.currentTimeMillis();
		this.stubName = stubName;
		this.v = v;
		this.v.setTag(key, time);
		set(name);
	}

	public ViewImgCallBack(String name, View v) {
		time = System.currentTimeMillis();
		this.v = v;
		this.v.setTag(key, time);
		set(name);
	}

	public ViewImgCallBack(String name, View v, CallBack callBack,
			CallBack failCallBack) {
		time = System.currentTimeMillis();
		this.v = v;
		this.callBack = callBack;
		setFailCallBack(failCallBack);
		this.v.setTag(key, time);
		set(name);
	}

	@Override
	public void setImage(Drawable d) {
		if(v == null)
		{
			return;
		}		
		if (d == null)
			return;
		long t = (Long) v.getTag(key);
		if (t != time)
			return;
		if(v != null)
		{
			if (v.getBackground() != null)
				v.getBackground().setCallback(null);
		v.setBackgroundDrawable(d);
		}
		if (callBack != null) {
			callBack.onCall();
		}
	}
}
