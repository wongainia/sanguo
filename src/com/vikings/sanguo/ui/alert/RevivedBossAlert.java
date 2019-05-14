/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-3-25 下午7:49:34
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.ManorReviveInvoker;
import com.vikings.sanguo.message.ManorReviveReq;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.CurrencyCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RevivedBossAlert extends CustomConfirmDialog implements
		TextWatcher, OnEditorActionListener, OnFocusChangeListener,
		OnClickListener {

	private static final int layout = R.layout.alert_revivedboss;
	private BuildingProp bp;
	private Button okBtn, closeBtn;

	private TextView name, count, price, cost;
	private EditText amount;

	private ViewGroup iconGroup;

	private TroopProp trop;
	private int max;
	private int bossCount;

	public RevivedBossAlert(BuildingProp bp, TroopProp prop, int max) {
		super("救治BOSS");
		this.bp = bp;
		this.trop = prop;
		this.max = max;
		this.bossCount = max;
	}

	// Boss治疗消耗
	private int getCost(int count) {
		int cost = 0;
		if (!ListUtil.isNull(Account.myLordInfo.getReviveBossInfo())) {
			for (ArmInfoClient it : Account.myLordInfo.getReviveBossInfo()) {
				cost += (int) FloatMath.ceil(CacheMgr.manorReviveCache
						.getBossReviveCost(bp.getLevel(), it, count));
			}
		}
		return cost;
	}

	private int getAmount() {
		return StringUtil.parseInt(amount.getText().toString());
	}

	public void show() {
		super.show();
		name = (TextView) content.findViewById(R.id.armname);
		count = (TextView) content.findViewById(R.id.armcount);
		price = (TextView) content.findViewById(R.id.armprice);
		cost = (TextView) content.findViewById(R.id.cost);

		amount = (EditText) content.findViewById(R.id.amount);
		okBtn = (Button) content.findViewById(R.id.okBtn);
		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		iconGroup = (ViewGroup) content.findViewById(R.id.iconGroup);
		amount.addTextChangedListener(this);
		amount.setOnEditorActionListener(this);
		amount.setOnFocusChangeListener(this);
		setStaticValue(trop);
		setValue();// 设值
	}

	// 士兵描述
	private void setStaticValue(final TroopProp troopProp) {
		new ViewImgScaleCallBack(troopProp.getIcon(),
				iconGroup.findViewById(R.id.icon), Constants.ARM_ICON_WIDTH
						* Config.SCALE_FROM_HIGH, Constants.ARM_ICON_HEIGHT
						* Config.SCALE_FROM_HIGH);
		iconGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(troopProp,
						Account.getUserTroopEffectInfo(troopProp.getId()));
			}
		});

		ViewUtil.setText(name, troopProp.getName());
		ViewUtil.setText(count, "数量 : " + String.valueOf(max));// 数量
		ViewUtil.setRichText(price, "价格 : #rmb#" + getCost(max));// 价格
	}

	private void setValue() {
		ViewUtil.setText(amount, String.valueOf(max));
		ViewUtil.setRichText(cost, "#rmb#" + getCost(max));
		ViewUtil.setVisible(okBtn);
		okBtn.setOnClickListener(this);
		ViewUtil.setVisible(closeBtn);
		closeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == okBtn) {
			dismiss();
			new CurrencyCallBack(getCost(getAmount())) {
				@Override
				public void handle() {
					new ManorReviveInvoker(ManorReviveReq.TARGET_BOSS,
							bp.getId(), null, trop.getId(), bossCount).start();
				}
			}.onCall();
		} else if (v == closeBtn) {
			dismiss();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			amount.requestFocus();
			amount.selectAll();
			amount.postDelayed(new Runnable() {

				@Override
				public void run() {
					InputMethodManager inputManager = (InputMethodManager) amount
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(amount, 0);
				}
			}, 100);

		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		String str = amount.getText().toString().trim();
		if (str.length() == 0) {
			bossCount = max;
			ViewUtil.setEditText(amount, String.valueOf(max));
			ViewUtil.setText(cost, getCost(max));
		} else {
			int cnt = Integer.valueOf(str);
			if (cnt > max) {
				bossCount = max;
				ViewUtil.setEditText(amount, String.valueOf(max));
				ViewUtil.setText(cost, getCost(max));
			} else {
				ViewUtil.setText(cost, getCost(cnt));
				bossCount = cnt;
			}
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable s) {
		String str = amount.getText().toString().trim();
		if (str.length() > 0) {
			if (StringUtil.isNumeric(str)) {
				int cnt = Integer.valueOf(str);
				if (cnt > max) {
					ViewUtil.setEditText(amount, String.valueOf(max));
					ViewUtil.setText(cost, getCost(max));
				} else {
					ViewUtil.setText(cost, getCost(cnt));
				}
			} else {
				ViewUtil.setText(cost, "0");
			}
		} else {
			ViewUtil.setText(cost, "0");
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

}
