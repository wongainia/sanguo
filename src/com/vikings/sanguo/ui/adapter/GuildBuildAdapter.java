package com.vikings.sanguo.ui.adapter;

import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.thread.GuildIconCallBack;
import com.vikings.sanguo.ui.alert.GuildInviteLogTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildBuildAdapter extends ObjectAdapter {

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public int getLayoutId() {
		return R.layout.guild_build_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.leader = (TextView) convertView.findViewById(R.id.leader);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LogInfoClient lic = (LogInfoClient) getItem(position);
		BriefGuildInfoClient gic = lic.getGuildInfo();
		new GuildIconCallBack(gic, holder.icon, Config.SCALE_FROM_HIGH
				* Constants.GUILD_ICON_WIDTH, Config.SCALE_FROM_HIGH
				* Constants.GUILD_ICON_HEIGHT);
		if (lic.getLogInfo().getStid() == Constants.GUILD_INVITE) {
			ViewUtil.setText(holder.name, gic.getName() + "家族 的邀请函");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_MAKE_OVER) {
			ViewUtil.setText(holder.name, "成为 " + gic.getName() + "家族 的族长");
			ViewUtil.setText(holder.leader, "原族长:"
					+ lic.getFromUser().getNickName());

		} else if (lic.getLogInfo().getStid() == Constants.GUILD_KICK_OUT) {
			ViewUtil.setText(holder.name, "被踢出 " + gic.getName() + "家族");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_JOIN) {
			ViewUtil.setText(holder.name, "申请加入 " + gic.getName() + "家族");
			ViewUtil.setText(holder.leader, "族长:" + Account.user.getNick());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_JOIN_AGREE) {
			ViewUtil.setText(holder.name, "加入 " + gic.getName() + "家族");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_JOIN_REFUSE) {
			ViewUtil.setText(holder.name, "被拒绝加入" + gic.getName() + "家族");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_SET_ELDER) {
			ViewUtil.setText(holder.name, "被" + gic.getName() + "家族族长提升为长老");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		} else if (lic.getLogInfo().getStid() == Constants.GUILD_REMOVE_ELDER) {
			ViewUtil.setText(holder.name, "被" + gic.getName() + "家族族长贬为平民");
			ViewUtil.setText(holder.leader, "族长:"
					+ lic.getFromUser().getNickName());
		}

		ViewUtil.setText(
				holder.time,
				"时间:"
						+ DateUtil.db2MinuteFormat.format(new Date(lic
								.getLogInfo().getTime().intValue() * 1000L)));
		convertView.setOnClickListener(new ClickListener(lic));
		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private LogInfoClient lic;

		public ClickListener(LogInfoClient lic) {
			this.lic = lic;
		}

		@Override
		public void onClick(View v) {
			if (lic.isGuildInvite())
				new GuildInviteLogTip(lic).show();
			else if (lic.isKickOut())
				Config.getController().alert(lic.getKickOutLogDesc());
			else if (lic.isMakeOver())
				Config.getController().alert(lic.getMakeOverLogDesc());
			else if (lic.isJoin())
				Config.getController().alert(lic.getJoinDesc());
			else if (lic.isJoinAgree())
				Config.getController().alert(lic.getJoinAgreeDesc());
			else if (lic.isJoinRefuse())
				Config.getController().alert(lic.getJoinRefuseDesc());
		}

	}

	private class ViewHolder {
		ImageView icon;
		TextView name, leader, time;
	}

}
