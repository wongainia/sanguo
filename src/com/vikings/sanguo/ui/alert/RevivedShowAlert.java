/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-3-27 下午2:13:15
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

import android.view.View;
import android.widget.TextView;

public class RevivedShowAlert extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_revived_show;
	private TextView desc;

	public RevivedShowAlert() {
		super("医馆说明");
	}

	public void show() {
		super.show();
		desc = (TextView) content.findViewById(R.id.desc);
		setValue();
	}

	private void setValue() {
		ViewUtil.setRichText(desc,
				CacheMgr.uiTextCache.getTxt(UITextProp.MANOR_REVIVE_RULE));
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

}
