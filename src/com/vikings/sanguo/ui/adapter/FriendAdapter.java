package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildUserData;
import com.vikings.sanguo.ui.alert.FriendTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class FriendAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.friend_list_item;
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
			holder.chat = (Button) convertView.findViewById(R.id.chatBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GuildUserData data = (GuildUserData) getItem(position);
		BriefUserInfoClient briefUser = data.getUser();
		if(briefUser.isVip()){
			IconUtil.setUserIcon(holder.icon, briefUser,"VIP" +briefUser.getCurVip().getLevel());
		}else{
			IconUtil.setUserIcon(holder.icon, briefUser);
		}
		
		
		ViewUtil.setText(holder.name, briefUser.getNickName());
		ViewUtil.setText(holder.level, "Lv" + briefUser.getLevel());
		if (null != data.getBgic()) {
			ViewUtil.setText(holder.guild, "家族: " + data.getBgic().getName());
		} else {
			ViewUtil.setText(holder.guild, "家族: 无");
		}
		if (null != data.getCountry()) {
			ViewUtil.setText(holder.country, "国家: "
					+ data.getCountry().getName());
		} else {
			ViewUtil.setText(holder.country, "国家: 无");
		}

		convertView.setOnClickListener(new ClickListener(data));
		holder.chat.setOnClickListener(new ChatClickListener(briefUser));
		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private GuildUserData data;

		public ClickListener(GuildUserData data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {
			new FriendTip(data).show();
		}
	}

	private class ChatClickListener implements OnClickListener {
		private BriefUserInfoClient briefUser;

		public ChatClickListener(BriefUserInfoClient briefUser) {
			this.briefUser = briefUser;
		}

		@Override
		public void onClick(View v) {
			Config.getController().openChatWindow(briefUser);
		}

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		ViewGroup icon;
		TextView name, level, guild, country;
		Button chat;
	}
}
