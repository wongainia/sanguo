/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-12-5 下午3:25:32
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ViewUtil;

import android.R.color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.FrameLayout.LayoutParams;

public class ReachAtkPos extends BaseAnim
{
	private int x;
	//private ViewGroup vg;
	
	private View v;

	public ReachAtkPos(View view, Animation anim, Drawable d, int x)
	{
		super(view, anim, false);
		this.x = x;
		this.v = view;
	}
	
	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected void animationEnd()
	{
		// 动画结束改变了view的位置，必须clearAnimation
//		view.clearAnimation();		
//		ViewUtil.setMarginLeft(view, 0);
//		ViewUtil.setMarginTop(view, 0);
//	    view.layout(0, 0, Config.screenWidth, Config.screenHeight);
////		int w = view.getWidth();
////		int h = view.getHeight();
////		int top = view.getTop();
//		if(v instanceof ViewGroup)		
//		{
//		ViewGroup vg = (ViewGroup)v;
//		if (x > 0)
//		{
//			if(vg != null && vg.getChildCount() > 0)
//			{
//				int childSum = vg.getChildCount();
//				for(int i = 0; i < childSum; i ++)
//				{
//					View v = vg.getChildAt(i);
//					v.clearAnimation();
//					int width = v.getWidth();
//					int height = v.getHeight();
//					int top = v.getTop();
//					int left = v.getLeft() + x;
////					ViewUtil.setMargin(v, left, top - x, 0, 0, Gravity.TOP);
//					//ViewUtil.setMarginLeft(v, left);
//					//ViewUtil.setMarginTop(v, top - x);
//					v.layout(left, top - x, left + width, top + height - x);						
//				}				
//			}					
//			//更新矩阵的左上角的坐标
//			BattleCoordUtil.downMatrixLB.x +=x;
//			BattleCoordUtil.downMatrixLB.y -=x;					
//		} else
//		{
//			if(vg != null && vg.getChildCount() > 0)
//			{
//				int childSum = vg.getChildCount();
//				for(int i = 0; i < childSum; i ++)
//				{
//					View v = vg.getChildAt(i);
//					v.clearAnimation();
//					int width = v.getWidth();
//					int height = v.getHeight();
//					int top1 = v.getTop();
//					
//					int r = v.getRight() + x;
////					ViewUtil.setMargin(v, r - width, top1 - x, 0, 0, Gravity.TOP);
//					//ViewUtil.setMarginLeft(v, r - width);
//					//ViewUtil.setMarginTop(v, top1 - x);
//					v.layout(r - width, top1 - x, r, top1 + height - x);
//				}				
//			}						
//			//更新矩阵的右上角的坐标
//			BattleCoordUtil.upMatrixLB.x +=x;
//			BattleCoordUtil.upMatrixLB.y -=x;			
//		}	
//		}		
//		else
		{
			if (x > 0)
			{				
				v.clearAnimation();
				int width = v.getWidth();
				int height = v.getHeight();
				int top = v.getTop() - x;
				int r = v.getRight() + x;				
				ViewUtil.setMarginLeft(v, r - width);
				ViewUtil.setMarginTop(v, top);
				//v.layout(left-x, top - x, left + width, top + height - x);		
				
				//v.layout(left, top, left + width, top + height);	
				v.layout(r - width, top, r, top + height);	
				//更新矩阵的左上角的坐标
				BattleCoordUtil.downMatrixLB.x +=x; 
				BattleCoordUtil.downMatrixLB.y -=x;					
			}
			else
			{
					v.clearAnimation();
					int width = v.getWidth();
					int height = v.getHeight();
					int top = v.getTop() - x;
						
					int r = v.getRight() + x;
					//ViewUtil.setMargin(v, r - width, top - x, 0, 0, Gravity.TOP);
					ViewUtil.setMarginLeft(v, r - width);
					ViewUtil.setMarginTop(v, top);
					//v.layout(r - width, top - x, r-x, top + height - x);
					v.layout(r - width, top, r, top + height); 
	
					BattleCoordUtil.upMatrixLB.x +=x;
					BattleCoordUtil.upMatrixLB.y -=x;			
				}						
				//更新矩阵的右上角的坐标				
		}
	}

}
