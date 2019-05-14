package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseMoveTroopInfo;
import com.vikings.sanguo.protos.BattleEvent;
import com.vikings.sanguo.protos.BattleEventArmInfo;
import com.vikings.sanguo.protos.BattleEventInfo;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.protos.BattleLogReturnInfo;
import com.vikings.sanguo.protos.MoveTroopInfo;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.protos.ThingType;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class BattleLogInfoClient {
	// 我作为主战一方的战斗，我的信息总在左上，否则attacker在左上
	private BattleSideInfo atkSide; // 进攻方信息
	private BattleSideInfo defSide; // 防守方信息
	private BattleSideInfo downSide; // 左侧战斗方信息  改成下侧   便于理解
	private BattleSideInfo upSide; // 右侧战斗方信息 改成上侧 
	private BattleDetailInfo detail;
	private List<BattleEventInfoClient> eventInfoClientList;
	private final String default_bg = "battle_map.jpg";
	private String bgName;

	private String wallName;

	// 保存所有技能特效的icon 包括放大 缩小
	private HashSet<String> battleEffectImgs = new HashSet<String>();

	// private HashSet<Herop> comboHeros = new HashSet<String>();

	// 在子线程中初始化
	public void init(BattleLogInfo info) throws GameException {
		CacheMgr.battleBuffCache.checkLoad();
		setBothSidesInfo(info);
		setBattleDetailInfo(info);
		setBattleEventClientList(info);
		if (1 == info.getType() || 2 == info.getType()) {
			bgName = CacheMgr.battleBgPropCache.getBattleBg(
					info.getDefendFiefid(), info.getDefendFiefPropid(),
					isDownAtk());

			wallName = CacheMgr.battleBgPropCache.getWall(
					info.getDefendFiefid(), info.getDefendFiefPropid(),
					isDownAtk());
		}
		//设置左右那边的军队信息  从事件中去取     第一条记录就是军队信息的
		setTroopInfo();
	}

	public void init(BattleLogInfo info, String bgName, String wallName)
			throws GameException {
		this.init(info);
		this.bgName = bgName;
		this.wallName = wallName;
	}

	private void setBattleEventClientList(BattleLogInfo info) {
		eventInfoClientList = new ArrayList<BattleEventInfoClient>();
		List<BattleEventInfo> ls = info.getEventInfosList();
		if (!ListUtil.isNull(ls)) {
			for (BattleEventInfo it : ls)
				eventInfoClientList.add(new BattleEventInfoClient(it));
		}
	}

	private void setBattleDetailInfo(BattleLogInfo info) throws GameException {
		detail = new BattleDetailInfo();

		// 设置战斗信息\
		detail.setId(info.getId());
		detail.setType(info.getType());
		detail.setResult(info.getBattleResult());
		detail.setTime(info.getTime());
		detail.setAttackerType(info.getAttackerType());
		detail.setRecord(info.getRecord());

		if (info.getFiefScale() > 0) {
			BattleSkill defence = (BattleSkill) CacheMgr.battleSkillCache
					.get(info.getFiefScale());
			detail.setFiefDefence(defence);
			atkSide.setBattleSkill(defence);
			defSide.setBattleSkill(defence);
		}
		if (info.hasRis()) {
			for (int i = 0; i < info.getRisList().size(); i++) {
				BattleLogReturnInfoClient blric = new BattleLogReturnInfoClient();
				BattleLogReturnInfo blri = info.getRisList().get(i);
				ReturnInfoClient ric = ReturnInfoClient.convert2Client(
						blri.getInfo(), false);
				blric.setType(blri.getType());
				blric.setReturnInfoClient(ric);
				detail.addBattleReturnInfos(blric);
			}
		}

		if (info.hasRhis()) {
			for (int i = 0; i < info.getRhisCount(); i++) {
				if (info.getRhis(i).getHero() != 0) {
					ReturnHeroInfoClient rhic = ReturnHeroInfoClient
							.convert(info.getRhis(i));
					detail.addReturnHeroInfoClient(rhic);
				}
			}
		}

		if (info.hasVersion()) {
			detail.setVersion(info.getVersion());
		}
	}

	public void setBothSidesInfo(BattleLogInfo info) throws GameException {
		atkSide = new BattleSideInfo();
		defSide = new BattleSideInfo();

		// 设置主战
		if (info.hasAttacker())
			atkSide.setMainFighter(info.getAttacker());
		if (info.hasDefender())
			defSide.setMainFighter(info.getDefender());

		// 设置所属领地
		if (info.hasAttackFiefid())
			atkSide.setFiefid(info.getAttackFiefid());
		if (info.hasDefendFiefid())
			defSide.setFiefid(info.getDefendFiefid());

		List<Integer> userIds = new ArrayList<Integer>();

		// 设置双方的开始军队和结束军队
		if (info.hasAttackBeginTroopInfos()) {
			List<MoveTroopInfo> list = info.getAttackBeginTroopInfosList();
			setBeginTroop(info, atkSide, userIds, list);

		}

		if (info.hasAttackEndTroopInfos())
			atkSide.setEndTroop(info.getAttackEndTroopInfosList());

		if (info.hasDefendBeginTroopInfos()) {
			List<MoveTroopInfo> list = info.getDefendBeginTroopInfosList();
			setBeginTroop(info, defSide, userIds, list);

			// defSide.setInitTotalTroopAmount(defSide.getTotalTroopAmount());
		}

		if (info.hasDefendEndTroopInfos())
			defSide.setEndTroop(info.getDefendEndTroopInfosList());

		// 获取user信息
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(info.getAttacker());
		ids.add(info.getDefender());
		CacheMgr.userCache.get(ids);

		// 设置将领信息
		if (info.hasAttackHeroInfos()) {
			// 系统将领，从prop_campaign_hero.csv中读取，下同
			atkSide.setHeroInfo(BattleLogHeroInfoClient.convert2List(info
					.getAttackHeroInfosList()));

		}

		if (info.hasDefendHeroInfos()) {
			defSide.setHeroInfo(BattleLogHeroInfoClient.convert2List(info
					.getDefendHeroInfosList()));

		}

		if (info.hasUserTroopEffects()) {
			List<UserTroopEffectInfo> attackInfos = new ArrayList<UserTroopEffectInfo>();
			List<UserTroopEffectInfo> defendInfos = new ArrayList<UserTroopEffectInfo>();
			for (UserTroopEffectInfo effectInfo : info
					.getUserTroopEffectsList()) {
				if (atkSide.isPartner(effectInfo.getUserid()))
					attackInfos.add(effectInfo);
				else if (defSide.isPartner(effectInfo.getUserid()))
					defendInfos.add(effectInfo);
			}
			atkSide.setUserTroopEffectInfos(attackInfos);
			defSide.setUserTroopEffectInfos(defendInfos);
		}

		// 设置总伤/亡
		List<BattleLogReturnInfo> ls = info.getRisList();
		if (!ListUtil.isNull(ls)) {
			int atkTotalDeath = 0;
			int atkTotalWound = 0;

			int defTotalDeath = 0;
			int defTotalWound = 0;

			for (BattleLogReturnInfo br : ls) {
				ReturnInfo it = br.getInfo();
				int userId = it.getUserid();

				// if (it.getUserid().equals(info.getAttacker())
				// && !ListUtil.isNull(it.getRtisList())) {
				// 8.14与洲誉确认：显示总的损失
				if ((userId == info.getAttacker() || atkSide.isPartner(userId))
						&& !ListUtil.isNull(it.getRtisList())) {
					for (ReturnThingInfo rtiIt : it.getRtisList()) {
						if (ThingType.THING_TYPE_WOUNDED.getNumber() == rtiIt
								.getType()) {
							if (BriefUserInfoClient.isNPC(userId))
								atkTotalDeath += rtiIt.getCount();
							else
								atkTotalWound += rtiIt.getCount();
						}

						if (ThingType.THING_TYPE_DEAD.getNumber() == rtiIt
								.getType())
							atkTotalDeath += rtiIt.getCount();
					}
				}

				if ((userId == info.getDefender() || defSide.isPartner(userId))
						&& !ListUtil.isNull(it.getRtisList())) {
					for (ReturnThingInfo rtiIt : it.getRtisList()) {
						if (ThingType.THING_TYPE_WOUNDED.getNumber() == rtiIt
								.getType()) {
							if (BriefUserInfoClient.isNPC(userId))
								defTotalDeath += rtiIt.getCount();
							else
								defTotalWound += rtiIt.getCount();
						}

						if (ThingType.THING_TYPE_DEAD.getNumber() == rtiIt
								.getType())
							defTotalDeath += rtiIt.getCount();
					}
				}

			}

			atkSide.setTotalWound(atkTotalWound);
			atkSide.setTotalDeath(atkTotalDeath);
			atkSide.setDeathDetail(ls);
			defSide.setTotalWound(defTotalWound);
			defSide.setTotalDeath(defTotalDeath);
			defSide.setDeathDetail(ls);
		}

		// atkSide.setTroopInfo();
		// defSide.setTroopInfo();

		if (isMyBattle()) {
			if (Account.user.getId() == info.getAttacker()) {
				setBothSides(atkSide, defSide);
			} else {
				setBothSides(defSide, atkSide);
			}
		} else {
			setBothSides(atkSide, defSide);
		}
	}

	private void setBothSides(BattleSideInfo atkSide, BattleSideInfo defSide) {
		downSide = atkSide;
		upSide = defSide;
	}

	private void setBeginTroop(BattleLogInfo info, BattleSideInfo atkSide,
			List<Integer> userIds, List<MoveTroopInfo> list) {
		for (MoveTroopInfo it : list) {
			if (it.hasBi()) {
				BaseMoveTroopInfo bi = it.getBi();

				int userId = bi.getUserid();
				if (!userIds.contains(userId))
					userIds.add(userId);
				// 设置参战者
				atkSide.addPartner(userId);

				// 如果开战时间大于到达时间，过滤
				// if (bi.getTime() <= info.getTime())
				atkSide.addBeginTroop(it);
			}
		}

		// 获得
	}

	public boolean isMyBattle() {
		return atkSide.isMe() || defSide.isMe();
	}

	// 是否有一方没有部队，直接胜利
	public boolean isDirectWin() {
		return (atkSide.hasNoBeginTroop() || defSide.hasNoBeginTroop());
	}
	//下方赢
	public boolean isDownWin() {
		if (downSide.getMainFighter().getId()
				.equals(atkSide.getMainFighter().getId()))
			return detail.isAtkWin();
		else
			return detail.isDefWin();
	}
   //下方进攻   
	public boolean isDownAtk() {
		return downSide.getMainFighter().getId()
				.equals(atkSide.getMainFighter().getId());
	}

	public boolean isMeWin() {
		return ((atkSide.isMe() && detail.isAtkWin()) || (defSide.isMe() && detail
				.isDefWin()));
	}

	public boolean isMeLose() {
		return ((atkSide.isMe() && detail.isDefWin()) || (defSide.isMe() && detail
				.isAtkWin()));
	}

	public boolean isPvp() {
		return !BriefUserInfoClient.isNPC(atkSide.getMainFighterId())
				&& !BriefUserInfoClient.isNPC(defSide.getMainFighterId());
	}

	// 判断战斗结果：1.我参加的战斗，以我的结果为准，
	// 2.没我参加的战斗，如果双方都是玩家，以攻方结果为准，如果有一方是npc，以玩家结果为准
	public boolean isBattleWin() {
		if (isMyBattle())
			return isMeWin();
		else {
			boolean isAtkNPC = BriefUserInfoClient.isNPC(atkSide
					.getMainFighter().getId());
			boolean isDefNPC = BriefUserInfoClient.isNPC(defSide
					.getMainFighter().getId());

			if (!isAtkNPC && !isDefNPC)
				return detail.isAtkWin();
			else {
				if (isAtkNPC)
					return detail.isDefWin();
				else if (isDefNPC)
					return detail.isAtkWin();
				else
					return false;
			}
		}
	}

	public BriefUserInfoClient getResultUser() {
		if (isMyBattle())
			return Account.user.bref();
		else {
			boolean isAtkNPC = BriefUserInfoClient.isNPC(atkSide
					.getMainFighter().getId());
			boolean isDefNPC = BriefUserInfoClient.isNPC(defSide
					.getMainFighter().getId());

			if (!isAtkNPC && !isDefNPC)
				return atkSide.getMainFighter();
			else {
				if (isAtkNPC)
					return defSide.getMainFighter();
				else if (isDefNPC)
					return atkSide.getMainFighter();
				else
					return null;
			}
		}
	}

	public boolean isMeAttacker() {
		return atkSide.isMe();
	}

	public boolean isMeDefender() {
		return defSide.isMe();
	}

	public int getHeroLvl(long id) {
		int heroLvl = -1;

		if (null != downSide)
			heroLvl = downSide.getHeroLvl(id);

		if (-1 == heroLvl && null != upSide)
			heroLvl = upSide.getHeroLvl(id);

		return heroLvl;
	}

	public BattleSideInfo getAtkSide() {
		return atkSide;
	}

	public BattleSideInfo getDefSide() {
		return defSide;
	}

	public BattleSideInfo getDownSide() {
		return downSide;
	}

	public BattleDetailInfo getDetail() {
		return detail;
	}

	public BattleSideInfo getUpSide() {
		return upSide;
	}

	public List<BattleEventInfoClient> getEventInfoClientList() {
		return eventInfoClientList;
	}

	// private HashSet<String> aloneDownImg = new HashSet<String>();

	public HashSet<String> getSkillIcons() throws GameException {
		HashSet<String> icons = new HashSet<String>();
		// aloneDownImg.clear();
		// 战斗技能
		if (!ListUtil.isNull(eventInfoClientList)) {
			for (BattleEventInfoClient it : eventInfoClientList) {
				if (it.hasSkillIcon()) {
					boolean isDown= it.isDownAct(isMyBattle(), isMeAttacker());
					BattleSideInfo bsi = isDown ? downSide : upSide;
					int skillId = 0;
					if (it.hasActives()) {
						skillId = it.getActive().getValue().intValue();
					}
					if (skillId == 0) {
						continue;
					}

					if (it.getValue() == 0 && it.hasActives()
							&& it.getActive().getEx() == 1) {
						// buff
						String img = it.getBuffIconById();
						if (TextUtils.isEmpty(img) == false
								&& (img.endsWith(".png") || img
										.endsWith(".jpg"))) {
							// aloneDownImg.add(img);
							icons.add(img);
						}
						continue;
					}

					BattleSkill battleSkill = null;
					try {
						battleSkill = (BattleSkill) CacheMgr.battleSkillCache
								.get(skillId);
					} catch (GameException exception) {
						exception.printStackTrace();
					}
					if (battleSkill == null) {
						continue;
					}

					int skillAt = battleSkill.getSkillAt();
					if (skillAt == 1) {
						if (it.hasActives()) {
							// 技能
							String img = it.getSkillIconById();
							if (TextUtils.isEmpty(img) == false
									&& (img.endsWith(".png") || img
											.endsWith(".jpg"))) {
								icons.add(img);
								// aloneDownImg.add(img);
							}
						}
					} else if (skillAt == 3) {
						List<BattleLogHeroInfoClient> heroInfos = bsi
								.getHeroInfos();
						if (heroInfos == null || heroInfos.size() == 0) {
							continue;
						}
						HashSet<HeroProp> comboHeroInfos = getHeroHdIcons(
								heroInfos, skillId);
						HashSet<String> hdIcon = new HashSet<String>();
						if (comboHeroInfos != null && comboHeroInfos.size() > 0) {
							for (HeroProp heroProp : comboHeroInfos) {
								if (heroProp != null) {
									hdIcon.add(heroProp.getImg());
								}
							}
						}
						if (hdIcon != null && hdIcon.size() > 0) {
							// aloneDownImg.addAll(hdIcon);
							icons.addAll(hdIcon);

							for (String hdicon : hdIcon) {
								if (StringUtil.isNull(hdicon) == false) {
									battleEffectImgs.add(hdicon);
									battleEffectImgs.add(ImageUtil
											.imageMirrorName(hdicon));
								}
							}
						}
						// 下载英雄技能名称
						String img = it.getEffectsById();
						if (TextUtils.isEmpty(img) == false
								&& (img.endsWith(".png") || img
										.endsWith(".jpg"))) {
							// aloneDownImg.add(img);
							icons.add(img);
							battleEffectImgs.add(img);
						}
					} else {
						if (it.getValue() == 1) {
							List<BattleLogHeroInfoClient> heroInfos = bsi
									.getHeroInfos();
							if (heroInfos == null || heroInfos.size() == 0) {
								continue;
							}
							// HashSet<String> hdIcon =
							// getHeroHdIcons(heroInfos,skillId);
							// aloneDownImg.addAll(hdIcon);

							HashSet<HeroProp> comboHeroInfos = getHeroHdIcons(
									heroInfos, skillId);
							HashSet<String> hdIcon = new HashSet<String>();
							if (comboHeroInfos != null
									&& comboHeroInfos.size() > 0) {
								for (HeroProp heroProp : comboHeroInfos) {
									if (heroProp != null) {
										hdIcon.add(heroProp.getImg());
									}
								}
							}
							if (hdIcon != null && hdIcon.size() > 0) {
								icons.addAll(hdIcon);
							}
							// 下载英雄技能名称
							String img = it.getEffectsById();
							if (TextUtils.isEmpty(img) == false
									&& (img.endsWith(".png") || img
											.endsWith(".jpg"))) {
								// aloneDownImg.add(img);
								icons.add(img);
							}
						} else {
							if (it.hasActives()) {
								// 技能
								String img = it.getSkillIconById();
								if (TextUtils.isEmpty(img) == false
										&& (img.endsWith(".png") || img
												.endsWith(".jpg"))) {
									// aloneDownImg.add(img);
									icons.add(img);
								}
							}
						}
					}
				}
				if (it.hasDemage()) {
					// 下载技能特效的图片
					int skillId = it.getValue();
					if (skillId != 0) {
						int effectId = CacheMgr.battleSkillCache
								.getAnimType(skillId);
						// Config.getController().getBitmapCache()
						// .get("battle_scene_14_farm.jpg");
						if (effectId != 0) {
							BattleAnimEffects battleAnimEffects = null;
							boolean isDown = !it.isDownAct(isMyBattle(),
									isMeAttacker());
							if (isDown) {
								battleAnimEffects = CacheMgr.battleAnimEffect
										.getEffectAnim(effectId, 1);
							} else {
								battleAnimEffects = CacheMgr.battleAnimEffect
										.getEffectAnim(effectId, 2);
							}
							if (battleAnimEffects != null) {
								String effectImage = battleAnimEffects
										.getIcon();
								String seqFrame = battleAnimEffects
										.getSeqFrame();
								if (!StringUtil.isNull(effectImage)
										&& !StringUtil.isNull(seqFrame)) {
									int frameNum = battleAnimEffects
											.getFrameNum();
									for (int i = 1; i <= frameNum; i++) {
										String img = effectImage + "_" + i
												+ ".png";
										if (!StringUtil.isNull(img)) {
											battleEffectImgs.add(img);

											String mirrorImg = "";
											if (battleAnimEffects
													.getHoriMirror() != 0
													|| battleAnimEffects
															.getVerticalMirror() != 0
													|| battleAnimEffects
															.getRotateDegress() != 0) {
												mirrorImg = ImageUtil
														.imageMirrorName(img)
														+ "_"
														+ battleAnimEffects
																.getRotateDegress();
											} else {
												mirrorImg = ImageUtil
														.imageScaleName(
																img,
																(int) battleAnimEffects
																		.getXScale(),
																(int) battleAnimEffects
																		.getYScale());
											}
											battleEffectImgs.add(mirrorImg);

											icons.add(img);
											// aloneDownImg.add(img);
										}
									}
								}
							}
						}
					}
				}
			}
			// 加上默认的特效对应的image
			addDefaultEffect();
			// battleEffectImgs.addAll(icons);
		}
		return icons;
	}

	private void addDefaultEffect() {
		BattleAnimEffects battleAnimEffects = null;
		for (int i = 1; i <= 3; i++) {
			for (int beatSide = 1; beatSide <= 2; beatSide++) {
				battleAnimEffects = CacheMgr.battleAnimEffect.getEffectAnim(i,
						beatSide);
				if (battleAnimEffects != null) {
					String effectImage = battleAnimEffects.getIcon();
					String seqFrame = battleAnimEffects.getSeqFrame();
					if (!StringUtil.isNull(effectImage)
							&& !StringUtil.isNull(seqFrame)) {
						int frameNum = battleAnimEffects.getFrameNum();
						for (int j = 1; j <= frameNum; j++) {
							String img = effectImage + "_" + j + ".png";
							if (!StringUtil.isNull(img)) {
								battleEffectImgs.add(img);

								String mirrorImg = "";
								if (battleAnimEffects.getHoriMirror() != 0
										|| battleAnimEffects
												.getVerticalMirror() != 0
										|| battleAnimEffects.getRotateDegress() != 0) {
									mirrorImg = ImageUtil.imageMirrorName(img)
											+ "_"
											+ battleAnimEffects
													.getRotateDegress();
								} else {
									mirrorImg = ImageUtil
											.imageScaleName(img,
													(int) battleAnimEffects
															.getXScale(),
													(int) battleAnimEffects
															.getYScale());
								}
								battleEffectImgs.add(mirrorImg);

							}
						}
					}
				}
			}
		}
	}

	// 得到组合英雄信息
	public HashSet<HeroProp> getHeroHdIcons(
			List<BattleLogHeroInfoClient> heroLogInfos, int skillId) {
		HashSet<HeroProp> heroInfos = new HashSet<HeroProp>();
		BattleSkill battleSkill = null;
		try {
			battleSkill = (BattleSkill) CacheMgr.battleSkillCache.get(skillId);
		} catch (GameException exception) {
			exception.printStackTrace();
		}
		if (battleSkill == null) {
			return heroInfos;
		}

		if (battleSkill.getMainType() == 7) {
			Vector<Integer> battleHeroList = new Vector<Integer>();

			List<SkillCombo> skillsCombo = null;
			//
			skillsCombo = CacheMgr.battleSkillCombo.getComboHeros(skillId);

			for (int i = 0; i < heroLogInfos.size(); i++) {
				BattleLogHeroInfoClient heroInfo = heroLogInfos.get(i);
				if (heroInfo.getHeroId() != 0) {
					battleHeroList.add(heroInfo.getHeroId());
				}
			}

			skillsCombo = CacheMgr.battleSkillCombo.getComboHeros(skillId);
			// 对进行一次遍历 取到相同的
			if (skillsCombo == null || skillsCombo.size() == 0) {
				return heroInfos;
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
							|| battleHeroList.get(0).intValue() == comboHeroId
									.get(1).intValue() || battleHeroList.get(0)
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
						for (int i = 0; i < heroLogInfos.size(); i++) {
							BattleLogHeroInfoClient heroInfo = heroLogInfos
									.get(i);
							if ((heroInfo.getHeroId() == hero1Id && hero1Id != 0)
									|| (heroInfo.getHeroId() == hero2Id && hero2Id != 0)
									|| (heroInfo.getHeroId() == hero3Id && hero3Id != 0)) {
								HeroProp hp = heroInfo.getHeroProp();
								if (hp != null) {
									heroInfos.add(hp);
								}
							}
						}
						return heroInfos;
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
							for (int i = 0; i < heroLogInfos.size(); i++) {
								BattleLogHeroInfoClient heroInfo = heroLogInfos
										.get(i);
								if ((heroInfo.getHeroId() == comboHeroId.get(0)
										.intValue() && comboHeroId.get(0)
										.intValue() != 0)
										|| (heroInfo.getHeroId() == comboHeroId
												.get(1).intValue() && comboHeroId
												.get(1).intValue() != 0)) {
									HeroProp hp = heroInfo.getHeroProp();
									if (hp != null) {
										heroInfos.add(hp);
									}
								}
							}
							return heroInfos;
						}
					}
				}
			}
		} else {
			for (BattleLogHeroInfoClient heroInfo : heroLogInfos) {
				HeroProp hp = heroInfo.getHeroProp();
				if (heroInfo.getRole() == 1 || (heroInfo.getRole() == 3)) // 主将技能图片下载
				{
					if (hp != null) {
						heroInfos.add(hp);
					}
				}
			}
		}
		return heroInfos;

	}

	private ArrayList<String> logs = new ArrayList<String>();

	public ArrayList<String> getLogList() {
		logs.clear();
		createLog();
		return logs;
	}

	public void addLogInlist(String log) {
		if (null != log) {
			logs.add(log);
		}
	}

	public List<BattleLogHeroInfoClient> getHeroInfos(int userId) {

		if (atkSide.getMainFighter().getId() == userId)
			return atkSide.getHeroInfos();
		else if (defSide.getMainFighter().getId() == userId)
			return defSide.getHeroInfos();
		return null;
	}

	// 单将领改多将领,需要修改为用getHeroInfos方法
	public BattleLogHeroInfoClient getHeroInfo(int userId, long heroId) {
		List<BattleLogHeroInfoClient> list = getHeroInfos(userId);
		if (null != list && !list.isEmpty())
			for (BattleLogHeroInfoClient blhic : list) {
				if (blhic.getId() == heroId)
					return blhic;
			}
		return null;
	}

	public boolean isMePartner() {
		return atkSide.isMe() || atkSide.isMePartner() || defSide.isMe()
				|| defSide.isMePartner();
	}

	// 获取日志中需要下载的图片
	public HashSet<String> getDownloadIcons() throws GameException {
		HashSet<String> icons = new HashSet<String>();

		HashSet<String> downIcons = getDownSide().getDownloadIcons();
		if (downIcons != null && downIcons.size() > 0)
			icons.addAll(downIcons);

		HashSet<String> upIcons = getUpSide().getDownloadIcons();
		if (upIcons != null && upIcons.size() > 0) {
			icons.addAll(upIcons);
		}

		HashSet<String> skillIcons = getSkillIcons();
		if (skillIcons != null && skillIcons.size() > 0)
			icons.addAll(skillIcons);

		if (!StringUtil.isNull(bgName) && (bgName.equals(default_bg) == false)
				&& (bgName.endsWith(".png") || bgName.endsWith(".jpg"))) {
			icons.add(bgName);
		}
		//
		if (!StringUtil.isNull(wallName)
				&& (wallName.endsWith(".png") || wallName.endsWith(".jpg"))) {
			icons.add(wallName);
		}

		if (StringUtil.isNull(bgName) == false)
			battleEffectImgs.add(bgName);
		// 默认的背景
		battleEffectImgs.add("battle_map.jpg");
		battleEffectImgs.add("flyings");
		String fly_m = ImageUtil.imageMirrorName("flyings"); // 将领的花
		battleEffectImgs.add(fly_m);

		battleEffectImgs.add("skill_bg");
		battleEffectImgs.add("skill_bg1");

		battleEffectImgs.add("arm_skill_bg");
		battleEffectImgs.add("arm_skill");
		battleEffectImgs.add("arm_skill_bottom");

		battleEffectImgs.add("officer_bg");
		battleEffectImgs.add("troop_info_bg");
		String officer_bg_m = ImageUtil.imageRotateName("officers_bg.png", 180);
		String troop_info_bg_m = ImageUtil.imageRotateName("troop_info_bg.png",
				180);
		battleEffectImgs.add(officer_bg_m);
		battleEffectImgs.add(troop_info_bg_m);
		if (StringUtil.isNull(wallName) == false)
			battleEffectImgs.add(wallName);

		return icons;
	}

	public int getTotalTroopAmount(boolean isDown) {
		return isDown ? downSide.getTotalTroopAmount() : upSide
				.getTotalTroopAmount();
	}

	public String getNickName(boolean isDown) {
		return isDown ? downSide.getMainFighter().getNickName() : upSide
				.getMainFighter().getNickName();
	}

	public int getCurrTroopAmount(boolean isDown) {
		return isDown ? downSide.getCurrTroopAmount() : upSide
				.getCurrTroopAmount();
	}

	public boolean isAtkSide(int userId) {
		return userId == atkSide.getMainFighterId()
				|| atkSide.isPartner(userId);
	}

	public boolean isAlly(int userId) {
		if (atkSide.isMe()) {
			List<Integer> partners = atkSide.getPartner();
			if (partners.contains(userId))
				return true;
		} else if (defSide.isMe()) {
			List<Integer> partners = defSide.getPartner();
			if (partners.contains(userId))
				return true;
		} else {
			if (atkSide.isMePartner())
				return userId == atkSide.getMainFighterId()
						|| atkSide.isPartner(userId);
			else if (defSide.isMePartner())
				return userId == defSide.getMainFighterId()
						|| defSide.isPartner(userId);
		}

		return false;
	}

	public String getBgName() {
		return bgName;
	}

	public String getWall() {
		return wallName;
	}

	// 是否是副本
	public boolean isActType() {
		return 3 == detail.getType();
	}

	public boolean needShowHeroLevelHint() {
		// return (3 == detail.getType() || BriefUserInfoClient.isNPC(atkSide
		// .getMainFighterId())) && isMyBattle();
		return isMyBattle();
	}

	// 被掠夺
	public boolean isPlundered() {
		if (2 == detail.getType()
				&& Account.user.getId() == defSide.getMainFighterId()
				&& detail.isAtkWin())
			return true;
		return false;
	}

	public List<BattleLogReturnInfoClient> getPlunderedThings() {
		List<BattleLogReturnInfoClient> battleReturnInfos = getDetail()
				.getBattleReturnInfos();

		List<BattleLogReturnInfoClient> plunderedThing = new ArrayList<BattleLogReturnInfoClient>();

		if (!ListUtil.isNull(battleReturnInfos)) {
			for (BattleLogReturnInfoClient it : battleReturnInfos) {
				// 获取攻方掠夺物资
				if (isPlundered() && isAtkSide(it.getUserId())
						&& it.isPlunder())
					plunderedThing.add(it);
			}
		}

		return plunderedThing;
	}

	public List<BattleLogReturnInfoClient> getOwnReward() {
		List<BattleLogReturnInfoClient> battleReturnInfos = getDetail()
				.getBattleReturnInfos();

		List<BattleLogReturnInfoClient> ownReward = new ArrayList<BattleLogReturnInfoClient>();

		if (!ListUtil.isNull(battleReturnInfos)) {
			for (BattleLogReturnInfoClient it : battleReturnInfos) {
				if (it.getUserId() == Account.user.getId()) {
					if (it.isDropNothing())
						continue;

					ownReward.add(it);
				}
			}
		}
		return ownReward;
	}

	public List<BattleLogReturnInfoClient> getAllyReward() {
		List<BattleLogReturnInfoClient> battleReturnInfos = getDetail()
				.getBattleReturnInfos();

		List<BattleLogReturnInfoClient> allyReward = new ArrayList<BattleLogReturnInfoClient>();

		if (!ListUtil.isNull(battleReturnInfos)) {
			for (BattleLogReturnInfoClient it : battleReturnInfos) {
				if (isAlly(it.getUserId())
						&& it.getUserId() != Account.user.getId()) {
					if (it.isDropNothing())
						continue;
					allyReward.add(it);
				}
			}
		}

		return allyReward;
	}

	public int getOwnHeroUpdateExp() {
		int exp = 0;

		if (!ListUtil.isNull(getDetail().getRhics())) {
			for (ReturnHeroInfoClient rhic : getDetail().getRhics()) {
				if (rhic.getUserid() == Account.user.getId()) {
					exp = rhic.getUpdateExp(getHeroLvl(rhic.getId()));
					if (exp != 0) {
						break;
					}
				}
			}
		}

		return exp;
	}

	public BattleLossDetail getMyTotalLoss() {
		if (!isMyBattle())
			return null;

		BattleSideInfo bsi = downSide.isMe() ? downSide : upSide;
		if (null == bsi.getDeathDetail()
				|| ListUtil.isNull(bsi.getDeathDetail().getLossDetailLs()))
			return null;

		return bsi.getDeathDetail().getLossDetailLs().get(0);
	}

	public HashSet<String> getBattleImgs() {
		return battleEffectImgs;
	}

	private boolean isNewLog;
	private int curEventIdx = 0;
	private int round = 1; // 回合数

	// 把 伤害、数量事件一起解析 当收到数量的时候再处理 不处理伤害事件

	public void createLog() { // BaseAnim
		atkSide.resetBattleArrayInfo();
		defSide.resetBattleArrayInfo();
		setTroopInfo();
		// atkSide.setTroopInfo();
		// defSide.setTroopInfo();

		if (ListUtil.isNull(getEventInfoClientList())) {
			return;
		}
		int count = getEventInfoClientList().size();
		for (int index = 0; index < count; index++) {
			BattleEventInfoClient info = getEventInfoClientList().get(
					curEventIdx++);

			boolean isMyBattle = isMyBattle();
			boolean isMeAttacker = isMeAttacker();
			if (BattleEvent.valueOf(info.getEvent()) != BattleEvent.BATTLE_EVENT_NUM)
			{
				if (BattleEvent.valueOf(info.getEvent()) == BattleEvent.BATTLE_EVENT_SKILL) 
				{
					if (info.getValue() == 1) 
					{
						String heroName = getAttackHeroName(info);
						info.setHeroName(heroName);
					}
				}
				addLogInlist(info.createLogEx(isMyBattle, isMeAttacker,isNewLog, round, null, null));
			}
			switch (BattleEvent.valueOf(info.getEvent())) {
			case BATTLE_EVENT_ACT: // 行动者
				round++;
				break;
			case BATTLE_EVENT_TARGET: // 选择目标
				break;
			case BATTLE_EVENT_FIGHT_BACK: // 还击
			case BATTLE_EVENT_BYPASS: // 绕过
				break;
			// 和数量一起处理
			case BATTLE_EVENT_DEMAGE: // 伤害
				break;
			case BATTLE_EVENT_SKILL: // 触发技能 士兵技能、buff是一个事件
				break;
			case BATTLE_EVENT_DEATH: // 死亡
				break;
			case BATTLE_EVENT_RUN_AWAY: // 逃跑
				break;
			case BATTLE_EVENT_ROUND: // 轮次
				break;
			// 处理整个伤害和 飘数字 士兵数量变化
			case BATTLE_EVENT_NUM: // 数量
				if (curEventIdx < 2) {
					break;
				}
				BattleEventInfoClient demageInfo = getEventInfoClientList()
						.get(curEventIdx - 2);

				List<Integer> forceInfo = new ArrayList<Integer>();
				if (demageInfo.hasPassive()) {
					List<BattleEventArmInfo> ls = demageInfo.getPassives();
					for (int i = 0; i < ls.size(); i++) {
						BattleEventArmInfo it = ls.get(i);
						if (it.getEx() != null) {
							forceInfo.add(it.getEx());
						}
					}
				}
				modifyNum(info, demageInfo);

				List<BattleEventArmInfo> deadInfo = null;
				if (demageInfo.hasPassive()) {
					deadInfo = demageInfo.getPassives();
					addLogInlist(demageInfo.createLogEx(isMyBattle,
							isMeAttacker, isNewLog, round, deadInfo, forceInfo));
				}
				break;
			case BATTLE_EVENT_FALL_HP: // HP

				break;
			case BATTLE_EVENT_BOUT: // 回合结束
				break;
			case BATTLE_EVENT_BUFF_SET: // 设置buff
				break;
			case BATTLE_EVENT_BUFF_CLEAR: // 清除buff
				break;
			case BATTLE_EVENT_MODIFY_HP:
				handleEventModifyHP(info);
				break;
			
//			case BATTLE_EVENT_INIT:
//				handleInitData(info);
//				break;
			
			default:
				break;
			}
		}
	}

	private String getAttackHeroName(BattleEventInfoClient info) {
		StringBuilder attackName = new StringBuilder();
		// 得到攻击方的名字
		if (info != null && info.getValue() == 1) {
			// 判断是不是组合技能
			int skillId = 0;
			if (info.hasActives()) {
				skillId = info.getActive().getValue().intValue();
				if (skillId == 0) {
					return "";
				}

				BattleSkill battleSkill = null;
				try {
					battleSkill = (BattleSkill) CacheMgr.battleSkillCache
							.get(skillId);
				} catch (GameException exception) {
					exception.printStackTrace();
				}

				if (battleSkill == null) {
					return "";
				}
				boolean isDown = info.isDownAct(isMyBattle(), isMeAttacker());
				BattleSideInfo bsi = isDown ? downSide : upSide;
				List<BattleLogHeroInfoClient> heroInfos = bsi.getHeroInfos();
				// 组合技能 判断组合英雄是哪些英雄
				if (battleSkill.getMainType() == 7) {
					HashSet<HeroProp> comboHeros = getHeroHdIcons(heroInfos,
							skillId);
					if (comboHeros != null && comboHeros.size() > 0) {
						for (HeroProp heroProp : comboHeros) {
							if (heroProp != null) {
								attackName.append("[");
								attackName.append(heroProp.getName());
								attackName.append("]");
							}
						}
						return attackName.toString();
					}
					return "";
				} else {
					int armId = info.getActiveId();
					HeroProp hero = null;
					try {
						if (0 != armId) {
							hero = (HeroProp) CacheMgr.heroPropCache.get(armId);
						}
					} catch (GameException e) {
						e.printStackTrace();
					}

					if (null != hero) {
						attackName.append("[");
						attackName.append(hero.getName());
						attackName.append("]");
						return attackName.toString();
					} else {
						return "";
					}
				}
			}

		}
		return "";
	}

	public BattleArrayInfoClient getBattleArrayInfoClient(boolean isDown,
			int troopId) {
		BattleSideInfo bsi = isDown ? getDownSide() : getUpSide();
		return bsi.getCurTroop(troopId);
	}

	public void modifyNum(BattleEventInfoClient info,
			BattleEventInfoClient demageInfo) {
		if (BattleEvent.BATTLE_EVENT_NUM != BattleEvent
				.valueOf(info.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle(), isMeAttacker());

		List<BattleEventArmInfo> ls = info.getActives();
		List<BattleEventArmInfo> demageList = demageInfo.getPassives();

		for (int i = 0; i < ls.size(); i++) {
			BattleEventArmInfo beai = ls.get(i);
			BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
					beai.getArmid());
			int numLoss = 0;
			int fallHp = 0;
			if (baic != null) {
				numLoss = (int) (baic.getNum() - beai.getEx());
				fallHp = (int) (baic.getCurrentHp() - beai.getValue());
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
						armInfo.setEx(fallHp);
					} else {
						armInfo.setEx(numLoss);
					}
				}
			}
		}
	}

	public void handleEventModifyHP(BattleEventInfoClient info) {
		if (BattleEvent.BATTLE_EVENT_MODIFY_HP != BattleEvent.valueOf(info
				.getEvent()))
			return;

		boolean isDown = info.isDownAct(isMyBattle(), isMeAttacker());
		BattleArrayInfoClient baic = getBattleArrayInfoClient(isDown,
				info.getActiveId());
		{
			List<BattleEventArmInfo> ls = info.getActives();
			BattleEventArmInfo beai = ls.get(0);

			long curHP = baic.getCurrentHp() + beai.getValue();// baic.getHp() +
																// beai.getValue();
			baic.setCurrentHp(curHP);
			if (curHP > baic.getHp()) {
				baic.setHp(curHP);
			}
			long num = beai.getEx() + baic.getNum();
			baic.setNum(num);
		}

	}

	public void releseBitmap() {
		HashSet<String> imgs = getBattleImgs();
		if (imgs != null && imgs.size() > 0) {
			BitmapCache bitmapCache = Config.getController().getBitmapCache();
			for (String img : imgs) {
				if (StringUtil.isNull(img) == false) {
					Bitmap bmp = bitmapCache.get(img);
					if (bmp != null && bmp.isRecycled() == false) {
						bitmapCache.remove(img);
					}
				}
			}
		}
	}
	
	public void handleInitData(BattleEventInfoClient info)
	{
		boolean isDown = info.isDownAct(isMyBattle(), isMeAttacker());
		BattleSideInfo leftSide = getDownSide();
		BattleSideInfo rightSide = getUpSide();
	
		if(info.hasActives())
		{
			List<BattleEventArmInfo> ls = info.getActives();
			if(ListUtil.isNull(ls) == false)
			{
					List<BattleArrayInfoClient> amyArray = new ArrayList<BattleArrayInfoClient>();
					for(BattleEventArmInfo armInfo : ls)
					{
						long num = armInfo.getEx().intValue();
						long hp = armInfo.getValue().longValue();
						int id = armInfo.getArmid().intValue();
						BattleArrayInfoClient arrayInfoClient = new BattleArrayInfoClient(id, num, hp);
						amyArray.add(arrayInfoClient);
					}
					if(isDown)
					{
						leftSide.setBattleArrayInfo(amyArray);
					}
					else
					{
						rightSide.setBattleArrayInfo(amyArray);
					}
				}
			}
		
			if(info.hasPassive())
			{
				List<BattleEventArmInfo> ls = info.getPassives();
				if(ListUtil.isNull(ls) == false)
				{
					List<BattleArrayInfoClient> amyArray = new ArrayList<BattleArrayInfoClient>();
					for(BattleEventArmInfo armInfo : ls)
					{
						long num = armInfo.getEx().intValue();
						long hp = armInfo.getValue().longValue();
						int id = armInfo.getArmid().intValue();
						BattleArrayInfoClient arrayInfoClient = new BattleArrayInfoClient(id, num, hp);
						amyArray.add(arrayInfoClient);
					}
					if(isDown)
					{
						rightSide.setBattleArrayInfo(amyArray);
					}
					else
					{
						leftSide.setBattleArrayInfo(amyArray);
					}
				}
			}
		}	
	
	private void setTroopInfo()
	{
		if(ListUtil.isNull(eventInfoClientList) == false)
		{
			for(BattleEventInfoClient infoClient : eventInfoClientList)
			{
				if(BattleEvent.valueOf(infoClient.getEvent()) == BattleEvent.BATTLE_EVENT_INIT)
				{
					handleInitData(infoClient);
					break;
				}
			}
		}
	}
}
