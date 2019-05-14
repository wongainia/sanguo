package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.ArenaAttackResp;
import com.vikings.sanguo.message.ArenaQueryResp;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.BattleWindow;

public class ArenaAttackInvoker extends BaseInvoker {
	private int target;
	private int targetPos;
	private int selfPos;
	private ArenaAttackResp resp;
	private BattleLogInfoClient blic;
	private BattleDriver battleDriver;
	private List<ArenaUserRankInfoClient> topUsers;
	private List<ArenaUserRankInfoClient> attackUsers;
	private ArenaUserRankInfoClient targetAuriic;

	public ArenaAttackInvoker(List<ArenaUserRankInfoClient> topUsers,
			List<ArenaUserRankInfoClient> attackUsers, int target,
			int targetPos, int selfPos) {
		this.target = target;
		this.targetPos = targetPos;
		this.selfPos = selfPos;
		this.topUsers = topUsers;
		this.attackUsers = attackUsers;
	}

	@Override
	protected String loadingMsg() {
		return "挑战巅峰战场";
	}

	@Override
	protected String failMsg() {
		return "挑战巅峰战场失败";
	}

	@Override
	protected void fire() throws GameException {
		try {
			for (ArenaUserRankInfoClient auriic : attackUsers) {
				if (auriic.getUserId() == target) {
					targetAuriic = auriic;
					break;
				}
			}

			resp = GameBiz.getInstance()
					.arenaAttack(target, targetPos, selfPos);
			blic = new BattleLogInfoClient();
			blic.init(resp.getLog());
			battleDriver = new BattleDriver(blic, resp.getRic());
		} catch (GameException e) {
			if (e.getResult() == ResultCode.RESULT_FAILED_ARENA_RANK_CHANGED)
				refresh();
			throw e;
		}
		if (null != blic && blic.isMeWin()) {
			refresh();
			targetAuriic.setRank(selfPos);
			targetAuriic.setCanAttack(false);
		}
	}

	private void refresh() throws GameException {
		ArenaQueryResp resp = GameBiz.getInstance().arenaQuery(false);
		attackUsers.clear();
		attackUsers.addAll(resp.getAttackableUsers());
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRic(), false);
		if (!resp.isHasBattleLog()) {
			ctr.alert("挑战巅峰战场成功，你已获得排名！");
			ctr.goBack();
		} else {
			new BattleWindow().open(battleDriver, new CallBack() {

				@Override
				public void onCall() {
					ctr.goBack();
				}
			}, false, selfPos, Account.myLordInfo.getArenaRank());
		}

	}

}
