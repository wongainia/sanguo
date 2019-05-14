package com.vikings.sanguo.access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.ServerFileCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.UserAccountClient;
import com.vikings.sanguo.protos.BattleLogInfo;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class FileAccess {

	private static final String tag = "FileAccess";

	public static String PARNET_PATH = ".com.vikings.sanguo";

	public static final String IMAGE_PATH = PARNET_PATH + "/image/";

	public static final String SOUND_PATH = PARNET_PATH + "/sound/";

	public static final String BATTLE_LOG_PATH = PARNET_PATH + "/battlelog/";

	private static final String ENCODING = "UTF-8";

	public static final String BATTLE_LOG_PREFIX = "_battlelog_v1_";

	public static final String USER_FILE_NAME = "datas";

	public static final String LOGIN_TIME = "login_time";

	public static final String HONOR_RANK = "honor_rank_reward";

	// public static final String TRIGGER_GUILD_STEP = "trigger_guild_step";

	public static final String USER_RECHARGE_FILE_NAME = "month_data";

	private static LinkedBuffer linkedBuffer = LinkedBuffer.allocate(512);

	private Context context;

	private File sdCardDir;

	public FileAccess(Context context) {
		this.context = context;
		Log.i(tag, context.getFilesDir().getAbsolutePath());
		Log.i(tag, context.getCacheDir().getAbsolutePath());
	}

	public boolean checkSDCard() {
		boolean sdCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCard)
			sdCardDir = Environment.getExternalStorageDirectory();
		return sdCard;
	}

	public File getSdCardDir() {
		return sdCardDir;
	}

	public File getSdCardFile(String name) {
		if (!checkSDCard())
			return null;
		File f = new File(sdCardDir, PARNET_PATH + "/" + name);
		File p = f.getParentFile();
		if (!p.exists())
			p.mkdirs();
		return f;
	}

	public boolean checkImageExist(String name) {
		try {
			File f = null;
			if (!checkSDCard()) {
				f = new File(context.getFilesDir() + name);
			} else {
				f = new File(sdCardDir, IMAGE_PATH + "/" + name);
			}
			if (f.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存私有文件内容
	 * 
	 * @param name
	 * @param lines
	 */
	public void saveLocal(String name, List<String> lines, boolean append) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput(name, append ? Context.MODE_APPEND
							: Context.MODE_PRIVATE), ENCODING));
			for (String line : lines) {
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
		}
	}

	private void saveLocal(byte[] bytes, String name) {
		try {
			OutputStream out = context.openFileOutput(name,
					Context.MODE_PRIVATE);
			out.write(bytes);
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error");
		}
	}

	/**
	 * 读取私有文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<String> readLocal(String fileName) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					context.openFileInput(fileName), ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
		}
		return lines;
	}

	public List<String> readLocal(InputStream is) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is,
					ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error");
		}
		return lines;
	}

	public byte[] readLocalBytes(String fileName) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			FileInputStream in = context.openFileInput(fileName);
			int count = -1;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error");
		}
		return out.toByteArray();
	}

	public File getFile(String fileName, String path) {
		if (!checkSDCard()) {
			return readLocalFile(fileName);
		} else {
			return readSDCardFile(fileName, path);
		}
	}

	public File readLocalFile(String fileName) {
		return context.getFileStreamPath(fileName);
	}

	public File readSDCardFile(String fileName, String path) {
		File file = new File(sdCardDir, path + "/" + fileName);
		if (file.exists())
			return file;
		else
			return null;
	}

	public void clearCacheFile(String folder, String prefix) {
		if (!checkSDCard()) {
			String[] filenames = context.fileList();
			for (int i = 0; i < filenames.length; i++) {
				String filename = filenames[i];
				if (!StringUtil.isNull(filename)) {
					if (filename.startsWith(prefix)) {
						context.deleteFile(filename);
					}
				}
			}
		} else {
			// 有SD卡
			File file = new File(sdCardDir, folder);
			if (file.exists()) {
				File[] chirden = file.listFiles();
				if (null != chirden) {
					for (int i = 0; i < chirden.length; i++) {
						if (chirden[i].isFile()) {
							chirden[i].delete();
						}
					}
				}
			}
		}
	}

	/**
	 * 清除SD卡图片
	 */
	public void clearImage() {
		// SD卡不存在，清除內存中的文件
		if (!checkSDCard()) {
			String[] filenames = context.fileList();
			for (int i = 0; i < filenames.length; i++) {
				String filename = filenames[i];
				if (!StringUtil.isNull(filename)) {
					if (Character.isDigit(filename.charAt(0))
							&& (filename.endsWith(".png") || filename
									.endsWith(".jpg"))) {
						context.deleteFile(filename);
					}
				}
			}
		} else {
			// 有SD卡
			File file = new File(sdCardDir, IMAGE_PATH);
			if (file.exists()) {
				File[] chirden = file.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						if (!StringUtil.isNull(filename))
							return Character.isDigit(filename.charAt(0));
						return false;
					}
				});
				if (null != chirden) {
					for (int i = 0; i < chirden.length; i++) {
						if (chirden[i].isFile()) {
							chirden[i].delete();
						}
					}
				}
			}
		}

	}

	// 清理配置文件
	public void clearCfg() {
		String[] filenames = context.fileList();
		for (int i = 0; i < filenames.length; i++) {
			String filename = filenames[i];
			try {
				if (filename.endsWith("_vc")) {
					context.deleteFile(filename);
				} else {
					if (filename.indexOf('.') == -1) {
						context.deleteFile(filename);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	public void saveFile(byte[] bytes, String fileName, String path) {
		if (!checkSDCard()) {
			saveLocal(bytes, fileName);
			return;
		}
		fileName = path + fileName;
		try {
			File f = new File(sdCardDir, fileName);
			File p = f.getParentFile();
			if (!p.exists())
				p.mkdirs();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			Log.e(tag, "save sdcard error", e);
		}
	}

	public void saveSound(byte[] bytes, String fileName) {
		saveFile(bytes, fileName, SOUND_PATH);
	}

	public void saveImage(byte[] bytes, String fileName) {
		saveFile(bytes, fileName, IMAGE_PATH);
	}

	public void saveImage(Bitmap bitmap, String fileName) {

		try {
			FileOutputStream fileOutputStream = null;
			if (!checkSDCard()) {
				fileOutputStream = context.openFileOutput(fileName,
						Context.MODE_PRIVATE);
			} else {
				File file = new File(sdCardDir, IMAGE_PATH + "/" + fileName);
				File p = file.getParentFile();
				if (!p.exists())
					p.mkdirs();
				fileOutputStream = new FileOutputStream(file);
			}
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveData(byte[] bytes, String fileName, String folder) {
		saveFile(bytes, fileName, PARNET_PATH + "/" + folder + "/");
	}

	public File readFile(String fileName, String folder) {
		if (!checkSDCard()) {
			return context.getFileStreamPath(fileName);
		}
		File file = new File(sdCardDir + "/" + folder, fileName);
		if (file.exists())
			return file;
		else
			return null;
	}

	public byte[] readFileDate(File file) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			FileInputStream in = new FileInputStream(file);
			int count = -1;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error", e);
		}
		return out.toByteArray();
	}

	public byte[] readInputStreamDate(InputStream is) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			int count = -1;
			while ((count = is.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			is.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error", e);
		}
		return out.toByteArray();
	}

	public File readImage(String fileName) {
		if (!checkSDCard()) {
			return context.getFileStreamPath(fileName);
		}
		fileName = IMAGE_PATH + fileName;
		return new File(sdCardDir, fileName);
	}

	public List<String> readSdcard(String fileName) {
		if (!checkSDCard()) {
			return readLocal(fileName);
		}
		fileName = PARNET_PATH + "/" + fileName;
		File f = new File(sdCardDir, fileName);
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readSdcard error", e);
		}
		return lines;
	}

	public void saveSdcard(String fileName, List<String> lines, boolean append) {
		if (!checkSDCard()) {
			saveLocal(fileName, lines, append);
			return;
		}
		fileName = PARNET_PATH + "/" + fileName;
		try {
			File f = new File(sdCardDir, fileName);
			File p = f.getParentFile();
			if (!p.exists())
				p.mkdirs();
			if (!f.exists())
				f.createNewFile();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f, append), ENCODING));
			for (String line : lines) {
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
		}
	}

	public boolean isSDCardEnough() {
		StatFs statFs = new StatFs(sdCardDir.getPath());
		long blocSize = statFs.getBlockSize(); // 获取block的SIZE
		long availaBlock = statFs.getAvailableBlocks(); // 空闲的Block的数量

		// SD卡可用空间要大于1M
		if (blocSize * availaBlock >= Constants.MEM_LOW_THRESHOLD)
			return true;
		return false;
	}

	private String getBattleLogPath(long battleId) {
		return Config.serverId + BATTLE_LOG_PREFIX + String.valueOf(battleId);
	}

	public void saveBattleLogInfo(BattleLogInfo info) {
		try {
			saveFile(ProtobufIOUtil.toByteArray(info,
					BattleLogInfo.getSchema(), linkedBuffer),
					getBattleLogPath(info.getId()), BATTLE_LOG_PATH);
		} catch (Exception e) {
			Log.e(FileAccess.class.getSimpleName(), "err", e);
		} finally {
			linkedBuffer.clear();
		}

	}

	public BattleLogInfo getBattleLogInfo(long logId) {
		try {
			byte[] bytes = null;
			if (checkSDCard()) {
				File f = getBattleLogInfoFile(logId);
				if (f != null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					FileInputStream in = new FileInputStream(f);
					byte[] buf = new byte[1024];

					int count = -1;
					while ((count = in.read(buf)) != -1) {
						out.write(buf, 0, count);
					}
					in.close();
					bytes = out.toByteArray();

				}
			}
			if (bytes != null && bytes.length > 0) {
				BattleLogInfo bi = new BattleLogInfo();
				ProtobufIOUtil.mergeFrom(bytes, bi, BattleLogInfo.getSchema());
				return bi;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public File getBattleLogInfoFile(long logId) {
		return getFile(getBattleLogPath(logId), BATTLE_LOG_PATH);
	}

	public JSONObject getUserData() {
		return getData(USER_FILE_NAME);
	}

	public JSONObject getData(String fileName) {
		File file = readFile(fileName, PARNET_PATH);
		if (null != file) {
			try {
				String dataStr = new String(readFileDate(file), "UTF-8");
				return new JSONObject(dataStr);
			} catch (Exception e) {
				file.delete();
			}
		}
		return new JSONObject();
	}

	/**
	 * @param key
	 *            移动cmcc，电信telecom
	 * @return
	 */
	public int getRechargeAmountThisMonth(String key) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTimeInMillis(Config.serverTime());
		JSONObject data = getData(USER_RECHARGE_FILE_NAME);
		try {
			if (data.has(key)) {
				JSONObject obj = data.getJSONObject(key);
				int year = obj.getInt("year");
				int month = obj.getInt("month");
				int amount = obj.getInt("amount");
				if (c.get(Calendar.YEAR) != year)
					return 0;
				if (c.get(Calendar.MONTH) != month)
					return 0;
				return amount;
			}
		} catch (Exception e) {

		}
		return 0;
	}

	public void saveRechargeAmountThisMonth(String key, int count) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTimeInMillis(Config.serverTime());
		JSONObject data = getData(USER_RECHARGE_FILE_NAME);
		try {
			if (data.has(key)) {
				JSONObject obj = data.getJSONObject(key);
				int year = obj.getInt("year");
				int month = obj.getInt("month");
				int amount = obj.getInt("amount");
				if (c.get(Calendar.YEAR) == year
						&& c.get(Calendar.MONTH) == month) {
					obj.put("amount", amount + count);
				} else {
					obj.put("year", c.get(Calendar.YEAR));
					obj.put("month", c.get(Calendar.MONTH));
					obj.put("amount", count);
				}
			} else {
				JSONObject obj = new JSONObject();
				obj.put("year", c.get(Calendar.YEAR));
				obj.put("month", c.get(Calendar.MONTH));
				obj.put("amount", count);
				data.put(key, obj);
			}
			saveFile(data.toString().getBytes("UTF-8"),
					USER_RECHARGE_FILE_NAME, PARNET_PATH + "/");
		} catch (Exception e) {

		}
	}

	public void saveUser(List<AccountPswInfoClient> infos) {
		if (null == infos || infos.isEmpty())
			return;
		try {
			JSONObject data = getUserData();
			data.put("servers", new JSONArray());
			JSONArray array = data.getJSONArray("servers");
			for (AccountPswInfoClient info : infos) {
				JSONObject json = new JSONObject();
				json.put("sid", info.getSid());
				json.put("id", info.getUserid());
				json.put("pwd", info.getPsw());
				json.put("nick", info.getNick());
				json.put("level", info.getLevel());
				array.put(json);
			}
			saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
					PARNET_PATH + "/");
		} catch (Exception e) {
		}
	}

	public void saveLastInfo(int sid, int userId) {
		if (userId <= 0 || sid <= 0)
			return;
		try {
			JSONObject data = getUserData();
			JSONObject json = new JSONObject();
			json.put("sid", sid);
			json.put("id", userId);
			json.put("ip", Config.gameIp);
			json.put("port", Config.gamePort);
			data.put("last", json);
			saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
					PARNET_PATH + "/");
		} catch (Exception e) {
		}
	}

	public List<AccountPswInfoClient> getUsers() {
		List<AccountPswInfoClient> clients = new ArrayList<AccountPswInfoClient>();
		JSONObject data = getUserData();
		try {
			if (data.has("servers")) {
				JSONArray array = data.getJSONArray("servers");
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = array.getJSONObject(i);
					AccountPswInfoClient client = new AccountPswInfoClient();
					client.setUserid(json.getInt("id"));
					client.setSid(json.getInt("sid"));
					client.setLevel(json.getInt("level"));
					client.setNick(json.getString("nick"));
					client.setPsw(json.getString("pwd"));
					clients.add(client);
				}
			}
		} catch (Exception e) {

		}
		return clients;
	}

	public AccountPswInfoClient getLastUser() {
		JSONObject data = getUserData();
		try {
			if (data.has("last") && data.has("servers")) {
				JSONObject lastInfo = data.getJSONObject("last");
				int sid = lastInfo.getInt("sid");
				int userId = lastInfo.getInt("id");
				if (sid >= 0 && userId > 0) {
					JSONArray array = data.getJSONArray("servers");
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						if (json.getInt("sid") == sid
								&& json.getInt("id") == userId) {
							AccountPswInfoClient client = new AccountPswInfoClient();
							client.setUserid(json.getInt("id"));
							client.setSid(json.getInt("sid"));
							client.setLevel(json.getInt("level"));
							client.setNick(json.getString("nick"));
							client.setPsw(json.getString("pwd"));
							return client;
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return null;
	}

	public void saveUser(int sid, int userId, int level, String nick, String pwd) {
		if (sid <= 0 || userId <= 0)
			return;
		try {
			JSONObject data = getUserData();
			if (!data.has("servers"))
				data.put("servers", new JSONArray());
			JSONArray array = data.getJSONArray("servers");
			JSONObject json = new JSONObject();
			json.put("sid", sid);
			json.put("id", userId);
			json.put("pwd", pwd);
			json.put("nick", nick);
			json.put("level", level);
			array.put(json);
			saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
					PARNET_PATH + "/");
		} catch (Exception e) {

		}
	}

	public void updateUser(int sid, UserAccountClient uac) {
		if (null == uac || !uac.isValidUser())
			return;
		try {
			JSONObject data = getUserData();
			if (!data.has("servers"))
				data.put("servers", new JSONArray());
			JSONArray array = data.getJSONArray("servers");
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				if (json.getInt("sid") == sid
						&& json.getInt("id") == uac.getId()) {
					if ((!StringUtil.isNull(uac.getNick()) && !json.getString(
							"nick").equals(uac.getNick()))
							|| (uac.getLevel() > 0 && json.getInt("level") != uac
									.getLevel())) {
						json.put("nick", uac.getNick());
						json.put("level", uac.getLevel());
						saveFile(data.toString().getBytes("UTF-8"),
								USER_FILE_NAME, PARNET_PATH + "/");
						break;

					}

				}
			}
		} catch (Exception e) {

		}
	}

	public void updateUser(ServerData serverData, AccountPswInfoClient client) {
		if (null == serverData || null == client || client.getUserid() <= 0)
			return;
		try {
			JSONObject data = getUserData();
			JSONObject lastJson = new JSONObject();
			lastJson.put("sid", serverData.getServerId());
			lastJson.put("id", client.getUserid());
			lastJson.put("ip", Config.gameIp);
			lastJson.put("port", Config.gamePort);
			data.put("last", lastJson);
			if (!data.has("servers"))
				data.put("servers", new JSONArray());
			JSONArray array = data.getJSONArray("servers");
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				if (json.getInt("sid") == serverData.getServerId()
						&& json.getInt("id") == client.getUserid()) {
					json.put("pwd", client.getPsw());
					saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
							PARNET_PATH + "/");
					return;
				}
			}
		} catch (Exception e) {

		}
	}

	public void saveUser(UserAccountClient uac, int sid) {
		if (null == uac || !uac.isValidUser())
			return;
		try {
			JSONObject data = getUserData();
			data.put("last", sid);
			data.put("ip", Config.gameIp);
			data.put("port", Config.gamePort);
			if (!data.has("servers")) {
				data.put("servers", new JSONArray());
			}
			JSONArray array = data.getJSONArray("servers");
			JSONObject temp = null;
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				if (json.getInt("sid") == sid
						&& json.getInt("id") == uac.getId()) {
					temp = json;
					break;
				}
			}

			if (null == temp) {
				temp = new JSONObject();
				temp.put("sid", sid);
				temp.put("id", uac.getId());
				temp.put("pwd", uac.getPsw());
			}
			temp.put("level", uac.getLevel());
			temp.put("nick", uac.getNick());

			saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
					PARNET_PATH + "/");
		} catch (Exception e) {
		}
	}

	public boolean hasUser(int sid) {
		JSONObject data = getUserData();
		try {
			if (data.has("servers")) {
				JSONArray a = data.getJSONArray("servers");
				for (int i = 0; i < a.length(); i++) {
					JSONObject json = a.getJSONObject(i);
					if (json.getInt("sid") == sid) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.e(tag, e.getMessage(), e);
		}
		return false;
	}

	public boolean hasUser() {
		try {
			JSONObject data = getUserData();
			if (data.has("servers")
					&& data.getJSONArray("servers").length() > 0) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public void deleteUser(int sid, int userId) {
		JSONObject data = getUserData();
		try {
			if (data.has("servers")) {
				JSONArray a = data.getJSONArray("servers");
				JSONArray newArray = new JSONArray();
				for (int i = 0; i < a.length(); i++) {
					if (null != a.getJSONObject(i)
							&& (a.getJSONObject(i).getInt("sid") != sid || a
									.getJSONObject(i).getInt("id") != userId)) {
						newArray.put(a.getJSONObject(i));
					}
				}
				data.remove("servers");
				data.put("servers", newArray);

				saveFile(data.toString().getBytes("UTF-8"), USER_FILE_NAME,
						PARNET_PATH + "/");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] readServerConfig() {
		File file = getFile(ServerFileCache.FILE_NAME, PARNET_PATH + "/");
		if (null != file && file.exists())
			return readFileDate(file);
		return readServerConfigFromRaw();
	}

	public byte[] readServerConfigFromRaw() {
		InputStream is = context.getResources().openRawResource(R.raw.server);
		return readInputStreamDate(is);
	}

	public void saveServerConfig(byte[] bytes) {
		saveFile(bytes, ServerFileCache.FILE_NAME, PARNET_PATH + "/");
	}

	public int getLastLoginServerId() {
		int lastServerId = -1;
		try {
			JSONObject data = getUserData();
			if (data.has("last")) {
				JSONObject json = data.getJSONObject("last");
				if (data.has("sid"))
					lastServerId = json.getInt("sid");
			}
		} catch (Exception e) {

		}
		return lastServerId;
	}

	public void saveLastLoginTime() {
		try {
			JSONObject data = getData(LOGIN_TIME);
			if (!data.has("data"))
				data.put("data", new JSONArray());

			JSONArray jArray = data.getJSONArray("data");
			int i = 0;
			for (; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				if (Config.serverId == obj.getInt("serverId")) {
					obj.put("time", Config.serverTime());
					break;
				}
			}

			if (i == jArray.length()) {
				JSONObject obj = new JSONObject();
				obj.put("serverId", Config.serverId);
				obj.put("time", Config.serverTime());
				jArray.put(obj);
			}

			saveFile(data.toString().getBytes("UTF-8"), LOGIN_TIME, PARNET_PATH
					+ "/");
		} catch (Exception e) {
		}
	}

	public long getLastLoginTime() {
		try {
			JSONObject data = getData(LOGIN_TIME);
			if (data.isNull("data") || !data.has("data"))
				return 0;

			JSONArray jArray = data.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				if (Config.serverId == obj.getInt("serverId"))
					return obj.getLong("time");
			}

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void saveHonorRankReward(int honorType, boolean hasReward) {
		try {
			JSONObject data = getData(HONOR_RANK);
			if (!data.has("data"))
				data.put("data", new JSONArray());

			JSONArray jArray = data.getJSONArray("data");
			int i = 0;
			for (; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				if (honorType == obj.getInt("honorType")) {
					obj.put("time", Config.serverTime());
					obj.put("hasReward", hasReward);
					break;
				}
			}

			if (i == jArray.length()) {
				JSONObject obj = new JSONObject();
				obj.put("honorType", honorType);
				obj.put("time", Config.serverTime());
				obj.put("hasReward", hasReward);
				jArray.put(obj);
			}

			saveFile(data.toString().getBytes("UTF-8"), HONOR_RANK, PARNET_PATH
					+ "/");
		} catch (Exception e) {
		}
	}

	public boolean getHasHonorRankReward(int honorType) {
		try {
			JSONObject data = getData(HONOR_RANK);
			if (data.isNull("data") || !data.has("data"))
				return false;

			JSONArray jArray = data.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject obj = jArray.getJSONObject(i);
				if (honorType == obj.getInt("honorType")
						&& DateUtil.isToday(obj.getLong("time")))
					return !obj.getBoolean("hasReward");
			}

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public void saveTriggerGuildStep(int val) {
	// try {
	// JSONObject data = getData(TRIGGER_GUILD_STEP);
	// data.put("val", val);
	// saveFile(data.toString().getBytes("UTF-8"), TRIGGER_GUILD_STEP,
	// PARNET_PATH
	// + "/");
	// } catch (Exception e) {
	// }
	// }
	//
	// public boolean getHasTriggerGuildStep() {
	// try {
	// JSONObject data = getData(TRIGGER_GUILD_STEP);
	// if (data.isNull("val") || !data.has("val"))
	// return false;
	//
	// return 1 == data.getInt("val");
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }
}
