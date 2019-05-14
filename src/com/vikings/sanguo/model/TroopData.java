package com.vikings.sanguo.model;

public class TroopData {
	int id; // 军队的ID
	int type; // 类型 0:表示驻防部队，1表示调动部队
	int cnt; // 人数
	int time;// 到达时间
	long from; // 来自哪个领地
	int mainType;// 主要类型，将领还是士兵
	private BaseHeroInfoClient heroInfoClient;

	public int getMainType() {
		return mainType;
	}

	public void setMainType(int mainType) {
		this.mainType = mainType;
	}

	public BaseHeroInfoClient getHeroInfoClient() {
		return heroInfoClient;
	}

	public void setHeroInfoClient(BaseHeroInfoClient heroInfoClient) {
		this.heroInfoClient = heroInfoClient;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCnt() {
		return cnt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}