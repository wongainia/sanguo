package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.utils.ViewUtil;

public class FiefPositionSearchTip extends FiefSearchTypeTip implements
		OnClickListener {

	@Override
	public void onClick(View v) {
		if (v == type1Btn) {
			dismiss();
			new FiefIndexSearchTip().show();
		} else if (v == type2Btn) {
			dismiss();
			new FiefCitySearchTip().show();
		}
	}

	@Override
	protected void setClickListener() {
		type1Btn.setOnClickListener(this);
		type2Btn.setOnClickListener(this);
	}

	@Override
	protected void setBtnText() {
		ViewUtil.setText(type1Btn, "坐标查找");
		ViewUtil.setBold(type1Btn);
		ViewUtil.setText(type2Btn, "城市地图");
		ViewUtil.setBold(type2Btn);
		ViewUtil.setGone(type3Btn);
	}
}
