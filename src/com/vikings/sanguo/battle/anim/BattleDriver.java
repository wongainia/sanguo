/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-10 上午10:36:03
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.battle.anim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.animation.Animation;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BattleAnimEffects;
import com.vikings.sanguo.model.BattleArrayInfoClient;
import com.vikings.sanguo.model.BattleEventInfoClient;
import com.vikings.sanguo.model.BattleLogHeroInfoClient;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleSideInfo;
import com.vikings.sanguo.model.BattleSkill;
import com.vikings.sanguo.model.Buff;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroSkillSlotInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.model.SkillCombo;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.BattleArrayInfo;
import com.vikings.sanguo.protos.BattleEvent;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.BattleCoordUtil;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;

/**
 ** 日志事件中value的处理： 1.BATTLE_EVENT_SKILL：技能id = passives(0).getValue()
 * 2.BATTLE_EVENT_DEATH：死亡人数 = passives(i).getValue() 3.BATTLE_EVENT_ROUND：轮次 =
 * value 4.BATTLE_EVENT_NUM：数量 = actives(0).getValue() 5.BATTLE_EVENT_FALL_HP：掉血
 * = actives(0).getValue() 6.BATTLE_EVENT_BOUT：回合结束 = actives(0).getValue()
 * 7.BATTLE_EVENT_BUFF_SET：设置buf = actives(0).getValue()
 * 8.BATTLE_EVENT_BUFF_CLEAR：清除buf = actives(0).getValue()
 **/
// 把所用的isLeft 改成 isDown

public class BattleDriver {
	private ArrayList<List<Anim>> animList = new ArrayList<List<Anim>>(); // 存放一条日志的动画,支持同时播放
	private AnimPool animPool = new AnimPool();
	private ModifyBattleWindow modifyBtlWnd;
	private BattleLogInfoClient blic;
	private boolean isMyBattle;
	private boolean isMeAttacker;
	private boolean isNewLog;
	private int curEventIdx = 0;
	private int round = 1; // 回合数

	private List<Anim> animLs = new ArrayList<Anim>(); // 临时存放动画
	private ReturnInfoClient ric = null;
	private static int AMY_SHORT_DIS = (int) (30 * Config.SCALE_FROM_HIGH);
	private static int READY_ATTACK_DIS = (int) (40 * Config.SCALE_FROM_HIGH);

	private final int MELEE_ATTACK = 1;
	private final int REMOTE_ATTACK = 2;
	private final int MAGIC_ATTACK = 3;

	private final int DEFEALT_MELEE_ATTACK = 1;
	private final int DEFEALT_REMOTE_ATTACK = 2;
	private final int DEFEALT_MAGIC_ATTACK = 3;

	private boolean isStage = false; // 是否有军队出场

	private int downSkillId = 0; // 技能ID 用于判断当伤害出现时候 使用特效的类型
	private int upSkillId = 0;

	private int downSceneId; // active id
	private int upSceneId; // passive id

	private boolean isPause = false; // 回合开始后停0.3s

	// bitmap 回收

	public BattleDriver(BattleLogInfoClient blic, ReturnInfoClient ric) {
		this.blic = blic;
		this.isMyBattle = blic.isMyBattle();
		this.isMeAttacker = blic.isMeAttacker();
		this.isNewLog = blic.getDetail().isNewLog();
		this.ric = ric;
		System.gc();
	}

	public void setModifyBattleWindow(ModifyBattleWindow modifyBtlWnd) {
		this.modifyBtlWnd = modifyBtlWnd;
	}

	public boolean isAnimOver() {
		return curEventIdx == blic.getEventInfoClientList().size();
	}

	private void addBaseAnim(BaseAnim anim) {
		if (ListUtil.isNull(animList)) {
			animLs.clear();
			animList.add(animLs);
		}
		animLs.add(anim);
	}

	private void addAnimList(List<BaseAnim> ls) {
		if (ls == null) {
			return;
		}
		List<Anim> anims = new ArrayList<Anim>();
		for (int i = 0; i < ls.size(); i++) {
			anims.add(ls.get(i));
		}
		if (!ListUtil.isNull(anims))
			animList.add(anims);
	}

	private void addDrawAnimList(List<Anim> ls) {
		if (!ListUtil.isNull(ls))
			animList.add(ls);
	}

	long start_time = 0;

	// 把 伤害、数量事件一起解析 当收到数量的时候再处理 不处理伤害事件
	public ArrayList<List<Anim>> createAnim() { // BaseAnim
		if (curEventIdx >= blic.getEventInfoClientList().size())
			return null;

		animList.clear();
		BattleEventInfoClient info = blic.getEventInfoClientList().get(
				curEventIdx++);
		// Log.e("battleDriver", info.getEventType());
		// Log.e("battleDriver", info.tosString());
		switch (BattleEvent.valueOf(info.getEvent())) {
		case BATTLE_EVENT_ACT: // 行动者
			handleEventAct(info);
			isStage = true;
			isPause = false;
			round++;
			break;
		case BATTLE_EVENT_TARGET: // 选择目标
			handleEventTarget(info);
			break;
		case BATTLE_EVENT_FIGHT_BACK: // 还击
		case BATTLE_EVENT_BYPASS: // 绕过
			break;
		// 和数量一起处理
		case BATTLE_EVENT_DEMAGE: // 伤害
			// handleEventAttack(info);
			break;
		case BATTLE_EVENT_SKILL: // 触发技能 士兵技能、buff是一个事件
			List<BattleEventInfoClient> skillEventInfo = new ArrayList<BattleEventInfoClient>();
			BattleEventInfoClient skillInfo = blic.getEventInfoClientList()
					.get(curEventIdx - 1);
			BattleEventInfoClient nextInfoClient = blic
					.getEventInfoClientList().get(curEventIdx);
			skillEventInfo.add(skillInfo);
			skillEventInfo.add(nextInfoClient);
			handleEventSkill(skillEventInfo);
			break;
		case BATTLE_EVENT_DEATH: // 死亡
			handleEventDeath(info);
			break;
		case BATTLE_EVENT_RUN_AWAY: // 逃跑
			handleEventRunAway(info);
			break;
		case BATTLE_EVENT_ROUND: // 轮次
			handleEventRound(info);
			break;
		// 处理整个伤害和 飘数字 士兵数量变化
		case BATTLE_EVENT_NUM: // 数量
			if (curEventIdx < 2) {
				break;
			}
			BattleEventInfoClient demageInfo = blic.getEventInfoClientList()
					.get(curEventIdx - 2);
			List<BattleEventInfoClient> battleEventInfo = new ArrayList<BattleEventInfoClient>();
			battleEventInfo.add(demageInfo);
			battleEventInfo.add(info);
			handleEventDemage(battleEventInfo);
			// handleEventNum(info);
			break;
		case BATTLE_EVENT_FALL_HP: // HP
			// 现在不处理这个事件
			// handleEventFallHp(info);
			break;
		case BATTLE_EVENT_BOUT: // 回合结束
			handleEventBout(info);
			isStage = false;
			downSkillId = 0;
			upSkillId = 0;
			downSceneId = 0;
			upSceneId = 0;
			break;
		case BATTLE_EVENT_BUFF_SET: // 设置buff
			handleEventBuffSet(info);
			break;
		case BATTLE_EVENT_BUFF_CLEAR: // 清除buff
			handleEventBuffClear(info);
			break;
		case BATTLE_EVENT_MODIFY_HP:
			handleEventModifyHP(info);
			break;
		case BATTLE_EVENT_ARRAY:
			handleEventArray(info);
			break;

		default:
			break;
		}
		return animList;
	}

	// 出场顺序
	private void handleEventArray(BattleEventInfoClient info) {

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		BattleSideInfo downSide = blic.getDownSide();
		BattleSideInfo upSide = blic.getUpSide();

		List<BattleArrayInfoClient> lArray = downSide.getBattleArrayInfo();
		List<BattleArrayInfoClient> rArray = upSide.getBattleArrayInfo();

		List<BattleLogHeroInfoClient> lHero = downSide.getHeroInfos();
		List<BattleLogHeroInfoClient> rHero = upSide.getHeroInfos();

		// List<TroopEffectInfo> lEffect = leftSide.getTroopEffectInfo();
		// List<TroopEffectInfo> rEffect = rightSide.getTroopEffectInfo();

		UserTroopEffectInfo lUserTroopEffectInfo = downSide
				.getUserTroopEffectInfo2();
		UserTroopEffectInfo rUserTroopEffectInfo = upSide
				.getUserTroopEffectInfo2();

		List<BattleArrayInfoClient> lOrderArray = new ArrayList<BattleArrayInfoClient>();
		List<BattleArrayInfoClient> rOrderArray = new ArrayList<BattleArrayInfoClient>();

		if (isDown) {
			if (info.hasActives()) {
				List<BattleEventArmInfo> ls = info.getActives();
				for (BattleEventArmInfo armInfo : ls) {
					BattleArrayInfoClient blic = getBattleArrayInfoClient(true,
							armInfo.getArmid());
					if (blic != null) {
						lOrderArray.add(blic);
					}
				}
				if (ListUtil.isNull(lArray) == false) {
					for (BattleArrayInfoClient lsInfo : lArray) {
						if (lsInfo.getNum() == 0) {
							lOrderArray.add(lsInfo);
						}
					}
				}
			}
			if (info.hasPassive()) {
				List<BattleEventArmInfo> ls = info.getPassives();
				for (BattleEventArmInfo armInfo : ls) {
					BattleArrayInfoClient blic = getBattleArrayInfoClient(
							false, armInfo.getArmid());
					if (blic != null) {
						rOrderArray.add(blic);
					}
				}
				if (ListUtil.isNull(rArray) == false) {
					for (BattleArrayInfoClient lsInfo : rArray) {
						if (lsInfo.getNum() == 0) {
							rOrderArray.add(lsInfo);
						}
					}
				}
			}

		} else {
			if (info.hasActives()) {
				List<BattleEventArmInfo> ls = info.getActives();
				for (BattleEventArmInfo armInfo : ls) {
					BattleArrayInfoClient blic = getBattleArrayInfoClient(
							false, armInfo.getArmid());
					if (blic != null) {
						rOrderArray.add(blic);
					}
				}
				if (ListUtil.isNull(rArray) == false) {
					for (BattleArrayInfoClient lsInfo : rArray) {
						if (lsInfo.getNum() == 0) {
							rOrderArray.add(lsInfo);
						}
					}
				}
			}
			if (info.hasPassive()) {
				List<BattleEventArmInfo> ls = info.getPassives();
				for (BattleEventArmInfo armInfo : ls) {
					BattleArrayInfoClient blic = getBattleArrayInfoClient(true,
							armInfo.getArmid());
					if (blic != null) {
						lOrderArray.add(blic);
					}
				}
				if (ListUtil.isNull(lArray) == false) {
					for (BattleArrayInfoClient lsInfo : lArray) {
						if (lsInfo.getNum() == 0) {
							lOrderArray.add(lsInfo);
						}
					}
				}
			}
		}

		// 只有守方 有城防技能
		modifyBtlWnd.setAmyOrder(lOrderArray, lHero, true,
				downSide.getMainFighterId() == blic.getDefSide()
						.getMainFighterId() ? downSide.getBattleSkill() : null,
				lUserTroopEffectInfo); // blic.getDetail().getType()
		modifyBtlWnd.setAmyOrder(rOrderArray, rHero, false,
				upSide.getMainFighterId() == blic.getDefSide()
						.getMainFighterId() ? upSide.getBattleSkill() : null,
				rUserTroopEffectInfo); // blic.getDetail().getType());

	}

