/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-8 下午3:54:38
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleLogReturnInfo;
import com.vikings.sanguo.protos.ReturnThingInfo;
import com.vikings.sanguo.protos.ThingType;
import com.vikings.sanguo.utils.ListUtil;

public class BattleLossDetail {
	private String name;
	private int userId;
	private int time;
	private String role;
	private List<TroopMoveInfoClient> armInfoLs;

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setArmInfoLs(List<TroopMoveInfoClient> armInfoLs) {
		this.armInfoLs = armInfoLs;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public int getUserId() {
		return userId;
	}

	public List<TroopMoveInfoClient> getArmInfoLs() {
		return armInfoLs;
	}

	public int getTime() {
		return time;
	}

	public int getTotalLoss() {
		if (ListUtil.isNull(armInfoLs))
			return 0;

		int cnt = 0;
		for (TroopMoveInfoClient it : armInfoLs)
			cnt += it.getCount();

		return cnt;
	}

	public int size() {
		if (ListUtil.isNull(armInfoLs))
			return 0;

		if (isTotal())
			return 1 + armInfoLs.size();
		return 2 + armInfoLs.size();
	}

	public boolean isTotal() {
		return (null == name && -1 == time && -1 == userId);
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public List<ArmInfoClient> toArmInfoList() {
		List<ArmInfoClient> ls = new ArrayList<ArmInfoClient>();
		for (TroopMoveInfoClient it : armInfoLs) {
			try {
				ArmInfoClient ai = new ArmInfoClient(it.getId(), it.getCount());
				ls.add(ai);
			} catch (GameException e) {
				e.printStackTrace();
			}

		}
		return ls;
	}

	// mainId:主攻id
	public BattleLossDetail toBattleLossDetail(BattleLogReturnInfo blri,
			int mainId, List<Integer> assist) {
		if (null == blri || null == blri.getInfo())
			return null;

		if (ListUtil.isNull(blri.getInfo().getRtisList()))
			return null;

		boolean hasDead = false;
		boolean hasWound = false;
		for (ReturnThingInfo it : blri.getInfo().getRtisList()) {
			if (ThingType.THING_TYPE_DEAD.getNumber() == it.getType())
				hasDead = true;
			else if (ThingType.THING_TYPE_WOUNDED.getNumber() == it.getType())
				hasWound = true;
		}

		int userId = blri.getInfo().getUserid();

		// npc无死无伤时，没有损失
		if (BriefUserInfoClient.isNPC(userId) && (!hasDead && !hasWound))
			return null;

		// 非npc，无死则没有损失
		if (!BriefUserInfoClient.isNPC(userId) && !hasDead)
			return null;

		if ((userId == mainId || (!ListUtil.isNull(assist))
				&& assist.contains(userId))) {
			BattleLossDetail bld = new BattleLossDetail();

			bld.userId = userId;
			if (bld.userId == mainId)
				bld.role = "主力";
			else if (assist.contains(bld.userId))
				bld.role = "援军";

			bld.armInfoLs = new ArrayList<TroopMoveInfoClient>();

			for (ReturnThingInfo it : blri.getInfo().getRtisList()) {
				if (ThingType.THING_TYPE_DEAD.getNumber() == it.getType()) {
					TroopMoveInfoClient aic = new TroopMoveInfoClient();
					aic.init(it.getThingid(), it.getCount());
					bld.armInfoLs.add(aic);
				}
			}

			if (BriefUserInfoClient.isNPC(userId)) {
				for (ReturnThingInfo it : blri.getInfo().getRtisList()) {
					if (ThingType.THING_TYPE_WOUNDED.getNumber() == it
							.getType()) {
						boolean hasDeath = false;

						for (TroopMoveInfoClient aicIt : bld.armInfoLs) {
							if (aicIt.getId() == it.getThingid()) {
								aicIt.addCount(it.getCount());
								hasDeath = true;
								break;
							}
						}

						if (!hasDeath) {
							TroopMoveInfoClient aic = new TroopMoveInfoClient();
							aic.init(it.getThingid(), it.getCount());
							bld.armInfoLs.add(aic);
						}
					}
				}
			}

			return bld;
		}

		return null;
	}
}
