package com.vikings.sanguo.ui.guide;

import android.view.View;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.ui.FiefMap;
import com.vikings.sanguo.ui.window.Step505ResourceFief;
import com.vikings.sanguo.utils.TroopUtil;

public class Step504 extends BaseStep {
	private View v;
	private BriefFiefInfoClient bfic;
	private int fiefWidth = 110;
	private int fiefHeight = 110;

	@Override
	protected void setUI() {
		// 居中画图
		v = cpGameUI((Config.screenWidth - fiefWidth) / 2,
				(int) ((Config.screenHeight - fiefHeight) / 2), fiefWidth,
				fiefHeight);
		addArrow(v, 3, 0, 0);
		initPromptView("野地");
	}

	@Override
	protected View getListenerView() {
		return v;
	}

	@Override
	protected void onDestory() {
		bfic = ctr.getFiefMap().getBattleMap()
				.getCachedFief(ctr.getBattleMap().getGuildeNpcFiefID());
		if (bfic != null
				&& !BattleStatus.isInBattle(TroopUtil.getCurBattleState(
						bfic.getBattleState(), bfic.getBattleTime()))) {
			new Step505ResourceFief().show(bfic);

		}

	}

	@Override
	protected BaseStep getNextStep() {
		if (bfic == null
				|| BattleStatus.isInBattle(TroopUtil.getCurBattleState(
						bfic.getBattleState(), bfic.getBattleTime()))) {
			return null;
		} else {
			return null;
		}

	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof FiefMap;
	}

}
