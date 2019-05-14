package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class PressedZoomButton extends Button implements OnFocusChangeListener,
		OnTouchListener {

	public static int zoom = 90;

	public PressedZoomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PressedZoomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setOnFocusChangeListener(this);
		this.setOnTouchListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Drawable d = this.getBackground();
		if (d == null)
			return;
		int w = v.getWidth();
		int h = v.getHeight();
		// int w = d.getIntrinsicWidth();
		// int h = d.getIntrinsicHeight();
		if (hasFocus) {
			int w1 = w * zoom / 100;
			int h1 = h * zoom / 100;
			d.setBounds((w - w1) / 2, (h - h1) / 2, w / 2 + w1 / 2, h / 2 + h1
					/ 2);
		} else {
			d.setBounds(0, 0, w, h);
		}
		this.invalidate();
	}

	// 外部控制按钮放大或者缩小
	public void handZoom(boolean hasZoom) {
		this.onFocusChange(this, hasZoom);
	}

	// 外部控制按钮放大或者缩小
	public void handZoomSmall(boolean hasZoom) {
		zoom = 90;
		this.onFocusChange(this, hasZoom);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Drawable d = this.getBackground();
		if (d == null)
			return false;
		int w = v.getWidth();
		int h = v.getHeight();
		// int w = d.getIntrinsicWidth();
		// int h = d.getIntrinsicHeight();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int w1 = w * zoom / 100;
			int h1 = h * zoom / 100;
			d.setBounds((w - w1) / 2, (h - h1) / 2, w / 2 + w1 / 2, h / 2 + h1
					/ 2);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			d.setBounds(0, 0, w, h);
			break;
		}
		this.invalidate();
		return false;
	}

}
