/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-8 下午4:44:05
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HonorRuleTip extends CustomConfirmDialog{

	public HonorRuleTip(String title, String desc) {
		super(title, MEDIUM);
		ViewUtil.setRichText(tip, R.id.desc, desc);
		setCloseBtn();
	}
	
	@Override
	protected View getContent() {
		return controller.inflate(R.layout.scroll_text, tip, false);
	}
}
