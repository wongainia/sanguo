package com.vikings.sanguo.ui.alert;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class HolyDescTip extends CustomConfirmDialog {

	public HolyDescTip(HolyProp holyProp) {
		super(holyProp.getEvilDoorName(), DEFAULT, true);
		ViewUtil.setRichText(content, R.id.desc, holyProp.getDesc());
	}

	public void show() {
		super.show();
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_holy_desc, contentLayout,
				false);
	}

}
