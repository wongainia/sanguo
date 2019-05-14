package com.vikings.sanguo.ui.window;

import java.util.Iterator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.AddFriendInvoker;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.SendChatMsgInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ChatAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ChatWindow extends CustomListViewWindow implements OnClickListener {

	private ViewGroup inputViewGroup, costLayout;
	private View sendBtn;
	private EditText inputText;
	private ChatAdapter adapter;
	private BriefUserInfoClient briefUser;

	@Override
	protected void init() {
		adapter = new ChatAdapter(briefUser);
		super.init(briefUser.getNickName());
		setLeft();
		setRight();
		adapter.setListView(listView);
		inputViewGroup = (ViewGroup) controller.inflate(R.layout.chat,
				(ViewGroup) window.findViewById(R.id.content));
		inputText = (EditText) inputViewGroup.findViewById(R.id.inputText);
		sendBtn = inputViewGroup.findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(this);
		costLayout = (ViewGroup) window.findViewById(R.id.costLayout);
		if (briefUser.getId().intValue() == Constants.GM_USER_ID) {
			ViewUtil.setGone(costLayout);
		} else {
			ViewUtil.setVisible(costLayout);
			ViewUtil.setRichText(costLayout, R.id.cost, "发送消息需消耗 "
					+ CacheMgr.dictCache.getDict(Dict.TYPE_BATTLE_COST, 23)
					+ "#money#");
		}
		Account.msgInfoCache.setRead(briefUser.getId().intValue());
	}

	private void setRight() {
		setRightTxt("#money# " + Account.user.getMoney());
	}

	private void setLeft() {
		if (Account.isFriend(briefUser)) {
			setLeftBtnBg(WINDOW_BTN_BG_TYPE_NULL);
			setLeftBtn("", null);
		} else {
			setLeftBtnBg(WINDOW_BTN_BG_TYPE_CLICK);
			setLeftBtn("添加好友", new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Account.friends.contains(briefUser.getId().intValue())) {
						controller.alert(CacheMgr.errorCodeCache
								.getMsg((short) 47));
						return;
					}
					new AddFriendInvoker(briefUser, new CallBack() {

						@Override
						public void onCall() {
							setLeft();
						}
					}).start();
				}
			});
		}
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	public void open(BriefUserInfoClient briefUser) {
		this.briefUser = briefUser;
		if (Account.isBlackList(briefUser)) {
			controller.alert(Constants.IN_SELF_BLACK_LIST);
			return;
		}
		doOpen();
	}

	public BriefUserInfoClient getBriefUser() {
		return briefUser;
	}

	private void sendText() {
		String str = inputText.getText().toString();
		if (str == null || str.trim().length() == 0) {
			controller.alert("发送失败", "聊天信息不能为空", null, true);
			return;
		}

		if (str.trim().length() > 128) {
			controller.alert("发送失败", "聊天信息不能超过128个字", null, true);
			return;
		}

		MessageInfoClient msg = new MessageInfoClient(Account.user.bref(),
				briefUser, str);

		// 增加聊天记录
		addMsg(msg);
		new SendInvoker(msg, briefUser, adapter, listView).start();
		inputText.setText("");

	}

	@Override
	public void onClick(View v) {
		if (v == sendBtn) {
			sendText();
		}
	}

	public BriefUserInfoClient getUser() {
		return briefUser;
	}

	@Override
	protected void destory() {
		super.destory();
		new SaveInvoker().start();
	}

	@Override
	public void showUI() {
		setLeft();
		chatDate();
		super.showUI();
	}

	@SuppressWarnings("unchecked")
	private void chatDate() {
		List<MessageInfoClient> contents = adapter.getContent();
		List<MessageInfoClient> cacheMsgs = Account.msgInfoCache
				.getChat(briefUser.getId().intValue());
		if (contents.size() != cacheMsgs.size()) {
			addMsg(cacheMsgs);
		} else {
			if (!contents.isEmpty() && !cacheMsgs.isEmpty()) {
				MessageInfoClient content = contents.get(contents.size() - 1);
				MessageInfoClient cacheMsg = cacheMsgs
						.get(cacheMsgs.size() - 1);
				if (!content.equals(cacheMsg)) {
					addMsg(cacheMsgs);
				}
			}
		}

	}

	@Override
	public void hideUI() {
		super.hideUI();
	}

	// 增加一条聊天记录
	public void addMsg(MessageInfoClient msg) {
		adapter.addItem(msg);
		Account.msgInfoCache.addMsg(msg);
		setListViewToBottom();
	}

	private void setListViewToBottom() {
		adapter.notifyDataSetChanged();
		listView.setSelection(adapter.getCount() - 1);
	}

	// 重置聊天记录队列
	public void addMsg(List<MessageInfoClient> msg) {
		adapter.clear();
		adapter.addItems(msg);
		setListViewToBottom();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected int refreshInterval() {
		return Config.getIntConfig("heartBeatTime");
	}

	private class SaveInvoker extends BaseInvoker {

		@Override
		protected void beforeFire() {

		}

		@Override
		protected void afterFire() {
		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected String failMsg() {
			return null;
		}

		@Override
		protected void fire() throws GameException {
			if (null != Account.msgInfoCache) {
				List<MessageInfoClient> cacheMsgs = Account.msgInfoCache
						.getChat(briefUser.getId().intValue());
				for (Iterator<MessageInfoClient> iter = cacheMsgs.iterator(); iter
						.hasNext();) {
					MessageInfoClient mic = iter.next();
					if (!StringUtil.isNull(mic.getSendState())) {
						iter.remove();
					}
				}
				Account.msgInfoCache.save(false);
			}
		}

		@Override
		protected void onOK() {
		}

	}

	private class SendInvoker extends SendChatMsgInvoker {

		public SendInvoker(MessageInfoClient msg, BriefUserInfoClient target,
				ObjectAdapter adapter, ListView listView) {
			super(msg, target, adapter, listView);
		}

		@Override
		protected void onOK() {
			super.onOK();
			setRight();
		}

	}

}
