package com.vikings.sanguo.invoker;

import java.util.List;

import android.R.integer;
import android.util.Log;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.RichBattleInfoQueryResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.FiefScale;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.OtherFiefInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichBattleInfoClient;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.alert.AttackVipTip;
import com.vikings.sanguo.ui.alert.FiefSucTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.widget.FiefDetailTopInfo;

public class TroopRiseInvoker extends BaseInvoker {

	private ReturnInfoClient ri;
	private int type;
	private int battleType;
	private BriefFiefInfoClient myBfc;
	private BriefFiefInfoClient lordBfc;
	private List<ArmInfoClient> armInfoList;
	private List<HeroIdInfoClient> heros;
	private OtherFiefInfoClient ofic;
	private int cost;
	// 引导标识位
	private boolean isGuide = false;

	private FiefScale scale;

	private RichBattleInfoClient rbic;

	public TroopRiseInvoker(int type, int battleType,
			BriefFiefInfoClient myBfc, BriefFiefInfoClient lordBfc,
			List<ArmInfoClient> armInfoList, List<HeroIdInfoClient> heros,
			int cost) {
		this.type = type;
		this.battleType = battleType;
		this.myBfc = myBfc;
		this.lordBfc = lordBfc;
		this.armInfoList = armInfoList;
		this.heros = heros;
		this.cost = cost;
	}

	public TroopRiseInvoker(int type, int battleType,
			BriefFiefInfoClient myBfc, BriefFiefInfoClient lordBfc,
			List<ArmInfoClient> armInfoList, List<HeroIdInfoClient> heros,
			int cost, boolean isGuide) {
		this.type = type;
		this.battleType = battleType;
		this.myBfc = myBfc;
		this.lordBfc = lordBfc;
		this.armInfoList = armInfoList;
		this.heros = heros;
		this.cost = cost;
		this.isGuide = isGuide;
	}

	@Override
	protected String loadingMsg() {
		return "出征中";
	}

	@Override
	protected String failMsg() {
		return "出征失败";
	}

	@Override
	protected void fire() throws GameException {
		ofic = GameBiz.getInstance().otherFiefInfoQuery(lordBfc.getId());
		ri = GameBiz
				.getInstance()
				.battleRiseTroop(lordBfc.getId(), type, lordBfc.getUserId(),
						battleType, armInfoList, heros, cost).getRi();
		scale = lordBfc.getFiefScale();
		// if (battleType == Constants.TROOP_PLUNDER) {
		if (battleType == BattleAttackType.E_BATTLE_PLUNDER_ATTACK.number) {
			Account.plunderedCache.update(lordBfc.getId(), true);
		}

		// if (isGuide) {
		RichBattleInfoQueryResp resp = GameBiz.getInstance()
				.richBattleInfoQuery(lordBfc.getId(), true);
		rbic = resp.getInfo();
		// }
	}

	@Override
	protected void onOK() {
		int stamina = 0;
		// if (hero != null && hero.getId() > 0)
		// stamina = CacheMgr.heroCommonConfigCache.getCostStamina();

		if (!isGuide && rbic != null) {
			int battleSurroundTime = 0;
			if (rbic.getBbic() != null
					&& rbic.getBbic().getCurState() == BattleStatus.BATTLE_STATE_SURROUND_END) {
				battleSurroundTime = 0;
			} else {
				battleSurroundTime = scale.getLockTime();
			}
			new FiefSucTip().show(type, ri.getItemPack(), battleType,
					TroopUtil.countArm(armInfoList), ri.getFood(),
					battleSurroundTime, ri.getCurrency(), stamina,
					TileUtil.fiefId2TileId(lordBfc.getId()));

		}

		ctr.updateUI(ri, true);
		if (!isGuide) {
			Config.getController().goBack();
			ctr.openWarInfoWindow(lordBfc); // , null
		}

		// 引导代码
		if (isGuide && rbic != null) {
			new WarInvoker(rbic.getBattleInfo().getBbic().getType(),
					Constants.BATTLE_TYPE_ATTACK, rbic.getBattleInfo()
							.getBbic().getDefender(), rbic.getBattleInfo()
							.getBbic().getDefendFiefid(), lordBfc.getId(),
					new CallBack() {

						@Override
						public void onCall() {
							Config.getController().goBack();
						}
					}, null, isGuide).start();
		}

	}

	@Override
	protected void onFail(GameException exception) {
		if (exception.getResult() == 1108) {
			Log.e("Invoker fail", Log.getStackTraceString(exception));
			new AttackVipTip().show();
		} else if (exception.getResult() == 1079/* 恶魔之门时间倒计时 */) {
			String msg = failMsg();
			String time = "";
			if (ofic != null) {
				int times = FiefDetailTopInfo.getBattleTime(ofic
						.getNextExtraBattleTime());
				if (times > 0)
					time = DateUtil.formatTime(times);
			}
			if (!StringUtil.isNull(msg))
				msg = msg + exception.getErrorMsg().replace("<time>", time);
			else
				msg = exception.getErrorMsg().replace("<time>", time);
			Config.getController().alert("", msg, null, false);
		} else {
			super.onFail(exception);
		}
	}
}
