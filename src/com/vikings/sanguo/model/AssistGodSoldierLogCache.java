package com.vikings.sanguo.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;

public class AssistGodSoldierLogCache implements Serializable {
	private static final long serialVersionUID = -2336575540043333875L;

	private static final String file = "assist_info_";

	private HashMap<Long, GodSoldierInfoClient> content;// key:BattleId

	private AssistGodSoldierLogCache() {
		content = new HashMap<Long, GodSoldierInfoClient>();
	}

	public void addAssistGodSoldier(long battleId, long uniqueId, int cost,
			MoveTroopInfoClient mtic) {
		if (content.containsKey(battleId)) {
			GodSoldierInfoClient info = content.get(battleId);
			if (info.getUniqueId() != uniqueId) {
				info.clear();
				info.setUniqueId(uniqueId);
			}
			BaseGodSoldierInfoClient baseInfo = new BaseGodSoldierInfoClient();
			baseInfo.setCost(cost);
			baseInfo.setTime(Config.serverTime());
			baseInfo.setMtic(mtic.copy());
			info.addInfo(baseInfo);
		} else {
			GodSoldierInfoClient info = new GodSoldierInfoClient();
			info.setBattleId(battleId);
			info.setUniqueId(uniqueId);
			BaseGodSoldierInfoClient baseInfo = new BaseGodSoldierInfoClient();
			baseInfo.setCost(cost);
			baseInfo.setTime(Config.serverTime());
			baseInfo.setMtic(mtic.copy());
			info.addInfo(baseInfo);
			content.put(battleId, info);
		}
	}

	// 领地放弃时，把在此地购买神兵的记录清空
	public void deleteGodSoldierByFiefid(long battleID) {
		if (null == content)
			return;
		if (content.containsKey(battleID))
			content.remove(battleID);
	}

	private boolean isValid(long battleId, long uniqueId) {
		if (null == content)
			return false;
		if (!content.containsKey(battleId))
			return false;
		if (content.get(battleId).getUniqueId() != uniqueId)
			return false;
		return true;
	}

	public List<BaseGodSoldierInfoClient> getAssistGodSoldier(long battleId,
			long uniqueId) {
		if (!isValid(battleId, uniqueId))
			return new ArrayList<BaseGodSoldierInfoClient>();

		return content.get(battleId).getInfos();
	}

	public int getTotalCost(long battleId, long uniqueId) {
		int total = 0;
		if (!isValid(battleId, uniqueId))
			return total;
		return content.get(battleId).getTotalCost();
	}

	public int getTotalTroopAmount(long battleId, long uniqueId) {
		int total = 0;

		if (!isValid(battleId, uniqueId))
			return total;

		return content.get(battleId).getTotalTroop();
	}

	public void setTimes(long battleId, long uniqueId, int times) {
		if (isValid(battleId, uniqueId)) {
			content.get(battleId).setTimes(times);
		}
	}

	private void tidyDate() {
		if (content.isEmpty())
			return;
		List<Long> keys = new ArrayList<Long>();
		keys.addAll(content.keySet());
		List<Long> battleIds = Account.briefBattleInfoCache.getBattleIds();
		for (Long key : keys) {
			if (content.containsKey(key) && !battleIds.contains(key)) {
				content.remove(key);
			}
		}
	}

	public void save() {
		try {
			tidyDate();
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

	public static AssistGodSoldierLogCache getInstance() {
		AssistGodSoldierLogCache cache = null;
		try {
			ObjectInputStream in = new ObjectInputStream(Config.getController()
					.getUIContext()
					.openFileInput(file + Account.user.getSaveID()));
			cache = (AssistGodSoldierLogCache) in.readObject();
			if (cache.content == null)
				cache.content = new HashMap<Long, GodSoldierInfoClient>();
			in.close();
			cache.tidyDate();
		} catch (Exception e) {
			cache = new AssistGodSoldierLogCache();
		}
		return cache;
	}
}
