package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.MachinePlayStatInfoClient;
import com.vikings.sanguo.utils.ViewUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LuckyAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.lucky_list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.info = (TextView) convertView.findViewById(R.id.info);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		MachinePlayStatInfoClient info = (MachinePlayStatInfoClient) getItem(index);
		ViewHolder holder = (ViewHolder) v.getTag();
		ViewUtil.setRichText(holder.info, info.getLuckyDesc());
	}

	static class ViewHolder {
		TextView info;
	}
}
