package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.ReinforceBuyUnitResp;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.thread.CallBack;

public class ReinforceBuyUnitInvoker extends BaseInvoker {
	private int cost;
	private int type;
	private ReinforceBuyUnitResp resp;
	private CallBack cb;
	private RichBattleInfoClient rbic;

	public ReinforceBuyUnitInvoker(RichBattleInfoClient rbic, int cost,
			int type, CallBack cb) {
		this.cost = cost;
		this.type = type;
		this.cb = cb;
		this.rbic = rbic;
	}

	@Override
	protected String loadingMsg() {
		return "购买神兵中";
	}

	@Override
	protected String failMsg() {
		return "购买神兵失败";
	}

	@Override
	protected void fire() throws GameException {
		CacheMgr.pokerReinforceCache.checkLoad();
		resp = GameBiz.getInstance().reinforceBuyUnit(rbic.getId(), cost, type);
		Account.assistCache.addAssistGodSoldier(rbic.getId(),
				rbic.getUniqueId(), cost, resp.getMoveTroopInfoClient());
		if (null != resp.getTimes())
			Account.assistCache.setTimes(rbic.getId(), rbic.getUniqueId(), resp
					.getTimes().getValue().intValue());
	}

	@Override
	protected void onOK() {
		Config.getController().updateUI(resp.getReturnInfoClient(), false);
		rbic.mergeMoveTroopInfoClient(resp.getMoveTroopInfoClient());
		if (null != cb)
			cb.onCall();
	}
}
