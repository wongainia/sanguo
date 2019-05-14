package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.alert.FavorFiefSearchTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class SearchFiefUserAdapter extends ObjectAdapter {
	private CallBack cb;

	public SearchFiefUserAdapter(CallBack cb) {
		this.cb = cb;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			// holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.icon = (ViewGroup) convertView.findViewById(R.id.iconLayout);

			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GuildUserData data = (GuildUserData) getItem(position);
		final BriefUserInfoClient briefUser = data.getUser();
		if (briefUser.isVip()) {
			IconUtil.setUserIcon(holder.icon, briefUser, "VIP"
					+ briefUser.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(holder.icon, briefUser);
		}

		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != cb)
					cb.onCall();
				Config.getController().showCastle(briefUser);
			}
		});

		ViewUtil.setText(holder.name, briefUser.getNickName());
		ViewUtil.setText(holder.level, "Lv:" + briefUser.getLevel());
		if (briefUser.hasGuild() && null != data.getBgic()) {
			ViewUtil.setText(holder.guild, "家族:" + data.getBgic().getName());
		} else {
			ViewUtil.setText(holder.guild, "家族:无");
		}

		if (briefUser.hasCountry()
				&& !StringUtil.isNull(briefUser.getCountryName())) {
			ViewUtil.setText(holder.country, "国家:" + briefUser.getCountryName());
		} else {
			ViewUtil.setText(holder.country, "国家:无");
		}

		convertView.setOnClickListener(new ClickListener(data));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.search_fief_user_item;
	}

	static class ViewHolder {
		// ImageView icon;
		ViewGroup icon;
		TextView name, level, guild, country;
	}

	private class ClickListener implements OnClickListener {

		private GuildUserData data;

		public ClickListener(GuildUserData data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {
			if (v instanceof ViewGroup) {
				if (null != cb)
					cb.onCall();
				new FavorFiefSearchTip(Constants.USER, data.getUser()).show();
			}
		}
	}
}
