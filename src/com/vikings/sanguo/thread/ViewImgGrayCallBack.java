package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ViewImgGrayCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;

	private long time;

	private float width, height;

	public ViewImgGrayCallBack(String name, View v) {
		this(name, v, 0, 0);
	}

	public ViewImgGrayCallBack(String name, View v, float width, float height) {
		this.width = width;
		this.height = height;
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
		ImageUtil.setBgGray(v);
		if (width > 0 && height > 0)
			ViewUtil.adjustLayout(v, (int) width, (int) height);
	}

}
