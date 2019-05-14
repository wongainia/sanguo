package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
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
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.RechargeBuyGiftTip;
import com.vikings.sanguo.ui.alert.ToActionTip;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.ui.window.ArmEnhanceWindow;
import com.vikings.sanguo.ui.window.ArmTrainingWindow;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArmEnhanceAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.arm_training_item;
	}

	// 兵种是否解锁
	public static boolean armUnLock(int armId) {
		try {
			List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(armId);
			if (ListUtil.isNull(drafts))
				return true;
			BuildingProp bp = (BuildingProp) CacheMgr.buildingPropCache
					.get(drafts.get(0).getBuildingId());
			BuildingInfoClient bic = Account.manorInfoClient.getBuilding(bp);

			if (bic == null && !ArmTrainingAdapter.unlock(bp.getId(), true)) {
				return false;
			}
		} catch (GameException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.blackIcon = (ImageView) convertView
					.findViewById(R.id.blackIcon);
			holder.armicon = (ImageView) convertView.findViewById(R.id.armicon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.stateIcon = (ImageView) convertView
					.findViewById(R.id.stateIcon);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@SuppressWarnings("unused")
	@Override
	public void setViewDisplay(View v, Object o, int index) {
		final TroopProp prop = (TroopProp) getItem(index);

		List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(prop.getId());
		BuildingProp buiprop = null;
		BuildingInfoClient bic = null;
		ManorDraft manorDraft = null;
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

		ViewHolder holder = (ViewHolder) v.getTag();

		ViewUtil.setText(holder.name, prop.getName());
		ViewUtil.setImage(holder.armicon, prop.getSmallIcon());
		ViewUtil.setVisible(holder.stateIcon, R.id.stateIcon);
		ViewUtil.setGone(holder.count);
		holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new TroopDetailTip().show(prop,
						Account.getUserTroopEffectInfo(prop.getId()));
			}
		});

		new ViewImgScaleCallBack(prop.getIcon(), holder.icon,
				Constants.ARM_ICON_WIDTH * Config.SCALE_FROM_HIGH,
				Constants.ARM_ICON_HEIGHT * Config.SCALE_FROM_HIGH);

		if (null != bic) {// 建筑是否存在
			ViewUtil.setText(holder.desc, "特性：" + prop.getDesc());
			ViewUtil.setGone(holder.blackIcon, R.id.blackIcon);
			ViewUtil.setGone(holder.stateIcon, R.id.stateIcon);

			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new ArmEnhanceWindow().open(prop);
				}

			});
		} else {

			if (ArmTrainingAdapter.unlock(buiprop.getId(), true)) {// 建筑是否解锁
				ViewUtil.setText(holder.desc, "特性：" + prop.getDesc());
				ViewUtil.setGone(holder.blackIcon, R.id.blackIcon);
				ViewUtil.setGone(holder.stateIcon, R.id.stateIcon);
				if (BuildingProp.BUILDING_TYPE_GUARDIAN == buiprop.getId()) {
					getBuildingBuy(prop, buiprop, manorDraft, null);
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new ArmTrainingWindow().open(prop);
						}

					});
				} else {

					if (null != manorDraft) {
						v.setOnClickListener(new ClickListener(prop, buiprop,
								manorDraft, v, holder));
					} else {
						v.setOnClickListener(null);
					}
				}

			} else {
				ViewUtil.setVisible(holder.stateIcon, R.id.stateIcon);
				ViewUtil.setVisible(holder.count, R.id.count);
				ViewUtil.setText(holder.count, "兵种未获得");
				ViewUtil.setRichText(
						holder.desc,
						"获得条件："
								+ ArmTrainingAdapter.getRequirement(buiprop
										.getId()));

				ViewUtil.setVisible(holder.blackIcon);
				ViewUtil.setImage(holder.stateIcon,
						R.drawable.building_state_wjs1);
				v.setOnClickListener(null);

			}
		}

	}

	// 建筑创建资源条件
	protected boolean checkCondition(int type, BuildingProp buildprop,
			ManorDraft manorDraft) {
		boolean checkLevel = true;
		if (type == Constants.TYPE_CURRENCY) {
			checkLevel = buildprop.isCheckLv();
		}
		// 检查前提条件
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

	// 建筑购买
	protected void getBuildingBuy(TroopProp prop, BuildingProp buildprop,
			ManorDraft manorDraft, CallBack mBack) {
		if (null == Account.manorInfoClient.getBuilding(buildprop)
				|| !Account.manorInfoClient.getArmids().contains(prop.getId())) {
			if (ArmTrainingAdapter.unlock(buildprop.getId(), true)) {
				if (checkCondition(Constants.TYPE_MATERIAL, buildprop,
						manorDraft)) {// 解锁情况下普通购买
					new BuildingBuyImplInvoker(Constants.TYPE_MATERIAL,
							buildprop, prop, mBack).start();
				}
			}
		}
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
			getBuildingBuy(troopProp, buildingProp, manorDraft,
					new setItemCallback(view, holder, troopProp));
		}

	}

	// item执行点击操作后修改item的值
	private class setItemCallback implements CallBack {
		private ViewHolder holder;
		private TroopProp prop;
		private View v;

		public setItemCallback(View v, ViewHolder holder, TroopProp prop) {
			super();
			this.holder = holder;
			this.v = v;
			this.prop = prop;
		}

		@Override
		public void onCall() {
			if (holder != null) {
				holder.stateIcon.setVisibility(View.GONE);
				holder.blackIcon.setVisibility(View.GONE);
			}
			if (null != v) {
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						new ArmEnhanceWindow().open(prop);
					}

				});
			}
		}

	}

	private class BuildingBuyImplInvoker extends BaseInvoker {

		private int type;
		protected BuildingProp bprop;
		protected BuildingBuyResp resp;
		protected TroopProp prop;
		private CallBack mBack;

		public BuildingBuyImplInvoker(int type, BuildingProp bprop,
				TroopProp prop, CallBack mBack) {
			this.type = type;
			this.bprop = bprop;
			this.prop = prop;
			this.mBack = mBack;
		}

		@Override
		protected String loadingMsg() {
			return "请稍后...";
		}

		@Override
		protected String failMsg() {
			return "失败";
		}

		@Override
		protected void fire() throws GameException {
			resp = GameBiz.getInstance().buildingBuy(type, bprop.getId());
			Account.manorInfoClient.update(resp.getMic());
		}

		@Override
		protected void onOK() {
			if (mBack != null) {
				mBack.onCall();
			}
			if (BuildingProp.BUILDING_TYPE_GUARDIAN != bprop.getId()) {
				new ArmEnhanceWindow().open(prop);
			}
		}

	}

	private static class ViewHolder {
		ImageView icon, stateIcon, armicon, blackIcon;
		TextView name, desc, count;
	}

}
