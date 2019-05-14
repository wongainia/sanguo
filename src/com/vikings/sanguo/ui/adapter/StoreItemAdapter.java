package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.PlayerWantedTip;
import com.vikings.sanguo.ui.alert.StoreItemTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class StoreItemAdapter extends ObjectAdapter implements OnClickListener {

	@Override
	public int getLayoutId() {
		return R.layout.store_grid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.itemName = convertView.findViewById(R.id.itemName);
			holder.itemIcon = convertView.findViewById(R.id.itemIcon);
			holder.itemCount = convertView.findViewById(R.id.itemCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemBag bag = (ItemBag) getItem(position);
		if (bag.getItem() == null) {
			ViewUtil.setText(holder.itemName, "");
			ViewUtil.setGone(holder.itemIcon);
			ViewUtil.setGone(holder.itemCount);
			convertView.setOnClickListener(null);
		} else {
			ViewUtil.setText(holder.itemName,
					StringUtil.getNCharStr(bag.getItem().getName(), 4));
			ViewUtil.setVisible(holder.itemIcon);
			ViewUtil.setVisible(holder.itemCount);
			new ViewImgScaleCallBack(bag.getItem().getImage(), holder.itemIcon,
					Constants.SHOP_ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.SHOP_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(holder.itemCount, "X" + bag.getCount());
			holder.itemName.setTag(bag);
			convertView.setOnClickListener(this);
		}
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public void fillEmpty() {
		if (content.size() == 0)
			return;
		int count = 0;
		int tmp = content.size() % 3;
		if (tmp != 0) {
			count = 3 - tmp;
		}

		for (int i = 0; i < count; i++) {
			addItem(new ItemBag());
		}
	}

	public boolean isEmpty() {
		for (Object o : content) {
			ItemBag bag = (ItemBag) o;
			if (bag.getItem() != null)
				return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		ItemBag bag = (ItemBag) v.findViewById(R.id.itemName).getTag();
		bag.setNew(false);
		if (bag != null && bag.getItem() != null) {
			if (bag.getItemId() == Item.ITEM_ID_PLAYER_WANTED)
				new PlayerWantedTip(bag).show();
			else
				new StoreItemTip(bag).show();
		}
	}

	static class ViewHolder {
		View itemName, itemIcon, itemCount;
	}

}
