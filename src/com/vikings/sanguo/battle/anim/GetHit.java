/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-12-15 下午7:21:16
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.utils.ViewUtil;

import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout.LayoutParams;

public class GetHit extends BaseAnim{
	private View amy;
	
	public GetHit(View view, View amy, Animation anim) {
		super(view, anim, true);
		this.amy = amy;
	}

	@Override
	protected void prepare() {
		ViewUtil.setHide(view);
		
		LayoutParams lp = (LayoutParams) amy.getLayoutParams();
		
		int amyW = amy.getMeasuredWidth();
		int viewW = view.getMeasuredWidth();
		
		int gap = ((amyW - viewW) >> 1);
		
		int l = amy.getLeft() + gap;
		int r = l + view.getWidth();
		int b = amy.getBottom();
		int t = b - view.getHeight();
		
		ViewUtil.setMarginLeft(view, l);
		view.layout(l, t, r, b);
		
//		if (0!= lp.leftMargin) 
//			ViewUtil.setMarginLeft(view, lp.leftMargin + gap);
//		else if (0 != lp.rightMargin)
//			ViewUtil.setMarginRight(view, lp.rightMargin + gap);
	}

	@Override
	protected void stop() {
//		Log.e("GetHit", "needGone = " + needGone);
		super.stop();
	}
}
