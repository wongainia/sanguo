package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.protos.BattleHotInfo;
import com.vikings.sanguo.utils.StringUtil;

public class BattleHotInfoClient {

	private int attackerId; // 发起进攻的领主id
	private int defenderId; // 防守方领主Id
	private int totalAtkTroop; // 发起进攻的总兵力
	private int totalDefTroop; // 防守方的总兵力
	private long fiefid; // 被进攻的领地id
	private long battleLogId; // 战斗日志id
	private int time; // 开战时间
	private int scale; // 领地规模，3：Lv4、4：Lv5
	private int result; // 结果，1:攻方胜利 2：守方胜利
	private int type; // 战争类型
	private HeroIdBaseInfoClient attackerHeroId; // 进攻方英雄
	private HeroIdBaseInfoClient defenderHeroId; // 防守方英雄

	private BriefUserInfoClient attacker;
	private BriefUserInfoClient defender;

	public void setAttacker(BriefUserInfoClient attacker) {
		this.attacker = attacker;
	}

	public BriefUserInfoClient getAttacker() {
		if (attacker == null)
			return new BriefUserInfoClient();
		else
			return attacker;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}

	public int getAttackerId() {
		return attackerId;
	}

	public void setTotalAtkTroop(int totalTroop) {
		this.totalAtkTroop = totalTroop;
	}

	public long getFiefid() {
		return fiefid;
	}

	public void setFiefid(long fiefid) {
		this.fiefid = fiefid;
	}

	public long getBattleLogId() {
		return battleLogId;
	}

	public void setBattleLogId(long battleLogId) {
		this.battleLogId = battleLogId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void setTotalDefTroop(int totalDefTroop) {
		this.totalDefTroop = totalDefTroop;
	}

	public int getTotalAtkTroop() {
		return totalAtkTroop;
	}

	public int getTotalDefTroop() {
		return totalDefTroop;
	}

	public void setDefenderId(int defenderId) {
		this.defenderId = defenderId;
	}

	public int getDefenderId() {
		return defenderId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HeroIdBaseInfoClient getAttackerHeroId() {
		return attackerHeroId;
	}

	public void setAttackerHeroId(HeroIdBaseInfoClient attackerHeroId) {
		this.attackerHeroId = attackerHeroId;
	}

	public HeroIdBaseInfoClient getDefenderHeroId() {
		return defenderHeroId;
	}

	public void setDefenderHeroId(HeroIdBaseInfoClient defenderHeroId) {
		this.defenderHeroId = defenderHeroId;
	}

	public void setDefender(BriefUserInfoClient defender) {
		this.defender = defender;
	}

	public BriefUserInfoClient getDefender() {
		if (defender == null)
			return new BriefUserInfoClient();
		else
			return defender;
	}

	public String getBattleTypeName() {
		String typeName = "战争";
		if (type == BattleAttackType.E_BATTLE_COMMON_ATTACK.getNumber()) {
			typeName = StringUtil.color("征讨战争", R.color.k7_color4);
		} else if (type == BattleAttackType.E_BATTLE_PLUNDER_ATTACK.getNumber()) {
			typeName = StringUtil.color("掠夺战争", R.color.k7_color4);
		} else if (type == BattleAttackType.E_BATTLE_DUEL_ATTACK.getNumber()) {
			typeName = StringUtil.color("单挑战争", R.color.k7_color4);
		} else if (type == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
				.getNumber()) {
			typeName = StringUtil.color("屠城战争", R.color.k7_color4);
		}
		return typeName;
	}

	public String getAttackCountry() {
		return CacheMgr.countryCache.getCountry(attacker.getCountry())
				.getName();
	}

	public String getDefendCountry() {
		if (null == defender)
			return "";
		return CacheMgr.countryCache.getCountry(defender.getCountry())
				.getName();
	}

	private static BattleHotInfoClient convert(BattleHotInfo info)
			throws GameException {
		if (null == info)
			return null;
		BattleHotInfoClient bhic = new BattleHotInfoClient();
		bhic.setAttackerId(info.getAttacker());
		bhic.setDefenderId(info.getDefender());
		bhic.setTotalAtkTroop(info.getAttackTotalTroop());
		bhic.setTotalDefTroop(info.getDefendTotalTroop());
		bhic.setFiefid(info.getFiefid());
		bhic.setBattleLogId(info.getBattleLogId());
		bhic.setTime(info.getTime());
		bhic.setScale(info.getScale());
		bhic.setResult(info.getResult());
		bhic.setType(info.getType());
		bhic.setAttackerHeroId(HeroIdInfoClient.convert(info
				.getAttackHeroInfo()));
		bhic.setDefenderHeroId(HeroIdInfoClient.convert(info
				.getDefendHeroInfo()));
		return bhic;
	}

	public static List<BattleHotInfoClient> convertList(
			List<BattleHotInfo> infos) throws GameException {
		List<BattleHotInfoClient> ghics = new ArrayList<BattleHotInfoClient>();
		if (null == infos || infos.isEmpty())
			return ghics;
		for (BattleHotInfo info : infos) {
			ghics.add(BattleHotInfoClient.convert(info));
		}
		CacheMgr.fillBattleHotInfoClients(ghics);
		return ghics;
	}
}
