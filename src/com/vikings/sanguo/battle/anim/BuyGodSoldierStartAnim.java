/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-26 下午5:21:39
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.animation.AccelerateInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.AnimAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class BuyGodSoldierStartAnim {
	private View view;
	private int[] viewPoint;
	private int[] targetPoint;
	private CallBack cb;
	private static int cnt = 0;
	
	public BuyGodSoldierStartAnim(View view, int[] viewPoint, int[] targetPoint, CallBack cb) {
		this.view = view;
		this.viewPoint = viewPoint;
		this.targetPoint = targetPoint;
		this.cb = cb;
		cnt = 0;
	}
	
	public void startAnim() {
		Animation backAnim = AnimationUtils.loadAnimation(
				Config.getController().getUIContext(), R.anim.back_scale);
		backAnim.setAnimationListener(new StartAnimListener());
		view.startAnimation(backAnim);
	}
	
	class StartAnimListener extends AnimAdapter {
		@Override
		public void onAnimationEnd(Animation anim) {
			Animation frontAnim = AnimationUtils.loadAnimation(
					Config.getController().getUIContext(), R.anim.front_scale);
					
			frontAnim.setAnimationListener(new TranslateCenterAnimListener());
			ViewUtil.setVisible(view.findViewById(R.id.reverse));
			ViewUtil.setGone(view.findViewById(R.id.front));
			view.startAnimation(frontAnim);
		}
	}
	
	class TranslateCenterAnimListener extends AnimAdapter {
		@Override
		public void onAnimationEnd(Animation animation) {
			Animation anim = getAnimation(0, targetPoint[0] - viewPoint[0], 0, targetPoint[1] - viewPoint[1]);
			anim.setAnimationListener(new TranslateRoundAnimListener());
			view.startAnimation(anim);
		}
	}
	
	class TranslateRoundAnimListener extends AnimAdapter {
		@Override
		public void onAnimationEnd(Animation animation) {
			Animation anim = getAnimation(targetPoint[0] - viewPoint[0], 0, targetPoint[1] - viewPoint[1], 0);
			anim.setAnimationListener(new EndAnimListener());
			view.startAnimation(anim);
		}
	}
	
	class EndAnimListener extends AnimAdapter {

		@Override
		public void onAnimationEnd(Animation animation) {
			if (0 == cnt && null != cb) {
				cnt++;
				cb.onCall();
			}
		}
	}
	
	private Animation getAnimation(float fromXDelta, float toXDelta,
			float fromYDelta, float toYDelta) {

		TranslateAnimation animation = new TranslateAnimation(fromXDelta,
				toXDelta, fromYDelta, toYDelta);
		animation.setFillAfter(true);
		animation.setDuration(200);
		animation.setInterpolator(new AccelerateInterpolator());
		return animation;
	}
} 