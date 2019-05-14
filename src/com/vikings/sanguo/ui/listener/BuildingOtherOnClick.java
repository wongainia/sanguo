package com.vikings.sanguo.ui.listener;

import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.ui.CastleUI;
import com.vikings.sanguo.ui.alert.BuildingTip;

public class BuildingOtherOnClick implements CastleUI.BuildingClickListener {

	@Override
	public void click(BuildingInfoClient b) {
		new BuildingTip(b, false).show();

	}

}
