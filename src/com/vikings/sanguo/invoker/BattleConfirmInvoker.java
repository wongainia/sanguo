/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-26 下午5:01:46
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.RichBattleInfoQueryResp;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.ui.alert.WarEndInfromTip;
import com.vikings.sanguo.ui.alert.WarEndInfromTip.EndType;
import com.vikings.sanguo.thread.CallBack;

public class BattleConfirmInvoker extends BaseInvoker {
	private RichBattleInfoQueryResp resp;
	private long battleId;
	private EndType endType;
	private RichBattleInfoClient rbic;
	private CallBack cb;

	public BattleConfirmInvoker(long battleId, EndType endType,
			RichBattleInfoClient rbic, CallBack cb) {
		this.battleId = battleId;
		this.endType = endType;
		this.rbic = rbic;
		this.cb = cb;
	}

	@Override
	protected String loadingMsg() {
		return "加载中…";
	}

	@Override
	protected String failMsg() {
		return "加载数据失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().richBattleInfoQuery(battleId, true);
	}

	@Override
	protected void onOK() {
		RichBattleInfoClient newRbic = resp.getInfo();
		// 表示战斗结束，战争信息已被清除，且不能update，否则rbic会被清空，无法判断攻守方
		if (null == newRbic || 0 == newRbic.getId()) {
			new WarEndInfromTip(endType, battleId).show();
			// 自动战斗结束
		} else if (newRbic.getAutoBattleTime() <= 0) {
			rbic.update(resp.getInfo(), resp.getHeroInfos());
			new WarEndInfromTip(endType, battleId).show();
		} else {
			rbic.update(resp.getInfo(), resp.getHeroInfos());
			if (null != cb)
				cb.onCall();
		}
	}

	@Override
	protected void afterFire() {
		super.afterFire();
	}

}