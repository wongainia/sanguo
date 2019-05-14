/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-13 下午6:30:08
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.view.View;
import android.view.animation.Animation;

import com.vikings.sanguo.utils.ViewUtil;

public class SetSkillBg extends BaseAnim {
	private String name;
	private View amy;

	private int x;
	private int y; // 中心点的坐标 兵的

	private int childNum;

	public SetSkillBg(View view, Animation anim, int x, int y, int childNum) {
		super(view, anim, false);
		this.amy = view;
		this.x = x;
		this.y = y;
		this.childNum = childNum;
		setSoundName("battle_skill.ogg");
	}

	@Override
	protected void prepare() {
		// // 设置初始化位置
		int width;
		int height;
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		width = view.getMeasuredWidth();
		height = view.getMeasuredHeight();
		int left = 0;
		int top = 0;
		if (childNum == 1) // 1的时候单独处理
		{
			left = x - width / 2;
			top = y - height / 2;			
		} else {
			left = x - width / 2;
			top = y - height * 4 / 5;
		}
		ViewUtil.setMarginLeft(view, left);
		ViewUtil.setMarginTop(view, top);
		view.layout(left, top, left+width, top+height);
	}

	@Override
	protected void animationStart() {
		super.animationStart();
	}

	@Override
	protected void animationEnd() {
		super.animationEnd();
		view.clearAnimation();
	}
}
