/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-16 下午6:39:44
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.utils.ViewUtil;

public class SetSkillAnim extends BaseAnim {
	private Buff buff;
	
	public SetSkillAnim(View v, Animation anim, Buff buff) {
		super(v, anim, false);
		this.buff = buff;
	}

	@Override
	protected void prepare() {
		//ViewUtil.setText(view, buff.amount);
		view.setTag(buff.buffId);
	}
}
