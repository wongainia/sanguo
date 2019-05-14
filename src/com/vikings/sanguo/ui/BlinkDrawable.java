package com.vikings.sanguo.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

public class BlinkDrawable extends Drawable {

	private Drawable d;

	private View v;

	private boolean draw = true;

	public BlinkDrawable(Drawable d, View v) {
		this.d = d;
		this.v = v;
	}

	@Override
	public void draw(Canvas canvas) {
		setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		if (draw) {
			d.setBounds(getBounds());
			d.draw(canvas);
		} else {
			canvas.drawColor(Color.TRANSPARENT);
		}
		draw = !draw;
		v.postInvalidateDelayed(1000);
	}

	@Override
	public void setAlpha(int alpha) {
		d.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		d.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return d.getOpacity();
	}

	@Override
	public int getIntrinsicHeight() {
		return d.getIntrinsicHeight();
	}

	@Override
	public int getIntrinsicWidth() {
		return d.getIntrinsicWidth();
	}

}