	// 保存士兵的血量 和数量
	public int modifyNum(BattleEventInfoClient info,
			BattleEventInfoClient demageInfo) {
		if (BattleEvent.BATTLE_EVENT_NUM != BattleEvent
				.valueOf(info.getEvent()))
			return 0;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<BattleEventArmInfo> ls = info.getActives();
		List<BattleEventArmInfo> demageList = demageInfo.getPassives();
		int initCount = 0; // 开始该显示的个数
		int endCount = 0; // 结束该显示的个数

		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
					beai.getArmid());
			int numLoss = 0;
			int fallHp = 0;
			if (baic != null) {
				numLoss = (int) (baic.getNum() - beai.getEx());
				fallHp = (int) (baic.getCurrentHp() - beai.getValue());

				if (i == 0) {
					TroopProp tp = null;
					try {
						tp = (TroopProp) CacheMgr.troopPropCache.get(baic
								.getId());
					} catch (GameException e) {
						e.printStackTrace();
					}
					if (tp != null) {
						initCount = getBattleArmCount(baic.getNum(),
								tp.getFormation());
						endCount = getBattleArmCount(beai.getEx(),
								tp.getFormation());
					}
				}
				baic.setNum(beai.getEx());
				baic.setCurrentHp(beai.getValue());
			}

