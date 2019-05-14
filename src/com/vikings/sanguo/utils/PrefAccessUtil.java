package com.vikings.sanguo.utils;

import java.util.Date;
import android.content.Context;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.message.Constants;

public class PrefAccessUtil {
	public static boolean notLoginFor(Context c, long time) {
		return getIntevalToLastLoginTime(c) > time;
	}

	public static long getIntevalToLastLoginTime(Context c) {
		return System.currentTimeMillis() + PrefAccess.getTimeOffset(c)
				- PrefAccess.getLoginTime(c);
	}

	public static boolean hasOfflineNotify(Context c) {
		if (!isOfflineNotifyConfigOpen(c))
			return false;

		try {
			Date curDate = DateUtil.time2.parse(DateUtil.time2.format(new Date(
					System.currentTimeMillis())));
			String[] str = PrefAccess.beginAndendTime(c);

			if (str[0].equals(str[1])) {
				return true;
			} else {
				Date beginTime = DateUtil.time2.parse(str[0]);
				Date endTime = DateUtil.time2.parse(str[1]);

				if (beginTime.before(endTime)) {
					if (curDate.after(beginTime) && curDate.before(endTime)) {
						return true;
					} else {
						return false;
					}
				} else {
					Date endPastTime = DateUtil.time2.parse("23:59");
					Date beginPastTime = DateUtil.time2.parse("00:01");
					if (curDate.after(beginTime) && curDate.before(endPastTime)) {
						return true;
					} else if (curDate.after(beginPastTime)
							&& curDate.before(endTime)) {
						return true;
					} else {
						return false;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private static boolean isOfflineNotifyConfigOpen(Context c) {
		return Constants.VALUEOPEN.equals(PrefAccess.getNotice(c));
	}
}
