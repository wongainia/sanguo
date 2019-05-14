/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-3-8 下午3:19:32
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.protos.BattleLogReturnInfo;
import com.vikings.sanguo.protos.MoveTroopInfo;
import com.vikings.sanguo.protos.TroopInfo;
import com.vikings.sanguo.utils.ListUtil;

public class BattleLossDetailList implements Comparator<MoveTroopInfo> {
	private List<MoveTroopInfo> beginTroop;
	private List<MoveTroopInfo> endTroop;
	private List<BattleLossDetail> lossDetailLs = new ArrayList<BattleLossDetail>();

	public BattleLossDetailList(List<MoveTroopInfo> beginTroop,
			List<MoveTroopInfo> endTroop) { // BattleLogInfo battleLogInfo
		this.beginTroop = beginTroop;
		this.endTroop = endTroop;

		// 获取详细损失列表
		List<BattleLossDetail> bldLs = getDetailLoss(this.beginTroop,
				this.endTroop);
		mergeLossDetail(bldLs);

		if (!ListUtil.isNull(bldLs))
			lossDetailLs.addAll(bldLs);

		BattleLossDetail total = getTotalLoss();

		// if (hasHelpArm(this.beginTroop))
		if (lossDetailLs.size() > 1)
			lossDetailLs.add(0, total);
		else {
			lossDetailLs.clear();
			if (null != total)
				lossDetailLs.add(total);
		}
	}

	public BattleLossDetailList(List<BattleLogReturnInfo> ls, final int mainId,
			List<Integer> partner) {
		for (BattleLogReturnInfo it : ls) {
			BattleLossDetail bld = new BattleLossDetail();
			bld = bld.toBattleLossDetail(it, mainId, partner);
			if (null != bld)
				lossDetailLs.add(bld);
		}

		if (!ListUtil.isNull(lossDetailLs)) {
			Collections.sort(lossDetailLs, new Comparator<BattleLossDetail>() {
				@Override
				public int compare(BattleLossDetail lhs, BattleLossDetail rhs) {
					if (lhs.getUserId() == mainId && rhs.getUserId() != mainId)
						return -1;
					else if (lhs.getUserId() != mainId
							&& rhs.getUserId() == mainId)
						return 1;
					else
						return 0;
				}
			});

			mergeLossDetail(lossDetailLs);
			BattleLossDetail total = getTotalLoss();
			if (null != total)
				lossDetailLs.add(0, total);
		}
	}

	// 将损失列表按userId合并
	private void mergeLossDetail(List<BattleLossDetail> bldLs) {
		if (ListUtil.isNull(bldLs))
			return;

		Iterator<BattleLossDetail> it = bldLs.iterator();
		// 经过排序，第一个数据一定是主战信息
		BattleLossDetail first = it.next();
		while (it.hasNext()) {
			BattleLossDetail tmp = it.next();
			// 合并兵种
			if (first.getUserId() == tmp.getUserId()) {
				for (TroopMoveInfoClient tmpIt : tmp.getArmInfoLs()) {
					// 遍历，如果有相同兵种则合并，如果没有则增加
					boolean isFind = false;
					for (TroopMoveInfoClient firstIt : first.getArmInfoLs()) {
						if (firstIt.getArmProp().getId() == tmpIt.getArmProp()
								.getId()) {
							firstIt.setCount(firstIt.getCount()
									+ tmpIt.getCount());
							isFind = true;
						}
					}
					if (!isFind)
						first.getArmInfoLs().add(tmpIt);
				}
				it.remove();
			}
		}
	}

	// 是否有援军，如果没有，则不用做详细解析，begin必须被排序
	private boolean hasHelpArm(List<MoveTroopInfo> begin) {
		// 排序
		Collections.sort(begin, this);

		if (ListUtil.isNull(begin))
			return false;

		MoveTroopInfo first = begin.get(0);

		for (int i = 1; i < begin.size(); i++) {
			MoveTroopInfo mti = begin.get(i);

			if (!isMoveTroopInfoValid(first) || !isMoveTroopInfoValid(mti))
				continue;

			if (first.getBi().getUserid() != mti.getBi().getUserid())
				return true;
		}

		return false;
	}

	private List<BattleLossDetail> getDetailLoss(List<MoveTroopInfo> begin,
			List<MoveTroopInfo> end) {
		List<BattleLossDetail> bldLs = new ArrayList<BattleLossDetail>();
		Collections.sort(begin, this);
		for (MoveTroopInfo itBegin : begin) {
			MoveTroopInfo leftTroop = getLeftTroop(itBegin, end);
			if (null != leftTroop) {
				List<TroopMoveInfoClient> aicLs = getArmInfoClientLs(itBegin,
						leftTroop);
				BattleLossDetail bld = ArmInfoClient2BattleLossDetail(itBegin,
						aicLs);
				if (null != bld)
					bldLs.add(bld);
			} else {
				continue;
			}
		}
		return bldLs;
	}

	private BattleLossDetail ArmInfoClient2BattleLossDetail(
			MoveTroopInfo begin, List<TroopMoveInfoClient> aicLs) {
		if (null == aicLs || 0 == aicLs.size() || !isMoveTroopInfoValid(begin))
			return null;

		// 设置BattleLossDetail
		BattleLossDetail bld = new BattleLossDetail();

		int r = begin.getBi().getRole();
		// 攻方主力或者守方主力
		String role = (1 == r || 3 == r) ? Config.getController().getString(
				R.string.the_main_force) : Config.getController().getString(
				R.string.reinforcements);
		bld.setRole(role);
		bld.setUserId(begin.getBi().getUserid());
		// bld.setTime(begin.getBi().getTime());
		bld.setArmInfoLs(aicLs);

		return bld;
	}

