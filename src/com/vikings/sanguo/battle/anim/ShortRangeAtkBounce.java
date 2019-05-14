/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-7-25 下午6:07:55
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ShortRangeAtkBounce extends BaseAnim
{
	private int dis;
	private boolean isLeft;

	public ShortRangeAtkBounce(boolean isLeft,View view, Animation anim, boolean needGone, int dis)
	{
		super(view, anim, needGone);
		setSoundName("knight.ogg");
		this.dis = dis;
		this.isLeft = isLeft;
	}

	@Override
	protected void prepare()
	{
		//view.bringToFront();
	}

	@Override
	protected void start()
	{
		super.start();
	}

	@Override
	protected void stop()
	{
		super.stop();
	}
	
	@Override
	protected void animationEnd()
	{
		super.animationEnd();
		view.clearAnimation();
		int w = view.getWidth();
		int h = view.getHeight();
		int top = view.getTop();

		if (dis > 0)
		{
			int l = view.getLeft() + dis;
			ViewUtil.setMarginLeft(view, l);
			ViewUtil.setMarginTop(view, top - dis);
			view.layout(l, top - dis, l + w, top + h - dis);
		} 
		else
		{
			int r = view.getRight() + dis;
			ViewUtil.setMarginLeft(view, r - w);
			ViewUtil.setMarginTop(view, top - dis);
			view.layout(r - w, top - dis, r, top + h - dis);
		}
		// 更新矩阵的中心点
		if(isLeft)
		{
			BattleCoordUtil.downMatrixLB.x += dis;
			BattleCoordUtil.downMatrixLB.y -= dis;
			//Log.d("battleDriver", "ShortRangeAtkBounce---" + "atkackLB  dis:" + dis);
			//Log.d("battleDriver", "ShortRangeAtkBounce---" + "atkackLB  x:" + BattleCoordUtil.atkackLB.x + "y---"+BattleCoordUtil.atkackLB.y );
		}
		else
		{
			BattleCoordUtil.upMatrixLB.x += dis;
			BattleCoordUtil.upMatrixLB.y -= dis;
			//Log.d("battleDriver", "ShortRangeAtkBounce---" + "defendLB  dis:" + dis);
			//Log.d("battleDriver", "ShortRangeAtkBounce---" + "defendLB  x:" + BattleCoordUtil.defendLB.x + "y---"+BattleCoordUtil.defendLB.y );
		}
	}
}
