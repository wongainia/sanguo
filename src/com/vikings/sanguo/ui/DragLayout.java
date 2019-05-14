package com.vikings.sanguo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;

/**
 * 拦截拖拽事件
 * 
 * @author Brad.Chen
 * 
 */
public class DragLayout extends AbsoluteLayout {

	public static final int MIN_MOVE_PX = (int) (10 * Config.SCALE_FROM_HIGH);

	protected float touchX;

	protected float touchY;

	protected float moveX;

	protected float moveY;

	protected float diff;

	protected float diffX;

	protected float diffY;

	private OnMoveListener onMove = null;

	private OnClickListener onClick = null;

	private OnTouchDownListener onTouchDown = null;

	public DragLayout(Context context) {
		super(context);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private boolean isMove(float src, float dest) {
		diff = src - dest;
		return diff > MIN_MOVE_PX || diff < -MIN_MOVE_PX;
	}

	protected boolean gt(float x, int compare) {
		return x > compare || x < -compare;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int x = (int) event.getRawX();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			touchX = x;
			return false;
		case MotionEvent.ACTION_UP:
			return false;
		case MotionEvent.ACTION_MOVE:
			diff = touchX - x;
			if (diff > Constants.MIN_MOVE_PX || diff < -MIN_MOVE_PX)
				return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			// 按下时候 可能是点击 可能是移动 所以先行记录2个起始坐标
			// 记录点击坐标
			// 记录移动开始坐标
			touchX = event.getX();
			touchY = event.getY();
			moveX = touchX;
			moveY = touchY;
			if (null != onTouchDown)
				onTouchDown.touchDown();
			break;
		case MotionEvent.ACTION_UP:
			// up的时候，可能是点击确认，可能是移动结束
			// 如果up的偏移够小，认为是一次点击事件
			if (!isMove(touchX, event.getX()) && !isMove(touchY, event.getY())) {
				if (onClick != null)
					onClick.click(touchX, touchY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isMove(moveX, event.getX()) || isMove(moveY, event.getY())) {
				diffX = event.getX() - moveX;
				diffY = event.getY() - moveY;
				if (onMove != null)
					onMove.move(diffX, diffY);

				moveX = event.getX();
				moveY = event.getY();
			}
			break;
		default:
			break;
		}
		return true;
	}

	public void setOnMove(OnMoveListener l) {
		this.onMove = l;
	}

	public void setOnClick(OnClickListener onClick) {
		this.onClick = onClick;
	}

	public void setOnTouchDown(OnTouchDownListener onTouchDown) {
		this.onTouchDown = onTouchDown;
	}

	public interface OnClickListener {
		void click(float x, float y);
	}

	public interface OnMoveListener {

		void move(float x, float y);

	}

	public interface OnTouchDownListener {
		void touchDown();
	}
}
