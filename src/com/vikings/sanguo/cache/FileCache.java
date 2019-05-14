package com.vikings.sanguo.cache;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.config.Mapping;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.Constants;
import com.vikings.sanguo.utils.AesUtil;
import com.vikings.sanguo.utils.BytesUtil;
import com.vikings.sanguo.utils.GzipUtil;
import com.vikings.sanguo.utils.RsaUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 文件缓存 用于保存用资源服获取的资源，在客户端保存在本地
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
abstract public class FileCache {

	static public final String ERROR_MSG = Config.getController().getString(
			R.string.FileCache_ERROR_MSG);
	static public final String ERROR_MSG_EXT = Config.getController()
			.getString(R.string.FileCache_ERROR_MSG_EXT);

	protected static FileAccess fa = Config.getController().getFileAccess();

	protected HashMap content;

	abstract public String getName();

	abstract public Object getKey(Object obj);

	abstract public Object fromString(String line);

	public static String getCfgFileName(int num) {
		return Config.serverId + "_" + num;
	}

	synchronized public void init() throws GameException {
		content = new HashMap();
		int num = Mapping.getNum(getName());
		if (num == 0)
			throw new GameException(Config.getController().getString(
					R.string.FileCache_init));
		if (Mapping.div.containsKey(num)) {
			for (Integer i : Mapping.div.get(num)) {
				doInit(i);
			}
		} else
			if(num != 139)
			doInit(num);
	}

	private void doInit(int num) throws GameException {
		try {
			init(num);
		} catch (Exception e) {
			Config.getController().getUIContext()
					.deleteFile(getCfgFileName(num));
			throw new GameException(StringUtil.repParams(Config.getController()
					.getString(R.string.FileCache_doInit_2), String
					.valueOf(num)));
		}
	}

	private void init(int num) throws Exception {
		byte[] buf = fa.readLocalBytes(getCfgFileName(num));
		// 第一个配置包含了key iv pub
		if (num == 1) {
			byte[] tmp = null;
			int index = 0;
			while (index < buf.length) {
				int type = BytesUtil.getInt(buf, index);
				index += Constants.INT_LEN;
				int len = BytesUtil.getInt(buf, index);
				index += Constants.INT_LEN;
				byte[] data = BytesUtil.getBytes(buf, index, len);
				index += len;
				switch (type) {
				case 0:
					// 配置数据
					tmp = data;
					break;
				case 1:
					// 配置aes key
					Config.configKey = AesUtil.loadKey(data);
					break;
				case 2:
					// aes iv
					Config.iv = new IvParameterSpec(data);
					break;
				case 3:
					// rsa key
					Config.pubKey = RsaUtil.loadPublicKey(data);
					break;
				case 4:
					Config.check = data;
				default:
					break;
				}
			}
			buf = tmp;
		}
		buf = AesUtil.decrypt(buf, Config.configKey, Config.iv);
		buf = GzipUtil.decompress(buf);
		convert(buf);
	}

	protected void convert(byte[] buf) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(buf), "UTF-8"));
		// 跳过第一行
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			if (StringUtil.isNull(line))
				continue;
			parse(line);
		}
	}

	protected void parse(String line) {
		Object obj = fromString(line);
		addContent(obj);
	}

	protected void addContent(Object obj) {
		if (content == null)
			content = new HashMap();
		content.put(getKey(obj), obj);
	}

	public List getList(List keys) throws GameException {
		List rs = new ArrayList(keys.size());
		for (Object key : keys) {
			if (content.containsKey(key))
				rs.add(content.get(key));
			else
				throw new GameException(getName() + ERROR_MSG + key + ","
						+ ERROR_MSG_EXT);
		}
		return rs;
	}

	public Object get(Object key) throws GameException {
		if (!content.containsKey(key))
			throw new GameException(getName() + ERROR_MSG + key + ","
					+ ERROR_MSG_EXT);
		return content.get(key);
	}

	public boolean contains(Object key) {
		return content.containsKey(key);
	}

	public Object[] getSortedKey() {
		Object[] keys = this.content.keySet().toArray();
		Arrays.sort(keys);
		return keys;
	}

	// 取全部数据
	public List getAll() {
		List list = new ArrayList();
		if (null != content && !content.isEmpty())
			list.addAll(content.values());
		return list;
	}

	protected long buildKey(int param1, int param2) {
		long temp = param1;
		temp <<= 32;
		temp |= param2;
		return temp;
	}

}
