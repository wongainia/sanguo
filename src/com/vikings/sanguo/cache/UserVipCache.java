package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.UserVip;

public class UserVipCache extends ArrayFileCache {

	private static final String FILE_NAME = "user_vip.csv";
	private int maxLvl;

	@Override
	public String getName() {
		return FILE_NAME;
	}

	@Override
	public synchronized void init() throws GameException {
		super.init();

		for (Object o : list) {
			UserVip vip = (UserVip) o;
			if (vip.getLevel() > maxLvl)
				maxLvl = vip.getLevel();
		}
	}

	public UserVip getVipByCharge(int charge) {
		UserVip v = null;
		for (Object o : list) {
			UserVip vip = (UserVip) o;
			if (vip.getCharge() <= charge) {
				v = vip;
			} else {
				break;
			}
		}
		return v;
	}

	public UserVip getVipByLvl(int lvl) {
		for (Object o : list) {
			UserVip t = (UserVip) o;
			if (t.getLevel() == lvl)
				return t;
		}
		return null;
	}

	public UserVip getVipByQuestId(int id) {
		for (Object o : list) {
			UserVip t = (UserVip) o;
			if (t.getQuestId() == id)
				return t;
		}
		return null;
	}

	public UserVip getVipByPoker(int count) {
		for (Object o : list) {
			UserVip t = (UserVip) o;
			if (t.getBloodPokerCount() == count)
				return t;
		}
		return null;
	}

	@Override
	public long getSearchKey1(Object obj) {
		return ((UserVip) obj).getLevel();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((UserVip) obj).getCharge();
	}

	@Override
	public Object fromString(String line) {
		return UserVip.fromString(line);
	}

	public int getMaxLvl() {
		return maxLvl;
	}
}
