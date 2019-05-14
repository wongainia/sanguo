package com.vikings.sanguo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BaseMoveTroopInfo;
import com.vikings.sanguo.utils.ListUtil;

/**
 * 调动军队信息
 * 
 * @author susong
 * 
 */
public class MoveTroopInfoClient implements Comparable<MoveTroopInfoClient>,
		Serializable {
	private static final long serialVersionUID = -1902260026267480227L;

	public static final int TROOP_ROLE_ATTACK_MAIN_FORCE = 1; // 进攻方主力
	public static final int TROOP_ROLE_ATTACE_REINFORCE = 2; // 进攻方援兵
	public static final int TROOP_ROLE_DEFEND_MAIN_FORCE = 3; // 防守方主战
	public static final int TROOP_ROLE_DEFEND_REINFORCE = 4; // 防守方援兵

	private long id; // 调动批次id
	private int role; // 类型 1：主战部队，2：援军
	private int userid; // 所属领主
	private List<ArmInfoClient> troopInfo;

	private transient BriefUserInfoClient user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public List<ArmInfoClient> getTroopInfo() {
		return troopInfo == null ? new ArrayList<ArmInfoClient>() : troopInfo;
	}

	public void setTroopInfo(List<ArmInfoClient> troopInfo) {
		this.troopInfo = troopInfo;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public boolean isMainForce() {
		return role == TROOP_ROLE_ATTACK_MAIN_FORCE
				|| role == TROOP_ROLE_DEFEND_MAIN_FORCE;
	}

	public boolean isReinforce() {
		return role == TROOP_ROLE_ATTACE_REINFORCE
				|| role == TROOP_ROLE_DEFEND_REINFORCE;
	}

	public boolean isAttack() {
		return role == TROOP_ROLE_ATTACK_MAIN_FORCE
				|| role == TROOP_ROLE_ATTACE_REINFORCE;
	}

	public boolean isDefend() {
		return role == TROOP_ROLE_DEFEND_MAIN_FORCE
				|| role == TROOP_ROLE_DEFEND_REINFORCE;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (o instanceof MoveTroopInfoClient) {
			return id == ((MoveTroopInfoClient) o).getId();
		}
		return false;
	}

	public static MoveTroopInfoClient convert(
			com.vikings.sanguo.protos.MoveTroopInfo moverInfo)
			throws GameException {
		MoveTroopInfoClient info = new MoveTroopInfoClient();
		BaseMoveTroopInfo bi = moverInfo.getBi();
		info.setId(bi.getId());
		info.setRole(bi.getRole());
		info.setUserid(bi.getUserid());
		if (bi.hasTroopInfo()) {
			info.setTroopInfo(ArmInfoClient.convertList(bi.getTroopInfo()));
		} else {
			info.setTroopInfo(new ArrayList<ArmInfoClient>());
		}
		return info;
	}

	public static List<MoveTroopInfoClient> convertList(
			List<com.vikings.sanguo.protos.MoveTroopInfo> infos)
			throws GameException {
		List<MoveTroopInfoClient> list = new ArrayList<MoveTroopInfoClient>();
		if (null == infos || infos.isEmpty())
			return list;
		for (com.vikings.sanguo.protos.MoveTroopInfo info : infos) {
			list.add(convert(info));
		}
		return list;
	}

	@Override
	public int compareTo(MoveTroopInfoClient another) {
		return this.getRole() - another.getRole();
	}

	public static List<MoveTroopInfoClient> merge(
			final List<MoveTroopInfoClient> ls) {
		List<MoveTroopInfoClient> mtics = new ArrayList<MoveTroopInfoClient>();

		if (ListUtil.isNull(ls))
			return mtics;

		Set<Integer> userIds = new HashSet<Integer>();
		for (MoveTroopInfoClient mtic : ls)
			userIds.add(mtic.getUserid());

		for (Integer it : userIds) {
			MoveTroopInfoClient troopInfo = new MoveTroopInfoClient();
			for (MoveTroopInfoClient mtic : ls) {
				if (mtic.getUserid() == it.intValue())
					troopInfo.merge(mtic);
			}
			mtics.add(troopInfo);
		}

		return mtics;
	}

	public void merge(MoveTroopInfoClient mtic) {
		if (0 == getId())
			setId(mtic.getId());
		if (0 == getRole() || 2 == getRole())
			setRole(mtic.getRole());
		setUserid(mtic.getUserid());
		if (ListUtil.isNull(getTroopInfo()))
			setTroopInfo(new ArrayList<ArmInfoClient>());

		if (!ListUtil.isNull(mtic.getTroopInfo())) {
			if (ListUtil.isNull(getTroopInfo())) {
				for (ArmInfoClient ai : mtic.getTroopInfo()) {
					ArmInfoClient armInfo = ai.copy();
					getTroopInfo().add(armInfo);
				}
			} else {
				for (ArmInfoClient otherAi : mtic.getTroopInfo()) {
					boolean isFound = false;
					for (ArmInfoClient ai : getTroopInfo()) {
						if (ai.getId() == otherAi.getId()) {
							ai.addCount(otherAi.getCount());
							isFound = true;
							break;
						}
					}

					if (!isFound)
						getTroopInfo().add(otherAi.copy());
				}
			}
		}
	}

	public String getTotalTroopAmount() {
		if (ListUtil.isNull(troopInfo))
			return "";

		StringBuilder buf = new StringBuilder();
		for (ArmInfoClient it : troopInfo)
			buf.append(it.getCount()).append("名")
					.append(it.getProp().getName()).append(",");

		if (buf.length() > 0)
			buf.deleteCharAt(buf.length() - 1);

		return buf.toString();
	}

	public MoveTroopInfoClient copy() {
		MoveTroopInfoClient mtic = new MoveTroopInfoClient();
		mtic.setId(this.id);
		mtic.setRole(this.role);
		mtic.setUserid(this.userid);
		mtic.setUser(this.user);

		List<ArmInfoClient> troopInfo = new ArrayList<ArmInfoClient>();
		for (ArmInfoClient it : this.troopInfo)
			troopInfo.add(it.copy());

		mtic.setTroopInfo(troopInfo);

		return mtic;
	}
}
