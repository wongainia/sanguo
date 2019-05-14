package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.invoker.WarInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class WarConfirmTip extends CustomConfirmDialog {

	private int target;
	private long fiefid;
	private long battleId;
	private RichBattleInfoClient rbic;
	private long needRmb = 0;

	public WarConfirmTip(long battleId, RichBattleInfoClient rbic) {
		super("", CustomConfirmDialog.DEFAULT);

		this.battleId = battleId;
		this.rbic = rbic;
		this.fiefid = rbic.getBattleInfo().getBbic().getDefendFiefid();
		if (rbic.isAttacker()) {
			this.target = rbic.getBattleInfo().getBbic().getDefender();
		} else if (rbic.isDefender()) {
			this.target = rbic.getBattleInfo().getBbic().getAttacker();
		}
		setButton(CustomConfirmDialog.THIRD_BTN, "关闭", closeL);
	}

	private void setValue() {
		StringBuilder builderDesc = new StringBuilder();
		if (rbic.isAttacker()) {
			if (rbic.getBattleInfo().getBbic().getCurState() == BattleStatus.BATTLE_STATE_SURROUND) {
				needRmb = getForceAtkCost();

				builderDesc
						.append("围城还没有结束，不能发起进攻,您是否要花费#rmb#")
						.append(StringUtil.color(String.valueOf(needRmb), "red"))
						.append(",立刻强攻?");

				setButton(CustomConfirmDialog.FIRST_BTN, "强攻#rmb#" + needRmb,
						fightL);
				setTitle("强行攻击");
			} else {
				builderDesc.append("你的士兵们已经双眼血红了，赶快发起进攻吧！！");
				setButton(CustomConfirmDialog.FIRST_BTN, "发起进攻", fightL);
				setTitle("进攻确认");
				fightL.onClick(tip.findViewById(R.id.btn1));
			}

		} else if (rbic.isDefender()) {
			if (rbic.getBattleInfo().getBbic().getCurState() == BattleStatus.BATTLE_STATE_SURROUND) {
				needRmb = getForceDefCost();

				builderDesc
						.append("围城还没有结束，不能发起进攻,您是否要花费#rmb#")
						.append(StringUtil.color(String.valueOf(needRmb), "red"))
						.append(",立刻突围?");

				setButton(CustomConfirmDialog.FIRST_BTN, "突围#rmb#" + needRmb,
						fightL);
				setTitle("强行突围");
			} else {
				builderDesc.append("你的士兵们已经双眼血红了，赶快出城应战吧!");
				setButton(CustomConfirmDialog.FIRST_BTN, "出城应战", fightL);
				setTitle("出战确认");
				fightL.onClick(tip.findViewById(R.id.btn1));
			}
		}
		ViewUtil.setRichText(content, R.id.msg, builderDesc.toString());
	}

	@Override
	public void show() {
		setValue();
		if (rbic.getBattleInfo().getBbic().getCurState() != BattleStatus.BATTLE_STATE_SURROUND)
			return;
		super.show();
	}

	private long getForceAtkCost() {
		int para1 = CacheMgr.dictCache.getDictInt(Dict.TYPE_FORCE, 1);
		int para2 = CacheMgr.dictCache.getDictInt(Dict.TYPE_FORCE, 2);

		int threshold = CacheMgr.dictCache
				.getDictInt(Dict.TYPE_BATTLE_COST, 30);
		long totalHp = rbic.getDefendArmTotalHP();
		totalHp = (totalHp > threshold ? threshold : totalHp);
		totalHp = totalHp / 100;
		return totalHp / para1 + para2;
	}

	private long getForceDefCost() {
		int para1 = CacheMgr.dictCache.getDictInt(Dict.TYPE_FORCE, 3);
		int para2 = CacheMgr.dictCache.getDictInt(Dict.TYPE_FORCE, 4);

		int threshold = CacheMgr.dictCache
				.getDictInt(Dict.TYPE_BATTLE_COST, 30);
		long totalHp = rbic.getAttackArmTotalHP();
		totalHp = (totalHp > threshold ? threshold : totalHp);
		totalHp = totalHp / 100;
		return totalHp / para1 + para2;
	}

	private OnClickListener fightL = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();

			// 判断元宝是否满足
			if (Account.user.getCurrency() < needRmb) {
				ViewUtil.showRechargeTip("元宝不足", "请前去充值,提升Vip等级可以获得更多精兵强将.");
			} else {
				if (rbic.isAttacker()) {
					if (rbic.getBattleInfo().getBbic().getCurState() == BattleStatus.BATTLE_STATE_SURROUND)
						startWar(Constants.BATTLE_TYPE_STORM);
					else
						startWar(Constants.BATTLE_TYPE_ATTACK);
				} else {
					if (rbic.getBattleInfo().getBbic().getCurState() == BattleStatus.BATTLE_STATE_SURROUND)
						startWar(Constants.BATTLE_TYPE_FORCE_BREAK);
					else
						startWar(Constants.BATTLE_TYPE_BREAK_THROUGH);
				}
			}
		}
	};

	// 发起进攻
	private void startWar(int battleType) {
		new WarInvoker(rbic.getBattleInfo().getBbic().getType(), battleType,
				target, fiefid, battleId, new CallBack() {
					@Override
					public void onCall() {
						controller.goBack();
					}
				}, null).start();
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_confirm, tip, false);
	}

}
