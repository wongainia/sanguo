package com.vikings.sanguo.model;

import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.utils.StringUtil;

public class PropStronger {
	// 已经完成未领奖
	public static final int FINISHED = 0;
	// 已经完成并且已领奖
	public static final int AWARDED = 1;
	// 未完成的任务
	public static final int UNFINISHED_TASK = 2;

	// 不推荐任何类型
	public static final int UNRECOMMEND = 10;

	private int type;// 变强类型
	private int star;// 评价星级
	private String icon;// 图标
	private String desc;// 文字描述
	private int jumpId;
	private int questId;
	private String url;//

	private QuestInfoClient questInfoClient;

	public QuestInfoClient getQuestInfoClient() {
		return questInfoClient;
	}

	public void setQuestInfoClient(QuestInfoClient questInfoClient) {
		this.questInfoClient = questInfoClient;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getJumpId() {
		return jumpId;
	}

	public void setJumpId(int jumpId) {
		this.jumpId = jumpId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static PropStronger fromString(String line) {
		PropStronger ps = new PropStronger();
		StringBuilder buf = new StringBuilder(line);
		ps.setType(StringUtil.removeCsvInt(buf));
		ps.setStar(StringUtil.removeCsvInt(buf));
		ps.setIcon(StringUtil.removeCsv(buf));
		ps.setDesc(StringUtil.removeCsv(buf));
		ps.setJumpId(StringUtil.removeCsvInt(buf));
		ps.setQuestId(StringUtil.removeCsvInt(buf));
		ps.setUrl(StringUtil.removeCsv(buf));
		return ps;
	}

	public PropStronger updateQuestInfoClient() {
		questInfoClient = Account.getStrongerQuestInfoById(questId);
		return this;
	}

	public int getQuestState() {
		if (questInfoClient == null) {
			return AWARDED;
		}
		if (questInfoClient.isComplete()) {
			return FINISHED;
		}
		return UNFINISHED_TASK;
	}

	// 是不是未完成的任务
	public boolean isUnfinishedTask() {
		return (getQuestState() == UNFINISHED_TASK);
	}

	// 是不是已经完成的任务 木有领奖
	public boolean isFinishedTask() {
		return (getQuestState() == FINISHED);
	}

	// 得到当前需要强化的 将领 、 设备 、士兵
	public static PropStronger getCurrentPs(int type) {
		List<PropStronger> propStrongers = CacheMgr.propStrongerCache
				.getPsByMainKey(type);
		for (int i = 0; i < propStrongers.size(); i++) {

			// 完成了的任务 但是没有领奖
			if (propStrongers.get(i).getQuestState() == FINISHED) {
				return propStrongers.get(i);
			}
			// 首先遍历未完成的任务
			if (propStrongers.get(i).getQuestState() == UNFINISHED_TASK) {
				return propStrongers.get(i);
			}

		}
		return null;
	}

	public static int getRcommendType() {
		int Type = UNRECOMMEND;
		PropStronger psGeneral = PropStronger.getCurrentPs(Constants.GENERAL);
		PropStronger psEquipment = PropStronger
				.getCurrentPs(Constants.EQUIPMENT);
		PropStronger psSolldier = PropStronger.getCurrentPs(Constants.SOLDIER);

		if (psGeneral != null && psGeneral.isLessStar(2)) {
			Type = Constants.GENERAL;
		} else {
			if (psEquipment != null && psEquipment.isLessStar(3)) {
				Type = Constants.EQUIPMENT;
			} else {
				if (psSolldier != null && psSolldier.isLessStar(2)) {
					Type = Constants.SOLDIER;
				} else {
					if (psGeneral.isLessStar(5)) {
						Type = Constants.GENERAL;
					} else {
						if (psEquipment.isLessStar(5)) {
							Type = Constants.EQUIPMENT;
						} else {
							if (psSolldier.isLessStar(5)) {
								Type = Constants.SOLDIER;
							}
						}
					}
				}
			}
		}

		return Type;
	}

	// 判断 将领、装备、士兵 是否小于指定的星级
	private boolean isLessStar(int star) {
		// if ((this.getStar() == star && this.isUnfinishedTask())
		// || (this.getStar() < star)) {
		// return true;
		// }

		if ((this.getStar() - 1) < star && this.isUnfinishedTask()
				|| (this.getStar() < star) && this.isFinishedTask()) {
			return true;
		}
		return false;
	}

}
