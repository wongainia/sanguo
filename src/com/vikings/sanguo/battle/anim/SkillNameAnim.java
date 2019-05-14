/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-8 下午2:56:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;


import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

public class SkillNameAnim extends BaseAnim{
	private String name;
	private View v;
	private Drawable d;
	private boolean isDown;
	
	public SkillNameAnim(boolean isDown,View view, View v, Animation anim, String name, boolean isGone,Drawable d) {
		super(view, anim, isGone);
		this.name = name;
		this.v = view;
		this.d = d;
		this.isDown = isDown;
	}

	@Override
	protected void prepare() {
		if(isDown)
		{
			ViewUtil.setMargin(view, (int)(-86*Config.SCALE_FROM_HIGH),0,0,(int)(120*Config.SCALE_FROM_HIGH),Gravity.LEFT|Gravity.BOTTOM);			
		}
		else
		{
			ViewUtil.setMargin(view, 0,(int)(120*Config.SCALE_FROM_HIGH),(int)(-86*Config.SCALE_FROM_HIGH),0,Gravity.RIGHT|Gravity.TOP);
		}
		this.v.setBackgroundDrawable(d);
	}
	
	@Override
	protected void animationEnd() {
		if (null != v) {
			ViewUtil.setVisible(v);
		}
	}
}
