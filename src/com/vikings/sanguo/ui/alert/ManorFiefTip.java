package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.AddFavorateFief;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.DelFavorateFief;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefGuildInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.ui.window.FiefTroopWindow;
import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class ManorFiefTip extends CustomConfirmDialog implements
		OnClickListener {

	private Button expeditionBtn, castleBtn, favoriteBtn, closeBtn, battleBtn,
			assistAttackBtn, assistDefendBtn, heroBtn;

	private BriefFiefInfoClient bfic;
	private OtherUserClient ouc;
	private BriefGuildInfoClient gic;

	public ManorFiefTip() {
		super(CustomConfirmDialog.DEFAULT);
		expeditionBtn = (Button) content.findViewById(R.id.expeditionBtn);
		expeditionBtn.setOnClickListener(this);
		castleBtn = (Button) content.findViewById(R.id.castleBtn);
		castleBtn.setOnClickListener(this);
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
		heroBtn = (Button) content.findViewById(R.id.heroBtn);
		heroBtn.setOnClickListener(this);
	}

	public void show(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
		new QueryDataInvoker().start();
	}

	private void showTip() {
		setValue();
		super.show();

	}

	private void setValue() {
		new AddrLoader(getTitle(), TileUtil.fiefId2TileId(bfic.getId()),
				AddrLoader.TYPE_SUB);

		setButton();

		if (bfic.isMyFief()) {
			ViewUtil.setText(castleBtn, "进入主城");
			FiefDetailTopInfo.setResources(findViewById(R.id.resourceLayout),
					Account.user, bfic);
		} else {
			ViewUtil.setText(castleBtn, "查看主城");
			FiefDetailTopInfo.setResources(findViewById(R.id.resourceLayout),
					ouc, bfic);
		}

		IconUtil.setFiefIconWithBattleState(findViewById(R.id.infoLayout), bfic);
		FiefDetailTopInfo.setBaseFiefInfo(tip, bfic, true);
		FiefDetailTopInfo.setGuildInfo(tip, gic, closeCb);
	}

	private void setButton() {
		ViewUtil.setVisible(closeBtn);
		if (bfic.isMyFief()) {
			ViewUtil.setVisible(castleBtn);
			ViewUtil.setVisible(heroBtn);
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) {
				ViewUtil.setVisible(battleBtn);
			}
		} else {
			ViewUtil.setVisible(favoriteBtn);
			ViewUtil.setGone(heroBtn);
			if (BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) {
				setAssistAttackBtn();
				setAssistDefendBtn();
				ViewUtil.setVisible(battleBtn);
			} else {
				ViewUtil.setVisible(expeditionBtn);
				// 保护单挑玩家
				if (null != ouc && ouc.isDuelProtected()) {
					ViewUtil.disableButton(expeditionBtn);
				} else {
					ViewUtil.enableButton(expeditionBtn);
				}

				ViewUtil.setVisible(castleBtn);
			}

			if (BriefUserInfoClient.isNPC(bfic.getUserId())) {
				ViewUtil.disableButton(expeditionBtn);
				expeditionBtn.setOnClickListener(null);
				ViewUtil.disableButton(castleBtn);
				castleBtn.setOnClickListener(null);
			}
			setFavoriteBtn();
		}
	}

	private void setAssistDefendBtn() {
		ViewUtil.setVisible(assistDefendBtn);
		if (!bfic.canReinforceDefend()) {
			ViewUtil.disableButton(assistDefendBtn);
			assistDefendBtn.setOnClickListener(null);
		}
	}

	private void setAssistAttackBtn() {
		ViewUtil.setVisible(assistAttackBtn);
		if (!bfic.canReinforceAttack()) {
			ViewUtil.disableButton(assistAttackBtn);
			assistAttackBtn.setOnClickListener(null);
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
		return controller.inflate(R.layout.alert_manor_fief, tip, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == expeditionBtn) {
			if (BriefUserInfoClient.attackLevelLimit()) {
				if (Account.user.isNewerProtectedLevel()
						&& !ouc.bref().isNewerProtected()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 3));
					return;
				}

				if (!Account.user.isNewerProtectedLevel()
						&& ouc.bref().isNewerProtected()) {
					controller.alert(CacheMgr.dictCache.getDict(
							Dict.TYPE_BATTLE_PROTECTED, 1));
					return;
				}
			}

			new ExpeditionTip(bfic, ouc).show();
		} else if (v == castleBtn) {
			controller.showCastle(bfic.getLord());
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
		} else if (v == heroBtn) {
			new FiefTroopWindow().open(Account.richFiefCache.getManorFief()
					.brief());
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
			if (bfic.isMyFief()) {
				if (Account.user.hasGuild()) {
					Account.guildCache.updata(false);
					RichGuildInfoClient rgic = Account.guildCache
							.getRichInfoInCache();
					if (null != rgic)
						gic = rgic.getGic();
				}
			} else if (!BriefUserInfoClient.isNPC(bfic.getUserId())) {
				ouc = GameBiz.getInstance().queryRichOtherUserInfo(
						bfic.getUserId(), Constants.DATA_TYPE_OTHER_RICHINFO);
				if (ouc.bref().hasGuild()) {
					gic = CacheMgr.bgicCache.get(ouc.getGuildid().intValue());
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
