/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-6-29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.alert;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.CallBack;
public class TouchCloseDialog extends Dialog {
	protected CallBack callBack;
	
	public TouchCloseDialog(Context context, CallBack callBack) {
		super(context, R.style.dialog);
		this.callBack = callBack;
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP)
			callBack.onCall();
		return super.onTouchEvent(event);
	}
}
