package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefUserInfoClient;

/**
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserCache {

	private int MAX_NUMBER = 500;

	private LRUCache<Integer, BriefUserInfoClient> content = new LRUCache<Integer, BriefUserInfoClient>(
			MAX_NUMBER);

	private List<BriefUserInfoClient> getFromServer(List keys)
			throws GameException {
		return GameBiz.getInstance().queryBriefUserList(keys);
	}

	private void add(BriefUserInfoClient o) {
		content.put(o.getId(), o);
	}

	// 只更新信息
	public void updateCache(BriefUserInfoClient u) {
		if (null == u)
			return;

		BriefUserInfoClient user = content.get(u.getId());
		if (user != null) {
			user.setImage(u.getImage());
			user.setNickName(u.getNickName());
			user.setSex(u.getSex());
			user.setBirthday(u.getBirthday());
			user.setProvince(u.getProvince());
			user.setCity(u.getCity());
			user.setLevel(u.getLevel());
			user.setGuildid(u.getGuildid());
		} else
			content.put(u.getId(), u);
	}

	public void replaceCache(BriefUserInfoClient u) {
		if (null == u)
			return;
		content.put(u.getId(), u);
	}

	public BriefUserInfoClient getNew(int id) throws GameException {
		content.remove(id);
		return get(id);
	}

	public BriefUserInfoClient getUser(int key) throws GameException {
		if (key == Account.user.getId())
			return Account.user.bref();

		BriefUserInfoClient u = content.get(key);
		if (u != null)
			return u;
		List id = new ArrayList();
		id.add(key);
		List<BriefUserInfoClient> rs = getFromServer(id);
		if (rs.size() != 0) {
			u = rs.get(0);
			add(u);
			return u;
		}
		throw new GameException("找不到用户，ID " + key);
	}
	
	public BriefUserInfoClient get(int key) throws GameException {
		if (BriefUserInfoClient.isNPC(key))
			return CacheMgr.npcCache.getNpcUser(key);

		return getUser(key);
	}

	public List get(List keys) throws GameException {
		List rs = new ArrayList();
		List tmp = new ArrayList();

		for (Object o : keys) {
			Integer key = (Integer) o;
			if (key.equals(Account.user.getId()))
				rs.add(Account.user.bref());
			else if (BriefUserInfoClient.isNPC(key)) {
				rs.add(CacheMgr.npcCache.getNpcUser(key));
			} else {
				tmp.add(key);
			}
		}

		if (tmp.size() != 0) {
			int i = 0;
			while (i < tmp.size()) {
				List<BriefUserInfoClient> more = null;
				if (i + 10 <= tmp.size()) {
					more = getFromServer(tmp.subList(i, i
							+ Constants.USER_COUNT));
					i = i + Constants.USER_COUNT;
				} else {
					more = getFromServer(tmp.subList(i, tmp.size()));
					i = tmp.size();
				}
				if (null != more && !more.isEmpty()) {
					for (BriefUserInfoClient it : more) {
						rs.add(it);
						add(it);
					}
				}
			}
		}
		return rs;
	}

	public static List<BriefUserInfoClient> sequenceByIds(List<Integer> ids,
			List<BriefUserInfoClient> users) {
		List<BriefUserInfoClient> sequenceUsers = new ArrayList<BriefUserInfoClient>();
		if (ids != null && users != null) {
			for (int id : ids) {
				for (BriefUserInfoClient briefUser : users) {
					if (id == briefUser.getId().intValue())
						sequenceUsers.add(briefUser);
				}
			}
		}
		return sequenceUsers;
	}

}
