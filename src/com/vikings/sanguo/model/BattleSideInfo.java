/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-10 上午10:19:56
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.text.TextUtils;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ArmInfo;
import com.vikings.sanguo.protos.BattleLogReturnInfo;
import com.vikings.sanguo.protos.MoveTroopInfo;
import com.vikings.sanguo.protos.TroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.ListUtil;

public class BattleSideInfo {
	private int mainFighter; // 主战
	private long fiefid; // 玩家所属领地
	private List<MoveTroopInfo> beginTroop; // 战斗开始时的军队
	private List<MoveTroopInfo> endTroop; // 战斗结束时的军队
	private List<Integer> partner; // 参战者userId
	private List<BattleArrayInfoClient> battleArrayInfo; // 军队信息
	private List<BattleLogHeroInfoClient> heroInfos; // 将领信息
	private BattleLossDetailList lossDetail; // 损失
	private int initTotalTroopAmount = 0;
	private int totalDeath = 0; // 总死亡数
	private int totalWound = 0; // 总受伤数
	private BattleLossDetailList deathDetail; // 损失
	private List<UserTroopEffectInfo> userTroopEffectInfos;
	private List<TroopEffectInfo> troopEffectInfos;

	private BattleSkill battleSkill;// 城防技能

	public BattleSkill getBattleSkill() {
		return battleSkill;
	}

	public void setBattleSkill(BattleSkill battleSkill) {
		this.battleSkill = battleSkill;
	}

	// 合并多种军队的增强效果 结果
	private UserTroopEffectInfo userTroopEffectInfo2 = new UserTroopEffectInfo();

	private ArrayList<Integer> amyTypes = new ArrayList<Integer>(); // 兵种的种类

	public UserTroopEffectInfo getUserTroopEffectInfo2() {
		return userTroopEffectInfo2;
	}

	public void setUserTroopEffectInfo2(UserTroopEffectInfo userTroopEffectInfo2) {
		this.userTroopEffectInfo2 = userTroopEffectInfo2;
	}

	// 合并多种军队的增强效果 ---强烈推荐在线程中调用 此方法--必须在具备 军队信息 和 用户兵种强化效果数据时 调用
	public void merge(List<MoveTroopInfo> moveTroopInfos,
			List<UserTroopEffectInfo> userTroopEffectInfos,
			UserTroopEffectInfo userTroopEffectInfo) {
		userTroopEffectInfo.setInfosList(new ArrayList<TroopEffectInfo>());
		if (ListUtil.isNull(amyTypes) || ListUtil.isNull(moveTroopInfos)
				|| ListUtil.isNull(userTroopEffectInfos)
				|| ListUtil.isNull(userTroopEffectInfos.get(0).getInfosList()))
			return;
		// 遍历兵种
		for (int i = 0; i < amyTypes.size(); i++) {
			// 属性遍历
			for (int j = 0; j < BattlePropDefine.propID.length; j++) {
				TroopEffectInfo troopEffectInfo = new TroopEffectInfo();
				troopEffectInfo.setAttr(BattlePropDefine.propID[j]);
				troopEffectInfo.setArmid(amyTypes.get(i));
				// 计算指定兵种 指定属性的加成值 公式
				// (兵种总数*兵种强化+兵种总数1*兵种强化1+...)/(兵种总数+兵种总数1+...)

				int armCount = 0;// 兵种总数 （公式分母部分）
				int strengthenVal = 0;// 分母部分

				for (int k = 0; k < moveTroopInfos.size(); k++) {
					if (moveTroopInfos.get(k).hasBi()
							&& moveTroopInfos.get(k).getBi().hasTroopInfo()
							&& moveTroopInfos.get(k).getBi().getTroopInfo()
									.hasInfos())
						for (int k2 = 0; k2 < moveTroopInfos.get(k).getBi()
								.getTroopInfo().getInfosList().size(); k2++) {
							if (amyTypes.get(i) == moveTroopInfos.get(k)
									.getBi().getTroopInfo().getInfosList()
									.get(k2).getId().intValue()) {
								armCount += moveTroopInfos.get(k).getBi()
										.getTroopInfo().getInfosList().get(k2)
										.getCount();
								for (int l = 0; l < userTroopEffectInfos.size(); l++) {
									if (moveTroopInfos.get(k).getBi()
											.getUserid().intValue() == userTroopEffectInfos
											.get(l).getUserid().intValue()
											&& userTroopEffectInfos.get(l)
													.hasInfos()) {
										for (int l2 = 0; l2 < userTroopEffectInfos
												.get(l).getInfosList().size(); l2++) {
											TroopEffectInfo troopEffectInfo2 = userTroopEffectInfos
													.get(l).getInfosList()
													.get(l2);

											if (troopEffectInfo2.getAttr()
													.intValue() == BattlePropDefine.propID[j]
													&& troopEffectInfo2
															.getArmid()
															.intValue() == amyTypes
															.get(i).intValue()) {
												strengthenVal += troopEffectInfo2
														.getValue()
														* moveTroopInfos.get(k)
																.getBi()
																.getTroopInfo()
																.getInfosList()
																.get(k2)
																.getCount();
											}

										}
										break;
										//
									}
								}

							}
						}

				}

				//
				if (armCount != 0) {
					troopEffectInfo.setValue(strengthenVal / armCount);
					userTroopEffectInfo.getInfosList().add(troopEffectInfo);
				}

			}
		}

	}

