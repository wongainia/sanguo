package com.vikings.sanguo.ui.alert;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.message.ManorDraftResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RmbTrainingTip extends CustomConfirmDialog implements TextWatcher,
		OnEditorActionListener, OnFocusChangeListener {

	private static final int layout = R.layout.alert_rmb_training;

	private TextView desc, cost;
	private EditText amount;

	private TroopProp prop;
	private ManorDraft manorDraft;
	private int max;

	public RmbTrainingTip(TroopProp prop, ManorDraft manorDraft, int max) {
		super("元宝招募", CustomConfirmDialog.DEFAULT);
		this.prop = prop;
		this.manorDraft = manorDraft;
		this.max = max;
		desc = (TextView) content.findViewById(R.id.desc);
		cost = (TextView) content.findViewById(R.id.cost);
		amount = (EditText) content.findViewById(R.id.amount);
		amount.addTextChangedListener(this);
		amount.setOnEditorActionListener(this);
		amount.setOnFocusChangeListener(this);
	}

	public void show() {
		setValue();// 设值
		super.show();
	}

	private void setValue() {
		ViewUtil.setText(desc, "主城尚有" + max + "人口可供招募士兵");
		ViewUtil.setText(amount, String.valueOf(max));
		ViewUtil.setText(cost, getCost(max));
		setButton(CustomConfirmDialog.FIRST_BTN, "确  定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				int costCurrency = getCost(getAmount());
				if (Account.user.getCurrency() < costCurrency) {
					dismiss();
					new ToActionTip(costCurrency).show();
				} else {
					new RmbTrainingInvoker().start();
				}
			}
		});

		setButton(CustomConfirmDialog.SECOND_BTN, "放  弃",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});
	}

	private int getCost(int count) {
		return CalcUtil
				.upNum((manorDraft.getCost() / Constants.PER_TEN_THOUSAND)
						* count);
	}

	private int getAmount() {
		return StringUtil.parseInt(amount.getText().toString());
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
			BuildingProp bp = (BuildingProp) CacheMgr.buildingPropCache.get(manorDraft.getBuildingId());
			if (null == Account.manorInfoClient.getBuilding(bp) && CacheMgr.buildingBuyCostCache.isEnough(bp.getId()))
				buildingResp = GameBiz.getInstance().buildingBuy(Constants.TYPE_MATERIAL, bp.getId());
			
			count = getAmount();
			resp = GameBiz.getInstance().manorDraft(manorDraft.getBuildingId(),
					prop.getId(), Constants.MANOR_DRAFT_BY_RMB, count);
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
							+ "名" + prop.getName(), null, false);
			ctr.updateUI(resp.getRi(), true);
			Account.manorInfoClient.update(resp.getMic());

		}
	}

	@Override
	public View getContent() {
		return controller.inflate(layout);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

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
}
