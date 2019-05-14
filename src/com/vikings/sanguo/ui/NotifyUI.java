package com.vikings.sanguo.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.GuildIconCallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.utils.ViewUtil;

/**
 * 系统通知ui
 * 
 * @author Brad.Chen
 * 
 */
public class NotifyUI extends BaseUI implements OnClickListener {

	private ImageView noticIconBtn;

	private View notifyNotice;

	private boolean running = false;

	private Runnable blink = new Blink();

	private int wait_time = 1000;

	// 保存当前闪动的聊天对象信息，用于打开chatWindow用
	private BriefUserInfoClient user;

	private GuildChatData chatData;

	@Override
	protected void bindField() {
		noticIconBtn = (ImageView) controller.findViewById(R.id.noticIconBtn);
		notifyNotice = controller.findViewById(R.id.notifyNotice);
		notifyNotice.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == notifyNotice) {
			// 如果有聊天对象头像闪动，进入chatWIndow后，直接和此人聊天
			// 否则chatWindow显示上次聊天时，最后一个聊天对象的记录
			if (null != chatData) {
				if (null != Account.guildCache.getRichInfoInCache())
					controller.openGuildChatWindow(Account.guildCache
							.getRichInfoInCache());
				chatData = null;
			} else if (null != user) {
				controller.openChatWindow(user);
				user = null;
			} else
				controller.openChatUserListWindow();

			if (running) {
				this.stopNotify();
			}
		}
	}

	public void chatWithNobody() {
		ViewUtil.setGone(noticIconBtn);
		ViewUtil.setGone(notifyNotice.findViewById(R.id.cnt));
	}

	public void startNotify(BriefUserInfoClient user) {
		this.user = user;

		new UserIconCallBack(user, noticIconBtn, 45 * Config.SCALE_FROM_HIGH,
				45 * Config.SCALE_FROM_HIGH);
		// 有消息闪动时，屏蔽数量
		ViewUtil.setGone(notifyNotice.findViewById(R.id.cnt));
		if (running)
			return;
		running = true;
		noticIconBtn.postDelayed(blink, wait_time);
	}

	public void startNotify(GuildChatData chatData) {
		this.chatData = chatData;
		RichGuildInfoClient rgic = Account.guildCache.getRichInfoInCache();
		if (null == rgic)
			return;
		new GuildIconCallBack(rgic.getGic(), noticIconBtn,
				45 * Config.SCALE_FROM_HIGH, 45 * Config.SCALE_FROM_HIGH);
		// 有消息闪动时，屏蔽数量
		ViewUtil.setGone(notifyNotice.findViewById(R.id.cnt));
		if (running)
			return;
		running = true;
		noticIconBtn.postDelayed(blink, wait_time);
	}

	public void stopNotify() {
		running = false;
		ViewUtil.setGone(noticIconBtn);
		user = null;
		chatData = null;
	}

	// 未读人数
	public void setUnReadPlayerCnt(int cnt) {
		if (cnt > 0) {
			TextView tv = (TextView) notifyNotice.findViewById(R.id.cnt);
			ViewUtil.setVisible(tv);
			if (cnt <= 9)
				tv.setText(String.valueOf(cnt));
			else
				tv.setText(String.valueOf("9+"));
		} else
			ViewUtil.setGone(notifyNotice.findViewById(R.id.cnt));
	}

	private class Blink implements Runnable {

		@Override
		public void run() {
			if (running) {
				if (ViewUtil.isVisible(noticIconBtn)) {
					wait_time = 500;
					ViewUtil.setHide(noticIconBtn);
				} else {
					wait_time = 1000;
					ViewUtil.setVisible(noticIconBtn);
				}
				noticIconBtn.postDelayed(blink, wait_time);
			}

		}
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public GuildChatData getChatData() {
		return chatData;
	}
}
