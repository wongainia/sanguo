/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-11-8 下午2:51:36
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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.ScaleImageView;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroIconAnim extends BaseAnim {
	private ImageView v;
	private boolean isDown;
	private int index;
	private String heroHdImg;
	private final int HERO_ICON_MARGIN = (int) (135 * Config.SCALE_FROM_HIGH);  //英雄距离屏幕最上 最下的偏移 
	private final float HERO_ICON_HEIGHT = 286 * Config.SCALE_FROM_HIGH;
	private final String PREFIX = "battle";
	
	public HeroIconAnim(boolean isDown, ImageView view, View v, Animation anim,
			boolean isGone, Drawable d, int index, String heroHdImg) {
		super(view, anim, isGone);
		this.v = view;
		this.isDown = isDown;
		this.index = index;
		this.heroHdImg = heroHdImg;
	}

	@Override
	protected void prepare() {		
		if(v instanceof ScaleImageView)
		{
			this.v.setBackgroundDrawable(null);
			this.v.clearAnimation();
			this.v.setVisibility(View.GONE);
			((ScaleImageView) this.v).setMirror(false);
		}
		int width = 0;
		int height = 0;
		if (StringUtil.isNull(heroHdImg) == false) {
			//Drawable d = Config.getController().getDrawable(heroHdImg);
			Drawable d = Config.getController().getDrawableHdInBattle(heroHdImg,ImageUtil.ARGB4444);
			if (d != null) 
			{
				float scale = HERO_ICON_HEIGHT/ d.getIntrinsicHeight();
				height = (int) (HERO_ICON_HEIGHT);
				width = (int) (d.getIntrinsicWidth() * scale);
				if ((index == 1 && isDown == false)|| (index == 0 && isDown == true)) 
				{
					if(v instanceof ScaleImageView)
					{
						ScaleImageView scaleView = (ScaleImageView)v;
						scaleView.setMirror(true);
						scaleView.setScale(scale);
						scaleView.setBackgroundDrawable(d);
					
						//height = (int) (HERO_ICON_HEIGHT);
						//width = (int) (d.getIntrinsicWidth() * scale);
						FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)scaleView.getLayoutParams();
						lp.width = width;
						lp.height = height;
						scaleView.setLayoutParams(lp);
					}
				} 
				else 
				{					
					new ViewImgScaleCallBack(heroHdImg, v, scale);
				}				
			}
			else
			{
				System.gc();
			}
		}		
		if (isDown) {
			if (index == 0) 
			{
				ViewUtil.setMargin(v, 0, 0, 0,HERO_ICON_MARGIN, Gravity.RIGHT| Gravity.BOTTOM);
				int l = Config.screenWidth - width;
				int b = Config.screenHeight- HERO_ICON_MARGIN;
				int t = b - height;
				int r = Config.screenWidth;
				v.layout(l, t, r, b);
			} 
			else if (index == 1) 
			{
				ViewUtil.setMargin(v, 0, 0, 0,
						HERO_ICON_MARGIN, Gravity.LEFT
								| Gravity.BOTTOM);
				int l = 0;
				int b = Config.screenHeight
						- HERO_ICON_MARGIN;
				int t = b - height;
				int r = l + width;
				v.layout(l, t, r, b);
			} 
			else if (index == 2) 
			{
				ViewUtil.setMargin(v, 0, 0, 0,
						HERO_ICON_MARGIN,
						Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
				int l = (Config.screenWidth - width) / 2;
				int b = Config.screenHeight
						- HERO_ICON_MARGIN;
				int r = l + width;
				int t = b - height;
				v.layout(l, t, r, b);
			}											
		} 
		else 
		{			
			if (index == 0) 
			{
				ViewUtil.setMargin(v, 0, (int) (135 * Config.SCALE_FROM_HIGH),
						0, 0, Gravity.LEFT | Gravity.TOP);
				int l = 0;
				int t = HERO_ICON_MARGIN;
				int r = l + width;
				int b = t + height;
				v.layout(l, t, r, b);
			} 
			else if (index == 1) 
			{
				ViewUtil.setMargin(v, 0, HERO_ICON_MARGIN,
						0, 0, Gravity.RIGHT | Gravity.TOP);
				int l = Config.screenWidth - width;
				int t = HERO_ICON_MARGIN;
				int r = Config.screenWidth;
				int b = t + height;
				v.layout(l, t, r, b);
			} else if (index == 2) 
			{
				ViewUtil.setMargin(v, 0, HERO_ICON_MARGIN,
						0, 0, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
				int l = (Config.screenWidth - width) / 2;
				int t = HERO_ICON_MARGIN;
				int r = l + width;
				int b = t + height;
				v.layout(l, t, r, b);
			}			
		}
	}

	protected void animationStart() {
		super.animationStart();
	}

	@Override
	protected void animationEnd() {
		
	}
}
