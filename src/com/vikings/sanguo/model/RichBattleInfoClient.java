package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleAttackType;
import com.vikings.sanguo.protos.ExMoveTroopInfo;
import com.vikings.sanguo.protos.ExUserTroopEffectInfo;
import com.vikings.sanguo.protos.ExUserTroopInfo;
import com.vikings.sanguo.protos.HERO_ROLE;
import com.vikings.sanguo.protos.MoveTroopInfos;
import com.vikings.sanguo.protos.RichBattleInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.protos.UserTroopEffectInfos;
import com.vikings.sanguo.ui.alert.WarEndInfromTip.EndType;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class RichBattleInfoClient {

	private long id; // 战斗battleid
	private BattleInfoClient battleInfo;
	private List<MoveTroopInfoClient> attackTroopInfos; // 进攻方军队(多支购买的神兵部队合并为一支)
	private List<MoveTroopInfoClient> defendTroopInfos; // 防御方军队(多支购买的神兵部队合并为一支)
	private List<UserTroopInfoClient> diffTroopInfos;// 本次战斗军队变化的信息
	private UserTroopEffectInfos userTroopEffectInfos; // 用户军队强化效果

	// 得到防守方的指定用户ID
	public int getDefendArmCountByUserID(int userID) {
		int armCount = 0;
		if (!ListUtil.isNull(defendTroopInfos)) {
			for (MoveTroopInfoClient mtic : defendTroopInfos) {
				if (userID == mtic.getUserid()) {
					for (ArmInfoClient aic : mtic.getTroopInfo()) {
						armCount += aic.getCount();
					}
				}
			}
		}
		return armCount;
	}

	// 是否已经增援过  该部队
		public boolean isReinforce(boolean isAttack) {
			boolean isReinforce = false;
			int userID = Account.user.getId();
			List<MoveTroopInfoClient> moveTroopInfoClients = null;
			if(isAttack)
			{
				moveTroopInfoClients = attackTroopInfos;
			}
			else
			{
				moveTroopInfoClients = defendTroopInfos;
			}
			if (!ListUtil.isNull(moveTroopInfoClients)) {
				for (MoveTroopInfoClient mtic : moveTroopInfoClients) {
					if (userID == mtic.getUserid()) {
						isReinforce = true;
						break;
						}
					}
				}
			
			return isReinforce;
		}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUniqueId() {
		if (null != battleInfo) {
			return battleInfo.getUniqueId();
		} else {
			return id;
		}
	}

	public BattleInfoClient getBattleInfo() {
		return battleInfo;
	}

	public void setBattleInfo(BattleInfoClient battleInfo) {
		this.battleInfo = battleInfo;
	}

	public List<MoveTroopInfoClient> getAttackTroopInfos() {
		return attackTroopInfos == null ? new ArrayList<MoveTroopInfoClient>()
				: attackTroopInfos;
	}

	public void setAttackTroopInfos(List<MoveTroopInfoClient> attackTroopInfos) {
		this.attackTroopInfos = attackTroopInfos;
	}

	public List<MoveTroopInfoClient> getDefendTroopInfos() {
		return defendTroopInfos == null ? new ArrayList<MoveTroopInfoClient>()
				: defendTroopInfos;
	}

	public void setDefendTroopInfos(List<MoveTroopInfoClient> defendTroopInfos) {
		this.defendTroopInfos = defendTroopInfos;
	}

	public List<UserTroopInfoClient> getDiffTroopInfos() {
		return diffTroopInfos == null ? new ArrayList<UserTroopInfoClient>()
				: diffTroopInfos;
	}

	public void setDiffTroopInfos(List<UserTroopInfoClient> diffTroopInfos) {
		this.diffTroopInfos = diffTroopInfos;
	}

	public void setUserTroopEffectInfos(
			UserTroopEffectInfos userTroopEffectInfos) {
		this.userTroopEffectInfos = userTroopEffectInfos;
	}

	public UserTroopEffectInfos getUserTroopEffectInfos() {
		return userTroopEffectInfos;
	}

	public UserTroopEffectInfo getTroopEffectInfo(int userId) {
		if (null == userTroopEffectInfos || !userTroopEffectInfos.hasInfos())
			return null;

		for (ExUserTroopEffectInfo it : userTroopEffectInfos.getInfosList()) {
			if (it.hasInfo() && it.getInfo().getUserid() == userId)
				return it.getInfo();
		}

		return null;
	}

	public UserTroopEffectInfo getAtkUserTroopEffectInfo() {
		return getTroopEffectInfo(battleInfo.getBbic().getAttacker());
	}

	public UserTroopEffectInfo getDefUserTroopEffectInfo() {
		return getTroopEffectInfo(battleInfo.getBbic().getDefender());
	}

	public static RichBattleInfoClient convert(RichBattleInfo info)
			throws GameException {
		if (null == info)
			return null;
		RichBattleInfoClient rbic = new RichBattleInfoClient();
		rbic.setId(info.getId());
		rbic.setBattleInfo(BattleInfoClient.convert(info.getBattleInfo()
				.getInfo()));
		rbic.setAttackTroopInfos(getTroopInfos(info.getAttackTroopInfos()));
		rbic.setDefendTroopInfos(getTroopInfos(info.getDefendTroopInfos()));
		List<UserTroopInfoClient> utics = new ArrayList<UserTroopInfoClient>();
		if (info.hasDiffTroopInfos()) {
			for (ExUserTroopInfo exUserTroopInfo : info.getDiffTroopInfos()
					.getInfosList()) {
				utics.add(UserTroopInfoClient.convert(exUserTroopInfo.getInfo()));
			}
		}
		rbic.setDiffTroopInfos(utics);

		if (info.hasUserTroopEffectInfos())
			rbic.setUserTroopEffectInfos(info.getUserTroopEffectInfos());

		return rbic;
	}

	private static List<MoveTroopInfoClient> getTroopInfos(
			MoveTroopInfos moveTroopInfos) throws GameException {
		List<MoveTroopInfoClient> infos = new ArrayList<MoveTroopInfoClient>();
		if (null != moveTroopInfos && moveTroopInfos.hasInfos()) {
			List<ExMoveTroopInfo> list = moveTroopInfos.getInfosList();
			MoveTroopInfoClient godSoldierInfo = null;
			for (ExMoveTroopInfo exInfo : list) {
				MoveTroopInfoClient info = MoveTroopInfoClient.convert(exInfo
						.getInfo());
				if (null != info) {
					// && (!info.getTroopInfo().isEmpty() || null != info
					// .getHiic())) {
					// 如果是神兵则合并
					if (!info.getTroopInfo().isEmpty()) {
						TroopProp prop = (TroopProp) CacheMgr.troopPropCache
								.get(info.getTroopInfo().get(0).getId());
						if (prop.isGodSoldier()) {
							if (null == godSoldierInfo) {
								godSoldierInfo = info;
								infos.add(godSoldierInfo);
							} else {
								godSoldierInfo
										.getTroopInfo()
										.get(0)
										.setCount(
												godSoldierInfo.getTroopInfo()
														.get(0).getCount()
														+ info.getTroopInfo()
																.get(0)
																.getCount());
							}
						} else {
							infos.add(info);
						}
					} else {
						infos.add(info);
					}

				}
			}
		}
		return infos;
	}

	public void addGodSoldier(MoveTroopInfoClient info) {
		if (null == info || info.getTroopInfo().isEmpty())
			return;
		MoveTroopInfoClient moveTroopInfo = null;
		if (isAttacker()) {
			moveTroopInfo = getGodSoldier(attackTroopInfos);
			if (moveTroopInfo == null) {
				attackTroopInfos.add(info);
			} else {
				addGodSoldierWhenHasGodTroop(info, moveTroopInfo);
			}
		} else if (isDefender()) {
			moveTroopInfo = getGodSoldier(defendTroopInfos);
			if (moveTroopInfo == null) {
				defendTroopInfos.add(info);
			} else {
				addGodSoldierWhenHasGodTroop(info, moveTroopInfo);
			}
		}
	}

	private void addGodSoldierWhenHasGodTroop(MoveTroopInfoClient info,
			MoveTroopInfoClient moveTroopInfo) {
		if (moveTroopInfo.getTroopInfo().isEmpty()) {
			moveTroopInfo.setTroopInfo(info.getTroopInfo());
		} else {
			moveTroopInfo
					.getTroopInfo()
					.get(0)
					.setCount(
							moveTroopInfo.getTroopInfo().get(0).getCount()
									+ info.getTroopInfo().get(0).getCount());
		}
	}

	private MoveTroopInfoClient getGodSoldier(List<MoveTroopInfoClient> list) {
		if (list == null || list.isEmpty())
			return null;
		for (MoveTroopInfoClient info : list) {
			if (!info.getTroopInfo().isEmpty()) {
				TroopProp prop = info.getTroopInfo().get(0).getProp();
				if (prop != null && prop.isGodSoldier())
					return info;
			}
		}
		return null;
	}

	/**
	 * @param rbic
	 * @param ohics
	 *            战场中的将领信息
	 */
	public void update(RichBattleInfoClient rbic,
			List<OtherHeroInfoClient> ohics) {
		if (null == rbic || id != rbic.getId())
			return;
		setBattleInfo(rbic.getBattleInfo());
		setAttackTroopInfos(rbic.getAttackTroopInfos());
		setDefendTroopInfos(rbic.getDefendTroopInfos());
		setDiffTroopInfos(rbic.getDiffTroopInfos());
		fillBattleHeroInfos(battleInfo.getBbic().getAttackHeroInfos(), ohics);
		fillBattleHeroInfos(battleInfo.getBbic().getDefendHeroInfos(), ohics);
	}

	private void fillBattleHeroInfos(List<BattleHeroInfoClient> heroInfos,
			List<OtherHeroInfoClient> ohics) {
		if (null == ohics || null == heroInfos || ohics.isEmpty()
				|| heroInfos.isEmpty())
			return;
		for (BattleHeroInfoClient heroInfo : heroInfos) {
			for (OtherHeroInfoClient ohic : ohics) {
				if (heroInfo.getId() == ohic.getId()) {
					heroInfo.setHeroInfo(ohic);
					break;
				}
			}
		}
	}

	// 自己是攻击方
	public boolean isAttacker() {
		return Account.user.getId() == battleInfo.getBbic().getAttacker();
	}

	// 自己是防守方
	public boolean isDefender() {
		return Account.user.getId() == battleInfo.getBbic().getDefender();
	}

	public boolean isAttackerById(int useId) {
		if (useId == 0) {
			return false;
		}
		if (battleInfo.getBbic().getAttacker() == useId) {
			return true;
		} else {
			return false;
		}
	}

	// 自己是第三方
	public boolean isThirdPart() {
		return !isAttacker() && !isDefender();
	}

	public int getAttackArmTotalCount() {
		return getMainForcesCount(attackTroopInfos)
				+ getReinforcesCount(attackTroopInfos);
	}

	public int getDefendArmTotalCount() {
		return getMainForcesCount(defendTroopInfos)
				+ getReinforcesCount(defendTroopInfos);
	}

	public long getAttackArmTotalHP() {
		return getMainForcesHP(attackTroopInfos)
				+ getReinforcesHP(attackTroopInfos);
	}

	public long getDefendArmTotalHP() {
		return getMainForcesHP(defendTroopInfos)
				+ getReinforcesHP(defendTroopInfos);
	}

	// 主站部队数量
	public int getMainForcesCount(List<MoveTroopInfoClient> list) {
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isMainForce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	// 主站部队血量
	public long getMainForcesHP(List<MoveTroopInfoClient> list) {
		long count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isMainForce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount() * amrInfo.getProp().getHp();
					}
				}

			}
		}
		return count;
	}

	// 攻方部队主攻数量
	public int getAtkMainForcesCount() {
		List<MoveTroopInfoClient> list = attackTroopInfos;
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isMainForce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	// 攻方部队援军数量
	public int getAtkReinForcesCount() {
		List<MoveTroopInfoClient> list = attackTroopInfos;
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	// 守方部队主攻数量
	public int getDefMainForcesCount() {
		List<MoveTroopInfoClient> list = defendTroopInfos;
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isMainForce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	// 守方部队援军数量
	public int getDefReinForcesCount() {
		List<MoveTroopInfoClient> list = defendTroopInfos;
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	public List<MoveTroopInfoClient> getReinforces(
			List<MoveTroopInfoClient> list) {
		List<MoveTroopInfoClient> infos = new ArrayList<MoveTroopInfoClient>();
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					infos.add(info);
				}

			}
		}
		return infos;
	}

	// 增援部队数量
	public int getReinforcesCount(List<MoveTroopInfoClient> list) {
		int count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient armInfo : info.getTroopInfo()) {
						count += armInfo.getCount();
					}
				}

			}
		}
		return count;
	}

	public long getReinforcesHP(List<MoveTroopInfoClient> list) {
		long count = 0;
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient amrInfo : info.getTroopInfo()) {
						count += amrInfo.getCount() * amrInfo.getProp().getHp();
					}
				}
			}
		}
		return count;
	}

	public String getGodSoldierName(List<MoveTroopInfoClient> list) {
		String name = "";
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient armInfo : info.getTroopInfo()) {
						if (null != armInfo.getProp()
								&& armInfo.getProp().isGodSoldier()) {
							return armInfo.getProp().getName();
						}
					}
				}
			}
		}
		return name;
	}

	// 攻守双方的总HP
	public double getTotalHp(int base) {
		double total = getHp(attackTroopInfos) + getHp(defendTroopInfos);
		return total < base ? base : total;
	}

	private double getHp(List<MoveTroopInfoClient> infos) {
		int hp = 0;
		if (null != infos && !infos.isEmpty()) {
			for (MoveTroopInfoClient info : infos) {
				for (ArmInfoClient armInfo : info.getTroopInfo()) {
					if (null != armInfo.getProp()) {
						double armHp = armInfo.getProp().getHp();
						if (armHp >= 2)
							armHp = armHp / 2;
						hp += armHp * armInfo.getCount();
					}
				}
			}
		}
		return hp;
	}

	public boolean hasHero(boolean isAtk) {
		if (null != battleInfo && null != battleInfo.getBbic()) {
			if (isAtk) {
				return battleInfo.getBbic().getAttackHeroInfos().size() > 0;
			} else {
				return battleInfo.getBbic().getDefendHeroInfos().size() > 0;
			}
		}

		return false;
	}

	public List<BattleHeroInfoClient> getHeros(boolean isAtk) {
		if (null != battleInfo && null != battleInfo.getBbic()) {
			if (isAtk) {
				return battleInfo.getBbic().getAttackHeroInfos();
			} else {
				return battleInfo.getBbic().getDefendHeroInfos();
			}
		} else {
			return new ArrayList<BattleHeroInfoClient>();
		}
	}

	public boolean hasHeroSkill(boolean isAtk) {
		if (hasHero(isAtk)) {
			List<BattleHeroInfoClient> bhics = getHeros(isAtk);
			for (BattleHeroInfoClient bhic : bhics) {
				List<HeroSkillSlotInfoClient> hssics = bhic.getHeroInfo()
						.getSkillSlotInfos();
				if (null != hssics && hssics.size() > 0)
					return true;
			}
		}
		return false;
	}

	public boolean isPVP() {
		return !BriefUserInfoClient.isNPC(battleInfo.getBbic().getAttacker())
				&& !BriefUserInfoClient.isNPC(battleInfo.getBbic()
						.getDefender());
	}

	// 判断是否有主攻
	public boolean hasMainAttacker() {
		List<MoveTroopInfoClient> troops = getAttackTroopInfos();

		if (ListUtil.isNull(troops))
			return false;

		for (MoveTroopInfoClient it : troops) {
			if (it.isMainForce())
				return true;
		}

		return false;
	}

	public boolean hasMainDefender() {
		List<MoveTroopInfoClient> troops = getDefendTroopInfos();

		if (ListUtil.isNull(troops))
			return false;

		for (MoveTroopInfoClient it : troops) {
			if (it.isMainForce())
				return true;
		}

		return false;
	}

	public boolean canReinforceAttack() {
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();

		BriefUserInfoClient attackerUser = battleInfo.getBbic()
				.getAttackerUser();
		if (null != attackerUser
				&& !BriefUserInfoClient.isNPC(attackerUser.getId().intValue())) {
			// 攻击者是家族成员 领主非家族成员 可以援助进攻
			if (g != null && g.isMember(attackerUser.getId().intValue())
					&& !g.isMember(battleInfo.getBbic().getDefender()))
				return true;
			// 攻击者是同国 并且攻击非同国领地 可以援助进攻
			if (attackerUser.getCountry().intValue() == Account.user
					.getCountry().intValue()
					&& !BriefUserInfoClient.isNPC(battleInfo.getBbic()
							.getDefender())
					&& (battleInfo.getBbic().getDefenderUser().getCountry() != Account.user
							.getCountry()))
				return true;
		}

		return false;
	}

	public boolean canReinforceDefend() {
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();

		BriefUserInfoClient defenderUser = battleInfo.getBbic()
				.getDefenderUser();
		if (null != defenderUser
				&& !BriefUserInfoClient.isNPC(defenderUser.getId().intValue())) {
			if (g != null && g.isMember(defenderUser.getId().intValue())
					&& !g.isMember(battleInfo.getBbic().getAttacker()))
				return true;
			if (defenderUser.getCountry().intValue() == Account.user
					.getCountry().intValue()
					&& !BriefUserInfoClient.isNPC(battleInfo.getBbic()
							.getAttacker())
					&& battleInfo.getBbic().getAttackerUser().getCountry()
							.intValue() != Account.user.getCountry().intValue())
				return true;
		}
		return false;
	}

	//圣都
	public boolean canReinforceAttackInShenJi() {
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();
		BriefUserInfoClient attackerUser = battleInfo.getBbic()
				.getAttackerUser();
		HolyProp hpHolyProp = null;
		try
		{
			hpHolyProp	=	(HolyProp) CacheMgr.holyPropCache.get(id);
		} 
		catch (GameException e)
		{
			e.printStackTrace();
		}
		if (null != attackerUser
				&& !BriefUserInfoClient.isNPC(attackerUser.getId().intValue())) {
			// 攻击者是家族成员 领主非家族成员 可以援助进攻
			if (g != null && g.isMember(attackerUser.getId().intValue())
					&& !g.isMember(battleInfo.getBbic().getDefender()) 
					&& hpHolyProp != null && hpHolyProp.getProp().getReinforceType() == 2)
				return true;
			// 攻击者是同国 并且攻击非同国领地 可以援助进攻
			if (attackerUser.getCountry().intValue() == Account.user
					.getCountry().intValue()
					&& (battleInfo.getBbic().getDefenderUser().getCountry() != Account.user
							.getCountry()) && hpHolyProp != null&& hpHolyProp.getProp().getReinforceType() == 1)
				return true;
			// 增援自己
			if (attackerUser.getId() == Account.user.getId()) {
				return true;
			}
		}

		return false;
	}

	public boolean canReinforceDefendInShenJi() {
		RichGuildInfoClient g = Account.guildCache.getRichInfoInCache();
		BriefUserInfoClient defenderUser = battleInfo.getBbic()
				.getDefenderUser();
		HolyProp hpHolyProp = null;
		try
		{
			hpHolyProp	=	(HolyProp) CacheMgr.holyPropCache.get(id);
		} 
		catch (GameException e)
		{
			e.printStackTrace();
		}
		
		if (null != defenderUser) {
			if (g != null && g.isMember(defenderUser.getId().intValue())
					&& !g.isMember(battleInfo.getBbic().getAttacker())
					&& hpHolyProp != null && hpHolyProp.getProp().getReinforceType() == 2)
				return true;
			if (defenderUser.getCountry().intValue() == Account.user
					.getCountry().intValue()
					&& battleInfo.getBbic().getAttackerUser().getCountry()
							.intValue() != Account.user.getCountry().intValue()
							&& hpHolyProp != null&& hpHolyProp.getProp().getReinforceType() == 1)
				return true;
			// 增援自己
			if (defenderUser.getId() == Account.user.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> getDefendUsers() {
		List<Integer> userIds = new ArrayList<Integer>();
		for (MoveTroopInfoClient mtic : defendTroopInfos) {
			if (mtic.getUserid() != Account.user.getId()
					&& !userIds.contains(mtic.getUserid())) {
				userIds.add(mtic.getUserid());
			}
		}
		return userIds;
	}

	public String getBattleTypeName() {
		if (null == battleInfo)
			return "";

		if (null == battleInfo.getBbic())
			return "";

		return battleInfo.getBbic().getTypeName() + "战";
	}

	// 计算自动开战时间
	public int getAutoBattleTime() {
		int state = getBattleInfo().getBbic().getState();
		int s = 0;

		if (BattleStatus.BATTLE_STATE_SURROUND == state) {
			int time = CacheMgr.dictCache.getDictInt(Dict.TYPE_BATTLE_COST, 31);// 配置的超时时间

			if (CacheMgr.holyPropCache.isHoly(id)) {
				FiefScale fiefScale = getBattleInfo().getBbic().getFiefScale();
				if (null != fiefScale && fiefScale instanceof HolyProp) {
					HolyProp prop = (HolyProp) fiefScale;
					if (null != prop)
						time = prop.getTime();
				}
			}
			s = getBattleInfo().getBbic().getTime() + time
					- (int) (Config.serverTime() / 1000);

		} else if (BattleStatus.BATTLE_STATE_SURROUND_END == state) {
			s = getBattleInfo().getBbic().getTime()
					- (int) (Config.serverTime() / 1000);
		}

		return s;
	}

	public BaseBattleInfoClient getBbic() {
		if (null != battleInfo)
			return battleInfo.getBbic();

		return null;
	}

	public int getCurState() {
		if (null != getBbic())
			return getBbic().getCurState();

		return BattleStatus.BATTLE_STATE_NONE;
	}

	public int getTime() {
		if (null != getBbic())
			return getBbic().getTime();
		return 0;
	}

	public int timeToNextState() {
		if (0 == getTime())
			return 0;

		return getTime() - Config.serverTimeSS();
	}

	public String getCountDownDesc() {
		if (null != getBbic()) {
			int curState = getBbic().getCurState();

			switch (curState) {
			case BattleStatus.BATTLE_STATE_SURROUND:
				return StringUtil.color(DateUtil._formatTime(timeToNextState())
						+ "后围城结束", R.color.color24);
			case BattleStatus.BATTLE_STATE_SURROUND_END:
				int autoBattleTime = getAutoBattleTime();
				if (autoBattleTime > 0) {
					return StringUtil.color(
							DateUtil._formatTime(autoBattleTime) + "后自动开战",
							R.color.color24);
				} else
					return StringUtil.color("战争已自动开启", R.color.color24);
			case BattleStatus.BATTLE_STATE_FINISH:
				return StringUtil.color("战争已自动开启", R.color.color24);
			default:
				return "";
			}
		}

		return "";
	}

	public String getRedCountDownDesc() {
		if (null != getBbic()) {
			int curState = getBbic().getCurState();

			switch (curState) {
			case BattleStatus.BATTLE_STATE_SURROUND:
				return StringUtil.color(DateUtil._formatTime(timeToNextState())
						+ "后围城结束", R.color.color24);
			case BattleStatus.BATTLE_STATE_SURROUND_END:
				int autoBattleTime = getAutoBattleTime();
				if (autoBattleTime > 0) {
					return StringUtil.color(
							DateUtil._formatTime(autoBattleTime) + "后自动开战",
							R.color.color24);
				} else
					return StringUtil.color("战争已自动开启", R.color.color24);
			case BattleStatus.BATTLE_STATE_FINISH:
				return StringUtil.color("战争已自动开启", R.color.color24);
			default:
				return "";
			}
		}

		return "";
	}

	// pvp的攻方有进攻锁定
	public boolean hasAtkLockTime() {
		return BattleStatus.isInSurround(getCurState()) && isPVP()
				&& isAttacker();
	}

	public boolean isMassacreOrDuelSurround() {
		return (BattleStatus.isInSurround(getCurState()) && (getBattleInfo()
				.getBbic().getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK.number || getBattleInfo()
				.getBbic().getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK.number));
	}

	public boolean isSurroundEnd() {
		return BattleStatus.isInSurroundEnd(getCurState()) && !isThirdPart();
	}

	public List<BattleHeroInfoClient> getAttackHeroInfos() {
		if (null != getBbic())
			return getBbic().getAttackHeroInfos();
		return new ArrayList<BattleHeroInfoClient>();
	}

	public List<BattleHeroInfoClient> getDefendHeroInfos() {
		if (null != getBbic())
			return getBbic().getDefendHeroInfos();
		return new ArrayList<BattleHeroInfoClient>();
	}

	public int getDefendHeroAbility() {
		int ability = 0;
		List<BattleHeroInfoClient> bhic = getDefendHeroInfos();
		if (!ListUtil.isNull(bhic)) {
			for (BattleHeroInfoClient battleHeroInfoClient : bhic) {
				if (battleHeroInfoClient.getHeroInfo().getId() != 0) {
					ability += battleHeroInfoClient.getHeroInfo()
							.getHeroAbility();
				}
			}
		}
		return ability;
	}

	public int getAttackHeroAbility() {
		int ability = 0;
		List<BattleHeroInfoClient> bhic = getAttackHeroInfos();
		if (!ListUtil.isNull(bhic)) {
			for (BattleHeroInfoClient battleHeroInfoClient : bhic) {
				if (battleHeroInfoClient.getHeroInfo().getId() != 0) {
					ability += battleHeroInfoClient.getHeroInfo()
							.getHeroAbility();
				}
			}
		}
		return ability;
	}

	public BattleHeroInfoClient getMainHeroInfo(List<BattleHeroInfoClient> bhic) {
		for (BattleHeroInfoClient battleHeroInfoClient : bhic) {
			// 只取主将
			if (battleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_ATTACK_MAIN
					.getNumber()
					|| battleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_DEFEND_MAIN
							.getNumber()) {
				return battleHeroInfoClient;
			}
		}
		return null;
	}

	public List<HeroSkillSlotInfoClient> getAtkHeroSkills() {
		return getHeroSkills(getAttackHeroInfos());
	}

	public List<HeroSkillSlotInfoClient> getDefHeroSkills() {
		return getHeroSkills(getDefendHeroInfos());
	}

	private List<HeroSkillSlotInfoClient> getHeroSkills(
			List<BattleHeroInfoClient> bhic) {
		List<HeroSkillSlotInfoClient> hssics = new ArrayList<HeroSkillSlotInfoClient>();
		if (ListUtil.isNull(bhic))
			return hssics;
		for (BattleHeroInfoClient battleHeroInfoClient : bhic) {
			// 只取主将
			if (battleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_ATTACK_MAIN
					.getNumber()
					|| battleHeroInfoClient.getRole() == HERO_ROLE.HERO_ROLE_DEFEND_MAIN
							.getNumber()) {
				if (!ListUtil.isNull(battleHeroInfoClient.getHeroInfo()
						.getSkillSlotInfos())) {
					hssics.addAll(battleHeroInfoClient.getHeroInfo()
							.getSkillSlotInfos());
				}
			}
		}

		// 取组合技
		SkillCombo skillCombo = (SkillCombo) CacheMgr.battleSkillCombo
				.getSkillComboByHeroIds(bhic);
		if (skillCombo != null) {
			try {
				HeroSkillSlotInfoClient heroSkillSlotInfoClient = new HeroSkillSlotInfoClient(
						skillCombo.getId());
				hssics.add(heroSkillSlotInfoClient);
			} catch (GameException e) {
				e.printStackTrace();
			}
		}

		// 去掉重复的
		for (int i = 0; i < hssics.size() - 1; i++) {
			for (int j = hssics.size() - 1; j > i; j--) {
				if (hssics.get(j).getSkillId() == hssics.get(i).getSkillId()) {
					hssics.remove(j);
				}
			}
		}

		return hssics;
	}

	public List<OtherHeroArmPropInfoClient> getAtkHeroArmProp() {
		List<OtherHeroArmPropInfoClient> otherHeroArmPropInfoClients = new ArrayList<OtherHeroArmPropInfoClient>();
		if (ListUtil.isNull(getAttackHeroInfos()))
			return otherHeroArmPropInfoClients;

		for (BattleHeroInfoClient battleHeroInfoClient : getAttackHeroInfos()) {
			if (battleHeroInfoClient.getHeroInfo() != null) {
				return battleHeroInfoClient.getHeroInfo().getArmPropInfos();
			}
		}
		return otherHeroArmPropInfoClients;
	}

	public List<OtherHeroArmPropInfoClient> getDefHeroArmProp() {
		List<OtherHeroArmPropInfoClient> otherHeroArmPropInfoClients = new ArrayList<OtherHeroArmPropInfoClient>();
		if (ListUtil.isNull(getDefendHeroInfos()))
			return otherHeroArmPropInfoClients;

		for (BattleHeroInfoClient battleHeroInfoClient : getDefendHeroInfos()) {
			if (battleHeroInfoClient.getHeroInfo() != null) {
				return battleHeroInfoClient.getHeroInfo().getArmPropInfos();
			}
		}
		return otherHeroArmPropInfoClients;
	}

	private boolean isOpenEvilDoor() {
		if (null == getBbic())
			return false;

		return CacheMgr.holyPropCache.isHoly(getBbic().getDefendFiefid())
				&& BriefUserInfoClient.isNPC(getBbic().getAttacker());
	}

	private boolean isMoreThanMaxReinforceUser(int reinforceUserId) {
		if (null == getBbic())
			return false;

		if (!CacheMgr.holyPropCache.isHoly(getBbic().getDefendFiefid()))
			return false;

		HolyProp holyProp = (HolyProp) getBbic().getFiefScale();
		if (null == holyProp)
			return false;

		List<Integer> defendUserIds = getDefendUsers();
		if (defendUserIds.contains(reinforceUserId)) {
			return false;
		} else {
			if (defendUserIds.size() < holyProp.getMaxReinforceUser())
				return false;
			else
				return true;
		}
	}

	public boolean isReinforceEvilDoorUserFull(int reinforceUserId) {
		if (isOpenEvilDoor() && isMoreThanMaxReinforceUser(reinforceUserId)) {
			HolyProp holyProp = (HolyProp) getBbic().getFiefScale();
			Config.getController().alert("援助失败", "本场恶魔之门的战斗人数已经满了",
					"该领地恶魔之门最多允许" + holyProp.getMaxReinforceUser() + "名玩家增援",
					null, false);
			return true;
		}
		return false;
	}

	public EndType getEndType() {
		if (isAttacker())
			return EndType.sally;
		else if (isDefender())
			return EndType.attack;
		else
			return EndType.ohter;
	}

	public boolean isDuelOrMassacre() {
		return getBattleInfo().getBbic().getType() == BattleAttackType.E_BATTLE_DUEL_ATTACK
				.getNumber()
				|| getBattleInfo().getBbic().getType() == BattleAttackType.E_BATTLE_MASSACRE_ATTACK
						.getNumber();
	}

	public boolean isReinforce(int userId) {
		return (isParticipate(userId, attackTroopInfos) || isParticipate(
				userId, defendTroopInfos))
				&& userId != battleInfo.getBbic().getAttacker()
				&& userId != battleInfo.getBbic().getDefender();
	}

	private boolean isParticipate(int userId, List<MoveTroopInfoClient> ls) {
		if (ListUtil.isNull(ls))
			return false;

		for (MoveTroopInfoClient it : ls) {
			if (it.getUserid() == userId)
				return true;
		}

		return false;
	}

	public void mergeMoveTroopInfoClient(MoveTroopInfoClient mtic) {
		if (mtic.isAttack()) {
			mergeMoveTroopInfoClient(attackTroopInfos, mtic);
		} else if (mtic.isDefend()) {
			mergeMoveTroopInfoClient(defendTroopInfos, mtic);
		}
	}

	private void mergeMoveTroopInfoClient(List<MoveTroopInfoClient> list,
			MoveTroopInfoClient mtic) {
		for (MoveTroopInfoClient it : list) {
			if (mtic.getUserid() == it.getUserid()) {
				if (it.getId() == mtic.getId()) {
					ListUtil.mergeArmInfo(mtic.getTroopInfo(),
							it.getTroopInfo());
					return;
				}
			}
		}

		list.add(mtic);
		return;
	}

	public String getGoldSoldierCount() {
		return isAttacker() ? getGodSoldierCount(getAttackTroopInfos())
				: getGodSoldierCount(getDefendTroopInfos());
	}

	// 神兵数量
	public String getGodSoldierCount(List<MoveTroopInfoClient> list) {
		int count = 0;
		String name = "";
		if (null != list && !list.isEmpty()) {
			for (MoveTroopInfoClient info : list) {
				if (info.isReinforce()) {
					for (ArmInfoClient armInfo : info.getTroopInfo()) {
						if (null != armInfo.getProp()
								&& armInfo.getProp().isGodSoldier()) {
							count += armInfo.getCount();
							if (StringUtil.isNull(name))
								name = armInfo.getProp().getName();
						}
					}
				}
			}
		}
		return count + "名" + name;
	}

	public PokerPrice getNextPokerPrice() {
		int size = getBattleInfo().getBbic().getPokerResult().size() + 1; // +
																			// 2;
		if (size > CacheMgr.pokerPriceCache.getSize())
			return null;

		try {
			return (PokerPrice) CacheMgr.pokerPriceCache.get(size); // == 0 ? 1
																	// : size
		} catch (GameException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int getNextPokerNeedCurrency(PokerPrice pp) {
		if (pp != null) {
			int totalHp = getBattleInfo().getBbic().getPokerUnit();
			double realHp = totalHp < Constants.GOD_SOLDIER_BASE_HP ? Constants.GOD_SOLDIER_BASE_HP
					: totalHp;
			return (int) (((long) pp.getPrice() * realHp) / Constants.GOD_SOLDIER_BASE_HP);
		}
		return 0;
	}
}
