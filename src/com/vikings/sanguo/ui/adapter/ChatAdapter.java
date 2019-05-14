package com.vikings.sanguo.ui.adapter;

import java.util.Date;
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
import android.widget.ListView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.SendChatMsgInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.ui.alert.ChatMsgTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.guide.UIChecker;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ChatAdapter extends ObjectAdapter implements OnLongClickListener {
	private BriefUserInfoClient briefUser;
	private ListView listView;

	public ChatAdapter(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.chat_content;
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

		final MessageInfoClient mic = (MessageInfoClient) getItem(position);
		if (mic.getFrom() == Account.user.getId()) {
			ViewUtil.setVisible(holder.chatTo);

			TextView msgContent = (TextView) holder.chatTo
					.findViewById(R.id.content);

			if (null != Account.getCurVip()) {
				if (Account.getCurVip().getLevel() >= CacheMgr.dictCache
						.getDictInt(Dict.TYPE_BATTLE_COST, 33)) {
					ViewUtil.setImage(holder.chatToFrame,
							R.drawable.vip_chat_to);

					ViewUtil.setImage(holder.chatToFrame, R.id.chat_seprator,
							R.drawable.separate_line3_to);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.name,
							R.color.color19);
					ViewUtil.setTextColor(holder.chatToFrame, R.id.time,
							R.color.color6);
					ViewUtil.setTextColor(msgContent, R.color.color6);

				} else {
					ViewUtil.setImage(holder.chatToFrame, R.drawable.chatto_bg);
					ViewUtil.setTextColor(msgContent, R.color.color14);
				}
			} else {
				ViewUtil.setImage(holder.chatToFrame, R.drawable.chatto_bg);
				ViewUtil.setTextColor(msgContent, R.color.color14);
			}

			ViewUtil.setGone(holder.chatFrom);
			holder.chatTo.setTag(mic);
			// View icon = holder.chatTo.findViewById(R.id.icon);
			ViewGroup icon = (ViewGroup) holder.chatTo
					.findViewById(R.id.iconLayout);
			icon.setOnClickListener(null);

			if (Account.user.bref().isVip()) {
				IconUtil.setUserIcon(icon, Account.user.bref(), "VIP"
						+ Account.user.bref().getCurVip().getLevel());
			} else {
				IconUtil.setUserIcon(icon, Account.user.bref());
			}

			ViewUtil.setText(holder.chatTo, R.id.name, "我");
			ViewUtil.setText(holder.chatTo, R.id.time,
					DateUtil.db2MinuteFormat2.format(new Date(
							mic.getTime() * 1000L)));

			msgContent.setTag(mic);
			setMsgContent(msgContent, mic.getContext());
			msgContent.setOnLongClickListener(this);

			if (null == mic.getSendState()) {
				ViewUtil.setGone(holder.chatTo.findViewById(R.id.reSend));
			} else {
				TextView reSendText = (TextView) holder.chatTo
						.findViewById(R.id.reSend);
				ViewUtil.setRichText(reSendText, "<u>" + mic.getSendState()
						+ "</u>");
				ViewUtil.setVisible(reSendText);
				reSendText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MessageInfoClient msgInfo = new MessageInfoClient(mic
								.getFrom(), mic.getTo(), mic.getContext());
						// 发送
						Account.msgInfoCache.addMsg(msgInfo);
						addItem(msgInfo);
						notifyDataSetChanged();
						new SendChatMsgInvoker(msgInfo, briefUser,
								ChatAdapter.this, listView).start();
					}
				});
			}
			holder.chatTo.setOnLongClickListener(this);
		} else { // 收到其他玩家消息
			ViewUtil.setGone(holder.chatTo);
			ViewUtil.setVisible(holder.chatFrom);

			TextView msgContent = (TextView) holder.chatFrom
					.findViewById(R.id.content);

			if (null != briefUser.getCurVip()) {
				if (briefUser.getCurVip().getLevel() >= CacheMgr.dictCache
						.getDictInt(Dict.TYPE_BATTLE_COST, 33)) {
					ViewUtil.setImage(holder.chatFromFrame,
							R.drawable.vip_chat_from);

					ViewUtil.setImage(holder.chatFromFrame, R.id.chat_seprator,
							R.drawable.separate_line3_from);

					ViewUtil.setTextColor(holder.chatFromFrame, R.id.name,
							R.color.color19);
					ViewUtil.setTextColor(holder.chatFromFrame, R.id.time,
							R.color.color6);
					ViewUtil.setTextColor(msgContent, R.color.color6);
				} else {
					ViewUtil.setImage(holder.chatFromFrame,
							R.drawable.chatfrom_bg);
					ViewUtil.setTextColor(msgContent, R.color.color14);
				}
			} else {
				ViewUtil.setImage(holder.chatFromFrame, R.drawable.chatfrom_bg);
				ViewUtil.setTextColor(msgContent, R.color.color14);
			}

			holder.chatFrom.setTag(mic);
			// View icon = holder.chatFrom.findViewById(R.id.icon);
			ViewGroup icon = (ViewGroup) holder.chatFrom
					.findViewById(R.id.iconLayout);
			icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Config.getController().showCastle(briefUser);
				}
			});
			if (briefUser.isVip()) {
				IconUtil.setUserIcon(icon, briefUser, "VIP"
						+ briefUser.getCurVip().getLevel());
			} else {
				IconUtil.setUserIcon(icon, briefUser);
			}
			ViewUtil.setText(holder.chatFrom, R.id.name,
					briefUser.getNickName());
			ViewUtil.setText(holder.chatFrom, R.id.time,
					DateUtil.db2MinuteFormat2.format(new Date(
							mic.getTime() * 1000L)));

			msgContent.setTag(mic);
			setMsgContent(msgContent, mic.getContext());
			msgContent.setOnLongClickListener(this);
			holder.chatFrom.setOnLongClickListener(this);
		}
		return convertView;
	}

	private void setMsgContent(TextView textView, String context) {
		textView.setClickable(true);
		ViewUtil.setRichText(textView,
				String.valueOf(getClickableSpan(context)));
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

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	@Override
	public boolean onLongClick(View v) {
		new ChatMsgTip(v).show();
		return false;
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

	private class ViewHolder {
		ViewGroup chatTo;
		ViewGroup chatFrom;
		View chatToFrame;
		View chatFromFrame;
	}
}
