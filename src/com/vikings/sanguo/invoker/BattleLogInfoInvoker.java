package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.ui.window.BattleWindow;

public class BattleLogInfoInvoker extends BaseInvoker {
	private BattleLogInfoClient blic;
	protected BattleDriver battleDriver;
	private boolean isHistoryBattleLogTip = false;

	private long battleLogId;

	public BattleLogInfoInvoker(long battleLogId) { //
		this.battleLogId = battleLogId;
	}

	public BattleLogInfoInvoker(long battleLogId, boolean isHistoryBattleLogTip) { //
		this.battleLogId = battleLogId;
		this.isHistoryBattleLogTip = isHistoryBattleLogTip;
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.BattleLogInfoInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		BattleLogInfo info = Account.battleLogCache
				.getBattleLogInfo(battleLogId);
		blic = new BattleLogInfoClient();
		blic.init(info);

		battleDriver = new BattleDriver(blic, null);
		CacheMgr.downloadBattleImgAndSound(blic);
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.query_);
	}

	@Override
	protected void onOK() {
		if (!isHistoryBattleLogTip)
			ctr.goBack();
		new BattleWindow().open(battleDriver, null, true);
	}
}
