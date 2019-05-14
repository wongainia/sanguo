package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.AESKeyCache;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.thread.CallBack;

public class RegisterInvoker extends BaseInvoker {

	private String nikeName;
	private int sex;
	private int province = 0;
	private CallBack callBack;

	public RegisterInvoker(String nikeName, int sex, CallBack callBack) {
		this.nikeName = nikeName;
		this.sex = sex;
		this.callBack = callBack;
	}

	@Override
	protected String failMsg() {
		ctr.getHeartBeat().start();
		return ctr.getString(R.string.RegisterInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		UserAccountClient user = GameBiz.getInstance().register(nikeName, sex,
				province, "");
		// 下面登录消息封装包头的时候需要从Account.user中取id
		ctr.getFileAccess().saveUser(Config.serverId, user.getId(),
				user.getLevel(), nikeName, user.getPsw());
		ctr.getFileAccess().saveLastInfo(Config.serverId, user.getId());
		Account.user = user;
		Account.user = GameBiz.getInstance().login(user);

		// 注册后会送建筑等一堆东西，所以要做一个全量同步,且由于登录不返回roleinfo，所以登录完还是要做user信息的全量数据同步
		SyncDataSet dataSet = GameBiz.getInstance().userDataSyn2(
				Constants.SYNC_TYPE_ALL, Constants.DATA_TYPE_ALL);
		Account.setSyncDataAll(dataSet);
		AESKeyCache.save();
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.RegisterInvoker_loadingMsg);
	}

	@Override
	protected void onOK() {
		if (null != callBack) {
			callBack.onCall();
		}
		ctr.setAccountBarUser(Account.user);
		ctr.openFirst();
		new LogInvoker("注册完成").start();
	}

}
