package com.vikings.sanguo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Rect;

/**
 * 可以响应区域点击事件
 * 
 * @author susong
 * 
 */
public class AreaClickView extends View {
	private OnAreaClickListener listener; // 点击事件监听
	private Rect rect; // 点击事件响应区域
	private boolean inRect; // 按下时是否在区域内
	private float downX;// 按下的坐标X
	private float downY;// 按下的坐标Y
	private float upX;// 抬起的坐标X
	private float upY;// 抬起的坐标Y
	private long time;// 按下的时间点
	private static float MAX_DISTANCE = 10 * Config.SCALE_FROM_HIGH;// 超过该距离则不认为是点击事件；
	private long MAX_TIME = 1000;// 按下时间超过该时间则不认为是点击事件

	public AreaClickView(Context context) {
		super(context);
	}

	public AreaClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AreaClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 坐标原点为该view本身的左上角
	 * 
	 * @param listener
	 *            响应事件
	 * @param rect
	 *            点击区域
	 */
	public void setOnAreaClickListener(OnAreaClickListener listener, Rect rect) {
		this.listener = listener;
		this.rect = rect;
	}

	/**
	 * 坐标原点为该view本身的左上角
	 * @param listener
	 *            响应事件
	 * @param left
	 *            点击区域的左上点x
	 * @param top
	 *            点击区域的左上点y
	 * @param right
	 *            点击区域的右下点x
	 * @param bottom
	 *            点击区域的右下点y
	 */
	public void setOnAreaClickListener(OnAreaClickListener listener, int left,
			int top, int right, int bottom) {
		Rect rect = new Rect(left, top, right, bottom);
		setOnAreaClickListener(listener, rect);
	}

	public interface OnAreaClickListener {
		void onAreaClick(View v);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			inRect = false;// 重置inRect
			if (null != rect)
				inRect = rect.isInRect(x, y);// 判断是否在响应区域内
			downX = x;
			downY = y;
			time = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			upX = x;
			upY = y;
			if (!inRect || downX - upX < MAX_DISTANCE
					|| upX - downX > MAX_DISTANCE || downY - upY > MAX_DISTANCE
					|| upY - downY > MAX_DISTANCE
					|| (System.currentTimeMillis() - time > MAX_TIME)) {
				// 之上情况全认为非点击事件
				// return false;
			} else {
				if (null != listener)
					listener.onAreaClick(this);
			}
			break;
		default:
			break;
		}
		if (inRect)
			return true;
		else {// 如果touch事件不是发生在响应区域内，传给父类，进行其他事件的判断
			return super.onTouchEvent(event);
		}
	}
}
