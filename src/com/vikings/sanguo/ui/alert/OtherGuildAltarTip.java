/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-2 下午4:51:50
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.AddFavorateFief;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.invoker.DelFavorateFief;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.utils.ViewUtil;

public class OtherGuildAltarTip extends GuildAltarTip {
	private View sendTroop, battleField, assistAtk, detail, history, favorite,
			close;

	public OtherGuildAltarTip(BriefFiefInfoClient bfic) {
		super();
		this.bfic = bfic;
	}

	public void show() {
		new GetOtherAltarInvoker().start();
	}

	@Override
	protected void setButtons() {
		if (!Account.isAltarInBattle()) {
			ViewUtil.setGone(tip, R.id.sendTroop);
			ViewUtil.setGone(tip, R.id.favorite);

			battleField = findViewById(R.id.battleField);
			ViewUtil.setVisible(battleField);
			battleField.setOnClickListener(this);

			assistAtk = findViewById(R.id.assistAtk);
			ViewUtil.setVisible(assistAtk);
			assistAtk.setOnClickListener(this);
		} else {
			sendTroop = findViewById(R.id.sendTroop);
			sendTroop.setOnClickListener(this);

			favorite = findViewById(R.id.favorite);
			favorite.setOnClickListener(this);

			if (!Account.isFavoriteFief(bfic.getId())) {
				ViewUtil.setText(favorite, "添加收藏");
			} else {
				ViewUtil.setText(favorite, "移除收藏");
			}
		}

		detail = findViewById(R.id.detail);
		detail.setOnClickListener(this);

		history = findViewById(R.id.history);
		history.setOnClickListener(this);

		close = findViewById(R.id.close);
		close.setOnClickListener(closeL);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == sendTroop) {

		} else if (v == favorite) {
			if (Account.isFavoriteFief(bfic.getId())) {
				new DelFavorateFief(bfic.getId(), null).start();
			} else {
				new AddFavorateFief(bfic.getId()).start();
			}
		} else if (v == battleField) {

		} else if (v == assistAtk) {

		} else if (v == detail) {
			controller.openFiefTroopWindow(bfic);
		} else if (v == history) {
			controller.openHistoryWarInfoWindow(bfic);
		}
	}

	private class GetOtherAltarInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "获取家族总坛信息中";
		}

		@Override
		protected String failMsg() {
			return "获取家族总坛失败";
		}

		@Override
		protected void fire() throws GameException {
			if (null != bfic && null != bfic.getLord()) {
				if (bfic.getLord().hasGuild())
					gic = CacheMgr.bgicCache.get(bfic.getLord().getGuildid()
							.intValue());
			}
		}

		@Override
		protected void onOK() {
			showTip();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.alert_other_guild_altar;
	}
}