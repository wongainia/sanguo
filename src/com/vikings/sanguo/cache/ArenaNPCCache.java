/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-9 下午5:39:07
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ArenaNPC;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.utils.ListUtil;

public class ArenaNPCCache extends LazyLoadArrayCache {
	public static String NAME = "arena_npc.csv";

	@Override
	public long getSearchKey1(Object obj) {
		return ((ArenaNPC) obj).getLordId();
	}

	@Override
	public long getSearchKey2(Object obj) {
		return ((ArenaNPC) obj).getType();
	}

	@Override
	public Object fromString(String line) {
		return ArenaNPC.fromString(line);
	}

	@Override
	public String getName() {
		return NAME;
	}

	public List<ArmInfoClient> getArms(int userId) {
		List<ArenaNPC> npcs = new ArrayList<ArenaNPC>();
		checkLoad();
		for (Object it : list) {
			ArenaNPC npc = (ArenaNPC) it;
			if (npc.getLordId() == userId && npc.getType() == ArenaNPC.ARM)
				npcs.add(npc);
		}

		List<ArmInfoClient> arms = new ArrayList<ArmInfoClient>();
		if (!ListUtil.isNull(npcs)) {
			for (ArenaNPC it : npcs) {
				try {
					ArmInfoClient ai = new ArmInfoClient(it.getArmId(),
							it.getArmCount());
					arms.add(ai);
				} catch (GameException e) {
					e.printStackTrace();
				}
			}
		}
		return arms;
	}
}
