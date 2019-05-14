package com.vikings.sanguo.ui.adapter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.ui.alert.ChatMsgTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class GuildChatAdapter extends ObjectAdapter implements
		OnLongClickListener {
	private RichGuildInfoClient rgic;

	public GuildChatAdapter() {
		content = new LinkedList<GuildChatData>();
	}

	public GuildChatAdapter(RichGuildInfoClient rgic) {
		content = new LinkedList<GuildChatData>();
		this.rgic = rgic;
	}

	@Override
	public int getLayoutId() {
		return R.layout.chat_content;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addItems(List ls) {
		synchronized (this) {
			if (ls.isEmpty())
				return;
			if (content.isEmpty()) {
				content.addAll(ls);
			} else {
				GuildChatData data = (GuildChatData) (((LinkedList) content)
						.getFirst());
				boolean isNewData = true;
				if (data.getTime() >= ((GuildChatData) (ls.get(ls.size() - 1)))
						.getTime()) {
					isNewData = false;
				}
				if (isNewData) {
					while (hasFakeData()) {
						content.remove(content.size() - 1);
					}
					content.addAll(ls);
				} else {
					content.addAll(0, ls);
				}
			}
		}

	}

	private boolean hasFakeData() {
		if (content.isEmpty())
			return false;
		GuildChatData data = (GuildChatData) content.get(content.size() - 1);
		return data.isFake();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.chatTo = (ViewGroup) convertView.findViewById(R.id.chatTo);
			holder.chatFrom = (ViewGroup) convertView
					.findViewById(R.id.chatFrom);
			holder.chatToFrame = convertView.findViewById(R.id.chatToFrame);
			holder.chatFromFrame = convertView.findViewById(R.id.chatFromFrame);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GuildChatData chatData = (GuildChatData) getItem(position);
		Date date = new Date(chatData.getTime() * 1000L);
		if (chatData.getUserId() == Account.user.getId()) {
			ViewUtil.setVisible(holder.chatTo);

			TextView msgContent = (TextView) holder.chatTo
					.findViewById(R.id.content);

			if (null != Account.getCurVip()) {
				if (Account.getCurVip().getLevel() >= Constants.CHAT_VIP_LVL) {
					ViewUtil.setImage(holder.chatToFrame,
							R.drawable.vip_chat_to);
					msgContent.setTextColor(Config.getController()
							.getResources().getColor(R.color.color6));
					ViewUtil.setImage(holder.chatToFrame, R.id.chat_seprator,
							R.drawable.separate_line2);

					ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
							R.color.color19);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
							R.color.color6);
				} else {
					ViewUtil.setImage(holder.chatToFrame, R.drawable.chatto_bg);
					msgContent.setTextColor(Config.getController()
							.getResources().getColor(R.color.color14));
					ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
							R.color.color21);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
							R.color.color14);
				}
			} else {
				ViewUtil.setImage(holder.chatToFrame, R.drawable.chatto_bg);
				msgContent.setTextColor(Config.getController().getResources()
						.getColor(R.color.color14));
				ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
						R.color.color21);
				ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
						R.color.color14);
			}

			ViewUtil.setGone(holder.chatFrom);

			if (Account.user.bref().isVip()) {
				IconUtil.setUserIcon(
						(ViewGroup) holder.chatTo.findViewById(R.id.iconLayout),
						Account.user.bref(), "VIP"
								+ Account.user.bref().getCurVip().getLevel());
			} else {
				IconUtil.setUserIcon(
						(ViewGroup) holder.chatTo.findViewById(R.id.iconLayout),
						Account.user.bref());
			}

			ViewUtil.setRichText(holder.chatTo, R.id.name, "我 " + "("
					+ getExtends(chatData) + ")");
			ViewUtil.setText(holder.chatTo, R.id.time,
					DateUtil.db2TimeFormat.format(date));

			msgContent.setTag(chatData);
			setMsgContent(msgContent, chatData.getMsg());
			msgContent.setOnLongClickListener(this);

			holder.chatTo.findViewById(R.id.icon).setOnClickListener(
					new ClickIconListener(Account.user.bref()));
			ViewUtil.setGone(holder.chatTo.findViewById(R.id.reSend));
			holder.chatTo.setTag(chatData);
			holder.chatTo.setOnLongClickListener(this);
		} else {
			ViewUtil.setGone(holder.chatTo);
			ViewUtil.setVisible(holder.chatFrom);

			TextView msgContent = (TextView) holder.chatFrom
					.findViewById(R.id.content);

			if (null != chatData.getUser().getCurVip()) {
				if (chatData.getUser().getCurVip().getLevel() >= Constants.CHAT_VIP_LVL) {
					ViewUtil.setImage(holder.chatFromFrame,
							R.drawable.vip_chat_from);
					ViewUtil.setTextColor(msgContent, R.color.color6);
					ViewUtil.setImage(holder.chatFromFrame, R.id.chat_seprator,
							R.drawable.separate_line2);

					ViewUtil.setTextColor(holder.chatFromFrame, R.id.name,
							R.color.color19);
					ViewUtil.setTextColor(holder.chatFromFrame, R.id.time,
							R.color.color6);
				} else {
					ViewUtil.setImage(holder.chatFromFrame,
							R.drawable.chatfrom_bg);
					ViewUtil.setTextColor(msgContent, R.color.color14);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
							R.color.color21);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
							R.color.color14);
				}
			} else {
				ViewUtil.setImage(holder.chatFromFrame, R.drawable.chatfrom_bg);
				ViewUtil.setTextColor(msgContent, R.color.color14);
				ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
						R.color.color21);
				ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
						R.color.color14);
			}
			if (chatData.getUser().isVip()) {
				IconUtil.setUserIcon((ViewGroup) holder.chatFrom
						.findViewById(R.id.iconLayout), chatData.getUser(),
						"VIP" + chatData.getUser().getCurVip().getLevel());
			} else {
				IconUtil.setUserIcon((ViewGroup) holder.chatFrom
						.findViewById(R.id.iconLayout), chatData.getUser());
			}

			ViewUtil.setRichText(holder.chatFrom, R.id.name, chatData.getUser()
					.getNickName() + " " + getExtends(chatData));
			ViewUtil.setText(holder.chatFrom, R.id.time,
					DateUtil.db2TimeFormat.format(date));

			msgContent.setTag(chatData);
			setMsgContent(msgContent, chatData.getMsg());
			msgContent.setOnLongClickListener(this);
			holder.chatFrom.findViewById(R.id.icon).setOnClickListener(
					new ClickIconListener(chatData.getUser()));
			holder.chatFrom.setTag(chatData);
			holder.chatFrom.setOnLongClickListener(this);
		}
		return convertView;
	}

	private class ClickIconListener implements OnClickListener {
		private BriefUserInfoClient user;

		public ClickIconListener(BriefUserInfoClient user) {
			this.user = user;
		}

		@Override
		public void onClick(View v) {
			Config.getController().showCastle(user);
		}

	}

	private void setMsgContent(TextView textView, String context) {
		textView.setClickable(true);
		textView.setText(getClickableSpan(context));
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private SpannableString getClickableSpan(String context) {
		SpannableString spanableInfo = new SpannableString(context);
		List<String> markings = StringUtil.regexMarking(context);
		if (!markings.isEmpty()) {
			for (String marking : markings) {
				int i = 0;
				while (context.indexOf(marking, i) != -1) {
					int index = context.indexOf(marking, i);
					int start = index;
					int end = index + marking.length();
					spanableInfo.setSpan(new Clickable(new MarkClickListener(
							marking)), start, end,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					i = end;
				}
			}
		}
		return spanableInfo;
	}

	private class MarkClickListener implements OnClickListener {
		private String str;

		public MarkClickListener(String str) {
			this.str = str;
		}

		@Override
		public void onClick(View v) {
			if (Account.user.getLevel() < UIChecker.FUNC_BATTLE
					|| !Account.manorInfoClient.isLaydown()
					|| !StringUtil.isFlagOn(Account.user.getTraining(),
							BaseStep.INDEX_STEP401))
				return;
			String[] strs = str.split(Constants.FIEF_MARKING_SEPERATER);
			if (strs.length == 2) {
				int x = Integer.valueOf(strs[0]);
				int y = Integer.valueOf(strs[1]);
				Config.getController().closeAllPopup();
				Config.getController().getFiefMap().open();
				Config.getController().getBattleMap()
						.moveToFief(TileUtil.index2TileId(x, y), true, true);
			}

		}

	}

	private class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(0xfffcff00);
			ds.setUnderlineText(true);
		}

	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		ViewGroup chatTo;
		ViewGroup chatFrom;
		View chatToFrame;
		View chatFromFrame;
	}

	@Override
	public boolean onLongClick(View v) {
		new ChatMsgTip(v).show();
		return false;
	}

	private String getExtends(GuildChatData chatData) {
		if (chatData.getType() == Constants.MSG_COUNTRY
				|| chatData.getType() == Constants.MSG_WORLD) {
			return chatData.getUser().getCountryName();
		} else if (chatData.getType() == Constants.MSG_GUILD) {
			if (rgic.isLeader(chatData.getUserId()))
				return "族长";
			else if (rgic.isElder(chatData.getUserId()))
				return "长老";
			else
				return "族员";
		} else {
			return "";
		}

	}
}