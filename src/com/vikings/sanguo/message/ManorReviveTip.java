package com.vikings.sanguo.message;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.BuildingBuyTip;
import com.vikings.sanguo.ui.window.ManorReviveWindow;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ManorReviveTip extends CustomConfirmDialog implements
		OnClickListener {
	private static final int layout = R.layout.alert_building;

	private Button healBtn, upgradeBtn;
	private TextView level, desc;
	private BuildingProp prop;
	private BuildingProp next;

	public ManorReviveTip(BuildingInfoClient bic, boolean isMyBuilding) {

		this(bic.getProp(),
				(isMyBuilding ? bic.getProp().getNextLevel() : null));
	}

	public ManorReviveTip(BuildingProp prop, BuildingProp nextProp) {
		super(prop.getBuildingName());
		this.prop = prop;
		this.next = nextProp;
	}

	public void show() {
		super.show();
		level = (TextView) content.findViewById(R.id.level);
		desc = (TextView) content.findViewById(R.id.desc);
		healBtn = (Button) content.findViewById(R.id.upgradeBtn);
		upgradeBtn = (Button) content.findViewById(R.id.upgradeTwoBtn);
		setValue();// 设值
	}

	private void setValue() {
		new ViewImgScaleCallBack(prop.getImage(),
				content.findViewById(R.id.buildingIcon),
				300 * Config.SCALE_FROM_HIGH, 153 * Config.SCALE_FROM_HIGH);
		LayoutParams params = (LayoutParams) level.getLayoutParams();
		params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		ViewUtil.setText(level, "LV" + prop.getLevel());
		ViewUtil.setVisible(desc, R.id.desc);
		ViewUtil.setRichText(desc, R.id.desc, prop.getDesc());

		ViewUtil.setRichText(healBtn, R.id.upgradeBtn, "医  治", true);
		healBtn.setOnClickListener(this);
		// 有下一级就显示
		if (null != next) {
			ViewUtil.setVisible(upgradeBtn, R.id.upgradeTwoBtn);
			ViewUtil.setRichText(upgradeBtn, R.id.upgradeTwoBtn, "升  级", true);
			upgradeBtn.setOnClickListener(this);
		} else {
			ViewUtil.setGone(upgradeBtn, R.id.upgradeTwoBtn);
		}

	}

	@Override
	public View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upgradeBtn:// 医治
			dialog.dismiss();
			new ManorReviveWindow().open(prop);
			break;

		case R.id.upgradeTwoBtn:// 升级
			dialog.dismiss();
			new BuildingBuyTip(next).show();
			break;
		}
	}
}
