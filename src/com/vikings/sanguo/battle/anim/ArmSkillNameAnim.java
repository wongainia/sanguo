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

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.TextView;

public class ArmSkillNameAnim extends BaseAnim{
	private String name;
	private TextView v;
	
	public ArmSkillNameAnim(TextView view, Animation anim, String name, boolean isGone) {
		super(view, anim, isGone);
		this.name = name;
		this.v = view;
	}

	@Override
	protected void prepare() {
		v.setText(name);		
	}
	
	@Override
	protected void animationEnd() {
		
	}
}
