/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-12-6 下午2:40:43
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.graphics.drawable.Drawable;
import com.vikings.sanguo.utils.ViewUtil;
import android.view.View;
import android.view.animation.Animation;

public class GetBeaten extends BaseAnim{
	private Drawable d;
	private int x;
	
	public GetBeaten(View view, Animation anim, Drawable d, int x, String soundName) {
		super(view, anim, true);
		this.d = d;
		this.x = x;
		setSoundName(soundName);
	}

	@Override
	protected void prepare() {
		view.setBackgroundDrawable(d);
		if (0 != x) {
			ViewUtil.setMarginLeft(view, x);
			int r = x + d.getIntrinsicWidth();
			int b = view.getBottom();
			int t = b - d.getIntrinsicHeight();
			view.layout(x, t, r, b);
		}
			
	}
}
