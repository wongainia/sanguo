package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.utils.ViewUtil;

public class UserChooseAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.serverName = (TextView) convertView
					.findViewById(R.id.serverName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AccountPswInfoClient info = (AccountPswInfoClient) getItem(position);
		ServerData serverData = Config.getController().getServerFileCache()
				.getByServerId(info.getSid());
		if (null != serverData)
			ViewUtil.setText(holder.serverName, serverData.getName());
		else
			ViewUtil.setText(holder.serverName, "第" + info.getSid() + "区");

		ViewUtil.setText(holder.name, info.getNick());
		ViewUtil.setText(holder.level, "LV" + info.getLevel());
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.user_choose_item;
	}

	private static class ViewHolder {
		TextView serverName, name, level;
	}

}
