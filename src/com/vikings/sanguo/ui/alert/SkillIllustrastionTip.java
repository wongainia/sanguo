/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-30 上午11:38:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class SkillIllustrastionTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_skill_illu;
	private Item item;

	public SkillIllustrastionTip(Item item) {
		super(item.getName(), MEDIUM, true);
		this.item = item;
	}

	public void show() {
		if (item == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		new ViewImgScaleCallBack(item.getImage(), findViewById(R.id.icon),
				Config.SCALE_FROM_HIGH * Constants.ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.ICON_HEIGHT);
		ViewUtil.setText(findViewById(R.id.type), "类型：" + item.getTypeName());
		ViewUtil.setText(findViewById(R.id.quality), "品质：" + item.getUseTip());
		ViewUtil.setRichText(findViewById(R.id.desc), item.getDesc());
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
