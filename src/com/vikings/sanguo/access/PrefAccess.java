package com.vikings.sanguo.access;

import android.content.Context;
import android.content.SharedPreferences;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.ui.window.GameSettingWindow;

public class PrefAccess {

	private static final String tag = "com.vikings.sanguo.sharedPreferences";

	private static final String farm = "com.vikings.sanguo.farms";

	private static final String location = "com.vikings.sanguo.location";

	private static final String ADD_TIME = "ADD_TIME";

	private static final String NOTICE_VER = "NOTICE_VER";

	private static final String DOWNLOAD_PERCENT = "DOWNLOAD_PERCENT";

	private static final String DOWNLOAD_CONFIG = "DOWNLOAD_CONFIG";

	private static final String MAXIDREALLOG = "MAXIDREALLOG";

	private static final String TIMEOFFSET = "TIMEOFFSET";

	private static final String LAST_TILEID = "LAST_TILEID";

	private static final String FIEF_COUNT = "FIEF_COUNT";

	private static final String SECOND_CONFIRM_COUNT = "SECOND_CONFIRM_COUNT";

	private static final String LAST_SHOW_NOTIC_TIME = "LAST_SHOW_NOTIC_TIME";

	private static final String SHORT_CUT_ICON = "SHORT_CUT_ICON";

	private static final String EQUIP_FORGE_CURRENCY = "EQUIP_FORCE_CURRENCY";

	private static final String BLOOD_POKER_ANIM = "BLOOD_POKER_ANIMATION";

	private static final String BLOOD_LOG_ID = "BLOOD_LOG_ID";

	public static UserAccountClient getUser() {
		AccountPswInfoClient client = Config.getController().getFileAccess()
				.getLastUser();
		if (null != client) {
			return new UserAccountClient(client.getUserid(), client.getPsw());
		} else {
			return new UserAccountClient(0, "");
		}
	}

	public static long getBloodLogId() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		return share.getLong(BLOOD_LOG_ID, 0);
	}

	public static void setBloodLogId(long logId) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		share.edit().putLong(BLOOD_LOG_ID, logId).commit();
	}

	/**
	 * 是否播放过开始动画
	 * 
	 * @return true:播放过，false：未播放过
	 */
	public static boolean getBloodPokerAnim() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		return share.getBoolean(BLOOD_POKER_ANIM, false);
	}

	public static void setBloodPokerAnim(boolean anim) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		share.edit().putBoolean(BLOOD_POKER_ANIM, anim).commit();
	}

	public static boolean getEquipForgeCurrency() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		return share.getBoolean(EQUIP_FORGE_CURRENCY, true);
	}

	public static void setEquipForgeCurrency(boolean currency) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		share.edit().putBoolean(EQUIP_FORGE_CURRENCY, currency).commit();
	}

	public static boolean hasShortCutIcon() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getBoolean(SHORT_CUT_ICON, false);
	}

	public static void addShortCutIcon() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putBoolean(SHORT_CUT_ICON, true).commit();
	}

	public static int getLastFiefCount() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getInt(FIEF_COUNT, 0);
	}

	public static void saveLastFiefCount(int count) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putInt(FIEF_COUNT, count).commit();
	}

	public static int getSecondConfirmCount() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getInt(SECOND_CONFIRM_COUNT, 0);
	}

	public static void setSecondConfirmCount(int count) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putInt(SECOND_CONFIRM_COUNT, count).commit();
	}

	public static long getLastTileId() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		return share.getLong(LAST_TILEID, 0);
	}

	public static void saveLastTileId(long tileId) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(location, Context.MODE_PRIVATE);
		share.edit().putLong(LAST_TILEID, tileId).commit();
	}

	public static int getPercent() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getInt(DOWNLOAD_PERCENT, 0);
	}

	public static int[] getConfigPercent() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		String percent = share.getString(DOWNLOAD_CONFIG, "0,0,0");
		String[] tmp = percent.split(",");
		int[] rs = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			rs[i] = Integer.valueOf(tmp[i]);
		}
		return rs;
	}

	public static void saveConfigPercent(int cfgNum, int ver, int cur) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putString(DOWNLOAD_CONFIG, cfgNum + "," + ver + "," + cur)
				.commit();
	}

	public static void savePercent(int percent) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putInt(DOWNLOAD_PERCENT, percent).commit();
	}

	public static long getAddVer() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getLong(ADD_TIME, 0);
	}

	public static void saveAddVer(long time) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putLong(ADD_TIME, time).commit();
	}

	public static int getNotifyVer() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getInt(NOTICE_VER, 0);
	}

	public static void saveNotifyVer(int ver) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putInt(NOTICE_VER, ver).commit();
	}

	public static long getTimeOffset() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getLong(TIMEOFFSET, 0);
	}

	public static long getLastShowNoticeTime() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getLong(LAST_SHOW_NOTIC_TIME, 0);
	}

	public static void saveLastShowNoticeTime(long time) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putLong(LAST_SHOW_NOTIC_TIME, time).commit();
	}

	public static long getMaxIdRealLog() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		return share.getLong(MAXIDREALLOG, 0);
	}

	public static void saveMaxIdRealLog(long maxIdRealLog) {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(tag, Context.MODE_PRIVATE);
		share.edit().putLong(MAXIDREALLOG, maxIdRealLog).commit();
	}

	public static void saveQuit() {
		SharedPreferences share = Config.getController().getUIContext()
				.getSharedPreferences(farm, Context.MODE_PRIVATE);
		share.edit().clear().putLong(TIMEOFFSET, Config.timeOffset)
				.putLong("loginTime", Config.serverTime()).commit();
	}

	public static long getLoginTime(Context c) {
		SharedPreferences share = c.getSharedPreferences(farm,
				Context.MODE_PRIVATE);
		long loginTime = 0;
		if (share != null) {
			loginTime = share.getLong("loginTime", 0);
		}
		if (loginTime == 0) {
			loginTime = System.currentTimeMillis();
		}
		return loginTime;
	}

	public static String getNotice(Context c) {
		SharedPreferences share = c.getSharedPreferences(tag,
				Context.MODE_PRIVATE);
		String notice;
		if (share != null) {
			notice = share.getString(GameSettingWindow.NOTIFYSETTING,
					GameSettingWindow.VALUEOPEN);
			return notice;
		}
		return "";
	}

	public static String[] beginAndendTime(Context c) {
		SharedPreferences share = c.getSharedPreferences(tag,
				Context.MODE_PRIVATE);
		String[] str = new String[2];
		if (share != null) {
			str[0] = share.getString("BEGINTIME", "8:00");
			str[1] = share.getString("ENDTIME", "20:00");
		}
		return str;
	}

	public static long getTimeOffset(Context c) {
		SharedPreferences share = c.getSharedPreferences(farm,
				Context.MODE_PRIVATE);
		if (share != null) {
			return share.getLong("timeOffset", 0);
		}
		return 0;
	}
}
