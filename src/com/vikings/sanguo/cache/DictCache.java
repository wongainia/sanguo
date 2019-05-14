package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.StringUtil;

public class DictCache extends FileCache {

	private static final String FILE_NAME = "dict.csv";

	private static final int mul = 10000;

	@Override
	public Object fromString(String line) {
		return Dict.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		Dict d = (Dict) obj;
		return d.getType() * mul + d.getValue();
	}

	@SuppressWarnings("unchecked")
	public List<String> getDescByMainKey(int type) {
		List<String> list = new ArrayList<String>();
		for (Iterator<Entry<Integer, Dict>> iter = content.entrySet()
				.iterator(); iter.hasNext();) {
			Entry<Integer, Dict> entry = iter.next();
			if (entry.getKey() / mul == type) {
				list.add(entry.getValue().getDesc());
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Dict> getDictByMainKey(int type) {
		List<Dict> list = new ArrayList<Dict>();
		for (Iterator<Entry<Integer, Dict>> iter = content.entrySet()
				.iterator(); iter.hasNext();) {
			Entry<Integer, Dict> entry = iter.next();
			if (entry.getKey() / mul == type) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public String getDict(int type, int value) {
		int key = type * mul + value;
		if (content.containsKey(key))
			return ((Dict) content.get(key)).getDesc();
		return "";
	}

	public int getDictInt(int type, int value) {
		String v = getDict(type, value);
		if (StringUtil.isNull(v))
			return 0;
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return 0;
		}
	}

	public String[] getDict(int type, int[] value) {
		String[] str = new String[value.length];
		for (int i = 0; i < value.length; i++)
			str[i] = ((Dict) content.get(type * mul + value[i])).getDesc();
		return str;
	}

	public int getUserExpBonusRate() {
		int userExpTimeId = getDictInt(Dict.TYPE_USER_EXP_BONUS, 1);
		boolean hasUserExpBonus = CacheMgr.timeTypeConditionCache
				.isWithinTime(userExpTimeId);
		int rate = CacheMgr.dictCache.getDictInt(Dict.TYPE_USER_EXP_BONUS, 2);
		if (hasUserExpBonus && rate != 100)
			return rate;
		return 100;
	}

	public int getHeroExpBonusRate() {
		int heroExpTimeId = CacheMgr.dictCache.getDictInt(
				Dict.TYPE_HERO_EXP_BONUS, 1);
		boolean hasHeroExpBonus = CacheMgr.timeTypeConditionCache
				.isWithinTime(heroExpTimeId);
		int rate = CacheMgr.dictCache.getDictInt(Dict.TYPE_HERO_EXP_BONUS, 2);
		if (hasHeroExpBonus && rate != 100)
			return rate;
		return 100;
	}
}
