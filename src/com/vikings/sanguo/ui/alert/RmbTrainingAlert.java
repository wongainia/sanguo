/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-3-5 上午10:21:52
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.message.ManorDraftResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RmbTrainingAlert extends CustomConfirmDialog implements
		TextWatcher, OnEditorActionListener, OnFocusChangeListener,
		OnClickListener {

	private static final int layout = R.layout.alert_draft_detail;

	private Button okBtn, closeBtn;

	private TextView name, propdesc, desc, cost;
	private EditText amount;

	private ViewGroup iconGroup;

	private TroopProp trop;
	private ManorDraft manorDraft;
	private int max;

	public RmbTrainingAlert(TroopProp prop, ManorDraft manorDraft, int max) {
		super("元宝急征");
		this.trop = prop;
		this.manorDraft = manorDraft;
		this.max = max;
	}

	private int getCost(int count) {
		return CalcUtil
				.upNum((manorDraft.getCost() / Constants.PER_TEN_THOUSAND)
						* count);
	}

	private int getAmount() {
		return StringUtil.parseInt(amount.getText().toString());
	}

	public void show() {
		super.show();
		name = (TextView) content.findViewById(R.id.armname);
		propdesc = (TextView) content.findViewById(R.id.propdesc);
		desc = (TextView) content.findViewById(R.id.desc);
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
		ViewUtil.setText(propdesc, troopProp.getDesc());
	}

	private void setValue() {
		ViewUtil.setText(desc, "用元宝招募士兵，目前你还能招募" + max + "名士兵");
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
			int costCurrency = getCost(getAmount());
			if (Account.user.getCurrency() < costCurrency) {
				dismiss();
				new ToActionTip(costCurrency).show();
			} else {
				new RmbTrainingInvoker().start();
			}
		} else if (v == closeBtn) {
			dialog.dismiss();
		}
	}

	private class RmbTrainingInvoker extends BaseInvoker {
		private ManorDraftResp resp;
		private int count;
		private BuildingBuyResp buildingResp;

		@Override
		protected String loadingMsg() {
			return "训练中…";
		}

		@Override
		protected String failMsg() {
			return "训练失败";
		}

		@Override
		protected void fire() throws GameException {
			BuildingProp bp = (BuildingProp) CacheMgr.buildingPropCache
					.get(manorDraft.getBuildingId());
			if (null == Account.manorInfoClient.getBuilding(bp)
					&& CacheMgr.buildingBuyCostCache.isEnough(bp.getId()))
				buildingResp = GameBiz.getInstance().buildingBuy(
						Constants.TYPE_MATERIAL, bp.getId());

			count = getAmount();
			resp = GameBiz.getInstance().manorDraft(manorDraft.getBuildingId(),
					trop.getId(), Constants.MANOR_DRAFT_BY_RMB, count);
		}

		@Override
		protected void onOK() {
			if (null != buildingResp)
				ctr.updateUI(buildingResp.getRi(), true, true, true);

			dismiss();
			SoundMgr.play(R.raw.sfx_recruit);
			ctr.alert(
					"招募成功",
					"恭喜你招募了"
							+ StringUtil.color("" + count,
									ctr.getResourceColorText(R.color.k7_color5))
							+ "名" + trop.getName(), null, false);
			ctr.updateUI(resp.getRi(), true);
			Account.manorInfoClient.update(resp.getMic());

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
			ViewUtil.setEditText(amount, String.valueOf(max));
			ViewUtil.setText(cost, getCost(max));
		} else {
			int cnt = Integer.valueOf(str);
			if (cnt > max) {
				ViewUtil.setEditText(amount, String.valueOf(max));
				ViewUtil.setText(cost, getCost(max));
			} else {
				ViewUtil.setText(cost, getCost(cnt));
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
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

}
