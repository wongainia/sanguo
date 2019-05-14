package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.StringUtil;

public class Step601 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_61));
		Account.readLog.Step601 = 1;
	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
		ctr.showCastle(Account.user.getId());
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step602();
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP601);
	}

	public static boolean checkCondition() {
		// 1 成功前 2成功后
		if (Account.readLog.Step601 == 1) {
			return true;
		}
		return false;
	}

}
