package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.window.BattleResultWindow;
import com.vikings.sanguo.utils.StringUtil;

//角色战败红楼引导  第四关战败后触发
public class Step201 extends BaseStep {

	@Override
	protected void setUI() {
		addUICenterHeroSpeakTip(CacheMgr.uiTextCache
				.getTxt(UITextProp.GUIDE_DESC_11));

		// 添加成功前标识位
		Account.readLog.Step201SelectHero = 1;
		Account.readLog.save();
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
		return new Step202();
	}

	public static boolean checkConditions() {

		return true;
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP201);
	}

	public static void Step202Trigger() {
		// 1 成功前
		if (Account.readLog.Step201SelectHero == 1) {
			new Step202().run();
		}
		//
	}

	public static void Step2071Trigger() {
		// 1 成功前
		if (Account.readLog.Step201SelectHero == 1) {
			new Step2071().run();
		}
		//
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof BattleResultWindow;
	}
}
