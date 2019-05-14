package com.vikings.sanguo.model;

import com.vikings.sanguo.protos.UserStatData;

public class UserStatDataClient {

	/** 类别等级 */
	public static final byte RANK_BY_LEVEL = 0;
	/** 类别财富 */
	// public static final byte RANK_BY_MONEY = 1;
	public static final byte RANK_BY_TAX = 1;
	/** 类别成就 */
	public static final byte RANK_BY_SCORE = 2;
	/** 类别魅力 */
	public static final byte RANK_BY_REGARDE = 3;
	/** 崇拜 */
	public static final byte RANK_BY_CREDIT = 4;
	/** 庄园人口 */
	public static final byte RANK_BY_POPULATION = 5;
	/** 相册被赞值 */
	public static final byte RANK_BY_ALBUM_PRAISE = 6;

	/** 范围好友 */
	public static final byte RANGE_FRIENDS = 0;
	/** 范围同城 */
	public static final byte RANGE_LOCAL = 2;
	/** 范围全服 */
	public static final byte RANGE_ALL = 1;

	private int userId;

	private int level;

	private int money;

	private int exp;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int gameMoney) {
		this.money = gameMoney;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	@Override
	public boolean equals(Object o) {
		if (this.getUserId() == ((UserStatDataClient) o).getUserId())
			return true;
		return false;
	}

	public static UserStatDataClient convert2Client(UserStatData data) {
		UserStatDataClient rankInfo = new UserStatDataClient();
		rankInfo.setUserId(data.getUserid());
		rankInfo.setLevel(data.getLevel().intValue());
		rankInfo.setMoney(data.getMoney());
		rankInfo.setExp(data.getExp());
		return rankInfo;
	}
}
