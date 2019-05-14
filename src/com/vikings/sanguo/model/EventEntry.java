package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class EventEntry {
	private int id; // 活动ID（999是VIP首充双倍）
	private int sequence; // 显示排序
	private int relatedQuestid; // 关联任务id（不由任务驱动的填0）
	private int propTimeId; // 关联时间方案号（prop_time）
	private String img; // 图片
	private boolean needCountDown; // 倒计时开关（1开，0关）
	private String countDownDesc; // 倒计时内容（关闭是显示的文字内容）
	private String desc; // 描述
	private String reward; // 奖励
	private int toWindow; // 跳转界面（0无跳转，1跳转url）
	private String url; // 跳转URL

	public void setId(int id) {
		this.id = id;
	}

	public void setPropTimeId(int propTimeId) {
		this.propTimeId = propTimeId;
	}

	public void setRelatedQuestid(int relatedQuestid) {
		this.relatedQuestid = relatedQuestid;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public void setToWindow(int toWindow) {
		this.toWindow = toWindow;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCountDownDesc(String countDownDesc) {
		this.countDownDesc = countDownDesc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setNeedCountDown(boolean needCountDown) {
		this.needCountDown = needCountDown;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getId() {
		return id;
	}

	public int getPropTimeId() {
		return propTimeId;
	}

	public int getRelatedQuestid() {
		return relatedQuestid;
	}

	public int getSequence() {
		return sequence;
	}

	public int getToWindow() {
		return toWindow;
	}

	public String getUrl() {
		return url;
	}

	public String getCountDownDesc() {
		return countDownDesc;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isNeedCountDown() {
		return needCountDown;
	}

	public String getReward() {
		return reward;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public static EventEntry fromString(String line) {
		StringBuilder buf = new StringBuilder(line);
		EventEntry event = new EventEntry();

		event.setId(StringUtil.removeCsvInt(buf));
		event.setSequence(StringUtil.removeCsvInt(buf));
		event.setRelatedQuestid(StringUtil.removeCsvInt(buf));
		event.setPropTimeId(StringUtil.removeCsvInt(buf));
		event.setImg(StringUtil.removeCsv(buf));
		event.setNeedCountDown(1 == StringUtil.removeCsvInt(buf) ? true : false);
		event.setCountDownDesc(StringUtil.removeCsv(buf));
		event.setDesc(StringUtil.removeCsv(buf));
		event.setReward(StringUtil.removeCsv(buf));
		event.setToWindow(StringUtil.removeCsvInt(buf));
		event.setUrl(StringUtil.removeCsv(buf));

		return event;
	}
}
