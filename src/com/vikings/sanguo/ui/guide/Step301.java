package com.vikings.sanguo.ui.guide;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.utils.StringUtil;

public class Step301 extends BaseStep {
	private View view;

	@Override
	protected void setUI() {
		// 保证奖励界面一定显示 否则程序会崩溃
		if (findView(R.id.rightInfo) != null
				&& findView(R.id.rightInfo).getVisibility() == View.GONE) {
			findView(R.id.rightInfo).setVisibility(View.VISIBLE);
		}

		view = cpGameUI(findView(R.id.rewardsBtn));
		addArrow(view, 3, 0, 0);
	}

	@Override
	protected View getListenerView() {
		return view == null ? window : view;
	}

	@Override
	protected void onDestory() {
		if (findView(R.id.rewardsBtn) != null)
			findView(R.id.rewardsBtn).performClick();
	}

	@Override
	protected BaseStep getNextStep() {
		return new Step302();
	}

	public static boolean isFinish() {
		return StringUtil.isFlagOn(Account.user.getTraining(),
				BaseStep.INDEX_STEP301);
	}

	@Override
	protected boolean isSpecificWindow() {
		if (null == Config.getController().getCurPopupUI())
			return false;
		return Config.getController().getCurPopupUI() instanceof CastleWindow;
	}

}
