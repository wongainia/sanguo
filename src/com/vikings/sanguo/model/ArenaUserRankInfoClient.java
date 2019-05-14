package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArenaUserRankInfo;

public class ArenaUserRankInfoClient {
	private BriefUserInfoClient user;
	private int userId;
	private int rank;
	private List<HeroIdInfoClient> arenaHeros;
	private boolean canAttack;

	public void addArenaHeroIdInfoClient(HeroIdInfoClient arenaHero) {
		getArenaHeros().add(arenaHero);
	}

	public void setArenaHeros(List<HeroIdInfoClient> arenaHeros) {
		this.arenaHeros = arenaHeros;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public List<HeroIdInfoClient> getArenaHeros() {
		if (null == arenaHeros)
			arenaHeros = new ArrayList<HeroIdInfoClient>();
		return arenaHeros;
	}

	public int getRank() {
		return rank;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public int getUserId() {
		return userId;
	}

	public boolean canAttack() {
		return canAttack;
	}

	public void update(ArenaUserRankInfoClient auric) {
		if (null == auric || userId != auric.getUserId())
			return;
		setRank(auric.getRank());
		setArenaHeros(auric.getArenaHeros());
		setCanAttack(auric.canAttack());
	}

	public static ArenaUserRankInfoClient convert(
			ArenaUserRankInfo arenaUserRankInfo) throws GameException {
		ArenaUserRankInfoClient auric = new ArenaUserRankInfoClient();
		auric.setUserId(arenaUserRankInfo.getUserid());
		auric.setRank(arenaUserRankInfo.getRank());
		if (arenaUserRankInfo.hasHeroInfo()) {
			for (int i = 0; i < arenaUserRankInfo.getHeroInfoList().size(); i++) {
				if (BriefUserInfoClient.isNPC(arenaUserRankInfo.getUserid()))
					auric.addArenaHeroIdInfoClient(HeroIdInfoClient
							.convertNPC(arenaUserRankInfo.getHeroInfo(i)));
				else
					auric.addArenaHeroIdInfoClient(HeroIdInfoClient
							.convert(arenaUserRankInfo.getHeroInfo(i)));
			}
		}
		return auric;
	}

	// 是否定级
	public boolean hasRank() {
		return rank >= 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArenaUserRankInfoClient other = (ArenaUserRankInfoClient) obj;
		if (userId != other.getUserId())
			return false;
		return true;
	}

}
