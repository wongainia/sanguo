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
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ClearArmSkill extends BaseAnim
{
	private View window;

	public ClearArmSkill(View view, View window, Animation anim)
	{
		super(view, anim, false);
		this.window = window;
	}

	@Override
	protected void animationEnd()
	{		
		window.findViewById(R.id.skill_small).clearAnimation();
		window.findViewById(R.id.arm_skill).clearAnimation();
		window.findViewById(R.id.arm_skill_bottom).clearAnimation();
		window.findViewById(R.id.armSkillLayout).clearAnimation();
		
		TextView name = (TextView)window.findViewById(R.id.arm_skill_bottom);
		name.setText("");

		ViewUtil.setGone(window.findViewById(R.id.skill_small));
		ViewUtil.setGone(window.findViewById(R.id.arm_skill));
		ViewUtil.setGone(window.findViewById(R.id.arm_skill_bottom));
		ViewUtil.setGone(window.findViewById(R.id.armSkillLayout));

		super.animationEnd();
	}
}
