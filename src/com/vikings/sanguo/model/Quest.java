package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.StringUtil;

public class Quest {

	public static final byte QUEST_TYPE_MAIN = 1;
	public static final byte QUEST_TYPE_DAILY = 2;
	public static final byte QUEST_TYPE_OTHER = 3;

	public static final byte NO_GUAID = 0;
	public static final byte FORCE_GUAID = 1;
	public static final byte UNFORCE_GUAID = 2;

	public static final int WINDOW_CTR_DEFAULT = 0;
	public static final int WINDOW_CTR_CLOSE_ALL = 1;
	public static final int WINDOW_CTR_CURRENT = 2;

	public static final int SPECIAL_TYPE_NORMAL = 0;
	public static final int SPECIAL_TYPE_BBS = 1;
	public static final int SPECIAL_TYPE_WEB = 2;
	public static final int SPECIAL_TYPE_SITE = 3;
	public static final int SPECIAL_TYPE_UPDATE = 11;
	public static final int SPECIAL_TYPE_VIP = 12;
	public static final int SPECIAL_TYPE_NOVICE_HELP = 13;
	public static final int SPECIAL_TYPE_ARENA = 14;
	public static final int SPECIAL_TYPE_RECHARGE = 15;

	private int id;

	private int type; // 任务类型 1:主线任务； 2：日常任务； 3：其他任务

	private int tag; // 任务标签 0：不显示标签 1：引导 2：剧情

	private int minLevel; // 最小等级，即接任务等级

	private int maxLevel; // 最大等级，即超过等级任务失效

	private String name;

	private String target; // 任务目标

	private String icon;

	private String desc; // 详情

	private int specialType; // 特殊任务类型（0：普通任务 1：论坛任务 2：微博任务
								// 3：官网，11：更新客户端，12：VIP充值赠名将，13：暂无）

	private byte course; // 是否触发教程（0：没有教程动画；1：强制引导教程；2：半强制引导教程）

	private int courseOrder; // 引导排序（0为无引导，编号越小顺序越靠前）

	private int autoComplete; // 是否自动完成 1：自动完成

	private int windowCtr;// 引导结束界面跳转（0为无引导，1为回到地图界面，2为停留在当前引导中断的界面）

	private int timeLimit; // 任务限时（单位秒，0为不限时,从注册时间算起）

	private int sequence; // 任务排序（优先级低于任务的完成状态）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public byte getCourse() {
		return course;
	}

	public void setCourse(byte course) {
		this.course = course;
	}

	public int getCourseOrder() {
		return courseOrder;
	}

	public void setCourseOrder(int courseOrder) {
		this.courseOrder = courseOrder;
	}

	public int getAutoComplete() {
		return autoComplete;
	}

	public void setAutoComplete(int autoComplete) {
		this.autoComplete = autoComplete;
	}

	public int getWindowCtr() {
		return windowCtr;
	}

	public void setWindowCtr(int windowCtr) {
		this.windowCtr = windowCtr;
	}

	public String getTypeName() {
		if (type == QUEST_TYPE_MAIN) {
			return Config.getController().getString(
					R.string.Quest_getTypeName_1);
		} else if (type == QUEST_TYPE_DAILY) {
			return Config.getController().getString(
					R.string.Quest_getTypeName_2);
		} else {
			return Config.getController().getString(
					R.string.Quest_getTypeName_3);
		}
	}

	public String getTagName() {
		String str = "";
		switch (tag) {
		case (byte) 1:
			str = Config.getController().getString(R.string.Quest_getTagName_1);
			break;
		case (byte) 2:
			str = Config.getController().getString(R.string.Quest_getTagName_2);
			break;
		case (byte) 3:
			str = Config.getController().getString(R.string.Quest_getTagName_3);
			break;
		case (byte) 4:
			str = Config.getController().getString(R.string.Quest_getTagName_4);
			break;
		case (byte) 5:
			str = Config.getController().getString(R.string.Quest_getTagName_5);
			break;
		case (byte) 6:
			str = Config.getController().getString(R.string.Quest_getTagName_6);
			break;
		case (byte) 7:
			str = Config.getController().getString(R.string.Quest_getTagName_7);
			break;
		case (byte) 8:
			str = Config.getController().getString(R.string.Quest_getTagName_8);
			break;
		case (byte) 9:
			str = Config.getController().getString(R.string.Quest_getTagName_9);
			break;
		case (byte) 10:
			str = Config.getController()
					.getString(R.string.Quest_getTagName_10);
			break;
		case (byte) 11:
			str = Config.getController()
					.getString(R.string.Quest_getTagName_11);
			break;
		case (byte) 12:
			str = Config.getController()
					.getString(R.string.Quest_getTagName_12);
			break;
		case (byte) 13:
			str = Config.getController()
					.getString(R.string.Quest_getTagName_13);
			break;
		case (byte) 14:
			str = Config.getController()
					.getString(R.string.Quest_getTagName_14);
			break;

		default:
			break;
		}
		return str;
	}

	public boolean isAutoComplete() {
		return autoComplete == 1;
	}

	// 是否是强制引导教程
	public boolean isForcedInstructed() {
		if (1 == course)
			return true;
		return false;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}

	public static Quest fromString(String csv) {
		Quest quest = new Quest();
		StringBuilder buf = new StringBuilder(csv);
		quest.setId(StringUtil.removeCsvInt(buf));
		quest.setType(StringUtil.removeCsvInt(buf));
		quest.setTag(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf); // 跳过接受类型
		quest.setMinLevel(StringUtil.removeCsvInt(buf));
		quest.setMaxLevel(StringUtil.removeCsvInt(buf));
		quest.setName(StringUtil.removeCsv(buf));
		quest.setTarget(StringUtil.removeCsv(buf));
		quest.setIcon(StringUtil.removeCsv(buf));
		quest.setDesc(StringUtil.removeCsv(buf));
		StringUtil.removeCsv(buf); // 跳过最多奖励物品数量
		quest.setSpecialType(StringUtil.removeCsvInt(buf));
		quest.setCourse(StringUtil.removeCsvByte(buf));
		quest.setCourseOrder(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		StringUtil.removeCsv(buf);
		quest.setAutoComplete(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		quest.setWindowCtr(StringUtil.removeCsvInt(buf));
		StringUtil.removeCsv(buf);
		quest.setTimeLimit(StringUtil.removeCsvInt(buf));
		quest.setSequence(StringUtil.removeCsvInt(buf));
		return quest;
	}
}
