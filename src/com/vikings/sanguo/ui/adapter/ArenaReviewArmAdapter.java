package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaReviewArmAdapter extends ObjectAdapter {
	private float scale = 2 / 3f;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconGroup = (ViewGroup) convertView
					.findViewById(R.id.iconGroup);
			holder.itemName = convertView.findViewById(R.id.itemName);
			holder.itemCount = convertView.findViewById(R.id.itemCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ArmInfoClient aic = (ArmInfoClient) getItem(position);
		TroopProp prop = aic.getProp();
		if (null != prop) {
			new ViewImgScaleCallBack(prop.getIcon(),
					holder.iconGroup.findViewById(R.id.icon),
					Constants.ITEM_ICON_WIDTH * scale * Config.SCALE_FROM_HIGH,
					Constants.ITEM_ICON_HEIGHT * scale * Config.SCALE_FROM_HIGH);
			ViewUtil.setText(holder.itemName, prop.getName());
			ViewUtil.setText(holder.itemCount, "x" + aic.getCount());
		}
		adjustView(holder.iconGroup);
		return convertView;
	}

	private void adjustView(ViewGroup childView) {
		ViewGroup iconGroup = (ViewGroup) childView
				.findViewById(R.id.iconGroup);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		iconGroup.setLayoutParams(params);
		new ViewImgScaleCallBack("hero_icon_frame.png",
				childView.findViewById(R.id.iconBg), Constants.ITEM_ICON_WIDTH
						* scale * Config.SCALE_FROM_HIGH,
				Constants.ITEM_ICON_HEIGHT * scale * Config.SCALE_FROM_HIGH);

		new ViewImgScaleCallBack("hasTroopBg.png",
				childView.findViewById(R.id.hasTroopBg),
				Constants.ITEM_ICON_WIDTH * scale * Config.SCALE_FROM_HIGH,
				Constants.ITEM_ICON_HEIGHT * scale * Config.SCALE_FROM_HIGH);

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	private class ViewHolder {
		ViewGroup iconGroup;
		View itemName, itemCount;
	}

	@Override
	public int getLayoutId() {
		return R.layout.line_2_item;
	}

}
