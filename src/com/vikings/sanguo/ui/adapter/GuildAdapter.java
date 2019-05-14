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
import com.vikings.sanguo.model.GuildProp;
import com.vikings.sanguo.model.GuildSearchInfoClient;
import com.vikings.sanguo.thread.GuildIconCallBack;
import com.vikings.sanguo.ui.alert.GuildJoinConfirmTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.guild_list_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.memberCount = (TextView) convertView
					.findViewById(R.id.memberCount);
			holder.leader = (TextView) convertView.findViewById(R.id.leader);
			holder.joinBtn = (Button) convertView.findViewById(R.id.applyBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GuildSearchInfoClient data = (GuildSearchInfoClient) getItem(position);
		GuildProp prop = data.getGuildProp();
		new GuildIconCallBack(data.brief(),
				holder.iconLayout.findViewById(R.id.icon),
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.GUILD_ICON_HEIGHT);
		ViewUtil.setText(holder.name, data.getInfo().getName());
		ViewUtil.setText(holder.memberCount,
				"族员:" + data.getInfo().getMembers()
						+ (null != prop ? "/" + prop.getMaxMemberCnt() : ""));
		ViewUtil.setText(holder.leader, "族长:"
				+ data.getBriefUser().getNickName());
		holder.iconLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new GuildInfoWindow().open(data.getInfo().getId());
			}
		});
		holder.joinBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtil.isFlagOn(Account.user.getTraining(),
						BaseStep.INDEX_FIRST_JOIN_GUILD))
					new GuildJoinAskInvoker(data).start();
				else
					new GuildJoinConfirmTip(data).show();
			}
		});
		if (Account.user.hasGuild()) {
			ViewUtil.disableButton(holder.joinBtn);
		} else {
			ViewUtil.enableButton(holder.joinBtn);
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	static class ViewHolder {
		ViewGroup iconLayout;
		TextView name, memberCount, leader;
		Button joinBtn;
	}
}
