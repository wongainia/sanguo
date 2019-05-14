package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;
import com.vikings.sanguo.ui.window.BarWindow;
import com.vikings.sanguo.ui.window.HeroStrengthenListWindow;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BattleFailedTip extends CustomConfirmDialog implements
		OnClickListener {
	private static final int layout = R.layout.alert_battle_failed;

	private Button trainingBtn, strengthenBtn, refreshHeroBtn;

	public BattleFailedTip() {
		super("战争崛起指南", MEDIUM);
		trainingBtn = (Button) content.findViewById(R.id.trainingBtn);
		trainingBtn.setOnClickListener(this);

		strengthenBtn = (Button) content.findViewById(R.id.strengthenBtn);
		strengthenBtn.setOnClickListener(this);

		refreshHeroBtn = (Button) content.findViewById(R.id.refreshHeroBtn);
		refreshHeroBtn.setOnClickListener(this);
		setButton(FIRST_BTN, "离开", closeL);
	}

	public void show() {
		super.show();
	}

	@Override
	public View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == trainingBtn) {
			new ArmTrainingListWindow().open();
		} else if (v == strengthenBtn) {
			new HeroStrengthenListWindow().open();
		} else if (v == refreshHeroBtn) {
			new BarWindow().open();
		}
	}
}
