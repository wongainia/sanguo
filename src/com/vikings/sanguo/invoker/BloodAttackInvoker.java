package com.vikings.sanguo.invoker;

import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BloodAttackResp;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.ui.window.BattleWindow;

public class BloodAttackInvoker extends BaseInvoker {
	protected int num;
	protected BloodAttackResp resp;
	private BattleLogInfoClient blic;
	private BattleDriver battleDriver;

	public BloodAttackInvoker(int num) {
		this.num = num;
	}

	@Override
	protected String loadingMsg() {
		return "血战";
	}

	@Override
	protected String failMsg() {
		return "血战失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().bloodAttack(num);
		ctr.getFileAccess().saveBattleLogInfo(resp.getLog());
		PrefAccess.setBloodLogId(resp.getLog().getId());

		blic = new BattleLogInfoClient();
		blic.init(resp.getLog());
		battleDriver = new BattleDriver(blic, resp.getRi());
		
		CacheMgr.downloadBattleImgAndSound(blic);
	}

	@Override
	protected void onOK() {
		//ctr.goBack();
		new BattleWindow().open(battleDriver, null, true,true);
	}
}
