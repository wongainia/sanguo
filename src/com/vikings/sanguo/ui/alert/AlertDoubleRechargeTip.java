package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;

public class AlertDoubleRechargeTip extends ResultAnimTip {

	public AlertDoubleRechargeTip() {
		super(false);
	}

	public void show() {
		show(null, false);
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_jfbm;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.alert_result_double_recharge,
				rewardLayout, false);

		view.findViewById(R.id.i_know).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
		return view;
	}
}
