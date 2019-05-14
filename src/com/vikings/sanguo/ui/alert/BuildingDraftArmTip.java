package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.window.ArmTrainingListWindow;
import com.vikings.sanguo.ui.window.ArmTrainingWindow;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuildingDraftArmTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_building;

	private BuildingProp prop;
	private BuildingProp next;

	public BuildingDraftArmTip(BuildingInfoClient bic, boolean isMyBuilding) {
		this(bic.getProp(),
				(isMyBuilding ? bic.getProp().getNextLevel() : null));
	}

	public BuildingDraftArmTip(BuildingProp buildingProp, BuildingProp nextProp) {
		super(MEDIUM, null == nextProp);
		this.prop = buildingProp;
		this.next = nextProp;

		setButton(FIRST_BTN, "招兵", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (prop.getType() == BuildingProp.BUILDING_TYPE_JUNHUNJITAN) {
					new ArmTrainingListWindow().open();
				} else {
					ManorDraft manorDraft = CacheMgr.manorDraftCache
							.getSingleDraftByBuildingId(prop.getId());
					if (null != manorDraft) {
						try {
							TroopProp troopProp = (TroopProp) CacheMgr.troopPropCache
									.get(manorDraft.getArmId());
							new ArmTrainingWindow().open(troopProp);
						} catch (GameException e) {
							Log.e("BuildingDraftArmTip", e.getMessage());
						}

					}
				}
			}
		});

		if (null != next) {
			setButton(SECOND_BTN, "升级", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					new BuildingBuyTip(next).show();
				}
			});
		}

		setButton(THIRD_BTN, "关闭", closeL);
	}

	public void show() {
		setValue();// 设值
		super.show();
	}

	private void setValue() {

		setTitle(prop.getBuildingName());
		new ViewImgScaleCallBack(prop.getImage(),
				content.findViewById(R.id.buildingIcon),
				300 * Config.SCALE_FROM_HIGH, 153 * Config.SCALE_FROM_HIGH);
		TextView level = (TextView) content.findViewById(R.id.level);
		LayoutParams params = (LayoutParams) level.getLayoutParams();
		params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		ViewUtil.setText(level, "Lv" + prop.getLevel());
		ViewUtil.setVisible(content, R.id.desc);
		ViewUtil.setRichText(content, R.id.desc, prop.getDesc());
	}

	@Override
	public View getContent() {
		return controller.inflate(layout);
	}
}
