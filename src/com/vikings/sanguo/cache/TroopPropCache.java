package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.TroopProp;

public class TroopPropCache extends FileCache {
	private static final String FILE_NAME = "prop_troop.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((TroopProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return TroopProp.fromString(line);
	}

	public ArrayList<String> getImage() {
		Iterator iter = content.entrySet().iterator();
		ArrayList<String> list = new ArrayList<String>(1);
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			TroopProp val = (TroopProp) entry.getValue();
			list.add(val.getImageUp());
		}
		return list;
	}


	public boolean needShowHP(int id) {
		try {
			TroopProp tp = (TroopProp) get(id);
			int threshold = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_COST, 6);
			if (tp.getHp() > threshold)
				return true;
		} catch (GameException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isNearAtkTroop(int id) {
		if (0 == id)
			return false;

		try {
			TroopProp tp = (TroopProp) get(id);
			if (null != tp && 1 == tp.getAtkRange())
				return true;
		} catch (GameException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isArchor(int id) {
		if (0 == id)
			return false;

		try {
			TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(id);
			if (2 == tp.getAtkRange() || 3 == tp.getAtkRange())
				return true;
		} catch (GameException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<TroopProp> getTrainingTroops() {
		Object[] keys = getSortedKey();
		List<TroopProp> list = new ArrayList<TroopProp>();
		for (int i = 0; i < keys.length; i++) {
			try {
				TroopProp prop = (TroopProp) get((Integer) (keys[i]));
				if (prop.canTrain())
					list.add(prop);
			} catch (GameException e) {
			}
		}
		return list;
	}

	
	//获取全部兵种
	public List<TroopProp> getAllTrainingTroops() {
		Object[] keys = getSortedKey();
		List<TroopProp> list = new ArrayList<TroopProp>();
		for (int i = 0; i < keys.length; i++) {
			try {
				TroopProp prop = (TroopProp) get((Integer) (keys[i]));
				list.add(prop);
			} catch (GameException e) {
			}
		}
		return list;
	}

	// 加血系数大于0的兵种
	@SuppressWarnings("unchecked")
	public List<TroopProp> getHpAddTroops() {
		List<TroopProp> list = new ArrayList<TroopProp>();
		Set<Entry<Integer, TroopProp>> set = content.entrySet();
		for (Entry<Integer, TroopProp> entry : set) {
			TroopProp troopProp = entry.getValue();
			if (troopProp.getHpModulus() > 0) {
				list.add(troopProp);
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<TroopProp> getTroopPropByType(byte type) {
		List<TroopProp> list = new ArrayList<TroopProp>();
		Set<Entry<Integer, TroopProp>> set = content.entrySet();
		for (Entry<Integer, TroopProp> entry : set) {
			TroopProp troopProp = entry.getValue();
			if (troopProp.getType() == type) {
				list.add(troopProp);
			}
		}
		return list;
	}
}
