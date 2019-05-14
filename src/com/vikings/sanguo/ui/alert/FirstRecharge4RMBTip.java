/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-8 下午7:19:44
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.pay.VKConstants;

public class FirstRecharge4RMBTip extends CustomConfirmDialog {

	public FirstRecharge4RMBTip() {
		super("首次充值大礼", CustomConfirmDialog.HUGE);
		setRightTopCloseBtn();
		View charge = content.findViewById(R.id.charge);
		if (Config.isCMCC()) {
			ViewUtil.setText(charge, "充值4元立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.pay(VKConstants.CHANNEL_CMCC_MM,
							Account.user.getId(), 400, "");
				}
			});
			ViewUtil.setVisible(content, R.id.smNotice);
		} else if (Config.isTelecom()) {
			ViewUtil.setText(charge, "充值5元立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.pay(VKConstants.CHANNEL_TELCOM,
							Account.user.getId(), 500, "");
				}
			});
			ViewUtil.setGone(content, R.id.smNotice);
		} else {
			int chargeCount = Account.user.getCharge();
			int left = 0;
			UserVip vip1 = CacheMgr.userVipCache.getVipByLvl(1);
			if (null != vip1)
				left = vip1.getCharge() - chargeCount;
			if (left > 0)
				ViewUtil.setText(charge, "再充值" + (left / Constants.CENT)
						+ "元立刻开通");
			else
				ViewUtil.setText(charge, "去充值立刻开通");
			charge.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					controller.openRechargeWindow(Account.user.bref());
				}
			});
			ViewUtil.setGone(content, R.id.smNotice);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_first_recharge_4);
	}

	@Override
	protected void doOnDismiss() {
		QuestInfoClient qic = Account.getNoviceHelpQuest();
		if (null != qic && qic.isComplete()) {
			new SpecialFinishQuestInvoker(qic).start();
		}
	}

	private class SpecialFinishQuestInvoker extends FinishQuestInvoker {

		public SpecialFinishQuestInvoker(QuestInfoClient qic) {
			super(qic);
		}

		@Override
		protected void onFail(GameException exception) {
			if (exception.getResult() == ResultCode.RESULT_FAILED_ARM_COUNT_MAX) {
				Log.e("Invoker fail", Log.getStackTraceString(exception));
				new CommonCustomAlert(
						"士兵已达上限",
						CommonCustomAlert.DEFAULT,
						true,
						"你的士兵数量已经达到上限，无法领取新手救济的士兵<br/><br/><br/>"
								+ StringUtil
										.color("请先升级【军营】提升士兵上限,之后点击主城右上角的【新手救济】按钮,领取救济",
												R.color.k7_color9), "", null,
						"", null, "", false).show();
			} else {
				super.onFail(exception);
			}
		}

		@Override
		protected void onOK() {
			int nextCount = 0;
			QuestInfoClient qic = Account.getNoviceHelpQuest();
			if (qic != null && !qic.isComplete()) {
				nextCount = qic.getLeft();
			}

			SoundMgr.play(R.raw.sfx_receive);
			ctr.updateUI(rs, true, false, true);
			new NoviceHelpFinishTip(rs, nextCount).show();
		}
	}
}
