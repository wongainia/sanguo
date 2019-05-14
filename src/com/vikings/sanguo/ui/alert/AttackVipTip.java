package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class AttackVipTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_common_custom;

	public AttackVipTip() {
		super("你不是VIP用户", DEFAULT, false);
		setRightTopCloseBtn();
		setButton(FIRST_BTN, "立刻成为VIP", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				controller.openVipListWindow();
			}
		});
		ViewUtil.setRichText(
				content,
				R.id.desc,
				"很抱歉，你不是VIP用户，不能向VIP用户发起进攻。<br/><br/>充值后立刻成为VIP用户，您就可以肆意掠夺、大杀四方，更有精兵猛将前来效命，助你平定天下！");

	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
