package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ViewImgScaleCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private View v;

	private long time;

	private float width = -1f, height = -1f;
	private float scale = -1;

	/**
	 * 异步加载图片，同时按最大长和宽进行适应性等比缩放。本身图片长和宽都小于最大值，不缩放 与
	 * {@link ViewUtil#adjustLayout(View, int, int)}相似
	 * 
	 * @param name
	 * @param stubName
	 *            默认图片
	 * @param v
	 * @param width
	 *            长最大值
	 * @param height
	 *            宽最大值
	 */
	public ViewImgScaleCallBack(String name, String stubName, View v,
			float width, float height) {
		time = System.currentTimeMillis();
		this.stubName = stubName;
		this.v = v;
		this.v.setTag(key, time);
		this.width = width;
		this.height = height;
		set(name);
	}

	public ViewImgScaleCallBack(String name, View v, float width, float height) {
		time = System.currentTimeMillis();
		this.v = v;
		this.v.setTag(key, time);
		this.width = width;
		this.height = height;
		set(name);
	}

	public ViewImgScaleCallBack(String name, View v, float scale) {
		time = System.currentTimeMillis();
		this.v = v;
		this.v.setTag(key, time);
		this.scale = scale;
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
		if (scale > 0) {
			int w = d.getIntrinsicWidth();
			int h = d.getIntrinsicHeight();
			ViewUtil.adjustLayout(v, (int) (w * scale), (int) (h * scale));
		} else if (width > 0 && height > 0) {
			ViewUtil.adjustLayout(v, (int) width, (int) height);
		}
	}
}