	private List<TroopMoveInfoClient> getArmInfoClientLs(MoveTroopInfo begin,
			MoveTroopInfo end) {
		List<TroopMoveInfoClient> aicLs = new ArrayList<TroopMoveInfoClient>();

		if (!isMoveTroopInfoValid(begin) || !isMoveTroopInfoValid(end))
			return aicLs;

		List<ArmInfo> beginInfo = getArmInfoLs(begin);
		List<ArmInfo> endInfo = getArmInfoLs(end);

		if (null == beginInfo)
			return null;

		for (ArmInfo beginIt : beginInfo) {
			TroopMoveInfoClient aic = null;
			// 没有损失才计算
			if (null != endInfo) {
				// 计算损失
				boolean isFind = false;
				for (ArmInfo endIt : endInfo) {
					if (beginIt.getId().equals(endIt.getId())) {
						int cnt = beginIt.getCount() - endIt.getCount();
						if (0 != cnt) {
							aic = new TroopMoveInfoClient(beginIt.getId(), cnt,
									begin.getBi().getRole());
						}
						isFind = true;
						break;
					}

				}

				// 结束部队中没有某支部队，表示该部队全灭
				if (!isFind) {
					aic = new TroopMoveInfoClient(beginIt.getId(),
							beginIt.getCount(), begin.getBi().getRole());
				}

				if (null != aic)
					aicLs.add(aic);
			}
		}

		return aicLs;
	}

	// 获取剩余部队
	private MoveTroopInfo getLeftTroop(MoveTroopInfo mti, List<MoveTroopInfo> ls) {
		if (ListUtil.isNull(ls) || !isMoveTroopInfoValid(mti))
			return null;

		for (MoveTroopInfo it : ls) {
			if (!isMoveTroopInfoValid(it))
				continue;

			if (mti.getBi().getRole().equals(it.getBi().getRole())
					&& mti.getBi().getId().equals(it.getBi().getId()))
				// && mti.getBi().getTime().equals(it.getBi().getTime()))
				return it;
		}

		return null;
	}

	// 计算总损失
	private BattleLossDetail getTotalLoss() {
		if (ListUtil.isNull(lossDetailLs))
			return null;

		BattleLossDetail bld = new BattleLossDetail();
		bld.setName(null);
		bld.setTime(-1);
		bld.setUserId(-1);

		List<TroopMoveInfoClient> armInfoLs = new ArrayList<TroopMoveInfoClient>();
		for (BattleLossDetail it : lossDetailLs) {
			List<TroopMoveInfoClient> aicLs = it.getArmInfoLs();
			armInfoLs.addAll(aicLs);
		}

		if (ListUtil.isNull(armInfoLs))
			return null;

		// 按兵种Id对armInfoLs排序
		Collections.sort(armInfoLs, new Comparator<TroopMoveInfoClient>() {
			@Override
			public int compare(TroopMoveInfoClient lhs, TroopMoveInfoClient rhs) {
				if (null == lhs || null == rhs)
					return 0;
				if (null == lhs.getArmProp() || null == rhs.getArmProp())
					return 0;
				return lhs.getId() - rhs.getId();
			}
		});

		List<TroopMoveInfoClient> totalAicLs = new ArrayList<TroopMoveInfoClient>();
		if (1 == armInfoLs.size()) {
			bld.setArmInfoLs(armInfoLs);
			return bld;
		}

		TroopMoveInfoClient first = armInfoLs.get(0).copy();
		totalAicLs.add(first);

		for (int i = 1; i < armInfoLs.size(); i++) {
			TroopMoveInfoClient aic = armInfoLs.get(i);
			if (aic.getId() == first.getId())
				first.addCount(aic.getCount());
			else {
				first = aic.copy();
				totalAicLs.add(first);
			}
			// ArmInfoClient last = totalAicLs.get(totalAicLs.size() - 1);
			// if (aic.getId() == last.getId())
			// last.addCount(aic.getCount());
			// else
			// totalAicLs.add(aic);
		}
		bld.setArmInfoLs(totalAicLs);
		return bld;
	}

	// 获取armInfo
	private List<ArmInfo> getArmInfoLs(MoveTroopInfo mti) {
		TroopInfo atkTi = mti.getBi().getTroopInfo();
		return atkTi.getInfosList();
	}

	public List<BattleLossDetail> getLossDetailLs() {
		return lossDetailLs;
	}

	public int getLossDetailLines() { // boolean isAtk
		int cnt = 0;
		for (BattleLossDetail it : lossDetailLs)
			cnt += it.size();
		return cnt;
	}

	@Override
	public int compare(MoveTroopInfo lhs, MoveTroopInfo rhs) {
		if (!isMoveTroopInfoValid(lhs) || !isMoveTroopInfoValid(rhs))
			return 0;

		if (lhs.getBi().getRole() != rhs.getBi().getRole())
			return lhs.getBi().getRole() - rhs.getBi().getRole();
		else
			return 0;
		// return lhs.getBi().getTime() - rhs.getBi().getTime();
	}

	// 判断MoveTroopInfo是否有角色和时间
	private boolean isMoveTroopInfoValid(MoveTroopInfo mti) {
		return (null != mti) && mti.hasBi() && mti.getBi().hasRole()
		// && mti.getBi().hasTime()
				&& mti.getBi().hasUserid();
	}
}
