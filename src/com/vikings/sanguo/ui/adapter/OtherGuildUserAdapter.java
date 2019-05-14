package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.OtherRichGuildInfoClient;
import com.vikings.sanguo.ui.alert.GuildUserTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class OtherGuildUserAdapter extends ObjectAdapter {
	private OtherRichGuildInfoClient orgic;

	public OtherGuildUserAdapter(OtherRichGuildInfoClient orgic) {
		this.orgic = orgic;
	}

	@Override
	public int getLayoutId() {
		return R.layout.guild_user_item;
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
			holder.position = (TextView) convertView
					.findViewById(R.id.position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BriefUserInfoClient briefUser = (BriefUserInfoClient) getItem(position);

		if (briefUser.isVip()) {
			IconUtil.setUserIcon(holder.icon, briefUser, "VIP"
					+ briefUser.getCurVip().getLevel());
		} else {
			IconUtil.setUserIcon(holder.icon, briefUser);
		}
		ViewUtil.setText(holder.name, briefUser.getNickName());
		ViewUtil.setRichText(holder.level, briefUser.getLevel() + "级");
		if (orgic.getOgic().getLeader() == briefUser.getId()) {
			ViewUtil.setText(holder.position, "职务:族长");
		} else {
			ViewUtil.setText(holder.position, "职务:成员");
		}

		convertView.setOnClickListener(new ClickListener(briefUser));
		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private BriefUserInfoClient briefUser;

		public ClickListener(BriefUserInfoClient briefUser) {
			this.briefUser = briefUser;
		}

		@Override
		public void onClick(View v) {
			new GuildUserTip(briefUser, orgic).show();
		}

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	static class ViewHolder {
		// ImageView icon;
		ViewGroup icon;
		TextView name, level, position;
	}
}
