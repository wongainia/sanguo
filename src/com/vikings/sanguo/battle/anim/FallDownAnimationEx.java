package com.vikings.sanguo.battle.anim;

import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FallDownAnimationEx extends Animation {
	private int xCenter;
	private int yCenter;
	private int dis;
	private int time;
	private int startOffset;
	private boolean isLeft;

	public FallDownAnimationEx(boolean isLeft, int xCenter, int yCenter,
			int dis, int time, int startOffset) {
		this.isLeft = isLeft;
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.dis = dis / 2;
		this.time = time;
		this.startOffset = startOffset;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		setDuration(time);
		setStartOffset(startOffset);
		setFillAfter(true);
	}

	private long start_time = 0;

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (start_time == 0) {
			start_time = System.currentTimeMillis();
		}
		Log.d("FallDownAnimationEx", "" + interpolatedTime);
		Matrix matrix = t.getMatrix();
		// matrix.setTranslate(dis*interpolatedTime, -dis*interpolatedTime);
		int degree = (int) ((System.currentTimeMillis() - start_time) * 180 / time);
		if (isLeft) {
			matrix.setTranslate(-dis * interpolatedTime, dis * interpolatedTime);
			matrix.preRotate(-degree, xCenter - dis * interpolatedTime, yCenter
					+ dis * interpolatedTime);
		} else {
			matrix.setTranslate(dis * interpolatedTime, -dis * interpolatedTime);
			matrix.preRotate(degree, xCenter + dis * interpolatedTime, yCenter
					- dis * interpolatedTime);
		}
	}

}
