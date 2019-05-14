/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-27 上午10:08:32
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ActClearConfirmTip extends CustomConfirmDialog{

	public ActClearConfirmTip(String title, String msg1, String msg2,
			String btnStr, OnClickListener l) {
		super(title, DEFAULT);
		ViewUtil.setRichText(tip, R.id.msg, msg1, true);
		ViewUtil.setRichText(tip, R.id.confirm, msg2, true);
		setButton(FIRST_BTN, null == btnStr ? "确    定" : btnStr,
				null == l ? closeL : l);
	}
	
	@Override
	protected View getContent() {
		return controller.inflate(R.layout.setoff_confirm, tip, false);
	}

}
