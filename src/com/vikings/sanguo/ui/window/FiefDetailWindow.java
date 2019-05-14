package com.vikings.sanguo.ui.window;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.alert.BuildingFiefBuyTip;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.CustomPopupWindow;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class FiefDetailWindow extends CustomPopupWindow {
	private BriefFiefInfoClient bfic;
	// private String familyName = "无";
	private BriefGuildInfoClient bgic;

	public void open(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		if (null == bfic)
			return;

		if (null == bfic.getLord() || 0 == bfic.getLord().getGuildid())
			doOpen();
		else
			new GetFamalyInfo().start();
	}

	@Override
	protected void init() {
		super.init("领地详情");
		setContentBelowTitle(R.layout.fief_detail_top);
		setContent(R.layout.fief_detail_list);
		// 地址
		long tileId = TileUtil.fiefId2TileId(bfic.getId());
		TextView subAddr = (TextView) window.findViewById(R.id.subAddr);
		new AddrLoader(subAddr, tileId, AddrLoader.TYPE_SUB);
		ViewUtil.setBold(subAddr);
		new AddrLoader(window.findViewById(R.id.mainAddr), tileId,
				AddrLoader.TYPE_MAIN);
	}

	private void setDetailItem() {
		View evil = window.findViewById(R.id.evil);
		if (bfic.isHoly() || bfic.isOwnAltar() || bfic.isOtherAltar()) {
			try {
				HolyProp hpHolyProp = bfic.getHolyProp();
				ViewUtil.setVisible(evil);
				new ViewImgCallBack(hpHolyProp.getProp().getDetailIcon(),
						evil.findViewById(R.id.icon));
				ViewUtil.setRichText(evil, R.id.evilTitle, hpHolyProp.getProp()
						.getForeignTitle());
				ViewUtil.setRichText(evil, R.id.evilDesc, hpHolyProp.getProp()
						.getDetailSpec());
			} catch (GameException e) {
				Log.e("FiefDetailWindow", bfic.getId() + "not found!");
				e.printStackTrace();
			}

		} else {
			ViewUtil.setGone(evil);
		}
		evil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new HolyDetailWindow().open(bfic);

			}
		});

		View building = window.findViewById(R.id.building);
		View abandon = window.findViewById(R.id.abandon);
		if (bfic.isMyFief()) {
			if (bfic.isResource()) {
				ViewUtil.setVisible(building);
				new ViewImgCallBack("title_fief_build.png",
						building.findViewById(R.id.icon));
			} else {
				ViewUtil.setGone(building);
			}
			ViewUtil.setVisible(abandon);
			new ViewImgCallBack("title_fief_abandon.png",
					abandon.findViewById(R.id.icon));
			abandon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					controller.confirm("放弃领地", CustomConfirmDialog.DEFAULT,
							"是否确定放弃", "", new AbandomCallBack(), null);
				}
			});
		} else {
			ViewUtil.setGone(building);
			ViewUtil.setGone(abandon);
		}

		building.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BuildingProp prop = CacheMgr.buildingPropCache
						.getInitialBuildingByType(bfic.getProp()
								.getBuildingType());
				if (prop == null) {
					Config.getController().alert(
							bfic.getProp().getName() + "不能盖建筑");
					return;
				}

				if (null != bfic.getBuilding()) {
					prop = bfic.getBuilding().getProp();

					if (prop.getNextLevel() != null)
						prop = prop.getNextLevel();
				}

				new BuildingFiefBuyTip(prop).show(prop, bfic);
			}
		});

		View troopInfo = window.findViewById(R.id.troopInfo);
		// 如果是圣都 且不可占领 不显示 驻兵信息
		if (bfic.isHoly()) {
			try {
				HolyProp hpHolyProp = bfic.getHolyProp();
				if (!hpHolyProp.canOccupied()) {
					ViewUtil.setGone(troopInfo);
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

		new ViewImgCallBack("title_fief_troop.png",
				troopInfo.findViewById(R.id.icon));
		troopInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.openFiefTroopWindow(bfic);
			}
		});

		if (bfic.isMyFief()) {
			ViewUtil.setText(troopInfo, R.id.troop, "查看并安排领地上的驻兵,能够有效保护你的领地");
		} else {
			ViewUtil.setText(troopInfo, R.id.troop, "查看驻防在这里的士兵和将领，了解对方的兵力部署");
		}

		View history = window.findViewById(R.id.history);
		new ViewImgCallBack("title_fief_history.png",
				history.findViewById(R.id.icon));
		history.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.openHistoryWarInfoWindow(bfic);
			}
		});
	}

	@Override
	public void showUI() {
		FiefDetailTopInfo.update(window, bfic, bgic);// familyName
		setDetailItem();
		super.showUI();
	}

	@Override
	protected int refreshInterval() {
		return 1000;
	}

	private class GetFamalyInfo extends BaseInvoker {
		@Override
		protected String loadingMsg() {
			return "获取信息中...";
		}

		@Override
		protected String failMsg() {
			return "";
		}

		@Override
		protected void fire() throws GameException {
			bgic = CacheMgr.bgicCache.get(bfic.getLord().getGuildid());
		}

		@Override
		protected void onOK() {
			// familyName = bgic.getName();
			doOpen();
		}

		@Override
		protected void onFail(GameException exception) {
			// familyName = "暂未获取家族信息";
			doOpen();
		}
	}

	private class AbandonFiefInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "请稍候";
		}

		@Override
		protected String failMsg() {
			return "放弃失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().fiefAbandon(bfic.getId());
			// 放弃资源点时 刷新领地，使领地重置
			controller.getBattleMap().refreshCurFief(false);
		}

		@Override
		protected void onOK() {
			Account.richFiefCache.deleteFief(bfic.getId());
			Account.assistCache.deleteGodSoldierByFiefid(bfic.getId());
			controller.alert("您已放弃该领地!");
			controller.goBack();
		}

	}

	private class AbandomCallBack implements CallBack {

		@Override
		public void onCall() {

			if (Account.richFiefCache.isMyFief(bfic.getId())) {
				RichFiefInfoClient rfic = Account.richFiefCache.getInfo(bfic
						.getId());
				if (rfic.getFiefInfo().getUnitCount() > 0) {
					controller.confirm("放弃领地失败", CustomConfirmDialog.DEFAULT,
							"当前领地还有驻兵，不能放弃<br/><br/>请先撤回驻防士兵和将领", "", "撤回主城",
							new WithDrawTroopsCallBack(), "关闭", null);
					return;
				}
				new AbandonFiefInvoker().start();
			}
		}
	}

	private class WithDrawTroopsCallBack implements CallBack {

		@Override
		public void onCall() {
			new TroopMoveDetailWindow().open(bfic, Account.richFiefCache
					.getManorFief().brief(), null,
					com.vikings.sanguo.Constants.TROOP_DISPATCH);
		}

	}
}
