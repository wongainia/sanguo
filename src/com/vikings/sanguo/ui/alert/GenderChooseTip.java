package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class GenderChooseTip extends CustomConfirmDialog {

	private View view;

	public GenderChooseTip(View view) {
		super("性别选择", DEFAULT);
		this.view = view;
	}

	public void show() {
		setButton(FIRST_BTN, Config.sex[1], new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				ViewUtil.setText(view, Config.sex[1]);
				view.setTag(Constants.MALE);
			}
		});

		setButton(SECOND_BTN, Config.sex[0], new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				ViewUtil.setText(view, Config.sex[0]);
				view.setTag(Constants.FEMALE);
			}
		});

		setButton(THIRD_BTN, "不限", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				ViewUtil.setText(view, "不限");
				view.setTag(0);
			}
		});
		super.show();
	}

	@Override
	protected View getContent() {
		return null;
	}

}
