package com.vikings.sanguo.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

public class DiscountView extends TextView {
	private Paint paint = new Paint();

	public DiscountView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPaint();
	}

	public DiscountView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPaint();
	}

	public DiscountView(Context context) {
		super(context);
		setPaint();
	}

	private void setPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setColor(Color.YELLOW);
		paint.setStrokeWidth((float) 4);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int middle = (this.getBottom() - this.getTop()) / 2;
		canvas.drawLine(this.getLeft(), middle, this.getRight(), middle, paint);
	}
}
