package com.vikings.sanguo.battle.anim;

import java.util.Random;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.ProgressBar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MissionAnim extends Animation
{
	private int mPivotXType = ABSOLUTE;
	private int mPivotYType = ABSOLUTE;

	private float mPivotXValue = 0.0f;
	private float mPivotYValue = 0.0f;

	private float mFromDegrees;
	private float mToDegrees;
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

	private float xDis; // x方向移动的距离
	private float yDis; // y方向移动的距离
	private boolean isLeft;
	private int startOffset;
	private int during;

	private int threshold;
	private float throisidDat;
	float a, b, c;
	private long start_time = 0;

	private final int BEAT_TIME = 100;
	private int beatDis = 30;

	private ViewGroup window;
	private boolean isShowHp = false;
	private int currentHp;
	private int totalHp;

	public MissionAnim(float fromDegrees, float toDegrees, int pivotXType,
			float pivotXValue, int pivotYType, float pivotYValue,
			int mFromXDelta, float mToXDelta, int mFromYDelta, float mToYDelta)
	{
		this.mFromDegrees = fromDegrees;
		this.mToDegrees = toDegrees;
		this.mPivotXType = pivotXType;
		this.mPivotXValue = pivotXValue;
		this.mPivotYType = pivotYType;
		this.mPivotYValue = pivotYValue;
		this.mFromXDelta = mFromXDelta;
		this.mToXDelta = mToXDelta;
		this.mFromYDelta = mFromYDelta;
		this.mToYDelta = mToYDelta;
	}

	// 0-0.1 秒被击 后退s

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{

		float dx = mFromXDelta;
		float dy = mFromYDelta;
		if (mFromXDelta != mToXDelta)
		{
			dx = mFromXDelta + ((mToXDelta - mFromXDelta) * interpolatedTime);
		}
		if (mFromYDelta != mToYDelta)
		{
			dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
		}
		Log.e("SoliderFlyAnim", "time:"
				+ (System.currentTimeMillis() - start_time));
	

		float degrees = mFromDegrees
				+ ((mToDegrees - mFromDegrees) * (interpolatedTime));

	
		Log.e("MissionAnim", "dx:" + dx + " dy:" + dy + " degrees:"
				+ degrees + " interpolatedTime:" + interpolatedTime );
		if (mPivotX == 0.0f && mPivotY == 0.0f)
		{
			t.getMatrix().setRotate(degrees);
			t.getMatrix().postTranslate(dx, dy);
		} 
		else
		{
			t.getMatrix().setRotate(degrees, mPivotX, mPivotY);
				// t.getMatrix().postTranslate(dx, dy);
			t.getMatrix().postTranslate(dx, dy);
		}
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
		mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
		

	}
}
