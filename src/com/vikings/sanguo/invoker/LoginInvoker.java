package com.vikings.sanguo.invoker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.AESKeyCache;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.ReadLogCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BaseReq;
import com.vikings.sanguo.model.EventRewards;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.thread.CrashHandler;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class LoginInvoker extends BaseInvoker {

	private SyncDataSet syncData;

	private String imsi = "";

	public LoginInvoker() {
		TelephonyManager mTelephonyMgr = (TelephonyManager) ctr.getUIContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		imsi = mTelephonyMgr.getSubscriberId();
	}

	@Override
	protected void beforeFire() {
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.login_failed);
	}

	@Override
	protected void fire() throws GameException {
		UserAccountClient user = Config.getAccountClient();

		// 不登陆逻辑 本地保存有id和密码
		if (user.getId() != 0 && !StringUtil.isNull(user.getPsw())) {
			Account.user = user;
			// 取ase sessionid缓存也太隐晦了
			AESKeyCache.getInstance();

			Account.notifyVer = PrefAccess.getNotifyVer();
			Config.timeOffset = PrefAccess.getTimeOffset();

			// 且session不为0
			if (null != Config.aesKey && Config.sessionId != 0) {
				try {

					GameBiz.getInstance().gameEnter(Config.clientCode,
							Config.getClientVer());
					ctr.getFileAccess().saveLastInfo(Config.serverId,
							Account.user.getId());
					syncData = getUserSyncData();
					return;
				} catch (GameException e) {
					// 如果抛153或149，重新登陆
					if (e.getResult() != ResultCode.RESULT_FAILED_TOKEN
							&& e.getResult() != ResultCode.RESULT_FAILED_AES_INVALID) {
						throw e;
					}
				}
			}
		}

		// 走登陆流程
		BaseReq.seq = 0;
		user = GameBiz.getInstance().login(imsi);
		Account.user = user;
		ctr.getFileAccess().saveLastInfo(Config.serverId, Account.user.getId());
		AESKeyCache.save();
		if (user.getId() != 0) {
			syncData = getUserSyncData();
		} else {
			syncData = new SyncDataSet();
		}
	}

	private SyncDataSet getUserSyncData() throws GameException {
		SyncDataSet syncData = GameBiz.getInstance().userDataSyn2(
				com.vikings.sanguo.message.Constants.SYNC_TYPE_ALL,
				com.vikings.sanguo.message.Constants.DATA_TYPE_ALL);
		return syncData;
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.login);
	}

	@Override
	protected void onOK() {
		initAccount();
		ctr.initHeartBeat(syncData);
		new CrashHandler(Account.user.getId());
		ctr.getHome().init(0);
		new FetchDataAfterLoginInvoker().start();
		// 活动、奖励是否是第一次
		initEwardShow();
	}

	@Override
	protected void onFail(GameException exception) {
		if (exception.getResult() == ResultCode.RESULT_FAILED_ACCOUNT_RECHARGED
				|| exception.getResult() == ResultCode.RESULT_FAILED_ACCOUNT_VERIFY) {
			ctr.getHome().showMenu();
			ctr.openRertievePwd(false, true);
		} else {
			ctr.getHome().showMenu();
		}
		super.onFail(exception);
	}

	private void initAccount() {
		Account.initAccout();
		Account.setSyncDataAll(syncData);
	}

	private void initEwardShow() {
		List<EventRewards> listLocal = new ArrayList<EventRewards>();
		listLocal.addAll(CacheMgr.eventRewardsCache.getAllRewards());
		if (Account.readLog == null) {
			Account.readLog = ReadLogCache.getInstance();
		}

		if (ListUtil.isNull(listLocal) == false) {
			if (ListUtil.isNull(Account.readLog.rewards) == false) {
				for (EventRewards eventRewards : listLocal) {
					for (EventRewards rewards : Account.readLog.rewards) {
						if (eventRewards.getId() == rewards.getId()) {
							eventRewards.setFirstFlash(rewards.getFirstFlash());
						}
					}
				}
			}
			if (Account.readLog.rewards == null) {
				Account.readLog.rewards = new ArrayList<EventRewards>();
			}

			Account.readLog.rewards.clear();
			Account.readLog.rewards.addAll(listLocal);
		}
	}
}
