/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-16 下午5:35:11
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.TimeTypeCondition;

public class TimeTypeConditionCache extends ArrayFileCache{
	private final static String NAME = "prop_time.csv";
	private Map<Integer, Map<Integer, List<TimeTypeCondition>>> data = new HashMap<Integer, Map<Integer,List<TimeTypeCondition>>>();
	
	@Override
	public synchronized void init() throws GameException {
		super.init();
		parseContent();
	}
	
	private void parseContent() {
		for (Object it : list) {
			TimeTypeCondition ttc = (TimeTypeCondition) it;
			if (data.containsKey(ttc.getId())) {
				Map<Integer, List<TimeTypeCondition>> group = data.get(ttc.getId());
				
				if (group.containsKey(ttc.getGroup())) {
					List<TimeTypeCondition> ls = group.get(ttc.getGroup());
					ls.add(ttc);
				} else {
					List<TimeTypeCondition> ls = new ArrayList<TimeTypeCondition>();
					ls.add(ttc);
					group.put(ttc.getGroup(), ls);
				}
			} else {
				List<TimeTypeCondition> ls = new ArrayList<TimeTypeCondition>();
				ls.add(ttc);
				Map<Integer, List<TimeTypeCondition>> group = new HashMap<Integer, List<TimeTypeCondition>>();
				group.put(ttc.getGroup(), ls);
				data.put(ttc.getId(), group);
			}
		}
	}
	
	@Override
	public Object fromString(String line) {
		return TimeTypeCondition.fromString(line);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((TimeTypeCondition)obj).getId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((TimeTypeCondition)obj).getGroup();
	}
	
	public boolean isWithinTime(int id) {
		Map<Integer, List<TimeTypeCondition>> group = data.get(id);
		if (null == group || group.isEmpty())
			return false;
		
		Iterator<Entry<Integer, List<TimeTypeCondition>>> it = group.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, List<TimeTypeCondition>> entry = it.next();
			List<TimeTypeCondition> ls = entry.getValue();
			
			boolean ok = true;
			
			for (TimeTypeCondition ttc : ls) {
				if (!ttc.isWithinTime()){
					ok = false;
					break;
				}
			}
			
			if(ok)return true;
		}
		
		return false;
	}
	
	public int getCountDownSecond(int id) {
		if (id <= 0)
			return 0;

		int regCountDown = -1;
		int dayCountDown = -1;
		int hourCountDown = -1;
		
		Map<Integer, List<TimeTypeCondition>> group = data.get(id);
		if (null == group || group.isEmpty())
			return -1;
		
		Iterator<Entry<Integer, List<TimeTypeCondition>>> it = group.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, List<TimeTypeCondition>> entry = it.next();
			List<TimeTypeCondition> ls = entry.getValue();
			
			for (TimeTypeCondition ttc : ls) {
				// 9.24，与付斌确认，优先级：注册时间>自然天>小时
				if (4 == ttc.getType() && ttc.isWithinTime()) {
					if (-1 == regCountDown)
						regCountDown = ttc.getCountDownSecond();
					else {
						if (ttc.getCountDownSecond() < regCountDown)
							regCountDown = ttc.getCountDownSecond();
					}
				}
				else if (1 == ttc.getType() && ttc.isWithinTime()) {
					if (-1 == dayCountDown)
						dayCountDown = ttc.getCountDownSecond();
					else {
						if (ttc.getCountDownSecond() < dayCountDown)
							dayCountDown = ttc.getCountDownSecond();
					}
				}
				else if (3 == ttc.getType() && ttc.isWithinTime()) {
					if (-1 == hourCountDown)
						hourCountDown = ttc.getCountDownSecond();
					else {
						if (ttc.getCountDownSecond() < hourCountDown)
							hourCountDown = ttc.getCountDownSecond();
					}
				}
			}
		}
		
		if (-1 != regCountDown)
			return regCountDown;
		else if (-1 != dayCountDown)
			return dayCountDown;
		else if (-1 != hourCountDown)
			return hourCountDown;
		
		return -1;
	}
}
