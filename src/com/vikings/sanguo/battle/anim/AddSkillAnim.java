/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
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

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.utils.ViewUtil;

public class AddSkillAnim extends BaseAnim {
	private Drawable d;
	private View v = null;
	private Buff buff;
	
	public AddSkillAnim(View v, Animation anim,  Drawable d,  
			Buff buff) {  //, boolean isInit
		super(null, anim, false);
		this.v = v;
		this.d = d;
		this.buff = buff;
	}

	@Override
	protected void prepare() {
//		TextView skill = (TextView) v.findViewById(R.id.skill);
//		skill.setBackgroundDrawable(d);
//		if (buff.amount > 1) {
//			ViewUtil.setBold(skill);
//			ViewUtil.setText(skill, buff.amount);
//		}
		v.setTag(buff.buffId);
		setView(v);
	}
}
