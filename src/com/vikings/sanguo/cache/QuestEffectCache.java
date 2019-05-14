package com.vikings.sanguo.cache;

import com.vikings.sanguo.model.QuestEffect;

public class QuestEffectCache extends LazyLoadArrayCache {

	private static final String FILE_NAME = "quest_effect.csv";

	@Override
	public Object fromString(String line) {
		QuestEffect questEffect = QuestEffect.fromString(line);
		return questEffect;
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((QuestEffect) obj).getQuestId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return buildKey(((QuestEffect) obj).getTypeId(),
				((QuestEffect) obj).getTypeValue());
	}

}
