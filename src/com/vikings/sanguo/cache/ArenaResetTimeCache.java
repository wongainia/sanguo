/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-11 下午5:16:08
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArenaResetReward;
import com.vikings.sanguo.model.ArenaResetTime;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class ArenaResetTimeCache extends ArrayFileCache {
	public static final String NAME = "arena_reward_reset_time.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((ArenaResetTime) obj).getTime();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ArenaResetTime) obj).getTime();
	}

	@Override
	public Object fromString(String line) {
		return ArenaResetTime.fromString(line);
	}

	public ArenaResetReward[] getResetTime() {
		ArenaResetReward[] time = new ArenaResetReward[2];
		time[0] = new ArenaResetReward();
		time[1] = new ArenaResetReward();
		
		Collections.sort(list, new Comparator<ArenaResetTime>() {
			@Override
			public int compare(ArenaResetTime object1, ArenaResetTime object2) {
				return object1.getTime() - object2.getTime();
			}
		});

		int i = 0;
		for (; i < list.size(); i++) {
			long last = ((ArenaResetTime) list.get(i)).getTime() * 1000L;
			long sec = DateUtil.msFromLastSunday();

				if (sec < last) {
					time[0].time = DateUtil.dayBeginOfLastSunday() + last;
					time[0].rank = ((ArenaResetTime) list.get(i)).getRank();
					time[1].time = time[0].time;
					time[1].rank = time[0].rank;
					return time;
				}
				
				if (sec >= last && i + 1 < list.size()
						&& sec <= ((ArenaResetTime) list.get(i + 1)).getTime() * 1000L) {
					time[0].time = DateUtil.dayBeginOfLastSunday() + last;
					time[0].rank = ((ArenaResetTime) list.get(i)).getRank();
					time[1].time = DateUtil.dayBeginOfLastSunday()
							+ ((ArenaResetTime) list.get(i + 1)).getTime()
							* 1000L;
					time[1].rank = ((ArenaResetTime) list.get(i + 1)).getRank();
					return time;
				}
				
				if (i + 1 >= list.size() && sec >= last) {
					time[0].time = DateUtil.dayBeginOfLastSunday() + last;
					time[0].rank = ((ArenaResetTime) list.get(i)).getRank();
					time[1].time = DateUtil.dayBeginOfLastSunday() + 7
							* Constants.DAY * 1000L
							+ ((ArenaResetTime) list.get(0)).getTime() * 1000L;
					time[1].rank = ((ArenaResetTime) list.get(0)).getRank();
				}
		}

		return time;
	}

	public String getTitleDesc() {
		ArenaResetReward[] time = getResetTime();
		StringBuilder buf = new StringBuilder();
		Date now = new Date(Config.serverTime());  //  - 14 * 3600 * 1000L
		Date first = new Date(time[0].time);
		Date second = new Date(time[1].time);

		if (time[0].time > 0 && time[1].time > 0) {
			if ((DateUtil.isSameDay(now, first) && now.before(first)))
				buf.append("今天").append(
						StringUtil.color(DateUtil.time2.format(first),
								R.color.k7_color8));
			else if ((DateUtil.isSameDay(now, second) && now.before(second)))
				buf.append("今天").append(
						StringUtil.color(DateUtil.time2.format(second),
								R.color.k7_color8));
			else
				buf.append(DateUtil.getDayDesc(second)).append(
						StringUtil.color(DateUtil.time2.format(second),
								R.color.k7_color8));

			int rank = 0;
			if (now.before(first))
				rank = time[0].rank;
			else 
				rank = time[1].rank;
			
			buf.append("整，前")
					.append(StringUtil.color(String.valueOf(rank),
							R.color.k7_color8)).append("名玩家的可领功勋自动变满");
		}

		return buf.toString();
	}

	public boolean canGetAward() {
		ArenaResetReward[] time = getResetTime();
		Date now = new Date(Config.serverTime());
		Date first = new Date(time[0].time);
		Date reward = new Date(Account.myLordInfo.getArenaRewardStart() * 1000L);

		return now.after(first) && reward.before(first);  //DateUtil.isSameDay(now, first) && 
	}
}
