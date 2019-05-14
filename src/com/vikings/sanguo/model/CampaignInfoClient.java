package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.protos.CampaignTimeCondition;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

/**
 * 战役数据
 * 
 * @author chenqing
 * 
 */
@SuppressWarnings("unchecked")
public class CampaignInfoClient {

	private static final int RATE_MAX = 10000;

	public static final int BONUS_FIX = 1;
	public static final int BONUS_RATE = 3;
	public static final int CAMPAIGN_HELL_EVIL = 11061;
	public static final int CAMPAIGN_1_4 = 100104;

	private int id; // 战役ID
	private int times; // 通关次数
	private CampaignTimeCondition lastCondition; // 最近一次时间条件的记录
	private int optimes;// 操作次数

	private PropCampaign propCampaign;
	private PropCampaignMode propCampaignMode;
	private List<TimeCondition> timeCondition;

	public CampaignInfoClient(int id) throws GameException {
		this.id = id;
		propCampaign = (PropCampaign) CacheMgr.campaignCache.get(id);
		propCampaignMode = (PropCampaignMode) CacheMgr.campaignModeCache
				.get(id);
		if (!propCampaignMode.openForever())
			timeCondition = CacheMgr.timeConditionCache.search(propCampaignMode
					.getOpenSolutionId());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public CampaignTimeCondition getLastCondition() {
		return lastCondition;
	}

	public void setLastCondition(CampaignTimeCondition lastCondition) {
		this.lastCondition = lastCondition;
	}

	public int getOptimes() {
		return optimes;
	}

	public void setOptimes(int optimes) {
		this.optimes = optimes;
	}

	public PropCampaignMode getPropCampaignMode() {
		return propCampaignMode;
	}

	public void setPropCampaignMode(PropCampaignMode propCampaignMode) {
		this.propCampaignMode = propCampaignMode;
	}

	public List<TimeCondition> getTimeCondition() {
		return timeCondition;
	}

	public void setTimeCondition(List<TimeCondition> timeCondition) {
		this.timeCondition = timeCondition;
	}

	public void setPropCampaign(PropCampaign propCampaign) {
		this.propCampaign = propCampaign;
	}

	public PropCampaign getPropCampaign() {
		return propCampaign;
	}

	// 副本难度
	public int getDifficultyImgResId() {
		try {
			PropAct act = (PropAct) CacheMgr.actCache.get(propCampaign
					.getActId());
			return act.getDifficultyImgResId();
		} catch (GameException e) {
			Log.e("CampaignInfoClient", e.getErrorMsg());
		}
		return 0;
	}

	/**
	 * 敌军将领信息，没有为空，第0个为主将
	 * 
	 * @return
	 */
	public OtherHeroInfoClient[] getEnemyHeros() {
		OtherHeroInfoClient[] ohics = new OtherHeroInfoClient[PropCampaignMode.MAX_HERO_COUNT];
		if (null != propCampaignMode) {
			ohics[0] = getHero(propCampaignMode.getFoeHeroId1());
			ohics[1] = getHero(propCampaignMode.getFoeHeroId2());
			ohics[2] = getHero(propCampaignMode.getFoeHeroId3());
		}
		return ohics;
	}

	private OtherHeroInfoClient getHero(int sid) {
		if (sid == 0)
			return null;
		try {
			return CacheMgr.heroInitCache.getOtherHeroInfoClient(sid, 0, sid);
		} catch (GameException e) {
			Log.e("CampaignInfoClient", e.getErrorMsg());
			return null;
		}
	}

	// 取敌方主将
	public OtherHeroInfoClient getMainHero() {
		if (null != propCampaignMode) {
			try {
				return CacheMgr.heroInitCache.getOtherHeroInfoClient(0, 0,
						propCampaignMode.getFoeHeroId1());
			} catch (GameException e) {
				Log.e("CampaignInfoClient", e.getErrorMsg());
			}
		}
		return null;
	}

	/**
	 * 得到副本敌方兵力
	 * 
	 * @return
	 */
	public List<ArmInfoClient> getEnemyArms() {
		ArrayList<ArmInfoClient> rs = new ArrayList<ArmInfoClient>();
		findTroop(getEnemyTroopId(), rs);
		return rs;
	}

	public int getEnemyArmsCount() {
		List<ArmInfoClient> ls = getEnemyArms();
		int cnt = 0;
		for (ArmInfoClient it : ls)
			cnt += it.getCount();

		return cnt;
	}

	/**
	 * 得到副本赠送兵的方案
	 * 
	 * @return
	 */
	public ArrayList<ArmInfoClient> getSelfArms() {
		ArrayList<ArmInfoClient> rs = new ArrayList<ArmInfoClient>();
		int sid = getMyTroopId();
		findTroop(sid, rs);
		return rs;
	}

	private void findTroop(int sid, ArrayList<ArmInfoClient> rs) {
		if (sid == 0)
			return;
		List<CampaignTroop> ls = CacheMgr.campaignTroopCache.search(sid);
		for (CampaignTroop c : ls) {
			try {
				rs.add(new ArmInfoClient(c.getTroopId(), c.getTroopAmt()));
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

	// 是否完成过
	public boolean isComplete() {
		return times > 0;
	}

	public int[] getCompleteCnt() {
		if (0 != getOpenSolutionId())
			return getTimeLimit();
		return new int[] { 0, 0 };
	}

	// 是否开启
	public boolean canOpen() {
		return Account.user.getLevel() >= propCampaign.getOpenLevel();
	}

	// 判断等级
	private String checkLevel() {
		if (Account.user.getLevel() < propCampaign.getOpenLevel())
			return "等级(" + propCampaign.getOpenLevel() + ")开放";
		else
			return null;
	}

	// 判断前置关卡
	private String checkPreCampaign() {
		if (propCampaignMode.getPreCampaignId() == 0)
			return null;
		ActInfoClient act = Account.actInfoCache
				.getAct(propCampaign.getActId());
		if (act == null) // 该状态应该是数据有问题，此处做兼容处理
			return null;
		CampaignInfoClient cic = act.getCampaign(propCampaignMode
				.getPreCampaignId());
		if (cic == null) // 该状态应该是数据有问题，此处做兼容处理
			return null;

		if (!cic.isComplete()) // i + 1
			return "抱歉,您需要先通关战役\"" + cic.getPropCampaign().getName() + "\"";

		return null;
	}

	// vip对副本总次数无影响，只影响每段时间内通关次数
	public String checkLimit(int extra) {
		// int[] total = getTotalLimit();
		// int cnt = total[1];
		// if (total[0] >= cnt && total[1] != 0)
		// return "本战役通关次数已耗尽,请重置";
		int[] time = getTimeLimit();
		int cnt = time[1] + extra;
		if (time[0] >= cnt && time[1] != 0)
			return "抱歉,副本通关次数已耗尽,请重置";
		return null;
	}

	/**
	 * 得到时段内限次 if p[1]==0 表示不限次
	 * 
	 * @return
	 */
	public int[] getTimeLimit() {
		int[] p = new int[] { 0, 0 };
		if (propCampaignMode.getOpenSolutionId() == 0)
			return p;
		List<TimeCondition> list = CacheMgr.timeConditionCache
				.search(propCampaignMode.getOpenSolutionId());
		if (list.isEmpty())
			return p;
		int now = DateUtil.getTodaySS();
		for (TimeCondition t : list) {
			if (t.getOpenTime() <= now && now <= t.getCloseTime()) {
				p[1] = t.getTimes();
				break;
			}
		}

		if (propCampaign.isNormalCampaign()) {
			UserVip curVip = Account.getCurVip();
			if (null != curVip) {
				p[1] = p[1] + curVip.getExActTimes();
			}
		}

		if (null != lastCondition) {
			int today = getTodayServerFormat();
			if (lastCondition.getBt() <= now && now <= lastCondition.getEt()
					&& today == lastCondition.getDay())
				p[0] += lastCondition.getTimes();
		}
		return p;
	}

	private void addErrorMsg(String s, ArrayList<String> ls) {
		if (s == null)
			return;
		ls.add(s);
	}

	/**
	 * 判断是否可以开战，false 界面显示灰化，但是可以点击，点击出现提示信息
	 * 
	 * @return 错误提示信息 先盘但 list是不是size 0 0表示可以打， 有数据就直接提示
	 */
	public ArrayList<String> checkOpen() {
		ArrayList<String> rs = new ArrayList<String>();
		addErrorMsg(checkLevel(), rs);
		addErrorMsg(checkPreCampaign(), rs);
		return rs;
	}

	/**
	 * 判断副本是否开放 在开放时间段内 不再时段的界面显示开放时间 ，不能点进详情
	 * 
	 * @param difficulty
	 * @return
	 */

	public boolean isOpen() {
		// 用于活动副本,临时方案，先只判断一个难度

		if (getOpenSolutionId() == 0) // propCampaign.getOpenSolutionId()
			return true;
		List<TimeCondition> list = CacheMgr.timeConditionCache
				.search(getOpenSolutionId()); // propCampaign.getOpenSolutionId()
		int now = DateUtil.getTodaySS();
		for (TimeCondition t : list) {
			if (t.getOpenTime() <= now && now <= t.getCloseTime())
				return true;
		}
		return false;
	}

	/**
	 * 得到下次开启时间间隔 秒数
	 * 
	 * @return
	 */
	public int getNextOpenTime() {
		// 用于活动副本,临时方案，先只判断一个难度
		if (getOpenSolutionId() == 0) // propCampaign.getOpenSolutionId()
			return 0;
		List<TimeCondition> list = CacheMgr.timeConditionCache
				.search(getOpenSolutionId()); // propCampaign.getOpenSolutionId()
		if (list.size() == 0)
			return 0;
		int now = DateUtil.getTodaySS();
		for (TimeCondition t : list) {
			if (t.getOpenTime() > now)
				return t.getOpenTime() - now;
		}
		return list.get(0).getOpenTime() + 24 * 60 * 60 - now;
	}

	/**
	 * 得到下次关闭时间间隔 秒数
	 * 
	 * @return
	 */
	public int getNextCloseTime() {
		// 用于活动副本,临时方案，先只判断一个难度
		if (getOpenSolutionId() == 0)
			return 0;
		List<TimeCondition> list = CacheMgr.timeConditionCache
				.search(getOpenSolutionId());
		if (list.size() == 0)
			return 0;
		int now = DateUtil.getTodaySS();
		for (TimeCondition t : list) {
			if (t.getCloseTime() > now)
				return t.getCloseTime() - now;
		}
		return 0;
	}

	public String getDesc() {
		return propCampaign.getDesc();
	}

	public String getImg() {
		return propCampaign.getIcon();
	}

	public String getTitle() {
		return propCampaign.getName();
	}

	/**
	 * 判断是否我方进攻
	 * 
	 * @param difficulty
	 * @return
	 */
	public boolean isMeAttack() {
		return propCampaignMode.getPlayerState() == 2;
	}

	private int getMyTroopId() {
		return propCampaignMode.getOwnTroopSchemeId();
	}

	private int getEnemyTroopId() {
		return propCampaignMode.getFoeTroopSchemeId();
	}

	/**
	 * 根据奖励方案 ,奖励类型 填充returninfo
	 * 
	 * @param sid
	 * @param rc
	 */
	private void findBonus(int sid, int type, ReturnInfoClient rc) {
		if (sid == 0)
			return;
		List<PropCampaignSpoils> ls = CacheMgr.campaignSpoilsCache
				.searchByIdAndType(sid, type);
		for (PropCampaignSpoils c : ls) {
			rc.addCfg(c.getSpoilType(), c.getSpoilId(), c.getSpoilAmt());

		}
	}

	private List<PropCampaignBoss> getBossScheme() {
		ArrayList<PropCampaignBoss> rs = new ArrayList<PropCampaignBoss>();
		int sid = propCampaignMode.getBossId();
		if (sid == 0)
			return rs;
		List<PropCampaignBoss> ls = CacheMgr.campaignBossCache.search(sid);
		return ls;
	}

	public ArrayList<PropCampaignBoss> getSpecifiedBossScheme() {
		ArrayList<PropCampaignBoss> rs = new ArrayList<PropCampaignBoss>();
		for (PropCampaignBoss b : getBossScheme()) {
			if (RATE_MAX == b.getRate())
				rs.add(b);
		}
		return rs;
	}

	public ArrayList<PropCampaignBoss> getRandomBossScheme() {
		ArrayList<PropCampaignBoss> rs = new ArrayList<PropCampaignBoss>();
		for (PropCampaignBoss b : getBossScheme()) {
			if (RATE_MAX > b.getRate())
				rs.add(b);
		}
		return rs;
	}

	public ArrayList<ArmInfoClient> getSpecifiedBoss() {
		ArrayList<ArmInfoClient> rs = new ArrayList<ArmInfoClient>();
		ArrayList<PropCampaignBoss> sBoss = getSpecifiedBossScheme();
		for (PropCampaignBoss b : sBoss) {
			try {
				rs.add(new ArmInfoClient(b.getBossId(), 1));
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	public ArrayList<ArmInfoClient> getRandomBoss() {
		ArrayList<ArmInfoClient> rs = new ArrayList<ArmInfoClient>();
		ArrayList<PropCampaignBoss> rBoss = getRandomBossScheme();
		for (PropCampaignBoss b : rBoss) {
			try {
				rs.add(new ArmInfoClient(b.getBossId(), 1));
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * 获取奖励信息 tpye CampaignClient.BONUS_FIX CampaignClient.BONUS_RATE
	 * 
	 * @param difficulty
	 * @param type
	 * @return
	 */
	public ReturnInfoClient getBonus(int type) {
		ReturnInfoClient rc = new ReturnInfoClient();
		int sid = 0;
		if (times > 0)
			sid = propCampaignMode.getPassSpoil();
		else
			sid = propCampaignMode.getFirstPassSpoil();
		findBonus(sid, type, rc);
		// // 添加boss奖励

		// 随机boss奖励
		ArrayList<PropCampaignBoss> rBoss = getRandomBossScheme();
		if (!ListUtil.isNull(rBoss)) {
			CampaignSpoilsAppend csa = (CampaignSpoilsAppend) CacheMgr.campaignSpoilsAppendCache
					.search(id, type);
			if (null != csa)
				rc.addCfg(csa.getSpoilType(), csa.getSpoilId(), csa.getCount());
		}

		// 固定boss奖励
		ArrayList<PropCampaignBoss> sBoss = getSpecifiedBossScheme();
		if (!ListUtil.isNull(sBoss)) {
			for (PropCampaignBoss boss : sBoss) {
				if (times > 1)
					sid = boss.getFirtPassSpoilId();
				else
					sid = boss.getPassSpoilId();
				findBonus(sid, type, rc);
			}
		}
		return rc;
	}

	/**
	 * 更新服务器数据, 战役开战后 也应该更新这里
	 * 
	 * @param cp
	 */
	public void updateData(CampaignInfo cp) {
		this.times = cp.getTimes();
		this.lastCondition = cp.getLastCondition();
		this.optimes = cp.getOptimes();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampaignInfoClient other = (CampaignInfoClient) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private static int getTodayServerFormat() {
		// gmt + 8 时区
		return (int) ((Config.serverTime() + 8 * 3600 * 1000) / (24 * 3600 * 1000));
	}

	public boolean needRefreshLeftTimes() {
		return false;
	}

	public int getEnemyHeroCnt() {
		return propCampaignMode.getMaxEnemyBoss();
	}

	// 背景
	public String getBgName() {
		String name = "battle_map.jpg";
		if (null != propCampaignMode
				&& !StringUtil.isNull(propCampaignMode.getBackground())) {
			name = propCampaignMode.getBackground();
		}
		return name;
	}

	// 城墙
	public String getWall() {
		String wall = "";
		if (propCampaignMode.getPlayerState() == 2
				&& !StringUtil.isNull(propCampaignMode.getBattleWallDown())) {
			wall = propCampaignMode.getBattleWallDown();
		} else if (null != propCampaignMode
				&& !StringUtil.isNull(propCampaignMode.getBackground())) {
			wall = propCampaignMode.getBattleWallup();
		}
		return wall;
	}

	public int getOpenSolutionId() {
		if (null != propCampaignMode)
			return propCampaignMode.getOpenSolutionId();
		else
			return 0;
	}

	public static int getTypeImg(int type) {
		switch (type) {
		case CampaignInfoClient.BONUS_FIX:
			return R.drawable.pass_award;// "通关奖励";
		case CampaignInfoClient.BONUS_RATE:
			return R.drawable.extra_award;// "意外奖励";
		default:
			return 0;// "奖励";
		}
	}

	public boolean isLocked(ActInfoClient actClient) {
		return !ListUtil.isNull(checkOpen()) || actClient.isFreshAct();
	}
}
