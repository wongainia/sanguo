package com.vikings.sanguo.invoker;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.PrefAccess;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.cache.FileCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Mapping;
import com.vikings.sanguo.config.Setting;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.ui.alert.SystemNotifyTip;
import com.vikings.sanguo.ui.alert.UpdateBonusTip;
import com.vikings.sanguo.utils.StringUtil;

public class CheckVersion extends BaseInvoker {

	private Home home;

	public static int curVer;

	public static int minVer;

	public static int lastVer;

	private boolean brokenVer = false;

	private boolean halt = false;

	private TreeMap<Integer, Integer> resVer = new TreeMap<Integer, Integer>();

	private TreeMap<Integer, Integer> localVer;

	private int cfgNum;
	private int cfgVer;
	private int cfgCur;

	private ArrayList<Integer> totalUpdate = new ArrayList<Integer>();

	public CheckVersion(Home home) {
		this.home = home;
	}

	@Override
	protected void beforeFire() {
	}

	@Override
	protected String failMsg() {
		return ctr.getString(R.string.CheckVersion_failMsg);
	}

	private int getVerCode() {
		try {
			return Config
					.getController()
					.getMainActivity()
					.getPackageManager()
					.getPackageInfo(
							Config.getController().getMainActivity()
									.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	private TreeMap<Integer, Integer> getCfgVer() {
		TreeMap<Integer, Integer> rs = new TreeMap<Integer, Integer>();
		List<String> ver = Config.getController().getFileAccess()
				.readLocal(Config.serverId + "_vc");
		if (ver == null || ver.size() == 0)
			return rs;
		StringBuilder buf = new StringBuilder(ver.get(0));
		while (buf.length() > 0) {
			rs.put((int) StringUtil.removeCsvLong(buf),
					(int) StringUtil.removeCsvLong(buf));
		}
		return rs;
	}

	private void saveCfgVer() {
		StringBuilder buf = new StringBuilder();
		for (Integer i : localVer.keySet()) {
			buf.append(i).append(",").append(localVer.get(i)).append(",");
		}
		buf.deleteCharAt(buf.length() - 1);
		List<String> ls = new ArrayList<String>();
		ls.add(buf.toString());
		Config.getController().getFileAccess()
				.saveLocal(Config.serverId + "_vc", ls, false);
	}

	// 断点下载
	private void downloadCfg(Integer num, Integer ver) throws Exception {
		Config.getController().getUIContext().deleteFile(num + "");
		int cur = 0;
		if (cfgNum == num && cfgVer == ver) {
			cur = cfgCur;
		}
		// 断点下载重试3次
		int timeout = Config.serverTimeout;
		for (int i = 0; i < 3; i++) {
			RandomAccessFile out = null;
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(
						Config.resURl + num + "?" + System.currentTimeMillis())
						.openConnection();
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("RANGE", "bytes=" + cur + "-");
				InputStream in = conn.getInputStream();
				out = new RandomAccessFile(Config.getController()
						.getFileAccess()
						.readLocalFile(FileCache.getCfgFileName(num)), "rw");
				out.seek(cur);
				byte[] buf = new byte[2048];
				int count;
				while ((count = in.read(buf)) != -1) {
					out.write(buf, 0, count);
					cur += count;
				}
				out.setLength(cur);
				out.close();
				out = null;
				return;
			} catch (Exception e) {
				if (out != null) {
					out.close();
				}
				Log.e("CheckVersion", "update config fail", e);
			}
		}
		// 保存下载进度
		PrefAccess.saveConfigPercent(num, ver, cur);
		throw new GameException("download error");
	}

	private boolean isExist(Integer num) {
		try {
			if (Config.getController().getUIContext()
					.getFileStreamPath(FileCache.getCfgFileName(num)).length() > 0)
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private void checkCfg() throws GameException {
		localVer = getCfgVer();
		int[] cfg = PrefAccess.getConfigPercent();
		cfgNum = cfg[0];
		cfgVer = cfg[1];
		cfgCur = cfg[2];
		try {
			for (Integer num : Mapping.cfgMapping.keySet()) {
				if (Mapping.div.containsKey(num)) {
					for (Integer i : Mapping.div.get(num)) {
						count(i);
					}
				} else
					count(num);
			}
			ctr.postRunnable(new Runnable() {
				@Override
				public void run() {
					new LogInvoker("更新配置" + totalUpdate.size()).start();
				}
			});
			for (int i = 0; i < totalUpdate.size(); i++) {
				notice(i + 1, totalUpdate.size());
				int num = totalUpdate.get(i);
				downloadCfg(num, resVer.get(num));
				localVer.put(num, resVer.get(num));
			}
			ctr.postRunnable(new Runnable() {
				@Override
				public void run() {
					new LogInvoker("完成更新配置" + totalUpdate.size()).start();
				}
			});
		} catch (Exception e) {
			Log.e("CheckVersion", "update config fail", e);
			throw new GameException("无法更新配置，请检查网络，重启游戏");
		} finally {
			// 保存最新的resver
			saveCfgVer();
		}
	}

	private void notice(final int cur, final int total) {
		home.post(new Runnable() {

			@Override
			public void run() {
				home.setLoadingText("正在更新配置.. 预计剩余时间:"
						+ ((total - cur) * 5 / total + 1) + "秒");
				home.setPercent(50 + (cur * 20 / total));
			}
		});
	}

	private void count(int num) {
		// 如果资源服上已经没有这个配置，无视
		if (!resVer.containsKey(num))
			return;
		// 如果本地版本和资源服上不一样 重新下载
		// 或者 本地没有这个配置，重新下载
		if (!localVer.containsKey(num) || localVer.get(num) < resVer.get(num)
				|| !isExist(num)) {
			totalUpdate.add(num);
		}
	}

	/*
	 * 
	 * @see com.vikings.sanguo.invoker.BaseInvoker#fire()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void fire() throws GameException {
		home.setPercentInThread(25);
		curVer = getVerCode();
		try {
			JSONObject obj = HttpConnector.getInstance().getVersion();
			halt = obj.getBoolean("halt");
			if (halt)
				return;

			// 获取是否开放充值
			if (obj.isNull("charge"))
				Setting.canRecharge = true;
			else
				Setting.canRecharge = obj.getBoolean("charge");

			Setting.tileStat = obj.optBoolean("tile");

			JSONArray a = obj.getJSONArray("client");
			for (int i = 0; i < a.length(); i++) {
				if (a.getJSONObject(i).getInt("c") == Config.clientCode) {
					minVer = a.getJSONObject(i).getInt("min");
					lastVer = a.getJSONObject(i).getInt("cur");
					break;
				}
			}

			if (!obj.isNull("broken")) {
				a = obj.getJSONArray("broken");
				for (int i = 0; i < a.length(); i++) {
					if (a.getInt(i) == curVer) {
						brokenVer = true;
						break;
					}
				}
			}
			if (curVer < minVer)
				return;

			Setting.speedStat = obj.getJSONObject("stat").getBoolean("on");
			Setting.speedStatCount = obj.getJSONObject("stat").getInt("num");

			Setting.adTip = obj.getBoolean("ad");
			a = obj.getJSONArray("vc");
			for (int i = 0; i < a.length(); i = i + 2) {
				resVer.put(a.getInt(i), a.getInt(i + 1));
			}

			// 获取配置拆分设定
			if (obj.has("div")) {
				JSONObject div = obj.getJSONObject("div");
				for (Iterator it = div.keys(); it.hasNext();) {
					String num = (String) it.next();
					JSONArray aa = div.getJSONArray(num);
					Integer[] tt = new Integer[aa.length()];
					for (int i = 0; i < tt.length; i++) {
						tt[i] = aa.getInt(i);
					}
					Mapping.div.put(Integer.valueOf(num), tt);
				}
			}
			home.setPercentInThread(50);
			checkCfg();
			PrefAccess.saveConfigPercent(0, 0, 0);
		} catch (Exception e) {
			if (e instanceof GameException)
				throw (GameException) e;
			else
				throw new GameException(
						ctr.getString(R.string.CheckVersion_fire), e);
		}
	}

	@Override
	protected String loadingMsg() {
		return ctr.getString(R.string.CheckVersion_loadingMsg);
	}

	@Override
	protected void onOK() {
		if (halt) {
			new SystemNotifyTip(CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR,
					41), ctr.getString(R.string.CheckVersion_onOK)).show();
			home.showMenu();
			return;
		}

		if (curVer < minVer) {
			home.showMenu();
			new UpdateBonusTip(home).show();
		} else {
			// 进入游戏
			home.loadConfig();
		}
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		home.showMenu();
	}

	public static boolean isNewer(int ver) {
		return curVer >= ver;
	}
}
