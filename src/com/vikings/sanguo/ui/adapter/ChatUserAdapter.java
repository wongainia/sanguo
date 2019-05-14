package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ChatUserAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.chat_user_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.user_outline_bg = (ImageView) convertView
					.findViewById(R.id.user_outline_bg);
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BriefUserInfoClient briefUser = (BriefUserInfoClient) getItem(position);
		ViewUtil.setVisible(holder.user_outline_bg);
		if (briefUser.getId().intValue() == Constants.COUNTRY_ID) {
			setSpecialIcon(holder.iconLayout, "title_talk_01.png");
			setName(holder, "国家聊天频道");
			ViewUtil.setGone(holder.user_outline_bg);
		} else if (briefUser.getId().intValue() == Constants.WORLD_ID) {
			setSpecialIcon(holder.iconLayout, "title_talk_02.png");
			setName(holder, "世界聊天频道");
			ViewUtil.setGone(holder.user_outline_bg);
		} else if (briefUser.getId().intValue() == Constants.GUILD_ID) {
			setSpecialIcon(holder.iconLayout, "title_talk_03.png");
			setName(holder, "家族聊天频道");
		} else {
			if (briefUser.getId().intValue() == Constants.GM_USER_ID) {
				setSpecialIcon(holder.iconLayout, "title_talk_04.jpg");
			} else {
				if (briefUser.isVip()) {
					IconUtil.setUserIcon(holder.iconLayout, briefUser, "VIP"
							+ briefUser.getCurVip().getLevel());
				} else {
					IconUtil.setUserIcon(holder.iconLayout, briefUser);
				}
			}
			ViewUtil.setText(holder.name, briefUser.getNickName());
			MessageInfoClient mic = Account.msgInfoCache
					.getLastMessage(briefUser.getId().intValue());
			if (null != mic) { // 有未读消息
				if (!mic.isRead()) {
					int count = Account.msgInfoCache
							.getUnReadMsgCount(briefUser.getId().intValue());
					ViewUtil.setVisible(holder.state);
					ViewUtil.setText(holder.state, "新消息 " + count);
				} else {
					ViewUtil.setGone(holder.state);
				}
				ViewUtil.setVisible(holder.msg);
				ViewUtil.setRichText(holder.msg, mic.getContext());

				ViewUtil.setVisible(holder.time);

				long time = mic.getTime() * 1000L;
				if (DateUtil.isToday(time)) {
					ViewUtil.setText(holder.time,
							DateUtil.formatDate(time, DateUtil.time2));
				} else if (DateUtil.isYesterday(time)) {
					ViewUtil.setText(holder.time, "昨天");
				} else {
					ViewUtil.setText(holder.time,
							DateUtil.formatDate(time, DateUtil.sdateFormat2));
				}

			} else {
				ViewUtil.setGone(holder.msg);
				ViewUtil.setGone(holder.state);
				ViewUtil.setGone(holder.time);
			}
		}

		convertView.setOnClickListener(new ClickListener(briefUser));
		return convertView;
	}

	private void setName(ViewHolder holder, String name) {
		ViewUtil.setGone(holder.time);
		ViewUtil.setGone(holder.state);
		ViewUtil.setGone(holder.msg);
		ViewUtil.setText(holder.name, name);
	}

	private void setSpecialIcon(ViewGroup vg, String name) {
		ImageView icon = (ImageView) vg.findViewById(R.id.icon);
		LayoutParams params = (LayoutParams) icon.getLayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		icon.setLayoutParams(params);
		new ViewImgCallBack(name, icon);
		// ViewUtil.setImage(icon, resId);
		ViewUtil.setGone(vg, R.id.numLayout);
		ViewUtil.setGone(vg, R.id.rankTopLayout);
	}

	private class ClickListener implements OnClickListener {
		private BriefUserInfoClient briefUser;

		public ClickListener(BriefUserInfoClient briefUser) {
			this.briefUser = briefUser;
		}

		@Override
		public void onClick(View v) {
			if (briefUser.getId().intValue() == Constants.COUNTRY_ID) {
				Config.getController().openGroupChatWindow(
						Constants.MSG_COUNTRY);
			} else if (briefUser.getId().intValue() == Constants.WORLD_ID) {
				Config.getController().openGroupChatWindow(Constants.MSG_WORLD);
			} else if (briefUser.getId().intValue() == Constants.GUILD_ID) {
				if (Account.user.hasGuild()) {
					if (null != Account.guildCache.getRichInfoInCache()) {
						Config.getController().openGuildChatWindow(
								Account.guildCache.getRichInfoInCache());
					} else {
						new FetchGuildDateInvoker().start();
					}
				} else {
					Config.getController().alert("请先加入一个家族");
				}

			} else {
				Config.getController().openChatWindow(briefUser);
			}
		}
	}

	private class FetchGuildDateInvoker extends BaseInvoker {
		@Override
		protected String loadingMsg() {
			return "获取家族信息";
		}

		@Override
		protected String failMsg() {
			return "获取家族信息失败";
		}

		@Override
		protected void fire() throws GameException {
			RichGuildInfoClient richGuildInfo = Account.guildCache
					.getRichGuildInfoClient();
			if (null != richGuildInfo)
				Account.myGuildLeader = CacheMgr.userCache.get(richGuildInfo
						.getGic().getLeader());
		}

		@Override
		protected void onOK() {
			Config.getController().openGuildChatWindow(
					Account.guildCache.getRichInfoInCache());
		}

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		ViewGroup iconLayout;
		TextView name, state, time, msg;
		ImageView user_outline_bg;
	}
}
