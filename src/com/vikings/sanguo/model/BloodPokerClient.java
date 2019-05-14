package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.KeyValue;

//血战奖励翻牌数据结构
public class BloodPokerClient {
	private int scheme;// 方案
	private int open;// 是否翻开

	private BloodReward bloodReward;

	public BloodPokerClient(int scheme) throws GameException {
		this.scheme = scheme;
//		if (scheme > 0)
//			bloodReward = (BloodReward) CacheMgr.bloodRewardCache.get(scheme);
	}

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open == 1;
	}

	public BloodReward getBloodReward() {
		return bloodReward;
	}

	public void setBloodReward(BloodReward bloodReward) {
		this.bloodReward = bloodReward;
	}

	public int getCount(int record, boolean loss) {
		float count = 0;
		if (null != bloodReward)
			count = 1f * bloodReward.getRewardCount()
					* bloodReward.getAddRate() * record / 100;

		// if (loss) {
		// count = count / 2;
		// }

		if (count < 1)
			count = 1;

		return (int) count;
	}

	public void update(BloodPokerClient client) {
		if (null == client)
			return;
		setScheme(client.getScheme());
		setOpen(client.getOpen());
		setBloodReward(client.getBloodReward());
	}

	public static List<BloodPokerClient> convert2List(List<KeyValue> list)
			throws GameException {
		List<BloodPokerClient> bpcs = new ArrayList<BloodPokerClient>();
		if (null != list && !list.isEmpty()) {
			for (KeyValue keyValue : list) {
				bpcs.add(convert(keyValue));
			}
		}
		return bpcs;
	}

	public static BloodPokerClient convert(KeyValue kv) throws GameException {
		if (kv == null)
			return null;
		BloodPokerClient bpc = new BloodPokerClient(kv.getKey());
		bpc.setOpen(kv.getValue());
		return bpc;
	}
}
