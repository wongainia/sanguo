package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;

public class ResetChargeTip extends Alert implements OnClickListener {
	private ViewGroup content;

	public ResetChargeTip() {
		content = (ViewGroup) controller.inflate(R.layout.alert_reset_charge);
		ViewUtil.setText(content, R.id.msg, "元宝不足, 请前往充值!");
		TextView recharge = (TextView) content.findViewById(R.id.recharge);
		recharge.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		controller.openRechargeCenterWindow();
	}

	public void show() {
		super.show(content);
	}
}
