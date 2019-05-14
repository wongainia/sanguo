package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.protos.TroopInfo;

/**
 * 兵种信息
 * 
 * @author susong
 * 
 */
public class ArmInfoClient implements Serializable {

	private static final long serialVersionUID = 2148042329978831277L;

	public static final int RANDOM_BOSS = -1;

	private int id; // 兵种id
	private int count; // 兵种数量

	private TroopProp prop;

	private ArmInfoClient() {
	}

	public ArmInfoClient(int id, int count) throws GameException {
		this.id = id;
		this.count = count;
		if (this.id != RANDOM_BOSS) {
			this.prop = (TroopProp) CacheMgr.troopPropCache.get(id);
		}
	}

	public boolean isRandomBoss() {
		return this.id == RANDOM_BOSS;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addCount(int count) {
		this.count += count;
	}

	public TroopProp getProp() {
		return prop;
	}

	public void setProp(TroopProp prop) {
		this.prop = prop;
	}

	public static ArmInfoClient convert(ArmInfo armInfo) throws GameException {
		// 临时处理王磊的掉落乱掉的数据
		if (armInfo.getId() < 100)
			return null;

		ArmInfoClient info = new ArmInfoClient();
		info.setId(armInfo.getId());
		info.setCount(armInfo.getCount());
		info.setProp((TroopProp) CacheMgr.troopPropCache.get(armInfo.getId()));
		return info;
	}

	public static List<ArmInfoClient> convertList(TroopInfo ti)
			throws GameException {
		List<ArmInfoClient> arms = new ArrayList<ArmInfoClient>();
		if (ti != null) {
			List<ArmInfo> armInfos = ti.getInfosList();
			for (ArmInfo armInfo : armInfos) {
				ArmInfoClient ai = ArmInfoClient.convert(armInfo);
				if (ai != null)
					arms.add(ai);
			}
		}
		return arms;
	}

	// 过滤掉数量为0的
	public static List<ArmInfoClient> convertTidyList(TroopInfo ti)
			throws GameException {
		List<ArmInfoClient> arms = new ArrayList<ArmInfoClient>();
		if (ti != null) {
			List<ArmInfo> armInfos = ti.getInfosList();
			for (ArmInfo armInfo : armInfos) {
				if (armInfo.getCount() > 0) {
					ArmInfoClient ai = ArmInfoClient.convert(armInfo);
					if (ai != null)
						arms.add(ai);
				}
			}
		}
		return arms;
	}

	public static List<ArmInfo> conver2SerList(List<ArmInfoClient> troops) {
		List<ArmInfo> armInfos = new ArrayList<ArmInfo>();
		if (null != troops && !troops.isEmpty()) {
			for (ArmInfoClient info : troops) {
				if (info.getCount() <= 0)
					continue;
				ArmInfo armInfo = new ArmInfo();
				armInfo.setId(info.getId());
				armInfo.setCount(info.getCount());
				armInfos.add(armInfo);
			}
		}
		return armInfos;
	}

	public static ArmInfoClient convert(ReturnThingInfo info) {
		ArmInfoClient armInfo = new ArmInfoClient();
		armInfo.setId(info.getThingid());
		armInfo.setCount(info.getCount());
		return armInfo;
	}

	public ArmInfoClient copy() {
		ArmInfoClient armInfo = new ArmInfoClient();
		armInfo.setId(id);
		armInfo.setCount(count);
		armInfo.setProp(prop);
		return armInfo;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;

		if (!(o instanceof ArmInfoClient))
			return false;

		ArmInfoClient ai = (ArmInfoClient) o;
		return ai.getId() == this.id && ai.getCount() == this.count;
	}
}
