package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.model.EventRewards;

public class EventRewardsCache extends ArrayFileCache {
	public static String FILE_NAME = "event_rewards.csv";
	
	@Override
	public long getSearchKey1(Object obj) {
		return ((EventRewards) obj).getType();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((EventRewards) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return EventRewards.fromString(line);
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}
	
	public List<EventRewards> getAllRewards() {
		List<EventRewards> rs = new ArrayList<EventRewards>();
		for (int i = 0; i < list.size(); i++) 
		{		
			if(list.get(i) instanceof EventRewards)
			{
				EventRewards rewards = (EventRewards) list.get(i);
				rs.add(rewards);
			}
		}
		return rs;
	}	
}
