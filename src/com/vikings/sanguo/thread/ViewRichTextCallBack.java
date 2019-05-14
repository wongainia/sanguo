package com.vikings.sanguo.thread;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ViewRichTextCallBack extends ImageCallBack {
	private static final int key = R.id.icon;
	private View v;
	private long time;
	private String prefixText;
	private String suffixText;
	int width;
	int height;

	public ViewRichTextCallBack(String needDownLoadImag, String defImage,
			String prefixText, String suffixText, View v, int width, int height) {
		ViewUtil.setRichText(v, getRichText(prefixText, defImage, suffixText));
		time = System.currentTimeMillis();
		this.stubName = "";
		this.v = v;
		this.v.setTag(key, time);
		this.prefixText = prefixText;
		this.suffixText = suffixText;
		this.width = width;
		this.height = height;
		set(needDownLoadImag);

	}

	private String getRichText(String prefixText, String ImageName,
			String suffixText) {
		return prefixText + "#" + ImageName + "#" + suffixText;
	}

	@Override
	public void setImage(Drawable d) {
		long t = (Long) v.getTag(key);
		if (t != time)
			return;
		if (d == null)
			return;
		if (v.getBackground() != null)
			v.getBackground().setCallback(null);

		ViewUtil.setRichText(v, getRichText(prefixText, imgName, suffixText),
				true, width, height);
	}
}
