/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-16 下午6:44:34
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;
import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ClearSkill extends BaseAnim
{
	private View window;

	public ClearSkill(View view, View window, Animation anim)
	{
		super(view, anim, false);
		this.window = window;
	}

	@Override
	protected void animationEnd()
	{
		window.findViewById(R.id.screenSkillFrame).clearAnimation();
		window.findViewById(R.id.screenSkillImg).clearAnimation();
		window.findViewById(R.id.screenSkillName).clearAnimation();		
		window.findViewById(R.id.screenHeroIcon).clearAnimation();
		window.findViewById(R.id.screenHeroIcon1).clearAnimation();
		window.findViewById(R.id.screenHeroIcon2).clearAnimation();
		window.findViewById(R.id.screenSkillBg).clearAnimation();

		window.findViewById(R.id.short_light0).clearAnimation();
		window.findViewById(R.id.short_light1).clearAnimation();
		window.findViewById(R.id.long_light0).clearAnimation();
		window.findViewById(R.id.long_light1).clearAnimation();
		window.findViewById(R.id.short_light0_below).clearAnimation();
		window.findViewById(R.id.short_light1_below).clearAnimation();

		ViewUtil.setGone(window.findViewById(R.id.screenSkillFrame));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillImg));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillName));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon1));
		ViewUtil.setGone(window.findViewById(R.id.screenHeroIcon2));
		ViewUtil.setGone(window.findViewById(R.id.screenSkillBg));

		ViewUtil.setGone(window.findViewById(R.id.short_light0));
		ViewUtil.setGone(window.findViewById(R.id.short_light1));
		ViewUtil.setGone(window.findViewById(R.id.long_light0));
		ViewUtil.setGone(window.findViewById(R.id.long_light1));
		
		ViewUtil.setGone(window.findViewById(R.id.short_light1_below));
		ViewUtil.setGone(window.findViewById(R.id.short_light0_below));
				
		super.animationEnd();
	}
}
