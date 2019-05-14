package com.vikings.sanguo.ui.window;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentItemRemoveResp;
import com.vikings.sanguo.model.EquipEffectClient;
import com.vikings.sanguo.model.EquipmentDesc;
import com.vikings.sanguo.model.EquipmentEffect;
import com.vikings.sanguo.model.EquipmentForgeEffect;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItem;
import com.vikings.sanguo.model.EquipmentLevelUp;
import com.vikings.sanguo.model.EquipmentSlotInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.EquipmentStoneUpgradeTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class EquipmentDetailWindow extends CustomPopupWindow implements
		OnClickListener {
	private ViewGroup equipmentEffectLayout, stoneEffectLayout;
	private View equipmentLayout, rating;
	private TextView name, quality, properties, equipDesc;
	private Button upgradeEquipBtn, forgeBtn, replaceBtn, insertStoneBtn,
			upgradeStoneBtn, removeStoneBtn;

	private HeroInfoClient hic;
	private byte type;
	private EquipmentInfoClient eic;
	private boolean replace;

	private EquipmentInsertItem eii = null;

	@Override
	protected void init() {
		super.init("装备详情");
		setContent(R.layout.equipment_detail);
		equipmentEffectLayout = (ViewGroup) window
				.findViewById(R.id.equipmentEffectLayout);
		stoneEffectLayout = (ViewGroup) window
				.findViewById(R.id.stoneEffectLayout);
		equipmentLayout = (ViewGroup) window.findViewById(R.id.equipmentLayout);
		name = (TextView) window.findViewById(R.id.name);
		quality = (TextView) window.findViewById(R.id.quality);
		properties = (TextView) window.findViewById(R.id.properties);
		equipDesc = (TextView) window.findViewById(R.id.equipDesc);
		rating = window.findViewById(R.id.rating);
		upgradeEquipBtn = (Button) window.findViewById(R.id.upgradeEquipBtn);
		upgradeEquipBtn.setOnClickListener(this);
		forgeBtn = (Button) window.findViewById(R.id.forgeBtn);
		forgeBtn.setOnClickListener(this);
		replaceBtn = (Button) window.findViewById(R.id.replaceBtn);
		replaceBtn.setOnClickListener(this);
		insertStoneBtn = (Button) window.findViewById(R.id.insertStoneBtn);
		insertStoneBtn.setOnClickListener(this);
		upgradeStoneBtn = (Button) window.findViewById(R.id.upgradeStoneBtn);
		upgradeStoneBtn.setOnClickListener(this);
		removeStoneBtn = (Button) window.findViewById(R.id.removeStoneBtn);
		removeStoneBtn.setOnClickListener(this);
	}

	public void open(EquipmentInfoClient eic, HeroInfoClient hic,
			boolean replace) {
		this.eic = eic;
		this.hic = hic;
		this.type = eic.getProp().getType();
		this.replace = replace;
		super.doOpen();
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		if (null != hic) {
			EquipmentSlotInfoClient esic = hic.getEquipmentSlotInfoClient(type);
			eic = esic.getEic();
		}
		setButtons();
		IconUtil.setEquipmentIcon(equipmentLayout, type, eic, false);
		ViewUtil.setRichText(name, StringUtil.color(eic.getProp().getName(),
				eic.getEquipmentDesc().getColor()));
		ViewUtil.setRichText(quality, StringUtil.color("("
				+ eic.getEquipmentDesc().getName() + ")", eic
				.getEquipmentDesc().getColor()));

		EquipmentEffect ee = CacheMgr.equipmentEffectCache.getEquipmentEffect(
				eic.getEquipmentId(), (byte) eic.getQuality(), eic.getLevel());
		if (null != ee) {
			ViewUtil.setText(properties, "【属性】" + ee.getEffectTypeName() + "+"
					+ ee.getEffectValue(eic.getLevel()));
		} else {
			ViewUtil.setText(properties, "【属性】无");
		}

		ViewUtil.setRichText(equipDesc, eic.getProp().getDesc());
		ViewUtil.setImage(rating, eic.getProp().getRatingPic());

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
				eii = (EquipmentInsertItem) CacheMgr.equipmentInsertItemCache
						.get(eic.getSlotItemId());
				ViewUtil.setRichText(stoneEffectLayout, R.id.effect,
						eii.getDesc());
			} catch (GameException e) {
				Log.e("EquipmentDetailWindow", e.getMessage());
			}
		} else {
			ViewUtil.setText(stoneEffectLayout, R.id.effect, "无");
			eii = null;
		}

		// 套装效果
		IconUtil.setSuitEffect(window, hic, eic);
	}

	private void setButtons() {
		List<EquipmentLevelUp> levelUps = CacheMgr.equipmentLevelUpCache
				.getLevelUp(eic.getProp().getLevelUpScheme(),
						(byte) eic.getQuality(), (byte) eic.getLevel());
		if (!levelUps.isEmpty()) {
			ViewUtil.setVisible(upgradeEquipBtn);
		} else {
			ViewUtil.setGone(upgradeEquipBtn);
		}

		if (eic.canForge()) {
			ViewUtil.setVisible(forgeBtn);
		} else {
			ViewUtil.setGone(forgeBtn);
		}

		if (replace)
			ViewUtil.setVisible(replaceBtn);
		else
			ViewUtil.setGone(replaceBtn);

		if (eic.hasStone()) {
			if (eic.canUpgradeStrone())
				ViewUtil.setVisible(upgradeStoneBtn);
			else
				ViewUtil.setGone(upgradeStoneBtn);
			ViewUtil.setVisible(removeStoneBtn);
			ViewUtil.setGone(insertStoneBtn);
		} else {
			ViewUtil.setGone(upgradeStoneBtn);
			ViewUtil.setGone(removeStoneBtn);
			if (eic.openStoneSlot()) {
				ViewUtil.setVisible(insertStoneBtn);
			} else {
				ViewUtil.setGone(insertStoneBtn);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == upgradeEquipBtn) {
			new EquipmentLevelUpWindow().open(eic, hic);
		} else if (v == forgeBtn) {
			new EquipmentForgeWindow().open(eic, hic);
		} else if (v == replaceBtn) {
			if (null != hic)
				new EquipmentReplaceWindow().open(eic, hic);
		} else if (v == insertStoneBtn) {
			new EquipmentStoneListWindow().open(eic, hic);
		} else if (v == upgradeStoneBtn) {
			new EquipmentStoneUpgradeTip(eic, hic).show();
		} else if (v == removeStoneBtn) {
			if (eic.hasStone()) {
				String msg = "是否拆卸宝石？";
				String msgExt = "";
				if (null != eii) {
					if (eii.getCurrency() > 0) {
						msg = "是否花费 #rmb#"
								+ StringUtil.color(eii.getCurrency() + "元宝",
										R.color.color11) + " 拆卸宝石？";
					}
					if (eii.getItemId() > 0 && eii.getCount() > 0) {
						Item item = CacheMgr.getItemByID(eii.getItemId());
						if (null != item)
							msgExt = "拆卸后将获得 【" + item.getName() + "】x"
									+ eii.getCount();
					}
				}
				controller.confirm("拆卸宝石", CustomConfirmDialog.DEFAULT, msg,
						msgExt, new CallBack() {

							@Override
							public void onCall() {
								new EquipmentItemRemoveInvoker().start();
							}
						}, null);
			}
		}

	}

	private class EquipmentItemRemoveInvoker extends BaseInvoker {
		private EquipmentItemRemoveResp resp;

		@Override
		protected String loadingMsg() {
			return "拆卸宝石";
		}

		@Override
		protected String failMsg() {
			return "拆卸宝石失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().equipmentItemRemove(eic.getId(),
					eic.getHeroId());
			if (null != resp.getEic())
				eic.update(resp.getEic());
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRic(), true);
			setValue();
		}

	}
}
