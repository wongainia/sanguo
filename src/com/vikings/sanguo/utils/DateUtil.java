package com.vikings.sanguo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.config.Config;

public class DateUtil {

	public final static DateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMdd", Locale.CHINA);

	public final static DateFormat date1 = new SimpleDateFormat("yyyy/MM/dd",
			Locale.CHINA);

	public final static DateFormat time = new SimpleDateFormat("HH:mm:ss",
			Locale.CHINA);

	public final static DateFormat time1 = new SimpleDateFormat("HHmmss",
			Locale.CHINA);

	public final static DateFormat time2 = new SimpleDateFormat("HH:mm",
			Locale.CHINA); //

	public final static DateFormat sdateFormat = new SimpleDateFormat("yyMMdd",
			Locale.CHINA);

	public final static DateFormat sdateFormat2 = new SimpleDateFormat(
			"yy-MM-dd", Locale.CHINA);

	public final static DateFormat yearFormat = new SimpleDateFormat("yyyy",
			Locale.CHINA);

	public final static DateFormat timeStampFormat = new SimpleDateFormat(
			"yyyyMMdd HHmmss", Locale.CHINA);

	public final static DateFormat timeStampFormatTrade = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.CHINA);

	public final static DateFormat detailFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss", Locale.CHINA);

	public final static DateFormat reportTImeFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss", Locale.CHINA);

	public final static DateFormat db2TimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	public final static DateFormat db2DateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);

	public final static DateFormat db2MinuteFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.CHINA);

	public final static DateFormat db2MinuteFormat2 = new SimpleDateFormat(
			"yy-MM-dd HH:mm", Locale.CHINA);

	public final static DateFormat db2MinuteFormat3 = new SimpleDateFormat(
			"yy/MM/dd HH:mm", Locale.CHINA);

	public final static DateFormat db2MinuteFormat4 = new SimpleDateFormat(
			"HH:mm:ss", Locale.CHINA);

	public final static DateFormat cfgFormat = new SimpleDateFormat("yyyy/M/d",
			Locale.CHINA);

	public static Date monthBegin(Date date) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}

	public static Date yearBegin(Date date) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date);
		c1.set(Calendar.MONTH, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}

	public static Date dayBegin(Date date) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}

	public static long dayBeginOfLastSunday() {
		Date date = new Date(Config.serverTime());
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date);
		int day = c1.get(Calendar.DAY_OF_WEEK);
		;
		Date sunday = new Date(Config.serverTime() - (day - Calendar.SUNDAY)
				* Constants.DAY * 1000L);
		sunday = dayBegin(sunday);
		return sunday.getTime();
	}

	public static long msFromLastSunday() {
		return Config.serverTime() - dayBeginOfLastSunday(); // - 14 * 3600 *
																// 1000L
	}

	public static long msFromNextSunday() {
		return 7 * Constants.DAY * 1000L - msFromLastSunday();
	}

	/**
	 * 解析yyyyMMdd格式的日期
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static Date getTime(String yyyyMMdd) {
		try {
			return dateFormat.parse(yyyyMMdd);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 解析yyyy/MM/dd格式的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(String date) {
		try {
			return date1.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static int getAge(String date) {
		Date birthDay = getDate(date);
		if (birthDay == null) {
			return 18;
		}
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTimeInMillis(Config.serverTime());
		int now = c1.get(Calendar.YEAR);
		c1.setTime(birthDay);
		return now - c1.get(Calendar.YEAR);
	}

	public static Date julianDay(int julianDate) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.set(Calendar.DAY_OF_YEAR, julianDate);
		return c1.getTime();
	}

	public static Date getTomorrowDate(Date date) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date);
		c1.add(Calendar.DATE, 1);
		return c1.getTime();
	}

	public static Date nextDay(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.DATE, 1);
		return c1.getTime();
	}

	public static Date prevDay(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.DATE, -1);
		return c1.getTime();
	}

	public static Date prevMonth(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.MONTH, -1);
		return c1.getTime();
	}

	public static Date nextMonth(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.MONTH, 1);
		return c1.getTime();
	}

	public static Date prevYear(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.YEAR, -1);
		return c1.getTime();
	}

	public static Date nextYear(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		c1.add(Calendar.YEAR, 1);
		return c1.getTime();
	}

	public static boolean isSameDay(Date d1, Date d2) {
		return dateFormat.format(d1).equals(dateFormat.format(d2));
	}

	public static boolean isMonthEnd(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		int month = c1.get(Calendar.MONTH);
		c1.add(Calendar.DAY_OF_MONTH, 1);
		int month2 = c1.get(Calendar.MONTH);
		if (month == month2)
			return false;
		else
			return true;
	}

	public static boolean isYearEnd(Date d1) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(d1);
		int year = c1.get(Calendar.YEAR);
		c1.add(Calendar.DATE, 1);
		int year2 = c1.get(Calendar.YEAR);
		if (year == year2)
			return false;
		else
			return true;
	}

	public static boolean isAfter(Date start, Date end, int calendarField,
			int amount) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(start);
		c1.add(calendarField, amount);
		Calendar c2 = Calendar.getInstance(Locale.CHINA);
		c2.setTime(end);
		return !c2.before(c1);
	}

	public static String getDate(int ss) {
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTimeInMillis(ss * 1000L);
		return c1.get(Calendar.YEAR) + "年" + (c1.get(Calendar.MONTH) + 1) + "月"
				+ c1.get(Calendar.DAY_OF_MONTH) + "日";
	}

	public static String formatSecond(int ss) { // 提取中文 复数形式
		long days = ss / (60 * 60 * 24);
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		long seconds = ss % 60;
		StringBuilder buf = new StringBuilder();
		if (days != 0)
			buf.append(days).append("天");
		if (hours != 0)
			buf.append(hours).append("小时");
		if (minutes != 0)
			buf.append(minutes).append("分钟");
		if (seconds != 0)
			buf.append(seconds).append("秒");
		return buf.toString();
	}

	public static String formatMinute(int ss) {
		if (ss == 0)
			return "0分";
		if (ss > 0 && ss < 60)
			return "1分";
		long days = ss / (60 * 60 * 24);
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		StringBuilder buf = new StringBuilder();
		if (days != 0)
			buf.append(days).append("天");
		if (hours != 0)
			buf.append(hours).append("小时");
		if (minutes != 0)
			buf.append(minutes).append("分钟");
		return buf.toString();
	}

	public static String formatTime(int ss) {
		long days = ss / (60 * 60 * 24);
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		int seconds = ss % 60;
		StringBuilder buf = new StringBuilder();
		if (days != 0)
			buf.append(days).append("天");
		if (hours != 0)
			buf.append(hours).append("小时");
		if (minutes != 0)
			buf.append(minutes).append("分钟");
		if (seconds != 0)
			buf.append(seconds).append("秒");
		return buf.toString();
	}

	public static String formatDownCountTime(int ss) {
		long hours = ss / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		int seconds = ss % 60;
		StringBuilder buf = new StringBuilder();
		if (hours != 0)
			buf.append(hours).append(":");
		if (minutes != 0)
			buf.append(minutes > 9 ? minutes : "0" + minutes).append(":");
		else
			buf.append("00:");
		if (seconds != 0)
			buf.append(seconds > 9 ? seconds : "0" + seconds);
		else
			buf.append("00");
		return buf.toString();
	}

	public static String _formatTime(int ss) {
		long days = ss / (60 * 60 * 24);
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		int seconds = ss % 60;
		StringBuilder buf = new StringBuilder();
		if (days != 0)
			buf.append(days).append("天");
		if (hours != 0)
			buf.append(hours).append("小时");
		if (minutes != 0) {
			if (days == 0)
				buf.append(minutes).append("分钟");
		}
		if (seconds != 0) {
			if (hours == 0)
				buf.append(seconds).append("秒");
		}

		return buf.toString();
	}

	public static String _formatHHss(int ss) {
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		long minutes = (ss % (60 * 60)) / (60);
		StringBuilder buf = new StringBuilder();
		if (hours != 0)
			buf.append(hours).append(":");
		else
			buf.append("00:");

		if (minutes != 0)
			buf.append(minutes);
		else
			buf.append("00:");
		return buf.toString();
	}

	public static String formatHour(int second) {
		if (second <= 0)
			return 1 + "秒";

		int d = second / (60 * 60 * 24);
		if (d > 0)
			return d + "天";

		second = second % (60 * 60 * 24);
		int h = second / (60 * 60);
		if (h > 0)
			return h + "小时";

		second = second % (60 * 60);
		int m = second / 60;
		if (m > 0)
			return m + "分钟";

		return second + "秒";
	}

	public static String formatHourDesc(int hour) {
		int i = hour / 24;
		if (i > 0)
			return i + "天" + (hour % 24) + "小时";
		else
			return hour + "小时";
	}

	public static String getDateDesc(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH)
				+ "日" + c.get(Calendar.HOUR_OF_DAY) + "时";
	}

	public static String getDayDesc(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH) + "日";
	}

	public static String getMonthDay(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH)
				+ "日";
	}

	public static String formatBefore(int before) {
		long ss = (Config.serverTime() / 1000 - before);
		if (ss <= 0)
			return 1 + "秒";
		long month = ss / (60 * 60 * 24 * 30);
		long days = ss / (60 * 60 * 24);
		if (month != 0)
			return month + "月";
		if (days != 0)
			return days + "天";
		long hours = (ss % (60 * 60 * 24)) / (60 * 60);
		if (hours != 0)
			return hours + "小时";
		long minutes = (ss % (60 * 60)) / (60);
		if (minutes != 0)
			return minutes + "分钟";
		long seconds = ss % 60;
		return seconds + "秒";
	}

	public static int yearBetween(Date begin, Date end) {
		Calendar ca = Calendar.getInstance(Locale.CHINA);
		ca.setTime(begin);
		int i = ca.get(Calendar.YEAR);
		ca.setTime(end);
		int j = ca.get(Calendar.YEAR);
		return j - i;
	}

	/**
	 * 两个时间点中间的天数（不包括结束当天）
	 * 
	 * @param from
	 *            开始时间
	 * @param to
	 *            结束时间
	 * @return
	 */
	public static int dayBetween(int from, int to) {
		return (to - from) / (3600 * 24);
	}

	public static int dayBetween(Date from, Date to) {
		return (int) ((to.getTime() - from.getTime()) / (1000 * 3600 * 24));
	}

	public static String formatDuring(Date begin, Date end) {
		return formatSecond((int) end.getTime() / 1000 - (int) begin.getTime()
				/ 1000);
	}

	public static String formatHourDuring(Date begin, Date end) {
		return formatHour((int) (end.getTime() / 1000 - begin.getTime() / 1000));
	}

	public static boolean yearAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.YEAR, 1);
	}

	public static boolean halfYearAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.MONTH, 6);
	}

	public static boolean quarterAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.MONTH, 3);
	}

	public static boolean monthAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.MONTH, 1);
	}

	public static boolean week2After(Date start, Date end) {
		return isAfter(start, end, Calendar.WEEK_OF_YEAR, 2);
	}

	public static boolean weekAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.WEEK_OF_YEAR, 1);
	}

	public static boolean dayAfter(Date start, Date end) {
		return isAfter(start, end, Calendar.DATE, 1);
	}

	public static int getTimeSS() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static String adjustTimeString(String time) {
		if (time.endsWith("分"))
			return time + "钟";
		return time;
	}

	public static int getTodaySS() {
		Date d = new Date(Config.serverTime());
		return (int) ((d.getTime() - dayBegin(d).getTime()) / 1000);
	}

	public static boolean isToday(long time) {
		return dateCompare(new Date(time), new Date(Config.serverTime())) == 0;
	}

	public static boolean isYesterday(long time) {
		return dateCompare(new Date(time), new Date(Config.serverTime())) == -1;
	}

	private static int dateCompare(Date date, Date current) {
		try {
			String currentStr = dateFormat.format(current);
			Date zeroDate = dateFormat.parse(currentStr); // 今天0：00
			if (zeroDate.getTime() - date.getTime() > 0
					&& zeroDate.getTime() - date.getTime() <= 86400000) // 昨天
				return -1;
			else if (zeroDate.getTime() - date.getTime() <= 0
					&& zeroDate.getTime() - date.getTime() > -86400000) // 今天
				return 0;
			else
				return 1;
		} catch (ParseException e) {
			Log.e("DateUtil", e.getMessage());
			return 1;
		}
	}

	public static String formatDate(long time, DateFormat format) {
		Date date = new Date(time);
		return format.format(date);
	}

	public static String formatHourOrMinute(int second) {
		if (second < 60 * 60)
			return formatMinute(second);
		else
			return formatHour(second);
	}

	public static String formatMinuteOrSecond(int second) {
		if (second < 60 * 60)
			return formatTime(second);
		else
			return formatMinute(second);
	}

	public static Calendar getNow() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTimeInMillis(Config.serverTime());
		return c;
	}

	public static Date getTime2Now() {
		try {
			return time.parse(time.format(new Date(Config.serverTime()))); // System.currentTimeMillis()
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
}
