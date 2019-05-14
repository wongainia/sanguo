package com.vikings.sanguo.model;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleInfo;

public class BattleInfoClient {

	private long attackFiefid; // 攻击方领地id
	private long uniqueId;// 唯一id
	private BaseBattleInfoClient bbic;

	public long getAttackFiefid() {
		return attackFiefid;
	}

	public void setAttackFiefid(long attackFiefid) {
		this.attackFiefid = attackFiefid;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public BaseBattleInfoClient getBbic() {
		return bbic;
	}

	public void setBbic(BaseBattleInfoClient bbic) {
		this.bbic = bbic;
	}

	public static BattleInfoClient convert(BattleInfo info)
			throws GameException {
		if (null == info)
			return null;
		BattleInfoClient bic = new BattleInfoClient();
		if (info.hasCi()) {
			bic.setAttackFiefid(info.getCi().getAttackFiefid());
			bic.setUniqueId(info.getCi().getId());
		}
		bic.setBbic(BaseBattleInfoClient.convert(info.getBi()));
		return bic;
	}
}
