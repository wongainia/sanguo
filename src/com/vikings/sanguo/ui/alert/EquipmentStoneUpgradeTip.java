package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentInsertItemLevelUpResp;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItem;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EquipmentStoneUpgradeTip extends CustomConfirmDialog {
	private static final int layout = R.layout.alert_equipment_stone_upgrade;

	private View curLayout, nextLayout;
	private TextView nextEffect, cost;
	private Button upgradeBtn;

	private EquipmentInfoClient eic;
	private HeroInfoClient hic;

	public EquipmentStoneUpgradeTip(EquipmentInfoClient eic, HeroInfoClient hic) {
		super("宝石升级");
		this.eic = eic;
		this.hic = hic;
	}

	public void show() {
		super.show();
		curLayout = content.findViewById(R.id.curLayout);
		nextLayout = content.findViewById(R.id.nextLayout);
		cost = (TextView) content.findViewById(R.id.cost);
		nextEffect = (TextView) content.findViewById(R.id.nextEffect);
		upgradeBtn = (Button) content.findViewById(R.id.upgradeBtn);
		upgradeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new EquipmentStoneUpgradeInvoker(eic, hic).start();
			}
		});
		setValue();
	}

	private void setValue() {
		Item item = eic.getStone();
		ViewUtil.setGone(cost);
		ViewUtil.setGone(nextEffect);
		if (null != item) {
			setStoneInfo(curLayout, item);
			try {
				EquipmentInsertItem curEii = (EquipmentInsertItem) CacheMgr.equipmentInsertItemCache
						.get(item.getId());
				if (null != curEii && curEii.getUpgradeItemId() > 0
						&& curEii.getUpgradeItemCount() > 0) {
					Item costItem = CacheMgr.getItemByID(curEii
							.getUpgradeItemId());
					ItemBag itemBag = Account.store.getItemBag(curEii
							.getUpgradeItemId());
					int selfCount = (itemBag == null ? 0 : itemBag.getCount());
					if (selfCount < 0)
						selfCount = 0;
					if (null != costItem) {
						ViewUtil.setVisible(cost);
						ViewUtil.setRichText(
								cost,
								"升级消耗：#"
										+ costItem.getImage()
										+ "#"
										+ costItem.getName()
										+ " x"
										+ curEii.getUpgradeItemCount()
										+ "("
										+ StringUtil.color(
												"" + selfCount,
												(curEii.getUpgradeItemCount() > selfCount ? R.color.color24
														: R.color.color19))
										+ ")");
					}
				}
			} catch (GameException e) {
				Log.e("EquipmentStoneUpgradeTip", e.getErrorMsg());
			}
			EquipmentInsertItem nextEii = CacheMgr.equipmentInsertItemCache
					.getNextLevel(item.getId());
			if (null != nextEii) {
				Item nextItem = CacheMgr.getItemByID(nextEii.getId());
				if (null != nextItem)
					setStoneInfo(nextLayout, nextItem);
				ViewUtil.setVisible(nextEffect);
				ViewUtil.setText(
						nextEffect,
						"下级效果："
								+ CacheMgr.equipmentInsertItemEffectCache
										.getEffectDesc(nextEii.getId()));
			}
		}
	}

	private void setStoneInfo(View view, Item item) {
		new ViewImgScaleCallBack(item.getImage(), view.findViewById(R.id.icon),
				Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
		ViewUtil.setText(view.findViewById(R.id.name), item.getName());
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	private class EquipmentStoneUpgradeInvoker extends BaseInvoker {
		private EquipmentInfoClient eic;
		private HeroInfoClient hic;
		private long heroId;
		private EquipmentInsertItemLevelUpResp resp;

		public EquipmentStoneUpgradeInvoker(EquipmentInfoClient eic,
				HeroInfoClient hic) {
			this.eic = eic;
			this.hic = hic;
			if (null != hic)
				heroId = hic.getId();
		}

		@Override
		protected String loadingMsg() {
			return "升级宝石";
		}

		@Override
		protected String failMsg() {
			return "升级宝石失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().equipmentInsertItemLevelUp(
					eic.getId(), heroId);
			if (null != resp.getEic()) {
				eic.update(resp.getEic());
			} else if (null != resp.getHic()) {
				if (null != resp.getHic().getEquipmentInfoClient(eic.getId())) {
					eic.update(resp.getHic()
							.getEquipmentInfoClient(eic.getId()));
				}
			}

		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRic(), true);
			if (!eic.canUpgradeStrone())
				dismiss();
			else
				setValue();
			ctr.alert("升级宝石成功");
		}

	}
}
