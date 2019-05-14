package com.vikings.sanguo.battle.anim;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.vikings.sanguo.battle.anim.BaseDrawableAnim.FrameBeatListener;
import com.vikings.sanguo.model.BattleAnimEffects;

public class DrawableAnimationBasis extends AnimationDrawable {
	public AnimationDrawableFinish mAnimationDrawableFinish = null;
	private FrameBeatListener mBeatListener;
	private long start_time = 0;
	private int frameIndex = 0;
	private boolean isHit = false;
	private BattleAnimEffects battleAnimEffects = null;
		
	public  DrawableAnimationBasis(BattleAnimEffects battleAnimEffects)
	{
		this.battleAnimEffects = battleAnimEffects;
	}
	
	public  DrawableAnimationBasis()
	{
		
	}
	public void setAnimFinishListen(AnimationDrawableFinish drawableFinish) {
		mAnimationDrawableFinish = drawableFinish;
	}

	@Override
	public Drawable getFrame(int index) {
		return super.getFrame(index);
	}

	@Override
	public void draw(Canvas canvas) {	
		if (mBeatListener != null) 
		{
			if (getNumberOfFrames() >= frameIndex) {
				// 提前
				if (isHit == false) 
				{
					if ((frameIndex >= 2 && getCurrent() == getFrame(frameIndex - 2))
					|| (getCurrent() == getFrame(frameIndex - 1) && frameIndex > 1)) 
					{
						isHit = true;
						mBeatListener.beginBeat();
					}
				}
			}
		}
		//super.draw(canvas);
		if (mAnimationDrawableFinish != null) 
		{
			if (getNumberOfFrames() > 1)
			{
				if (getCurrent() == getFrame(getNumberOfFrames() - 1)) 
				{
					if(isHit == false)
					{
						isHit = true;
						mBeatListener.beginBeat();
					}
					mAnimationDrawableFinish.onAnimationFilish();
					mAnimationDrawableFinish = null;
				}
			}
		}
		Drawable currentDrawable = getCurrent();
		if(currentDrawable != null && battleAnimEffects != null)  
		{
			currentDrawable.setFilterBitmap(true);  
			if(currentDrawable instanceof BitmapDrawable)
			{
				Bitmap bitmap = ((BitmapDrawable) currentDrawable).getBitmap();
				try
				{
					drawBitmap(canvas,bitmap,battleAnimEffects);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}				
			}
		}
	}
	
	private void drawBitmap(Canvas canvas,Bitmap bitmap,BattleAnimEffects effectAttackAnim)
	{			
		if(bitmap == null || bitmap.isRecycled() == true)
		{
			return;
		}		
		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int horiMirror = battleAnimEffects.getHoriMirror();
		if (horiMirror == 1)
		{
			matrix.postScale(-1, 1,width/2,height/2);
		}
		int vertMirror = battleAnimEffects.getVerticalMirror();
		if (vertMirror == 1) 
		{
			matrix.postScale(1, -1,width/2,height/2);
		}
		int rotate = battleAnimEffects.getRotateDegress();
		if (rotate > 0) 
		{
			matrix.postRotate(rotate, (float) width/2,height / 2);			
		}
		float xScale = battleAnimEffects.getXScale();
		float yScale = battleAnimEffects.getYScale() ;
		
		if (xScale != 100f && yScale != 100f)
		{
			matrix.postScale(xScale / 100, yScale / 100);
		}				
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, matrix, paint);       		
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public boolean setVisible(boolean visible, boolean restart) 
	{
		return super.setVisible(visible, restart);
	}

	@Override
	public void stop() 
	{
		super.stop();
	}

	public void setFrameListener(FrameBeatListener mBeatListener, int frameIndex) 
	{
		this.mBeatListener = mBeatListener;
		this.frameIndex = frameIndex;
	}

	public interface AnimationDrawableFinish 
	{
		public void onAnimationFilish();
	}	
	
	public void setBattleAnimEffects(BattleAnimEffects battleAnimEffects)
	{
		this.battleAnimEffects = battleAnimEffects;
	}
}
