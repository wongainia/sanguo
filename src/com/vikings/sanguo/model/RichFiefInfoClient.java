package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.RichFiefInfo;

public class RichFiefInfoClient implements Comparable<RichFiefInfoClient> {
	private long fiefid;

	private FiefInfoClient fiefInfo;

	public RichFiefInfoClient() {
	}

	public RichFiefInfoClient(FiefInfoClient fiefInfo) {
		this.fiefInfo = fiefInfo;
		this.fiefid = fiefInfo.getId();
	}

	public List<ArmInfoClient> getArriveTroop() {
		Map<Integer, ArmInfoClient> map = new HashMap<Integer, ArmInfoClient>();
		if (null != fiefInfo)
			merge(map, fiefInfo.getTroopInfo());
		List<ArmInfoClient> armInfos = new ArrayList<ArmInfoClient>();
		for (Entry<Integer, ArmInfoClient> entry : map.entrySet()) {
			armInfos.add(entry.getValue());
		}
		return armInfos;
	}

	public List<HeroIdInfoClient> getAllHeroInfos() {
		if (null != fiefInfo && null != fiefInfo.getFhic())
			return fiefInfo.getFhic().getAllHeros();

		return new ArrayList<HeroIdInfoClient>();
	}

	public List<HeroIdInfoClient> getSecondHeroInfos() {
		List<HeroIdInfoClient> secondHeroInfos = new ArrayList<HeroIdInfoClient>();
		if (null != fiefInfo) {
			List<HeroIdInfoClient> list = fiefInfo.getFhic()
					.getSecondHeroInfos();
			if (null != list && !list.isEmpty())
				secondHeroInfos.addAll(list);
		}
		return secondHeroInfos;
	}

	private void merge(Map<Integer, ArmInfoClient> map, List<ArmInfoClient> list) {
		for (ArmInfoClient armInfo : list) {
			if (map.containsKey(armInfo.getId())) {
				ArmInfoClient info = map.get(armInfo.getId());
				info.setCount(info.getCount() + armInfo.getCount());
			} else {
				try {
					ArmInfoClient info = new ArmInfoClient(armInfo.getId(),
							armInfo.getCount());
					map.put(info.getId(), info);
				} catch (GameException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public long getFiefid() {
		return fiefid;
	}

	public void setFiefid(long fiefid) {
		this.fiefid = fiefid;
	}

	public FiefInfoClient getFiefInfo() {
		return fiefInfo;
	}

	public void setFiefInfo(FiefInfoClient fiefInfo) {
		this.fiefInfo = fiefInfo;
	}

	public static RichFiefInfoClient convert(RichFiefInfo richFiefInfo)
			throws GameException {
		RichFiefInfoClient info = new RichFiefInfoClient();
		info.setFiefid(richFiefInfo.getFiefid());
		if (richFiefInfo.hasFiefInfo() && richFiefInfo.getFiefInfo().hasInfo())
			info.setFiefInfo(FiefInfoClient.convert(richFiefInfo.getFiefInfo()
					.getInfo()));

		return info;
	}

	public BriefFiefInfoClient brief() {
		BriefFiefInfoClient info = new BriefFiefInfoClient();
		info.setId(getFiefid());
		info.setProp(fiefInfo.getProp());
		info.setBuilding(fiefInfo.getBuilding());
		info.setUserId(fiefInfo.getUserid());
		info.setLord(Account.user.bref());
		info.setUnitCount(fiefInfo.getUnitCount());
		info.setBattleState(fiefInfo.getBattleState());
		info.setBattleTime(fiefInfo.getBattleTime());
		info.setHeroIdInfos(fiefInfo.getHeroIdInfos());
		info.setSecondHeroCount(fiefInfo.getSecondHeroCount());
		info.setAttackerId(fiefInfo.getAttackerId());
		try {
			info.setCountry();
			info.setNatureCountry();
		} catch (GameException e) {
			e.printStackTrace();
		}
		if (info.isCastle() && fiefInfo.getUserid() == Account.user.getId()) {
			info.setManor(Account.manorInfoClient);
		}
		return info;
	}

	@Override
	public int compareTo(RichFiefInfoClient another) {
		return (int) (this.fiefid - another.fiefid);
	}

	public boolean isResource() {
		if (null == fiefInfo)
			return false;
		return fiefInfo.isResource();
	}

	/**
	 * 取消守城英雄
	 * 
	 * @param heroInfoClient
	 */
	// TODO 重新写该方法
	public void cancelHero(HeroInfoClient heroInfoClient) {
		// fiefInfo.getFhic().setFirstHeroInfo(null);
		// fiefInfo.getFhic().getSecondHeroInfos().add(heroInfoClient);
	}

	/**
	 * 选择守城英雄
	 * 
	 * @param heroInfoClient
	 */
	public void selHero(HeroInfoClient heroInfoClient) {
		// fiefInfo.getFhic().setFirstHeroInfo(heroInfoClient);
		// fiefInfo.getFhic().getSecondHeroInfos().remove(heroInfoClient);
	}

}
