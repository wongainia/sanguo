package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.GuildLogInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildLogAdapter extends ObjectAdapter {

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ViewGroup) convertView.findViewById(R.id.iconLayout);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GuildLogInfoClient glic = (GuildLogInfoClient) getItem(position);

		if (glic.getUser().isVip()) {
			IconUtil.setUserIcon(holder.icon, glic.getUser(), "VIP"
					+ glic.getUser().getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(holder.icon, glic.getUser());
		}

		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Config.getController().showCastle(glic.getUser());

			}
		});
		ViewUtil.setRichText(holder.desc, glic.toDesc());
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.guild_log_line;
	}

	private static class ViewHolder {
		// ImageView icon;
		ViewGroup icon;
		TextView desc;
	}
}
