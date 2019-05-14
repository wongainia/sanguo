package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.vikings.sanguo.model.EventEntry;

public class EventEntryCache extends FileCache {
	private final static String NAME = "event_entry.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((EventEntry) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return EventEntry.fromString(line);
	}

	public List<EventEntry> getAvailableEvents() {
		List<EventEntry> events = new ArrayList<EventEntry>();
		Set<Entry<Integer, EventEntry>> entrySet = content.entrySet();
		for (Iterator<Entry<Integer, EventEntry>> iter = entrySet.iterator(); iter
				.hasNext();) {
			Entry<Integer, EventEntry> event = iter.next();
			EventEntry val = event.getValue();
			if (available(val))
				events.add(val);
		}

		Collections.sort(events, new Comparator<EventEntry>() {
			@Override
			public int compare(EventEntry lhs, EventEntry rhs) {
				return lhs.getSequence() - rhs.getSequence();
			}
		});

		return events;
	}

	private boolean available(EventEntry entry) {
		if (0 != entry.getRelatedQuestid()) {
			if (null == Account.getQuestInfoById(entry.getRelatedQuestid()))
				return false;
			else
				return true;
		} else if (999 == entry.getId()) {
			return true;
		} else {
			if (CacheMgr.timeTypeConditionCache.isWithinTime(entry
					.getPropTimeId()) || entry.getPropTimeId() == 0)
				return true;
			else
				return false;
		}
	}
}
