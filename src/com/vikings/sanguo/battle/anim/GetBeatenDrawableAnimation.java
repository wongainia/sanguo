package com.vikings.sanguo.battle.anim;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class GetBeatenDrawableAnimation extends BaseDrawableAnim
{
	private final String TAG = "battleDriver";
	private boolean isLeft;
	//盾兵放大到140
	private int xScale = 140;   //x
	private int yScale = 140;   //y 方向放大比例   
	private int rotate = 90;       //旋转的角度 
	
	private int lXOffset = 48;  //与左边矩阵的x偏移	
	private int lYOffset = 291;//与左边矩阵的y偏移
	
	private int rXOffset = 4;  //与右边矩阵的x偏移	
	private int rYOffset = 210;//与右边矩阵的y偏移
	
	private int armX = 0;      //当前矩阵的左上的x
	private int armY = 0;       //当前矩阵的左上角y
	
	private BattleAnimEffects battleAnimEffects;
			
	public GetBeatenDrawableAnimation(View view, DrawableAnimationBasis anim, int x,
			boolean isLeft, String soundName,Point armCenter)
	{
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		//setDelay(100);
		this.armX = armCenter.x;
		this.armY = armCenter.y;
				
		Log.d(TAG, "GetBeatenDrawableAnimation");
	}
	
	public GetBeatenDrawableAnimation(View view, DrawableAnimationBasis anim,boolean isLeft, String soundName,Point armCenter,BattleAnimEffects battleAnimEffects)
	{
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);
		//setDelay(100);
		this.armX = armCenter.x;
		this.armY = armCenter.y;
		this.battleAnimEffects = battleAnimEffects;		
		Log.d(TAG, "GetBeatenDrawableAnimation");
	}

	@Override
	protected void prepare()
	{
		//view.bringToFront();		
		//view.clearAnimation();
//		int left;
//		int top;
//		if (isLeft)
//		{			
//			left = (int) (armX + lXOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
//			top = (int) (armY -lYOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);	
//		} 
//		else
//		{					
//			left = (int) (armX + rXOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
//			top = (int) (armY -rYOffset*Config.SCALE_FROM_HIGH/**Config.screenWidth/BattleCoordUtil.OFFSET_OPPOSITE_WIDTH*/);
//		}	
		
		int left = armX + battleAnimEffects.getXOffset();
		int top = armY - battleAnimEffects.getYOffset();
		
		Drawable d = view.getBackground();
		DrawableAnimationBasis beatAnimation = null;
		if (d != null && d instanceof AnimationDrawable)
		{
			beatAnimation = (DrawableAnimationBasis) d;
		} 	
//		if (isLeft)
//		{	
//			beatAnimation = ImageUtil.createAnimationDrawable("holy_frame", 3, 40,0, 0,xScale,yScale);			
//		} 
//		else
//		{
//			beatAnimation =  ImageUtil.createAnimationDrawable("holy_frame", 3, 40,2, rotate,xScale,yScale);					
//		}	
//		beatAnimation = ImageUtil.createAnimationDrawable("holy_frame", 3, 40,2, rotate,xScale,yScale);	
		beatAnimation =  ImageUtil.createAnimationDrawable(battleAnimEffects);
		if(beatAnimation != null)
		{
			setAnim(beatAnimation);
		}		
		view.setBackgroundDrawable(beatAnimation);
		ViewUtil.setMarginLeft(view, left );
		ViewUtil.setMarginTop(view, top);

		Log.d("battleDriver", "盾兵特效    isLeft---" + isLeft + "left---" + left + "---top--" + top);
//		int width = beatAnimation.getFrame(0).getIntrinsicWidth();
//		int height = beatAnimation.getFrame(0).getIntrinsicHeight();
//		Log.d("battleDriver",  "图片宽度---" + width + "图片高度---" + height + "矩阵 left---" + armX + "--top--" + armY);				
	}

	@Override
	public void animationEnd()
	{
		
	}
}
