package com.vikings.sanguo.invoker;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.RewardTip;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.guide.Step501;
import com.vikings.sanguo.ui.guide.Step503;
import com.vikings.sanguo.ui.guide.Step7051;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.StringUtil;

public class EndGuider extends BaseInvoker {

	public ReturnInfoClient rs;
	private int trainingId;

	protected void refreshUI() {

	}

	public EndGuider(int trainingId) {
		this.trainingId = trainingId;
	}

	@Override
	protected void onFail(GameException exception) {

	}

	@Override
	protected void fire() throws GameException {
		rs = GameBiz.getInstance().trainingComplete(trainingId);
		long flag = Account.user.getTraining();
		flag = StringUtil.setFlagOn(flag, trainingId);
		Account.user.setTraining(flag);
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
	protected void onOK() {
		ctr.updateUI(rs, true, false, true);
		// 显示结算奖励
		if (CacheMgr.trainingRewardsCache.hasRewards(trainingId)) {
			new RewardTip("完成教程获得奖励", CacheMgr.trainingRewardsCache.getRi(
					trainingId).showReturn(true), false, false).show();
		}

		// 特殊处理 世界引导 step501完成引导 则继续下一个引导
		if (trainingId == BaseStep.INDEX_STEP501 && Step501.isFinish()
				&& !Step503.isFinish() && !StepMgr.isRunning()) {
			new Step503().run();
		}

		refreshUI();
	}

	@Override
	protected String failMsg() {
		return null;
	}

}
