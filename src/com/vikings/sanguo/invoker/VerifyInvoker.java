package com.vikings.sanguo.invoker;

import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.AccountRestore2Resp;
import com.vikings.sanguo.message.AccountRestore3Resp;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.ui.alert.LoadingTip;

public abstract class VerifyInvoker extends BaseInvoker {
	protected AccountRestore2Resp resp;
	protected AccountRestore3Resp resp3;
	protected int flag, verifyCode;
	protected String value;
	protected List<AccountPswInfoClient> apis;

	private LoadingTip loadingTip = new LoadingTip();

	public VerifyInvoker(int flag, String value, int verifyCode) {
		this.flag = flag;
		this.value = value;
		this.verifyCode = verifyCode;
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().accountRestore2(flag, value, verifyCode);
		apis = resp.getInfos();
		ctr.getFileAccess().saveUser(resp.getInfos());
		if (singleId()) { // 单个账号，会直接进入游戏，需要发找回第三步
			doRestore3(apis.get(0));
		}
	}

	protected boolean singleId() {
		return null != apis && apis.size() == 1;
	}

	protected void doRestore3(AccountPswInfoClient info) throws GameException {

		TelephonyManager mTelephonyMgr = (TelephonyManager) ctr.getUIContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
		resp3 = GameBiz.getInstance().accountRestore3(info.getUserid(),
				info.getPsw(), imsi);
		ServerData serverData = Config.getController().getServerFileCache()
				.getByServerId(info.getSid());
		info.setPsw(resp3.getPsw());
		Config.setServer(serverData, info);
		ctr.getFileAccess().updateUser(serverData, info);
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.VerifiInvoker_failMsg);
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.VerifiInvoker_loadingMsg);
	}

	@Override
	protected void beforeFire() {
		loadingTip.show(loadingMsg());
	}

	@Override
	protected void afterFire() {
		loadingTip.dismiss();
	}
}
