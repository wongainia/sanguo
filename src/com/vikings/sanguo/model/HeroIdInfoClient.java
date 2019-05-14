package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.HeroIdInfo;
import com.vikings.sanguo.utils.ListUtil;

public class HeroIdInfoClient extends HeroIdBaseInfoClient {
	public static final int HERO_ROLE_ATTACK_MAIN = 1; // 进攻方主将
	public static final int HERO_ROLE_ATTACK_ASSIST = 2; // 进攻方副将
	public static final int HERO_ROLE_DEFEND_MAIN = 3; // 防守方主将
	public static final int HERO_ROLE_DEFEND_ASSIST = 4; // 防守方副将

	private static final long serialVersionUID = -7602068612333211635L;
	protected int role; // 将领角色
	protected int schema;// 方案号 （NPC将领该字段才有效）

	public HeroIdInfoClient(long id, int heroId, int star, int talent)
			throws GameException {
		super(id, heroId, star, talent);
	}

	protected HeroIdInfoClient() {
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public boolean isMainHero() {
		return role == HERO_ROLE_ATTACK_MAIN || role == HERO_ROLE_DEFEND_MAIN;
	}

	public int getSchema() {
		return schema;
	}

	public void setSchema(int schema) {
		this.schema = schema;
	}

	public static HeroIdInfoClient HICconvert2This(HeroInfoClient hic) {
		HeroIdInfoClient hiic = new HeroIdInfoClient();
		hiic.setId(hic.getId());
		hiic.setHeroId(hic.getHeroId());
		return hiic;

	}

	public static HeroIdInfoClient convert(HeroIdInfo info)
			throws GameException {
		if (null == info || info.getHero() <= 0)
			return null;
		HeroIdInfoClient hiic = new HeroIdInfoClient(info.getHero(),
				info.getHeroid(), info.getType(), info.getTalent());
		hiic.setRole(info.getRole());
		return hiic;
	}

	public static List<HeroIdInfoClient> convert2List(List<HeroIdInfo> infos)
			throws GameException {
		List<HeroIdInfoClient> list = new ArrayList<HeroIdInfoClient>();
		if (null != infos && !infos.isEmpty()) {
			for (HeroIdInfo info : infos) {
				list.add(convert(info));
			}
		}
		return list;
	}

	public static HeroIdInfoClient searchHiicById(long id,
			List<HeroIdInfoClient> hiics) {
		if (ListUtil.isNull(hiics))
			return null;
		for (HeroIdInfoClient heroIdInfoClient : hiics) {
			if (id == heroIdInfoClient.getId())
				return heroIdInfoClient;
		}
		return null;
	}

	public static HeroIdInfoClient convertNPC(HeroIdInfo info)
			throws GameException {
		if (null == info || info.getHero() < 0)
			return null;

		HeroInit heroInit = (HeroInit) CacheMgr.heroInitCache.get(info
				.getHeroid());
		HeroIdInfoClient hiic = new HeroIdInfoClient(info.getHero(),
				heroInit.getHeroId(), heroInit.getStar(), heroInit.getTalent());
		hiic.setRole(info.getRole());
		hiic.setSchema(info.getHeroid());
		return hiic;
	}

	public static HeroIdInfo convert2Server(HeroIdInfoClient hiic) {
		HeroIdInfo info = new HeroIdInfo();
		if (null != hiic) {
			info.setHero(hiic.getId()).setHeroid(hiic.getHeroId())
					.setTalent(hiic.getTalent()).setType(hiic.getStar())
					.setRole(hiic.getRole());
		}
		return info;
	}

	public static List<HeroIdInfo> convert2ServerList(
			List<HeroIdInfoClient> hiics) {
		List<HeroIdInfo> infos = new ArrayList<HeroIdInfo>();
		if (null != hiics) {
			for (HeroIdInfoClient hiic : hiics) {
				infos.add(convert2Server(hiic));
			}
		}
		return infos;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (o instanceof HeroIdInfoClient) {
			return id == ((HeroIdInfoClient) o).getId();
		}
		return false;
	}
}
