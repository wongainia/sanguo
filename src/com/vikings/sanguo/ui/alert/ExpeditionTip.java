package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.OtherUserClient;
import com.vikings.sanguo.ui.window.TroopMoveDetailWindow;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ExpeditionTip extends CustomConfirmDialog implements
		OnClickListener {
	private static final int layout = R.layout.alert_expedition;
	private Button plunderBtn, occupyBtn, duelBtn, massacreBtn, closeBtn;
	private BriefFiefInfoClient bfic;
	private OtherUserClient ouc;

	public ExpeditionTip(BriefFiefInfoClient bfic, OtherUserClient ouc) {
		super("选择出征方式", DEFAULT);
		this.bfic = bfic;
		this.ouc = ouc;
		plunderBtn = (Button) tip.findViewById(R.id.plunderBtn);
		plunderBtn.setOnClickListener(this);
		occupyBtn = (Button) tip.findViewById(R.id.occupyBtn);
		occupyBtn.setOnClickListener(this);
		duelBtn = (Button) tip.findViewById(R.id.duelBtn);
		duelBtn.setOnClickListener(this);
		massacreBtn = (Button) tip.findViewById(R.id.massacreBtn);
		massacreBtn.setOnClickListener(this);
		closeBtn = (Button) tip.findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		if (bfic.isMyFief())
			return;
		if (bfic.isCastle()) {
			if (!BriefUserInfoClient.isNPC(bfic.getUserId())) {
				ViewUtil.setVisible(duelBtn);
				ViewUtil.setVisible(plunderBtn);
				// 是否显示屠城按钮
				if (CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_MASSACRE, 3) == 1)
					ViewUtil.setVisible(massacreBtn);
				else
					ViewUtil.setHide(massacreBtn);
				if (bfic.isInBattle()) {
					ViewUtil.disableButton(duelBtn);
					ViewUtil.disableButton(massacreBtn);
					ViewUtil.disableButton(plunderBtn);
				} else {
					if (ouc != null) {
						if (ouc.isWeak())
							ViewUtil.disableButton(massacreBtn);
						else
							ViewUtil.enableButton(massacreBtn);

						if (ouc.isDuelProtected()) {
							ViewUtil.disableButton(duelBtn);
							ViewUtil.disableButton(plunderBtn);
							ViewUtil.disableButton(massacreBtn);
						} else {
							ViewUtil.enableButton(duelBtn);
							ViewUtil.enableButton(plunderBtn);
							ViewUtil.enableButton(massacreBtn);
						}
					}
				}

			}
		} else if (bfic.isResource()) {
			if (!BriefUserInfoClient.isNPC(bfic.getUserId())
					&& bfic.hasBuilding()) {
				ViewUtil.setVisible(plunderBtn);
			}
			ViewUtil.setVisible(occupyBtn);
		}

		if (Account.plunderedCache.plundered(bfic.getId()) || bfic.isInBattle()) {
			ViewUtil.disableButton(plunderBtn);
		} else {
			ViewUtil.enableButton(plunderBtn);
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (v == plunderBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_PLUNDER);
		} else if (v == occupyBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, null,
					com.vikings.sanguo.Constants.TROOP_OCCUPY);
		} else if (v == duelBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_DUEL);
		} else if (v == massacreBtn) {
			new TroopMoveDetailWindow().open(Account.richFiefCache
					.getManorFief().brief(), bfic, ouc,
					com.vikings.sanguo.Constants.TROOP_MASSACRE);
		}
	}
}
