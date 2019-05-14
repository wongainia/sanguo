package com.vikings.sanguo.battle.anim;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EffectAttackAnim extends BaseDrawableAnim
{
	private boolean isLeft;
	// 盾兵放大到140
	
	private int armX = 0; // 当前矩阵的左上的x
	private int armY = 0; // 当前矩阵的左上角y

	private BattleAnimEffects battleAnimEffects;
	DrawableAnimationBasis beatAnimation = null;
	
	float rate = 1.0f;

	public EffectAttackAnim(View view, DrawableAnimationBasis anim, int x,
			boolean isLeft, String soundName, Point armCenter)
	{
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		this.armX = armCenter.x;
		this.armY = armCenter.y;

		rate = Config.screenWidth/480f;
		//Log.d(TAG, "GetBeatenDrawableAnimation");
	}

	public EffectAttackAnim(View view,boolean isLeft, String soundName, Point armCenter,
			BattleAnimEffects battleAnimEffects)
	{
		super(view, null, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		this.armX = armCenter.x;
		this.armY = armCenter.y;
		this.battleAnimEffects = battleAnimEffects;
		
		if(this.battleAnimEffects.getId()/10 == 1)
		{
			setDelay(80);
		}		
		setHitFrame(battleAnimEffects.getHitFrame());
		
		Drawable d = view.getBackground();
		if (d != null && d instanceof AnimationDrawable)
		{
			beatAnimation = (DrawableAnimationBasis) d;
		}
		//Log.d(TAG, "特效ID--" +battleAnimEffects.getId());
		beatAnimation = ImageUtil.createAnimationDrawable(battleAnimEffects);				
	}
	
	public DrawableAnimationBasis  getAnimationDrawable()
	{
		return beatAnimation;
	}

	@Override
	protected void prepare()
	{
		int left = (int) (armX + battleAnimEffects.getXOffset()*Config.SCALE_FROM_HIGH - 29*Config.SCALE_FROM_HIGH);
		int top = (int) (armY + battleAnimEffects.getYOffset()*Config.SCALE_FROM_HIGH + 23*Config.SCALE_FROM_HIGH);
	   
		if (beatAnimation != null)
		{
			setAnim(beatAnimation);
		}
		view.setBackgroundDrawable(beatAnimation);
		
//		FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) view.getLayoutParams();
//		lp.width = beatAnimation.getFrame(0).getMinimumWidth();
//		lp.height = beatAnimation.getFrame(0).getIntrinsicHeight() ;
//		view.setLayoutParams(lp);
		
	
		ViewUtil.setMarginLeft(view, left);
		ViewUtil.setMarginTop(view, top);
		
//		ViewUtil.setMargin(view, left, top, 0,0, Gravity.LEFT|Gravity.TOP);
		int l = left;
		int t = top;
		int b = t + Config.screenHeight;
		int r = l + Config.screenWidth;
		view.layout(l, t, r, b);

	}

	@Override
	public void animationEnd()
	{

	}
}