package com.vikings.sanguo.ui.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.ui.window.OthersHeroDetailWindow;

public class OtherHeroClickListener implements OnClickListener {
	private OtherHeroInfoClient ohic;

	public OtherHeroClickListener(OtherHeroInfoClient ohic) {
		this.ohic = ohic;
	}

	@Override
	public void onClick(View v) {
		new OthersHeroDetailWindow().open(ohic);
	}
}
