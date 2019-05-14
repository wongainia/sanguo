package com.vikings.sanguo.invoker;

import android.widget.ListView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;

public class SendChatMsgInvoker extends BaseInvoker {

	private MessageInfoClient msg;
	private BriefUserInfoClient target;
	private ObjectAdapter adapter;
	private ListView listView;

	private ReturnInfoClient ric;

	public SendChatMsgInvoker(MessageInfoClient msg,
			BriefUserInfoClient target, ObjectAdapter adapter, ListView listView) {
		this.msg = msg;
		this.target = target;
		this.adapter = adapter;
		this.listView = listView;
	}

	@Override
	protected String failMsg() {
		return "发送消息(" + msg.getContext() + ")失败";
	}

	@Override
	protected void onFail(GameException exception) {
		// msg.setSendState(exception.getErrorMsg());
		msg.setSendState("(发送失败)");
		setListViewToBottom();
		super.onFail(exception);
	}

	@Override
	protected void fire() throws GameException {
		ric = GameBiz.getInstance().messagePost(Constants.MSG_PRIVATE,
				target.getId(), msg.getContext());
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
		ctr.updateUI(ric, true);
		msg.setSendState(null);
		// Account.msgInfoCache.addMsg(msg);
	}

	private void setListViewToBottom() {
		if (null == adapter || null == listView)
			return;
		adapter.notifyDataSetChanged();
		listView.setSelection(adapter.getCount() - 1);
	}

}
