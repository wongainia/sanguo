package com.vikings.sanguo.config;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.Key;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.activity.MainActivity;
import com.vikings.sanguo.aop.ControllerHandler;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.controller.GameController;
import com.vikings.sanguo.invoker.AddrInvoker;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.utils.AesUtil;
import com.vikings.sanguo.utils.StringUtil;

public class Config {

	// wap网络类型
	// 电信wap
	public static final String CTWAP = "ctwap";
	// 移动wap
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	// 联通wap
	public static final String UNIWAP = "uniwap";

	private static GameController controller = null;

	private static GameController proxy = null;

	public static int densityDpi;

	public static int screenWidth;

	public static int screenHeight;

	public static int contentHeight;

	public static float scaleRate;

	public static float SCALE_FROM_HIGH;

	private static Properties prop;

	public static long lastUpdateTime;

	public static long timeOffset = 0;

	public static int clientCode;

	public static int verifyCode = 0;

	/** 版本用途 0：公测 1：内测 2：预发布 3:201 */
	public static int useFor;
	// 合作方账号绑定游戏id分配的号码, 用于新手教程结束后发放奖励
	public static int bindChannel;
	// 官网包1是，0否
	public static int mainPak;

	public static String url;
	public static String serverURl;
	public static String dvURL;

	public static String gameIp;
	public static int gamePort;

	public static String resURl;
	public static String configUrl;
	public static String versionUrl;
	public static String noticeUrl;
	public static String imgUrl;
	public static String soundUrl;
	public static String iconUrl;
	public static String guildIconUrl;
	public static String downloadUrl;
	public static String uploadUrl;
	public static String statUrl;
	public static String crashUrl;
	public static String snsUrl;
	public static String rechargeUrl;

	public static String[] sex;
	public static String[] style;
	public static String[] blood;
	public static String[] generation;
	public static String[] age;
	public static String[] province;
	public static String[] marriage;

	// 《三国撸啊撸》游戏标识
	public static String gameId;

	public static Integer[] iconId = { R.drawable.user_icon_1,
			R.drawable.user_icon_2 };

	public static IvParameterSpec iv;

	public static SecretKeySpec configKey;

	public static SecretKeySpec clientKey;

	public static SecretKeySpec aesKey;

	public static Key pubKey;

	public static byte[] check;

	public static int MANOR_GRID_SIZE = 48;

	public static long sessionId;

	// -1失败，1成功
	public static int latestDungeonResult = 0;
	public static int latestBattleResult = 0;
	public static int firstComplement1_4 = 0;
	public static int firstComplement1_4_win = 0;// 第一章第四关 首次赢 =3，第二次赢==4

	public static int serverTimeout;

	public static int CAMPAIGN_ID = 0;// 战役ID 用于记录副本本次战役章节id
	public static boolean CAMPAIGN_IS_FAIL = false;// 记录对应 CAMPAIGN_ID战役的胜利 或者
													// 失败

	// 分服数据
	public static int serverId;
	public static AccountPswInfoClient userClient;// 选择的登录用户
	public static ServerData serverData;

	public static int FIEF_SIZE;
	public static int zoomLevel;

	public static boolean isCheck = true;

