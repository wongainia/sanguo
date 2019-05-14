package com.vikings.sanguo.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.vikings.sanguo.utils.ImageUtil;

public class PressedImageButton extends ImageButton implements
		OnFocusChangeListener, OnTouchListener {


	public PressedImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PressedImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setOnFocusChangeListener(this);
		this.setOnTouchListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		Drawable d = this.getDrawable();
		if (d == null || !(d instanceof BitmapDrawable))
			return;
		if (hasFocus) {
			d.setColorFilter(ImageUtil.textHighLightFilter);
		} else{
			d.clearColorFilter();
			this.invalidate();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Drawable d = this.getDrawable();
		if (d == null || !(d instanceof BitmapDrawable))
			return false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			d.setColorFilter(ImageUtil.textHighLightFilter);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			d.clearColorFilter();
			this.invalidate();
			break;
		}
		return false;
	}

}
