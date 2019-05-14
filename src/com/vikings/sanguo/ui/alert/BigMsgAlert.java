/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-29 上午9:47:03
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BigMsgAlert extends Alert {
	private final static int layout = R.layout.alert_big_msg;
	private View content;
	private TextView msg;

	public BigMsgAlert() {
		super(true);
		content = Config.getController().inflate(layout);
		msg = (TextView) content.findViewById(R.id.msg);
		content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void show() {
		content.requestLayout();
		super.show(content);
		this.dialog.setOnDismissListener(null);
	}

	public View getLayout() {
		return this.content;
	}

	public void show(String msgStr, boolean playDefaultSound) {
		if (playDefaultSound)
			SoundMgr.play(R.raw.sfx_tips2);

		setText(msg, msgStr);
		show();
	}

	private void setText(View view, String str) {
		if (StringUtil.isNull(str))
			ViewUtil.setGone(view);
		else {
			ViewUtil.setVisible(view);
			ViewUtil.setRichText(view, str);
		}
	}

	public TextView getMsgView() {
		return msg;
	}
}
