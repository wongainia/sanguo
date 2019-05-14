package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildInviteInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.UserTimeData;
import com.vikings.sanguo.ui.alert.GuildJoinInfoApproveTip;
import com.vikings.sanguo.ui.alert.ManageInvitedUserTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildJoinAdapter extends ObjectAdapter {
	private RichGuildInfoClient rgic;

	public GuildJoinAdapter(RichGuildInfoClient rgic) {
		this.rgic = rgic;
	}

	@Override
	public int getLayoutId() {
		return R.layout.guild_join_item;
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
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		UserTimeData data = (UserTimeData) getItem(position);
		final BriefUserInfoClient briefUser = data.getBriefUser();
		if (null != briefUser) {
			if (briefUser.isVip()) {
				IconUtil.setUserIcon(holder.icon, briefUser, "VIP"
						+ briefUser.getCurVip().getLevel());
			} else {
				IconUtil.setUserIcon(holder.icon, briefUser);
			}
			ViewUtil.setText(holder.name, briefUser.getNickName());
			ViewUtil.setRichText(holder.level, "LV" + briefUser.getLevel());
			holder.icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Config.getController().showCastle(briefUser);
				}
			});
			convertView.setOnClickListener(new ClickListener(data));

		} else {
			holder.icon.setOnClickListener(null);
			convertView.setOnClickListener(null);
		}

		if (data.getType() == UserTimeData.TYPE_GUILD_JOIN) {
			ViewUtil.setRichText(
					holder.time,
					"申请时间:"
							+ StringUtil.color(
									DateUtil.formatBefore(data.getTime()),
									R.color.color19));
		} else if (data.getType() == UserTimeData.TYPE_GUILD_INVITE) {
			ViewUtil.setRichText(
					holder.time,
					"邀请时间:"
							+ StringUtil.color(
									DateUtil.formatBefore(data.getTime()),
									R.color.color19));
		}

		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private UserTimeData data;

		public ClickListener(UserTimeData data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {
			if (data.getType() == UserTimeData.TYPE_GUILD_JOIN)
				new GuildJoinInfoApproveTip(rgic, (GuildJoinInfoClient) data)
						.show();
			else if (data.getType() == UserTimeData.TYPE_GUILD_INVITE)
				new ManageInvitedUserTip(rgic, (GuildInviteInfoClient) data)
						.show();
		}

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	static class ViewHolder {
		ViewGroup icon;
		TextView name, level, time;
	}
}
