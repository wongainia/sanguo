package com.vikings.sanguo.ui.alert;

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
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuildingTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_building;

	private Button upgradeBtn;
	private TextView level, desc;
	private BuildingProp prop;
	private BuildingProp next;

	public BuildingTip(BuildingInfoClient bic, boolean isMyBuilding) {
		this(bic.getProp(),
				(isMyBuilding ? bic.getProp().getNextLevel() : null));
	}

	public BuildingTip(BuildingProp prop, BuildingProp nextProp) {
		super(prop.getBuildingName());
		this.prop = prop;
		this.next = nextProp;
	}

	public void show() {
		super.show();
		level = (TextView) content.findViewById(R.id.level);
		desc = (TextView) content.findViewById(R.id.desc);
		upgradeBtn = (Button) content.findViewById(R.id.upgradeBtn);
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
		// 有下一级就显示
		if (null != next) {
			upgradeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					new BuildingBuyTip(next).show();
				}
			});
		} else {
			ViewUtil.setGone(upgradeBtn, R.id.upgradeBtn);
		}
	}

	@Override
	public View getContent() {
		return controller.inflate(layout);
	}
}
