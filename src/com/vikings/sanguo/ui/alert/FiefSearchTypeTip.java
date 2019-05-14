package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public abstract class FiefSearchTypeTip extends CustomConfirmDialog {
	protected Button type1Btn;
	protected Button type2Btn;
	protected Button type3Btn;

	public FiefSearchTypeTip() {
		super("选择查找方式", CustomConfirmDialog.DEFAULT);
		setCloseBtn();

		type1Btn = (Button) tip.findViewById(R.id.type1Btn);
		type2Btn = (Button) tip.findViewById(R.id.type2Btn);
		type3Btn = (Button) tip.findViewById(R.id.type3Btn);

	}

	public void show() {
		setBtnText();
		setClickListener();
		super.show();
	}

	protected abstract void setClickListener();

	protected abstract void setBtnText();

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_holy_fief, tip,
				false);
	}
}
