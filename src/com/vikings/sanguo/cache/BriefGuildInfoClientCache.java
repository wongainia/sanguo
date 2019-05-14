package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BriefGuildInfoClient;

/**
 * 
 * @author Brad.Chen
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BriefGuildInfoClientCache {

	private int MAX_NUMBER = 100;

	private LRUCache<Integer, BriefGuildInfoClient> content = new LRUCache<Integer, BriefGuildInfoClient>(
			MAX_NUMBER);

	private List<BriefGuildInfoClient> getFromServer(List keys)
			throws GameException {
		return GameBiz.getInstance().briefGuildInfoQuery(keys);
	}

	private void add(BriefGuildInfoClient o) {
		content.put(o.getId(), o);
	}

	public void updateCache(BriefGuildInfoClient bgic) {
		if (null == bgic)
			return;

		BriefGuildInfoClient briefGuildInfoClient = content.get(bgic.getId());
		if (briefGuildInfoClient != null) {
			briefGuildInfoClient.setImage(bgic.getImage());
			briefGuildInfoClient.setLeader(bgic.getLeader());
			briefGuildInfoClient.setLevel(bgic.getId());
			briefGuildInfoClient.setName(bgic.getName());
		} else {
			add(bgic);
		}
	}

	public BriefGuildInfoClient getNew(int id) throws GameException {
		content.remove(id);
		return get(id);
	}

	public BriefGuildInfoClient get(int key) throws GameException {
		BriefGuildInfoClient bgic = content.get(key);
		if (bgic != null)
			return bgic;
		List id = new ArrayList();
		id.add(key);
		List<BriefGuildInfoClient> rs = getFromServer(id);
		if (rs.size() != 0) {
			bgic = rs.get(0);
			add(bgic);
			return bgic;
		}
		throw new GameException("找不到家族，ID " + key);
	}

	public List get(List keys) throws GameException {
		List rs = new ArrayList();
		List tmp = new ArrayList();
		for (Object key : keys) {
			BriefGuildInfoClient bgic = content.get((Integer) key);
			if (bgic != null)
				rs.add(bgic);
			else
				tmp.add(key);
		}
		if (tmp.size() != 0) {
			int i = 0;
			while (i < tmp.size()) {
				List<BriefGuildInfoClient> more = null;
				if (i + 10 <= tmp.size()) {
					more = getFromServer(tmp
							.subList(i, i + Constants.MAX_COUNT));
					i = i + Constants.MAX_COUNT;
				} else {
					more = getFromServer(tmp.subList(i, tmp.size()));
					i = tmp.size();
				}
				if (null != more && !more.isEmpty()) {
					for (BriefGuildInfoClient it : more) {
						rs.add(it);
						add(it);
					}
				}
			}
		}
		return rs;
	}

}
