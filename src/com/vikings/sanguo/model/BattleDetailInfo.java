/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-10 上午10:32:08
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.List;
import java.util.ArrayList;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.utils.StringUtil;

public class BattleDetailInfo {
	private long id; // 战斗信息id
	private int type; // 1 占领 2 掠夺 3 副本
	private int result; // 1:攻方胜利 2：守方胜利
	private int time; // 开战时间
	private BattleSkill fiefDefence; // 城防技能
	private int attackerType; // 进攻者类型： 1：领主 2：庄园主 3：黑暗精灵
	private List<BattleLogReturnInfoClient> battleReturnInfos;// 用户战利品
	private List<ReturnHeroInfoClient> rhics; // 将领回报信息
	private int version;// 日志版本号
	private int pop; // 俘虏人数，正数是俘虏别人，负数是被人俘虏
	private int money;// 掠夺钱，正数是掠夺别人，负数是被人掠夺
	private int record; // 战绩

	public void setAttackerType(int attackerType) {
		this.attackerType = attackerType;
	}

	public void setBattleReturnInfos(
			List<BattleLogReturnInfoClient> battleReturnInfos) {
		this.battleReturnInfos = battleReturnInfos;
	}

	public void addBattleReturnInfos(BattleLogReturnInfoClient info) {
		if (null == info)
			return;

		if (null == battleReturnInfos)
			battleReturnInfos = new ArrayList<BattleLogReturnInfoClient>();

		battleReturnInfos.add(info);
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void setPop(int pop) {
		this.pop = pop;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void setRhics(List<ReturnHeroInfoClient> rhics) {
		this.rhics = rhics;
	}

	public void addReturnHeroInfoClient(ReturnHeroInfoClient info) {
		if (null == info)
			return;

		if (null == this.rhics)
			this.rhics = new ArrayList<ReturnHeroInfoClient>();

		rhics.add(info);
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getAttackerType() {
		return attackerType;
	}

	public List<BattleLogReturnInfoClient> getBattleReturnInfos() {
		return battleReturnInfos == null ? new ArrayList<BattleLogReturnInfoClient>()
				: battleReturnInfos;
	}

	public long getId() {
		return id;
	}

	public int getMoney() {
		return money;
	}

	public int getPop() {
		return pop;
	}

	public int getResult() {
		return result;
	}

	public List<ReturnHeroInfoClient> getRhics() {
		return rhics == null ? new ArrayList<ReturnHeroInfoClient>() : rhics;
	}

	public int getTime() {
		return time;
	}

	public int getType() {
		return type;
	}

	public int getVersion() {
		return version;
	}

	// 是否是新日志
	public boolean isNewLog() {
		return 1 == version;
	}

	public boolean isAtkWin() {
		return 1 == result;
	}

	public boolean isDefWin() {
		return 2 == result;
	}

	public boolean hasMyBooty() {
		if (null == battleReturnInfos)
			return false;

		for (BattleLogReturnInfoClient it : battleReturnInfos) {
			if (it.getReturnInfoClient().getUserId() == Account.user.getId())
				return true;
		}
		return false;
	}

	public void setFiefDefence(BattleSkill fiefDefence) {
		this.fiefDefence = fiefDefence;
	}

	public BattleSkill getFiefDefence() {
		return fiefDefence;
	}

	public void setRecord(int record) {
		this.record = record;
	}

	public int getRecord() {
		return record;
	}

	// 是否有奖励
	public boolean hasBonus() {
		for (BattleLogReturnInfoClient it : battleReturnInfos) {
			if (it.getUserId() == Account.user.getId()) {
				if (it.getType() == 1
						|| it.getType() == 2
						|| it.getType() == 3
						|| it.getType() == 5
						|| (it.getType() == 4 && !StringUtil.isNull(it
								.getReturnInfoClient().toTextDesc(true))))
					return true;
			}
		}

		return false;
	}
}
