package com.vikings.sanguo.cache;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.MessageInfoClient;
import com.vikings.sanguo.utils.ListUtil;

public class MessageInfoClientCache implements Serializable {

	private static final long serialVersionUID = 8863214988385640270L;

	private static final int NUM_LIMIT = 100;

	private static final String file = "ChatMsgInfos";

	// 玩家自己ID
	transient private int userId;

	private MessageInfoClientCache() {
	}

	private HashMap<Integer, List<MessageInfoClient>> content = new HashMap<Integer, List<MessageInfoClient>>();

	public void addMsg(MessageInfoClient msg) {
		int key = msg.getFrom();
		if (key == getUserId())
			key = msg.getTo();
		if (!content.containsKey(key))
			content.put(key, new ArrayList<MessageInfoClient>());
		content.get(key).add(msg);
	}

	public void removeMsg(MessageInfoClient msg) {
		int key = msg.getFrom();
		if (key == getUserId())
			key = msg.getTo();
		if (!content.containsKey(key))
			return;
		content.get(key).remove(msg);
	}

	public void removeMsg(int key) {
		if (!content.containsKey(key))
			return;
		content.remove(key);
	}

	public void addSyncMsg(List<MessageInfoClient> list) {
		for (MessageInfoClient mic : list) {
			if (mic.getType() != MessageInfoClient.TYPE_CHAT)
				continue;
			addMsg(mic);
			// 设置未读消息条数,去掉系统消息
			// Account.unReadCntCache.add(mic.getFrom(), 1);
		}
	}

	public List<MessageInfoClient> getChat(int id) {
		if (!content.containsKey(id))
			return new ArrayList<MessageInfoClient>();
		else
			return content.get(id);
	}

	private void clear() {
		Set<Integer> set = content.keySet();
		for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
			Integer i = it.next();
			List<MessageInfoClient> list = content.get(i);
			if (list == null)
				continue;
			if (list.size() > NUM_LIMIT) {
				content.put(i,
						list.subList(list.size() - NUM_LIMIT, list.size()));
			}
		}
	}

	public void save() {
		save(true);
	}

	public void save(boolean needClear) {
		if (needClear)
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

	public static MessageInfoClientCache getInstance(int myId) {
		MessageInfoClientCache cache = null;
		try {
			ObjectInputStream in = new ObjectInputStream(Config.getController()
					.getUIContext()
					.openFileInput(file + Account.user.getSaveID()));
			cache = (MessageInfoClientCache) in.readObject();
			in.close();
		} catch (Exception e) {
			cache = new MessageInfoClientCache();
		}
		cache.userId = myId;
		if (!cache.content.containsKey(Constants.GM_USER_ID)) {
			cache.content.put(Constants.GM_USER_ID,
					new ArrayList<MessageInfoClient>());
		}
		return cache;
	}

	public List<Integer> getUserIds() {
		List<Integer> userIds = new ArrayList<Integer>();
		List<MessageInfoClient> readMics = new ArrayList<MessageInfoClient>();
		List<MessageInfoClient> unReadMics = new ArrayList<MessageInfoClient>();

		for (Integer userId : content.keySet()) {
			List<MessageInfoClient> mics = content.get(userId);
			if (!mics.isEmpty()) {
				MessageInfoClient mic = mics.get(mics.size() - 1);
				if (mic.isRead())
					readMics.add(mic);
				else
					unReadMics.add(mic);
			}
		}

		Collections.sort(readMics);
		Collections.sort(unReadMics);
		for (MessageInfoClient mic : unReadMics) {
			if (Account.user.getId() != mic.getFrom())
				userIds.add(mic.getFrom());
			else
				userIds.add(mic.getTo());
		}
		userIds.add(Constants.COUNTRY_ID);
		if (Account.user.hasGuild())
			userIds.add(Constants.GUILD_ID);
		userIds.add(Constants.WORLD_ID);
		List<MessageInfoClient> messages = content.get(Constants.GM_USER_ID);
		if (null == messages || messages.isEmpty()) {
			userIds.add(Constants.GM_USER_ID);
		}
		for (MessageInfoClient mic : readMics) {
			if (Account.user.getId() != mic.getFrom())
				userIds.add(mic.getFrom());
			else
				userIds.add(mic.getTo());
		}
		return userIds;
	}

	public MessageInfoClient getLastMessage(int userId) {
		if (!content.containsKey(userId))
			return null;
		List<MessageInfoClient> list = content.get(userId);
		if (list.isEmpty())
			return null;
		Collections.sort(list);
		return list.get(list.size() - 1);
	}

	public int getUnReadMsgCount(int userId) {
		return getUnReadmsg(userId).size();
	}

	// 得到未读消息的总数
	public int getUnReadMsgCountTotal() {
		int unReadMsgTotal = 0;
		List<Integer> userIDs = getUserIds();
		if (!ListUtil.isNull(userIDs)) {
			for (Integer id : userIDs) {
				if (Account.user.getId() != id) {
					unReadMsgTotal += getUnReadMsgCount(id);
				}
			}
		}
		return unReadMsgTotal;
	}

	public List<MessageInfoClient> getUnReadmsg(int userId) {
		List<MessageInfoClient> mics = new ArrayList<MessageInfoClient>();
		if (content.containsKey(userId)) {
			List<MessageInfoClient> list = content.get(userId);
			if (!list.isEmpty()) {
				for (MessageInfoClient mic : list) {
					if (!mic.isRead())
						mics.add(mic);
				}
			}
		}
		return mics;
	}

	public void setRead(int userId) {
		if (content.containsKey(userId)) {
			List<MessageInfoClient> list = content.get(userId);
			if (!list.isEmpty()) {
				for (MessageInfoClient mic : list) {
					if (!mic.isRead())
						mic.setRead(true);
				}
			}
		}
	}

	private int getUserId() {
		if (userId > 0)
			return userId;
		else
			return Account.user.getId();
	}
}
