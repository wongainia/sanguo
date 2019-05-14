package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.StringUtil;

public class Step401 extends BaseStep {
	private String heroName;

	public Step401(String heroName) {
		super();
		this.heroName = heroName;
	}

	@Override
	protected void setUI() {
		Account.readLog.step401 = 1;
		addUICenterHeroSpeakTip(heroName + CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_31));
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
		return new Step402();
	}

	public static boolean checkCondition() {
		if (Account.readLog.step401 == 1) {
			return true;
		}
		return false;
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP401);
	}

}
