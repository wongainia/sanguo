package com.vikings.sanguo.ui.window;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.GuildChatData;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.network.GuildConnector;
import com.vikings.sanguo.thread.HeartBeat;
import com.vikings.sanguo.ui.adapter.GuildChatAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.alert.ToShopTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public class GuildChatWindow extends CustomBaseListWindow implements
		OnClickListener {

	private int type;
	private int target;

	private ViewGroup inputViewGroup, listTitleLayout;
	private View sendBtn, costLayout;
	private EditText inputText;
	private TextView listTitle;

	private RichGuildInfoClient rgic;

	private boolean run = true;
	private boolean start = false;
	private boolean hasNoOldMsg = false;
	private Worker worker = new Worker();
	private int pageSize;

	@Override
	protected void init() {
		if (type == Constants.MSG_GUILD) {
			adapter = new GuildChatAdapter(rgic);
			target = rgic.getGuildid();
			super.init("家族聊天");
		} else if (type == Constants.MSG_COUNTRY) {
			adapter = new GuildChatAdapter();
			target = Account.user.getCountry().intValue();
			super.init("国家频道");
		} else if (type == Constants.MSG_WORLD) {
			adapter = new GuildChatAdapter();
			target = 0;
			super.init("世界频道");
		}

		inputViewGroup = (ViewGroup) controller.inflate(R.layout.chat,
				(ViewGroup) window.findViewById(R.id.content));
		inputText = (EditText) inputViewGroup.findViewById(R.id.inputText);
		sendBtn = inputViewGroup.findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(this);
		costLayout = window.findViewById(R.id.costLayout);
		setCost();
		setWoodBtn();
	}

	@Override
	protected byte getRightBtnBgType() {
		if (type != Constants.MSG_GUILD) {
			return WINDOW_BTN_BG_TYPE_DESC;
		}
		return WINDOW_BTN_BG_TYPE_NULL;
	}

	public void setWoodBtn() {
		if (type != Constants.MSG_GUILD) {
			int itemId = CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID, 2);
			ItemBag itemBag = Account.store.getItemBag(itemId);
			int count = 0;
			if (null != itemBag) {
				count = itemBag.getCount();
			}
			if (count < 0)
				count = 0;
			setRightTxt("#laba#  " + count);
		}
	}

	@Override
	protected View initHeaderView() {
		listTitleLayout = (ViewGroup) controller.inflate(R.layout.list_title,
				listView, false);
		listTitleLayout.setOnClickListener(this);
		listTitle = (TextView) listTitleLayout.findViewById(R.id.listTitle);
		return listTitleLayout;
	}

	private void setCost() {
		if (type == Constants.MSG_GUILD) {
			ViewUtil.setGone(costLayout);
		} else if (type == Constants.MSG_COUNTRY) {
			ViewUtil.setVisible(costLayout);
			int count = CacheMgr.dictCache
					.getDictInt(Dict.TYPE_BATTLE_COST, 24);
			ViewUtil.setRichText(costLayout, R.id.cost, "发送消息需消耗" + count
					+ "个[喇叭]道具", true);
		} else if (type == Constants.MSG_WORLD) {
			ViewUtil.setVisible(costLayout);
			int count = CacheMgr.dictCache
					.getDictInt(Dict.TYPE_BATTLE_COST, 25);
			ViewUtil.setRichText(costLayout, R.id.cost, "发送消息需消耗" + count
					+ "个[喇叭]道具", true);
		}

	}

	private boolean checkCost() {
		if (type == Constants.MSG_COUNTRY) {
			int itemId = CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID, 2);
			int count = CacheMgr.dictCache
					.getDictInt(Dict.TYPE_BATTLE_COST, 24);
			ReturnInfoClient ric = new ReturnInfoClient();
			ric.addCfg(2, itemId, count);
			return ric.checkRequire().isEmpty();
		} else if (type == Constants.MSG_WORLD) {
			int itemId = CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID, 2);
			int count = CacheMgr.dictCache
					.getDictInt(Dict.TYPE_BATTLE_COST, 25);
			ReturnInfoClient ric = new ReturnInfoClient();
			ric.addCfg(2, itemId, count);
			return ric.checkRequire().isEmpty();
		} else {
			return true;
		}

	}

	public void open(int type) {
		this.type = type;
		doOpen();
		firstPage();
		pageSize = resultPage.getPageSize();
		new Thread(worker).start();
	}

	public int getType() {
		return this.type;
	}

	public void open(RichGuildInfoClient rgic) {
		this.rgic = rgic;
		open(Constants.MSG_GUILD);
	}

	@Override
	public void showUI() {
		ViewUtil.setText(listTitle, "点击查看历史聊天记录（最多保存1000条）");
		super.showUI();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	private List<GuildChatData> fetchMsg(boolean newMsg) throws GameException {
		List<GuildChatData> fetchDatas = null;
		int time = getTime(newMsg);
		if (newMsg) {
			fetchDatas = GuildConnector.getChatMsg(Config.snsUrl
					+ "/chat/guild/next", target, Account.user.getId(), time);
			if (type == Constants.MSG_GUILD && !fetchDatas.isEmpty()) {
				HeartBeat.guildChatMsgTime = fetchDatas.get(
						fetchDatas.size() - 1).getTime();
			}
		} else {
			fetchDatas = GuildConnector.getChatMsg(Config.snsUrl
					+ "/chat/guild/pre", target, Account.user.getId(), time);
		}
		fillChatDataUsers(fetchDatas);
		return fetchDatas;
	}

	private void fillChatDataUsers(List<GuildChatData> fetchDatas)
			throws GameException {
		if (null != fetchDatas && !fetchDatas.isEmpty()) {
			List<Integer> userids = new ArrayList<Integer>();
			for (GuildChatData data : fetchDatas) {
				if (!BriefUserInfoClient.isNPC(data.getUserId())
						&& !userids.contains(data.getUserId())) {
					userids.add(data.getUserId());
				}
			}
			if (!userids.isEmpty()) {
				List<BriefUserInfoClient> users = CacheMgr.getUser(userids);
				for (GuildChatData data : fetchDatas) {
					data.setType(type);
					if (!BriefUserInfoClient.isNPC(data.getUserId())) {
						data.setUser(CacheMgr.getUserById(data.getUserId(),
								users));
					} else {
						data.setUser(CacheMgr.npcCache.getNpcUser(data
								.getUserId()));
					}
				}
			}
		}

	}

	private int getTime(boolean newMsg) {
		synchronized (adapter) {
			if (adapter.getContent().isEmpty())
				return 0;
			List<GuildChatData> datas = adapter.getContent();
			if (newMsg) {
				int time = 0;
				for (int i = datas.size() - 1; i >= 0; i--) {
					GuildChatData data = datas.get(i);
					if (!data.isFake()) {
						time = data.getTime();
						break;
					}
				}
				return time;
			} else
				return ((GuildChatData) (adapter.getContent().get(0)))
						.getTime();
		}

	}

	@Override
	protected void fetchData() {
		new FetchChatDataInvoker(true, true).start();
	}

	@Override
	public void handleItem(Object o) {

	}

	@Override
	protected void destory() {
		run = false;
		controller.removeContentFullScreen(window);
		super.destory();
	}

	@Override
	public void onClick(View v) {
		if (v == sendBtn) {
			sendMsg();
		} else if (v == listTitleLayout) {
			new FetchChatDataInvoker(false).start();
		}
	}

	private synchronized void sendMsg() {
		String msg = inputText.getText().toString().trim();
		if (StringUtil.isNull(msg)) {
			controller.alert("发送消息不能为空");
			return;
		}

		if (msg.length() > Constants.CHAT_MSG_MAX_LENGTH) {
			controller.alert(Constants.CHAT_MSG_HINT);
			return;
		}

		if (type == Constants.MSG_GUILD
				&& Account.user.getGuildId() != rgic.getGuildid()) {
			// 统一不跳转界面，停留在该界面报错即可
			controller.alert("你已经不在该家族中,不能执行该操作");
			return;
		}

		if (!checkCost()) {
			// controller.alert(CacheMgr.errorCodeCache.getMsg((short) 49));
			new ToShopTip(CacheMgr.dictCache.getDictInt(Dict.TYPE_ITME_ID, 2))
					.show();
			return;
		}

		new SendInvoker(msg).start();
		inputText.setText("");
	}

	private void setListViewToBottom(boolean scroll2Bottom) {
		adapter.notifyDataSetChanged();
		if (scroll2Bottom)
			listView.post(new Runnable() {

				@Override
				public void run() {
					listView.setSelection(adapter.getCount() - 1);
				}
			});

	}

	private class FetchChatDataInvoker extends BaseInvoker {
		private boolean fetchNewMsg;
		private boolean firstPage = false;

		private List<GuildChatData> fetchDatas;

		public FetchChatDataInvoker(boolean fetchNewMsg) {
			this.fetchNewMsg = fetchNewMsg;
		}

		public FetchChatDataInvoker(boolean fetchNewMsg, boolean firstPage) {
			this.fetchNewMsg = fetchNewMsg;
			this.firstPage = firstPage;
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void beforeFire() {
			loading.start();
		}

		@Override
		protected void fire() throws GameException {
			if (fetchNewMsg) {
				fetchDatas = fetchMsg(fetchNewMsg);
			} else {
				if (!hasNoOldMsg) {
					fetchDatas = fetchMsg(fetchNewMsg);
					if (fetchDatas.size() < resultPage.getPageSize()) {
						hasNoOldMsg = true;
					}
				}
			}

		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void afterFire() {
			loading.stop();
		}

		@Override
		protected void onOK() {
			if (null != adapter
					&& (null != fetchDatas && !fetchDatas.isEmpty())) {
				adapter.addItems(fetchDatas);
				if (!fetchNewMsg) {
					listView.setSelection(0);
				} else {
					if (firstPage) {
						setListViewToBottom(true);
						start = true;
					}
				}
			}

		}
	}

	// 发送聊天消息
	private class SendInvoker extends BaseInvoker {
		private String msg;
		private ReturnInfoClient ric;

		public SendInvoker(String msg) {
			this.msg = msg;
		}

		@Override
		protected String failMsg() {
			return "发送消息(" + msg + ")失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().messagePost(type, target, msg);
		}

		@Override
		protected void beforeFire() {
		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void onOK() {
			controller.updateUI(ric, true);
			GuildChatData data = new GuildChatData();
			data.setFake(true);
			if (type == Constants.MSG_GUILD)
				data.setGuildid(rgic.getGuildid());
			data.setUser(Account.user.bref());
			data.setUserId(Account.user.getId());
			data.setTime((int) (Config.serverTime() / 1000));
			data.setMsg(msg);
			data.setType(type);
			adapter.addItem(data);
			setListViewToBottom(true);
			setWoodBtn();
		}

	}

	private class Worker implements Runnable {

		@Override
		public void run() {
			while (run) {
				if (!start) {
					continue;
				}

				try {
					List<GuildChatData> datas = null;
					boolean scroll2Bottom = true;
					int position = listView.getLastVisiblePosition();
					if (null != adapter && adapter.getCount() > position + 2) {
						scroll2Bottom = false;
					}

					do {
						datas = fetchMsg(true);
						if (null != adapter) {
							adapter.addItems(datas);
						}

					} while (datas != null && datas.size() == pageSize);
					if (datas.size() > 0) {
						controller.getHandler().post(
								new ScrollRunnable(scroll2Bottom));
					}
					if (datas.size() < 10) {
						Thread.sleep(5000);
					}
				} catch (GameException e) {

				} catch (InterruptedException e) {

				}
			}
		}
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {

	}

	private class ScrollRunnable implements Runnable {
		private boolean scroll2Bottom;

		public ScrollRunnable(boolean scroll2Bottom) {
			this.scroll2Bottom = scroll2Bottom;
		}

		@Override
		public void run() {

			setListViewToBottom(scroll2Bottom);
		}

	}

}