	public void setInitTotalTroopAmount(int initTotalTroopAmount) {
		this.initTotalTroopAmount = initTotalTroopAmount;
	}

	public int getInitTotalTroopAmount() {
		return initTotalTroopAmount;
	}

	public void setBattleArrayInfo(List<BattleArrayInfoClient> battleArrayInfo) {
		this.battleArrayInfo = battleArrayInfo;
	}

	public void resetBattleArrayInfo() {
		if (!ListUtil.isNull(battleArrayInfo)) {
			this.battleArrayInfo.clear();
		}
	}

	public List<UserTroopEffectInfo> getUserTroopEffectInfos() {
		return userTroopEffectInfos;
	}

	public void setUserTroopEffectInfos(
			List<UserTroopEffectInfo> userTroopEffectInfos) {
		this.userTroopEffectInfos = userTroopEffectInfos;

		// 强烈推荐在线程中调用 此方法--必须在具备 军队信息 和 用户兵种强化效果数据时 调用
		merge(beginTroop, this.userTroopEffectInfos, userTroopEffectInfo2);
	}

	public void addBattleArrayInfo(BattleArrayInfoClient info) {
		if (null == info)
			return;

		if (null == battleArrayInfo)
			battleArrayInfo = new ArrayList<BattleArrayInfoClient>();

		if (!battleArrayInfo.contains(info))
			battleArrayInfo.add(info);
	}

	public void addBeginTroop(MoveTroopInfo troop) {
		if (null == troop)
			return;

		if (null == beginTroop)
			beginTroop = new ArrayList<MoveTroopInfo>();

		if (!beginTroop.contains(beginTroop))
			beginTroop.add(troop);

		// 兵种类型
		if (troop.getBi().hasTroopInfo()
				&& troop.getBi().getTroopInfo().hasInfos())
			for (int i = 0; i < troop.getBi().getTroopInfo().getInfosList()
					.size(); i++) {
				if (!amyTypes.contains(troop.getBi().getTroopInfo()
						.getInfosList().get(i).getId())) {
					amyTypes.add(troop.getBi().getTroopInfo().getInfosList()
							.get(i).getId());
				}
			}
	}

	public void setEndTroop(List<MoveTroopInfo> list) {
		this.endTroop = list;
	}

	public void setFiefid(final long fiefid) {
		this.fiefid = fiefid;
	}

	public void setHeroInfo(List<BattleLogHeroInfoClient> heroInfos) {
		this.heroInfos = heroInfos;
	}

	public void setMainFighter(Integer mainFighter) {
		this.mainFighter = mainFighter;
	}

	public void setPartner(List<Integer> partner) {
		this.partner = partner;
	}

	public void setLossDetail(BattleLossDetailList lossDetail) {
		this.lossDetail = lossDetail;
	}

	public List<BattleArrayInfoClient> getBattleArrayInfo() {
		return battleArrayInfo;
	}

	public List<MoveTroopInfo> getBeginTroop() {
		return beginTroop;
	}

	public List<MoveTroopInfo> getEndTroop() {
		return endTroop;
	}

	public long getFiefid() {
		return fiefid;
	}

	public List<BattleLogHeroInfoClient> getHeroInfos() {
		return heroInfos;
	}

	public BriefUserInfoClient getMainFighter() {
		BriefUserInfoClient user = null;

		try {
			if (!BriefUserInfoClient.isNPC(mainFighter)) {
				if (Account.user.getId() == mainFighter)
					user = Account.user.bref();
				else
					user = CacheMgr.userCache.get(mainFighter);
			} else
				user = CacheMgr.npcCache.getNpcUser(mainFighter);
		} catch (GameException e) {
			e.printStackTrace();
		}

		return user;
	}

	public int getMainFighterId() {
		return mainFighter;
	}

	public List<Integer> getPartner() {
		return partner;
	}

	public void addPartner(int id) {
		if (null == partner)
			partner = new ArrayList<Integer>();

		if (partner.contains(id))
			return;

		partner.add(id);
	}

	public BattleLossDetailList getLossDetail() {
		if (null == lossDetail)
			lossDetail = new BattleLossDetailList(beginTroop, endTroop);
		return lossDetail;
	}

