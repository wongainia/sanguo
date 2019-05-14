package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.FiefMap;
import com.vikings.sanguo.utils.StringUtil;

public class Step503 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip("我们隶属于" + Account.user.bref().getCountryName()
				+ "，" + CacheMgr.uiTextCache.getTxt(UITextProp.GUIDE_DESC_51));

	}

	@Override
	protected View getListenerView() {
		return window;
	}

	@Override
	protected void onDestory() {
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP503);
	}

	@Override
	protected BaseStep getNextStep() {
		// 没有5000士兵 教程直接退出
		if (Account.manorInfoClient.getCurArmCount() < 5000) {
			endGuider(INDEX_STEP503);
		} else {
			// 设置504回调
			ctr.getFiefMap().getBattleMap()
					.setStep504CallBack(new Step504CallBack());
			ctr.getFiefMap().getBattleMap().moveToFief(true);
		}
		return null;
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof FiefMap;
	}

	public class Step504CallBack implements CallBack {

		@Override
		public void onCall() {
			if (ctr.getFiefMap().getBattleMap().getGuildeNpcFiefID() != 0) {
				ctr.getFiefMap()
						.getBattleMap()
						.moveToFief(
								ctr.getFiefMap().getBattleMap()
										.getGuildeNpcFiefID(), false);

				new Step504().run();
			}
		}

	}
}
