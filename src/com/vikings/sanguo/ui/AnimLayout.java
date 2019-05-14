package com.vikings.sanguo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsoluteLayout;

import com.vikings.sanguo.thread.CallBack;

@SuppressWarnings("deprecation")
public class AnimLayout extends AbsoluteLayout {
	public boolean doOnAnimationEnd = true;
	
	private CallBack callBack;
	
	public AnimLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AnimLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setCallBack(CallBack callBack)
	{
		this.callBack = callBack;
	}
	
	/**
	 * 动画结束后调用onCall，具体内容用到时设置
	 */
	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		if(null != callBack)
		{
			callBack.onCall();
		}
	}

	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
	}
}
