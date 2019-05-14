package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vikings.sanguo.config.Config;

public class ProgressBar extends ImageView {

	private int cur = 0;

	private int mid = 0;

	private int total = 100;

	private Rect fBitmapRect = new Rect(), fSceenRect = new Rect(),
			mBitmapRect = new Rect(), mSceenRect = new Rect();

	private int fLeft = 0, mLeft = 0;

	private int midResId = -1;

	public ProgressBar(Context context) {
		super(context);
	}

	public ProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (cur < 0)
			cur = 0;
		if (cur > total)
			cur = total;
		if (total < 0)
			total = 100;

		if (getBackground() == null) {
			return; // couldn't resolve the URI
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return; // nothing to draw (empty bounds)
		}
		if (total == 0)
			return;
		getBackground().draw(canvas);

		// 画中间层
		if (midResId > 0) {
			BitmapDrawable m = (BitmapDrawable) Config.getController()
					.getDrawable(midResId);
			if (null != m) {
				mLeft = (getWidth() - m.getIntrinsicWidth()) / 2;

				mBitmapRect.set(0, 0, m.getBitmap().getWidth() * mid / total, m
						.getBitmap().getHeight());
				mSceenRect.set(mLeft, 0, m.getIntrinsicWidth() * mid / total
						+ mLeft, m.getIntrinsicHeight());
				canvas.drawBitmap(m.getBitmap(), mBitmapRect, mSceenRect, null);
			}

		}

		// 画前景

		BitmapDrawable f = (BitmapDrawable) getDrawable();

		fLeft = (getWidth() - f.getIntrinsicWidth()) / 2;

		// 尼玛啊。。。 Drawable是正确缩放过的大小，bitmap不一定
		// canvas draw的时候根据rect缩放，src根据bitmap计算，dst根据drawable计算
		fBitmapRect.set(0, 0, f.getBitmap().getWidth() * cur / total, f
				.getBitmap().getHeight());

		fSceenRect.set(fLeft, 0, f.getIntrinsicWidth() * cur / total + fLeft,
				f.getIntrinsicHeight());
		canvas.drawBitmap(f.getBitmap(), fBitmapRect, fSceenRect, null);
	}

	public void set(int cur, int total, int mid, int midResId) {
		this.mid = mid;
		this.midResId = midResId;
		set(cur, total);
	}

	public void set(int cur, int total) {
		this.cur = cur;
		this.total = total;
		this.invalidate();
	}

	public void set(int cur) {
		this.cur = cur;
		this.invalidate();
	}

	public void setTotal(int total) {
		this.total = total;
		this.invalidate();
	}

}
