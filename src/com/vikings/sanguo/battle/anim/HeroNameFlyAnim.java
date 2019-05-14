package com.vikings.sanguo.battle.anim;

import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class HeroNameFlyAnim extends Animation {
	private int mPivotXType = ABSOLUTE;
	private int mPivotYType = ABSOLUTE;

	private float mPivotXValue = 0.0f;
	private float mPivotYValue = 0.0f;

	 private float mFromX;
	    private float mToX;
	    private float mFromY;
	    private float mToY;
	private float mPivotX;
	private float mPivotY;

	private int mFromXType = ABSOLUTE;
	private int mToXType = ABSOLUTE;
	private int mFromYType = ABSOLUTE;
	private int mToYType = ABSOLUTE;
	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;

	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;

	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;  
	
	private float xDis;  //x方向移动的距离
	private float yDis; // y方向移动的距离
	private boolean isLeft;
	private int startOffset;
	private int during;
	
	private long start_time = 0;
	
	private float  mFromAlpha;
	private float mToAlpha;
		
	 public HeroNameFlyAnim(float fromX, float toX, float fromY, float toY,
	            int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
	        mFromX = fromX;
	        mToX = toX;
	        mFromY = fromY;
	        mToY = toY;

	        mPivotXValue = pivotXValue;
	        mPivotXType = pivotXType;
	        mPivotYValue = pivotYValue;
	        mPivotYType = pivotYType;
	    }
	 
	  public void setParam(int fromXType, float fromXValue, int toXType, float toXValue,
              int fromYType, float fromYValue, int toYType, float toYValue,int during)
	  {
		   
			this.mFromXType = fromXType;
			this.mFromXValue = fromXValue;
			this.mToXType = toXType;
			this.mToXValue = toXValue;
			this.mFromYType = fromYType;
			this.mFromYValue = fromYValue;
			this.mToYType = toYType;
			this.mToYValue = toYValue;
		   
		   this.during = during;
	   }
	  	 	 	  
	   public void setParam(float mFromXDelta,float mToXDelta,float mFromYDelta,float mToYDelta,float mFromAlpha,float mToAlpha,int during)
	   {
		   this.mFromXDelta = mFromXDelta;
		   this.mToXDelta = mToXDelta;
		   this.mFromYDelta = mFromYDelta;
		   this.mToYDelta = mToYDelta;
		   this.mFromAlpha = mFromAlpha;
		   this.mToAlpha = mToAlpha; 
		   this.during = during;
	   }

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        float sx = 1.0f;
	        float sy = 1.0f;

	        if(start_time == 0)
	        {
	        	start_time = System.currentTimeMillis();
	        }
	        
	        float alpha = mFromAlpha;
	        
	        {
	        	if (mFromX != 1.0f || mToX != 1.0f) 
	 	        {
	        		if(System.currentTimeMillis() - start_time > 500)
	        		{
	        			sx = 0.5f + ((1 - 0.5f) * interpolatedTime);
	        		}
	        		else
	        		{
	        			sx = mFromX + ((mToX - mFromX) * interpolatedTime);
	        		}
	 	        }
	 	        if (mFromY != 1.0f || mToY != 1.0f) 
	 	        {
	 	        	if(System.currentTimeMillis() - start_time > 500)
	        		{
	 	        		sy = 0.5f + ((1 - 0.5f) * interpolatedTime);
	        		}
	        		else
	        		{
	        			sy = mFromY + ((mToY - mFromY) * interpolatedTime);
	        		}
	 	        }
	 	        
	 	        float dx = mFromXDelta;
	 			float dy = mFromYDelta;
	 			if (mFromXDelta != mToXDelta) {
	 				dx = mFromXDelta + ((mToXDelta - mFromXDelta) * interpolatedTime);
	 			}
	 			if (mFromYDelta != mToYDelta) {
	 				dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
	 			}
	 			t.setAlpha(alpha + ((mToAlpha - alpha) * interpolatedTime));
	 			
	 			
	 	        if (mPivotX == 0 && mPivotY == 0) {
	 	            t.getMatrix().setScale(sx, sy);
	 	        	t.getMatrix().postTranslate(dx, dy);
	 	        } else {
	 	        	
	 	            t.getMatrix().setScale(sx, sy, mPivotX, mPivotY);
	 	            
	 	            t.getMatrix().postTranslate(dx, dy);
	 	        }
	        }	       
	    }

	    @Override
	    public void initialize(int width, int height, int parentWidth, int parentHeight) {
	        super.initialize(width, height, parentWidth, parentHeight);

	        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
	        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
	        
			//mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
			//mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
			//mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
			//mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
			setDuration(during);
	    }
}
