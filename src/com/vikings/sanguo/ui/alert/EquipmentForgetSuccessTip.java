package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentForgetSuccessTip extends ResultAnimTip {

	private EquipmentInfoClient eic;

	public EquipmentForgetSuccessTip(EquipmentInfoClient eic) {
		super(true);
		this.eic = eic;
	}

	public void show() {
		SoundMgr.play(R.raw.sfx_evolution);
		super.show(null, false);
	}

	@Override
	protected int getDrawable() {
		return R.drawable.txt_qhcg;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_equipment_forge,
				rewardLayout, false);
		ViewGroup equipmentLayout = (ViewGroup) view
				.findViewById(R.id.equipmentLayout);
		ViewUtil.setRichText(equipmentLayout
				.findViewById(R.id.equipmentQuality), StringUtil.color(eic
				.getEffect().getEffect().getEffectName(), eic
				.getEquipmentDesc().getColor()));
		IconUtil.setEquipmentIcon(equipmentLayout, (byte) 0, eic, false);
		ViewUtil.setRichText(view.findViewById(R.id.upgrade_effect), "效果: "
				+ eic.getEffect().getEffect().getEffectDesc());

		ViewUtil.setImage(view.findViewById(R.id.stateIconSmall),
				R.drawable.forge_state_hot_small);
		ViewUtil.setVisible(view.findViewById(R.id.stateDesc));

		ViewUtil.setText(view.findViewById(R.id.stateDesc), "系统赠送"
				+ CacheMgr.equipmentCommonConfigCache.getRewardRate() + "%强化值，"
				+ DateUtil.formatSecond(eic.getEffect().getHotUpTime())
				+ "内没有强化到下一级，赠送值将消失");

		return view;
	}
}
