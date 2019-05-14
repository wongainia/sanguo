package com.vikings.sanguo.battle.anim;

import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ArcherLongAtkAnim extends BaseDrawableAnim
{	
	//盾兵放大到140   下载的特效   放大、旋转角度、偏移  放到配置文件中
	private int xScale = 150;   		//x
	private int yScale = 150;  	 	//y 方向放大比例   
	private int rotate = 180;       	//旋转的角度 
	
	private int lXOffset = 0;  			//与左边矩阵的x偏移	
	private int lYOffset = 313;		//与左边矩阵的y偏移
	
	private int rXOffset = -61;  	//与右边矩阵的x偏移	
	private int rYOffset = 250;	//与右边矩阵的y偏移
	
	private int armX = 0;     	 	//当前矩阵的左上的x
	private int armY = 0;       		//当前矩阵的左上角y
	private boolean isLeft;
	
	private BattleAnimEffects battleAnimEffects;
	
		
	public ArcherLongAtkAnim(View view, DrawableAnimationBasis anim,boolean isLeft, String soundName)
	{
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);	
		Point armLR;
		if(isLeft)
		{
			armLR = BattleCoordUtil.downMatrixLB;			
		}
		else
		{
			armLR = BattleCoordUtil.upMatrixLB;		
		}		
		this.armX = armLR.x;
		this.armY = armLR.y;		
	}
	
	
	
	public ArcherLongAtkAnim(View view, DrawableAnimationBasis anim,boolean isLeft, String soundName,BattleAnimEffects battleAnimEffects)
	{
		super(view, anim, true);
		this.isLeft = isLeft;
		setSoundName(soundName);	
		Point armLR;
		if(isLeft)
		{
			armLR = BattleCoordUtil.downMatrixLB;			
		}
		else
		{
			armLR = BattleCoordUtil.upMatrixLB;		
		}		
		this.armX = armLR.x;
		this.armY = armLR.y;		
		this.battleAnimEffects = battleAnimEffects;
	}
	
//	@Override
//	protected void prepare()
//	{
//		//view.bringToFront();		
//        view.clearAnimation();		
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
//		
//		Drawable d = view.getBackground();
//		DrawableAnimationBasis beatAnimation = null;
//		if (d != null && d instanceof DrawableAnimationBasis)
//		{
//			beatAnimation = (DrawableAnimationBasis) d;
//		} 		
//		if (isLeft)
//		{				
//			beatAnimation =  ImageUtil.createAnimationDrawable("btl_arrow_frame", 6, 80,0, rotate,xScale,yScale);						
//		} 
//		else
//		{
//			beatAnimation = ImageUtil.createAnimationDrawable("btl_arrow_frame", 6, 80,0, 0,xScale,yScale);												
//		}		
//		view.setBackgroundDrawable(beatAnimation);
//		if(beatAnimation != null)
//		{
//			setAnim(beatAnimation);
//		}
//		
//		ViewUtil.setMarginLeft(view, left);
//		ViewUtil.setMarginTop(view, top);	
//		Log.d("battleDriver", "弓兵特效    isLeft---" + isLeft + "left---" + left + "---top--" + top);		
//	}

	@Override
	protected void prepare()
	{
		//view.bringToFront();		
        view.clearAnimation();		
       
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
		
		Drawable d = view.getBackground();
		DrawableAnimationBasis beatAnimation = null;
		if (d != null && d instanceof DrawableAnimationBasis)
		{
			beatAnimation = (DrawableAnimationBasis) d;
		} 				
		beatAnimation =  ImageUtil.createAnimationDrawable(battleAnimEffects);
		//beatAnimation =  ImageUtil.createAnimationDrawable("btl_arrow_frame", 6, 80,0, rotate,xScale,yScale);
		view.setBackgroundDrawable(beatAnimation);
		if(beatAnimation != null)
		{
			setAnim(beatAnimation);
		}
		int left = armX + battleAnimEffects.getXOffset();
		int top = armY - battleAnimEffects.getYOffset();
		ViewUtil.setMarginLeft(view, left);
		ViewUtil.setMarginTop(view, top);	
		Log.d("battleDriver", "弓兵特效    isLeft---" + isLeft + "left---" + left + "---top--" + top);		
	}

	@Override
	public void animationEnd()
	{
	}
	
}
