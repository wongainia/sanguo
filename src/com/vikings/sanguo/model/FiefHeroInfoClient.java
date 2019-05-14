package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.FiefHeroInfo;

public class FiefHeroInfoClient {
	private List<HeroIdInfoClient> firstHeroInfos; // 守城将领
	private List<HeroIdInfoClient> secondHeroInfos; // 待命将领

	public List<HeroIdInfoClient> getFirstHeroInfos() {
		return firstHeroInfos;
	}

	public void setFirstHeroInfos(List<HeroIdInfoClient> firstHeroInfos) {
		this.firstHeroInfos = firstHeroInfos;
	}

	public List<HeroIdInfoClient> getSecondHeroInfos() {
		return null == secondHeroInfos ? new ArrayList<HeroIdInfoClient>()
				: secondHeroInfos;
	}

	public void setSecondHeroInfos(List<HeroIdInfoClient> secondHeroInfos) {
		this.secondHeroInfos = secondHeroInfos;
	}

	public int getAllHeroCount() {
		return getFirstHeroInfos().size() + getSecondHeroInfos().size();
	}

	public List<HeroIdInfoClient> getAllHeros() {
		List<HeroIdInfoClient> ls = new ArrayList<HeroIdInfoClient>();
		ls.addAll(getFirstHeroInfos());
		ls.addAll(getSecondHeroInfos());
		return ls;
	}

	public static FiefHeroInfoClient convert(FiefHeroInfo info)
			throws GameException {
		if (null == info)
			return null;
		FiefHeroInfoClient fhic = new FiefHeroInfoClient();
		List<HeroIdInfoClient> firsts = new ArrayList<HeroIdInfoClient>();
		if (info.hasFirstHeroInfos()) {
			firsts.addAll(HeroIdInfoClient.convert2List(info
					.getFirstHeroInfosList()));
		}
		fhic.setFirstHeroInfos(firsts);
		List<HeroIdInfoClient> seconds = new ArrayList<HeroIdInfoClient>();
		if (info.hasSecondHeroInfos()) {
			seconds.addAll(HeroIdInfoClient.convert2List(info
					.getSecondHeroInfosList()));
		}
		fhic.setSecondHeroInfos(seconds);
		return fhic;
	}
}
