package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.AccountRestore3Resp;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.UserChooseAdapter;
import com.vikings.sanguo.widget.PageListView;

public class UserChooseTip extends PageListView {
	private CallBack callBack;

	public UserChooseTip(List<AccountPswInfoClient> infos, CallBack callBack) {
		super();
		setTitle("请选择角色");
		setContentTitle("找回以下账户，选择服务器分区进入");
		this.callBack = callBack;
		adapter.addItems(infos);
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return new UserChooseAdapter();
	}

	@Override
	public void handleItem(Object o) {
		controller.setBackKeyValid(true);
		// ServerData serverData = Config.getController().getServerFileCache()
		// .getByServerId(client.getSid());
		// Config.setServer(serverData, client);
		// GameBiz.reset();
		// controller.getHome().checkVer();
		new AccountRestore3Invoker((AccountPswInfoClient) o).start();
	}

	@Override
	public void getServerData(ResultPage resultPage) throws GameException {

	}

	private class AccountRestore3Invoker extends BaseInvoker {
		private AccountPswInfoClient client;
		private AccountRestore3Resp resp3;

		public AccountRestore3Invoker(AccountPswInfoClient client) {
			this.client = client;
		}

		@Override
		protected void fire() throws GameException {
			try {
				TelephonyManager mTelephonyMgr = (TelephonyManager) ctr
						.getUIContext().getSystemService(
								Context.TELEPHONY_SERVICE);
				String imsi = mTelephonyMgr.getSubscriberId();
				resp3 = GameBiz.getInstance().accountRestore3(
						client.getUserid(), client.getPsw(), imsi);
				ServerData serverData = Config.getController()
						.getServerFileCache().getByServerId(client.getSid());
				client.setPsw(resp3.getPsw());
				Config.setServer(serverData, client);
				ctr.getFileAccess().updateUser(serverData, client);
				GameBiz.reset();
			} catch (GameException e) {
				Log.e("VerifyInvoker", e.getMessage());
			}
		}

		@Override
		protected void onOK() {
			dismiss();
			SharedPreferences share = Config
					.getController()
					.getUIContext()
					.getSharedPreferences(Constants.RESTORE_TAG,
							Context.MODE_PRIVATE);
			share.edit().clear().commit();
			if (null != callBack)
				callBack.onCall();
		}

		@Override
		protected String loadingMsg() {
			return "加载中";
		}

		@Override
		protected String failMsg() {
			return "加载失败";
		}

	}
}
