package com.vikings.sanguo.ui.window;

import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.ui.adapter.EquipmentReplaceAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentReplaceWindow extends EquipmentWearWindow {
	private EquipmentInfoClient eic;
	private ViewGroup topLayout;

	/**
	 * @param hic
	 *            要装备的将领
	 * @param type
	 *            装备类型
	 */
	public void open(EquipmentInfoClient eic, HeroInfoClient hic) {
		this.eic = eic;
		super.open(hic, eic.getProp().getType());
	}

	@Override
	protected void init() {
		super.init("替换装备");
		setContentBelowTitle(R.layout.equipment_replace_top);
		topLayout = (ViewGroup) window.findViewById(R.id.topLayout);
		ViewUtil.setRichText(topLayout, R.id.gradientMsg,
				"为 " + hic.getColorTypeName() + hic.getColorHeroName()
						+ " 替换一件" + PropEquipment.getTypeName(type));
	}

	@Override
	public void showUI() {
		setEquipmentInfo();
		super.showUI();
	}

	private void setEquipmentInfo() {
		IconUtil.setEquipmentIcon(topLayout, eic.getProp().getType(), eic,
				false);
		ViewUtil.setRichText(topLayout.findViewById(R.id.name), StringUtil
				.color(eic.getProp().getName(), eic.getEquipmentDesc()
						.getColor()));
		ViewUtil.setRichText(topLayout.findViewById(R.id.quality), StringUtil
				.color("(" + eic.getEquipmentDesc().getName() + ")", eic
						.getEquipmentDesc().getColor()));

		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				eic.getEquipmentId(), (byte) eic.getQuality(), eic.getLevel());
		if (null != ee) {
			ViewUtil.setText(topLayout, R.id.properties,
					"【属性】" + ee.getEffectTypeName());
		} else {
			ViewUtil.setText(topLayout, R.id.properties, "【属性】无");
		}

		EquipEffectClient effect = eic.getEffect();
		if (effect != null && effect.getType() > 0) {
			EquipmentForgeEffect skill = CacheMgr.getEffectById(effect
					.getType());
			ViewUtil.setText(topLayout, R.id.effect,
					"【特效】" + skill.getEffectDesc());
		} else {
			ViewUtil.setText(topLayout, R.id.effect, "【特效】无");
		}

		if (eic.hasStone()) {
			ViewUtil.setText(topLayout, R.id.stone, "【宝石】"
					+ eic.getStone().getName());
		} else {
			ViewUtil.setText(topLayout, R.id.stone, "【宝石】无");
		}

	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new EquipmentReplaceAdapter(eic, hic);
	}

}
