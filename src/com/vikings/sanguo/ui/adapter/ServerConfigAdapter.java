package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.ServerUserData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.ChooseUserServerTip;
import com.vikings.sanguo.utils.ViewUtil;

public class ServerConfigAdapter extends ObjectAdapter {

	private CallBack callBack;

	public ServerConfigAdapter(CallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public int getLayoutId() {
		return R.layout.server_config_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.layout = (ViewGroup) convertView.findViewById(R.id.layout);
			holder.userLayout = (ViewGroup) convertView
					.findViewById(R.id.userLayout);
			holder.hasUser = (ImageView) convertView.findViewById(R.id.hasUser);
			holder.stateIcon = (ImageView) convertView
					.findViewById(R.id.stateIcon);
			holder.serverName = (TextView) convertView
					.findViewById(R.id.serverName);
			holder.userCount = (TextView) convertView
					.findViewById(R.id.userCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ServerUserData data = (ServerUserData) getItem(position);
		ServerData serverData = data.getServerData();

		ViewUtil.setImage(holder.stateIcon, serverData.getStateImgId());
		ViewUtil.setText(holder.serverName, serverData.getName());
		int count = data.getUserSize();
		if (count > 0) {
			ViewUtil.setVisible(holder.hasUser);
			ViewUtil.setVisible(holder.userCount);
			ViewUtil.setText(holder.userCount, "(" + "角色数:" + count + ")");
			int viewCount = holder.userLayout.getChildCount();
			for (int i = viewCount; i < count; i++) {
				View view = Config.getController().inflate(
						R.layout.choose_user_item, holder.userLayout, false);
				holder.userLayout.addView(view);
			}
			viewCount = holder.userLayout.getChildCount();
			for (int i = 0; i < viewCount; i++) {
				View view = holder.userLayout.getChildAt(i);
				if (count > i) {
					ViewUtil.setVisible(view);
					AccountPswInfoClient client = data.getInfos().get(i);
					ViewUtil.setText(view, R.id.userName, client.getNick());
					ViewUtil.setText(view, R.id.userLevel,
							"LV" + client.getLevel());
					view.setOnClickListener(new ChooseUserListener(data
							.getServerData(), client));
				} else {
					ViewUtil.setGone(view);
				}
			}

		} else {
			ViewUtil.setHide(holder.hasUser);
			ViewUtil.setHide(holder.userCount);
		}

		if (Config.serverId == serverData.getServerId()) {
			ViewUtil.setImage(holder.layout, R.drawable.bg_server_cur);
		} else {
			ViewUtil.setImage(holder.layout, R.drawable.bg_server);
		}

		if (serverData.isRepair())
			ViewUtil.setImage(holder.layout, R.drawable.bg_server_wh);

		convertView.setOnClickListener(new ClickListener(data));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	private class ClickListener implements OnClickListener {
		private ServerUserData data;

		public ClickListener(ServerUserData data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {
			if (data.getUserSize() > 1) {
				new ChooseUserServerTip().show(data.getServerData(),
						data.getInfos(), callBack);
			} else if (data.getUserSize() == 1) {
				Config.setServer(data.getServerData(), data.getInfos().get(0));
				if (null != callBack)
					callBack.onCall();
			} else {
				Config.setServer(data.getServerData(), null);
				if (null != callBack)
					callBack.onCall();
			}
		}
	}

	private class ChooseUserListener implements OnClickListener {
		private ServerData data;
		private AccountPswInfoClient client;

		public ChooseUserListener(ServerData data, AccountPswInfoClient client) {
			this.data = data;
			this.client = client;
		}

		@Override
		public void onClick(View v) {
			Config.setServer(data, client);
			if (null != callBack)
				callBack.onCall();
		}

	}

	private static class ViewHolder {
		ViewGroup layout, userLayout;
		ImageView hasUser, stateIcon;
		TextView userCount, serverName;
	}
}