package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArenaLogInfo;
import com.vikings.sanguo.protos.HeroIdInfo;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class ArenaLogInfoClient implements Serializable {
	private static final long serialVersionUID = -2591187980637590296L;
	private long id;
	private int attackerId;
	private BriefUserInfoClient attacker;
	private int attackerPos;
	private int defenderId;
	private BriefUserInfoClient defender;
	private int defenderPos;
	private int battleResult;
	private int time;
	private long battleLog;
	private List<ArenaHero> atkHeros = new ArrayList<ArenaHero>();
	private List<ArenaHero> defHeros = new ArrayList<ArenaHero>();

	public void setAttacker(BriefUserInfoClient attacker) {
		this.attacker = attacker;
	}

	public void setAttackerId(int attackerId) {
		this.attackerId = attackerId;
	}

	public void setAttackerPos(int attackerPos) {
		this.attackerPos = attackerPos;
	}

	public void setBattleResult(int battleResult) {
		this.battleResult = battleResult;
	}

	public void setDefender(BriefUserInfoClient defender) {
		this.defender = defender;
	}

	public void setDefenderId(int defenderId) {
		this.defenderId = defenderId;
	}

	public void setDefenderPos(int defenderPos) {
		this.defenderPos = defenderPos;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBattleLog(long battleLog) {
		this.battleLog = battleLog;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public BriefUserInfoClient getAttacker() {
		return attacker;
	}

	public int getAttackerId() {
		return attackerId;
	}

	public int getAttackerPos() {
		return attackerPos;
	}

	public int getBattleResult() {
		return battleResult;
	}

	public BriefUserInfoClient getDefender() {
		return defender;
	}

	public int getDefenderId() {
		return defenderId;
	}

	public int getDefenderPos() {
		return defenderPos;
	}

	public long getId() {
		return id;
	}

	public int getTime() {
		return time;
	}

	public long getBattleLog() {
		return battleLog;
	}

	private static ArenaLogInfoClient convertData(ArenaLogInfo ali)
			throws GameException {
		ArenaLogInfoClient alic = new ArenaLogInfoClient();

		alic.setId(ali.getId());
		alic.setAttackerId(ali.getAttacker());
		alic.setAttackerPos(ali.getAttackerPos());
		alic.setDefenderId(ali.getDefender());
		alic.setDefenderPos(ali.getDefenderPos());
		alic.setBattleResult(ali.getBattleResult());
		alic.setTime(ali.getTime());
		alic.setBattleLog(ali.getBattleLog());
		// alic.setArenaHeros();
		alic.setDNPHeros(ali);
		return alic;
	}

	public static ArenaLogInfoClient convert(ArenaLogInfo info)
			throws GameException {
		if (null == info)
			return null;
		ArenaLogInfoClient alic = convertData(info);
		List<Integer> userIds = new ArrayList<Integer>();
		if (!userIds.contains(info.getAttacker()))
			userIds.add(info.getAttacker());
		if (!userIds.contains(info.getDefender()))
			userIds.add(info.getDefender());

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
		for (BriefUserInfoClient user : users) {
			if (alic.getAttackerId() == user.getId())
				alic.setAttacker(user);
			if (alic.getDefenderId() == user.getId())
				alic.setDefender(user);
		}

		return alic;
	}

	public static List<ArenaLogInfoClient> convert2List(List<ArenaLogInfo> ls)
			throws GameException {
		List<ArenaLogInfoClient> alics = new ArrayList<ArenaLogInfoClient>();
		if (ListUtil.isNull(ls))
			return alics;

		List<Integer> userIds = new ArrayList<Integer>();
		for (ArenaLogInfo it : ls) {
			if (!userIds.contains(it.getAttacker()))
				userIds.add(it.getAttacker());
			if (!userIds.contains(it.getDefender()))
				userIds.add(it.getDefender());
			alics.add(convertData(it));
		}

		List<BriefUserInfoClient> users = CacheMgr.userCache.get(userIds);
		for (ArenaLogInfoClient it : alics) {
			for (BriefUserInfoClient user : users) {
				if (it.getAttackerId() == user.getId())
					it.setAttacker(user);
				if (it.getDefenderId() == user.getId())
					it.setDefender(user);
			}
		}

		return alics;
	}

	public String getResult() {
		String atk = StringUtil.color("冲榜", R.color.k7_color15);
		String def = StringUtil.color("守榜", "blue");
		String suc = StringUtil.color("成功", R.color.k7_color1);
		String fail = StringUtil.color("失败", R.color.k7_color1);
		if (1 == battleResult) {
			if (attackerId == Account.user.getId())
				return atk + suc;
			else if (defenderId == Account.user.getId())
				return def + fail;
		} else if (2 == battleResult) {
			if (attackerId == Account.user.getId())
				return atk + fail;
			else if (defenderId == Account.user.getId())
				return def + suc;
		}

		return "";
	}

	public String getDesc() {
		String atkPos = StringUtil.color("No." + attackerPos, "blue");
		String defPos = StringUtil.color("No." + defenderPos, "blue");

		String you = StringUtil.color("你", "blue");

		StringBuilder partner = new StringBuilder();
		if (attackerId == Account.user.getId())
			partner.append(you)
					.append("向")
					.append(defPos)
					.append(" ")
					.append(StringUtil.color(defender.getNickName() + "(ID:"
							+ defender.getId() + ")", "blue"));
		else
			partner.append(atkPos)
					.append(" ")
					.append(StringUtil.color(attacker.getNickName() + "(ID:"
							+ attacker.getId() + ")", "blue")).append("向")
					.append(you);

		StringBuilder result = new StringBuilder();
		if (attackerId == Account.user.getId()) {
			if (1 == battleResult)
				result.append(partner).append("发动挑战完爆了对手，你的排名从").append(atkPos)
						.append("上升到").append(defPos).append("。");
			else if (2 == battleResult)
				result.append(partner).append("发动挑战没能够战胜对手，你的排名没有发生变化。");
		}

		else if (defenderId == Account.user.getId()) {
			if (1 == battleResult)
				result.append(partner).append("发动挑战将你击败，你的排名从").append(defPos)
						.append("下降到了").append(atkPos).append("。");
			else if (2 == battleResult)
				result.append(partner).append("向你发动挑战被你击败，你的排名巍然不动。");
		}

		return result.toString();
	}

	public boolean isMeWin() {
		if ((attackerId == Account.user.getId() && 1 == battleResult)
				|| (defenderId == Account.user.getId() && 2 == battleResult))
			return true;
		else
			return false;
	}

	public boolean isMeAtk() {
		return attackerId == Account.user.getId();
	}

	private void addHero(boolean isDead, HeroIdBaseInfoClient atkHero,
			List<ArenaHero> ahs) {
		ArenaHero ah = new ArenaHero();
		ah.setHero(atkHero);
		ah.setDead(isDead);
		ahs.add(ah);
	}

	private void setDNPHeros(ArenaLogInfo ali) throws GameException {
		if (ali.hasAttackHeros()) {
			for (HeroIdInfo it : ali.getAttackHerosList()) {
				if (!isContain(it.getHero(), atkHeros))
					addHero(false, HeroIdInfoClient.convert(it), atkHeros);
			}
		}

		if (ali.hasDefendHeros()) {
			for (HeroIdInfo it : ali.getDefendHerosList()) {
				if (!isContain(it.getHero(), defHeros))
					addHero(false, HeroIdInfoClient.convert(it), defHeros);
			}
		}
	}

	private boolean isContain(long id, List<ArenaHero> heros) {
		if (ListUtil.isNull(heros))
			return false;

		for (ArenaHero ah : heros) {
			if (id == ah.getHero().getId())
				return true;
		}

		return false;
	}

	public List<ArenaHero> getAtkHeros() {
		return atkHeros;
	}

	public List<ArenaHero> getDefHeros() {
		return defHeros;
	}

	public boolean isAtkWin() {
		return 1 == battleResult;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;

		if (getClass() != o.getClass())
			return false;

		if (this != o)
			return false;

		ArenaLogInfoClient alic = (ArenaLogInfoClient) o;
		return this.id == alic.getId();
	}

	public int getHeroIdx(boolean isAtk, long hero) {
		List<ArenaHero> heros = isAtk ? atkHeros : defHeros;
		for (int i = 0; i < heros.size(); i++) {
			if (heros.get(i).getHero().getId() == hero)
				return i + 1;
		}

		return 1;
	}
}
