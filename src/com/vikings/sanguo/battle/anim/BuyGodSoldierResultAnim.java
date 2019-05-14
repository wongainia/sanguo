/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-26 下午5:20:38
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.adapter.AnimAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class BuyGodSoldierResultAnim {
	private Animation graduallShow, gradualHide;
	private View result;

	public BuyGodSoldierResultAnim(View result) {
		graduallShow = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.gradually_fade);
		gradualHide = AnimationUtils.loadAnimation(Config.getController()
				.getUIContext(), R.anim.gradually_hide);
		graduallShow.setAnimationListener(new ShowAnimListener());
		this.result = result;
	}

	public void start() {
		result.clearAnimation();
		result.startAnimation(graduallShow);
	}
	
	class ShowAnimListener extends AnimAdapter {
		@Override
		public void onAnimationEnd(Animation animation) {
			gradualHide.setAnimationListener(new HideAnimListener());
			result.postDelayed(new Runnable() {
				@Override
				public void run() {
					result.startAnimation(gradualHide);
				}
			}, 300);

		}
	}

	class HideAnimListener extends AnimAdapter {

		@Override
		public void onAnimationEnd(Animation animation) {
			ViewUtil.setGone(result);
		}
	}
}