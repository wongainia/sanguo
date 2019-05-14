package com.vikings.sanguo.cache;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.UserNotifyInfoClient;

public class UserNotifyInfoClientCache implements Serializable {

	private static final long serialVersionUID = 8863214988385640270L;

	private static final int NUM_LIMIT = 100;

	private static final String file = "SystemMsgInfos";

	private UserNotifyInfoClientCache() {
	}

	// 存系统消息
	private List<UserNotifyInfoClient> systemMsgs = new ArrayList<UserNotifyInfoClient>();

	public void addMsg(UserNotifyInfoClient unic) {
		if (!systemMsgs.contains(unic))
			systemMsgs.add(unic);
	}

	public void removeMsg(UserNotifyInfoClient unic) {
		if (systemMsgs.contains(unic))
			systemMsgs.remove(unic);
	}

	public void addSyncMsg(List<UserNotifyInfoClient> list) {
		systemMsgs.clear();
		for (UserNotifyInfoClient unic : list) {
			addMsg(unic);
		}
	}

	public List<UserNotifyInfoClient> getSysMsg() {
		return systemMsgs;
	}


	private void clear() {
		if (systemMsgs.size() > NUM_LIMIT)
			systemMsgs = systemMsgs.subList(systemMsgs.size() - NUM_LIMIT,
					systemMsgs.size());
	}

	public void save() {
		clear();
		try {
			ObjectOutputStream out = new ObjectOutputStream(Config
					.getController()
					.getUIContext()
					.openFileOutput(file + Account.user.getSaveID(),
							Context.MODE_PRIVATE));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static UserNotifyInfoClientCache getInstance(int myId) {
		UserNotifyInfoClientCache cache = null;
		try {
			ObjectInputStream in = new ObjectInputStream(Config.getController()
					.getUIContext().openFileInput(file + Account.user.getSaveID()));
			cache = (UserNotifyInfoClientCache) in.readObject();
			in.close();
		} catch (Exception e) {
			cache = new UserNotifyInfoClientCache();
		}
		return cache;
	}

}
