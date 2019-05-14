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
package com.vikings.sanguo.battle.anim;

import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class SetAmyIcon extends BaseAnim {
	private Drawable d;
	private boolean isDown;
	private int amrCount;
	private byte type;
	private boolean isBoss;
	private ViewGroup vg;
	private List<ImageView> amyPools;

	public SetAmyIcon(ViewGroup vg, boolean isDown, Animation anim, Drawable d,
			int amrCount, byte type, boolean isBoss, List<ImageView> amyPools) {
		super(vg, anim, false);
		this.d = d;
		this.isDown = isDown;
		this.vg = vg;
		this.amrCount = amrCount;
		this.type = type;
		this.isBoss = isBoss;
		this.amyPools = amyPools;
	}

	@Override
	protected void prepare() {
		if(vg == null )
		{
			return;
		}
		int w = view.getRight() - view.getLeft();
		int h = view.getBottom() - view.getTop();

		vg.clearAnimation();
		ViewUtil.setMarginLeft(vg, 0); // Config.scaleRate
		ViewUtil.setMarginTop(vg, 0); // Config.scaleRate
		view.layout(0, 0, w, h);
		
		BattleCoordUtil.initArmLBCoord(isDown);
		for (int i = 0; i < vg.getChildCount(); i++) {
			vg.getChildAt(i).clearAnimation();
		}
		for (int index = 0; index < amyPools.size(); index++) {
			amyPools.get(index).clearAnimation();
		}
		// vg.clearAnimation();
		vg.removeAllViews();
		if (isDown) {
			for (int i = 0; i < amrCount; i++) {
				Point point = null;
				if (type == 2) {
					point = BattleCoordUtil.downAvalvyPosition[i];
				} else if (type == 3) {
					point = BattleCoordUtil.downElephantsPosition[i];
				} else if (type == 4) {
					point = BattleCoordUtil.downBossPosition;
				} else {
					point = BattleCoordUtil.downArmPosition[i];
				}

				if (point.x == 0 && point.y == 0) {
					continue;
				} else {
					ImageView arm = amyPools.get(i);
					arm.setVisibility(View.VISIBLE);
					if (d != null && arm != null) {
						arm.setImageDrawable(d);
					}
					if(d != null)
					{
					int width = d.getIntrinsicWidth();
					int height = d.getIntrinsicHeight();

					android.widget.FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp.leftMargin = point.x - width / 2;
					lp.topMargin = point.y - height / 2;
					lp.gravity = Gravity.LEFT | Gravity.TOP;
					vg.addView(arm, lp);

					}
				}
			}
		} else {
			for (int i = 0; i < amrCount; i++) {
				Point point = null;
				if (type == 2) {
					point = BattleCoordUtil.upAvalvyPosition[i];
				} else if (type == 3) {
					point = BattleCoordUtil.upElephantsPosition[i];
				} else if (type == 4) {
					point = BattleCoordUtil.upBossPosition;
				} else {
					point = BattleCoordUtil.upArmPosition[i];
				}
				if (point.x == 0 && point.y == 0) {
					continue;
				} else {
					ImageView arm = amyPools.get(i);
					arm.setVisibility(View.VISIBLE);
					if (d != null && arm != null) {
						arm.setBackgroundDrawable(d);
					}
					if(d != null)
					{
					int width = d.getIntrinsicWidth();
					int height = d.getIntrinsicHeight();

					android.widget.FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp.leftMargin = point.x - width / 2;
					lp.topMargin = point.y - height / 2;
					lp.gravity = Gravity.LEFT | Gravity.TOP;
					vg.addView(arm, lp);					
					}
				
				}
			}
		}
	}

	@Override
	protected void animationEnd() {
		int w = view.getWidth();
		int h = view.getHeight();
		super.animationEnd();

		vg.clearAnimation();
		ViewUtil.setMarginLeft(vg, 0); // Config.scaleRate
		ViewUtil.setMarginTop(vg, 0); // Config.scaleRate
		view.layout(0, 0, w, h);
	}
}
