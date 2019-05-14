package com.vikings.sanguo.invoker;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.utils.ViewUtil;

public abstract class AnimInvoker extends BaseInvoker implements
		AnimationListener {

	protected View v;

	private Animation anim;

	private Object lock = null;

	private View block;

	// 动画已经运行次数
	private int runTimes = 0;

	private int minAnimRuntimes = 1;

	public AnimInvoker(View v, Animation anim, int minAnimRuntimes) {
		this.v = v;
		this.minAnimRuntimes = minAnimRuntimes;
		this.anim = anim;
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(1);
		anim.setAnimationListener(this);
		lock = new Object();
		block = Config.getController().findViewById(R.id.animBlock);
	}

	public AnimInvoker(View v, Animation anim) {
		this(v, anim, 1);
	}

	@Override
	protected void beforeFire() {
		Config.getController().getHeartBeat().stop();
		ViewUtil.setVisible(v);
		// 如果是帧动画背景 动起来先
		Drawable d = v.getBackground();
		if (d instanceof AnimationDrawable) {
			AnimationDrawable gif = (AnimationDrawable) d;
			gif.setOneShot(false);
			gif.start();
		}
		ViewUtil.setVisible(block);
		v.startAnimation(anim);
		ctr.setBackKeyValid(false);
	}

	protected void afterFire() {
		// 如果是帧动画背景 停止
		Drawable d = v.getBackground();
		if (d instanceof AnimationDrawable) {
			AnimationDrawable gif = (AnimationDrawable) d;
			gif.stop();
		}
		ViewUtil.setGone(v);
		ViewUtil.setGone(block);
		v.startAnimation(anim);
		try {
			anim.getClass().getMethod("cancel").invoke(anim);
		} catch (Exception e) {
		}
		v.clearAnimation();
		((ViewGroup) block).clearDisappearingChildren();
		ctr.setBackKeyValid(true);
		Config.getController().getHeartBeat().start();
	}

	protected abstract void doFire() throws GameException;

	@Override
	protected void fire() throws GameException {
		doFire();
		synchronized (lock) {
			while (runTimes < minAnimRuntimes)
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	protected String loadingMsg() {
		return null;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		synchronized (lock) {
			runTimes++;
			v.startAnimation(anim);
			lock.notifyAll();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

}
