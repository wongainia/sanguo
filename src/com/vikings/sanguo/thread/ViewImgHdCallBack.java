package com.vikings.sanguo.thread;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;

import com.vikings.sanguo.R;

public class ViewImgHdCallBack extends ImageCallBack {

	private static final int key = R.id.icon;

	private ViewGroup viewGroup;
	private View hdImg;

	private long time;

	private CallBack callBack;

	public ViewImgHdCallBack(String name, ViewGroup viewGroup, View hdImg,
			CallBack callBack, CallBack failCallBack) {
		time = System.currentTimeMillis();
		this.viewGroup = viewGroup;
		this.hdImg = hdImg;
		this.callBack = callBack;
		setFailCallBack(failCallBack);
		this.viewGroup.setTag(key, time);
		set(name);
	}
	
	public ViewImgHdCallBack(String name, ViewGroup viewGroup, View hdImg,
			CallBack callBack, CallBack failCallBack,boolean isLowQuality) {
		time = System.currentTimeMillis();
		this.viewGroup = viewGroup;
		this.hdImg = hdImg;
		this.callBack = callBack;
		setFailCallBack(failCallBack);
		this.viewGroup.setTag(key, time);
		setLowQuality(name);
	}

	@Override
	public void setImage(Drawable d) {
		if (d == null || viewGroup == null || null == hdImg)
			return;
		long t = (Long) viewGroup.getTag(key);
		if (t != time)
			return;
		if (viewGroup.getBackground() != null)
			viewGroup.getBackground().setCallback(null);
		if (viewGroup.getLayoutParams() instanceof LayoutParams) {
			LayoutParams params = (LayoutParams) viewGroup.getLayoutParams();
			params.width = d.getIntrinsicWidth();
			params.height = d.getIntrinsicHeight();
		}

		if (null != hdImg) {
			hdImg.setBackgroundDrawable(d);
		}

		if (callBack != null) {
			callBack.onCall();
		}
	}

	public void recycle() {
		if (hdImg == null)
			return;

		Drawable d = hdImg.getBackground();
		if (d == null)
			return;
		hdImg.setBackgroundDrawable(null);
		if (d instanceof BitmapDrawable) {
			recycleBitmapDrawable((BitmapDrawable) d);
		} else if (d instanceof AnimationDrawable) {
			((AnimationDrawable) d).stop();
			int frames = ((AnimationDrawable) d).getNumberOfFrames();
			for (int i = 0; i < frames; i++) {
				Drawable frameDrawable = ((AnimationDrawable) d).getFrame(i);
				if (frameDrawable instanceof BitmapDrawable)
					recycleBitmapDrawable((BitmapDrawable) frameDrawable);
			}
		}
	}

	private void recycleBitmapDrawable(BitmapDrawable drawable) {
		Bitmap b = drawable.getBitmap();
		if (b != null && !b.isRecycled())
			b.recycle();
	}
}
