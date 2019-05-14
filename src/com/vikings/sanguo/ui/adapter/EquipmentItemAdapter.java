/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-4-10 下午2:55:07
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EquipmentInfoClient;
import com.vikings.sanguo.model.PropEquipment;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.EquipmentItemTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class EquipmentItemAdapter extends ObjectAdapter implements
		OnClickListener {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.itemIcon = convertView.findViewById(R.id.itemIcon);
			holder.itemName = convertView.findViewById(R.id.itemName);
			holder.itemCount = convertView.findViewById(R.id.itemCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		EquipmentInfoClient eqic = (EquipmentInfoClient) getItem(position);
		if (null == eqic.getProp()) {
			ViewUtil.setText(holder.itemName, "");
			ViewUtil.setGone(holder.itemIcon);
			ViewUtil.setGone(holder.itemCount);
			convertView.setOnClickListener(null);
		} else {
			ViewUtil.setText(holder.itemName,
					StringUtil.getNCharStr(eqic.getProp().getName(), 4));
			ViewUtil.setVisible(holder.itemIcon);
			ViewUtil.setVisible(holder.itemCount);
			new ViewImgScaleCallBack(eqic.getProp().getIcon(), holder.itemIcon,
					Constants.SHOP_ITEM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
					Constants.SHOP_ITEM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(holder.itemCount, "X1");
			holder.itemName.setTag(eqic);
			convertView.setOnClickListener(this);
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public void onClick(View v) {
		EquipmentInfoClient eqic = (EquipmentInfoClient) v.findViewById(
				R.id.itemName).getTag();
		if (null != eqic) {
			PropEquipment propEquipment = eqic.getProp();
			if (null != propEquipment) {
				new EquipmentItemTip(eqic).show();
			}
		}
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
			addItem(new EquipmentInfoClient());
		}
	}

	public boolean isEmpty() {
		for (Object o : content) {
			EquipmentInfoClient eqic = (EquipmentInfoClient) o;
			if (null != eqic.getProp())
				return false;
		}
		return true;
	}

	@Override
	public int getLayoutId() {
		return R.layout.store_grid;
	}

	static class ViewHolder {
		View itemName, itemIcon, itemCount;
	}

}
