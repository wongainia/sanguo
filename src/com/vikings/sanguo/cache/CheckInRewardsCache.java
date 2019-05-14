/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-23 下午3:06:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.CheckInRewards;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.DateUtil;

public class CheckInRewardsCache extends LazyLoadArrayCache {
	public static String FILE_NAME = "checkin_rewards.csv";

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object fromString(String line) {
		return CheckInRewards.fromString(line);
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((CheckInRewards) obj).getVip();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((CheckInRewards) obj).getDays();
	}

	/**
	 * 取配了的最大天数
	 * 
	 * @return
	 */
	public int maxDay(int curVip) {
		checkLoad();
		if (list.size() == 0)
			return 0;
		List ls = search(curVip);

		int maxDay = 0;
		for (Object it : ls) {
			CheckInRewards cir = (CheckInRewards) it;
			if (cir.getDays() > maxDay)
				maxDay = cir.getDays();
		}
		return maxDay;
		// return ((CheckInRewards) list.get(list.size() - 1)).getDays();
	}

	/**
	 * 得到天数的奖励
	 * 
	 * @param day
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ReturnInfoClient day(int curVip, int day) {
		ReturnInfoClient rs = new ReturnInfoClient();
		List<CheckInRewards> ls = this.search(curVip, day);
		for (CheckInRewards c : ls) {
			rs.addCfg(c.getType(), c.getThingId(), c.getAmount());
			// 只取第一个物品 和付斌商量过
//			break;
		}
		rs.setCheckInRewards(ls.get(0));
		return rs;
	}

	public List search(long key1, long key2) {
		List<CheckInRewards> reward = new ArrayList<CheckInRewards>();
		List list = search(key1);
		for (int i = 0; i < list.size(); i++) {
			if (getSearchKey2(list.get(i)) == key2)
				reward.add((CheckInRewards) list.get(i));
		}
		return reward;
	}

	private boolean isReset() {
		long last = DateUtil.dayBegin(
				new Date(Account.user.getLastCheckinTime() * 1000)).getTime()
				+ 1000 * 60 * 60 * 24 * 2;
		if (Config.serverTime() < last)
			return false;
		else
			return true;
	}

	/**
	 * 判断这天是否已经签到
	 * 
	 * @param day
	 * @return
	 */
	public boolean isChecked(int day) {
		return !isReset() && day <= Account.user.getCheckinCount();
	}

	/**
	 * 判断这天是否可以签到
	 * 
	 * @param day
	 * @return
	 */
	public boolean canCheckNow(int curVip, int day) {
		if (isReset())
			return day == 1;
		else {
			if (day >= maxDay(curVip))
				return true;
			if (day == Account.user.getCheckinCount() + 1)
				return true;
			return false;
		}

	}

}
