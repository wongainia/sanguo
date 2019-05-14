package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.AttrType;
import com.vikings.sanguo.protos.BaseQuestInfo;
import com.vikings.sanguo.protos.ConditionInfo;
import com.vikings.sanguo.protos.QuestInfo;

public class QuestInfoClient {

	private static final byte STATE_INIT = 0;
	private static final byte STATE_START = 1;
	// public static final byte STATE_END = 0;
	private static final byte STATE_COMPLETE = 2;
	// 0：普通任务 1：论坛任务 2：微博任务
	private static final byte STATE_ORDINARY = 0;
	private static final byte STATE_BBS = 1;
	private static final byte STATE_WEIBO = 2;
	private static final byte STATE_WEB = 3;

	// 暂不用
	private long id;

	// 暂不用
	private int userId;

	private int questId;

	private int start;

	private int state;

	private Quest quest;

	private List<QuestCondition> conditions;

	private List<QuestEffect> questEffect;

	private List<QuestConditionInfoClient> qcics;

	private boolean isOpen = false;

	@SuppressWarnings("unchecked")
	public QuestInfoClient(int questId) throws GameException {
		this.questId = questId;
		this.quest = (Quest) CacheMgr.questCache.get(questId);
		this.conditions = CacheMgr.questConditonCache.search(quest.getId());
		this.questEffect = CacheMgr.questEffectCache.search(quest.getId());
		fillEffec();
	}

	public QuestInfoClient() {
	}

	private void fillEffec() throws GameException {
		if (questEffect != null) {
			for (QuestEffect qe : questEffect) {
				// 先填充奖励的Item
				if (qe.isItem())
					qe.setItem((Item) CacheMgr.itemCache.get(qe.getTypeValue()));
				else if (qe.isHero()) {
					qe.setHero((HeroProp) CacheMgr.heroPropCache
							.get(((HeroInit) CacheMgr.heroInitCache.get(qe
									.getTypeValue())).getHeroId()));
				} else if (qe.isTroop()) {
					qe.setTroop((TroopProp) CacheMgr.troopPropCache.get(qe
							.getTypeValue()));
				}
			}
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public List<QuestCondition> getConditions() {
		return null == conditions ? new ArrayList<QuestCondition>()
				: conditions;
	}

	public void setConditions(List<QuestCondition> conditions) {
		this.conditions = conditions;
	}

	public List<QuestEffect> getQuestEffect() {
		return null == questEffect ? new ArrayList<QuestEffect>() : questEffect;
	}

	public void setQuestEffect(List<QuestEffect> questEffect) {
		this.questEffect = questEffect;
	}

	public List<QuestConditionInfoClient> getQcics() {
		return null == qcics ? new ArrayList<QuestConditionInfoClient>()
				: qcics;
	}

	public void setQcics(List<QuestConditionInfoClient> qcics) {
		this.qcics = qcics;
	}

	/**
	 * 某种任务
	 */
	public boolean isOrdinary() {
		return this.quest.getSpecialType() == STATE_ORDINARY;
	}

	public boolean isBBS() {
		return this.quest.getSpecialType() == STATE_BBS;
	}

	public boolean isWeibo() {
		return this.quest.getSpecialType() == STATE_WEIBO;
	}

	public boolean isWeb() {
		return this.quest.getSpecialType() == STATE_WEB;
	}

	// 是否有教程
	public boolean hasCourse() {
		return this.quest.getCourse() == 1;
	}

	/**
	 * 任务状态是否已完成
	 */
	public boolean isComplete() {
		return this.state == STATE_COMPLETE;
	}

	public boolean isInit() {
		return this.state == STATE_INIT;
	}

	public boolean isStart() {
		return this.state == STATE_START;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + questId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestInfoClient other = (QuestInfoClient) obj;
		if (questId != other.questId)
			return false;
		return true;
	}

	public static QuestInfoClient convert(QuestInfo info) throws GameException {
		if (null == info || null == info.getBi())
			return null;

		BaseQuestInfo bqi = info.getBi();
		QuestInfoClient qic = new QuestInfoClient(bqi.getQuestid());
		qic.setId(bqi.getId());
		qic.setUserId(bqi.getUserid());
		qic.setStart(bqi.getStart());
		qic.setState(bqi.getState());
		List<QuestConditionInfoClient> qcics = new ArrayList<QuestConditionInfoClient>();
		if (bqi.hasInfos()) {
			for (ConditionInfo ci : bqi.getInfosList()) {
				qcics.add(QuestConditionInfoClient.convert(ci));
			}
		}
		qic.setQcics(qcics);
		return qic;
	}

	public boolean isWithinTime() {
		if (0 == quest.getTimeLimit())
			return true;

		if (Config.serverTimeSS() < Account.user.getRegTime()
				|| Config.serverTimeSS() >= Account.user.getRegTime()
						+ quest.getTimeLimit())
			return false;

		return true;
	}

	public int getCountDownSecond() {
		int countDown = Account.user.getRegTime() + quest.getTimeLimit()
				- Config.serverTimeSS();
		return countDown > 0 ? countDown : 0;
	}

	public boolean isNormalType() {
		return getQuest().getSpecialType() == Quest.SPECIAL_TYPE_NORMAL
				|| getQuest().getSpecialType() == Quest.SPECIAL_TYPE_BBS
				|| getQuest().getSpecialType() == Quest.SPECIAL_TYPE_WEB
				|| getQuest().getSpecialType() == Quest.SPECIAL_TYPE_SITE;
	}

	public boolean isArenaType() {
		return getQuest().getSpecialType() == Quest.SPECIAL_TYPE_ARENA;
	}

	public boolean isUpdate() {
		return getQuest().getSpecialType() == Quest.SPECIAL_TYPE_UPDATE;
	}

	// 完成任务还差的值
	public int getLeft() {
		int left = 0;
		List<QuestCondition> conditions = getConditions();
		for (QuestCondition condition : conditions) {
			left += condition.getTargetCount();
		}
		List<QuestConditionInfoClient> infos = getQcics();
		for (QuestConditionInfoClient info : infos) {
			left -= info.getValue();
		}
		return left < 0 ? 0 : left;
	}

	public int getNeedExploit() {
		int exploit = 0;
		for (QuestConditionInfoClient it : getQcics()) {
			if (it.getType() == AttrType.ATTR_TYPE_EXPLOIT.number)
				return it.getValue();
		}
		return exploit;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public String toString() {
		String questData = "任务id:" + questId + " 任务类别" + quest.getType()
				+ " 描述：" + quest.getDesc();
		Log.d("QuestInfoClient", questData);
		return questData;
	}

}
