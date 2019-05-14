package com.vikings.sanguo.battle.anim;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class RouletteRotateAnimation extends Animation {

	private float mFromDegrees;
	private float mToDegrees;

	private int mPivotXType = ABSOLUTE;
	private int mPivotYType = ABSOLUTE;
	private float mPivotXValue = 0.0f;
	private float mPivotYValue = 0.0f;

	private float mPivotX;
	private float mPivotY;

	private float mGridDegree; // 格子角度
	private int mTime;// 转1圈的时间
	private int mIndex = 0;
	private float mCurDegree = -1f;

	public RouletteRotateAnimation(float fromDegrees, float toDegrees,
			int pivotXType, float pivotXValue, int pivotYType,
			float pivotYValue, float gridDegree, int time) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;

		mPivotXValue = pivotXValue;
		mPivotXType = pivotXType;
		mPivotYValue = pivotYValue;
		mPivotYType = pivotYType;
		mGridDegree = gridDegree;
		mTime = time;
	}

	public float getCurDegree() {
		return mCurDegree;
	}

	public int getTime() {
		return mTime;
	}

	// public void setDegrees(int index) {
	//
	// }

	public void setDegrees(float fromDegrees, float toDegrees) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
	}

	public void setIndex(int index) {
		if (mIndex >= index)
			mToDegrees = 360 - mGridDegree * (mIndex - index);
		else
			mToDegrees = mGridDegree * (index - mIndex);
		setRepeatCount(1);
		setDuration(mTime);
	}

	public int getIndex() {
		return mIndex;
	}

	public boolean isFirst() {
		return mIndex == 0;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float degrees = mFromDegrees
				+ ((mToDegrees - mFromDegrees) * interpolatedTime);

		mCurDegree = degrees;

		if (mPivotX == 0.0f && mPivotY == 0.0f) {
			t.getMatrix().setRotate(degrees);
		} else {
			t.getMatrix().setRotate(degrees, mPivotX, mPivotY);
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
