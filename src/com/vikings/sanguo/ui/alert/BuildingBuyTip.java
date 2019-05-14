package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.BuildingBuyInvoker;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BuildingBuyCost;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class BuildingBuyTip extends CustomConfirmDialog {

	private static final int layout = R.layout.alert_buy_building;

	protected int TYPE_MATERIAL = 1;
	protected int TYPE_CURRENCY = 2;
	private Button upgradeBtn, rmbBtn;
	private TextView levelnow, levelnext, desc, upgradeEffect,
			preBuildingRequired, cost;// level,
	private ImageView upIcon;

	protected BuildingProp buildingProp;
	protected BuildingInfoClient bic;

	public BuildingBuyTip(BuildingProp prop) {
		super(prop.getBuildingName());
		this.buildingProp = prop;
	}

	protected OnClickListener getListener(final int type) {

		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkCondition(type))
					new BuildingBuyImplInvoker(type, buildingProp).start();
			}
		};

	}

	protected boolean checkCondition(int type) {

		boolean isBuy = true;
		boolean checkLevel = true;
		if (type == TYPE_CURRENCY) {
			isBuy = buildingProp.isCurrencyBuy();
		}

		if (!isBuy) {
			return false;
		} else {
			checkLevel = buildingProp.isCheckLv();
		}

		// 检查前提条件
		if (!StringUtil.isNull(BuildingRequirementCache.checkRequirement(
				buildingProp.getId(), checkLevel))) {
			dismiss();
			controller.alert("条件不足，不能建造");
			return false;
		}
		if (type == TYPE_CURRENCY) {
			if (Account.user.getCurrency() < buildingProp.getPrice()) {
				dismiss();
				new ToActionTip(buildingProp.getPrice()).show();
				return false;
			}
		} else {
			ReturnInfoClient ric = addCfg();
			List<ShowItem> showItems = ric.checkRequire();
			if (!showItems.isEmpty()) {
				dismiss();
				if (AttrData.isResource(showItems.get(0).getType())
						|| AttrData.isMaterial(showItems.get(0).getType()))
					new RechargeBuyGiftTip(showItems.get(0).getType()).show();
				else
					controller.alert("资源不足，请先积累资源<br><br>"
							+ StringUtil.color("[奖励]中的[幸运大轮盘]可快速获得大量资源",
									R.color.k7_color5));
				return false;
			}

		}
		return true;

	}

	public void show() {
		super.show();
		levelnow = (TextView) content.findViewById(R.id.nowlevel);// 当前等级
		levelnext = (TextView) content.findViewById(R.id.nextlevel);// 下一等级
		desc = (TextView) content.findViewById(R.id.desc);// 最高等级时显示的效果
		upIcon = (ImageView) content.findViewById(R.id.upIcon);// 升级图标
		upgradeEffect = (TextView) content.findViewById(R.id.upgradeEffect);// 下级效果
		preBuildingRequired = (TextView) content
				.findViewById(R.id.preBuildingRequired);// 升级条件
		cost = (TextView) content.findViewById(R.id.cost);// 消耗
		upgradeBtn = (Button) content.findViewById(R.id.upgradeBtn);// 金币升级
		rmbBtn = (Button) content.findViewById(R.id.rmbBtn);// 元宝升级
		setValue();// 设值
	}

	protected void setValue() {
		bic = getBic();
		new ViewImgScaleCallBack(buildingProp.getImage(),
				content.findViewById(R.id.buildingIcon),
				280 * Config.SCALE_FROM_HIGH, 160 * Config.SCALE_FROM_HIGH);

		String costStr = getCost(); // 消耗

		if (StringUtil.isNull(costStr)) {
			ViewUtil.setGone(cost, R.id.cost);
		} else {
			ViewUtil.setVisible(cost, R.id.cost);
			ViewUtil.setRichText(cost, R.id.cost, costStr, true);
		}

		setRequireMent();

		LayoutParams params = (LayoutParams) levelnext.getLayoutParams();
		LayoutParams paramsnow = (LayoutParams) levelnow.getLayoutParams();

		if (null == bic) { // 购买

			ViewUtil.setRichText(upgradeEffect, R.id.upgradeEffect, "建筑效果："
					+ getBuildingDesc(buildingProp), true);
			if (buildingProp.useMaterial()) {
				if (BuildingRequirementCache.unlockByResource(buildingProp
						.getId())) {

					ViewUtil.setRichText(upgradeBtn, R.id.upgradeBtn, "金币购买",
							true);
					upgradeBtn.setOnClickListener(getListener(TYPE_MATERIAL));

				} else {

					ViewUtil.setRichText(upgradeBtn, R.id.upgradeBtn, "金币购买",
							true);
					ViewUtil.setVisible(upgradeBtn);
					ImageUtil.setBgGray(upgradeBtn);
					upgradeBtn.setOnClickListener(null);

				}
			} else {
				ViewUtil.setGone(upgradeBtn, R.id.upgradeBtn);
			}

			if (buildingProp.getPrice() <= 0) { // 元宝购买
				if (BuildingRequirementCache.unlockByCurrency(
						buildingProp.getId(), buildingProp.isCheckLv())) {
					ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝购买#rmb#"
							+ buildingProp.getPrice(), true);
					rmbBtn.setOnClickListener(getListener(TYPE_CURRENCY));
				} else {
					ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝购买#rmb#"
							+ buildingProp.getPrice(), true);
					ViewUtil.setVisible(rmbBtn);
					ImageUtil.setBgGray(rmbBtn);
					rmbBtn.setOnClickListener(null);
				}
			} else {
				ViewUtil.setGone(rmbBtn);
			}

			params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			params.rightMargin = (int) (11 * Config.SCALE_FROM_HIGH);
			ViewUtil.setRichText(levelnow, "LV" + buildingProp.getLevel());
			if (buildingProp.getNextLevel() != null) {
				ViewUtil.setRichText(levelnext, "LV"
						+ buildingProp.getNextLevel().getLevel());
			}

		} else { // 升级

			// 最高级
			if (buildingProp.getLevel() == bic.getProp().getLevel()) {
				params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
				params.rightMargin = (int) (11 * Config.SCALE_FROM_HIGH);
				// 等级显示
				ViewUtil.setVisible(levelnext, R.id.nextlevel);
				ViewUtil.setRichText(levelnext, R.id.nextlevel, "LV"
						+ buildingProp.getLevel());

				// 当前等级
				ViewUtil.setVisible(levelnow, R.id.nowlevel);
				ViewUtil.setGone(levelnow, R.id.nowlevel);

				// 最高级描述
				ViewUtil.setVisible(desc, R.id.desc);
				ViewUtil.setRichText(desc, R.id.desc,
						getBuildingDesc(buildingProp), true);

				ViewUtil.setGone(upgradeEffect, R.id.upgradeEffect);

				ViewUtil.setVisible(cost, R.id.cost);
				ViewUtil.setRichText(cost, R.id.cost,
						StringUtil.color("已达到最高级", R.color.k7_color7));
				ViewUtil.setGone(upIcon);
				ViewUtil.setGone(upgradeBtn, R.id.upgradeBtn);
				ViewUtil.setGone(rmbBtn, R.id.rmbBtn);
			} else {
				// 未达到最高级
				ViewUtil.setVisible(upIcon, R.id.upIcon);

				// 当前等级
				paramsnow.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
				paramsnow.leftMargin = (int) (18 * Config.SCALE_FROM_HIGH);

				// 等级显示
				ViewUtil.setRichText(levelnow, "LV"
						+ (buildingProp.getLevel() - 1));

				// 下一等级
				params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
				params.rightMargin = (int) (14 * Config.SCALE_FROM_HIGH);
				ViewUtil.setRichText(levelnext, "LV" + buildingProp.getLevel());

				ViewUtil.setRichText(content, R.id.upgradeEffect, "下级效果："
						+ getBuildingDesc(buildingProp), true);

				// 判断是否资源升级满足条件
				if (buildingProp.useMaterial()) {
					if (BuildingRequirementCache.unlockByResource(buildingProp
							.getId())) {

						ViewUtil.setRichText(upgradeBtn, R.id.upgradeBtn,
								"金币升级", true);
						upgradeBtn
								.setOnClickListener(getListener(TYPE_MATERIAL));

					} else {

						ViewUtil.setRichText(upgradeBtn, R.id.upgradeBtn,
								"金币升级", true);
						ViewUtil.disableButton(upgradeBtn);
					}
				} else {
					ViewUtil.setGone(upgradeBtn, R.id.upgradeBtn);
				}

				// 判断元宝等级条件是否满足
				if (buildingProp.getPrice() > 0) {
					if (BuildingRequirementCache.unlockByCurrency(
							buildingProp.getId(), buildingProp.isCheckLv())) {
						ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝升级#rmb#"
								+ buildingProp.getPrice(), true);
						rmbBtn.setOnClickListener(getListener(TYPE_CURRENCY));
					} else {
						ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝升级#rmb#"
								+ buildingProp.getPrice(), true);
						ViewUtil.disableButton(rmbBtn);
					}

				} else {
					ViewUtil.setGone(rmbBtn);
				}
			}
		}

		if (null != upgradeBtn) {
			ViewUtil.setRichText(upgradeBtn, R.id.upgradeBtn, "金币升级", true);
			if (Account.user.isWeak()) {
				ViewUtil.disableButton(upgradeBtn);
			} else if (!BuildingRequirementCache.unlockByResource(buildingProp
					.getId())) {
				// 未解锁

				ViewUtil.disableButton(upgradeBtn);
			} else {
				ViewUtil.enableButton(upgradeBtn);
			}
		}

		if (null != rmbBtn) {
			if (Account.user.isWeak()) {
				ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝升级", true);
				ViewUtil.disableButton(rmbBtn);
			} else if (!BuildingRequirementCache.unlockByCurrency(
					buildingProp.getId(), buildingProp.isCheckLv())) {

				ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝升级", true);
				ViewUtil.disableButton(rmbBtn);
			} else {
				ViewUtil.setRichText(rmbBtn, R.id.rmbBtn, "元宝升级#rmb#"
						+ buildingProp.getPrice(), true);
				ViewUtil.enableButton(rmbBtn);
			}
		}

		if (buildingProp.getLevel() <= 1 && null == buildingProp.getNextLevel()) {
			ViewUtil.setGone(levelnow);
			ViewUtil.setGone(levelnext);
		}

	}

	// 建筑描述
	protected String getBuildingDesc(BuildingProp buildingProp) {
		return buildingProp.getDesc();
	}

	protected BuildingInfoClient getBic() {
		return Account.manorInfoClient.getBuilding(buildingProp);
	}

	// 设前提条件
	private void setRequireMent() {
		if (null != bic && bic.getProp().getLevel() == buildingProp.getLevel()) {
			ViewUtil.setGone(preBuildingRequired, R.id.preBuildingRequired);
		} else {
			String requirementStr = BuildingRequirementCache
					.getRequirement(buildingProp.getId());
			if (StringUtil.isNull(requirementStr)) {
				ViewUtil.setGone(preBuildingRequired, R.id.preBuildingRequired);
			} else {
				if (null == bic) {
					ViewUtil.setRichText(preBuildingRequired,
							R.id.preBuildingRequired, "建造条件：" + requirementStr,
							true);
				} else {
					ViewUtil.setRichText(preBuildingRequired,
							R.id.preBuildingRequired, "升级条件：" + requirementStr,
							true);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected ReturnInfoClient addCfg() {
		ReturnInfoClient ric = new ReturnInfoClient();
		List<BuildingBuyCost> list = CacheMgr.buildingBuyCostCache
				.search(buildingProp.getId());
		for (BuildingBuyCost cost : list) {
			ric.addCfg(cost.getCostType(), cost.getCostId(), cost.getCount());
		}
		return ric;
	}

	private String getCost() {
		// 如果已经满级，不显示消耗
		if (null != bic && bic.getProp().getLevel() == buildingProp.getLevel()) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		ReturnInfoClient ric = addCfg();
		List<ShowItem> showItems = ric.showRequire();
		for (ShowItem showItem : showItems) {
			buf.append("#" + showItem.getImg() + "#")
					.append(showItem.getName() + ":")
					.append(showItem.getCount() + "    ")
					.append("("
							+ (showItem.isEnough() ? showItem.getSelfCount()
									: StringUtil.color(String.valueOf(showItem
											.getSelfCount()), "red")) + ")")
					.append("<br/>");
		}
		int index = buf.lastIndexOf("<br/>");
		if (index >= 0)
			buf.delete(index, index + "<br/>".length());
		return buf.toString();
	}

	@Override
	public View getContent() {
		return controller.inflate(layout);
	}

	private class BuildingBuyImplInvoker extends BuildingBuyInvoker {

		public BuildingBuyImplInvoker(int type, BuildingProp prop) {
			super(type, prop);
		}

		@Override
		protected void onOK() {
			if (buildingProp.getLevel() == 1) {
				dismiss();
				controller.closeAllPopup();
				controller
						.getCastleWindow()
						.getCastleUI()
						.firstBuy(
								Account.manorInfoClient
										.getBuilding(buildingProp),
								new CallBack() {
									@Override
									public void onCall() {
										BuildingBuyImplInvoker.super.onOK();
									}
								});
			} else {
				super.onOK();
				if (prop.getNextLevel() != null)
					buildingProp = prop.getNextLevel();
				else
					buildingProp = prop;
				setValue();
			}
		}
	}

}
