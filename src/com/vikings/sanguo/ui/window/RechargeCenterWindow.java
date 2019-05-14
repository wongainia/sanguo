package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.ui.alert.RechargeOtherInputTip;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class RechargeCenterWindow extends CustomPopupWindow implements
		OnClickListener {

	private TextView smVipDesc, rewardVipDesc;
	private Button smBtn, rewardBtn, doubleBtn, monthBtn;

	@Override
	protected void init() {
		super.init("充值中心");
		setContent(R.layout.recharge_center);
		ViewGroup body = (ViewGroup) window.findViewById(R.id.body);
		body.addView(getContent());
		smVipDesc = (TextView) window.findViewById(R.id.smVipDesc);
		rewardVipDesc = (TextView) window.findViewById(R.id.rewardVipDesc);
		smBtn = (Button) window.findViewById(R.id.smBtn);
		smBtn.setOnClickListener(this);
		rewardBtn = (Button) window.findViewById(R.id.rewardBtn);
		rewardBtn.setOnClickListener(this);
		doubleBtn = (Button) window.findViewById(R.id.doubleBtn);
		doubleBtn.setOnClickListener(this);
		monthBtn = (Button) window.findViewById(R.id.monthBtn);
		monthBtn.setOnClickListener(this);
		setLeftBtn("给他人充值", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RechargeOtherInputTip(RechargeCenterWindow.this).show();
			}
		});
		setRightBtn("VIP特权", new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.openVipListWindow();
			}
		});
	}

	@Override
	public void showUI() {
		setValue();
		super.showUI();
	}

	public void changeUser(BriefUserInfoClient briefUser) {
		controller.openRechargeWindow(RechargeWindow.TYPE_REWARD_RECHARGE,
				briefUser);
	}

	private void setValue() {
		int charge = Account.user.getCharge();
		UserVip cur = Account.getCurVip();
		if (cur.getLevel() < CacheMgr.userVipCache.getMaxLvl()) {
			UserVip next = CacheMgr.userVipCache
					.getVipByLvl(cur.getLevel() + 1);
			int left = next.getCharge() - charge;
			if (next.getSmChargeRate() > 0) {
				int amount = CalcUtil.upNum(1f * left / next.getSmChargeRate());
				ViewUtil.setVisible(smVipDesc);
				ViewUtil.setText(smVipDesc, "【短信充值】 再充值" + amount
						+ "元，可升级为VIP " + next.getLevel());
			} else {
				ViewUtil.setGone(smVipDesc);
			}

			if (next.getChargeRate() > 0) {
				int amount = CalcUtil.upNum(1f * left / next.getChargeRate());
				ViewUtil.setVisible(rewardVipDesc);
				ViewUtil.setText(rewardVipDesc, "【有奖充值】 仅需" + amount
						+ "元，可升级为VIP " + next.getLevel());
			} else {
				ViewUtil.setGone(rewardVipDesc);
			}

		} else {
			ViewUtil.setGone(smVipDesc);
			ViewUtil.setVisible(rewardVipDesc);
			ViewUtil.setText(rewardVipDesc, "恭喜您已成为顶级VIP!");
		}
	}

	protected WebView getContent() {
		WebView webView = ViewUtil.getWebView(controller.getUIContext());

		LayoutParams lp = new LayoutParams(
				(int) (480 * Config.SCALE_FROM_HIGH),
				(int) (520 * Config.SCALE_FROM_HIGH));
		webView.setLayoutParams(lp);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 当有新连接时，使用当前的 WebView
				view.loadUrl(url);
				return true;
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (null != window) {
					if (newProgress == 100) {
						ViewUtil.setGone(window, R.id.loading);
						ViewUtil.setVisible(window, R.id.scroll);
					} else {
						ViewUtil.setVisible(window, R.id.loading);
						ViewUtil.setGone(window, R.id.scroll);
					}
				}
			}

		});

		int level = Account.getCurVip().getLevel();
		String urlStr = "";
		if (level == 0)
			urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 44);
		else if (level == 1)
			urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 45);
		else if (level == 2)
			urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 46);
		else if (level >= 3 && level <= 5)
			urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 47);
		else
			urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 48);

		webView.loadUrl(urlStr + "?sid=" + Config.serverId);
		return webView;
	}

	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	public void open() {
		doOpen();
	}

	@Override
	public void onClick(View v) {
		if (v == smBtn) { // 短信充值
			controller.openRechargeWindow(RechargeWindow.TYPE_SMS_RECHARGE,
					Account.user.bref());
		} else if (v == rewardBtn) { // 有奖充值
			controller.openRechargeWindow(RechargeWindow.TYPE_REWARD_RECHARGE,
					Account.user.bref());
		} else if (v == doubleBtn) { // 双倍优惠
			controller.openDoubleRechargeWindow();
		} else if (v == monthBtn) {// 包月
			controller.openMonthRechargeWindow();
		}
	}
}
