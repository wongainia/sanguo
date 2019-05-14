/*
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-20 下午9:24:58
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

public class FallDownAnim extends BaseAnim {	
	private ViewGroup vg;
	private long start_time;
		
	public FallDownAnim(ViewGroup vg,View v, Animation anim,boolean isGone) {
		super(v, anim, isGone);	
		this.vg = vg;
	}

	@Override
	protected void prepare() 
	{
		start_time = System.currentTimeMillis();
	}
	
	@Override
	protected void animationEnd()
	{
		//Log.d("FallDownAnim", "倒下动画所用时间" + (System.currentTimeMillis()-start_time));
		view.clearAnimation();
		view.setVisibility(View.INVISIBLE);
		super.animationEnd();		
	}
}