	public static void loadProperty(Context c) {
		prop = new Properties();
		try {
			prop.load(c.getResources().openRawResource(R.raw.config));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getZoomLevel() {
		if (Config.screenWidth >= 1080) {
			return zoomLevel + 1;
		}
		return zoomLevel;
	}

	public static void setController(GameController c) {
		DisplayMetrics dm = c.getResources().getDisplayMetrics();
		Config.controller = c;
		InvocationHandler ih = new ControllerHandler(c);
		proxy = (GameController) Proxy.newProxyInstance(c.getClass()
				.getClassLoader(), c.getClass().getInterfaces(), ih);
		loadProperty(c.getUIContext());
		useFor = getIntConfig("useFor");
		bindChannel = getIntConfig("bindChannel");
		mainPak = getIntConfig("mainPak");
		url = getConfig("url");
		serverURl = url + getConfig("serverUrl");
		dvURL = url + getConfig("dvUrl");
		gameId = getConfig("gameId");
		serverTimeout = Config.getIntConfig("serverTimeout");
		sex = c.getResources().getStringArray(R.array.sex);
		province = c.getResources().getStringArray(R.array.province);
		marriage = c.getResources().getStringArray(R.array.marriage);
		style = c.getResources().getStringArray(R.array.style);
		blood = c.getResources().getStringArray(R.array.blood);
		generation = c.getResources().getStringArray(R.array.generation);
		age = c.getResources().getStringArray(R.array.age);
		densityDpi = dm.densityDpi;
		scaleRate = (float) densityDpi / (float) DisplayMetrics.DENSITY_MEDIUM;
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		contentHeight = (int) (screenHeight);
		SCALE_FROM_HIGH = (float) Config.densityDpi
				/ (float) DisplayMetrics.DENSITY_HIGH;

		clientCode = Config.getIntConfig("clientType");
		Setting.init();

		try {
			clientKey = AesUtil.generateKey();
		} catch (Exception e) {
		}

	}

	public static void setServer(ServerData data, AccountPswInfoClient client) {
		serverData = data;
		serverId = data.getServerId();
		userClient = client;
		Account.user = new UserAccountClient(0, "");
		Account.user.setInfo(client);

		// 用server配置的地址更新所有地址
		gameIp = data.getServerUrl();
		gamePort = data.getPort();
		resURl = data.getResourceUrl();
		versionUrl = resURl + getConfig("versionUrl");
		noticeUrl = resURl + getConfig("noticeUrl");
		imgUrl = resURl + getConfig("imgUrl");
		soundUrl = resURl + getConfig("soundUrl");
		iconUrl = resURl + getConfig("iconUrl");
		guildIconUrl = resURl + getConfig("guildIconUrl");
		downloadUrl = getConfig("downloadUrl");
		uploadUrl = resURl + getConfig("uploadUrl");
		statUrl = resURl + getConfig("statUrl");

		snsUrl = data.getSnsUrl();
		crashUrl = snsUrl + "/" + getConfig("crashUrl");

		rechargeUrl = data.getPayUrl();
		FIEF_SIZE = data.getFiefSize();
		zoomLevel = data.getZoomLevel();
		isCheck = data.isCheck();
		// 提前解压配置文件, 在CacheMgr.init之前需要取dict中的官网地址等
		unzipConfig();
		CacheMgr.preInit();
		new AddrInvoker().start();
	}

	public static UserAccountClient getAccountClient() {
		UserAccountClient uac = new UserAccountClient(0, "");
		uac.setInfo(userClient);
		return uac;
	}

	/**
	 * 检查是否允许模拟地点
	 * 
	 * @return
	 */
	public static boolean allowMockLocation() {
		try {
			int v = Settings.Secure.getInt(controller.getMainActivity()
					.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
			if (v > 0)
				return true;
			else
				return false;
		} catch (SettingNotFoundException e) {
		}
		return false;
	}

	/**
	 * 判断是否移动账户
	 * 
	 * @return
	 */
	public static boolean isCMCC() {
		String imsi = getImsi();
		if (imsi.length() < 5)
			return false;
		if (!imsi.startsWith("460"))
			return false;
		String code = imsi.substring(3, 5);
		if ("00".equals(code) || "02".equals(code) || "07".equals(code))
			return true;
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			if ("GPRS".equalsIgnoreCase(info.getSubtypeName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否电信账户
	 * 
	 * @return
	 */
	public static boolean isTelecom() {
		try {
			String imsi = getImsi();
			if (imsi.length() < 5)
				return false;
			if (!imsi.startsWith("460"))
				return false;
			String code = imsi.substring(3, 5);
			if ("03".equals(code) || "05".equals(code))
				return true;
			ConnectivityManager cm = (ConnectivityManager) controller
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null) {
				if (info.getSubtypeName().startsWith("CDMA")) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断是否联通账户
	 * 
	 * @return
	 */
	public static boolean isCUCC() {
		try {
			String imsi = getImsi();
			if (imsi.length() < 5)
				return false;
			if (!imsi.startsWith("460"))
				return false;
			String code = imsi.substring(3, 5);
			if ("01".equals(code) || "06".equals(code))
				return true;
			ConnectivityManager cm = (ConnectivityManager) controller
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null) {
				if ("UMTS".equalsIgnoreCase(info.getSubtypeName())
						|| "HSDPA".equalsIgnoreCase(info.getSubtypeName())) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static String getImsi() {
		TelephonyManager mTelephonyMgr = (TelephonyManager) controller
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = null;
		try {
			imsi = mTelephonyMgr.getSubscriberId();
		} catch (Exception e) {
		}
		if (StringUtil.isNull(imsi))
			imsi = "";
		return imsi;
	}

	public static String getImei() {
		TelephonyManager mTelephonyMgr = (TelephonyManager) controller
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			return mTelephonyMgr.getDeviceId();
		} catch (Exception e) {
			return "";
		}
	}

	public static String checkNetwork() {
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected())
			return info.getTypeName();
		else
			return null;
	}

	public static boolean isWap() {
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			String extraInfo = info.getExtraInfo();
			if (null != extraInfo
					&& (CMWAP.equalsIgnoreCase(extraInfo)
							|| CTWAP.equalsIgnoreCase(extraInfo)
							|| UNIWAP.equalsIgnoreCase(extraInfo) || WAP_3G
								.equalsIgnoreCase(extraInfo))) {
				return true;
			}
		}
		return false;
	}

	public static String getNetworkInfo() {
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if ("WIFI".equals(info.getTypeName())) {
				return info.getTypeName();
			} else {
				return info.getSubtypeName();
			}
		} else
			return null;
	}

	/**
	 * 是否是2G网络
	 * 
	 * @return
	 */
	public static boolean is2GNet() {
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (!"WIFI".equals(info.getTypeName())
					&& ("CDMA - 1xRTT".equalsIgnoreCase(info.getSubtypeName())
							|| "EDGE".equalsIgnoreCase(info.getSubtypeName()) || "GPRS"
								.equalsIgnoreCase(info.getSubtypeName()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是WIFI
	 * 
	 * @return
	 */
	public static boolean isWifi() {
		ConnectivityManager cm = (ConnectivityManager) controller
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if ("WIFI".equals(info.getTypeName())) {
				return true;
			}
		}
		return false;
	}

	public static GameController getController() {
		return proxy;
	}

	public static String getConfig(String name) {
		return prop.getProperty(name);
	}

	public static int getIntConfig(String name) {
		return Integer.valueOf(prop.getProperty(name));
	}

	public static int indexOfImg(int resId) {
		for (int i = 0; i < iconId.length; i++) {
			if (iconId[i] == resId)
				return i;
		}
		return -1;
	}

	public static int indexOf(String value, String[] arrays) {
		for (int i = 0; i < arrays.length; i++) {
			if (arrays[i].equals(value))
				return i + 1;
		}
		return 0;
	}

	private static int fix(int index) {
		return index - 1;
	}

	public static String arrayValue(int i, String[] array) {
		i = fix(i);
		if (i < 0 || i >= array.length)
			return "";
		return array[i];
	}

	public static String cityValue(int c, int p) {
		if (p == 0)
			return "";
		String[] citys = getCitys(p);
		return arrayValue(c, citys);
	}

	public static String[] getCitys(int index) {
		if (index == 0)
			return null;
		try {
			return controller.getResources().getStringArray(
					R.array.city1 + index - 1);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object getGameUI(String name) {
		try {
			Field field = MainActivity.class.getDeclaredField(name);
			field.setAccessible(true);
			return field.get(controller);
		} catch (Exception e) {
			return null;
		}
	}

	// public static Object invokeMethod(String name) {
	// try {
	// Method m = MainActivity.class.getDeclaredMethod(name);
	// return m.invoke(controller);
	// } catch (Exception e) {
	// throw new RuntimeException("invokeMethod fail!", e);
	// }
	// }

	static private void unzipcfg(InputStream in) throws IOException {
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry entry = null;

		ByteArrayOutputStream vcBuf = new ByteArrayOutputStream();

		while ((entry = zin.getNextEntry()) != null) {
			// 从raw解压配置时 默认去掉指定一定要更新的配置， 例如圣都配置
			if (Mapping.isDefaultUpdate(entry.getName()))
				continue;

			BufferedOutputStream bos;

			if (entry.getName().equals("vc")) {
				bos = new BufferedOutputStream(vcBuf);
			} else
				bos = new BufferedOutputStream(controller.getUIContext()
						.openFileOutput(serverId + "_" + entry.getName(),
								Context.MODE_PRIVATE));
			int count;
			byte data[] = new byte[4096];
			while ((count = zin.read(data)) != -1) {
				bos.write(data, 0, count);
			}
			bos.close();
			zin.closeEntry();
			entry = null;
		}
		zin.close();

		// 解压完毕后保存vc
		FileOutputStream out = controller.getUIContext().openFileOutput(
				serverId + "_" + "vc", Context.MODE_PRIVATE);
		out.write(vcBuf.toByteArray());
		out.close();
	}

	static public void unzipConfig() {
		List<String> ver = controller.getFileAccess().readLocal(
				serverId + "_vc");
		if (ver == null || ver.size() == 0) {
			try {
				unzipcfg(controller.getUIContext().getResources()
						.openRawResource(R.raw.data));
			} catch (IOException e) {
				Log.e("TAG", e.getMessage(), e);
			}
		}
	}

	public static long serverTime() {
		return System.currentTimeMillis() + timeOffset;
	}

	public static int serverTimeSS() {
		return (int) ((System.currentTimeMillis() + timeOffset) / 1000);
	}

	public static int getClientVer() {
		int clientVer = 0;
		try {
			clientVer = Config
					.getController()
					.getMainActivity()
					.getPackageManager()
					.getPackageInfo(
							Config.getController().getMainActivity()
									.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return clientVer;
	}

	public static String getGameId() {
		return gameId;
	}

	public static boolean isSmallScreen() {
		return densityDpi < 160;
	}

	public static int getChannel() {
		return getIntConfig("channle");
	}

	/**
	 * 判断是否mm商城的包
	 * 
	 * @return
	 */
	public static boolean isMMPak() {
		return getChannel() == 10385;
	}

	/**
	 * 判断是否是合作方
	 * 
	 * @return
	 */
	public static boolean isMainPak() {
		return mainPak == 1;
	}
}
