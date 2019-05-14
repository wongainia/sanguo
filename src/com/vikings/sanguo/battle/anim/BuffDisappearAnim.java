/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-13 下午6:30:08
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class BuffDisappearAnim extends BaseAnim{
	private Drawable d;
	private View iv;
	private ViewGroup vg;
	private ViewGroup window;
	
	private ImageView flyIv;
	private boolean isLeft;
	
	public BuffDisappearAnim(boolean isLeft,ViewGroup window,View view,ViewGroup vg,Animation anim, Drawable d) {
		super(view, anim, true);
		this.d = d;
		this.iv = view;
		this.vg = vg;
		this.window = window;
		this.isLeft = isLeft;
	}

	@Override
	protected void prepare() {		
		int[] location = new  int[2] ;
		//iv.getLocationOnScreen(location);
		iv.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
			
		flyIv = new ImageView(Config.getController().getUIContext());
		flyIv.setBackgroundDrawable(d);
						
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(isLeft)
		{
			lp.leftMargin = location[0];
			lp.topMargin = (int) (location[1] -65*Config.SCALE_FROM_HIGH);
			lp.gravity = Gravity.TOP ;
		}
		else
		{
			lp.leftMargin = location[0];
			lp.topMargin = (int) (location[1] +65*Config.SCALE_FROM_HIGH);
			lp.gravity = Gravity.TOP;
		}
		window.addView(flyIv,lp);
		flyIv.getLocationInWindow(location);     //获取在当前窗口内的绝对坐标
		flyIv.bringToFront();
		setView(flyIv);
		
	}
	
	@Override
	protected void animationEnd()
	{
		super.animationEnd();		
		flyIv.clearAnimation();
		flyIv.setVisibility(View.GONE);		
		//window.removeViewInLayout(flyIv);				
	}
}