package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.MoneyTrainingInvoker;
import com.vikings.sanguo.invoker.RecoverPopInvoker;
import com.vikings.sanguo.invoker.UpgradeTrainingInvoker;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BuildingBuyCost;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingRequirement;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.CurrencyCallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.RechargeBuyGiftTip;
import com.vikings.sanguo.ui.alert.RmbTrainingAlert;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public class ArmTrainingWindow extends CustomPopupWindow {
	private TroopProp troopProp;
	private ManorDraft manorDraft;
	private BuildingProp prop;
	private BuildingInfoClient bic;

	@Override
	protected void init() {
		super.init("招募士兵");
		setContentAboveTitle(R.layout.user_info_head);
		setContent(R.layout.conscribe_choose);

	}

	public void open(TroopProp troopProp) {
		this.troopProp = troopProp;
		initData(troopProp);
		doOpen();
		setStaticValue(troopProp);
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	private void initData(TroopProp troopProp) {
		List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(troopProp
				.getId());
		if (!ListUtil.isNull(drafts)) {
			try {
				manorDraft = drafts.get(0);
				prop = (BuildingProp) CacheMgr.buildingPropCache.get(manorDraft
						.getBuildingId());
				bic = Account.manorInfoClient.getBuilding(prop);
				if (null != bic) {
					for (int i = 0; i < drafts.size(); i++) {
						if (drafts.get(i).getBuildingId() == bic.getProp()
								.getId()) {
							manorDraft = drafts.get(i);
						}
					}
				}

			} catch (GameException e) {
				e.printStackTrace();
				controller.alert("无法招募该士兵");
				return;
			}
		} else {
			controller.alert("无法招募该士兵");
			return;
		}
	}

	@Override
	public void showUI() {
		setDynValue();
		super.showUI();
		if (null != Account.manorInfoClient.getBuilding(prop)) {
			bic = Account.manorInfoClient.getBuilding(prop);
		}
	}

	// 士兵价格
	private void setStaticValue(final TroopProp troopProp) {
		View iconGroup = findViewById(R.id.iconGroup);
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
		ViewUtil.setText(window, R.id.armName, troopProp.getName());
		ViewUtil.setImage(window, R.id.armicon, troopProp.getSmallIcon());// 兵种图标

		ViewUtil.setRichText(window, R.id.price1,
				"招募" + TroopUtil.getPrice(troopProp.getId(), prop.getId()),
				true);

		ViewUtil.setRichText(window, R.id.price2, "元宝单价: #rmb#"
				+ CacheMgr.manorDraftCache.getCostByCurrencyOne(manorDraft),
				true);
	}

	private void setDynValue() {
		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);
		// 该兵种的数量
		ViewUtil.setText(window, R.id.armCount,
				"" + Account.myLordInfo.getArmCountByType(troopProp.getId()));

		setResourceDraft();
		setCurrencyDraft();
		setRecovery();
		// setUpgradeDraft();
	}

	// 金币急征
	private void setResourceDraft() {
		final float resTrainingRate = manorDraft.getResourceDraftRate() / 100f; // 资源招募百分比
		final int realCountByResource = Account.manorInfoClient
				.getDraftCountByRate(resTrainingRate);
		final List<ShowItem> showItems = CacheMgr.manorDraftResourceCache
				.checkResourceEnough(troopProp, prop, realCountByResource);
		final boolean isResEnough = ListUtil.isNull(showItems);

		String[] str = getResourceDraftDesc(realCountByResource, isResEnough);

		View resGroup = window.findViewById(R.id.goldLayout);
		ViewUtil.setVisible(resGroup, R.id.count);
		ViewUtil.setRichText(resGroup, R.id.count, "数量: #arm#"
				+ realCountByResource, true);

		// 虚弱+刚点击招募后隐藏
		if ((Account.user.getWeakLeftTime() > 0)
				|| (bic != null && bic.getDraftCd() > 0)) {
			ViewUtil.setGone(resGroup, R.id.count);
		}

		ViewUtil.setRichText(resGroup, R.id.titleDesc, str[1], true);
		ViewUtil.setRichText(resGroup, R.id.cost, str[2], true);

		View moneyTrainingBtn = window.findViewById(R.id.goldFrameBtn);
		if (canResDraft(isResEnough)) {
			// 兵力已达上限，主城无可训练人口
			if (realCountByResource <= 0) {
				if (Account.manorInfoClient.getValidPop() <= 0) {
					ViewUtil.setRichText(
							resGroup,
							R.id.count,
							"数量: #arm#"
									+ realCountByResource
									+ StringUtil.color("(兵力已达上限)",
											R.color.color24), true);
				}
				ImageUtil.setBgGray(moneyTrainingBtn);
				moneyTrainingBtn.setOnClickListener(null);
			} else {

				ImageUtil.setBgNormal(moneyTrainingBtn);
				moneyTrainingBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isResEnough) {
							handleResNotEnough(showItems);
							return;
						}
						new MoneyTrainingInvoker(manorDraft, troopProp,
								realCountByResource, cb).start();
					}
				});
			}

		} else {
			ImageUtil.setBgGray(moneyTrainingBtn);
			moneyTrainingBtn.setOnClickListener(null);
		}

	}

	private void handleResNotEnough(final List<ShowItem> showItems) {
		if (AttrData.isResource(showItems.get(0).getType())
				|| AttrData.isMaterial(showItems.get(0).getType()))
			new RechargeBuyGiftTip(showItems.get(0).getType()).show();
		else
			controller.alert(StringUtil.color("你的资源不足，不能招募这些士兵",
					R.color.k7_color5), StringUtil.color(
					"你可以使用【元宝招募】，不消耗资源，一次性招募所有人口成士兵", R.color.k7_color1), null,
					true);
	}

	// Str数组内容
	private String[] getResourceDraftDesc(int count, boolean isResEnough) {
		String[] str = new String[3];

		int weakTime = Account.user.getWeakLeftTime();
		if (weakTime > 0) {
			str[1] = new String("招募当前人口的" + manorDraft.getResourceDraftRate()
					+ "%，" + DateUtil.formatMinute(manorDraft.getCd())
					+ "后招募一次");
			str[2] = new String(StringUtil.color(
					DateUtil.formatMinuteOrSecond(weakTime)
							+ "后可继续招募，使用【元宝急征】征兵没有间隔时间", R.color.k7_color15));
			return str;
		}

		if (bic != null && bic.getDraftCd() > 0) {
			str[1] = new String("招募当前人口的" + manorDraft.getResourceDraftRate()
					+ "%，" + DateUtil.formatMinute(manorDraft.getCd())
					+ "后招募一次");
			str[2] = new String(StringUtil.color(
					DateUtil.formatMinuteOrSecond(bic.getDraftCd())
							+ "后可继续招募，使用【元宝急征】征兵没有间隔时间", R.color.k7_color15));
			return str;
		}

		str[0] = new String("数量: #arm#" + count);
		if (count <= 0) {
			str[1] = new String(StringUtil.color("(主城已无人口)", R.color.color8));
			if (Account.manorInfoClient.getDraftCountByRate(count) <= 0)
				str[1] = StringUtil.color("(兵力已达上限)", R.color.color8);
		} else {
			str[1] = new String("招募当前人口的" + manorDraft.getResourceDraftRate()
					+ "%，" + DateUtil.formatMinute(manorDraft.getCd())
					+ "后招募一次");
		}

		String costByResource = TroopUtil.getCost(troopProp.getId(),
				prop.getId(), count);

		if (!isResEnough)
			str[2] = new String(costByResource
					+ StringUtil.color("(缺资源)", R.color.k7_color15));
		else
			str[2] = new String(costByResource);

		return str;

	}

	private boolean canResDraft(boolean isResEnough) {
		return Account.user.getWeakLeftTime() <= 0
				&& (null == bic || (bic != null && bic.getDraftCd() <= 0))
				&& isResEnough;
	}

	// 元宝急征
	private void setCurrencyDraft() {
		View byCurrencyGroup = window.findViewById(R.id.moneyLayout);
		View currencyTrainingBtn = window.findViewById(R.id.moneyFrameBtn);
		final int realCountByCurrency = Account.manorInfoClient
				.getDraftCountByRate(1f);

		ViewUtil.setRichText(byCurrencyGroup, R.id.count, "数量: #arm#"
				+ realCountByCurrency, true);
		ViewUtil.setVisible(byCurrencyGroup, R.id.titleDesc);
		if (realCountByCurrency <= 0) {// Account.manorInfoClient.getDraftCount(1f)
										// <= 0
			if (Account.manorInfoClient.getValidPop() <= 0) {
				ViewUtil.setRichText(
						byCurrencyGroup,
						R.id.count,
						"数量: #arm#" + realCountByCurrency
								+ StringUtil.color("(兵力已达上限)", R.color.color24),
						true);
			}

			ImageUtil.setBgGray(currencyTrainingBtn);
			currencyTrainingBtn.setOnClickListener(null);
		} else {
			ImageUtil.setBgNormal(currencyTrainingBtn);
			currencyTrainingBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new RmbTrainingAlert(troopProp, manorDraft,
							realCountByCurrency).show();
				}
			});
		}

		ViewUtil.setRichText(
				byCurrencyGroup,
				R.id.cost,
				"总价: #rmb#"
						+ CacheMgr.manorDraftCache.getCostByCurrency(
								manorDraft, realCountByCurrency), true);
	}

	// 恢复人口
	private void setRecovery() {

		View recoverPopGroup = window.findViewById(R.id.recoverLayout);
		View recoverBtn = window.findViewById(R.id.recoverFrameBtn);
		ViewUtil.setText(recoverPopGroup, R.id.titleDesc, "立刻恢复当前人口至上限，提升招兵数量");
		if (Account.manorInfoClient.getValidPop() > 0) {
			int currency = Account.manorInfoClient.getRecoverPopCost();
			ViewUtil.setVisible(recoverPopGroup, R.id.count);
			ViewUtil.setRichText(recoverPopGroup, R.id.count, "当前人口: #ren#"
					+ Account.manorInfoClient.getRealCurPop() + "/"
					+ Account.manorInfoClient.getTotPop(), true);
			ViewUtil.setVisible(recoverPopGroup, R.id.cost);
			ViewUtil.setRichText(recoverPopGroup, R.id.cost, "恢复价格: #rmb#"
					+ currency, true);
			ImageUtil.setBgNormal(recoverBtn);
			recoverBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new CurrencyCallBack(Account.manorInfoClient
							.getRecoverPopCost()) {
						@Override
						public void handle() {
							new RecoverPopInvoker(cb).start();
						}
					}.onCall();
				}
			});
		} else {
			ViewUtil.setGone(recoverPopGroup, R.id.cost);
			ViewUtil.setVisible(recoverPopGroup, R.id.count);
			ViewUtil.setText(recoverPopGroup, R.id.count, "人口已达上限，无需恢复");
			// ViewUtil.setGone(recoverPopGroup, R.id.costDesc);
			ImageUtil.setBgGray(recoverBtn);
			recoverBtn.setOnClickListener(null);
		}
	}

	// 招募升级
	private void setUpgradeDraft() {

		View upgradeGroup = window.findViewById(R.id.upgradeLayout);
		View upgradeTrainingBtn = window.findViewById(R.id.upgradeFrameBtn);

		ViewUtil.setVisible(upgradeGroup, R.id.titleDesc);

		if (bic != null) {
			final BuildingProp next = bic.getProp().getNextLevel();
			if (null != next) {

				ViewUtil.setRichText(upgradeGroup, R.id.count,
						"效果：" + next.getDesc(), true);
				ImageUtil.setBgNormal(upgradeTrainingBtn);
				ViewUtil.setVisible(upgradeGroup, R.id.cost);
				ViewUtil.setRichText(upgradeGroup, R.id.cost, "消耗："
						+ getCost(next), true);

				String reqStr = getRequirement(next.getId());
				if (StringUtil.isNull(reqStr)) {
					ViewUtil.setGone(upgradeGroup, R.id.limit);
				} else {
					ViewUtil.setVisible(upgradeGroup, R.id.limit);
					ViewUtil.setRichText(upgradeGroup, R.id.limit, "条件："
							+ reqStr, true);
				}

				upgradeTrainingBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						manorDraft = CacheMgr.manorDraftCache
								.getSingleDraftByBuildingId(next.getId());

						new UpgradeTrainingInvoker(Constants.TYPE_MATERIAL,
								manorDraft, next, cb).start();
					}
				});
			} else {
				ViewUtil.setRichText(upgradeGroup, R.id.titleDesc,
						StringUtil.color("已达到最高级", R.color.color8));
				ViewUtil.setRichText(upgradeGroup, R.id.count, "效果: "
						+ getBuildingDesc(bic.getProp()), true);
				ViewUtil.setGone(upgradeGroup, R.id.cost);
				ImageUtil.setBgGray(upgradeTrainingBtn);
				upgradeTrainingBtn.setOnClickListener(null);
				ViewUtil.setGone(upgradeGroup, R.id.limit);
			}
		} else {
			ViewUtil.setVisible(upgradeGroup, R.id.cost);
			ViewUtil.setRichText(upgradeGroup, R.id.cost,
					"消耗：" + getCost(prop), true);

			String reqStr = getRequirement(prop.getId());
			if (StringUtil.isNull(reqStr)) {
				ViewUtil.setGone(upgradeGroup, R.id.limit);
			} else {
				ViewUtil.setVisible(upgradeGroup, R.id.limit);
				ViewUtil.setRichText(upgradeGroup, R.id.limit, "条件：" + reqStr,
						true);
			}
			ImageUtil.setBgGray(upgradeTrainingBtn);
			upgradeTrainingBtn.setOnClickListener(null);
		}
	}

	@SuppressWarnings("unchecked")
	public static String getRequirement(int id) {
		StringBuffer buf = new StringBuffer();
		List<BuildingRequirement> list = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement br : list) {
			if (br.getType() == BuildingRequirement.TYPE_RESOURCES
					&& br.getValue() == AttrType.ATTR_TYPE_LEVEL.getNumber()) {
				if (Account.user.getLevel() < br.getExtension()) {
					buf.append(StringUtil.color(
							"需玩家" + br.getExtension() + "级", R.color.color11));
					break;
				}
			}
		}
		return buf.toString();
	}

	// 效果描述
	protected String getBuildingDesc(BuildingProp buildingProp) {
		return buildingProp.getDesc();
	}

	@SuppressWarnings("unchecked")
	protected ReturnInfoClient addCfg(int buildingId) {
		ReturnInfoClient ric = new ReturnInfoClient();
		List<BuildingBuyCost> list = CacheMgr.buildingBuyCostCache
				.search(buildingId);
		for (BuildingBuyCost cost : list) {
			ric.addCfg(cost.getCostType(), cost.getCostId(), cost.getCount());
		}
		return ric;
	}

	// 消耗计算
	private String getCost(BuildingProp prop) {
		// 如果已经满级，不显示消耗
		if (prop == null) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		ReturnInfoClient ric = addCfg(prop.getId());
		List<ShowItem> showItems = ric.showRequire();
		// 显示需求
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

	private CallBack cb = new CallBack() {
		@Override
		public void onCall() {
			setDynValue();
		}
	};
}
