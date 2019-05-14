package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.protos.BriefBattleLogInfo;
import com.vikings.sanguo.protos.TROOP_ROLE;
import com.vikings.sanguo.protos.TroopLogEvent;
import com.vikings.sanguo.protos.TroopLogInfo;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;

public class BattleLogClient {

	public static final int LOG_TYPE_BATTLE = 1;
	public static final int LOG_TYPE_TROOP = 2;

	private long id;
	private int time;
	private int attackUserId;
	private int defendUserId;
	private int type;// 1:为战争日志，2为部队日志
	private String title = "";
	private String text = "";
	private BriefUserInfoClient attackUser;
	private BriefUserInfoClient defendUser;
	private List<Long> tilesList = new ArrayList<Long>();
	private long[] tiles;
	private long[] tilesTitle;
	private BriefBattleLogInfo blogInfo;
	private TroopLogInfo logInfo;

	public int getAttackUserId() {
		return attackUserId;
	}

	public void setAttackUserId(int attackUserId) {
		this.attackUserId = attackUserId;
	}

	public int getDefendUserId() {
		return defendUserId;
	}

	public void setDefendUserId(int defendUserId) {
		this.defendUserId = defendUserId;
	}

	public long[] getTilesTitle() {
		return tilesTitle;
	}

	public void setTilesTitle(long[] tilesTitle) {
		this.tilesTitle = tilesTitle;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BriefUserInfoClient getAttackUser() {
		return attackUser;
	}

	public void setAttackUser(BriefUserInfoClient attackUser) {
		this.attackUser = attackUser;
	}

	public BriefUserInfoClient getDefendUser() {
		return defendUser;
	}

	public void setDefendUser(BriefUserInfoClient defendUser) {
		this.defendUser = defendUser;
	}

	public TroopLogInfo getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(TroopLogInfo logInfo) {
		this.logInfo = logInfo;
	}

	public BriefBattleLogInfo getBlogInfo() {
		return blogInfo;
	}

	public void setBlogInfo(BriefBattleLogInfo blogInfo) {
		this.blogInfo = blogInfo;
	}

	public String getBattleTypeName() {
		String typeName = "战争";
		if (null != blogInfo) {
			if (blogInfo.getType().intValue() == BattleAttackType.E_BATTLE_COMMON_ATTACK
					.getNumber()) {
				String string = "占领战争";
				if (CacheMgr.holyPropCache.isHoly(blogInfo.getDefendFiefid())) {
					string = "征讨战争";
				}
				typeName = StringUtil.color(string, R.color.k7_color4);
			} else if (blogInfo.getType().intValue() == BattleAttackType.E_BATTLE_PLUNDER_ATTACK
					.getNumber()) {
				typeName = StringUtil.color("掠夺战争", R.color.k7_color4);
			} else if (blogInfo.getType().intValue() == BattleAttackType.E_BATTLE_DUEL_ATTACK
					.getNumber()) {
				typeName = StringUtil.color("单挑战争", R.color.k7_color4);
			} else if (blogInfo.getType().intValue() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
					.getNumber()) {
				typeName = StringUtil.color("屠城战争", R.color.k7_color4);
			}
		}
		return typeName;
	}

	private static BattleLogClient convert(BriefBattleLogInfo bbli) {
		BattleLogClient blc = new BattleLogClient();
		blc.setBlogInfo(bbli);
		blc.setId(bbli.getId());
		blc.setType(1);
		blc.setTime(bbli.getTime());
		blc.setAttackUserId(bbli.getAttacker());
		blc.setDefendUserId(bbli.getDefender());
		return blc;
	}

	public static List<BattleLogClient> convertList(
			List<BriefBattleLogInfo> ls, boolean self) throws GameException {
		List<BattleLogClient> blcLs = new ArrayList<BattleLogClient>();
		if (ListUtil.isNull(ls))
			return blcLs;
		for (BriefBattleLogInfo it : ls)
			blcLs.add(convert(it));
		CacheMgr.fillBattleLogClient(blcLs);
		for (BattleLogClient it : blcLs)
			it.analysisBattleDetail(); // self
		return blcLs;
	}

	public static List<BattleLogClient> convertroopLog(List<TroopLogInfo> lics) {
		List<BattleLogClient> logList = new ArrayList<BattleLogClient>();
		for (TroopLogInfo tl : lics) {
			BattleLogClient blc = new BattleLogClient();
			blc.setId(tl.getId());
			blc.setType(2);
			blc.setTime(tl.getTime());
			blc.setLogInfo(tl);
			blc.toTroopDetail();
			logList.add(blc);
		}
		return logList;
	}

	private String getCountryName(BriefUserInfoClient briefUser) {
		if (null == briefUser)
			return "";
		if (BriefUserInfoClient.isNPC(briefUser.getId().intValue()))
			return "";
		String countryName = briefUser.getCountryName();
		if (!StringUtil.isNull(countryName)) {
			countryName = "(" + countryName + ")";
		}
		return countryName;
	}

	// self:是否与我相关，攻守方都不是我的日志，也可能与我相关
	public void analysisBattleDetail() { // boolean self
		boolean self = (Account.user.getId() == attackUserId || Account.user
				.getId() == defendUserId);

		if (blogInfo == null)
			return;
		StringBuilder bufContent = new StringBuilder();
		String bufTitle = "";
		long defFiefId = blogInfo.getDefendFiefid();

		if (self) {
			// 攻方是我
			if (blogInfo.getAttacker() == Account.user.getId()) {
				bufTitle = (blogInfo.getBattleResult() == 1) ? "胜利" : "失败";

				bufContent
						.append("你向")
						.append(StringUtil.nickNameColor(defendUser))
						.append(StringUtil.color(getCountryName(defendUser),
								StringUtil.getNickNameColor(defendUser)))
						.append("的")
						.append(getFiefName(blogInfo.getDefendFiefid()))
						.append("发起了").append(getBattleTypeName());

				if (blogInfo.getBattleResult() == 1) {
					if (blogInfo.getType() == BattleAttackType.E_BATTLE_COMMON_ATTACK
							.getNumber()) {
						bufContent.append("，获得胜利并占领了该领地！");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_PLUNDER_ATTACK
							.getNumber()) {
						bufContent.append("，获得胜利并掠夺他的资源！");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber()) {
						bufContent.append("，获得胜利!屠戮了他的士兵和平民！");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
							.getNumber()) {
						bufContent.append("，你力克对方，凯旋而归！");
					} else {
						bufContent.append("，获得胜利!");
					}
				} else if (blogInfo.getBattleResult() == 2) {
					if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber()) {
						bufContent.append("，遭到了猛烈的抵抗，惨败而归。");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
							.getNumber()) {
						bufContent.append("，你力有不逮，惨遭败北。");
					} else {
						bufContent.append("，战败而归。");
					}
				} else {
					bufContent.append("，围城后没有进攻，士兵撤退了");
				}
			}

			// 守方是我
			else if (blogInfo.getDefender() == Account.user.getId()) {
				bufTitle = (blogInfo.getBattleResult() == 1) ? "失败" : "胜利";
				// long defFiefId = blogInfo.getDefendFiefid();

				// 攻方胜
				if (blogInfo.getBattleResult() == 1) {
					if (!BriefUserInfoClient.isNPC(blogInfo.getAttacker())) {
						bufContent
								.append(StringUtil.nickNameColor(attackUser))
								.append(StringUtil.color(
										getCountryName(attackUser),
										StringUtil.getNickNameColor(attackUser)))
								.append("向你的").append(getFiefName(defFiefId))
								.append("发起了").append(getBattleTypeName());

						if (blogInfo.getType() == BattleAttackType.E_BATTLE_COMMON_ATTACK
								.getNumber()) {
							bufContent.append("，你战败失去了该领地。");
						} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_PLUNDER_ATTACK
								.getNumber()) {
							bufContent.append("，掠夺了大批资源后离开了。");
						} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
								.getNumber()) {
							bufContent.append("，你遭受惨败，主城进入了虚弱状态。");
						} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
								.getNumber()) {
							bufContent.append("，你力有不逮，惨遭败北！");
						} else {
							bufContent.append("你战败了");
						}

					} else {
						if (CacheMgr.holyPropCache.isHoly(defFiefId)) {
							bufContent.append("你开启了")
									.append(getFiefName(defFiefId))
									.append("的恶魔之门，但被来势汹汹的恶魔击败了。");
						} else {
							bufContent
									.append(StringUtil
											.nickNameColor(attackUser))
									.append(StringUtil
											.color(getCountryName(attackUser),
													StringUtil
															.getNickNameColor(attackUser)))
									.append("进攻你的")
									.append(getFiefName(defFiefId));
							bufContent.append("你经历了惨痛的失败");
						}

					}
				}
				// 守方胜
				else if (blogInfo.getBattleResult() == 2) {
					if (!BriefUserInfoClient.isNPC(blogInfo.getAttacker())) {

						bufContent
								.append(StringUtil.nickNameColor(attackUser))
								.append(StringUtil.color(
										getCountryName(attackUser),
										StringUtil.getNickNameColor(attackUser)))
								.append("向你的").append(getFiefName(defFiefId))
								.append("发起了").append(getBattleTypeName());

						if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
								.getNumber()) {
							bufContent.append("，你奋起反抗，成功抵御了他的进攻！");
						} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
								.getNumber()) {
							bufContent.append("，你从容应战，凯旋而归！");
						} else {
							bufContent.append("，你成功的抵御了TA的进攻.");
						}

					} else {
						if (CacheMgr.holyPropCache.isHoly(blogInfo
								.getDefendFiefid())) {
							bufContent.append("你开启了")
									.append(getFiefName(defFiefId))
									.append("的恶魔之门，并取得了胜利！");
						} else {
							bufContent
									.append(StringUtil
											.nickNameColor(attackUser))
									.append(StringUtil
											.color(getCountryName(attackUser),
													StringUtil
															.getNickNameColor(attackUser)))
									.append("进攻你的")
									.append(getFiefName(defFiefId))
									.append("你成功的抵御了TA的进攻！");
						}
					}
				} else {// 其他
					bufContent
							.append(StringUtil.nickNameColor(attackUser))
							.append(StringUtil.color(
									getCountryName(attackUser),
									StringUtil.getNickNameColor(attackUser)))
							.append("进攻你的").append(getFiefName(defFiefId))
							.append("围城后撤退了，你成功抵御了TA的进攻！");
				}
			}
		} else {// 第三视角
			if (blogInfo.getBattleResult() == 1) {
				if (BriefUserInfoClient.isNPC(blogInfo.getAttacker())) {
					if (CacheMgr.holyPropCache.isHoly(blogInfo
							.getDefendFiefid())) {
						bufTitle = StringUtil.nickNameColor(attackUser) + "胜利";
						bufContent
								.append(StringUtil.nickNameColor(defendUser))
								.append(StringUtil.color(
										getCountryName(defendUser),
										StringUtil.getNickNameColor(defendUser)))
								.append("开启了").append(getFiefName(defFiefId))
								.append("的恶魔之门，但被来势汹汹的敌人打得落荒而逃。");
					}
				} else {
					bufTitle = StringUtil.nickNameColor(attackUser) + "胜利";
					bufContent
							.append(StringUtil.nickNameColor(attackUser))
							.append(StringUtil.color(
									getCountryName(attackUser),
									StringUtil.getNickNameColor(attackUser)))
							.append("向")
							.append(StringUtil.nickNameColor(defendUser))
							.append(StringUtil.color(
									getCountryName(defendUser),
									StringUtil.getNickNameColor(defendUser)))
							.append("的领地发起了")
							.append(getBattleTypeName())
							.append("，")
							.append(StringUtil.nickNameColor(attackUser))
							.append(StringUtil.color(
									getCountryName(attackUser),
									StringUtil.getNickNameColor(attackUser)));

					if (blogInfo.getType() == BattleAttackType.E_BATTLE_COMMON_ATTACK
							.getNumber()) {
						bufContent.append("成为该领地的新领主！");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_PLUNDER_ATTACK
							.getNumber()) {
						bufContent.append("获得胜利并掠夺他的资源！");
					} else if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber()) {
						bufContent.append("获得胜利,屠戮了他的士兵和平民！");
					} else {
						bufContent.append("获得胜利");
					}
				}

			} else {
				if (BriefUserInfoClient.isNPC(blogInfo.getAttacker())) {
					if (CacheMgr.holyPropCache.isHoly(blogInfo
							.getDefendFiefid())) {
						bufTitle = StringUtil.nickNameColor(defendUser) + "胜利";
						bufContent.append(StringUtil.nickNameColor(defendUser))
								.append("开启了").append(getFiefName(defFiefId))
								.append("的恶魔之门，并成功击溃了来袭的敌人。");
					}
				} else {
					bufTitle = StringUtil.nickNameColor(defendUser) + "胜利";
					bufContent
							.append(StringUtil.nickNameColor(attackUser))
							.append(StringUtil.color(
									getCountryName(attackUser),
									StringUtil.getNickNameColor(attackUser)))
							.append("向")
							.append(StringUtil.nickNameColor(defendUser))
							.append(StringUtil.color(
									getCountryName(defendUser),
									StringUtil.getNickNameColor(defendUser)))
							.append("的领地发起了").append(getBattleTypeName());
					if (blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber()) {
						bufContent.append("，")
								.append(StringUtil.nickNameColor(defendUser))
								.append("奋起反抗，成功抵御了他的进攻！");
					} else {
						bufContent.append("，被打败后落荒而逃。");
					}
				}

			}
		}

		tiles = new long[tilesList.size()];
		for (int i = 0; i < tilesList.size(); i++) {
			tiles[i] = tilesList.get(i);
		}
		title = bufTitle.toString();
		text = bufContent.toString();
	}

	private String getTroopLogFiefName(long fiefId) {
		StringBuilder bufContent = new StringBuilder();

		if (CacheMgr.holyPropCache.isHoly(fiefId)) {
			bufContent
					.append(StringUtil.color(
							CacheMgr.holyPropCache.getFiefName(fiefId),
							R.color.color24));
		} else if (Account.manorInfoClient.getPos() == fiefId) {
			bufContent.append(StringUtil.color("主城", R.color.color24));
		} else {
			bufContent.append(StringUtil.color("<tile>", R.color.color24))
					.append(StringUtil.color(
							"(" + TileUtil.uniqueMarking(fiefId) + ")",
							R.color.color24));
			tilesList.add(TileUtil.fiefId2TileId(fiefId));
		}

		return bufContent.toString();
	}

	private String getFiefName(long fiefId) {
		StringBuilder bufContent = new StringBuilder();

		if (CacheMgr.holyPropCache.isHoly(fiefId)) {
			bufContent
					.append(StringUtil.color(
							CacheMgr.holyPropCache.getFiefName(fiefId),
							R.color.color24));
		} else if (Account.manorInfoClient.getPos() == fiefId) {
			bufContent.append(StringUtil.color("主城", R.color.color24));
		} else {
			if (null != blogInfo
					&& (blogInfo.getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
							.getNumber() || blogInfo.getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
							.getNumber())) {
				bufContent.append(StringUtil.color("主城", R.color.color24));
			} else {
				bufContent
						.append(StringUtil.color("<tile>", R.color.color24))
						.append(StringUtil.color(
								"(" + TileUtil.uniqueMarking(fiefId) + ")",
								R.color.color24)).append("领地");
				tilesList.add(TileUtil.fiefId2TileId(fiefId));
			}
		}
		return bufContent.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long[] getTiles() {
		return tiles;
	}

	public void setTiles(long[] tiles) {
		this.tiles = tiles;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private int getTroopCount() {
		if (logInfo == null || !logInfo.hasInfo())
			return 0;
		int count = 0;
		for (ArmInfo a : logInfo.getInfo().getInfosList()) {
			count += a.getCount();
		}
		return count;
	}

	private boolean isAssist() {
		if (!logInfo.hasParams())
			return false;

		return logInfo.getParams(0) == TROOP_ROLE.TROOP_ROLE_ATTACE_REINFORCE.number
				|| logInfo.getParams(0) == TROOP_ROLE.TROOP_ROLE_DEFEND_REINFORCE.number;
	}

	public void toTroopDetail() {
		if (logInfo == null)
			return;
		StringBuilder bufContent = new StringBuilder();
		StringBuilder bufTitle = new StringBuilder();

		switch (TroopLogEvent.valueOf(logInfo.getEvent())) {
		// 进攻
		case EVENT_TROOP_ATTACK_RISE:
			if (null != getHeroProp()) {
				bufContent.append("你麾下将领").append(getHeroColorName())
						.append("及");
			} else {
				bufContent.append("你集结了");
			}

			bufContent.append(StringUtil.color(getTroopCount() + "", "red"))
					.append("名士兵,").append("踏上了进攻")
					.append(getTroopLogFiefName(logInfo.getDst()))
					.append("领地的征程！");
			// tilesList.add(TileUtil.fiefId2TileId(logInfo.getDst()));
			break;

		// 援助
		case EVENT_TROOP_ASSIST:
			if (null != getHeroProp()) {
				bufContent.append("你麾下将领").append(getHeroColorName())
						.append("及");
			} else {
				bufContent.append("你集结了");
			}
			bufContent.append(StringUtil.color(getTroopCount() + "", "red"))
					.append("名士兵,").append("踏上了援助")
					.append(getTroopLogFiefName(logInfo.getDst()))
					.append("领地的征程！");
			// tilesList.add(TileUtil.fiefId2TileId(logInfo.getDst()));
			break;
		// 移动
		case EVENT_TROOP_MOVE:
			bufContent.append("从")
					.append(getTroopLogFiefName(logInfo.getSrc())).append("调往")
					.append(getTroopLogFiefName(logInfo.getDst())).append("的");
			if (null != getHeroProp()) {
				bufContent.append(getHeroColorName()).append("及");
			}
			bufContent.append(StringUtil.color(getTroopCount() + "", "red"))
					.append("名士兵出发了!");
			// tilesList.add(TileUtil.fiefId2TileId(logInfo.getSrc()));
			// tilesList.add(TileUtil.fiefId2TileId(logInfo.getDst()));
			break;
		// EVENT_TROOP_BACK_MANOR时间包括增援返回的情况
		case EVENT_TROOP_BACK_MANOR:
			if (isAssist()) {
				bufContent.append("你派往援助")
						.append(getTroopLogFiefName(logInfo.getSrc()))
						.append("的士兵,剩余")
						.append(StringUtil.color(getTroopCount() + "", "red"))
						.append("人");

				if (null != getHeroProp())
					bufContent.append("随着将领").append(getHeroColorName());

				bufContent.append("返回了主城");
			} else {
				if (null != getHeroProp()) {
					bufContent.append("你麾下将领").append(getHeroColorName())
							.append("及");
				} else {
					bufContent.append("你有一支");
				}

				bufContent
						.append(StringUtil.color(getTroopCount() + "", "red"))
						.append("人的部队，从")
						.append(getTroopLogFiefName(logInfo.getSrc()))
						.append("返回了")
						.append(StringUtil.color("主城", R.color.color24));
			}

			// tilesList.add(TileUtil.fiefId2TileId(logInfo.getSrc()));
			break;
		// 饿死
		case EVENT_TROOP_STARVATION:
			bufContent.append("你的士兵因为缺粮，有").append(getTroopCount())
					.append("人逃亡了");
			break;
		default:
			break;
		}

		tiles = new long[tilesList.size()];
		for (int i = 0; i < tilesList.size(); i++) {
			tiles[i] = tilesList.get(i);
		}
		title = bufTitle.toString();
		text = bufContent.toString();
	}

	private HeroProp getHeroProp() {
		HeroProp heroProp = null;
		if (logInfo.getHeroid() > 0) {
			try {
				heroProp = (HeroProp) CacheMgr.heroPropCache.get(logInfo
						.getHeroid());
			} catch (GameException e) {
			}
		}
		return heroProp;
	}

	private String getHeroColorName() {
		String heroName = "";
		if (logInfo.getHeroid() > 0) {
			try {
				HeroProp heroProp = (HeroProp) CacheMgr.heroPropCache
						.get(logInfo.getHeroid());
				// TroopLogInfo中需要给talent
				// HeroQuality heroQuality = (HeroQuality)
				// CacheMgr.heroQualityCache
				// .get(heroProp.getType());
				// heroName = StringUtil.getHeroName(heroProp, heroQuality);
			} catch (GameException e) {
			}
		}
		return heroName;
	}
}
