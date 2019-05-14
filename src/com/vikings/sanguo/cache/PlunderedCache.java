package com.vikings.sanguo.cache;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.model.Dict;

public class PlunderedCache implements Serializable {

	private static final long serialVersionUID = -4440870248634883230L;

	private static final String file = "PlunderedInfos";

	// 玩家自己ID
	transient private int userId;

	private HashMap<Long, Integer> content = new HashMap<Long, Integer>();

	@SuppressWarnings("rawtypes")
	synchronized public void save() {
		for (Iterator iter = content.entrySet().iterator(); iter.hasNext();) {
			Entry entry = (Entry) iter.next();
			Integer time = (Integer) entry.getValue();
			if (time == null || expire(time))
				iter.remove();
		}
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

	public static PlunderedCache getInstance(int myId) {
		PlunderedCache cache = null;
		try {
			ObjectInputStream in = new ObjectInputStream(Config.getController()
					.getUIContext()
					.openFileInput(file + Account.user.getSaveID()));
			cache = (PlunderedCache) in.readObject();
			in.close();
		} catch (Exception e) {
			cache = new PlunderedCache();
		}
		cache.userId = myId;
		return cache;
	}

	public boolean plundered(long fiefId) {
		if (content.containsKey(fiefId) && !Account.richFiefCache.isMyFief(fiefId)) {
			int time = content.get(fiefId);
			return !expire(time);
		}
		return false;
	}

	public boolean expire(int time) {
		return Config.serverTimeSS() > time
				+ CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 29);
	}

	public void update(long fiefId, boolean save) {
		content.put(fiefId, Config.serverTimeSS());
		if (save)
			new SaveInvoker().start();
	}

	private class SaveInvoker extends BackgroundInvoker {

		@Override
		protected void fire() throws GameException {
			save();
		}

		@Override
		protected void onOK() {

		}
	}
}
