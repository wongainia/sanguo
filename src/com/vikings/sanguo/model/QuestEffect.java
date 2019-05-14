package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

/**
 * 任务奖励
 * 
 * @author susong
 * 
 */
public class QuestEffect {

	private int questId;

	/*
	 * 1属性，2 道具， 4 将领， 5 士兵
	 */
	private int typeId;

	/*
	 * typeid==1时 typeValue为属性id 2 玩家经验 3 功勋 4 成就值 6 玩家等级 7 元宝 20 金币 21 粮草 22 木材
	 * 23 雪晶 24 魂晶 25 风晶 26 光晶 27 沙晶 28 泪晶 29 水晶
	 * 
	 * typeid==2时， typeValue为道具id 
	 * typeid==4时， typeValue为将领id 
	 * typeid==5时，typeValue为士兵id
	 */
	private int typeValue;

	private int count;

	private Item item;

	private HeroProp hero;

	private TroopProp troop;

	public int getQuestId() {
		return questId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(int typeValue) {
		this.typeValue = typeValue;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;

	}

	public boolean isAttr() {
		return typeId == 1;
	}

	public boolean isItem() {
		return typeId == 2;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public boolean isHero() {
		return typeId == 4;
	}

	public HeroProp getHero() {
		return hero;
	}

	public void setHero(HeroProp hero) {
		this.hero = hero;
	}

	public boolean isTroop() {
		return typeId == 5;
	}

	public TroopProp getTroop() {
		return troop;
	}

	public void setTroop(TroopProp troop) {
		this.troop = troop;
	}

	public static QuestEffect fromString(String csv) {
		QuestEffect qe = new QuestEffect();
		StringBuilder buf = new StringBuilder(csv);
		qe.setQuestId(StringUtil.removeCsvInt(buf));
		qe.setTypeId(StringUtil.removeCsvInt(buf));
		qe.setTypeValue(StringUtil.removeCsvInt(buf));
		qe.setCount(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf); // 客户端不用读概率
		StringUtil.removeCsv(buf); // 客户端不用读描述
		return qe;
	}
}
