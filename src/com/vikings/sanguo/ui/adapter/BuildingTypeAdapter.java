package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingType;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class BuildingTypeAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.building_type_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.typeIcon = (ImageView) convertView
					.findViewById(R.id.typeIcon);
			holder.typeName = (ImageView) convertView
					.findViewById(R.id.typeName);
			holder.typeDesc = (TextView) convertView
					.findViewById(R.id.typeDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final BuildingType type = (BuildingType) getItem(position);
		new ViewImgCallBack(type.getNameImg(), holder.typeName);
		ViewUtil.setText(holder.typeDesc, type.getDesc());
		new ViewImgCallBack(type.getImage(), holder.typeIcon);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Config.getController().openBuildingListWindow(type);
			}
		});

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	public void refreshAdapter() {
		this.notifyDataSetChanged();
	}

	static class ViewHolder {
		ImageView typeIcon, typeName;
		TextView typeDesc;
	}
}