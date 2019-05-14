package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseBattleInfo;
import com.vikings.sanguo.protos.BattleHeroInfo;
import com.vikings.sanguo.protos.KeyValue;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.TroopUtil;

public class BaseBattleInfoClient {
	private int type;
	private long defendFiefid; // 被攻击方领地id
	private int attacker; // 进攻方用户id
	private int defender; // 防御方用户id
	private int state; // 状态（0：没有战争 1：战斗准备,行军 2:围城中，防守方可以突围 3：围城结束，攻击方可以发生战斗
						// 4：战斗结束）
	private int time; // 进入下一个状态阶段的时间
	private int scale; // 战场领地规模
	private List<PokerInfoclient> attackPokerResult; // 进攻方翻牌结果(每次翻牌对应结果)
	private List<PokerInfoclient> defendPokerResult; // 防御方翻牌信息(每次翻牌对应结果)
	private int attackPokerUnit; // 进攻方翻牌时计算兵力
	private int defendPokerUnit; // 防御方翻牌时计算兵力

	private List<BattleHeroInfoClient> attackHeroInfos; // 进攻方主将
	private List<BattleHeroInfoClient> defendHeroInfos; // 防守方主将

	private FiefScale fiefScale;
	private BriefUserInfoClient attackerUser;
	private BriefUserInfoClient defenderUser;

	private List<KeyValue> usersBuyUnit;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDefendFiefid() {
		return defendFiefid;
	}

	public void setDefendFiefid(long defendFiefid) {
		this.defendFiefid = defendFiefid;
	}

	public int getAttacker() {
		return attacker;
	}

	public void setAttacker(int attacker) {
		this.attacker = attacker;
	}

	public int getDefender() {
		return defender;
	}

	public void setDefender(int defender) {
		this.defender = defender;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCurState() {
		return TroopUtil.getCurBattleState(state, time);
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

	public List<PokerInfoclient> getPokerResult() {
		if (Account.user.getId() == attacker) {
			return getAttackPokerResult();
		} else if (Account.user.getId() == defender) {
			return getDefendPokerResult();
		} else {
			return new ArrayList<PokerInfoclient>();
		}
	}

	public List<PokerInfoclient> getAttackPokerResult() {
		return attackPokerResult == null ? new ArrayList<PokerInfoclient>()
				: attackPokerResult;
	}

	public void setAttackPokerResult(List<PokerInfoclient> attackPokerResult) {
		this.attackPokerResult = attackPokerResult;
	}

	public List<PokerInfoclient> getDefendPokerResult() {
		return defendPokerResult == null ? new ArrayList<PokerInfoclient>()
				: defendPokerResult;
	}

	public void setDefendPokerResult(List<PokerInfoclient> defendPokerResult) {
		this.defendPokerResult = defendPokerResult;
	}

	public int getPokerUnit() {
		if (Account.user.getId() == attacker) {
			return attackPokerUnit;
		} else if (Account.user.getId() == defender) {
			return defendPokerUnit;
		} else {
			return 0;
		}
	}

	public int getAttackPokerUnit() {
		return attackPokerUnit;
	}

	public void setAttackPokerUnit(int attackPokerUnit) {
		this.attackPokerUnit = attackPokerUnit;
	}

	public int getDefendPokerUnit() {
		return defendPokerUnit;
	}

	public void setDefendPokerUnit(int defendPokerUnit) {
		this.defendPokerUnit = defendPokerUnit;
	}

	public List<BattleHeroInfoClient> getAttackHeroInfos() {
		return attackHeroInfos;
	}

	public void setAttackHeroInfos(List<BattleHeroInfoClient> attackHeroInfos) {
		this.attackHeroInfos = attackHeroInfos;
	}

	public List<BattleHeroInfoClient> getDefendHeroInfos() {
		return defendHeroInfos;
	}

	public void setDefendHeroInfos(List<BattleHeroInfoClient> defendHeroInfos) {
		this.defendHeroInfos = defendHeroInfos;
	}

	public FiefScale getFiefScale() {
		return fiefScale;
	}

	public void setFiefScale(FiefScale fiefScale) {
		this.fiefScale = fiefScale;
	}

	public BriefUserInfoClient getAttackerUser() {
		return attackerUser;
	}

	public void setAttackerUser(BriefUserInfoClient attackerUser) {
		this.attackerUser = attackerUser;
	}

	public BriefUserInfoClient getDefenderUser() {
		return defenderUser;
	}

	public void setDefenderUser(BriefUserInfoClient defenderUser) {
		this.defenderUser = defenderUser;
	}

	public void setUsersBuyUnit(List<KeyValue> usersBuyUnit) {
		this.usersBuyUnit = usersBuyUnit;
	}

	public KeyValue getUsersBuyUnit(int userId) {
		if (ListUtil.isNull(usersBuyUnit))
			return null;

		for (KeyValue it : usersBuyUnit) {
			if (userId == it.getKey().intValue())
				return it;
		}

		return null;
	}

	public static BaseBattleInfoClient convert(BaseBattleInfo info)
			throws GameException {
		if (null == info)
			return null;
		BaseBattleInfoClient bbic = new BaseBattleInfoClient();
		bbic.setType(info.getType());
		bbic.setDefendFiefid(info.getDefendFiefid());
		bbic.setAttacker(info.getAttacker());
		bbic.setDefender(info.getDefender());
		bbic.setState(info.getState());
		bbic.setTime(info.getTime());
		bbic.setScale(info.getScale());
		if (info.hasAttackPokerResult())
			bbic.setAttackPokerResult(PokerInfoclient.convert2List(info
					.getAttackPokerResultList()));
		else
			bbic.setAttackPokerResult(new ArrayList<PokerInfoclient>());
		if (info.hasDefendPokerResult())
			bbic.setDefendPokerResult(PokerInfoclient.convert2List(info
					.getDefendPokerResultList()));
		else
			bbic.setDefendPokerResult(new ArrayList<PokerInfoclient>());
		bbic.setAttackPokerUnit(info.getAttackPokerUnit());
		bbic.setDefendPokerUnit(info.getDefendPokerUnit());
		List<BattleHeroInfoClient> ahis = new ArrayList<BattleHeroInfoClient>();
		if (info.hasAttackHeroInfos() && info.getAttackHeroInfosCount() > 0) {
			for (BattleHeroInfo heroInfo : info.getAttackHeroInfosList())
				ahis.add(BattleHeroInfoClient.convert(heroInfo));
		}
		bbic.setAttackHeroInfos(ahis);

		List<BattleHeroInfoClient> dhis = new ArrayList<BattleHeroInfoClient>();
		if (info.hasDefendHeroInfos() && info.getDefendHeroInfosCount() > 0) {
			for (BattleHeroInfo heroInfo : info.getDefendHeroInfosList())
				dhis.add(BattleHeroInfoClient.convert(heroInfo));
		}
		bbic.setDefendHeroInfos(dhis);

		if (info.hasUsersBuyUnit())
			bbic.setUsersBuyUnit(info.getUsersBuyUnitList());
		return bbic;
	}

	public String getTypeName() {
		switch (type) {
		case 1:
			return "占领";
		case 2:
			return "掠夺";
		case 3:
			return "副本";
		case 4:
			return "单挑";
		case 5:
			return "屠城";
		default:
			return "其他";
		}
	}

	public boolean isSingled() {
		return 4 == type;
	}
}
