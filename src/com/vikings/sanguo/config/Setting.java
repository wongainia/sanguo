package com.vikings.sanguo.config;

import java.io.File;
import java.util.Date;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;

import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.ui.window.GameSettingWindow;
import com.vikings.sanguo.utils.DateUtil;

public class Setting {
	public static String music;
	public static String sound;
	public static String map;
	public static String notify;
	public static String icon;
	private static long mem;

	public static boolean speedStat = false;

	public static boolean tileStat = false;

	public static int speedStatCount = 20;

	public static boolean mapPullNotice = true;

	// 是否弹出广告窗口
	public static boolean adTip = false;

	// 2G、3G网络是否提示开启关闭地图
	public static boolean mapNetNotice = true;

	public static boolean meetRewards = true;

	public static int updateWishTime = 0;

	public static BriefUserInfoClient userInfo = new BriefUserInfoClient();

	public static boolean lowMem = false;

	public static boolean canRecharge = false; // 是否开放充值功能

	// public static boolean canSms = false; // 是否开放短信充值功能

	public static String noticeBegin;
	public static String noticeEnd;

	static void init() {
		SharedPreferences share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences("com.vikings.sanguo.sharedPreferences",
						Context.MODE_PRIVATE);
		music = share.getString("MUSICSETTING", "OPEN");
		sound = share.getString("SOUNDSETTING", "OPEN");
		map = share.getString("MAPSETTING", "OPEN");
		notify = share.getString(GameSettingWindow.NOTIFYSETTING,
				GameSettingWindow.VALUEOPEN);
		icon = share.getString("ICONSETTING", "OPEN");
		mapPullNotice = share.getBoolean("MAPPULLNOTICE", true);
		mapNetNotice = share.getBoolean("MAPNETNOTICE", true);
		updateWishTime = share.getInt("UPDATEWISHTIME", 0);

		noticeBegin = share.getString(GameSettingWindow.BEGIN, "8:00");
		noticeEnd = share.getString(GameSettingWindow.END, "20:00");

		ActivityManager activityManager = (ActivityManager) Config
				.getController().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		mem = memoryInfo.availMem;
		lowMem = lowMemery();
	}

	public static boolean lowMemery() {
		return mem < 250 * 1024 * 1024;
	}

	public static boolean checkAdd() {
		if (!adTip)
			return false;
		long time = PrefAccess.getAddVer();
		if (!DateUtil.isSameDay(new Date(), new Date(time))) {
			PrefAccess.saveAddVer(System.currentTimeMillis());
			return true;
		} else
			return false;
	}

	public static boolean isMapEnable() {
		return Constants.VALUEOPEN.equals(map);
	}

	public static void saveValue(String key, boolean value) {
		SharedPreferences share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences("com.vikings.sanguo.sharedPreferences",
						Context.MODE_PRIVATE);
		share.edit().putBoolean(key, value).commit();
	}

	public static void updateWishTime() {
		updateWishTime = (int) (Config.serverTime() / 1000);
		SharedPreferences share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences("com.vikings.sanguo.sharedPreferences",
						Context.MODE_PRIVATE);
		share.edit().putInt("UPDATEWISHTIME", updateWishTime).commit();
	}

	public static long getRMId() {
		SharedPreferences share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences("com.vikings.sanguo.sharedPreferences",
						Context.MODE_PRIVATE);
		return share.getLong("RMID", 0L);
	}

	public static void updateRMId(long id) {
		SharedPreferences share = Config
				.getController()
				.getUIContext()
				.getSharedPreferences("com.vikings.sanguo.sharedPreferences",
						Context.MODE_PRIVATE);
		share.edit().putLong("RMID", id).commit();
	}

	public static boolean isDataMemEnough() {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long availCount = sf.getAvailableBlocks();

		if (blockSize * availCount > com.vikings.sanguo.Constants.MEM_LOW_THRESHOLD)
			return true;
		return false;
	}
}
