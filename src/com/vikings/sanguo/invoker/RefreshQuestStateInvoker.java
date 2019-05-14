package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.thread.CallBack;

public class RefreshQuestStateInvoker extends BaseInvoker {

	public CallBack callBack;

	public RefreshQuestStateInvoker(CallBack callBack) {
		this.callBack = callBack;
	}

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
	protected void onFail(GameException exception) {
		if (null != callBack) {
			ctr.getHandler().post(new Runnable() {

				@Override
				public void run() {
					callBack.onCall();
				}
			});
		}
	}

	@Override
	protected void fire() throws GameException {
		SyncDataSet data = GameBiz.getInstance().refreshQuest();
		ctr.getHeartBeat().updateUI(data, true, false);
	}

	@Override
	protected void onOK() {
		if (null != callBack) {
			ctr.getHandler().post(new Runnable() {

				@Override
				public void run() {
					callBack.onCall();
				}
			});
		}

	}

}
