package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.pay.VKConstants;
import com.vikings.pay.VKSkyPay;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.ui.window.RechargeWindow;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class VipFailTip extends CustomConfirmDialog {
	private ImageView img;
	private View rewardLayout, recommendLayout, smLayout;
	private Button rewardBtn, smBtn;
	private TextView smDesc;
	private int vipLevel;
	private UserVip vip;
	private boolean skipAnim;

	public VipFailTip(int vipLevel) {
		this(vipLevel, false);
	}

	public VipFailTip(int vipLevel, boolean skipAnim) {
		super("开通VIP" + vipLevel);
		this.skipAnim = skipAnim;
		this.vipLevel = vipLevel;
		img = (ImageView) content.findViewById(R.id.img);
		rewardLayout = content.findViewById(R.id.rewardLayout);
		recommendLayout = content.findViewById(R.id.recommendLayout);
		smLayout = content.findViewById(R.id.smLayout);
		rewardBtn = (Button) content.findViewById(R.id.rewardBtn);
		smBtn = (Button) content.findViewById(R.id.smBtn);
		smDesc = (TextView) content.findViewById(R.id.smDesc);
		setRightTopCloseBtn();
	}

	public void show() {
		vip = CacheMgr.userVipCache.getVipByLvl(vipLevel);
		if (vip == null)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		int charge = Account.user.getCharge();
		ViewUtil.setImage(img, "vip_img" + vipLevel);
		int left = vip.getCharge() - charge;

		int rewardAmount = CalcUtil.upNum(1 * left / Constants.CENT);
		if (vip.getChargeRate() > 0)
			rewardAmount = CalcUtil.upNum(1 * left / vip.getChargeRate());
		if (skipAnim) {
			ViewUtil.setImage(img, "skip_anim.jpg");
			ViewUtil.setText(rewardBtn, "充值" + rewardAmount + "元立刻开通VIP");
			ViewUtil.setImage(rewardBtn, R.drawable.btn_red);
		} else {
			ViewUtil.setText(rewardBtn, "有奖充值(仅需" + rewardAmount + "元)");
		}

		rewardBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				controller.openRechargeWindow(
						RechargeWindow.TYPE_REWARD_RECHARGE,
						Account.user.bref());
			}
		});

		if ((Config.isCMCC() || Config.isTelecom() || Config.isCUCC())
				&& vip.getSmChargeRate() > 0) {
			ViewUtil.setVisible(smLayout);
			ViewUtil.setVisible(smDesc);
			final int amount = CalcUtil.upNum(1 * left / vip.getSmChargeRate());
			if (skipAnim) {
				ViewUtil.setText(smBtn, "充值" + amount + "元立刻开通VIP");
				ViewUtil.setImage(smBtn, R.drawable.btn_red);
				ViewUtil.setGone(rewardLayout);
			} else {
				ViewUtil.setText(smBtn, "短信充值(需要" + amount + "元)");
			}
			ViewUtil.setText(smDesc, "选择【短信充值】，将会从您的手机中扣除" + amount + "元话费");
			smBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (!VKSkyPay.isValidAmount(amount)) {
						controller
								.alert("很抱歉！您的手机号码无法充值该额度<br>请选择【有奖充值】方式，或进入充值中心的[短信充值]中，选择其他充值额度");
						return;
					}
					controller.pay(VKConstants.CHANNEL_SKYPAY,
							Account.user.getId(), amount,
							"" + Account.user.getId());
				}
			});
		} else {
			ViewUtil.setGone(smLayout);
			ViewUtil.setGone(smDesc);
			ViewUtil.setGone(recommendLayout);
		}
	}

	@Override
	protected View getContent() {
		return controller
				.inflate(R.layout.alert_vip_fail, contentLayout, false);
	}

}
