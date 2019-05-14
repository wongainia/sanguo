package com.vikings.sanguo.ui.guide;

import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.ui.FiefMap;
import com.vikings.sanguo.ui.window.ActTypeWindow;
import com.vikings.sanguo.ui.window.BattleResultWindow;
import com.vikings.sanguo.ui.window.GodWealthWindow;
import com.vikings.sanguo.ui.window.HeroEvolveListWindow;
import com.vikings.sanguo.utils.ViewUtil;

public class StepMgr {
	public static boolean checkCastleStep() {
		boolean run = false;
		if (!run && !Step100.isFinish() && !isRunning()) {
			new Step100().run();
			run = true;
		}

		// 红楼引导
		// 1.当玩家达到22级时，如果 VIP小于2 那么触发
		// 2.当玩家达到40级时，如果是VIP小于3，并且没完成红楼引导，那么触发

		if (!run
				&& !Step202.isFinish()
				&& !isRunning()
				&& (Account.user.getLevel() == 22
						&& Account.user.getCurVip().getLevel() < 2 || Account.user
						.getLevel() == 40
						&& Account.user.getCurVip().getLevel() < 3)/*
																	 * Account.user
																	 * .
																	 * isUnlockExtHero
																	 * ()
																	 */) {
			new Step202().run();
			run = true;
		}

		// // 红楼引导
		// if (!run && !Step211.isFinish() && !isRunning() &&
		// Step201.isFinish()) {
		// Step201.Step2071Trigger();
		// run = true;
		// }

		// 角色升15级 天降横财次引导触发
		if (!run && Account.user.getLevel() == 15 && !Step301.isFinish()
				&& !isRunning()) {
			new Step301().run();
			run = true;
		}

		// 天降横财引导进入 然后退出 需要指引副本
		if (!run && Step301.isFinish() && !isRunning()
				&& GodWealthWindow.enterGodWealthWindowGuide) {
			new Step7053().run();
			GodWealthWindow.enterGodWealthWindowGuide = false;
		}

		// 任意1星将领达到10级可进化时触发
		if (!run /* && Step401.checkCondition() */&& !Step401.isFinish()
				&& !isRunning() && HeroEvolveListWindow.hasCanEvolveHero()) {
			new Step402().run();
			run = true;
		}

		// 角色升10级 引导触发
		if (!run && Account.user.getLevel() >= 10 && !Step501.isFinish()
				&& !isRunning()) {
			new Step501().run();
			run = true;
		}

		// 只要引导触发过且没有完成 就进入
		if (!run && !Step601.isFinish() && !isRunning() && Step501.isFinish()) {
			startStep601();
			run = true;
		}

		if (!run && !isRunning() && Account.readLog.STEP1001 == 1) {
			new Step1001().run();
			run = true;
		}

		if (!run && !Step701.isFinish() && Step702.checkCondition()
				&& !isRunning()) {
			new Step702().run();
			run = true;
		}

		return run;
	}

	// 副本战败充值指引
	public static void checkStep14001_15001() {
		if (Account.user.getCurVip().getLevel() == 0
				&& Config.CAMPAIGN_ID == BaseStep.ACT_CAMPAINGN_1_8
				&& Config.CAMPAIGN_IS_FAIL && !isRunning()
				&& !Step14001.isFinish()) {
			BattleResultWindow.alertVip = false;
			new Step14001().run();
		} else if (Account.user.getCurVip().getLevel() == 1
				&& Config.CAMPAIGN_IS_FAIL && !isRunning()
				&& !Step15001.isFinish()) {
			BattleResultWindow.alertVip = false;
			new Step15001().run();
		}
		Config.CAMPAIGN_IS_FAIL = false;
	}

	// 检测第四关以及之后战败 引导
	public static void checkStep201() {
		if (Config.firstComplement1_4 == 1 && !Step201.isFinish()
				&& !isRunning()) {
			new Step201().run();
		}
		Config.firstComplement1_4 = 0;
	}

	// 首次打赢第1章第4关触发
	public static void checkStep701() {
		if (Config.firstComplement1_4_win == 3 && !Step701.isFinish()
				&& !isRunning() && !Account.allHeroHasFullEquipment()/* 保证有英雄没有穿武器装备 */) {
			new Step701().run();
		}
		Config.firstComplement1_4 = 4;
	}

	// 无尽血战--- 进入副本列表界面 只要无尽血战开放了 才指引
	public static void checkStep801() {
		if (ActTypeWindow.actTypeBloodPosition() != -1 && !isRunning()
				&& !Step801.isFinish()) {
			new Step801().run();
		}
	}

	// 巅峰战场
	public static void checkStep901() {
		if (!isRunning() && !Step901.isFinish()) {
			new Step901().run();
		}
	}

	// step201未完成前 进入红楼特殊提示
	public static boolean checkStep201Bar() {
		if (!Account.isEnterCampaingn4() && !isRunning()) {
			new Step10000(CacheMgr.uiTextCache.getTxt(UITextProp.GUIDE_DESC_19))
					.run();
			return true;
		}
		return false;
	}

	// 角色升到15级 此引导触发
	public static void checkStep301() {
		if (Account.user.getLevel() == 15 && !Step301.isFinish()
				&& !isRunning()) {
			// new Step300().run();
			Config.getController().showCastle(Account.user.getId());
			new Step301().run();
		}

	}

	// 任意1星将领达到10级可进化时触发
	public static void startStep401(String heroName) {
		if (!(Config.getController().getCurPopupUI() instanceof FiefMap)
				&& !Step401.isFinish() && !isRunning()
				&& HeroEvolveListWindow.hasCanEvolveHero()) {
			new Step401(heroName).run();
		}
	}

	// 角色升到10级 此引导触发
	public static void checkStep501() {
		if (Account.user.getLevel() >= 10 && !Step501.isFinish()
				&& !isRunning()) {
			// new Step500().run();
			Config.getController().showCastle(Account.user.getId());
			new Step501().run();
		}

	}

	public static void startStep507() {
		if (!Step503.isFinish() && !isRunning()) {
			new Step507().run();
		}
	}

	// 世界引导结束 才触发 募兵所 《*%林瑞宇%*》要求的
	public static void startStep603() {
		if (!Step601.isFinish() && !isRunning() && Step501.isFinish()) {
			new Step603().run();
		}
	}

	public static void startStep601() {
		// 客户端全部兵力 小于49400 触发 且在step501引导完成触发
		if (!Step601.isFinish()
				&& !isRunning()
				&& Account.myLordInfo.getArmCount() < CacheMgr.uiTextCache
						.getInt(UITextProp.GUIDE_TROOP_TOTAL_69)
				&& Step501.isFinish()) {
			new Step601().run();
		}
	}

	// private static boolean startStep501() {
	// if (Step501.checkConditions()) {
	// new Step501().run();
	// return true;
	// } else
	// return false;
	// }

	public static boolean isRunning() {
		ViewGroup window = (ViewGroup) Config.getController().findViewById(
				R.id.guideWindow);
		return ViewUtil.isVisible(window);
	}

	public static void quit() {
		ViewGroup window = (ViewGroup) Config.getController().findViewById(
				R.id.guideWindow);

		if (ViewUtil.isVisible(window)) {
			if (window.getChildCount() > 0)
				window.removeAllViews();
			window.setBackgroundColor(0x77000000);
			ViewUtil.setGone(window);
		}
	}
}
