/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-15 下午2:26:14
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-20 下午8:58:33
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SetPermenantSkill extends BaseAnim{
	private Drawable d;
	
	public SetPermenantSkill(ImageView view, Animation anim, Drawable d) {
		super(view, anim, false);
		this.d = d;
	}
	
	@Override
	protected void prepare() {
		((ImageView)view).setImageDrawable(d);
	}
}

