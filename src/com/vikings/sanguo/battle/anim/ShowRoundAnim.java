/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.

 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

public class ShowRoundAnim extends BaseAnim {

	public ShowRoundAnim(View v, Animation anim) {
		super(v, anim, true);

	}

	@Override
	protected void prepare() {
		ViewUtil.setMargin(view, 0, 0, 0, 0, Gravity.CENTER);
		int width = 0;
		int height = 0;

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		width = view.getMeasuredWidth();
		height = view.getMeasuredHeight();

		int l = (Config.screenWidth - width) / 2;
		int t = (Config.screenHeight - height) / 2;
		int r = l + width;
		int b = t + height;
		view.layout(l, t, r, b);
	}

	@Override
	protected void animationEnd() {
		super.animationEnd();
		view.clearAnimation();
		ViewUtil.setGone(view);
	}

}
