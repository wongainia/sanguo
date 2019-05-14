package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.CallBack;

public class ArenaAwradInvoker extends BaseInvoker {

	private CallBack callBack;
	private ReturnInfoClient ric;

	public ArenaAwradInvoker(CallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	protected String loadingMsg() {
		return "领取巅峰战场奖励 ";
	}

	@Override
	protected String failMsg() {
		return "领取巅峰战场奖励 失败";
	}

	@Override
	protected void fire() throws GameException {
		ric = GameBiz.getInstance().arenaAwrad();
		GameBiz.getInstance().refreshQuest();
	}

	@Override
	protected void onOK() {
		ctr.updateUI(ric, false);
		ctr.alert("领取巅峰战场奖励成功");
		if (null != callBack)
			callBack.onCall();
	}

}
