package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.ChargeMonthRewardResp;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class MonthRechargeWindow extends CustomPopupWindow implements
		OnClickListener {
	private ImageView monthRechargeBg;
	private TextView desc, rewardDesc, rewardDays;
	private Button rechargeBtn, rewardBtn;

	@Override
	protected void init() {
		super.init("包月优惠");
		setContent(R.layout.month_recharge);
		android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) content
				.getLayoutParams();
		params.topMargin = 0;
		params.bottomMargin = 0;
		monthRechargeBg = (ImageView) window.findViewById(R.id.monthRechargeBg);
		desc = (TextView) window.findViewById(R.id.desc);
		rewardDesc = (TextView) window.findViewById(R.id.rewardDesc);
		rewardDays = (TextView) window.findViewById(R.id.rewardDays);
		rechargeBtn = (Button) window.findViewById(R.id.rechargeBtn);
		rechargeBtn.setOnClickListener(this);
		rewardBtn = (Button) window.findViewById(R.id.rewardBtn);
		rewardBtn.setOnClickListener(this);
	}

	public void open() {
		doOpen();
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	private void setValue() {
		ViewUtil.setImage(monthRechargeBg, "month_recharge_bg.jpg");
		ViewUtil.setRichText(desc, CacheMgr.uiTextCache
				.getTxt(UITextProp.MONTH_CHARGE_REWARDS_DESC));
		int leftDays = Account.user.getMonthChargeRewardLeftDays();
		if (leftDays <= 0 || (leftDays == 1 && Account.user.rewardToday()) ) {
			ViewUtil.setGone(rewardDays);
			ViewUtil.setText(rewardDesc, "充30元,  获18000元宝");
			ViewUtil.setVisible(rechargeBtn);
			ViewUtil.setGone(rewardBtn);
		}
		else {
			ViewUtil.setVisible(rewardDays);
			ViewUtil.setGone(rechargeBtn);
			ViewUtil.setVisible(rewardBtn);
			if (Account.user.rewardToday()) {
				ViewUtil.setText(rewardDesc, "今日已领奖，明天再来吧");
				ViewUtil.setText(rewardDays, "还可持续领取" + (leftDays - 1) + "天");
				ViewUtil.disableButton(rewardBtn);
			} else {
				ViewUtil.setText(rewardDesc, "今日可领取  "
						+ CacheMgr.chargeCommonConfigCache.getMonthReward()
						+ "  元宝");
				ViewUtil.setText(rewardDays, "还可持续领取" + leftDays + "天");
				ViewUtil.enableButton(rewardBtn);
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v == rechargeBtn) {
			controller.goBack();
			controller.openRechargeWindow(RechargeWindow.TYPE_REWARD_RECHARGE,
					Account.user.bref());
		} else if (v == rewardBtn) {
			new ChargeMonthRewardInvoker().start();
		}
	}

	private class ChargeMonthRewardInvoker extends BaseInvoker {
		private ChargeMonthRewardResp resp;
		private ReturnInfoClient ric;

		@Override
		protected String loadingMsg() {
			return "领取奖励";
		}

		@Override
		protected String failMsg() {
			return "领取奖励失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().chargeMonthReward();
			ric = resp.getRic();
		}

		@Override
		protected void onOK() {
			ric.setMsg("领奖成功");
			controller.updateUI(ric, true);
			setValue();
		}

	}
}
