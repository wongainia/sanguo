package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.AccountPswInfoClient;
import com.vikings.sanguo.model.ServerData;
import com.vikings.sanguo.model.ServerUserData;
import com.vikings.sanguo.model.UserAccountClient;

public class ServerFileCache extends FileCache {

	public static final String FILE_NAME = "ss_v1";

	private byte[] bytes = null;

	@Override
	public synchronized void init() {
		convert();
	}

	protected void convert() {
		if (content == null)
			content = new HashMap();
		else
			content.clear();

		// 如果bytes有值，直接解析
		if (null != bytes) {
			try {
				convert(bytes);
				setDefault();
				return;
			} catch (Exception e) {

			}
		}

		// 当上面流程失败时，读sdcard数据
		try {
			bytes = fa.readServerConfig();
			convert(bytes);
			setDefault();
			return;
		} catch (Exception e) {

		}

		// 当上面流程失败时，读raw文件中数据
		try {
			bytes = fa.readServerConfigFromRaw();
			convert(bytes);
			setDefault();
		} catch (Exception e) {

		}
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((ServerData) obj).getServerId();
	}

	@Override
	public Object fromString(String line) {
		return ServerData.fromString(line);
	}

	public ServerData getByServerId(int serverId) {
		if (content.containsKey(serverId))
			return (ServerData) content.get(serverId);
		else
			return null;
	}

	public synchronized ServerData getLatest() {
		List<ServerData> list = getAll();
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<ServerData> getAll() {
		List<ServerData> list = new ArrayList<ServerData>();
		if (!content.isEmpty()) {
			list.addAll(content.values());
		}
		Collections.sort(list);
		return list;
	}

	public void setDefault() {
		List<ServerData> list = getAll();
		// 先去上次登陆的serverId，如果没有，则取默认
		ServerData serverData = null;
		AccountPswInfoClient lastClient = fa.getLastUser();
		int lastServerId = -1;
		if (null != lastClient) {
			lastServerId = lastClient.getSid();
		}
		if (lastServerId != -1)
			serverData = getByServerId(lastServerId);
		if (serverData == null && !list.isEmpty()) {
			serverData = list.get(0);
		}
		Config.setServer(serverData, lastClient);
	}

	public void update(byte[] bytes) {
		if (null == bytes || bytes.length == 0)
			return;
		// 保存当前数据
		this.bytes = bytes;
		convert();
	}

	public void save() {
		if (null != bytes)
			Config.getController().getFileAccess().saveServerConfig(bytes);

	}

}
