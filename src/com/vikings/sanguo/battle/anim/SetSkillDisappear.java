/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-15 下午4:44:48
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;


import android.view.View;
import android.view.animation.Animation;

public class SetSkillDisappear extends BaseAnim{
	
	public SetSkillDisappear(View view, Animation anim) {  //, CallBack cb  , ViewGroup vg
		super(view, anim, true);
	}

	@Override
	protected void animationEnd() {  //Animation animation
		view.setTag(-1);
//		view.setLayoutParams(new LayoutParams(-1, 0));
//		ViewUtil.setGone(view);
	}
}
