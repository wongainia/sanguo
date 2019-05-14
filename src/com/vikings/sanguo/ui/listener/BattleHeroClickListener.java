package com.vikings.sanguo.ui.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.ui.window.BattleLogHeroDetailWindow;

public class BattleHeroClickListener implements OnClickListener {
	private BattleLogHeroInfoClient blhic;

	public BattleHeroClickListener(BattleLogHeroInfoClient blhic) {
		this.blhic = blhic;
	}

	@Override
	public void onClick(View v) {
		new BattleLogHeroDetailWindow().open(blhic);
	}

}
