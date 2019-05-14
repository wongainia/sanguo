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

public class ResourceFiefTransfer extends CustomConfirmDialog implements
		OnClickListener {

	private Button guardBtn, withdrawBtn, closeBtn;
	private BriefFiefInfoClient bfic;
	private BriefGuildInfoClient gic;
	private OtherUserClient ouc;

	public ResourceFiefTransfer() {
		super(CustomConfirmDialog.DEFAULT);

		guardBtn = (Button) content.findViewById(R.id.guardBtn);
		guardBtn.setOnClickListener(this);

		withdrawBtn = (Button) content.findViewById(R.id.withdrawBtn);
		withdrawBtn.setOnClickListener(this);

		closeBtn = (Button) content.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);

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
	}

	private void setButton() {
		ViewUtil.setVisible(closeBtn);
		if (bfic.isMyFief()) { // 自己的资源点
			if (!BattleStatus.isInBattle(TroopUtil.getCurBattleState(
					bfic.getBattleState(), bfic.getBattleTime()))) { // 战争中
				ViewUtil.setVisible(guardBtn);
				ViewUtil.setVisible(withdrawBtn);
			}

		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_resource_transfer, tip, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == guardBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_DISPATCH);
		} else if (v == withdrawBtn) {
			new TroopMoveDetailWindow().open(bfic, Account.richFiefCache
					.getManorFief().brief(), ouc,
					com.vikings.sanguo.Constants.TROOP_DISPATCH);
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
