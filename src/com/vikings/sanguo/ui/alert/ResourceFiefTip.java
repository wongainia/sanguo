package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.AddFavorateFief;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.DelFavorateFief;
import com.vikings.sanguo.invoker.FiefReceiveInvoker;
import com.vikings.sanguo.invoker.SpeedUpInvoker;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.SiteSpecial;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.thread.CurrencyCallBack;
import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class ResourceFiefTip extends CustomConfirmDialog implements
		OnClickListener {

	private Button buildBtn, receiveBtn, expeditionBtn, detailBtn, favoriteBtn,
			closeBtn, battleBtn, assistAttackBtn, assistDefendBtn,
			updateBuildBtn, transferBtn;

	private BriefFiefInfoClient bfic;
	private BriefGuildInfoClient gic;
	private OtherUserClient ouc;
	private SiteSpecial siteSpecial = null;

	public ResourceFiefTip() {
		super(CustomConfirmDialog.DEFAULT);

		buildBtn = (Button) content.findViewById(R.id.buildBtn);
		buildBtn.setOnClickListener(this);

		updateBuildBtn = (Button) content.findViewById(R.id.updateBuildBtn);
		updateBuildBtn.setOnClickListener(this);

		transferBtn = (Button) content.findViewById(R.id.transferBtn);
		transferBtn.setOnClickListener(this);

		receiveBtn = (Button) content.findViewById(R.id.receiveBtn);
		receiveBtn.setOnClickListener(this);

		expeditionBtn = (Button) content.findViewById(R.id.expeditionBtn);
		expeditionBtn.setOnClickListener(this);

		detailBtn = (Button) content.findViewById(R.id.detailBtn);
		detailBtn.setOnClickListener(this);

		favoriteBtn = (Button) content.findViewById(R.id.favoriteBtn);
		favoriteBtn.setOnClickListener(this);

		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);

		battleBtn = (Button) content.findViewById(R.id.battleBtn);
		battleBtn.setOnClickListener(this);

		assistAttackBtn = (Button) content.findViewById(R.id.assistAttackBtn);
		assistAttackBtn.setOnClickListener(this);

		assistDefendBtn = (Button) content.findViewById(R.id.assistDefendBtn);
		assistDefendBtn.setOnClickListener(this);
	}

	public void show(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		refreshInterval = 1000;
		siteSpecial = bfic.getSiteSpecial();
		new QueryDataInvoker().start();
	}

	private void showTip() {
		setValue();
		super.show();

	}

	protected void updateDynView() {
		FiefDetailTopInfo.setProduct(bfic, findViewById(R.id.productLayout),
				R.drawable.setoff_cnt_sg, siteSpecial, ouc);

		Button speedUpBtn = (Button) content.findViewById(R.id.speedUpBtn);
		if (bfic.isMyFief() && bfic.hasBuilding()) {
			ViewUtil.setVisible(speedUpBtn);
			ViewUtil.setVisible(content, R.id.speedUpDesc);

			String speedUpDesc = "";

			int cd = bfic.getBuilding().getResetCd();
			if (cd > 0) {
				speedUpDesc = StringUtil.color(DateUtil.formatSecond(cd)
						+ "后可再次加速", R.color.k7_color8);
				ImageUtil.setBgGray(speedUpBtn);
				speedUpBtn.setOnClickListener(null);
			} else {
				speedUpDesc = "花费#rmb#" + bfic.getSpeedUpCost()
						+ "元宝加速，可立即满产收获";
				ImageUtil.setBgNormal(speedUpBtn);
				speedUpBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (bfic.hasMaxStore()) {
							controller.alert("已经生产完成，不用加速");
							return;
						}

						new CurrencyCallBack(bfic.getSpeedUpCost(), closeCb) {
							@Override
							public void handle() {
								new FiefSpeedUpInvoker(bfic.getBuilding(), bfic)
										.start();
							}
						}.onCall();
					}
				});
			}
			// 如果已经产满
			if (null != siteSpecial && bfic.hasMaxStore())
				speedUpDesc = siteSpecial.getDesc();

			ViewUtil.setRichText(content, R.id.speedUpDesc, speedUpDesc, true);
		} else {
			ViewUtil.setGone(speedUpBtn);
			ViewUtil.setGone(content, R.id.speedUpDesc);
		}
	}

	private void setValue() {
		new AddrLoader(getTitle(), TileUtil.fiefId2TileId(bfic.getId()),
				AddrLoader.TYPE_SUB);

		setButton();
		updateDynView();
		IconUtil.setFiefIconWithBattleState(findViewById(R.id.infoLayout), bfic);
		FiefDetailTopInfo.setBaseFiefInfo(findViewById(R.id.infoLayout), bfic,
				true);
		FiefDetailTopInfo.setGuildInfo(findViewById(R.id.infoLayout), gic,
				closeCb);
	}

	private void setButton() {
		ViewUtil.setVisible(closeBtn);
		if (BriefUserInfoClient.isNPC(bfic.getUserId())) {// npc
			if (BattleStatus.isInBattle(bfic.getBattleState())) { // 战争中
				setAssistAttackBtn();
				setAssistDefendBtn();
				ViewUtil.setVisible(battleBtn);
				ViewUtil.setVisible(detailBtn);
			} else {
				ViewUtil.setVisible(expeditionBtn);
				expeditionBtn.setOnClickListener(this);
				ViewUtil.setVisible(detailBtn);
			}
			ViewUtil.setVisible(favoriteBtn);
			setFavoriteBtn();

		} else if (bfic.isMyFief()) { // 自己的资源点
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) { // 战争中
				setAssistAttackBtn();
				setAssistDefendBtn();
				ViewUtil.setVisible(battleBtn);
				ViewUtil.setVisible(detailBtn);
			} else {
				ViewUtil.setVisible(transferBtn);
				ViewUtil.setVisible(detailBtn);
				if (bfic.hasBuilding()) {
					ViewUtil.setVisible(receiveBtn);
				}
			}
			if (bfic.hasBuilding()) {
				if (bfic.isResource()) {
					ViewUtil.setVisible(updateBuildBtn);
				}
			} else {
				ViewUtil.setVisible(buildBtn);
			}

		} else {
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) { // 战争中
				setAssistAttackBtn();
				setAssistDefendBtn();
				ViewUtil.setVisible(battleBtn);
				ViewUtil.setVisible(detailBtn);
			} else {
				ViewUtil.setVisible(detailBtn);
				ViewUtil.setVisible(expeditionBtn);
			}
			ViewUtil.setVisible(favoriteBtn);
			setFavoriteBtn();
		}
	}

	private void setAssistDefendBtn() {
		ViewUtil.setVisible(assistDefendBtn);
		if (!bfic.canReinforceDefend()) {
			ViewUtil.disableButton(assistDefendBtn);
			assistDefendBtn.setOnClickListener(null);
			ViewUtil.setGone(assistDefendBtn);
		}
	}

	private void setAssistAttackBtn() {
		ViewUtil.setVisible(assistAttackBtn);
		if (!bfic.canReinforceAttack()) {
			ViewUtil.disableButton(assistAttackBtn);
			assistAttackBtn.setOnClickListener(null);
			ViewUtil.setGone(assistAttackBtn);
		}
	}

	private void setFavoriteBtn() {
		if (!Account.isFavoriteFief(bfic.getId())) {
			ViewUtil.setText(favoriteBtn, "添加收藏");
		} else {
			ViewUtil.setText(favoriteBtn, "移除收藏");
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_resource_fief, tip, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == buildBtn) {
			RichFiefInfoClient r = Account.richFiefCache.getInfo(bfic.getId());
			if (r == null)
				return;
			if (r.getFiefInfo().getBuilding() == null) {
				BuildingProp prop = CacheMgr.buildingPropCache
						.getInitialBuildingByType(bfic.getProp()
								.getBuildingType());
				if (prop == null) {
					Config.getController().alert(
							bfic.getProp().getName() + "不能盖建筑");
					return;
				}

				new BuildingFiefBuyTip(prop).show(prop, bfic);
			} else {
				Config.getController().alert("该领地上已经有建筑了");
				return;
			}

		} else if (v == updateBuildBtn) {
			BuildingProp prop = CacheMgr.buildingPropCache
					.getInitialBuildingByType(bfic.getProp().getBuildingType());
			if (prop == null) {
				Config.getController()
						.alert(bfic.getProp().getName() + "不能盖建筑");
				return;
			}

			if (null != bfic.getBuilding()) {
				prop = bfic.getBuilding().getProp();

				if (prop.getNextLevel() != null)
					prop = prop.getNextLevel();
			}

			new BuildingFiefBuyTip(prop).show(prop, bfic);

		} else if (v == receiveBtn) {
			RichFiefInfoClient r = Account.richFiefCache.getInfo(bfic.getId());
			if (r == null)
				return;
			if (r.getFiefInfo().getBuilding() == null) {
				Config.getController().alert("请先建造资源建筑");
				return;
			} else {
				int count = r
						.getFiefInfo()
						.getBuilding()
						.produce(bfic.getProp().getBuildingStatus(),
								bfic.getLord().getLastLoginTime());
				if (count <= 0) {
					Config.getController().alert("无产出");
					return;
				} else {
					String msg = Account.user.checkWeight(bfic.getProp()
							.getProductType());
					if (msg != null) {
						controller.alert(msg);
						return;
					}
					new FiefReceiveInvoker(bfic.getId()).start();
				}
			}

		} else if (v == expeditionBtn) {
			if (BriefUserInfoClient.isNPC(bfic.getUserId())) {
				new TroopMoveDetailWindow().open(Account.richFiefCache
						.getManorFief().brief(), bfic, ouc,
						com.vikings.sanguo.Constants.TROOP_OCCUPY);
			} else {
				if (BriefUserInfoClient.attackLevelLimit()) {
					if (!Account.user.isNewerProtectedLevel()
							&& ouc.bref().isNewerProtected()) {
						controller.alert(CacheMgr.dictCache.getDict(
								Dict.TYPE_BATTLE_PROTECTED, 1));
						return;
					}

					if (Account.user.isNewerProtectedLevel()
							&& !ouc.bref().isNewerProtected()) {
						controller.alert(CacheMgr.dictCache.getDict(
								Dict.TYPE_BATTLE_PROTECTED, 3));
						return;
					}
				}
				new ExpeditionTip(bfic, ouc).show();
			}
		} else if (v == detailBtn) {
			controller.openFiefDetailWindow(bfic);
		} else if (v == favoriteBtn) {
			if (Account.isFavoriteFief(bfic.getId())) {
				new DelFavorateFief(bfic.getId(), null).start();
			} else {
				new AddFavorateFief(bfic.getId()).start();
			}
		} else if (v == battleBtn) {
			controller.openWarInfoWindow(bfic); // , null
		} else if (v == assistAttackBtn) {
			if (BriefUserInfoClient.attackLevelLimit()) {
				if (bfic.isNewerBattle() && !bfic.isMyBattle()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 2));
					return;
				}
				if (!bfic.isNewerBattle() && !bfic.isMyBattle()
						&& Account.user.isNewerProtectedLevel()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 4));
					return;
				}
			}
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_REINFORCE);
		} else if (v == assistDefendBtn) {
			if (BriefUserInfoClient.attackLevelLimit()) {
				if (bfic.isNewerBattle() && !bfic.isMyBattle()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 2));
					return;
				}

				if (!bfic.isNewerBattle() && !bfic.isMyBattle()
						&& Account.user.isNewerProtectedLevel()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 4));
					return;
				}
			}
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_REINFORCE);
		} else if (v == transferBtn) {
			new ResourceFiefTransfer().show(bfic);
		}
	}

	private class FiefSpeedUpInvoker extends SpeedUpInvoker {

		public FiefSpeedUpInvoker(BuildingInfoClient bic,
				BriefFiefInfoClient bfic) {
			super(bic, bfic);
		}

		@Override
		protected void onOK() {
			super.onOK();
			updateDynView();
		}
	}

	private class QueryDataInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "加载数据";
		}

		@Override
		protected String failMsg() {
			return "加载数据失败";
		}

		@Override
		protected void fire() throws GameException {
			if (!BriefUserInfoClient.isNPC(bfic.getUserId())) {
				if (bfic.isMyFief()) {
					if (Account.user.hasGuild()) {
						Account.guildCache.updata(false);
						RichGuildInfoClient rgic = Account.guildCache
								.getRichInfoInCache();
						if (null != rgic)
							gic = rgic.getGic();
					}
				} else {
					ouc = GameBiz.getInstance().queryRichOtherUserInfo(
							bfic.getUserId(),
							Constants.DATA_TYPE_OTHER_RICHINFO);
					if (ouc.bref().hasGuild()) {
						gic = CacheMgr.bgicCache.get(ouc.getGuildid()
								.intValue());
					}
				}
			}

			bfic.setDefenderHero();
		}

		@Override
		protected void onOK() {
			showTip();
		}

	}
}
