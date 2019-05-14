package com.vikings.sanguo.ui.window;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RuleSpecTip extends CustomConfirmDialog {
	String spec;

	public RuleSpecTip(String title, String spec) {
		super(title);
		this.spec = spec;
	}

	@Override
	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		ViewUtil.setRichText(tip, R.id.gamble_spc, spec);
		setCloseBtn();
		tip.findViewById(R.id.scroll).setVerticalScrollBarEnabled(false);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.gamble_recharge_direct,
				contentLayout, false);
	}
}
