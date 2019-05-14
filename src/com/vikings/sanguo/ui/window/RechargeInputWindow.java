/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-3
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 选择充值金额，及输入卡号密码的界面
 */

package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.vikings.pay.VKConstants;
import com.vikings.pay.VKSkyPay;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.RechargeCardInfo;
import com.vikings.sanguo.model.RechargeState;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.RechargeInputAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class RechargeInputWindow extends CustomPopupWindow {
	private ViewGroup msg1Layout, msg2Layout, msg3Layout, msg4Layout,
			cardNoLayout, pwdLayout, rechargeDescLayout;
	private GridView gridView;
	private EditText cardNum, pwdNum;

	private byte rechargeId;// 对应RechargeState中id
	private String name = "";// 充值方式
	private BriefUserInfoClient briefUser; // 充值目标对象

	private static int[] cmCardAmounts = { 1000, 2000, 3000, 5000, 10000,
			20000, 30000, 50000 };
	private static int[] cuCardAmounts = { 2000, 3000, 5000, 10000, 30000,
			50000 };
	private static int[] ctCardAmounts = { 5000, 10000 };

	private int[] amounts;

	private RechargeInputAdapter adapter;

	// private int cardAmount; // 充值卡面额 单位:分

	@Override
	protected void init() {
		super.init(name);
		setContent(R.layout.recharge_input);
		gridView = (GridView) window.findViewById(R.id.gridView);
		msg1Layout = (ViewGroup) window.findViewById(R.id.msg1Layout);
		msg2Layout = (ViewGroup) window.findViewById(R.id.msg2Layout);
		ViewUtil.setText(msg2Layout, R.id.gradientMsg, "充值卡序列号");
		msg3Layout = (ViewGroup) window.findViewById(R.id.msg3Layout);
		ViewUtil.setText(msg3Layout, R.id.gradientMsg, "充值卡密码");
		msg4Layout = (ViewGroup) window.findViewById(R.id.msg4Layout);
		ViewUtil.setText(msg4Layout, R.id.gradientMsg, "充值说明");
		cardNoLayout = (ViewGroup) window.findViewById(R.id.cardNoLayout);
		pwdLayout = (ViewGroup) window.findViewById(R.id.pwdLayout);
		rechargeDescLayout = (ViewGroup) window
				.findViewById(R.id.rechargeDescLayout);

		cardNum = (EditText) findViewById(R.id.cardNum);
		pwdNum = (EditText) findViewById(R.id.pwdNum);

		setBottomButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				doClick();
			}
		});

		WebView webView = ViewUtil.getWebView(controller.getUIContext());
		String amountDescSuffix = "元充值";
		switch (rechargeId) {
		case RechargeState.ID_CHINA_MOBILE_CARD:
			webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 20));
			amounts = cmCardAmounts;
			amountDescSuffix = "元卡充值";
			ViewUtil.setVisible(cardNoLayout);
			ViewUtil.setVisible(pwdLayout);
			ViewUtil.setText(msg1Layout, R.id.gradientMsg, "充值卡面额");
			break;
		case RechargeState.ID_CHINA_UNICOM_CARD:
			webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 21));
			amounts = cuCardAmounts;
			amountDescSuffix = "元卡充值";
			ViewUtil.setVisible(cardNoLayout);
			ViewUtil.setVisible(pwdLayout);
			ViewUtil.setText(msg1Layout, R.id.gradientMsg, "充值卡面额");
			break;
		case RechargeState.ID_CHINA_TELECOM_CARD:
			webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 22));
			amounts = ctCardAmounts;
			amountDescSuffix = "元卡充值";
			ViewUtil.setVisible(cardNoLayout);
			ViewUtil.setVisible(pwdLayout);
			ViewUtil.setText(msg1Layout, R.id.gradientMsg, "充值卡面额");
			break;
		case RechargeState.ID_CHINA_MOBILE_SM:
			webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 33));
			ViewUtil.setGone(cardNoLayout);
			ViewUtil.setGone(pwdLayout);
			amounts = VKSkyPay.getAmount();
			ViewUtil.setText(msg1Layout, R.id.gradientMsg, "充值金额");
			checkSMAmounts();
			break;
		case RechargeState.ID_CHINA_TELCOM_SM:
			webView.loadUrl(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					(byte) 34));
			amounts = VKSkyPay.getAmount();
			ViewUtil.setGone(cardNoLayout);
			ViewUtil.setGone(pwdLayout);
			ViewUtil.setText(msg1Layout, R.id.gradientMsg, "充值金额");
			checkSMAmounts();
			break;
		default:
			amounts = new int[0];
			break;
		}

		rechargeDescLayout.addView(webView);

		adapter = new RechargeInputAdapter(new CallBack() {

			@Override
			public void onCall() {
				adapter.notifyDataSetChanged();
			}
		}, amountDescSuffix);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < amounts.length; i++) {
			list.add(new Integer(amounts[i]));
		}
		adapter.addItems(list);
		gridView.setAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	private void checkSMAmounts() {
		if (amounts.length <= 0) {
			TextView textView = (TextView) msg1Layout
					.findViewById(R.id.gradientMsg);
			textView.setSingleLine(false);
			ViewUtil.setGone(msg1Layout, R.id.gradientBg);
			ViewUtil.setText(msg1Layout, R.id.gradientMsg,
					"读取sim卡失败，请确保sim卡已安装。如果sim卡安装正常，请退出游戏再重新进入。");
			ViewUtil.disableButton(belowBtnFrame);
		}
	}

	public void open(byte rechargeId, String name, BriefUserInfoClient briefUser) {
		this.rechargeId = rechargeId;
		this.briefUser = briefUser;
		this.name = name;
		doOpen();
	}

	// public String getTitle() {
	// switch (rechargeId) {
	// case RechargeState.ID_CHINA_MOBILE_CARD:
	// return "移动卡充值";
	// case RechargeState.ID_CHINA_UNICOM_CARD:
	// return "联通卡充值";
	// case RechargeState.ID_CHINA_TELECOM_CARD:
	// return "电信卡充值";
	// case RechargeState.ID_CHINA_MOBILE_SM:
	// return "移动短信充值";
	// case RechargeState.ID_CHINA_TELCOM_SM:
	// return "电信短信充值";
	// default:
	// return "";
	// }
	// }

	private void doClick() {
		String serial = cardNum.getText().toString().trim();
		String pwd = pwdNum.getText().toString().trim();
		int position = adapter.getSelect();
		if (position > amounts.length - 1) {
			controller.alert("请选择金额");
			return;
		}
		switch (rechargeId) {
		case RechargeState.ID_CHINA_MOBILE_CARD:
			if (isMoblieRechargeCardValid(serial, pwd)) {
				controller.openRechargeInputConfirmWindow(getRechargeCardInfo(
						amounts[position], serial, pwd, rechargeId));
			} else {
				showCardInvalidMsg();
			}
			break;
		case RechargeState.ID_CHINA_UNICOM_CARD:
			if (isUnicomRechargeCardValid(serial, pwd)) {
				controller.openRechargeInputConfirmWindow(getRechargeCardInfo(
						amounts[position], serial, pwd, rechargeId));
			} else {
				showCardInvalidMsg();
			}
			break;
		case RechargeState.ID_CHINA_TELECOM_CARD:
			if (isTelecomRechargeCardValid(serial, pwd)) {
				controller.openRechargeInputConfirmWindow(getRechargeCardInfo(
						amounts[position], serial, pwd, rechargeId));
			} else {
				showCardInvalidMsg();
			}
			break;
		case RechargeState.ID_CHINA_MOBILE_SM:
			controller.pay(VKConstants.CHANNEL_SKYPAY, briefUser.getId(),
					amounts[position], "" + Account.user.getId());
			// controller.pay(VKConstants.CHANNEL_CMCC_MM, briefUser.getId(),
			// amounts[position], "" + Account.user.getId());
			break;
		case RechargeState.ID_CHINA_TELCOM_SM:
			controller.pay(VKConstants.CHANNEL_SKYPAY, briefUser.getId(),
					amounts[position], "" + Account.user.getId());
			// controller.pay(VKConstants.CHANNEL_TELCOM, briefUser.getId(),
			// amounts[position], "" + Account.user.getId());
			break;
		default:
			break;
		}
	}

	private RechargeCardInfo getRechargeCardInfo(int amount, String serial,
			String pwd, byte rechargeId) {
		RechargeCardInfo info = new RechargeCardInfo();
		info.setTarget(briefUser);
		info.setAmount(amount / Constants.CENT);
		info.setSerial(serial);
		info.setPswd(pwd);
		info.setChannel(rechargeId);
		return info;
	}

	private void showCardInvalidMsg() {
		controller.alert("卡号或密码长度有误，请重试!");
	}

	private void showPhoneNumInvalidMsg() {
		controller.alert("您输入的号码不是中国电信号段号码，或号码长度有误，请重新输入");
	}

	private boolean isMoblieRechargeCardValid(String serial, String pwd) {
		if ((17 == serial.length() && 18 == pwd.length()) // 全国卡或广东卡
				|| (10 == serial.length() && 8 == pwd.length()) // 浙江卡
				|| (16 == serial.length() && 17 == pwd.length()) // 江苏卡或福建卡
				|| (16 == serial.length() && 21 == pwd.length())) // 辽宁卡
			return true;
		return false;
	}

	private boolean isUnicomRechargeCardValid(String serial, String pwd) {
		if (15 == serial.length() && 19 == pwd.length())
			return true;
		return false;
	}

	private boolean isTelecomRechargeCardValid(String serial, String pwd) {
		if (19 == serial.length() && 18 == pwd.length())
			return true;
		return false;
	}

	// 电信短信充值号码校验
	private boolean isCTShortMsgNumValid(String nr) {
		Pattern pattern = Pattern.compile("^(153|133|180|189)[0-9]{8}");
		Matcher matcher = pattern.matcher(nr);
		return matcher.matches();
	}
}
