package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.EventEntry;
import com.vikings.sanguo.ui.alert.EventEntryTip;

public class EventEntryAutoInvoker extends BackgroundInvoker {
	private List<EventEntry> events;

	@Override
	protected void fire() throws GameException {
		events = CacheMgr.eventEntryCache.getAvailableEvents();
	}

	@Override
	protected void onOK() {
		new EventEntryTip(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 5),
				"官方公告", events).show();
	}
}
