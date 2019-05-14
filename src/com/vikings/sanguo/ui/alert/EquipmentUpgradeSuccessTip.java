package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentUpgradeSuccessTip extends ResultAnimTip {

	private EquipmentInfoClient eic;
	private int forceValue;

	public EquipmentUpgradeSuccessTip(EquipmentInfoClient eic, int forceValue) {
		super(true);
		this.eic = eic;
		this.forceValue = forceValue;
	}

	public void show() {
		SoundMgr.play(R.raw.sfx_evolution);
		super.show(null, false);
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_sjcg;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_equipment_levelup,
				rewardLayout, false);
		ViewGroup equipmentLayout = (ViewGroup) view
				.findViewById(R.id.equipmentLayout);
		ViewUtil.setRichText(equipmentLayout.findViewById(R.id.equipmentName),
				eic.getColorName());
		ViewUtil.setRichText(
				equipmentLayout.findViewById(R.id.equipmentQuality),
				eic.getColorTypeName());
		IconUtil.setEquipmentIcon(equipmentLayout, (byte) 0, eic, false);
		ViewUtil.setRichText(view.findViewById(R.id.upgrade_effect),
				"效果:  武力 +" + forceValue);
		return view;
	}
}
