package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;

public class EquipmentCache {

	private HashMap<Byte, List<EquipmentInfoClient>> map = new HashMap<Byte, List<EquipmentInfoClient>>();

	// 装备仓库容积
	public static int getCapability() {
		return CacheMgr.dictCache.getDictInt(Dict.TYPE_TIP, 4);// 装备上限100
	}

	public void add(EquipmentInfoClient eic) {
		byte type = eic.getProp().getType();
		get(type).add(eic);
	}

	public void update(EquipmentInfoClient eic) {
		byte type = eic.getProp().getType();
		List<EquipmentInfoClient> list = get(type);
		for (EquipmentInfoClient equipmentInfoClient : list) {
			if (equipmentInfoClient.getId() == eic.getId()) {
				equipmentInfoClient.update(eic);
				return;
			}
		}
		list.add(eic);
	}

	public List<EquipmentInfoClient> getWeapon() {// 武器
		return get(PropEquipment.TYPE_WEAPON);
	}

	public List<EquipmentInfoClient> getAccessories() {// 饰品
		return get(PropEquipment.TYPE_ACCESSORIES);
	}

	public List<EquipmentInfoClient> getClothes() {// 战甲
		return get(PropEquipment.TYPE_CLOTHES);
	}

	public List<EquipmentInfoClient> getArmor() {// 防具
		return get(PropEquipment.TYPE_ARMOR);
	}

	public List<EquipmentInfoClient> get(byte type) {
		if (!map.containsKey(type))
			map.put(type, new ArrayList<EquipmentInfoClient>());
		return map.get(type);
	}

	// 返回全部的装备
	public List<EquipmentInfoClient> getAll() {
		List<EquipmentInfoClient> equipments = new ArrayList<EquipmentInfoClient>();
		Iterator<Entry<Byte, List<EquipmentInfoClient>>> it = map.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Byte, List<EquipmentInfoClient>> entry = it.next();
			equipments.addAll(entry.getValue());// 返回与此项对应的值
		}
		return equipments;
	}

	public boolean remove(EquipmentInfoClient eic) {
		if (null == eic)
			return false;
		return get(eic.getProp().getType()).remove(eic);
	}

	public boolean remove(long id) {
		if (id > 0) {
			Set<Entry<Byte, List<EquipmentInfoClient>>> set = map.entrySet();
			for (Entry<Byte, List<EquipmentInfoClient>> entry : set) {
				List<EquipmentInfoClient> list = entry.getValue();
				for (Iterator<EquipmentInfoClient> iter = list.iterator(); iter
						.hasNext();) {
					EquipmentInfoClient eic = iter.next();
					if (eic.getId() == id) {
						iter.remove();
						return true;
					}
				}
			}
		}

		return false;
	}

	public void synDiff() throws GameException {
		SyncDataSet dataSet = GameBiz.getInstance().refreshEquipmentInfo();
		if (null != dataSet.equipmentInfos)
			merge(dataSet.equipmentInfos);
	}

	public void merge(SyncData<EquipmentInfoClient>[] equipInfos) {
		for (int i = 0; i < equipInfos.length; i++) {
			SyncData<EquipmentInfoClient> data = equipInfos[i];
			switch (data.getCtrl().getOp()) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				update(data.getData());
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				remove(data.getData());
				break;
			default:
				break;
			}
		}
	}
}
