package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BaseHeroInfoClient;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EquipmentTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_equipment;
	private ViewGroup equipmentLayout, equipmentEffectLayout,
			stoneEffectLayout;
	private TextView name, quality, properties, description;
	private ImageView rating;

	public EquipmentTip() {
		super("装备详情");
		equipmentLayout = (ViewGroup) tip.findViewById(R.id.equipmentLayout);
		equipmentEffectLayout = (ViewGroup) tip
				.findViewById(R.id.equipmentEffectLayout);
		stoneEffectLayout = (ViewGroup) tip
				.findViewById(R.id.stoneEffectLayout);
		name = (TextView) tip.findViewById(R.id.name);
		quality = (TextView) tip.findViewById(R.id.quality);
		properties = (TextView) tip.findViewById(R.id.properties);
		description = (TextView) tip.findViewById(R.id.description);
		rating = (ImageView) tip.findViewById(R.id.rating);
		setButton(FIRST_BTN, "关闭", closeL);
	}

	public void show(EquipmentInfoClient eic, BaseHeroInfoClient ohic) {
		super.show();
		setValue(eic, ohic);
	}

	private void setValue(EquipmentInfoClient eic, BaseHeroInfoClient ohic) {
		IconUtil.setEquipmentIcon(equipmentLayout, (byte) 0, eic, false);
		ViewUtil.setRichText(name, StringUtil.color(eic.getProp().getName(),
				eic.getEquipmentDesc().getColor()));
		ViewUtil.setRichText(quality, StringUtil.color("("
				+ eic.getEquipmentDesc().getName() + ")", eic
				.getEquipmentDesc().getColor()));
		ViewUtil.setImage(rating, eic.getProp().getRatingPic());

		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				eic.getEquipmentId(), (byte) eic.getQuality(), eic.getLevel());
		if (null != ee) {
			ViewUtil.setVisible(properties);
			ViewUtil.setText(
					properties,
					ee.getEffectTypeName() + "+"
							+ ee.getEffectValue(eic.getLevel()));
		} else {
			ViewUtil.setGone(properties);
		}

		ViewUtil.setRichText(description, eic.getProp().getDesc());

		EquipEffectClient effect = eic.getEffect();
		if (effect != null && effect.getType() > 0) {
			EquipmentForgeEffect skill = CacheMgr.getEffectById(effect
					.getType());
			ViewUtil.setRichText(equipmentEffectLayout, R.id.effect,
					skill.getEffectDesc());
		} else {
			ViewUtil.setText(equipmentEffectLayout, R.id.effect, "无");
		}

		if (null == eic.getStone())
			ViewUtil.setText(stoneEffectLayout, R.id.effect, "无");
		else
			ViewUtil.setRichText(stoneEffectLayout, R.id.effect, eic.getStone()
					.getDesc());

		// 套装效果
		IconUtil.setSuitEffect(tip, ohic, eic);
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
