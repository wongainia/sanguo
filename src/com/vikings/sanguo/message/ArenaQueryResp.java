package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.ArenaUserRankInfoClient;
import com.vikings.sanguo.protos.ArenaUserRankInfo;
import com.vikings.sanguo.protos.MsgRspArenaQuery;

public class ArenaQueryResp extends BaseResp {
	private List<ArenaUserRankInfoClient> topUsers = new ArrayList<ArenaUserRankInfoClient>();
	private List<ArenaUserRankInfoClient> attackableUsers = new ArrayList<ArenaUserRankInfoClient>();

	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspArenaQuery resp = new MsgRspArenaQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);

		if (resp.hasOtherInfos()) {
			for (ArenaUserRankInfo it : resp.getOtherInfosList()) {
				ArenaUserRankInfoClient auric = ArenaUserRankInfoClient
						.convert(it);
				auric.setCanAttack(false);
				// auric.randomHeroPoistion();
				topUsers.add(auric);
			}
		}

		if (resp.hasAttackableInfos()) {
			for (ArenaUserRankInfo it : resp.getAttackableInfosList()) {
				ArenaUserRankInfoClient auric = ArenaUserRankInfoClient
						.convert(it);
				auric.setCanAttack(true);
				// auric.randomHeroPoistion();
				attackableUsers.add(auric);
			}
		}
	}

	public List<ArenaUserRankInfoClient> getTopUsers() {
		return topUsers;
	}

	public List<ArenaUserRankInfoClient> getAttackableUsers() {
		return attackableUsers;
	}
}
