package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroIdInfo;
import com.vikings.sanguo.protos.OtherLordInfo;
import com.vikings.sanguo.utils.CalcUtil;
import com.vikings.sanguo.utils.ListUtil;

public class OtherLordInfoClient {

	private OtherLordInfo info;
	private List<ArmInfoClient> arenaTroopInfo;
	private List<HeroIdInfoClient> arenaHeroInfos;

	public OtherLordInfo getInfo() {
		return info;
	}

	public void setInfo(OtherLordInfo info) {
		this.info = info;
	}

	public int getArmCount() {
		int count = 0;
		if (null != info) {
			count = info.getUnitCount();
		}
		return count;
	}

	public void setArenaHeroInfos(List<HeroIdInfoClient> arenaHeroInfos) {
		this.arenaHeroInfos = arenaHeroInfos;
	}

	public void setArenaTroopInfo(List<ArmInfoClient> arenaTroopInfo) {
		this.arenaTroopInfo = arenaTroopInfo;
	}

	public List<HeroIdInfoClient> getArenaHeroInfos() {
		return arenaHeroInfos == null ? new ArrayList<HeroIdInfoClient>()
				: arenaHeroInfos;
	}

	public List<Long> getArenaHeroIds() {
		if (ListUtil.isNull(arenaHeroInfos))
			return new ArrayList<Long>();
		else {
			List<Long> ids = new ArrayList<Long>();
			for (HeroIdInfoClient it : arenaHeroInfos) {
				ids.add(it.getId());
			}
			return ids;
		}
	}

	public List<ArmInfoClient> getArenaTroopInfo() {
		return arenaTroopInfo;
	}

	public static OtherLordInfoClient conver(OtherLordInfo oi)
			throws GameException {
		if (oi == null)
			return null;
		OtherLordInfoClient o = new OtherLordInfoClient();
		o.setInfo(oi);
		if (oi.hasArenaTroopInfo())
			o.setArenaTroopInfo(ArmInfoClient.convertList(oi
					.getArenaTroopInfo()));

		List<HeroIdInfoClient> ls = new ArrayList<HeroIdInfoClient>();
		if (oi.hasArenaHeroInfos()) {
			for (HeroIdInfo it : oi.getArenaHeroInfosList()) {
				ls.add(HeroIdInfoClient.convert(it));
			}
		}

		randomPosition(ls);

		o.setArenaHeroInfos(ls);

		return o;
	}

	private static void randomPosition(List<HeroIdInfoClient> list) {
		if (ListUtil.isNull(list))
			return;
		int[] randoms = CalcUtil.randomSerial(list.size());
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRole(randoms[i]);
		}
		Collections.sort(list, new Comparator<HeroIdInfoClient>() {

			@Override
			public int compare(HeroIdInfoClient ahiic1, HeroIdInfoClient ahiic2) {
				return ahiic1.getRole() - ahiic2.getRole();
			}

		});
	}

}
