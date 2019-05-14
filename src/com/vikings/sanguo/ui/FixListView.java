package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class FixListView extends ListView {

	public FixListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FixListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixListView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		try {
			return super.drawChild(canvas, child, drawingTime);
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} catch (Exception e) {
		}

	}

	@Override
	protected void layoutChildren() {
		try {
			super.layoutChildren();

		} catch (Exception e) {
			Log.e("FixListView", e.getMessage(), e);
		}
	}

}
