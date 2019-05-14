/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-11-20 上午10:13:11
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleArrayInfo;
import com.vikings.sanguo.utils.ListUtil;

public class BattleArrayInfoClient {
	private int id; // 兵种ID
	private int morale; // 士气
	private long num; // 数量
	private int row; // 位置
	private long hp = 0; // HP
	private long hp_current; // 当前hp
	private boolean hasConfuse; // 是否有阵形混乱技能
	private List<Buff> buffIds; // Integer
	private int eachArmHp;    //每个士兵的hp
	
	private long initNum;         //开始时的数量
	
	private int dValue = 0; //差值     每次根据血量计算数量的时候   保存上次的余数

	public BattleArrayInfoClient(BattleArrayInfo info) {
		this.id = info.getPropid();
		this.morale = info.getMorale();
		this.num = info.getNum();
		this.row = info.getRow()/* + 1*/;
		this.hasConfuse = false;
		
		try {
			TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(this.id);
			this.eachArmHp = tp.getHp();
			this.hp = tp.getHp() * this.num;
			this.hp_current = this.hp;
		} catch (GameException e) {
			e.printStackTrace();
		}
		buffIds = new ArrayList<Buff>();
		initNum = info.getNum();;
	}
	
	public BattleArrayInfoClient(int id,long num,long hp) {
		this.id = id;		
		this.num = num;
		this.hp = hp;
		this.hp_current = hp;
		this.initNum = num;
		this.hasConfuse = false;
		this.buffIds = new ArrayList<Buff>();
	}
	

	public void setId(int id) {
		this.id = id;
	}

	public void setMorale(int morale) {
		this.morale = morale;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setHasConfuse(boolean hasConfuse) {
		this.hasConfuse = hasConfuse;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}
	
	public void setCurrentHp(long hp) {		
		this.hp_current = hp;
	}
	public int getId() {
		return id;
	}

	public int getMorale() {
		return morale;
	}

	public long getNum() {
		return num;
	}

	public long getInitNum() {
		return initNum;
	}
	
	public int getRow() {
		return row;
	}

	public long getHp() {
		return hp;
	}
	
	public long getCurrentHp() {
		return this.hp_current ;
	}
	public boolean hasConfuse() {
		return hasConfuse;
	}

	public void addBuffId(int id, int cnt) {
//		if(ListUtil.isNull(buffIds))
//		{
//			return;
//		}
		for (Buff it : buffIds) {
			if (it.buffId == id) {
				it.amount = cnt;
				return;
			}
		}

		Buff buff = new Buff();
		buff.buffId = id;
		buff.amount = cnt;
		buffIds.add(buff);
	}

	public void removeBuffId(int id) {
//		if(ListUtil.isNull(buffIds))
//		{
//			return;
//		}
		Iterator<Buff> it = buffIds.iterator();
		while (it.hasNext()) {
			Buff buff = it.next();
			if (buff.buffId == id) {
				it.remove();
				break;
			}
		}
	}

	public int getLastBuffId() {
		if (ListUtil.isNull(buffIds))
			return 0;

		return buffIds.get(buffIds.size() - 1).buffId;
	}

	public boolean hasBuff() {
		if (ListUtil.isNull(buffIds))
			return false;

		return buffIds.size() > 0;
	}

	public List<Buff> getBuffIds() {
		return buffIds;
	}

	public Buff getBuffById(int id) {
		for (Buff it : buffIds) {
			if (it.buffId == id)
				return it;
		}
		return null;
	}
	
	//增强效果
	public void addHp(long value)
	{
		this.hp =  this.hp + value;
		this.hp_current = this.hp;
	}
	
	//每个士兵的血量
	public int getEachArmHp()
	{
		return eachArmHp;
	}
	
	public void addArmHp(int value)
	{
		 this.eachArmHp = this.eachArmHp + value;
	}
	
	public int getDValue()
	{
		return dValue;
	}
	
	public void setDValue(int dvalue)
	{
		 this.dValue = dvalue;
	}
	
	public String toSting()
	{
		return "\r\n兵种ID:" + id + "\n数量:" + num
				+ "\n位置:" + row + "\nHP:"
				+ hp + "\n当前hp:" + hp_current
				+ "\n每个士兵的hp:" + eachArmHp +"\r\n";
	}
}
