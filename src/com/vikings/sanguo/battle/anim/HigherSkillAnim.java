/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-8 下午3:01:25
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import com.vikings.sanguo.utils.ViewUtil;

public class HigherSkillAnim extends BaseAnim{
	private Drawable d;
	
	public HigherSkillAnim(View view, Animation anim, Drawable d, boolean isGone) {
		super(view, anim, isGone);
		this.d = d;
		setSoundName("battle_skill.ogg");
	}

	@Override
	protected void prepare() {
		view.clearAnimation();
		ViewUtil.setImage(view, d);
	}
}
