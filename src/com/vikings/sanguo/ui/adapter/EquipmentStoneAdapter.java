package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.EquipmentItemInsertResp;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.EquipmentInsertItem;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class EquipmentStoneAdapter extends ObjectAdapter {

	private EquipmentInfoClient eic;

	public EquipmentStoneAdapter(EquipmentInfoClient eic) {
		this.eic = eic;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ItemBag itemBag = (ItemBag) getItem(position);
		Item item = itemBag.getItem();
		EquipmentInsertItem eii = null;
		try {
			eii = (EquipmentInsertItem) CacheMgr.equipmentInsertItemCache
					.get(item.getId());
		} catch (GameException e) {

		}
		if (null != item && null != eii) {
			new ViewImgScaleCallBack(item.getImage(), holder.icon,
					Constants.ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(holder.name, itemBag.getItem().getName() + " LV"
					+ eii.getLevel());
			ViewUtil.setText(
					holder.desc,
					"效果："
							+ CacheMgr.equipmentInsertItemEffectCache
									.getEffectDesc(eii.getId()));
			if (null != eic.getEquipmentType()
					&& eic.getEquipmentType().getMaxGemLevel() < eii.getLevel()) {
				ViewUtil.setVisible(holder.state);
				ViewUtil.setText(holder.state, "装备品质过低");
				ViewUtil.disableButton(convertView);
			} else {
				ViewUtil.setGone(holder.state);
				ViewUtil.enableButton(convertView);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new EquipmentItemInsertInvoker(itemBag).start();
					}
				});
			}
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.equipment_stone_item;
	}

	private class EquipmentItemInsertInvoker extends BaseInvoker {
		private ItemBag itemBag;
		private EquipmentItemInsertResp resp;

		public EquipmentItemInsertInvoker(ItemBag itemBag) {
			this.itemBag = itemBag;
		}

		@Override
		protected String loadingMsg() {
			return "镶嵌宝石";
		}

		@Override
		protected String failMsg() {
			return "镶嵌宝石失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().equipmentItemInsert(eic.getId(),
					eic.getHeroId(), itemBag.getItemId());
		}

		@Override
		protected void onOK() {
			ctr.updateUI(resp.getRic(), true);
			if (null != resp.getEic())
				eic.update(resp.getEic());
			ctr.goBack();
			ctr.alert("镶嵌宝石成功");

		}

	}

	private static class ViewHolder {
		ImageView icon;
		TextView name, state, desc;
	}
}
