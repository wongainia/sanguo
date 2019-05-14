package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.AddFriendInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class SearchResultAdapter extends ObjectAdapter {
	@Override
	public int getLayoutId() {
		return R.layout.search_list_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ViewGroup) convertView.findViewById(R.id.iconLayout);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.addFriend = (Button) convertView
					.findViewById(R.id.addFriend);
			holder.vipLevel = (TextView) convertView
					.findViewById(R.id.vipLevel);
			holder.vipLayout = (ViewGroup) convertView
					.findViewById(R.id.vipLayout);
			holder.userIconLayout = (ViewGroup) convertView
					.findViewById(R.id.userIconLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GuildUserData data = (GuildUserData) getItem(position);
		final BriefUserInfoClient u = data.getUser();
		if (u.isVip()) {
			IconUtil.setUserIcon(holder.icon, u, "VIP"
					+ u.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(holder.icon, u);
		}
		holder.icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Config.getController().showCastle(u);
			}
		});

		if (Account.user.getId() == u.getId()) {
			ViewUtil.setGone(holder.addFriend);
			ViewUtil.setVisible(holder.state);
			ViewUtil.setText(holder.state, "你自己");
		} else if (Account.friends.contains(u.getId())) {
			ViewUtil.setGone(holder.addFriend);
			ViewUtil.setVisible(holder.state);
			ViewUtil.setRichText(holder.state, StringUtil.color("好友", Config
					.getController().getResourceColorText(R.color.color20)));
		} else if (Account.isBlackList(u)) {
			ViewUtil.setGone(holder.addFriend);
			ViewUtil.setVisible(holder.state);
			ViewUtil.setRichText(holder.state,
					StringUtil.color("仇人", R.color.color24));
		} else {
			if (u.hasCountry()
					&& u.getCountry().intValue() == Account.user.getCountry()
							.intValue()) {
				ViewUtil.setVisible(holder.addFriend);
				holder.addFriend.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new AddFriendInvoker(u, new CallBack() {

							@Override
							public void onCall() {
								notifyDataSetChanged();

							}
						}).start();
					}
				});
				ViewUtil.setGone(holder.state);
			} else {
				ViewUtil.setGone(holder.addFriend);
				ViewUtil.setVisible(holder.state);
				ViewUtil.setRichText(holder.state,
						StringUtil.color("国家不同", R.color.k7_color8) + "<br/>"
								+ StringUtil.color("不能添加", R.color.k7_color8));
			}

		}

		ViewUtil.setText(holder.name, u.getNickName());
		ViewUtil.setText(holder.level, "LV" + u.getLevel());

		if (u.hasCountry() && null != data.getCountry()) {
			ViewUtil.setText(holder.country, "国家: "
					+ data.getCountry().getName());
		} else {
			ViewUtil.setText(holder.country, "国家: 无");
		}
		if (u.hasGuild() && null != data.getBgic()) {
			ViewUtil.setText(holder.guild, "家族: " + data.getBgic().getName());
		} else {
			ViewUtil.setText(holder.guild, "家族: 无");
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	static class ViewHolder {
		ViewGroup userIconLayout, vipLayout;
		ViewGroup icon;
		TextView name, level, guild, country, state, vipLevel;
		Button addFriend;
	}
}
