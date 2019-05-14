package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class DeleteChatUserAdapter extends ObjectAdapter {
	private List<Integer> userIds;

	public DeleteChatUserAdapter(List<Integer> userIds) {
		this.userIds = userIds;
	}

	@Override
	public int getLayoutId() {
		return R.layout.delete_chat_user_item;
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
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.deleteBtn = (Button) convertView
					.findViewById(R.id.deleteBtn);
			holder.cantDelete = (TextView) convertView
					.findViewById(R.id.cantDelete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BriefUserInfoClient briefUser = (BriefUserInfoClient) getItem(position);

		if (briefUser.getId().intValue() == Constants.COUNTRY_ID) {
			new ViewImgCallBack("chat_country.jpg",
					holder.iconLayout.findViewById(R.id.icon));
			ViewUtil.setText(holder.name, "国家聊天频道");
			ViewUtil.setGone(holder.state);
			ViewUtil.setGone(holder.msg);
			ViewUtil.setGone(holder.deleteBtn);
			ViewUtil.setVisible(holder.cantDelete);
			holder.deleteBtn.setOnClickListener(null);
		} else if (briefUser.getId().intValue() == Constants.WORLD_ID) {
			new ViewImgCallBack("chat_world.jpg",
					holder.iconLayout.findViewById(R.id.icon));
			ViewUtil.setText(holder.name, "世界聊天频道");
			ViewUtil.setGone(holder.state);
			ViewUtil.setGone(holder.msg);
			ViewUtil.setGone(holder.deleteBtn);
			ViewUtil.setVisible(holder.cantDelete);
			holder.deleteBtn.setOnClickListener(null);
		} else if (briefUser.getId().intValue() == Constants.GUILD_ID) {
			new ViewImgCallBack("chat_guild.jpg",
					holder.iconLayout.findViewById(R.id.icon));
			ViewUtil.setText(holder.name, "家族聊天频道");
			ViewUtil.setGone(holder.state);
			ViewUtil.setGone(holder.msg);
			ViewUtil.setGone(holder.deleteBtn);
			ViewUtil.setVisible(holder.cantDelete);
			holder.deleteBtn.setOnClickListener(null);
		} else {
			if (briefUser.getId().intValue() == Constants.GM_USER_ID) {
				ViewUtil.setImage(holder.iconLayout, R.id.icon,
						R.drawable.gm_icon);
				ViewUtil.setGone(holder.deleteBtn);
				ViewUtil.setVisible(holder.cantDelete);
				holder.deleteBtn.setOnClickListener(null);
			} else {
				if (briefUser.isVip()) {
					IconUtil.setUserIcon(holder.iconLayout, briefUser, "VIP"
							+ briefUser.getCurVip().getLevel());
				} else {
					IconUtil.setUserIcon(holder.iconLayout, briefUser);
				}
				ViewUtil.setVisible(holder.deleteBtn);
				ViewUtil.setGone(holder.cantDelete);
				holder.deleteBtn
						.setOnClickListener(new ClickListener(briefUser));
			}
			ViewUtil.setText(holder.name, briefUser.getNickName());
			MessageInfoClient mic = Account.msgInfoCache
					.getLastMessage(briefUser.getId().intValue());

			ViewUtil.setVisible(holder.msg);

			if (null == mic) {
				ViewUtil.setGone(holder.state);
				ViewUtil.setRichText(holder.msg, "");
			} else {
				if (!mic.isRead()) { // 有未读消息
					int count = Account.msgInfoCache
							.getUnReadMsgCount(briefUser.getId().intValue());
					ViewUtil.setVisible(holder.state);
					ViewUtil.setText(holder.state, count);
				} else {
					ViewUtil.setGone(holder.state);
				}
				ViewUtil.setRichText(holder.msg, mic.getContext());
			}
		}

		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private BriefUserInfoClient briefUser;

		public ClickListener(BriefUserInfoClient briefUser) {
			this.briefUser = briefUser;
		}

		@Override
		public void onClick(View v) {
			removeItem(briefUser);
			userIds.remove(briefUser.getId());
			Account.msgInfoCache.removeMsg(briefUser.getId().intValue());
			notifyDataSetChanged();
		}
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		ViewGroup iconLayout;
		TextView name, state, msg, cantDelete;
		Button deleteBtn;
	}
}
