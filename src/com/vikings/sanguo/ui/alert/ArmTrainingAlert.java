package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BuildingBuyCost;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ArmTrainingAlert extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_buyarm;
	private TextView desc;
	private Button buyBtn;
	private BuildingProp buiprop;
	private BuildingInfoClient bic;
	private CallBack mBack;
	private ManorDraft manorDraft;
	private TroopProp prop;

	public ArmTrainingAlert(BuildingProp buiprop, TroopProp prop, CallBack mBack) {
		super("解锁招募" + prop.getName());
		this.buiprop = buiprop;
		this.prop = prop;
		this.mBack = mBack;
	}

	public void show() {
		super.show();
		desc = (TextView) content.findViewById(R.id.desc);
		buyBtn = (Button) content.findViewById(R.id.buyBt);
		setValue();
	}

	private void setValue() {
		ViewUtil.setRichText(
				desc,
				"只需#rmb#" + StringUtil.color(buiprop.getPrice() + "", "red")
						+ "立刻招募"
						+ StringUtil.color("[" + prop.getName() + "]", "red")
						+ ",冲锋突袭，运筹帷幄，尽在你的手中！");

		List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(prop.getId());
		if (!ListUtil.isNull(drafts)) {
			try {
				manorDraft = drafts.get(0);
				buiprop = (BuildingProp) CacheMgr.buildingPropCache
						.get(manorDraft.getBuildingId());
				bic = Account.manorInfoClient.getBuilding(buiprop);
			} catch (GameException e) {
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}

		buyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (buiprop.isCurrencyBuy()) {// 允许元宝购买
					if (checkCondition(Constants.TYPE_CURRENCY, buiprop,
							manorDraft)) {// 未解锁情况下元宝购买
						new BuildingBuyImplInvoker(buiprop, prop).start();
					}
				}
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout);
	}

	// 建筑创建资源条件
	protected boolean checkCondition(int type, BuildingProp buildprop,
			ManorDraft manorDraft) {
		boolean isBuy = true;
		boolean checkLevel = true;

		if (type == Constants.TYPE_CURRENCY) {
			isBuy = buildprop.isCurrencyBuy();
		}

		// 检查前提条件
		if (!isBuy) {
			return false;
		} else {
			checkLevel = buildprop.isCheckLv();
		}

		if (!StringUtil.isNull(BuildingRequirementCache.checkRequirement(
				buildprop.getId(), checkLevel))) {
			return false;
		}
		if (type == Constants.TYPE_CURRENCY) {
			if (Account.user.getCurrency() < buildprop.getPrice()) {
				new ToActionTip(buildprop.getPrice()).show();
				return false;
			}
		} else {
			ReturnInfoClient ric = addCfg(manorDraft);
			List<ShowItem> showItems = ric.checkRequire();
			if (!showItems.isEmpty()) {
				if (AttrData.isResource(showItems.get(0).getType())
						|| AttrData.isMaterial(showItems.get(0).getType())) {
					new RechargeBuyGiftTip(showItems.get(0).getType()).show();
				}
				return false;
			}

		}
		return true;

	}

	@SuppressWarnings("unchecked")
	protected ReturnInfoClient addCfg(ManorDraft manorDraft) {
		ReturnInfoClient ric = new ReturnInfoClient();
		List<BuildingBuyCost> list = CacheMgr.buildingBuyCostCache
				.search(manorDraft.getBuildingId());
		for (BuildingBuyCost cost : list) {
			ric.addCfg(cost.getCostType(), cost.getCostId(), cost.getCount());
		}
		return ric;
	}

	public class BuildingBuyImplInvoker extends BaseInvoker {

		protected BuildingProp bprop;
		protected BuildingBuyResp resp;
		protected TroopProp prop;

		public BuildingBuyImplInvoker(BuildingProp bprop, TroopProp prop) {
			this.bprop = bprop;
			this.prop = prop;
		}

		@Override
		protected String loadingMsg() {
			return "";
		}

		@Override
		protected String failMsg() {
			return "解锁失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().buildingBuy(Constants.TYPE_CURRENCY,
					bprop.getId());
			Account.manorInfoClient.update(resp.getMic());
		}

		@Override
		protected void onOK() {
			if (mBack != null) {
				mBack.onCall();
			}
			dismiss();
			controller.alert("恭喜你解锁了[" + bprop.getBuildingName() + "]");
		}

	}
}
