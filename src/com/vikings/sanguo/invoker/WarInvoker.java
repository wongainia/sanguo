package com.vikings.sanguo.invoker;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BattlePlunderResp;
import com.vikings.sanguo.message.CommonAttackResp;
import com.vikings.sanguo.message.DuelAttackResp;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLossDetail;
import com.vikings.sanguo.model.BattleSideInfo;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.HolyProp;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.window.BattleWindow;
import com.vikings.sanguo.utils.ListUtil;

public class WarInvoker extends BaseInvoker {
	private BattleLogInfoClient blc = null;
	private int type;
	private int attackType;
	private int target;
	private long fiefid;
	private long battleId;
	private CallBack callBack;
	private CallBack callBackAfterAnim;
	private BattleLogInfo info;
	private BattleDriver battleDriver;
	private boolean isGuide;

	public WarInvoker(int attackType, int type, int target, long fiefid,
			long battleId, CallBack callBack, CallBack callBackAfterAnim) {
		super();
		this.attackType = attackType;
		this.type = type;
		this.target = target;
		this.fiefid = fiefid;
		this.battleId = battleId;
		this.callBack = callBack;
		this.callBackAfterAnim = callBackAfterAnim;
	}

	public WarInvoker(int attackType, int type, int target, long fiefid,
			long battleId, CallBack callBack, CallBack callBackAfterAnim,
			boolean isGuide) {
		super();
		this.attackType = attackType;
		this.type = type;
		this.target = target;
		this.fiefid = fiefid;
		this.battleId = battleId;
		this.callBack = callBack;
		this.callBackAfterAnim = callBackAfterAnim;
		this.isGuide = isGuide;
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.WarInvoker_failMsg);
	}

	@Override
	protected void fire() throws GameException {
		ReturnInfoClient ric = null;
		if (BattleAttackType.E_BATTLE_COMMON_ATTACK.getNumber() == attackType
				|| Constants.BATTLE_FIGHT_NPC == attackType) {
			CommonAttackResp resp = GameBiz.getInstance().battleAttack(type,
					target, fiefid);
			info = resp.getBli();
			ric = resp.getRi();
		} else if (BattleAttackType.E_BATTLE_PLUNDER_ATTACK.getNumber() == attackType) {
			BattlePlunderResp resp = GameBiz.getInstance().plunderAttack(type,
					target, fiefid);
			info = resp.getBli();
			ric = resp.getRi();
		} else if (BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber() == attackType
				|| BattleAttackType.E_BATTLE_MASSACRE_ATTACK.getNumber() == attackType) {
			DuelAttackResp resp = GameBiz.getInstance().duelAttack(type,
					target, fiefid);
			info = resp.getBli();
			ric = resp.getRi();
		} else
			return;

		Account.briefBattleInfoCache.delete(battleId);

		// 写文件保存日志信息
		Account.battleLogCache.updateCache(info);

		blc = new BattleLogInfoClient();
		blc.init(info);

		// 获取防守方的领地信息
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(blc.getDefSide().getFiefid());
		// 同步最新领主信息，用于刷新数据(此处用处不大，调试取到的数据还是老数据，服务器此时还没有结算完)

		// 取消的注释
		if (null != Account.myLordInfo && null != blc) {
			BattleSideInfo battleSideInfo = null;
			if (type == Constants.BATTLE_TYPE_ATTACK
					|| type == Constants.BATTLE_TYPE_STORM)
				battleSideInfo = blc.getAtkSide();
			else
				battleSideInfo = blc.getDefSide();

			if (null != battleSideInfo
					&& null != battleSideInfo.getDeathDetail()
					&& !ListUtil.isNull(battleSideInfo.getDeathDetail()
							.getLossDetailLs())) {
				BattleLossDetail bld = battleSideInfo.getDeathDetail()
						.getLossDetailLs().get(0);
				Account.myLordInfo.battleClean(bld.toArmInfoList());
			}
		}
		// 取消的注释

		if (BriefUserInfoClient.isNPC(info.getAttacker())
				&& blc.getAtkSide().getFiefid() == blc.getDefSide().getFiefid()
				&& CacheMgr.holyPropCache.isHoly(blc.getDefSide().getFiefid())) {
			HolyProp p = (HolyProp) CacheMgr.holyPropCache.get(blc.getDefSide()
					.getFiefid());
			RichFiefInfoClient rfic = Account.richFiefCache.getInfo(fiefid);
			if (null != rfic) {
				rfic.getFiefInfo().setNextExtraBattleTime(
						(int) (Config.serverTime() / 1000 + p.getCd()));
			}
		}

		battleDriver = new BattleDriver(blc, ric);
		if (null != blc) {
			CacheMgr.downloadBattleImgAndSound(blc);
			// Account.heroInfoCache.update(blc.getDetail().getRhics());
			if (blc.isPvp()) {
				if (blc.isMeWin())
					Config.latestBattleResult = 1;
				else if (blc.isMeLose()) {
					Config.latestBattleResult = -1;
					if (BattleAttackType.E_BATTLE_MASSACRE_ATTACK.getNumber() == attackType
							&& blc.isMeDefender()) { // 被屠城输了,需要同步玩家信息
						Account.updateSyncData(GameBiz
								.getInstance()
								.userDataSyn2(
										com.vikings.sanguo.message.Constants.SYNC_TYPE_DIFF,
										com.vikings.sanguo.message.Constants.DATA_TYPE_ROLE_INFO));
					}
				}
			}

		}
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.WarInvoker_loadingMsg);
	}

	@Override
	protected void onOK() {
		if (null != callBack)
			callBack.onCall();
		if (null != blc) {
			new BattleWindow().open(isGuide, battleDriver, callBackAfterAnim);
		} else {
			if (null != callBackAfterAnim)
				callBackAfterAnim.onCall();
		}

	}

}
