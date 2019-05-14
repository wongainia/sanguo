/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-19 下午4:10:43
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

public class SkillBg extends BaseAnim{
	//private int resId;
	private boolean isDown;
	private Drawable d;
	
	public SkillBg(boolean isDown,View view, Animation anim, boolean needGone, Drawable d) {
		super(view, anim, needGone);
		this.isDown = isDown;
		//this.resId = resId;
		this.d = d;
	}

	@Override
	protected void prepare()
	{
		super.prepare();
		//if (resId > 0)
		{
			//Drawable d = Config.getController().getDrawable(resId);
			int width = 0;
			int height = 0;
			if(d!= null)
			{
				view.setBackgroundDrawable(d);			
				width = d.getIntrinsicWidth();
				height = d.getIntrinsicHeight();
			}
						
			if(isDown)
			{
				ViewUtil.setMargin(view,0,0,0, (int)(172*Config.SCALE_FROM_HIGH),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
				int l = (Config.screenWidth - width)/2;
				int b = Config.screenHeight - (int)(172*Config.SCALE_FROM_HIGH);
				int t = b - height;
				int r = l + width;				
				view.layout(l, t, r, b);				
			}
			else
			{
				ViewUtil.setMargin(view, 0,(int)(172*Config.SCALE_FROM_HIGH),0,0,Gravity.TOP|Gravity.CENTER_HORIZONTAL);
				int l = (Config.screenWidth - width)/2;
				int t = (int)(172*Config.SCALE_FROM_HIGH);
				int r = l + width;
				int b = t + height;
				view.layout(l, t, r, b);
			}			
		}
	}
	
	@Override
	protected void animationStart() {
		super.animationStart();
		
	}
}
