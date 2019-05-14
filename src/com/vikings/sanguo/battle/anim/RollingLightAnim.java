/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-3 下午2:47:36
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;

public abstract class RollingLightAnim {
	private Animation scale;
	protected View light;
	private int angle = 360;
	
	public RollingLightAnim(View v) {
		this.light = v;
		
		scale = new ScaleAnimation(0f, 1.0f, 0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(100);

		final Animation rotate = new RotateAnimation(0, angle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(2000);
		rotate.setInterpolator(new DecelerateInterpolator());

		scale.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				light.startAnimation(rotate);
			}
		});

		rotate.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animationEnd();
			}
		});
	}
	
	public RollingLightAnim(View v, int angle) {
		this(v);
		this.angle = angle;
	}
	
	public abstract void animationEnd(); 
	
	public void start() {
		if (null != light)
			light.startAnimation(scale);
	}
}
