package com.vikings.sanguo.ui.gif;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 分帧png组合动画
 * 
 * @author Brad.Chen
 * 
 */
public class GifView {

	private AnimationDrawable gif;
	private View view;

	public GifView(View v) {
		view = v;
		gif = (AnimationDrawable) view.getBackground();
		gif.setOneShot(false);
	}

	public void show() {
		if (gif.isRunning())
			return;
		else
			start();
	}

	public void stop() {
		if (gif.isRunning())
			gif.stop();
		ViewUtil.setGone(view);
	}

	public void start() {
		if (gif.isRunning())
			gif.stop();
		if (!ViewUtil.isVisible(view))
			ViewUtil.setVisible(view);
		gif.start();
	}

	public void setImage(int resId) {
		view.setBackgroundResource(resId);
		gif = (AnimationDrawable) view.getBackground();
		gif.setOneShot(false);
	}

	public static void startGif(View v) {
		stopGif(v);
		Drawable d = v.getBackground();
		if (!(d instanceof AnimationDrawable))
			return;
		AnimationDrawable gif = (AnimationDrawable) d;
		gif.setOneShot(false);
		gif.start();
	}

	public static void startGif(View v, int resId) {
		stopGif(v);
		v.setBackgroundDrawable(Config.getController().getDrawable(resId));
		Drawable d = v.getBackground();
		if (!(d instanceof AnimationDrawable))
			return;
		AnimationDrawable gif = (AnimationDrawable) d;
		gif.setOneShot(false);
		gif.start();
	}

	public static void startGif(View v, String name) {
		stopGif(v);
		v.setBackgroundDrawable(Config.getController().getDrawable(name));
		Drawable d = v.getBackground();
		if (!(d instanceof AnimationDrawable))
			return;
		AnimationDrawable gif = (AnimationDrawable) d;
		gif.setOneShot(false);
		gif.start();
	}

	public static void stopGif(View v) {
		Drawable d = v.getBackground();
		if (!(d instanceof AnimationDrawable))
			return;
		AnimationDrawable gif = (AnimationDrawable) d;
		gif.stop();
	}

}
