package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.FiefHeroInfoClient;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.HeroIdBaseInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.ReturnHeroInfoClient;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.ReturnHeroInfo;
import com.vikings.sanguo.utils.ListUtil;

public class HeroInfoCache {
	private List<HeroInfoClient> infos = new ArrayList<HeroInfoClient>();

	public void merge(SyncData<HeroInfoClient>[] heroInfos) {
		for (int i = 0; i < heroInfos.length; i++) {
			heroInfos[i].update2List(infos);
		}
	}

	// 取缓存的将领
	public List<HeroInfoClient> get() {
		return infos;
	}

	// 取品质高于或等于minTalent的将领
	public List<HeroInfoClient> get(byte minTalent) {
		List<HeroInfoClient> hics = new ArrayList<HeroInfoClient>();
		for (HeroInfoClient hic : infos) {
			if (hic.getTalent() >= minTalent)
				hics.add(hic);
		}
		return hics;
	}

	// 从缓存中取单个将领
	public HeroInfoClient get(long hero) {
		if (null == infos)
			return null;
		for (HeroInfoClient hic : infos) {
			if (hic.getId() == hero)
				return hic;
		}
		return null;
	}

	public List<HeroInfoClient> get(List<Long> heroIds) {
		List<HeroInfoClient> list = new ArrayList<HeroInfoClient>();
		if (null != infos)
			for (HeroInfoClient hic : infos) {
				if (heroIds.contains(hic.getId()))
					list.add(hic);
			}
		return list;
	}

	public boolean hasHero(long heroId) {
		for (HeroInfoClient hic : infos) {
			if (hic.getId() == heroId)
				return true;
		}
		return false;
	}

	// /**
	// * 查询最高品质的英雄
	// *
	// * @return
	// */
	// public HeroInfoClient getTopQuality() {
	// if (infos == null || infos.size() == 0)
	// return null;
	// HeroInfoClient hc = infos.get(0);
	// for (int i = 1; i < infos.size(); i++) {
	// if (hc.getHeroQuality().getType() < infos.get(i).getHeroQuality()
	// .getType())
	// hc = infos.get(i);
	// }
	// return hc;
	// }

	// 没有添加，有则更新
	public HeroInfoClient update(HeroInfoClient heroInfoClient) {
		if (heroInfoClient == null)
			return null;
		synchronized (infos) {
			if (null != infos) {
				// 遍历如果有更新
				for (HeroInfoClient hic : infos) {
					if (hic.getId() == heroInfoClient.getId())
						return hic.update(heroInfoClient);
				}
				// 不存在，添加
				infos.add(heroInfoClient);
			}
			return heroInfoClient;
		}
	}

	public void update(List<HeroInfoClient> heroInfoClients) {
		if (heroInfoClients == null)
			return;
		synchronized (infos) {
			if (null != infos) {
				// 遍历如果有更新
				for (HeroInfoClient newhic : heroInfoClients) {
					boolean has = false;
					for (HeroInfoClient hic : infos) {
						if (hic.getId() == newhic.getId()) {
							hic.update(newhic);
							has = true;
						}
					}
					// 不存在，添加
					if (!has)
						infos.add(newhic);
				}

			}
		}
	}

	public void updateHeroFief(FiefInfoClient fief) {
		FiefHeroInfoClient f = fief.getFhic();
		if (f != null) {
			for (HeroIdBaseInfoClient hi : f.getAllHeros())
				updateHeroFief(hi.getId(), fief.getId());
		}
	}

	private void updateHeroFief(long heroId, long fiefId) {
		HeroInfoClient h = get(heroId);
		if (h != null)
			h.setFiefid(fiefId);
	}

	public void updateReturnHero(List<ReturnHeroInfo> rhis)
			throws GameException { // Client
		if (ListUtil.isNull(rhis))
			return;

		synchronized (infos) {
			if (null != infos && null != rhis) {
				for (HeroInfoClient hic : infos) {
					for (ReturnHeroInfo rhi : rhis) { // Client
						if (hic.getId() == rhi.getHero()) { // getId()
							ReturnHeroInfoClient rhic = ReturnHeroInfoClient
									.convert(rhi);
							hic.setExp(hic.getExp() + rhic.getExpDiff());
							hic.setLevel(hic.getLevel() + rhic.getLevelDiff());
							hic.updateAmpPropInfos(rhic.getDiffArmProps());
						}
					}
				}
			}
		}
	}

