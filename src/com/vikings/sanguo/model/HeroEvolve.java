package com.vikings.sanguo.model;

import com.vikings.sanguo.utils.StringUtil;

public class HeroEvolve {
	// 1、步兵 2、骑兵 3、枪兵 4、弓兵 5、法师 6、车械
	private static final int TYPE_DUN = 1;
	private static final int TYPE_QI = 2;
	private static final int TYPE_QIANG = 3;
	private static final int TYPE_GONG = 4;
	private static final int TYPE_FA = 5;
	private static final int TYPE_CHE = 6;

	/*
	 * 进化增加的武力、防护，兵种统率，此处配的是基础值，最终算的时候再*hero_type里的系数;
	 * 如果不同的兵种有不同的上限值增加，则在后面配兵种系数(%); 进化所需的将魂数，这里也是基础值，算的时候再*hero_type里的系数
	 */
	private int heroId;
	private int attackAdd;// 武力增加
	private int defendAdd;// 防御增加
	private int showMaxAdd;// 显性上限增加
	private int hideMaxAdd;// 隐形上限增加
	private int curAdd;// 当前值增加
	private int dunRate;// 盾系数
	private int qiRate;// 骑系数
	private int qiangRate;// 枪系数
	private int gongRate;// 弓系数
	private int faRate;// 法系数
	private int cheRate;// 车系数
	private int soulCount;// 进化所需将魂数（基础值）

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getAttackAdd() {
		return attackAdd;
	}

	public void setAttackAdd(int attackAdd) {
		this.attackAdd = attackAdd;
	}

	public int getDefendAdd() {
		return defendAdd;
	}

	public void setDefendAdd(int defendAdd) {
		this.defendAdd = defendAdd;
	}

	public int getShowMaxAdd() {
		return showMaxAdd;
	}

	public void setShowMaxAdd(int showMaxAdd) {
		this.showMaxAdd = showMaxAdd;
	}

	public int getHideMaxAdd() {
		return hideMaxAdd;
	}

	public void setHideMaxAdd(int hideMaxAdd) {
		this.hideMaxAdd = hideMaxAdd;
	}

	public int getCurAdd() {
		return curAdd;
	}

	public void setCurAdd(int curAdd) {
		this.curAdd = curAdd;
	}

	public int getDunRate() {
		return dunRate;
	}

	public void setDunRate(int dunRate) {
		this.dunRate = dunRate;
	}

	public int getQiRate() {
		return qiRate;
	}

	public void setQiRate(int qiRate) {
		this.qiRate = qiRate;
	}

	public int getQiangRate() {
		return qiangRate;
	}

	public void setQiangRate(int qiangRate) {
		this.qiangRate = qiangRate;
	}

	public int getGongRate() {
		return gongRate;
	}

	public void setGongRate(int gongRate) {
		this.gongRate = gongRate;
	}

	public int getFaRate() {
		return faRate;
	}

	public void setFaRate(int faRate) {
		this.faRate = faRate;
	}

	public int getCheRate() {
		return cheRate;
	}

	public void setCheRate(int cheRate) {
		this.cheRate = cheRate;
	}

	public int getSoulCount() {
		return soulCount;
	}

	public void setSoulCount(int soulCount) {
		this.soulCount = soulCount;
	}

	public int getTroopArmRate(int type) {
		int rate = 100;
		switch (type) {
		case TYPE_DUN:
			rate = getDunRate();
			break;
		case TYPE_QI:
			rate = getQiRate();
			break;
		case TYPE_QIANG:
			rate = getQiangRate();
			break;
		case TYPE_GONG:
			rate = getGongRate();
			break;
		case TYPE_FA:
			rate = getFaRate();
			break;
		case TYPE_CHE:
			rate = getCheRate();
			break;
		default:
			break;
		}
		return rate;
	}

	public static HeroEvolve fromString(String csv) {
		HeroEvolve heroEvolve = new HeroEvolve();
		StringBuilder buf = new StringBuilder(csv);
		heroEvolve.setHeroId(StringUtil.removeCsvInt(buf));
		heroEvolve.setAttackAdd(StringUtil.removeCsvInt(buf));
		heroEvolve.setDefendAdd(StringUtil.removeCsvInt(buf));
		heroEvolve.setShowMaxAdd(StringUtil.removeCsvInt(buf));
		heroEvolve.setHideMaxAdd(StringUtil.removeCsvInt(buf));
		heroEvolve.setCurAdd(StringUtil.removeCsvInt(buf));
		heroEvolve.setDunRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setQiRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setQiangRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setGongRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setFaRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setCheRate(StringUtil.removeCsvInt(buf));
		heroEvolve.setSoulCount(StringUtil.removeCsvInt(buf));
		return heroEvolve;
	}

}
