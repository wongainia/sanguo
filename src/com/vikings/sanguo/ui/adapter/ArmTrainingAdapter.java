package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.AttrData;
import com.vikings.sanguo.model.BuildingBuyCost;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingRequirement;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ItemBag;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.ArmTrainingAlert;
import com.vikings.sanguo.ui.alert.RechargeBuyGiftTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.ui.window.ArmTrainingWindow;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArmTrainingAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.arm_training_item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.armicon = (ImageView) convertView.findViewById(R.id.armicon);
			holder.stateIcon = (ImageView) convertView
					.findViewById(R.id.stateIcon);
			holder.blackIcon = (ImageView) convertView
					.findViewById(R.id.blackIcon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.buyBtn = (Button) convertView.findViewById(R.id.buyBtn);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.draftCd = (TextView) convertView.findViewById(R.id.draftCd);
			holder.draftCdDesc = (TextView) convertView
					.findViewById(R.id.draftCdDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void setViewDisplay(View v, Object o, int index) {
		final TroopProp prop = (TroopProp) getItem(index);

		List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(prop.getId());
		BuildingProp buiprop = null;
		BuildingInfoClient bic = null;
		ManorDraft manorDraft = null;
		if (ListUtil.isNull(drafts))
			return;

		try {
			manorDraft = drafts.get(0);
			buiprop = (BuildingProp) CacheMgr.buildingPropCache.get(manorDraft
					.getBuildingId());
			bic = Account.manorInfoClient.getBuilding(buiprop);
		} catch (GameException e) {
			e.printStackTrace();
			return;
		}

		ViewHolder holder = (ViewHolder) v.getTag();

		ViewUtil.setText(holder.name, prop.getName());// 兵种名字

		ViewUtil.setImage(holder.armicon, prop.getSmallIcon());// 兵种图标
		ViewUtil.setVisible(holder.count);
		ViewUtil.setRichText(holder.count,
				"" + Account.myLordInfo.getArmCountByType(prop.getId()), true);
		holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(prop,
						Account.getUserTroopEffectInfo(prop.getId()));
			}
		});

		ViewUtil.setGone(holder.buyBtn);
		holder.buyBtn.setTag(index);

		new ViewImgScaleCallBack(prop.getIcon(), holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		if (null != bic) {// 建筑是否存在
			ViewUtil.setText(holder.desc, prop.getDesc());
			ViewUtil.setGone(holder.blackIcon, R.id.blackIcon);
			ViewUtil.setGone(holder.stateIcon, R.id.stateIcon);

			if (bic.getDraftCd() > 0) {
				ViewUtil.setVisible(holder.draftCd);
				ViewUtil.setVisible(holder.draftCdDesc);
				ViewUtil.setText(holder.draftCdDesc, "冷却中，[急征]可立即招募");
			} else {
				ViewUtil.setGone(holder.draftCd);
				ViewUtil.setGone(holder.draftCdDesc);
			}

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new ArmTrainingWindow().open(prop);
				}

			});
		} else {
			ViewUtil.setGone(holder.draftCd);
			ViewUtil.setGone(holder.draftCdDesc);
			ViewUtil.setVisible(holder.stateIcon, R.id.stateIcon);
			ViewUtil.setGone(holder.count);

			ViewUtil.setRichText(holder.desc,
					"获得条件: " + getRequirement(buiprop.getId()));

			ViewUtil.setVisible(holder.blackIcon);
			ViewUtil.setImage(holder.stateIcon, R.drawable.building_state_wjs1);
			v.setOnClickListener(null);

			if (needVipUnlock(buiprop.getId())) {
				ViewUtil.setVisible(holder.blackIcon);
				ViewUtil.setImage(holder.stateIcon,
						R.drawable.building_state_wjs1);
				ViewUtil.setVisible(holder.buyBtn);
				holder.buyBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Config.getController().openVipListWindow();
					}
				});
			} else {
				if (unlock(buiprop.getId(), false)) {
					if (buiprop.isCurrencyBuy()) {// 允许元宝购买
						ViewUtil.setVisible(holder.blackIcon);
						ViewUtil.setImage(holder.stateIcon,
								R.drawable.building_state_wjs1);
						if (buiprop.isNoCheckLv()) {// 允许元宝突破

							ViewUtil.setRichText(holder.desc, "获得条件: "
									+ getRequirement(buiprop.getId())
									+ "<br/>或花费#rmb#" + buiprop.getPrice()
									+ " 立即解锁");

							ViewUtil.setVisible(holder.buyBtn);
							if (null != manorDraft) {
								holder.buyBtn
										.setOnClickListener(new ClickListener(
												prop, buiprop, manorDraft, v,
												holder));
							} else {
								holder.buyBtn.setOnClickListener(null);
							}
						}

					}

				}
			}
		}
	}

	// 建筑是否解锁
	@SuppressWarnings("unchecked")
	public static boolean unlock(int id, boolean isMaterial) {
		List<BuildingRequirement> requirements = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement requirement : requirements) {
			if (requirement.getType() == BuildingRequirement.TYPE_BUILDING) {
				boolean has = false;
				for (BuildingInfoClient bic : Account.manorInfoClient
						.getBuildingInfos()) {
					if (bic.getProp().getType() == requirement.getValue()
							&& bic.getProp().getLevel() >= requirement
									.getExtension()) {
						has = true;
						break;
					}
				}
				if (!has) {
					return false;
				}

			} else if (requirement.getType() == BuildingRequirement.TYPE_VIPS) {
				if (requirement.getValue() > Account.getCurVip().getLevel()) {
					return false;
				}
			} else if (isMaterial) {

				if (requirement.getType() == BuildingRequirement.TYPE_RESOURCES) {
					if (requirement.getValue() == AttrType.ATTR_TYPE_LEVEL
							.getNumber()) {
						if (Account.user.getLevel() < requirement
								.getExtension()) {
							return false;
						}
					}
				} else if (requirement.getType() == BuildingRequirement.TYPE_DUPLICATE) {// 根据副本解锁建筑
					ActInfoClient aic = Account.actInfoCache.getAct(requirement
							.getValue());
					if (!aic.isComplete()) {
						return false;
					}
				}
			}

		}
		return true;

	}

	private boolean needVipUnlock(int id) {
		List<BuildingRequirement> requirements = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement requirement : requirements) {
			if (requirement.getType() == BuildingRequirement.TYPE_VIPS
					&& requirement.getValue() > Account.getCurVip().getLevel()) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static String getRequirement(int id) {
		StringBuffer buf = new StringBuffer();
		List<BuildingRequirement> list = CacheMgr.buildingRequireMentCache
				.search(id);
		for (BuildingRequirement br : list) {
			switch (br.getType()) {
			case BuildingRequirement.TYPE_RESOURCES:
				if (br.getValue() == AttrType.ATTR_TYPE_EXPLOIT.getNumber()) {
					buf.append(
							Account.user.getExploit() >= br.getExtension() ? "功勋"
									+ br.getExtension()
									: StringUtil.color(
											"功勋" + br.getExtension(), "red"))
							.append("、");
				} else if (br.getValue() == AttrType.ATTR_TYPE_SCORE
						.getNumber()) {
					buf.append(
							Account.user.getScore() >= br.getExtension() ? "成就"
									+ br.getExtension() : StringUtil.color("成就"
									+ br.getExtension(), "red")).append("、");
				} else if (br.getValue() == AttrType.ATTR_TYPE_LEVEL
						.getNumber()) {
					buf.append(
							Account.user.getLevel() >= br.getExtension() ? "等级达到"
									+ br.getExtension() + "级"
									: StringUtil.color(
											"等级达到" + br.getExtension() + "级",
											"red")).append("、");
				}
				break;
			case BuildingRequirement.TYPE_BUILDING:
				BuildingProp prop = CacheMgr.buildingPropCache
						.getBuildingByTypeAndLevel(br.getValue(),
								(short) br.getExtension());
				if (null != prop) {
					BuildingInfoClient bic = Account.manorInfoClient
							.getBuilding(prop);
					if (null != bic
							&& bic.getProp().getLevel() >= prop.getLevel()) {
						buf.append(
								prop.getBuildingName() + "Lv" + prop.getLevel())
								.append("、");
					} else {
						buf.append(
								StringUtil.color(prop.getBuildingName() + "Lv"
										+ prop.getLevel(), "red")).append("、");
					}
				}
				break;
			case BuildingRequirement.TYPE_TOOLS:
				Item item = CacheMgr.getItemByID(br.getValue());
				if (null != item) {
					ItemBag itemBag = Account.store.getItemBag(item);
					if (null != itemBag
							&& itemBag.getCount() >= br.getExtension()) {
						buf.append(item.getName() + br.getExtension() + "个")
								.append("、");
					} else {
						buf.append(
								StringUtil.color(
										item.getName() + br.getExtension()
												+ "个", "red")).append("、");
					}
				}
				break;
			case BuildingRequirement.TYPE_VIPS:// 根据vip等级解锁相应建筑
				buf.append(
						br.getValue() <= Account.getCurVip().getLevel() ? "VIP"
								+ br.getValue() + "尊享兵种" : StringUtil.color(
								"VIP" + br.getValue() + "尊享兵种", "red")).append(
						"、");
				break;
			case BuildingRequirement.TYPE_DUPLICATE:// 根据副本解锁相应建筑
				ActInfoClient aic = Account.actInfoCache.getAct(br.getValue());
				buf.append(
						aic.isComplete() ? "通关战役"
								+ aic.getPropAct().getSubName() : StringUtil
								.color("通关战役" + aic.getPropAct().getSubName(),
										"red")).append("、");
				break;

			default:
				break;
			}
		}
		int index = buf.lastIndexOf("、");
		if (index >= 0)
			buf.deleteCharAt(index);
		return buf.toString();
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

	// // 建筑购买
	// protected void getBuildingBuy(TroopProp prop, BuildingProp buildprop,
	// ManorDraft manorDraft, CallBack mBack) {
	// if (unlock(buildprop.getId(), true)) {
	// if (checkCondition(Constants.TYPE_MATERIAL, buildprop, manorDraft)) {//
	// 解锁情况下普通购买
	// new BuildingBuyImplInvoker(Constants.TYPE_MATERIAL, buildprop,
	// prop, mBack).start();
	// }
	// }
	// }

	// 元宝购买
	private void getCurrencyBuy(TroopProp prop, BuildingProp buildprop,
			ManorDraft manorDraft, CallBack mBack) {
		new ArmTrainingAlert(buildprop, prop, mBack).show();
	}

	// 购买点击事件
	private class ClickListener implements OnClickListener {
		private TroopProp troopProp;
		private BuildingProp buildingProp;
		private ManorDraft manorDraft;
		private View view;
		private ViewHolder holder;

		public ClickListener(TroopProp troopProp, BuildingProp buildingProp,
				ManorDraft manorDraft, View view, ViewHolder holder) {
			super();
			this.troopProp = troopProp;
			this.buildingProp = buildingProp;
			this.manorDraft = manorDraft;
			this.holder = holder;
			this.view = view;
		}

		@Override
		public void onClick(View v) {

			if (null == Account.manorInfoClient.getBuilding(buildingProp)
					|| !Account.manorInfoClient.getArmids().contains(
							troopProp.getId())) {
				if (Account.user.getCurrency() < buildingProp.getPrice()) {
					new ToActionTip(buildingProp.getPrice()).show();
					return;
				}
				getCurrencyBuy(troopProp, buildingProp, manorDraft,
						new SetItemCallback(view, holder, troopProp));
			}
		}

	}

	// item点击操作后修改item的值
	private class SetItemCallback implements CallBack {
		private ViewHolder holder;
		private TroopProp prop;
		private View v;

		public SetItemCallback(View v, ViewHolder holder, TroopProp prop) {
			super();
			this.holder = holder;
			this.v = v;
			this.prop = prop;
		}

		@Override
		public void onCall() {
			if (holder != null) {
				holder.buyBtn.setVisibility(View.GONE);
				holder.stateIcon.setVisibility(View.GONE);
				holder.blackIcon.setVisibility(View.GONE);
				holder.count.setVisibility(View.VISIBLE);
				ViewUtil.setText(holder.desc, "特性: " + prop.getDesc());
			}
			if (null != v) {
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new ArmTrainingWindow().open(prop);
					}

				});
			}
		}

	}

	// 购买操作成功跳转到相应兵种主页
	// public static class BuildingBuyImplInvoker extends BaseInvoker {
	//
	// private int type;
	// protected BuildingProp bprop;
	// protected BuildingBuyResp resp;
	// protected TroopProp prop;
	// private CallBack mBack;
	//
	// public BuildingBuyImplInvoker(int type, BuildingProp bprop,
	// TroopProp prop, CallBack mBack) {
	// this.type = type;
	// this.bprop = bprop;
	// this.prop = prop;
	// this.mBack = mBack;
	// }
	//
	// @Override
	// protected String loadingMsg() {
	// return "请稍后...";
	// }
	//
	// @Override
	// protected String failMsg() {
	// return "失败";
	// }
	//
	// @Override
	// protected void fire() throws GameException {
	// resp = GameBiz.getInstance().buildingBuy(type, bprop.getId());
	// Account.manorInfoClient.update(resp.getMic());
	// }
	//
	// @Override
	// protected void onOK() {
	// if (mBack != null) {
	// mBack.onCall();
	// }
	// if (BuildingProp.BUILDING_TYPE_GUARDIAN != bprop.getId()) {
	// new ArmTrainingWindow().open(prop);
	// }
	// }
	//
	// }

	private static class ViewHolder {
		Button buyBtn;
		ImageView icon, stateIcon, armicon, blackIcon;
		TextView name, desc, count, draftCd, draftCdDesc;
	}

}