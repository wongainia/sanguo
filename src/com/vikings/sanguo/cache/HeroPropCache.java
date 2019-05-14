package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.HeroProp;

public class HeroPropCache extends FileCache {

	private static final String FILE_NAME = "prop_hero.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((HeroProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return HeroProp.fromString(line);
	}

	public List<HeroProp> getAll() {
		List<HeroProp> list = new ArrayList<HeroProp>();
		list.addAll(content.values());
		return list;
	}

	public String getHeroNameByHeroId(int heroId) {
		try {
			HeroProp hpHeroProp = (HeroProp) CacheMgr.heroPropCache.get(heroId);
			return hpHeroProp.getName();
		} catch (GameException e) {
			Log.e("HeroProp", "heroId:" + heroId + "not found!    " + e);
		}
		return "";
	}

}
