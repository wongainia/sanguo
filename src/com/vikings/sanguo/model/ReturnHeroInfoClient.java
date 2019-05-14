package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ReturnHeroInfo;

public class ReturnHeroInfoClient extends HeroIdBaseInfoClient {

	public ReturnHeroInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	private int userid;
	private int levelDiff; // 等级变化
	private int expDiff; // 经验变化

	// 标识位 ：此将领是否显示过升级框框
	private boolean showed = false;

	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}

	private List<HeroArmPropClient> diffArmProps;// 统兵效果变化

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getLevelDiff() {
		return levelDiff;
	}

	public void setLevelDiff(int levelDiff) {
		this.levelDiff = levelDiff;
	}

	public int getExpDiff() {
		return expDiff;
	}

	public void setExpDiff(int expDiff) {
		this.expDiff = expDiff;
	}

	public List<HeroArmPropClient> getDiffArmProps() {
		return diffArmProps == null ? new ArrayList<HeroArmPropClient>()
				: diffArmProps;
	}

	public void setDiffArmProps(List<HeroArmPropClient> diffArmProps) {
		this.diffArmProps = diffArmProps;
	}

	public static ReturnHeroInfoClient convert(ReturnHeroInfo info)
			throws GameException {
		if (null == info)
			return null;
		ReturnHeroInfoClient rhic = new ReturnHeroInfoClient(info.getHero(),
				info.getHeroid(), info.getType(), info.getTalent());
		rhic.setUserid(info.getUserid());
		rhic.setLevelDiff(info.getLevelDiff());
		rhic.setExpDiff(info.getExpDiff());
		if (info.hasDiffArmProps())
			rhic.setDiffArmProps(HeroArmPropClient.convert2List(info
					.getDiffArmPropsList()));
		return rhic;
	}

	public int getLevelUpExp(int heroLvl) {
		int lvlExp = 0;

		// 计算实际升级经验
		for (int i = 0; i < getLevelDiff(); i++)
			lvlExp += ((HeroLevelUp) CacheMgr.heroLevelUpCache.getExp(
					getHeroType().getType(), star, heroLvl + i)).getNeedExp();

		return lvlExp;
	}

	public int getUpdateExp(int heroLvl) {
		return getLevelUpExp(heroLvl) + getExpDiff();
	}
}