			if (demageList.size() > 0) {
				BattleEventArmInfo armInfo = demageList.get(i);

				if (beai.getArmid().intValue() == armInfo.getArmid().intValue()) {
					TroopProp tp = null;
					try {
						tp = (TroopProp) CacheMgr.troopPropCache.get(beai
								.getArmid().intValue());
					} catch (GameException e) {
						e.printStackTrace();
					}
					// 设置死的数量
					if (tp != null && tp.isBoss()) {
						armInfo.setValue(new Long(fallHp));
					} else {
						armInfo.setValue(new Long(numLoss));
					}
				}
			}
		}
		return endCount - initCount;
	}

	// 连击的attack
	private void getAttackedContinus(BattleEventInfoClient demageInfo,
			boolean isDown, BattleEventArmInfo it, boolean isFall) {
		handleAttackContinus(demageInfo, it, isFall);
	}

	// 连击事件 单独处理
	private void handleEventContinous(BattleEventInfoClient info,
			BattleEventInfoClient demageInfo) {
		boolean isDown = demageInfo.isDownAct(isMyBattle, isMeAttacker);
		List<BattleEventArmInfo> ls = info.getActives();
		// 连击
		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo it = ls.get(i);
			if (it.getValue() >= 0) {
				getAttackedContinus(demageInfo, isDown, it, true);
			}
		}
	}

	public void handleHeroDemage(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_DEMAGE != BattleEvent.valueOf(info
				.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<BattleEventArmInfo> ls = info.getPassives();
		List<BaseAnim> fallUp = new ArrayList<BaseAnim>();

		List<BattleArrayInfoClient> gurpList = new ArrayList<BattleArrayInfoClient>();

		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			BattleArrayInfoClient baic = getBattleArrayInfoClient(!isDown,
					beai.getArmid());
			if (baic != null) {
				gurpList.add(baic);
			}
		}

		List<BattleEventArmInfo> groupArms = new ArrayList<BattleEventArmInfo>();
		for (int i = 0; i < ls.size(); i++) {
			groupArms.add(ls.get(i));
		}

		fallUp.addAll(modifyBtlWnd.groupHurt(!isDown, groupArms, gurpList,
				false));
		if (fallUp != null) {
			// fallUp.add(modifyBtlWnd.delay(isDown, animPool.stay(610)));
		}
		addAnimList(fallUp);
	}

	// 处理英雄释放技能时候数量变化 英雄未上场的情况下
	public void handleNum(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_NUM != BattleEvent
				.valueOf(info.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<BattleEventArmInfo> ls = info.getActives();
		List<BaseAnim> fallUp = new ArrayList<BaseAnim>();

		List<BattleArrayInfoClient> gurpList = new ArrayList<BattleArrayInfoClient>();

		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			// 把value 差值传

			BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
					beai.getArmid());
			if (baic != null) {
				gurpList.add(baic);
			}
		}

		List<BattleEventArmInfo> groupArms = new ArrayList<BattleEventArmInfo>();
		for (int i = 0; i < ls.size(); i++) {
			groupArms.add(ls.get(i));
		}
		fallUp.addAll(modifyBtlWnd
				.groupHurt(isDown, groupArms, gurpList, false));
		addAnimList(fallUp);
	}

	// 伤害 数量事件 一起解析 收到数量的事件的时候再播放动画 伤害和飘血一起处理
	private void handleEventDemage(
			List<BattleEventInfoClient> battleEventInfoClients) {

		BattleEventInfoClient demageInfoSrc = battleEventInfoClients.get(0);
		BattleEventInfoClient demageInfo = (BattleEventInfoClient) demageInfoSrc
				.deepClone();
		if (demageInfo == null) {
			return;
		}
		BattleEventInfoClient numInfo = battleEventInfoClients.get(1);
		boolean isDown = demageInfo.isDownAct(isMyBattle, isMeAttacker);
		// 修改 兵的数量，修改被打死兵的数量
		// handleNum(numInfo,demageInfo);
		// 如果有多个passive 处理群伤

		// 如果没有上场的情况下 是英雄的技能 只是飘数字 跳过伤害事件
		boolean isSceneArm = false;

		List<BattleEventArmInfo> info = null;// new
												// ArrayList<BattleEventArmInfo>();
		if (demageInfo.hasPassive()) {
			List<BattleEventArmInfo> ls = demageInfo.getPassives();
			if (isDown/*demageInfo.getAttack()*/ == false) {
				for (BattleEventArmInfo armInfo : ls) {
					if (armInfo.getArmid().intValue() == downSceneId) {
						isSceneArm = true;
						break;
					}
				}
			} else {
				for (BattleEventArmInfo armInfo : ls) {
					if (armInfo.getArmid().intValue() == upSceneId) {
						isSceneArm = true;
						break;
					}
				}
			}
		}
		if (isStage == false || isSceneArm == false) {
			modifyNum(numInfo, demageInfo);
			handleHeroDemage(demageInfo);
		} else {
			if (isStage == true && isPause == false) {
				// List<Anim> stay = new ArrayList<Anim>();
				// stay.add(modifyBtlWnd.stay(isLeft,
				// AnimPool.getNullAnim(300)));
				// addDrawAnimList(stay);
				isPause = true;
			}
			//
			if (demageInfo.hasPassive() && demageInfo.getPassives().size() > 1
					&& isStage == true) {
				// 连击
				if (isContinousAtk(demageInfo.getPassives())) {
					handleEventContinous(numInfo, demageInfo);
				} else {
					// 修改 兵的数量，修改被打死兵的数量
					modifyNum(numInfo, demageInfo);
					handleEventDeath(demageInfo);
				}
				return;
			} else {
				// 修改 兵的数量，修改被打死兵的数量
				int deadCount = modifyNum(numInfo, demageInfo);
				{
					handleAttack(demageInfo, null, true, false);
				}
			}
		}
	}

	private void handleEventAct(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_ACT != BattleEvent
				.valueOf(info.getEvent()))
			return;
		// 每回合开始时,重置界面
		// modifyBtlWnd.clearSkillColumn();
		List<BaseAnim> roundLs = new ArrayList<BaseAnim>();
		roundLs.add(modifyBtlWnd.showRound(animPool.showRoundAnim(), round));
		addAnimList(roundLs);
		BattleSideInfo bsi = info.getAttack() ? blic.getAtkSide() : blic
				.getDefSide();
		BattleArrayInfoClient baic = bsi.getCurTroop(info.getActiveId());
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		if (isDown /*info.getAttack()*/) {
			downSceneId = info.getActiveId();
		} else {
			upSceneId = info.getActiveId();
		}
		setPresentTroop(info.getActiveId(), baic, isDown, false);

		// 50s 延迟
		List<BaseAnim> stay = new ArrayList<BaseAnim>();
		stay.add(modifyBtlWnd.delay(isDown, AnimPool.getNullAnim(80)));
		addAnimList(stay);
	}

	private void handleEventTarget(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_TARGET != BattleEvent.valueOf(info
				.getEvent()))
			return;
		boolean isActiveDown = info.isDownAct(isMyBattle, isMeAttacker);

		BattleSideInfo bsi = info.getAttack() ? blic.getDefSide() : blic
				.getAtkSide();
		BattleArrayInfoClient baic = bsi.getCurTroop(info.getPassiveId());
		// 保证自己在左，敌方在右
		boolean isPassiveDown = !isActiveDown;

		if (isActiveDown /*info.getAttack()*/) {
			downSceneId = info.getActiveId();
			upSceneId = info.getPassiveId();
		} else {
			downSceneId = info.getPassiveId();
			upSceneId = info.getActiveId();
		}

		setPresentTroop(info.getPassiveId(), baic, isPassiveDown, true);

		Drawable d = getTroopImage(isActiveDown, info.getActiveId());
		if (!CacheMgr.troopPropCache.isArchor(info.getActiveId())) {
			List<BaseAnim> reach = new ArrayList<BaseAnim>();
			int dis = BattleCoordUtil.armGap + AMY_SHORT_DIS * 2 / 3;
			reach.add(modifyBtlWnd.reachAtkPos(isActiveDown,
					animPool.reachAtkPos(isActiveDown, dis), d, dis));

			addAnimList(reach);
			// List<BaseAnim> reachAtk =
			// modifyBtlWnd.reachAtkTarget(isActiveDown,
			// animPool.reachAtkPos(isActiveDown, dis), d, dis);
			// if(ListUtil.isNull(reachAtk) == false)
			// {
			// addAnimList(reachAtk);
			// }
		}

		// if (!CacheMgr.troopPropCache.isArchor(info.getActiveId()))
		// {
		// List<BaseAnim> stay = new ArrayList<BaseAnim>();
		// stay.add(modifyBtlWnd.delay(isActiveLeft, AnimPool.getNullAnim(50)));
		// addAnimList(stay);
		// }
	}

	// 设置出场部队信息
	private void setPresentTroop(int val, BattleArrayInfoClient baic,
			boolean isDown, boolean isTarget) {
		long amount = baic.getNum();
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(baic.getId());
		} catch (GameException e) {
			e.printStackTrace();
		}
		// 设置数量
		modifyBtlWnd.modifyTroopAmount(isDown, amount, tp.getName());

		// 设置HP
		if (CacheMgr.troopPropCache.needShowHP(baic.getId())) {
			modifyBtlWnd.modifyTroopHP(isDown, baic.getCurrentHp(),
					baic.getHp());
		} else {
			modifyBtlWnd.clearTroopHP(isDown);
		}

		// 设置部队栏信息
		modifyBtlWnd.setSelTroop(isDown, val, isTarget);
		// 进场动画
		List<Anim> enter = new ArrayList<Anim>();
		enter.add(modifyBtlWnd.enterBattleField(isDown,
				animPool.getEnter(isDown), getTroopImage(isDown, val),
				getBattleArmCount(baic.getNum(), tp.getFormation()),
				tp.getFormation(), tp.isBoss()));
		addDrawAnimList(enter);
		List<Buff> ls = baic.getBuffIds();
		if (ls != null)
			for (int i = ls.size() - 1; i >= 0; i--) {
				int value = ls.get(i).buffId;
				Buff buff = ls.get(i);
				if (null != buff) {
					modifyBtlWnd.setSkillAppear(isDown, animPool.skillAppear(),
							AnimPool.getNullAnim(200), buff,
							getSmallBuffIcon((int) value));
				}
			}
	}

	// 1 近战攻击 2 远程射击 3 远程法术
	// 攻击类型开始攻击时动画
	private void attackTypesStart(int type, boolean isDown, boolean isContinus,
			List<Anim> beatenLs) {
		switch (type) {
		case MELEE_ATTACK:
			int dis = (int) (0 * Config.SCALE_FROM_HIGH);// (2 * AMY_SHORT_DIS /
															// 3);
			// int dis = (AMY_SHORT_DIS + BattleCoordUtil.armGap);
			List<BaseAnim> shortAnims = new ArrayList<BaseAnim>();
			BaseAnim anim = modifyBtlWnd.shortRangeAtk(isDown,
					animPool.shortRangeAtk(isDown, dis, 10/* 300 */), dis, 0,
					false);
			if (anim != null)
				shortAnims.add(anim);
			addAnimList(shortAnims);
			break;

		case REMOTE_ATTACK:
			List<BaseAnim> atkFar = new ArrayList<BaseAnim>();

			Animation shortRange = animPool.remoteRangeAtk(isDown,
					-READY_ATTACK_DIS, 250);
			// Animation shortRange = animPool.shortRangeAtk(isDown,
			// -READY_ATTACK_DIS, 300);
			atkFar.add(modifyBtlWnd.shortRangeAtk(isDown, shortRange,
					-READY_ATTACK_DIS, 0, false));
			addAnimList(atkFar);
			break;

		case MAGIC_ATTACK:
			List<BaseAnim> atkMagic = new ArrayList<BaseAnim>();
			atkMagic.add(modifyBtlWnd.shortRangeAtk(isDown,
					animPool.shortRangeAtk(isDown, READY_ATTACK_DIS / 2, 150),
					READY_ATTACK_DIS / 2, 0, false));
			addAnimList(atkMagic);
			break;
		}
	}

	private void attackTypesEnd(int type, boolean isDown, List<Anim> beatenLs,
			boolean isContinus) {
		switch (type) {
		case MELEE_ATTACK:
			// int dis = -(int) (0*Config.SCALE_FROM_HIGH);//-(2 * AMY_SHORT_DIS
			// / 3);
			// // int dis = -(AMY_SHORT_DIS + BattleCoordUtil.armGap);
			// // int dis =-(3 * AMY_SHORT_DIS + BattleCoordUtil.armGap);
			// // int dis = -AMY_SHORT_DIS;
			// beatenLs.add(modifyBtlWnd.shortRangeAtkBounce(isDown, animPool
			// .shortRangeAtkBounce(isDown, dis, 100 /* 200 */, 100/* 200 */),
			// dis));
			break;
		case REMOTE_ATTACK:
			Animation bounce = animPool.shortRangeAtkBounce(isDown,
					READY_ATTACK_DIS, 0, 50/* 100 */);
			beatenLs.add(modifyBtlWnd.shortRangeAtkBounce(isDown, bounce,
					READY_ATTACK_DIS));
			break;
		case MAGIC_ATTACK:
			// 回原地
			beatenLs.add(modifyBtlWnd.shortRangeAtkBounce(isDown,
					animPool.shortRangeAtkBounce(isDown, -READY_ATTACK_DIS / 2,
							0, 150), -READY_ATTACK_DIS / 2));
			break;
		}
	}

	private int attackEffect(boolean isDown, List<Anim> beatenLs, int effectId) {
		BattleAnimEffects animEffects = getAnimEffect(effectId, isDown);
		// 特效中所有的图片都下载下来了 才可以启动，否则用默认的
		int frameNum = animEffects.getFrameNum();
		String effectImage = animEffects.getIcon();
		BaseDrawableAnim effectAttackAnim = null;
		// ID 大于3 才使用下载的特效
		if (effectId > 3) {
			boolean isDownload = true; // 是否下载完
			for (int i = 1; i < frameNum+1; i++) {
				FileAccess fa = Config.getController().getFileAccess();
				String name = effectImage + "_" + i + ".png";
				int srcId = Config.getController().findResId(name, "drawable");
				if (srcId <= 0 && fa.checkImageExist(name) == false) {
					isDownload = false;
					break;
				}
			}
			if (!isDownload) // 下载完了才可以启动 没有的话
								// 用默认的
			{
				effectId = effectId / 10;
			}
		}
		animEffects = getAnimEffect(effectId, isDown);
		effectAttackAnim = modifyBtlWnd.effectDrawableAnim(isDown, animEffects);
		// 对动画进行一次检查 是否下载下来了

		if (effectAttackAnim.getAnimationDrawable() == null) {
			System.gc();
			// sleep
			for (int i = 10000; i > 0; i--) {

			}
			// 从缓存中找 如果有同1边的特效 用那个替换
			if (effectId > 3) {
				effectId = effectId / 10;

				// 先取默认的
				animEffects = getAnimEffect(effectId, isDown);
				effectAttackAnim = modifyBtlWnd.effectDrawableAnim(isDown,
						animEffects);
				if (effectAttackAnim.getAnimationDrawable() != null) {
					beatenLs.add(effectAttackAnim);
					return effectId;
				}

				// 如果默认的没有 在缓存中查找是否还有默认的特效 从默认的三套中查找

				for (int index = 1; index <= 3; index++) {
					if (index == effectId) {
						continue;
					}
					animEffects = getAnimEffect(index, isDown);
					BitmapCache bitmapCache = Config.getController()
							.getBitmapCache();
					boolean isExist = true;
					for (int i = 1; i < animEffects.getFrameNum(); i++) {
						String mirrorImg = "";
						if (animEffects.getHoriMirror() != 0
								|| animEffects.getVerticalMirror() != 0
								|| animEffects.getRotateDegress() != 0) {
							mirrorImg = ImageUtil.imageMirrorName(mirrorImg)
									+ "_" + animEffects.getRotateDegress();
						} else {
							mirrorImg = ImageUtil.imageScaleName(mirrorImg,
									(int) animEffects.getXScale(),
									(int) animEffects.getYScale());
						}
						Bitmap bmp = bitmapCache.get(mirrorImg);
						if (bmp == null) {
							isExist = false;
							break;
						}
					}
					if (isExist == true) {
						effectAttackAnim = modifyBtlWnd.effectDrawableAnim(
								isDown, animEffects);
						beatenLs.add(effectAttackAnim);
						return index;
					}
				}
			}
			return -1;
		} else {
			beatenLs.add(effectAttackAnim);
		}
		return effectId;
	}

	// 伤害通用事件处理 连击和普通攻击 分开处理
	// 是否和掉血连在一起处理 index 是处理连击的第几下，单一攻击、群伤参数传-1
	private void handleAttack(BattleEventInfoClient info,
			BattleEventArmInfo it, boolean isFall, boolean isShort) {
		int skillAction = 0; // 技能动作 0是默认动作 1近战攻击 2远程射击 3远程法术
		int animType = 0; // 技能特效 0是默认的特效
		int skillId = info.getValue();
		// 如果是技能伤害 服务端会带下来 不是则使用 保存的
		if (skillId == 0) {
			if (info.getAttack() == true) {
				if (downSkillId != 0) {
					skillId = downSkillId;
					downSkillId = 0;
				}
			} else {
				if (upSkillId != 0) {
					skillId = upSkillId;
					upSkillId = 0;
				}
			}
		} else {
			if (info.getAttack() == true) {
				downSkillId = 0;
			} else {
				upSkillId = 0;
			}
		}
		// 通过技能ID 得到攻击动作、和技能特效 类型
		if (skillId != 0) {
			skillAction = getSkillAction(skillId);
			animType = getAnimType(skillId);
		}
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<Anim> beatenLs = new ArrayList<Anim>();
		// 攻方
		TroopProp tp = null;
		BattleArrayInfoClient atk_baic = null;
		int atkNum = 0;
		// 防守方
		int pass_count = 0;
		TroopProp tp_passive = null;
		BattleArrayInfoClient baic = null;
		BattleEventArmInfo beai = null;

		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}

		if (info.hasActives()) {
			atk_baic = getBattleArrayInfoClient(isDown, info.getActiveId());
			if (atk_baic != null) {
				atkNum = getBattleArmCount(atk_baic.getNum(), tp.getFormation());
			}
		}

		// 如果技能中攻击类型和攻击特效 单独处理
		if (animType == 0) {
			animType = setAnimValue(info, animType);
		}

		if (skillAction == 0) {
			skillAction = setSkillAction(info, skillAction);
		}

		attackTypesStart(skillAction, isDown, true, beatenLs);
		if (info.hasPassive()) {
			animType = attackEffect(!isDown, beatenLs, animType);
			{
				if (skillAction == 1) {
					List<Anim> ratateAnims = modifyBtlWnd.startReadyEffects(
							isDown, AnimPool.soldierRotate(isDown), false,
							false, 0);

					if (ListUtil.isNull(ratateAnims) == false) {
						beatenLs.addAll(ratateAnims);
					}
				}

			}

		}
		attackTypesEnd(skillAction, isDown, beatenLs, true);
		if (info.hasPassive()) {
			// isArchor
			boolean isArchor = CacheMgr.troopPropCache.isArchor(info
					.getPassiveId());
			// 受击
			Drawable d = getTroopImage(isDown, info.getActiveId());
			// 修改兵的数量
			List<BattleEventArmInfo> ls = info.getPassives();
			if (it != null) {
				beai = it;
				baic = getBattleArrayInfoClient(!isDown, beai.getArmid());
			} else {
				beai = ls.get(0);
				baic = getBattleArrayInfoClient(!isDown, beai.getArmid());
			}

			if (baic != null) {
				try {
					tp_passive = (TroopProp) CacheMgr.troopPropCache.get(baic
							.getId());
				} catch (GameException e) {
					e.printStackTrace();
				}
				// 如果是boss 直接判断
				if (tp_passive.isBoss()) {
					if (baic.getCurrentHp() > 0) {
						pass_count = 1;
					} else {
						pass_count = 0;
					}
				} else {
					pass_count = getBattleArmCount(baic.getNum(),
							tp_passive.getFormation());
				}
			}
			int type = 0;
			if (tp != null) {
				type = tp.getType();
			}
			if (animType == 3) {
				type = 5;
			}
			int totalHp = (int) baic.getHp();
			int currentHp = (int) baic.getCurrentHp();
			List<Anim> hitAnim = new ArrayList<Anim>();
			hitAnim = modifyBtlWnd.getHit(!isDown, d, isArchor, pass_count,
					type, currentHp, totalHp, beai, baic);
			if (hitAnim != null && hitAnim.size() > 0) {
				if (animType != -1) {
					beatenLs.addAll(hitAnim);
				}
			}
		}
		// 飘数字 和掉血 一起处理
		if (isFall) {
			List<BaseAnim> fallup = handFallHp(beai, !isDown, baic);
			if (fallup != null && fallup.size() > 0) {
				beatenLs.addAll(fallup);
			}

		}
		addDrawAnimList(beatenLs);
	}

	private int setAnimValue(BattleEventInfoClient info, int animType) {
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}

		if (tp != null && info.hasActives()) {
			if (CacheMgr.troopPropCache.isNearAtkTroop(info.getActiveId())) {
				animType = DEFEALT_MELEE_ATTACK;
			} else {
				if (tp.getType() == 5) {
					animType = DEFEALT_MAGIC_ATTACK;
				} else {
					animType = DEFEALT_REMOTE_ATTACK;
				}
			}
		}
		return animType;
	}

	private int setSkillAction(BattleEventInfoClient info, int skillAction) {
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}
		if (tp != null && info.hasActives()) {
			if (CacheMgr.troopPropCache.isNearAtkTroop(info.getActiveId())) {
				skillAction = MELEE_ATTACK;
			} else {
				if (tp.getType() == 5) {
					skillAction = MAGIC_ATTACK;
				} else {
					skillAction = REMOTE_ATTACK;
				}
			}
		}
		return skillAction;

	}

	private void handleAttackContinus(BattleEventInfoClient info,
			BattleEventArmInfo it, boolean isFall) {
		int skillAction = 0; // 技能动作 0是默认动作 1近战攻击 2远程射击 3远程法术
		int animType = 0; // 技能特效 0是默认的特效
		int skillId = info.getValue();
		// 通过技能ID 得到攻击动作、和技能特效 类型
		if (skillId != 0) {
			skillAction = getSkillAction(skillId);
			animType = getAnimType(skillId);
		}
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<Anim> atkLs = new ArrayList<Anim>();
		List<Anim> beatenLs = new ArrayList<Anim>();
		// 攻方
		TroopProp tp = null;
		BattleArrayInfoClient atk_baic = null;
		int atkNum = 0;
		// 防守方
		int pass_count = 0;
		TroopProp tp_passive = null;
		int passiveStartCount = 0;
		BattleArrayInfoClient baic = null;
		BattleEventArmInfo beai = null;

		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}
		if (info.hasActives()) {
			atk_baic = getBattleArrayInfoClient(isDown, info.getActiveId());
			if (atk_baic != null) {
				atkNum = getBattleArmCount(atk_baic.getNum(), tp.getFormation());
			}
		}

		if (animType == 0) {
			animType = setAnimValue(info, animType);
		}

		if (skillAction == 0) {
			skillAction = setSkillAction(info, skillAction);
		}

		attackTypesStart(skillAction, isDown, true, beatenLs);
		if (info.hasPassive()) {
			animType = attackEffect(!isDown, beatenLs, animType);
			{
				if (skillAction == 1) {
					List<Anim> ratateAnims = modifyBtlWnd.startReadyEffects(
							isDown, AnimPool.soldierRotate(isDown), false,
							false, 0);

					if (ListUtil.isNull(ratateAnims) == false) {
						beatenLs.addAll(ratateAnims);
					}
				}

			}

		}
		attackTypesEnd(skillAction, isDown, beatenLs, true);

		if (info.hasPassive()) {
			// isArchor
			boolean isArchor = CacheMgr.troopPropCache.isArchor(info
					.getPassiveId());
			// 受击
			Drawable d = getTroopImage(isDown, info.getActiveId());
			// 修改兵的数量
			List<BattleEventArmInfo> ls = info.getPassives();
			if (it != null) {
				beai = it;
			} else {
				beai = ls.get(0);
			}
			baic = getBattleArrayInfoClient(!isDown, beai.getArmid());

			if (baic != null) {
				try {
					tp_passive = (TroopProp) CacheMgr.troopPropCache.get(baic
							.getId());
				} catch (GameException e) {
					e.printStackTrace();
				}
				int numLoss = (int) (baic.getNum() - beai.getEx());
				int fallHp = (int) (baic.getCurrentHp() - beai.getValue());
				passiveStartCount = getBattleArmCount(baic.getNum(),
						tp_passive.getFormation());
				baic.setNum(beai.getEx()); // 修改数量
				baic.setCurrentHp(beai.getValue()); // 修改血量

				if (tp.isBoss()) {
					beai.setValue(new Long(fallHp));
				} else {
					beai.setValue(new Long(numLoss));
				}

				// 结束时候的士兵个数
				// 如果是boss 直接判断
				if (tp_passive.isBoss()) {
					if (baic.getCurrentHp() > 0) {
						pass_count = 1;
					} else {
						pass_count = 0;
					}
				} else {
					pass_count = getBattleArmCount(baic.getNum(),
							tp_passive.getFormation());
				}
			}

			int totalHp = (int) baic.getHp();
			int currentHp = (int) baic.getCurrentHp();
			List<Anim> beat = modifyBtlWnd.getHitContinus(!isDown, d, isArchor,
					passiveStartCount, pass_count, tp.getType(), currentHp,
					totalHp);
			if (beat != null) {
				if (animType != -1) {
					beatenLs.addAll(beat);
				}
			}

		}

		BattleArrayInfo arrayInfo = new BattleArrayInfo();
		arrayInfo.setPropid(beai.getArmid());
		arrayInfo.setNum(beai.getEx());
		BattleArrayInfoClient arrayInfoClient = new BattleArrayInfoClient(
				arrayInfo);
		arrayInfoClient.setCurrentHp(beai.getValue());
		arrayInfoClient.setHp(baic.getHp());

		// 飘数字 和掉血 一起处理
		if (isFall) {
			List<BaseAnim> fallup = handFallHp(beai, !isDown, baic);
			if (fallup != null && fallup.size() > 0) {
				beatenLs.addAll(fallup);
			}
		}
		addDrawAnimList(atkLs);
		addDrawAnimList(beatenLs);
	}

	// 伤害、群伤、连击 都是这1个事件
	private void handleEventAttack(BattleEventInfoClient info) {
		// 如果有多个passive 处理群伤
		if (info.hasPassive() && info.getPassives().size() > 1) {
			handleEventDeath(info);
			return;
		}
		// 单一伤害
		handleAttack(info, null, true, false);
	}

	// 士兵的kill buff 是一个动画 士兵的技能只有上场才能释放
	private void handleEventArmSkill(BattleEventInfoClient info) {
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		boolean isPlaySkill = false; // 是否播放技能动画
		if (isStage == true) {
			if (isDown) {
				if (info.hasActives() && info.getActiveId() == downSceneId) {
					isPlaySkill = true;
				}
			} else {
				if (info.hasActives() && info.getActiveId() == upSceneId) {
					isPlaySkill = true;
				}
			}
		}
		if (!isPlaySkill) {
			return;
		}

		boolean isArchor = CacheMgr.troopPropCache.isArchor(info.getActiveId());

		String name = "";
		String img = "";
		int ex = 2;
		if (info.hasActives()) {
			// 等于2 是技能
			ex = info.getActive().getEx();
			if (ex == 2) {
				name = info.getSkillName();
			} else if (ex == 1) {
				name = info.getBuffName();
			}
		}
		img = info.getIcon();
		// Log.d("battleDriver", "技能名:" + name + "ex值: " + ex);
		// int animType = info.getSkillAnimType();
		// 先放大
		List<BaseAnim> large = new ArrayList<BaseAnim>();
		// 技能图标显示
		List<BaseAnim> skillIcon = new ArrayList<BaseAnim>();

		List<BaseAnim> skillName = new ArrayList<BaseAnim>();
		// 旋转
		List<BaseAnim> rotate = new ArrayList<BaseAnim>();
		// 消失
		List<BaseAnim> disappear = new ArrayList<BaseAnim>();
		// 清除
		List<BaseAnim> clear = new ArrayList<BaseAnim>();

		large.add(modifyBtlWnd.armSkillAppear(isDown, animPool.skillBgAppear()));

		skillIcon.add(modifyBtlWnd.armSkillImageAppear(
				isDown,
				animPool.skillImageAppear(),
				getSkillIcon(ex, isNewLog,
						(int) (info.getActive().getValue().longValue())), img));

		skillName.add(modifyBtlWnd.armSkillNameAppear(isDown,
				animPool.getNullAnimSkillName(), name));

		rotate.add(modifyBtlWnd.armSkillRotate(isDown, animPool.skillRotate()));
		disappear.add(modifyBtlWnd.armSkilldisppear(isDown,
				animPool.skillDisappearNew()));

		clear.add(modifyBtlWnd.armSkillClear(isDown, AnimPool.getNullAnim(30)));

		addAnimList(large);
		addAnimList(skillIcon);
		addAnimList(skillName);
		addAnimList(rotate);
		addAnimList(disappear);
		addAnimList(clear);
	}

	private String getHeroImage(BattleLogHeroInfoClient heroInfoClient,
			boolean isDown) {
		if (heroInfoClient == null) {
			return null;
		}
		HeroProp heroProp = heroInfoClient.getHeroProp();
		if (heroProp != null) {
			return heroProp.getImg();
		}

		return "";
	}

	// 组合技能
	private void handleEventCombiSkill(BattleEventInfoClient info) {
		int skiilId = (int) (info.getActive().getValue().longValue());
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		// isDown = false;
		int animType = info.getSkillAnimType();
		int skillBgId = animType < 3 ? R.drawable.skill_bg
				: R.drawable.skill_bg1;
		List<BaseAnim> bg = new ArrayList<BaseAnim>();
		bg.add(modifyBtlWnd.showBg(animPool.skillBg())); // getNullAnim()
		addAnimList(bg);

		List<BaseAnim> large = new ArrayList<BaseAnim>();
		List<BaseAnim> move = new ArrayList<BaseAnim>();
		List<BaseAnim> move_flyings = new ArrayList<BaseAnim>();
		List<BaseAnim> move_name = new ArrayList<BaseAnim>();
		List<BaseAnim> zoom_name = new ArrayList<BaseAnim>();
		List<BaseAnim> stay = new ArrayList<BaseAnim>();
		List<BaseAnim> last = new ArrayList<BaseAnim>();

		List<BattleLogHeroInfoClient> blhic = isDown ? blic.getDownSide()
				.getHeroInfos() : blic.getUpSide().getHeroInfos();
		// 背景放大
		// isDown = false;
		large.add(modifyBtlWnd.moveSkillBg(animPool.skillLightLargeOut(),
				false, skillBgId, isDown));
		addAnimList(large);
		// 移动英雄
		Drawable d = null; // 英雄高清大图
		int heroCount = 0; // 组合英雄 英雄个数
		if (null != blhic && blhic.size() > 0) {
			List<Integer> heroIds = getComboHeroId(skiilId, blhic);
			{
				int hero1Id = heroIds.get(0);
				int hero2Id = heroIds.get(1);
				int hero3Id = heroIds.get(2);
				BattleLogHeroInfoClient major = null;// new
														// BattleLogHeroInfoClient();
				List<BattleLogHeroInfoClient> second = new ArrayList<BattleLogHeroInfoClient>();

				for (BattleLogHeroInfoClient battleLogHeroInfoClient : blhic) {
					if (battleLogHeroInfoClient.getHeroId() == hero1Id
							|| battleLogHeroInfoClient.getHeroId() == hero2Id
							|| battleLogHeroInfoClient.getHeroId() == hero3Id) {
						if (battleLogHeroInfoClient.getRole() == 1
								|| battleLogHeroInfoClient.getRole() == 3) {
							// 副将中是否已经有这个
							boolean isExistSecond = false;
							if (ListUtil.isNull(second) == false) {
								for (int i = 0; i < second.size(); i++) {
									if (second.get(i).getHeroId() == battleLogHeroInfoClient
											.getHeroId()) {
										isExistSecond = true;
										break;
									}
								}

							}
							if (isExistSecond == false) {
								major = battleLogHeroInfoClient;
							}

						} else {
							// 如果主将已有这个id　　则不加入
							if (major != null) {
								if (major.getHeroId() == battleLogHeroInfoClient
										.getHeroId()) {
									continue;
								}
							}
							second.add(battleLogHeroInfoClient);
						}
					}
				}
				// 三个英雄的连体
				if (hero1Id != 0 && hero2Id != 0 && hero3Id != 0
						&& blhic.size() == 3) {
					// 先是第一个副将
					List<BaseAnim> move1 = new ArrayList<BaseAnim>();
					if (ListUtil.isNull(second) == false) {
						BattleLogHeroInfoClient secondOffser1 = second.get(0);

						String img = getHeroImage(secondOffser1, isDown);
						move1.add(modifyBtlWnd.moveHeroIcon(
								animPool.showHeroIcon(isDown), false, isDown,
								d, 0, img));
						addAnimList(move1);
					}
					// 主将
					if (major != null) {
						List<BaseAnim> move2 = new ArrayList<BaseAnim>();
						// d = getHeroHdImage(major, !isDown);
						String img1 = getHeroImage(major, isDown);
						move2.add(modifyBtlWnd.moveHeroIcon(
								animPool.showHeroIconThird(isDown, 0, 200),
								false, isDown, d, 2, img1));
						addAnimList(move2);
					}
					// 第2个副将
					if (ListUtil.isNull(second) == false && second.size() > 1) {
						List<BaseAnim> move3 = new ArrayList<BaseAnim>();
						BattleLogHeroInfoClient secondOffser2 = major;// second.get(1);
						// d = getHeroHdImage(secondOffser2, !isDown);
						String img2 = getHeroImage(secondOffser2, !isDown);
						move3.add(modifyBtlWnd.moveHeroIcon(
								animPool.showHeroIconSecond(isDown, 0, 200),
								false, isDown, d, 1, img2));
						addAnimList(move3);
					}
					heroCount = 3;
				} else {
					if (major != null) {
						List<BaseAnim> moveMajor = new ArrayList<BaseAnim>();
						// d = getHeroHdImage(major, isDown);
						boolean isComboHero = false;
						if (hero1Id != 0 && major.getHeroId() == hero1Id) {
							isComboHero = true;
						}
						if (hero2Id != 0 && major.getHeroId() == hero2Id) {
							isComboHero = true;
						}
						if (hero3Id != 0 && major.getHeroId() == hero3Id) {
							isComboHero = true;
						}
						if (isComboHero) {
							String img = getHeroImage(major, isDown);
							moveMajor.add(modifyBtlWnd.moveHeroIcon(
									animPool.showHeroIcon(isDown), false,
									isDown, d, 0, img));
							addAnimList(moveMajor);
						}
					}
					for (int i = 0; i < second.size(); i++) {
						List<BaseAnim> moveSecond = new ArrayList<BaseAnim>();
						// 第一个副将
						BattleLogHeroInfoClient secondOffser = second.get(i);
						if (secondOffser != null) {
							boolean isComboHero = false;
							if (hero1Id != 0
									&& secondOffser.getHeroId() == hero1Id) {
								isComboHero = true;
							}
							if (hero2Id != 0
									&& secondOffser.getHeroId() == hero2Id) {
								isComboHero = true;
							}
							if (hero3Id != 0
									&& secondOffser.getHeroId() == hero3Id) {
								isComboHero = true;
							}
							if (isComboHero) {
								String img1 = getHeroImage(secondOffser, isDown);
								moveSecond
										.add(modifyBtlWnd.moveHeroIcon(animPool
												.showHeroIconSecond(isDown, 0,
														250), false, isDown, d,
												1, img1));
								addAnimList(moveSecond);
							}
						}
					}
					heroCount = 2;
				}
			}
		}
		addAnimList(move);
		// 移动将领的花
		move_flyings.add(modifyBtlWnd.skillFling(animPool.moveSkill(isDown),
				false, isDown));
		addAnimList(move_flyings);
		// 移动英雄名字

		Drawable skillName = null;
		String effectName = info.getSkillEffect();
		if (TextUtils.isEmpty(effectName) == false) {
			skillName = Config.getController().getDrawable(effectName);
			// skillName = getSkillName(isDown, info.getActiveId());
			if (skillName != null) {
				int dis = 0;//
				if (heroCount == 3) {
					dis = (int) (186 * Config.SCALE_FROM_HIGH);
				} else {
					dis = (int) (Config.screenWidth / 2 + 43 * Config.SCALE_FROM_HIGH);
				}
				move_name.add(modifyBtlWnd.moveSkillName("",
						animPool.moveSkillName(isDown, dis), false, isDown,
						skillName));
			}
		}
		addAnimList(move_name);

		if (isStage == true) {
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, 0));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, 1));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, -1));

			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 2));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 3));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 4));
		} else {
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, 0));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, 1));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, -1));

			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 2));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 3));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 4));

			for (BaseAnim baseAnim : stay) {
				baseAnim.setNeedEnd(false);
			}
		}
		addAnimList(stay);
		// 技能消失
		// last.add(modifyBtlWnd.clearSkill(animPool.getClearAnim()));

		addAnimList(last);
		// addAnimList(zoom_name);
	}

	// 处理英雄的特效
	private void handleHeroEffect(BattleEventInfoClient info, boolean isAttack,
			boolean isDefend, boolean isGain) {
		List<Anim> heroEffect = new ArrayList<Anim>();
		heroEffect.add(modifyBtlWnd.heroEffectAnim(isAttack, isDefend, isGain));
		addDrawAnimList(heroEffect);
	}

	// 英雄出场动画
	private void handleEventHeroSkill(BattleEventInfoClient info) {
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		int animType = info.getSkillAnimType();
		int skillBgId = animType < 3 ? R.drawable.skill_bg
				: R.drawable.skill_bg1;
		List<BaseAnim> bg = new ArrayList<BaseAnim>();
		bg.add(modifyBtlWnd.showBg(animPool.skillBg())); // getNullAnim()

		List<BaseAnim> large = new ArrayList<BaseAnim>();
		List<BaseAnim> move = new ArrayList<BaseAnim>();
		List<BaseAnim> move_flyings = new ArrayList<BaseAnim>();
		List<BaseAnim> move_name = new ArrayList<BaseAnim>();
		List<BaseAnim> zoom_name = new ArrayList<BaseAnim>();
		List<BaseAnim> stay = new ArrayList<BaseAnim>();
		List<BaseAnim> last = new ArrayList<BaseAnim>();

		List<BattleLogHeroInfoClient> blhic = isDown ? blic.getDownSide()
				.getHeroInfos() : blic.getUpSide().getHeroInfos();

		// 背景放大
		large.add(modifyBtlWnd.moveSkillBg(animPool.skillLightLargeOut(),
				false, skillBgId, isDown));
		// 移动英雄
		Drawable d = null; // 英雄高清大图
		String img = "";
		if (null != blhic && blhic.size() > 0) {
			// 英雄 根据技能id来得到
			int skillId = 0;
			if (info.hasActives()) {
				skillId = info.getActive().getValue().intValue();
			}
			if (skillId != 0) {
				int idx = getHeroIdxBySkill(isDown, skillId);
				if (idx != -1 && idx < blhic.size()) {
					BattleLogHeroInfoClient heroInfo = blhic.get(idx);
					if (heroInfo != null) {
						HeroProp heroProp = heroInfo.getHeroProp();
						img = heroProp.getImg();
						move.add(modifyBtlWnd.moveHeroIcon(
								animPool.showHeroIcon(isDown), false, isDown,
								d, 0, img));
					}
				}
			}

			// for (BattleLogHeroInfoClient heroInfo : blhic) {
			// if (heroInfo.getRole() == 1 || heroInfo.getRole() == 3) {
			// HeroProp heroProp = heroInfo.getHeroProp();
			// if (heroProp != null)
			// img = heroProp.getImg();
			// }
			// }
			// if (d == null) {
			// // 采用默认图片
			// // d = getHeroImage(!isDown, info.getActiveId());
			// }
			//
			// move.add(modifyBtlWnd.moveHeroIcon(animPool.showHeroIcon(isDown),
			// false, isDown, d, 0, img));
		}
		// 移动将领的花
		move_flyings.add(modifyBtlWnd.skillFling(animPool.moveSkill(isDown),
				false, isDown));
		// 移动英雄名字

		Drawable skillName = null;
		String effectName = info.getSkillEffect();
		if (TextUtils.isEmpty(effectName) == false) {
			skillName = Config.getController().getDrawable(effectName);
			if (skillName != null) {
				int dis = (int) (186 * Config.SCALE_FROM_HIGH);
				move_name.add(modifyBtlWnd.moveSkillName("",
						animPool.moveSkillName(isDown, dis), false, isDown,
						skillName));
			}
		}

		if (isStage == true) {
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, 0));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, 1));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, 3), false, isDown, -1));

			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 2));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 3));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, 2), false, isDown, 4));
		} else {
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, 0));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, 1));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(0, isDown, Animation.INFINITE),
					false, isDown, -1));

			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 2));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 3));
			stay.add(modifyBtlWnd.skillLight(
					animPool.getLightAnimation(80, isDown, Animation.INFINITE),
					false, isDown, 4));

			for (BaseAnim baseAnim : stay) {
				baseAnim.setNeedEnd(false);
			}
		}

		addAnimList(bg);
		addAnimList(large);
		addAnimList(move);
		addAnimList(move_flyings);
		addAnimList(move_name);
		addAnimList(zoom_name);
		addAnimList(stay);
		addAnimList(last);

	}

	private void handleEventSkill(List<BattleEventInfoClient> skillInfo) {
		// 是英雄技能 或者 士兵技能 技能喊话 如果有配置则用配置
		// 0是士兵的技能
		BattleEventInfoClient info = skillInfo.get(0);
		BattleEventInfoClient nextInfo = skillInfo.get(1);

		int skillId = 0;
		if (info.hasActives()) {
			skillId = info.getActive().getValue().intValue();
		}
		if (skillId == 0) {
			return;
		}
		// buff
		if (info.getValue() == 0 && info.hasActives()
				&& info.getActive().getEx() == 1) {

			if (isIconDisplay((int) skillId) == false || isStage == false) {
				return;
			}
			boolean isPlayBuff = false; // 是否播放释放buff动画 没上场释放buff不显示
			if (isStage == true) {
				boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
				if (isDown) {
					if (info.hasActives() && info.getActiveId() == downSceneId) {
						isPlayBuff = true;
					}
				} else {
					if (info.hasActives() && info.getActiveId() == upSceneId) {
						isPlayBuff = true;
					}
				}
			}
			if (isPlayBuff) {
				handleEventArmSkill(info);
			}
			return;
		}

		BattleSkill battleSkill = null;
		try {
			battleSkill = (BattleSkill) CacheMgr.battleSkillCache.get(skillId);
		} catch (GameException exception) {
			exception.printStackTrace();
		}

		if (battleSkill == null) {
			return;
		}

		// 英雄技能
		if (info.getValue() == 1) {
			boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
			boolean nextIsDown = nextInfo.isDownAct(isMyBattle, isMeAttacker);
			// 如果是英雄技能 针对下一帧做处理
			if (battleSkill.getMainType() == 7) {
				handleEventCombiSkill(info);
			} else {
				handleEventHeroSkill(info);
			}
			// 出场的时候 才有这个动画
			if (isStage == false) {
				// 伤害 对对方的处理

				int gain = battleSkill.getGain(); // 1是增益 2是损益
				boolean isGain = false;
				if (gain == 1) {
					isGain = true;
				} else if (gain == 2) {
					isGain = false;
				}

				// handleHeroEffect(nextInfo,true,true,isGain);
				if (nextInfo.getValue() == skillId) {
					if (isDown == nextIsDown) // 对自己
					{
						// 伤害事件 对对方
						if (BattleEvent.valueOf(nextInfo.getEvent()) == BattleEvent.BATTLE_EVENT_DEMAGE) {
							handleHeroEffect(nextInfo, isDown, !isDown, isGain);
						} else {
							handleHeroEffect(nextInfo, isDown, isDown, isGain);
						}
					} else // 对对方
					{
						handleHeroEffect(nextInfo, isDown, !isDown, isGain);
					}
				}
			}
			List<BaseAnim> last = new ArrayList<BaseAnim>();
			last.add(modifyBtlWnd.clearSkill(animPool.getClearAnim()));
			addAnimList(last);
		}
		// 士兵技能
		else if (info.getValue() == 0 && info.hasActives()
				&& info.getActive().getEx() == 2) {
			// 保存skillId
			if (info.getAttack()) {
				downSkillId = skillId;
			} else {
				upSkillId = skillId;
			}
			// 根据技能喊话设置
			int skillAt = battleSkill.getSkillAt();
			if (skillAt == 1) {
				handleEventArmSkill(info);
			} else if (skillAt == 3) {
				boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
				boolean nextIsDown = nextInfo.isDownAct(isMyBattle,
						isMeAttacker);
				// 如果是英雄技能 针对下一帧做处理
				if (battleSkill.getMainType() == 7) {
					handleEventCombiSkill(info);
				} else {
					handleEventHeroSkill(info);
				}

				List<BaseAnim> last = new ArrayList<BaseAnim>();
				last.add(modifyBtlWnd.clearSkill(animPool.getClearAnim()));
				addAnimList(last);
			} else {
				if (info.getValue() == 0) {
					handleEventArmSkill(info);
				} else {
					if (battleSkill.getMainType() == 7) {
						handleEventCombiSkill(info);
					} else {
						handleEventHeroSkill(info);
					}
				}

			}
		}
	}

	// 群伤害 给的是hp 如果是boss不转换，如果不是boss把血量转成数量
	private void handleEventDeath(BattleEventInfoClient info) {
		// Log.d("battleDriver", "handleEventDeath--");
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		// 新日志中有群伤
		if (info.hasPassive()) {
			List<BattleEventArmInfo> ls = info.getPassives();
			{
				// 群伤
				String pre = "#btl_minus#";

				BattleSideInfo bsi = info.getAttack() ? blic.getDefSide()
						: blic.getAtkSide();
				// gurpList.remove(0); //从第一个开始
				// 找到被攻击的那个
				int attackedId = 0;
				if (isDown == true) {
					for (int i = 0; i < ls.size(); i++) {
						if (ls.get(i).getArmid().intValue() == upSceneId) {
							attackedId = i;
							break;
						}
					}
				} else {
					for (int i = 0; i < ls.size(); i++) {
						if (ls.get(i).getArmid().intValue() == downSceneId) {
							attackedId = i;
							break;
						}
					}
				}

				TroopProp tp = null;
				try {
					tp = (TroopProp) CacheMgr.troopPropCache.get(ls
							.get(attackedId).getArmid().intValue());
				} catch (GameException e) {
					e.printStackTrace();
				}

				getAttacked(info, isDown, ls.get(attackedId), false, false);
				List<BaseAnim> fallUp = new ArrayList<BaseAnim>();
				// 1 是普通攻击 2 是暴击
				BattleArrayInfoClient baic = getBattleArrayInfoClient(!isDown,
						ls.get(attackedId).getArmid());
				if (baic != null && ls != null
						&& ls.get(attackedId).getValue() != 0) {
					if (2 == ls.get(attackedId).getEx()) {
						pre = "#!force_atk#" + pre;
						fallUp.addAll(getForceAtk(pre, "btl_", !isDown,
								ls.get(attackedId).getValue(), tp, baic, 0));
					} else {
						fallUp.addAll(setTopEffAnim(pre, "btl_", !isDown, ls
								.get(attackedId).getValue(), tp, baic, 0));
					}
				}
				if (baic != null) {
				}

				if (ls.size() > 1) {
					List<BattleArrayInfoClient> gurpList = new ArrayList<BattleArrayInfoClient>();
					// 把血量转成数量
					for (int i = 0; i < ls.size(); i++) {
						if (i != attackedId) {
							BattleEventArmInfo it = ls.get(i);
							BattleArrayInfoClient baic1 = bsi.getCurTroop(it
									.getArmid());
							gurpList.add(baic1);
						}
					}

					List<BattleEventArmInfo> groupArms = new ArrayList<BattleEventArmInfo>();
					for (int i = 0; i < ls.size(); i++) {
						// 把血量改成对应的数量
						if (i != attackedId) {
							groupArms.add(ls.get(i));
						}
					}
					fallUp.addAll(modifyBtlWnd.groupHurt(!isDown, groupArms,
							gurpList, false));
					if (fallUp != null) {
						fallUp.add(modifyBtlWnd.delay(isDown,
								AnimPool.stay(610)));
					}
					addAnimList(fallUp);
				}
			}
		}
	}

	// 盾兵的连击 单独处理
	private void getAttacked(BattleEventInfoClient info, boolean isDown,
			BattleEventArmInfo it, boolean isFall, boolean isShort) {
		handleAttack(info, it, isFall, isShort);
	}

	private boolean isContinousAtk(List<BattleEventArmInfo> ls) {
		if (ListUtil.isNull(ls) || ls.size() == 1)
			return false;

		boolean isCA = true;
		BattleEventArmInfo beai = ls.get(0);
		for (BattleEventArmInfo it : ls) {
			if (it.getArmid().intValue() != beai.getArmid().intValue()) {
				isCA = false;
				break;
			}
		}
		return isCA;
	}

	private void handleEventRunAway(BattleEventInfoClient info) {
		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		Drawable d = getTroopImage(!isDown, info.getActiveId());
		addBaseAnim(modifyBtlWnd.getHelp(isDown, animPool.getTop(0), d));
	}

	public void handleEventRound(BattleEventInfoClient info) {
		if (0 == info.getValue()) {
			BattleSideInfo leftSide = blic.getDownSide();
			BattleSideInfo rightSide = blic.getUpSide();

			List<BattleArrayInfoClient> lArray = leftSide.getBattleArrayInfo();
			List<BattleArrayInfoClient> rArray = rightSide.getBattleArrayInfo();

			List<BattleLogHeroInfoClient> lHero = leftSide.getHeroInfos();
			List<BattleLogHeroInfoClient> rHero = rightSide.getHeroInfos();

			// List<TroopEffectInfo> lEffect = leftSide.getTroopEffectInfo();
			// List<TroopEffectInfo> rEffect = rightSide.getTroopEffectInfo();

			modifyBtlWnd.setTroopInfo(lArray, lHero, true); // blic.getDetail().getType()
			modifyBtlWnd.setTroopInfo(rArray, rHero, false); // blic.getDetail().getType());
		}
	}

	// 处理作战军队数量,数量给的是当前值 ex是数量
	public void handleEventNum(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_NUM != BattleEvent
				.valueOf(info.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		List<BattleEventArmInfo> ls = info.getActives();
		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
					beai.getArmid());
			baic.setNum(beai.getEx());
			baic.setCurrentHp(beai.getValue());

			TroopProp tp = null;
			try {
				tp = (TroopProp) CacheMgr.troopPropCache.get(beai.getArmid());
			} catch (GameException e) {
				e.printStackTrace();
			}

			if (beai.getArmid().intValue() == info.getActiveId()) {
				modifyBtlWnd.modifyTroopHP(isDown, baic.getCurrentHp(),
						baic.getHp());
				modifyTroopNum(isDown, baic.getNum(), baic);
			}
		}
	}

	private void modifyTroopNum(boolean isDown, long value,
			BattleArrayInfoClient baic) {
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(baic.getId());
		} catch (GameException e) {
			e.printStackTrace();
		}
		modifyBtlWnd.modifyTroopAmount(isDown, value, tp.getName());
		modifyBtlWnd.modifyTroopGridAmount(isDown, baic);
	}

	// 处理掉血
	private List<BaseAnim> handFallHp(BattleEventArmInfo beai, boolean isDown,
			BattleArrayInfoClient baic) {
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(beai.getArmid());
		} catch (GameException e) {
			e.printStackTrace();
		}

		String pre = "";// "#btl_amy_hp#";
		pre += beai.getValue() > 0 ? "#btl_minus#" : "#btl_add#";

		List<BaseAnim> fallUp = new ArrayList<BaseAnim>();
		// 把血换成士兵数

		if (beai.getValue() != 0) {
			if (2 == beai.getEx()) {
				pre = "#!force_atk# " + pre;
				// 不是boss 返回数量
				fallUp.addAll(getForceAtk(pre, "btl_", isDown, beai.getValue(),
						tp, baic, 0));
			} else {
				// 不是boss 返回数量
				fallUp.addAll(setTopEffAnim(pre, "btl_", isDown,
						beai.getValue(), tp, baic, 0));
			}
		}
		if (baic != null) {
			// fallUp.add(showHpAnimation(isDown, baic, tp.getName(),
			// tp.isBoss()));
		}
		// addAnimList(fallUp);
		for (BaseAnim anim : fallUp) {
			anim.setFallNum(true);
		}
		return fallUp;
	}

	private BaseAnim showHpAnimation(boolean isDown,
			BattleArrayInfoClient baic, String name, boolean isBoss) {
		return modifyBtlWnd.modifyHpNum(animPool.stay(100), isDown, baic, name,
				isBoss);
	}

	// 掉血给的是差值
	public void handleEventFallHp(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_FALL_HP != BattleEvent.valueOf(info
				.getEvent()))
			return;

		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}

		if (CacheMgr.troopPropCache.needShowHP(info.getActiveId())) {
			// 修改数据
			boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

			List<BattleEventArmInfo> ls = info.getActives();
			for (int i = 0; i < ls.size(); i++) {
				BattleEventArmInfo beai = ls.get(i);
				BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
						beai.getArmid());
				long curHP = baic.getCurrentHp() - beai.getValue();
				baic.setCurrentHp(curHP);
				// 修改当前士兵的HP
				if (beai.getArmid() == info.getActiveId()
						&& 0 != beai.getValue()) {
					String pre = "";// #btl_amy_hp#";
					pre += beai.getValue() > 0 ? "#btl_minus#" : "#btl_add#";
					// 把血换成士兵数
					if (2 == beai.getEx()) {
						pre = "#!force_atk# " + pre;
						// 不是boss 返回数量
						if (tp != null && tp.isBoss() == false) {
							addAnimList(getForceAtk(pre, "btl_", isDown,
									beai.getValue() / baic.getEachArmHp(), tp,
									baic, 0));
						} else {
							addAnimList(getForceAtk(pre, "btl_", isDown,
									beai.getValue(), tp, baic, 0));
						}
					} else {
						// 不是boss 返回数量
						if (tp != null && tp.isBoss() == false) {
							addAnimList(setTopEffAnim(pre, "btl_", isDown,
									beai.getValue() / baic.getEachArmHp(), tp,
									baic, 0));
						} else {
							addAnimList(setTopEffAnim(pre, "btl_", isDown,
									beai.getValue(), tp, baic, 0));
						}
					}
					modifyBtlWnd.modifyTroopHP(isDown, curHP, baic.getHp());
					modifyTroopNum(isDown, baic.getNum(), baic);
				}
			}
		}
	}

	private void handleEventBuffSet(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_BUFF_SET != BattleEvent.valueOf(info
				.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		if (info.hasActives()) {
			// ArrayList<BaseAnim> buffDisappear = new ArrayList<BaseAnim>();
			for (BattleEventArmInfo beai : info.getActives()) {
				// 如果buff icon 不需要显示的话 则不加到list里面
				BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
						beai.getArmid());
				long value = beai.getValue();

				// 图标需要显示才加进去
				if (isIconDisplay((int) value) == true) {
					baic.addBuffId((int) value, beai.getEx());
				}

				// BaseAnim amyBuff = modifyBtlWnd.handleBuf(isDown,
				// animPool.getHandleBufAnim(isDown), beai.getArmid(), //
				// info.getActiveId(),
				// getSmallBuffIcon((int) value));
				// if (amyBuff != null)
				// {
				// buffDisappear.add(amyBuff);
				// }
			}
			// if (buffDisappear != null)
			// {
			// // buffDisappear.add(modifyBtlWnd.stayHp(isDown,
			// // animPool.stay(810)));
			// }
			// addAnimList(buffDisappear);

			if (isStage == true
					&& (isDown && downSceneId != 0 || !isDown && upSceneId != 0)) {
				long value = info.getActive().getValue();
				BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
						info.getActiveId());
				Buff buff = baic.getBuffById((int) value);
				if (null != buff) {
					modifyBtlWnd.setSkillAppear(isDown, animPool.skillAppear(),
							AnimPool.getNullAnim(200), buff,
							getSmallBuffIcon((int) value));
				}
			}
		}
	}

	public void handleEventBuffClear(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_BUFF_CLEAR != BattleEvent.valueOf(info
				.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
				info.getActiveId());
		// 删除buffId
		baic.removeBuffId((int) (info.getActive().getValue().longValue())); // info.getValue()

		// Drawable d = null;
		// int lastBuffId = baic.getLastBuffId();
		// if (lastBuffId > 0)
		// d = getSmallBuffIcon(lastBuffId); // isNewLog,
		modifyBtlWnd.setSkillDisappear(isDown, animPool.skillDisappear(),
				(int) (info.getActive().getValue()).longValue());
	}

	// BATTLE_EVENT_MODIFY_HP value:hp,ex:num
	public void handleEventModifyHP(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_MODIFY_HP != BattleEvent.valueOf(info
				.getEvent()))
			return;

		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(info.getActiveId());
		} catch (GameException e) {
			e.printStackTrace();
		}

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);

		// 找到当前上场的那个
		if (info.hasActives()) {
			List<BattleEventArmInfo> ls = info.getActives();
			int attackedId = -1;
			if (isDown == true) {
				for (int i = 0; i < ls.size(); i++) {
					if (ls.get(i).getArmid().intValue() == downSceneId) {
						attackedId = i;
						break;
					}
				}
			} else {
				for (int i = 0; i < ls.size(); i++) {
					if (ls.get(i).getArmid().intValue() == upSceneId) {
						attackedId = i;
						break;
					}
				}
			}

			if (attackedId != -1) {
				BattleEventArmInfo beai = ls.get(attackedId);
				if (beai.getValue() != 0) {
					BattleArrayInfoClient baic = getBattleArrayInfoClient(
							isDown, ls.get(attackedId).getArmid());
					long lossHp = beai.getValue();// (int) (beai.getValue() -
													// baic.getCurrentHp());
					long loss = beai.getEx();// (int) (beai.getEx() -
												// baic.getNum());

					long curHP = baic.getCurrentHp() + beai.getValue();// baic.getHp()
																		// +
																		// beai.getValue();
					baic.setCurrentHp(curHP);
					if (curHP > baic.getHp()) {
						baic.setHp(curHP);
					}
					// baic.setNum(beai.getEx());
					long num = beai.getEx() + baic.getNum();
					baic.setNum(num);
					String pre = "";// "#btl_amy_hp#";
					if (loss != 0) {
						pre += loss > 0 ? "#btl_add#" : "#btl_minus#";

						if (tp != null && tp.isBoss() == false) {
							addAnimList(setTopEffAnim(pre, "btl_", isDown,
									loss, tp, baic, 0));
						} else {
							addAnimList(setTopEffAnim(pre, "btl_", isDown,
									lossHp, tp, baic, 0));
						}
					}
					// modifyTroopNum(isDown, baic.getNum(), baic);
					// modifyBtlWnd.modifyTroopHP(isDown, baic.getCurrentHp(),
					// baic.getHp());
				}
			}
			BattleSideInfo bsi = info.getAttack() ? blic.getAtkSide() : blic
					.getDefSide();
			List<BaseAnim> fallUp = new ArrayList<BaseAnim>();
			List<BattleArrayInfoClient> gurpList = new ArrayList<BattleArrayInfoClient>();
			// 把血量转成数量
			for (int i = 0; i < ls.size(); i++) {
				if (i != attackedId) {
					BattleEventArmInfo it = ls.get(i);
					BattleArrayInfoClient baic1 = bsi
							.getCurTroop(it.getArmid());
					long lossHp = it.getValue();// (int) (beai.getValue() -
					// baic.getCurrentHp());
					long loss = it.getEx();// (int) (beai.getEx() -

					baic1.setNum(loss + baic1.getNum());
					baic1.setCurrentHp(lossHp + baic1.getCurrentHp());
					if (baic1.getCurrentHp() > baic1.getHp()) {
						baic1.setHp(baic1.getCurrentHp());
					}
					gurpList.add(baic1);
				}
			}

			List<BattleEventArmInfo> groupArms = new ArrayList<BattleEventArmInfo>();
			for (int i = 0; i < ls.size(); i++) {
				// 把血量改成对应的数量
				if (i != attackedId) {
					groupArms.add(ls.get(i));
				}
			}
			if (ListUtil.isNull(groupArms) == false
					&& ListUtil.isNull(gurpList) == false) {
				fallUp.addAll(modifyBtlWnd.groupHurt(isDown, groupArms,
						gurpList, true));
				if (fallUp != null) {
					fallUp.add(modifyBtlWnd.delay(isDown, AnimPool.stay(610)));
				}
				addAnimList(fallUp);
			}

		}
	}

	public void handleEventBout(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_BOUT != BattleEvent.valueOf(info
				.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle, isMeAttacker);
		if (0 != info.getActiveId() && 0 != info.getActive().getValue()) // info.getValue()
			addBaseAnim(modifyBtlWnd.getEscape(isDown, info.getActiveId(),
					animPool.escape(isDown)));

		modifyBtlWnd.clearTroop(info.getInfo());
		modifyBtlWnd.clearSkillColumn();

		List<BattleEventArmInfo> ls = info.getActives();
		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
					beai.getArmid());
			if (baic != null) {
				baic.setNum(beai.getEx());
				baic.setCurrentHp(beai.getValue());
				if (beai.getArmid() == info.getActiveId())
					modifyBtlWnd.modifyTroopHP(isDown, baic.getCurrentHp(),
							baic.getHp());
				modifyTroopNum(isDown, baic.getNum(), baic);
			}
		}

		modifyBtlWnd.clearTroopAmount(true);
		modifyBtlWnd.clearTroopAmount(false);

		modifyBtlWnd.clearTroopHP(true);
		modifyBtlWnd.clearTroopHP(false);
	}

	public BattleLogInfoClient getBlic() {
		return blic;
	}

	private Drawable getSkillName(boolean isDown, int id) {
		Drawable d = Config.getController().getDrawable("skill_name");
		return d;
	}

	private Drawable getHeroImage(boolean isDown, int id) {
		Drawable d = null;// new BitmapDrawable(bmp);
		if (!isDown) {
			d = ImageUtil.getMirrorBitmapDrawable("hero_42002");
		} else {
			d = Config.getController().getDrawable("hero_42002");
		}
		return d;
	}

	// 获取左右方的部队图片
	private Drawable getTroopImage(boolean isDown, int id) {
		TroopProp tp = null;
		try {
			tp = (TroopProp) CacheMgr.troopPropCache.get(id);
		} catch (GameException e) {
			e.printStackTrace();
		}
		Drawable d = null;
		if (tp != null) {
			if (isDown) {
				d = Config.getController().getDrawable(tp.getImageUp());
			} else {
				d = Config.getController().getDrawable(tp.getImageDown());
			}
			if (d == null) {
				if (isDown) {
					d = Config.getController()
							.getDrawable("default_solider_up");
				} else {
					d = Config.getController().getDrawable(
							"default_solider_down");
				}
			}

		}
		return d;
	}

	private boolean isIconDisplay(int id) {
		return CacheMgr.battleBuffCache.getBuffIsDisplay(id);
	}

	private Drawable getSmallBuffIcon(int id) {
		return CacheMgr.battleBuffCache.getBuffDrawable(id, true);
	}

	private Drawable getSkillIcon(int ex, boolean isNewLog, int id) {
		if (ex == 2) {
			return CacheMgr.battleSkillCache.getSkillDrawable(id, false);
		} else {
			return CacheMgr.battleBuffCache.getBuffDrawable(id, false);
		}
	}

	private byte getSkillAction(int id) {
		return CacheMgr.battleSkillCache.getSkillAction(id);
	}

	private int getAnimType(int id) {
		return CacheMgr.battleSkillCache.getAnimType(id);
	}

	private BattleAnimEffects getAnimEffect(int id, boolean isDown) {
		if (isDown) {
			return CacheMgr.battleAnimEffect.getEffectAnim(id, 1);
		} else {
			return CacheMgr.battleAnimEffect.getEffectAnim(id, 2);
		}
	}

	public BattleArrayInfoClient getBattleArrayInfoClient(boolean isDown,
			int troopId) {
		BattleSideInfo bsi = isDown ? blic.getDownSide() : blic.getUpSide();
		return bsi.getCurTroop(troopId);
	}

	private List<BaseAnim> setTopEffAnim(String pre, String numPre,
			boolean isDown, long loss, TroopProp tp,
			BattleArrayInfoClient baic, int startOffset) {
		ArrayList<Integer> valLs = null;
		StringBuilder builder = new StringBuilder();
		builder.append(pre);
		valLs = CalcUtil.parseLong(Math.abs(loss));
		builder.append(ImageUtil.getNumStr(valLs, numPre));

		List<BaseAnim> ls = new ArrayList<BaseAnim>();
		ls.add(modifyBtlWnd.getTopEff(isDown, AnimPool.getTop(startOffset),
				builder.toString(), tp, baic));
		return ls;
	}

	private List<BaseAnim> getForceAtk(String pre, String numPre,
			boolean isDown, long loss, TroopProp tp,
			BattleArrayInfoClient baic, int startOffset) {
		ArrayList<Integer> valLs = null;
		StringBuilder builder = new StringBuilder();
		builder.append(pre);
		valLs = CalcUtil.parseLong(Math.abs(loss));
		builder.append(ImageUtil.getNumStr(valLs, numPre));

		List<BaseAnim> ls = new ArrayList<BaseAnim>();
		ls.add(modifyBtlWnd.getForceAtk(isDown, AnimPool.forceAtk(startOffset),
				builder.toString(), tp, baic));
		return ls;
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	private List<Integer> getComboHeroId(int skillId,
			List<BattleLogHeroInfoClient> heroLogInfos) {
		List<Integer> heroIds = new ArrayList<Integer>();
		heroIds.add(0);
		heroIds.add(0);
		heroIds.add(0);

		ArrayList<Integer> battleHeroList = new ArrayList<Integer>();

		List<SkillCombo> skillsCombo = null;
		//
		skillsCombo = CacheMgr.battleSkillCombo.getComboHeros(skillId);

		for (int i = 0; i < heroLogInfos.size(); i++) {
			BattleLogHeroInfoClient heroInfo = heroLogInfos.get(i);
			if (heroInfo.getHeroId() != 0) {
				battleHeroList.add(heroInfo.getHeroId());
			}

		}
		// skillsCombo = CacheMgr.battleSkillCombo.getComboHeros(skillId);
		// 对进行一次遍历 取到相同的
		if (skillsCombo == null || skillsCombo.size() == 0) {
			return heroIds;
		}
		// 找到相等的一组
		for (SkillCombo combo : skillsCombo) {
			ArrayList<Integer> comboHeroId = new ArrayList<Integer>();
			if (combo.getHero1Id() != 0) {
				comboHeroId.add(combo.getHero1Id());
			}
			if (combo.getHero2Id() != 0) {
				comboHeroId.add(combo.getHero2Id());
			}
			if (combo.getHero3Id() != 0) {
				comboHeroId.add(combo.getHero3Id());
			}
			// 如果是三个将
			if (comboHeroId.size() == 3 && battleHeroList.size() == 3) {
				if ((battleHeroList.get(0).intValue() == comboHeroId.get(0)
						.intValue()
						|| battleHeroList.get(0).intValue() == comboHeroId.get(
								1).intValue() || battleHeroList.get(0)
						.intValue() == comboHeroId.get(2).intValue())
						&& (battleHeroList.get(1).intValue() == comboHeroId
								.get(0).intValue()
								|| battleHeroList.get(1).intValue() == comboHeroId
										.get(1).intValue() || battleHeroList
								.get(1).intValue() == comboHeroId.get(2)
								.intValue())
						&& (battleHeroList.get(2).intValue() == comboHeroId
								.get(0).intValue()
								|| battleHeroList.get(2).intValue() == comboHeroId
										.get(1).intValue() || battleHeroList
								.get(2).intValue() == comboHeroId.get(2)
								.intValue())) {
					int hero1Id = combo.getHero1Id();
					int hero2Id = combo.getHero2Id();
					int hero3Id = combo.getHero3Id();
					heroIds.set(0, hero1Id);
					heroIds.set(1, hero2Id);
					heroIds.set(2, hero3Id);
					return heroIds;
				}
			} else if (comboHeroId.size() == 2)// 如果是2个将
			{
				{
					boolean isExist1 = false;
					boolean isExist2 = false;
					for (int j = 0; j < battleHeroList.size(); j++) {
						if (battleHeroList.get(j).intValue() == comboHeroId
								.get(0).intValue()) {
							isExist1 = true;
						}
						if (battleHeroList.get(j).intValue() == comboHeroId
								.get(1).intValue()) {
							isExist2 = true;
						}

					}
					if (isExist1 == true && isExist2 == true) {
						heroIds.set(0, comboHeroId.get(0).intValue());
						heroIds.set(1, comboHeroId.get(1).intValue());
						return heroIds;
					}

				}

			}
		}
		return heroIds;
	}

	private int getBattleArmCount(long num, int type) {

		int count = 0;
		switch (type) {
		case 1:
			if (num > 20 * 10000) {
				count = 12;
			} else if (num > 10 * 10000) {
				count = 11;
			} else if (num > 5 * 10000) {
				count = 10;
			} else if (num > 2 * 10000) {
				count = 9;
			} else if (num > 1 * 10000) {
				count = 8;
			} else if (num > 5000) {
				count = 7;
			} else if (num > 2000) {
				count = 6;
			} else if (num > 1000) {
				count = 5;
			} else if (num > 500) {
				count = 4;
			} else if (num > 200) {
				count = 3;
			} else if (num > 100) {
				count = 2;
			} else if (num > 0) {
				count = 1;
			} else {
				count = 0;
			}
			break;

		case 2:
			if (num > 20 * 10000) {
				count = 8;
			} else if (num > 10 * 10000) {
				count = 7;
			} else if (num > 5 * 10000) {
				count = 6;
			} else if (num > 2 * 10000) {
				count = 5;
			} else if (num > 5000) {
				count = 4;
			} else if (num > 1000) {
				count = 3;
			} else if (num > 200) {
				count = 2;
			} else if (num > 0) {
				count = 1;
			} else {
				count = 0;
			}
			break;
		case 3: // 象兵
			if (num > 20 * 10000) {
				count = 6;
			} else if (num > 5 * 10000) {
				count = 5;
			} else if (num > 10000) {
				count = 4;
			} else if (num > 2 * 1000) {
				count = 3;
			} else if (num > 200) {
				count = 2;
			} else if (num > 0) {
				count = 2;
			} else {
				count = 0;
			}

			break;

		case 4: // boss
			count = 1;
			break;
		}
		return count;
	}

	// 如果是单个英雄的技能 根据技能ID找到对应的英雄index 再显示对应英雄的头像 在动画里面

	private int getHeroIdxBySkill(boolean isDown, int skillId) {
		int heroId = -1;
		BattleSideInfo downSide = blic.getDownSide();
		BattleSideInfo upSide = blic.getUpSide();

		List<BattleLogHeroInfoClient> heroArray = isDown ? downSide
				.getHeroInfos() : upSide.getHeroInfos();

		if (ListUtil.isNull(heroArray)) {
			return -1;
		}
		int indexList[] = getRandomList();
		for (int i = 0; i < indexList.length; i++) {
			int idx = indexList[i];
			if (idx >= heroArray.size()) {
				continue;
			}
			BattleLogHeroInfoClient heroInfoClient = heroArray.get(idx);
			List<HeroSkillSlotInfoClient> skillSlots = heroInfoClient
					.getSkillInfos();
			if (ListUtil.isNull(skillSlots) == false) {
				for (HeroSkillSlotInfoClient skillSlotInfoClient : skillSlots) {
					if (skillSlotInfoClient.getSkillId() == skillId) {
						return idx;
					}
				}
			}
		}
		return -1;
	}

	// 随机排序算法
	public int[] getRandomList() {
		// 数组长度
		int list[] = { 0, 1, 2 };
		int count = list.length;
		// 结果集
		int[] resultList = new int[count];
		// 用一个LinkedList作为中介
		LinkedList<Integer> temp = new LinkedList<Integer>();
		// 初始化temp
		for (int i = 0; i < count; i++) {
			temp.add((Integer) list[i]);
		}
		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList[i] = (Integer) temp.get(num);
			temp.remove(num);
		}

		return resultList;
	}
}
