package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FiefAttackTip extends CustomConfirmDialog implements
		OnClickListener {
	private Button famousBtn; // 圣都
	private Button holyBtn; // 外敌入侵
	private Button recommendRivalBtn;// 推荐对手
	private Button reinforceBtn; // 增援

	public FiefAttackTip() {
		super("查  找", CustomConfirmDialog.DEFAULT);
		setCloseBtn();

		ViewUtil.setBold((TextView) content.findViewById(R.id.mode));

		famousBtn = (Button) content.findViewById(R.id.famousBtn);
		ViewUtil.setVisible(famousBtn);
		ViewUtil.setBold(famousBtn);
		famousBtn.setOnClickListener(this);

		recommendRivalBtn = (Button) content
				.findViewById(R.id.recommendRivalBtn);
		ViewUtil.setVisible(recommendRivalBtn);
		ViewUtil.setBold(recommendRivalBtn);
		recommendRivalBtn.setOnClickListener(this);

		reinforceBtn = (Button) content.findViewById(R.id.reinforceBtn);
		ViewUtil.setVisible(reinforceBtn);
		ViewUtil.setBold(reinforceBtn);
		reinforceBtn.setOnClickListener(this);

		holyBtn = (Button) content.findViewById(R.id.holyBtn);
		ViewUtil.setVisible(holyBtn);
		ViewUtil.setBold(holyBtn);
		holyBtn.setOnClickListener(this);

		ViewUtil.setText(content, R.id.mode, "--请选择要出征的领地--");
	}

	@Override
	public void onClick(View v) {
		if (v == famousBtn) {
			dismiss();
			new FiefHolySearchTip("争夺圣地", true).show();
		} else if (v == reinforceBtn) {
			dismiss();
			new ReinforceTip().show();
		} else if (v == holyBtn) {
			dismiss();
			new FiefHolySearchTip("外敌入侵", false).show();
		} else if (v == recommendRivalBtn) {
			dismiss();
			new FatSheepTip().show();
		}
	}

	@Override
	public View getContent() {
		return Config.getController().inflate(R.layout.alert_fief_search, tip,
				false);
	}
}
