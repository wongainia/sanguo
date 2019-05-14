package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class PropCampaignMode {
	public static final byte PLAYER_STATE_ATTACK = 1;// 玩家角色进攻
	public static final byte PLAYER_STATE_DEFEND = 2;// 玩家角色防守
	public static final int MAX_HERO_COUNT = 3; // 副本最多将领个数

	private int id; // 战役ID
	private int preCampaignId; // 前置开启战役ID（0为无前置）
	private byte playerState; // 用户状态（1：进攻，2：防守）
	private int cityDefence; // 城防等级（填写对应的城防技能ID）
	private int foeHeroId1;// 敌军主将方案（对应hero_init方案号，0为无将领）
	private int foeHeroId2;// 敌军副将1将方案（对应hero_init方案号，0为无将领）
	private int foeHeroId3;// 敌军副将2方案（对应hero_init方案号，0为无将领）
	private int ownHeroId1;// 我方主将方案（对应hero_init方案号，0为无将领）
							// “我方将领”三个中有一个配置了，用户的将领就不能上场
	private int ownHeroId2;// 我方副将1方案（对应hero_init方案号，0为无将领）
	private int ownHeroId3;// 我方副将2方案（对应hero_init方案号，0为无将领）
	private int foeTroopSchemeId; // 敌军士兵方案号（对应prop_campaign_troop）
	private int ownTroopSchemeId; // 我军士兵方案号（对应prop_campaign_troop） 为0时系统盟军不上场
	private int firstPassSpoil; // 首次通关奖励方案id
	private int passSpoil; // 后续刷奖励方案ID
	private int maxEnemyBoss; // 敌方boss出场数量（填写本战场出现的BOSS数量，0为无BOSS）
	private int bossId; // boss刷新方案ID（无BOSS填0）
	private int userStaminaCost;// 副本行动力消耗
	private String background; // 副本背景（直接填写图片名称）
	private int openSolutionId; // 副本开启时间方案ID（对应prop_time_condition；0表示永久开放）
	private byte minHeroTalent;// 自己将领最低品质要求，0无限制

	private String battleWallUp; // 城墙(上）
	private String battleWallDown;// 城墙(下)

	private byte boss;// 是否boss关

	private byte ownTroop;// 玩家兵力上场（1能 0不能）

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public void setFirstPassSpoil(int firstPassSpoil) {
		this.firstPassSpoil = firstPassSpoil;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMaxEnemyBoss(int maxEnemyBoss) {
		this.maxEnemyBoss = maxEnemyBoss;
	}

	public void setPassSpoil(int passSpoil) {
		this.passSpoil = passSpoil;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setCityDefence(int cityDefence) {
		this.cityDefence = cityDefence;
	}

	public void setPreCampaignId(int preCampaignId) {
		this.preCampaignId = preCampaignId;
	}

	public int getCityDefence() {
		return cityDefence;
	}

	public int getBossId() {
		return bossId;
	}

	public int getFirstPassSpoil() {
		return firstPassSpoil;
	}

	public int getId() {
		return id;
	}

	public int getMaxEnemyBoss() {
		return maxEnemyBoss;
	}

	public int getPassSpoil() {
		return passSpoil;
	}

	public String getBackground() {
		if (null == background && background.equals(""))
			return "battle_map.jpg";
		return background;
	}

	public void setPlayerState(byte playerState) {
		this.playerState = playerState;
	}

	public byte getPlayerState() {
		return playerState;
	}

	public int getPreCampaignId() {
		return preCampaignId;
	}

	public void setOpenSolutionId(int openSolutionId) {
		this.openSolutionId = openSolutionId;
	}

	public int getOpenSolutionId() {
		return openSolutionId;
	}

	public boolean openForever() {
		return openSolutionId == 0;
	}

	public int getFoeHeroId1() {
		return foeHeroId1;
	}

	public void setFoeHeroId1(int foeHeroId1) {
		this.foeHeroId1 = foeHeroId1;
	}

	public int getFoeHeroId2() {
		return foeHeroId2;
	}

	public void setFoeHeroId2(int foeHeroId2) {
		this.foeHeroId2 = foeHeroId2;
	}

	public int getFoeHeroId3() {
		return foeHeroId3;
	}

	public void setFoeHeroId3(int foeHeroId3) {
		this.foeHeroId3 = foeHeroId3;
	}

	public int getOwnHeroId1() {
		return ownHeroId1;
	}

	public void setOwnHeroId1(int ownHeroId1) {
		this.ownHeroId1 = ownHeroId1;
	}

	public int getOwnHeroId2() {
		return ownHeroId2;
	}

	public void setOwnHeroId2(int ownHeroId2) {
		this.ownHeroId2 = ownHeroId2;
	}

	public int getOwnHeroId3() {
		return ownHeroId3;
	}

	public void setOwnHeroId3(int ownHeroId3) {
		this.ownHeroId3 = ownHeroId3;
	}

	public boolean canSetOwnHero() {
		return ownHeroId1 == 0 && ownHeroId2 == 0 && ownHeroId3 == 0;
	}

	public int getFoeTroopSchemeId() {
		return foeTroopSchemeId;
	}

	public void setFoeTroopSchemeId(int foeTroopSchemeId) {
		this.foeTroopSchemeId = foeTroopSchemeId;
	}

	public int getOwnTroopSchemeId() {
		return ownTroopSchemeId;
	}

	public void setOwnTroopSchemeId(int ownTroopSchemeId) {
		this.ownTroopSchemeId = ownTroopSchemeId;
	}

	public boolean hasSystemTroop() {
		return ownTroopSchemeId != 0;
	}

	public int getUserStaminaCost() {
		return userStaminaCost;
	}

	public void setUserStaminaCost(int userStaminaCost) {
		this.userStaminaCost = userStaminaCost;
	}

	public boolean checkHeroTalent() {
		return minHeroTalent > 0;
	}

	public byte getMinHeroTalent() {
		return minHeroTalent;
	}

	public void setMinHeroTalent(byte minHeroTalent) {
		this.minHeroTalent = minHeroTalent;
	}

	public String getBattleWallup() {
		return battleWallUp;
	}

	public void setBattleWallup(String battleWallUp) {
		this.battleWallUp = battleWallUp;
	}

	public String getBattleWallDown() {
		return battleWallDown;
	}

	public void setBattleWallDown(String battleWallDown) {
		this.battleWallDown = battleWallDown;
	}

	public byte getBoss() {
		return boss;
	}

	public void setBoss(byte boss) {
		this.boss = boss;
	}

	public boolean isBoss() {
		return boss == 1;
	}

	public byte getOwnTroop() {
		return ownTroop;
	}

	public void setOwnTroop(byte ownTroop) {
		this.ownTroop = ownTroop;
	}

	public boolean canSetOwnTroop() {
		return ownTroop == 1;
	}

	public static PropCampaignMode fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		PropCampaignMode cm = new PropCampaignMode();
		cm.setId(StringUtil.removeCsvInt(buf));
		cm.setPreCampaignId(StringUtil.removeCsvInt(buf));
		cm.setPlayerState(StringUtil.removeCsvByte(buf));
		cm.setCityDefence(StringUtil.removeCsvInt(buf));
		cm.setFoeHeroId1(StringUtil.removeCsvInt(buf));
		cm.setFoeHeroId2(StringUtil.removeCsvInt(buf));
		cm.setFoeHeroId2(StringUtil.removeCsvInt(buf));
		cm.setOwnHeroId1(StringUtil.removeCsvInt(buf));
		cm.setOwnHeroId2(StringUtil.removeCsvInt(buf));
		cm.setOwnHeroId3(StringUtil.removeCsvInt(buf));
		cm.setFoeTroopSchemeId(StringUtil.removeCsvInt(buf));
		cm.setOwnTroopSchemeId(StringUtil.removeCsvInt(buf));
		cm.setFirstPassSpoil(StringUtil.removeCsvInt(buf));
		cm.setPassSpoil(StringUtil.removeCsvInt(buf));
		cm.setMaxEnemyBoss(StringUtil.removeCsvInt(buf));
		cm.setBossId(StringUtil.removeCsvInt(buf));
		cm.setUserStaminaCost(StringUtil.removeCsvInt(buf));
		cm.setBackground(StringUtil.removeCsv(buf));
		cm.setOpenSolutionId(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		cm.setMinHeroTalent(StringUtil.removeCsvByte(buf));
		// 城墙
		cm.setBattleWallup(StringUtil.removeCsv(buf));
		cm.setBattleWallDown(StringUtil.removeCsv(buf));
		StringUtil.removeCsv(buf);
		cm.setBoss(StringUtil.removeCsvByte(buf));
		cm.setOwnTroop(StringUtil.removeCsvByte(buf));
		return cm;
	}
}
