package com.vikings.sanguo.model;

import java.io.Serializable;
import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BriefBattleInfo;
import com.vikings.sanguo.utils.TroopUtil;

/**
 * 对应于BriefBattleInfo
 * 
 * @author susong
 * 
 */
public class BriefBattleInfoClient implements Serializable {

	private static final long serialVersionUID = -6623751335348966822L;
	// 自己的角色
	public static final int ROLE_ATTACKER = 1;
	public static final int ROLE_ASSIST = 2;
	public static final int ROLE_DEFENDER = 3;

	// 战斗类型
	public static final int BATTLE_TYPE_OCCUPY = 1;
	public static final int BATTLE_TYPE_PLUNDER = 2;

	private long battleid; // 战场id
	private int type; // 1 占领 2 掠夺 3 副本
	private int attacker;// 攻击方id
	private int defender; // 防守方id
	private long attackFiefid; // 攻击方战场
	private long defendFiefid; // 战场的fiefid
	private int state; // 状态（0：没有战争 1：战斗准备,行军 2:围城中，防守方可以突围 3：围城结束，攻击方可以发生战斗
	// 4：战斗结束）
	private int time; // 进入下一个状态阶段的时间
	private int scale; // 战场领地规模
	private int attackUnit; // 攻方兵力数量
	private int defendUnit; // 守方兵力数量

	private FiefScale fiefScale; // 城堡规模图标

	private int stateWhenSave; // 该字段只用于离线提醒

	transient private BriefFiefInfoClient bfic;
	transient private BriefUserInfoClient attackerUser;
	transient private BriefUserInfoClient defendUser;

	public int getRole() {
		if (Account.user.getId() == attacker)
			return ROLE_ATTACKER;
		else if (Account.user.getId() == defender)
			return ROLE_DEFENDER;
		else
			return ROLE_ASSIST;
	}

	public long getDefendFiefid() {
		return defendFiefid;
	}

	public void setDefendFiefid(long defendFiefid) {
		this.defendFiefid = defendFiefid;
	}

	public void setBattleid(long battleid) {
		this.battleid = battleid;
	}

	public long getAttackFiefid() {
		return attackFiefid;
	}

	public void setAttackFiefid(long attackFiefid) {
		this.attackFiefid = attackFiefid;
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

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public long getBattleid() {
		return battleid;
	}

	public String getFiefImage() {
		if (null != fiefScale)
			return fiefScale.getIcon();
		return "";
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getAttackUnit() {
		return attackUnit;
	}

	public void setAttackUnit(int attackUnit) {
		this.attackUnit = attackUnit;
	}

	public int getDefendUnit() {
		return defendUnit;
	}

	public void setDefendUnit(int defendUnit) {
		this.defendUnit = defendUnit;
	}

	public int getStateWhenSave() {
		return stateWhenSave;
	}

	public void setStateWhenSave(int stateWhenSave) {
		this.stateWhenSave = stateWhenSave;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMyUnit() {
		if (Account.user.getId() == attacker)
			return attackUnit;
		else
			return defendUnit;
	}

	public int getEnemyUnit() {
		if (Account.user.getId() != attacker)
			return attackUnit;
		else
			return defendUnit;
	}

	public BriefFiefInfoClient getBfic() {
		return bfic;
	}

	public void setBfic(BriefFiefInfoClient bfic) {
		this.bfic = bfic;
	}

	public BriefUserInfoClient getAttackerUser() {
		return attackerUser;
	}

	public void setAttackerUser(BriefUserInfoClient attackerUser) {
		this.attackerUser = attackerUser;
	}

	public BriefUserInfoClient getDefendUser() {
		return defendUser;
	}

	public void setDefendUser(BriefUserInfoClient defendUser) {
		this.defendUser = defendUser;
	}

	public FiefScale getFiefScale() {
		return fiefScale;
	}

	public void setFiefScale(FiefScale fiefScale) {
		this.fiefScale = fiefScale;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BriefBattleInfoClient) {
			return battleid == ((BriefBattleInfoClient) obj).getBattleid();
		} else {
			return false;
		}
	}

	// public BriefBattleInfoClient update(BriefBattleInfoClient battleInfo) {
	// if (null != battleInfo) {
	// setBattleid(battleInfo.getBattleid());
	// setAttacker(battleInfo.getAttacker());
	// setDefender(battleInfo.getDefender());
	// setAttackFiefid(battleInfo.getAttackFiefid());
	// setDefendFiefid(battleInfo.getDefendFiefid());
	// setState(battleInfo.getState());
	// setTime(battleInfo.getTime());
	// setScale(battleInfo.getScale());
	// setAttackUnit(battleInfo.getAttackUnit());
	// setDefendUnit(battleInfo.getDefendUnit());
	// setFiefScale(battleInfo.getFiefScale());
	// setBfic(battleInfo.getBfic());
	// setAttackerUser(battleInfo.getAttackerUser());
	// setDefendUser(battleInfo.getDefendUser());
	// }
	//
	// return this;
	// }

	public static BriefBattleInfoClient convert(BriefBattleInfo battleInfo)
			throws GameException {
		BriefBattleInfoClient info = new BriefBattleInfoClient();
		info.setBattleid(battleInfo.getId());
		info.setType(battleInfo.getType());
		info.setDefendFiefid(battleInfo.getDefendFiefid());
		info.setAttackFiefid(battleInfo.getAttackFiefid());
		info.setAttacker(battleInfo.getAttacker());
		info.setDefender(battleInfo.getDefender());
		info.setState(battleInfo.getState());
		info.setTime(battleInfo.getTime());
		info.setScale(battleInfo.getScale());
		info.setAttackUnit(battleInfo.getAttackUnit());
		info.setDefendUnit(battleInfo.getDefendUnit());
		info.setFiefScale(CacheMgr.fiefScaleCache.getFiefScale(
				battleInfo.getScale(), battleInfo.getDefendFiefid()));
		return info;
	}
	
	public boolean isMeAttacker() {
		return attacker == Account.user.getId();
	}
	
	public boolean isMeDefender() {
		return defender == Account.user.getId(); 
	}
	
	public boolean isSurroundEndWhenAtk() {
		return isMeAttacker()
				&& BattleStatus.isInSurroundEnd(TroopUtil.getCurBattleState(
						getState(), getTime()));
	}
	
	public boolean isNoNeedToOfflineNotify() {
		return getStateWhenSave() >= TroopUtil.getCurBattleState(getState(),
				getTime());
	}
	
}