	public void setDeathDetail(List<BattleLogReturnInfo> ls) {
		if (null == deathDetail)
			deathDetail = new BattleLossDetailList(ls, mainFighter, partner);
	}

	public BattleLossDetailList getDeathDetail() {
		return deathDetail;
	}

	public HashSet<String> getDownloadIcons() throws GameException {

		HashSet<String> icons = new HashSet<String>();

		if (null != heroInfos && !heroInfos.isEmpty()) {
			for (BattleLogHeroInfoClient heroInfo : heroInfos) {
				HeroProp hp = heroInfo.getHeroProp();
				String img = hp.getIcon();
				if (TextUtils.isEmpty(img) == false) {
					icons.add(hp.getIcon()); // 将领头像
				}
			}
		}

		// 军队图片
		if (!ListUtil.isNull(battleArrayInfo)) {
			for (BattleArrayInfoClient it : battleArrayInfo) {
				TroopProp tp = (TroopProp) CacheMgr.troopPropCache.get(it
						.getId());
				if (tp != null) {
					String icon = tp.getIcon();
					if (TextUtils.isEmpty(icon) == false) {
						icons.add(icon);
					}
					String imgUp = tp.getImageUp();
					if (TextUtils.isEmpty(imgUp) == false) {
						icons.add(imgUp);
					}
					String imgDown = tp.getImageDown();
					if (TextUtils.isEmpty(imgDown) == false) {
						icons.add(imgDown);
					}
					String smallIcon = tp.getSmallIcon();
					if (TextUtils.isEmpty(smallIcon) == false) {
						icons.add(smallIcon);
					}
				}
			}
		}
		return icons;
	}

	public int getHeroLvl(long id) {
		int heroLvl = -1;
		if (null != heroInfos && !heroInfos.isEmpty()) {
			for (BattleLogHeroInfoClient heroInfo : heroInfos) {
				if (heroInfo.getId() == id) {
					heroLvl = heroInfo.getLevel();
					break;
				}
			}
		}
		return heroLvl;
	}

	public int getCurrTroopAmount() {
		int cnt = 0;
		if (ListUtil.isNull(battleArrayInfo))
			return cnt;

		for (BattleArrayInfoClient it : battleArrayInfo)
			cnt += it.getNum();
		return cnt;
	}

	public int getTotalTroopAmount() {
		int cnt = 0;
		// if (ListUtil.isNull(battleArrayInfo))
		// return cnt;
		//
		// for (BattleArrayInfoClient it : battleArrayInfo)
		// cnt += it.getNum();

		if (ListUtil.isNull(beginTroop))
			return cnt;

		for (MoveTroopInfo it : beginTroop) {
			List<ArmInfo> arms = it.getBi().getTroopInfo().getInfosList();

			if (!ListUtil.isNull(arms)) {
				for (ArmInfo armInfo : arms) {
					cnt += armInfo.getCount();
				}
			}
		}
		return cnt;
	}

	public BattleArrayInfoClient getCurTroop(int id) {
		if (ListUtil.isNull(battleArrayInfo))
			return null;

		for (BattleArrayInfoClient it : battleArrayInfo) {
			if (it.getId() == id)
				return it;
		}
		return null;
	}

	public boolean isMeMainFighter() {
		return Account.user.getId() == mainFighter;
	}

	public boolean isMePartner() {
		if (ListUtil.isNull(partner))
			return false;

		return partner.contains(Account.user.getId());
	}

	public boolean isPartner(int userId) {
		if (ListUtil.isNull(partner))
			return false;

		return partner.contains(userId);
	}

	public boolean hasNoBeginTroop() {
		return ListUtil.isNull(beginTroop)
				|| (1 == beginTroop.size() && null != beginTroop.get(0)
						&& null != beginTroop.get(0).getBi()
						&& null != beginTroop.get(0).getBi().getTroopInfo() && !beginTroop
						.get(0).getBi().getTroopInfo().hasInfos());
	}

	public boolean isMe() {
		return Account.user.getId() == mainFighter;
	}

	public void setTotalDeath(int totalDeath) {
		this.totalDeath = totalDeath;
	}

	public int getTotalDeath() {
		return totalDeath;
	}

	public void setTotalWound(int totalWound) {
		this.totalWound = totalWound;
	}

	public int getTotalWound() {
		return totalWound;
	}

	public int getTotalLoss() {
		return totalWound + totalDeath;
	}

	public int getWound() {
		if (!getMainFighter().isNPC())
			return getTotalWound();
		else
			return 0;
	}

	public int getDeath() {
		if (!getMainFighter().isNPC())
			return getTotalDeath();
		else
			return getTotalDeath();
	}

	// 设置兵种、数量 、等
	public void setTroopInfo() {
		
	}
}
