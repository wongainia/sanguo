package com.vikings.sanguo.cache;

import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.EquipmentInsertItem;

public class EquipmentInsertItemCache extends FileCache {
	private static final String FILE_NAME = "equipment_insert_item.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		EquipmentInsertItem eii = EquipmentInsertItem.fromString(line);
		return eii;
	}

	public Set<Integer> getAllIds() {
		return content.keySet();
	}

	@Override
	public Object getKey(Object obj) {
		return ((EquipmentInsertItem) obj).getId();
	}

	// 宝石升到下一级相关配置
	public EquipmentInsertItem getNextLevel(int itemId) {
		EquipmentInsertItem eiiNext = null;
		try {
			EquipmentInsertItem eii = (EquipmentInsertItem) get(itemId);
			Set<Entry<Integer, EquipmentInsertItem>> set = content.entrySet();
			for (Entry<Integer, EquipmentInsertItem> entry : set) {
				EquipmentInsertItem temp = entry.getValue();
				if (temp.getType() == eii.getType()
						&& temp.getLevel() == eii.getLevel() + 1)
					eiiNext = temp;
			}
		} catch (GameException e) {
		}
		return eiiNext;
	}

}