	// 删除将领
	public HeroInfoClient delete(long hero) {
		synchronized (infos) {
			if (null != infos) {
				for (Iterator<HeroInfoClient> iter = infos.iterator(); iter
						.hasNext();) {
					HeroInfoClient hic = iter.next();
					if (hic.getId() == hero) {
						iter.remove();
						return hic;
					}
				}
			}
			return null;
		}
	}

	// 删除将领
	public void delete(List<Long> heros) {
		if (null == heros || heros.isEmpty())
			return;
		synchronized (infos) {
			if (null != infos) {
				for (Iterator<HeroInfoClient> iter = infos.iterator(); iter
						.hasNext();) {
					HeroInfoClient hic = iter.next();
					if (heros.contains(hic.getId())) {
						iter.remove();
					}
				}
			}
		}
	}

	public void synDiff() throws GameException {
		SyncDataSet dataSet = GameBiz.getInstance().refreshHeroInfo();
		if (null != dataSet.heroInfos)
			merge(dataSet.heroInfos);
	}

	// private void synAll() throws GameException {
	// SyncDataSet dataSet = GameBiz.getInstance().userDataSyn2(
	// Constants.SYNC_TYPE_ALL, Constants.DATA_TYPE_HERO);
	// if (null == dataSet.heroInfos)
	// dataSet.heroInfos = new SyncData[0];
	// mergeAll(dataSet.heroInfos);
	// }

	public int size() {
		if (null == infos)
			return 0;
		return infos.size();
	}

	// /**
	// * 全量自己的同步hero数据
	// */
	// public void syncDataAll() {
	// new FetchDataInvoker().start();
	// }

	// private void mergeAll(SyncData<HeroInfoClient>[] heroInfos) {
	// infos = new ArrayList<HeroInfoClient>();
	// for (int i = 0; i < heroInfos.length; i++) {
	// HeroInfoClient hic = heroInfos[i].getData();
	// infos.add(hic);
	// }
	// }

	// private class FetchDataInvoker extends BaseInvoker {
	//
	// @Override
	// protected String loadingMsg() {
	// return null;
	// }
	//
	// @Override
	// protected String failMsg() {
	// return null;
	// }
	//
	// @Override
	// protected void fire() throws GameException {
	// while (null == infos) {
	// try {
	// if (null != Account.user && Account.user.getId() > 0)
	// synAll();
	// else
	// Thread.sleep(1000);
	// } catch (Exception e) {
	// Log.e("HeroInfoCache", e.getMessage(), e);
	// infos = null;
	// }
	// }
	//
	// }
	//
	// @Override
	// protected void onOK() {
	//
	// }
	//
	// @Override
	// protected void beforeFire() {
	//
	// }
	//
	// @Override
	// protected void afterFire() {
	//
	// }
	//
	// }

	public List<HeroInfoClient> getHeroInFief(long fiefId) {
		List<HeroInfoClient> hicLs = new ArrayList<HeroInfoClient>();
		for (HeroInfoClient it : infos) {
			if (fiefId == it.getFiefid())
				hicLs.add(it);
		}
		Collections.sort(infos, new Comparator<HeroInfoClient>() {
			@Override
			public int compare(HeroInfoClient lhs, HeroInfoClient rhs) {
				if (lhs.getHeroQuality().getTalent() == rhs.getHeroQuality()
						.getTalent()) {
					if (lhs.getStar() == rhs.getStar()) {
						return rhs.getLevel() - lhs.getLevel();
					} else {
						return rhs.getStar() - lhs.getStar();
					}
				} else {
					return rhs.getHeroQuality().getTalent()
							- lhs.getHeroQuality().getTalent();
				}
			}
		});
		return hicLs;
	}

	public List<HeroInfoClient> getHeroInMainCity() {
		List<HeroInfoClient> heros = getHeroInFief(Account.manorInfoClient
				.getPos());
		Collections.sort(heros, new Comparator<HeroInfoClient>() {
			@Override
			public int compare(HeroInfoClient lhs, HeroInfoClient rhs) {
				return rhs.getLevel() - lhs.getLevel();
			}
		});
		return heros;
	}
}
