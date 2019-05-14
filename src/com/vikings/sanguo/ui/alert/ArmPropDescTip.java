package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.PropTroopDesc;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ArmPropDescTip extends CustomConfirmDialog {
	private ViewGroup descLayout;

	public ArmPropDescTip() {
		super("属性介绍", MEDIUM);
		setCloseBtn();
		descLayout = (ViewGroup) content.findViewById(R.id.descLayout);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		List<PropTroopDesc> list = CacheMgr.propTroopDescCache.getAll();

		for (PropTroopDesc prop : list) {

			ViewGroup viewGroup = (ViewGroup) controller
					.inflate(R.layout.alert_arm_prop_desc_item);
			ViewUtil.setText(viewGroup, R.id.name, prop.getName());
			ViewUtil.setText(viewGroup, R.id.desc, prop.getDesc());
			ViewUtil.setWidthFillParent(viewGroup);
			descLayout.addView(viewGroup);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_arm_prop_desc, tip, false);
	}

}
