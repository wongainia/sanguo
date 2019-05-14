/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-7
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.model;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArmInfo;

public class TroopMoveInfoClient {
	private TroopProp armProp; // 军队id
	private int count = 0; // 军队数量
	private int sequence; // 军队触动顺序
	private int time;//到达时间
	private int role;//1为攻击方 2为防御方

	public TroopMoveInfoClient(ArmInfo ai, int role) {
		init(ai.getId(), ai.getCount());
		this.role = role;
	}
	
	public TroopMoveInfoClient(int id, int count, int role) {
		init(id, count);
		this.role = role;
	}
	
	public int getId() {
		return armProp.getId();
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public void init(int id, int count) {
		this.count = count;
		try {
			armProp = (TroopProp) CacheMgr.troopPropCache.get(id);
		} catch (GameException e) {
			e.printStackTrace();
		}
	}

	public TroopMoveInfoClient() {};

	public TroopProp getArmProp() {
		return armProp;
	}

	public int getCount() {
		return count;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	public int getSequence() {
		return sequence;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addCount(ArmInfo info) {
		this.count += info.getCount();
	}

	public void addCount(int _count) {
		this.count += _count;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TroopMoveInfoClient) {
			return getId() == ((TroopMoveInfoClient) obj).getId();
		} else {
			return false;
		}
	}
	
	public TroopMoveInfoClient copy(){
		TroopMoveInfoClient ai = new TroopMoveInfoClient();
		ai.armProp = this.armProp; // 军队id
		ai.count = this.count; // 军队数量
		ai.sequence = this.sequence; // 军队触动顺序
		ai.time = this.time ;//到达时间
		ai.role = this.role;//1为攻击方 0为防御方
		return ai;
	}
}
