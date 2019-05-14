package com.vikings.sanguo.cache;

import java.util.List;

import com.vikings.sanguo.model.EquipmentInsertItemEffect;
import com.vikings.sanguo.utils.StringUtil;

public class EquipmentInsertItemEffectCache extends ArrayFileCache {
	private static final String FILE_NAME = "equipment_insert_item_effect.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((EquipmentInsertItemEffect) obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((EquipmentInsertItemEffect) obj).getPropId();
	}

	@Override
	public Object fromString(String line) {
		return EquipmentInsertItemEffect.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public String getEffectDesc(int itemId) {
		List<EquipmentInsertItemEffect> effects = search(itemId);
		StringBuilder buf = new StringBuilder();
		for (EquipmentInsertItemEffect eiie : effects) {
			buf.append(eiie.getDesc()).append(";");
		}
		return buf.toString();
	}
}
