package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentDesc;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItem;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EquipmentItemTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_buy_equipment;

	private ViewGroup equipmentLayout, equipmentEffectLayout,
			stoneEffectLayout, suitEffectLayout;
	private TextView type, price, prop, desc;
	private ImageView rating;

	private EquipmentInfoClient eic;
	private PropEquipment equipment;

	public EquipmentItemTip(EquipmentInfoClient equipmentInfoClient) {
		super(equipmentInfoClient.getProp().getName());
		this.eic = equipmentInfoClient;
		this.equipment = eic.getProp();

		equipmentLayout = (ViewGroup) content
				.findViewById(R.id.equipmentLayout);
		equipmentEffectLayout = (ViewGroup) content
				.findViewById(R.id.equipmentEffectLayout);
		stoneEffectLayout = (ViewGroup) content
				.findViewById(R.id.stoneEffectLayout);
		suitEffectLayout = (ViewGroup) content
				.findViewById(R.id.suitEffectLayout);
		ViewUtil.setGone(content, R.id.limitLayout);
		type = (TextView) content.findViewById(R.id.type);
		ViewUtil.setGone(content, R.id.discountName);
		ViewUtil.setGone(content, R.id.oldPrice);
		price = (TextView) content.findViewById(R.id.price);
		prop = (TextView) content.findViewById(R.id.prop);
		desc = (TextView) content.findViewById(R.id.equipmentDesc);

		rating = (ImageView) content.findViewById(R.id.rating);

		setButton(FIRST_BTN, "出售", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				new EquipmentSellInput(eic).show();
			}
		});

		setButton(SECOND_BTN, "关闭", closeL);
	}

	public void show() {
		if (null == equipment)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		IconUtil.setEquipmentIcon(equipmentLayout, equipment,
				eic.getEquipmentDesc(), eic.getLevel());

		ViewUtil.setGone(equipmentLayout, R.id.equipmentLayoutItem);

		ViewUtil.setImage(rating, equipment.getRatingPic());
		ViewUtil.setText(type, "类型：" + equipment.getTypeName());
		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				equipment.getId(), (byte) eic.getQuality(), eic.getLevel());
		if (null != ee) {
			ViewUtil.setVisible(prop);
			ViewUtil.setRichText(
					prop,
					"属性："
							+ StringUtil.color(ee.getEffectTypeName() + "+"
									+ ee.getEffectValue(eic.getLevel()),
									R.color.color16));
		} else {
			ViewUtil.setGone(prop);
		}

		EquipEffectClient effect = eic.getEffect();
		if (effect != null && effect.getType() > 0) {
			EquipmentForgeEffect skill = CacheMgr.getEffectById(effect
					.getType());
			if (eic.getQuality() >= effect.getMinQuality()) {
				ViewUtil.setRichText(equipmentEffectLayout, R.id.effect,
						skill.getEffectDesc());
			} else {
				try {
					EquipmentDesc equipmentDesc = (EquipmentDesc) CacheMgr.equipmentDescCache
							.get((byte) effect.getMinQuality());
					ViewUtil.setRichText(
							equipmentEffectLayout,
							R.id.effect,
							skill.getEffectDesc()
									+ StringUtil.color(
											"(达到" + equipmentDesc.getName()
													+ "品质时开启)", R.color.color20));
				} catch (GameException e) {
					e.printStackTrace();
				}
			}

		} else {
			ViewUtil.setText(equipmentEffectLayout, R.id.effect, "无");
		}

		if (eic.hasStone()) {
			try {
				EquipmentInsertItem eii = (EquipmentInsertItem) CacheMgr.equipmentInsertItemCache
						.get(eic.getSlotItemId());
				ViewUtil.setRichText(stoneEffectLayout, R.id.effect,
						eii.getDesc());
			} catch (GameException e) {
				Log.e("EquipmentDetailWindow", e.getMessage());
			}
		} else {
			ViewUtil.setText(stoneEffectLayout, R.id.effect, "无");
		}

		if (equipment.getSell() > 0) {
			ViewUtil.setRichText(price, "售价：#money#" + equipment.getSell());
		} else {
			ViewUtil.setRichText(
					price,
					"售价："
							+ StringUtil.color("不可出售", Config.getController()
									.getResourceColorText(R.color.color11)));
		}

		BattleSkill skill = null;
		if (equipment.isSuitEq()) {
			int skillId = CacheMgr.battleSkillEquipmentCache
					.getSuitBattleSkillId(equipment.getId());
			if (skillId > 0)
				skill = CacheMgr.getBattleSkillById(skillId);
		}
		if (null != skill) {
			ViewUtil.setVisible(suitEffectLayout);
			ViewUtil.setRichText(suitEffectLayout, R.id.effect,
					skill.getEffectDesc());
		} else {
			ViewUtil.setGone(suitEffectLayout);
		}

		ViewUtil.setRichText(desc, equipment.getDesc());
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

}
