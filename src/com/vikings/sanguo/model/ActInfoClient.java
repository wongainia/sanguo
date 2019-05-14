package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ActInfo;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

@SuppressWarnings("unchecked")
public class ActInfoClient {
	public final static int GUILD_FIRST_ACT_ID = 1001;
	private int id; // 章节ID
	private int flag = 0; // 全通奖励是否获取
	private ArrayList<CampaignInfoClient> campaignList;// 战役列表
	private int clearTimes;// 扫荡次数
	private int clearStamina;// 扫荡需要的行动力
	private int lastResetTime;// 上次重置时间

	private PropAct propAct;
	private List<PropActSpoils> propActSpoils;

	public ActInfoClient(int id, ArrayList<CampaignInfoClient> campaigns)
			throws GameException {
		this.propAct = (PropAct) CacheMgr.actCache.get(id);
		this.id = propAct.getId();
		this.propActSpoils = (List<PropActSpoils>) CacheMgr.actSpoilsCache
				.search(id);
		campaignList = new ArrayList<CampaignInfoClient>();
		for (CampaignInfoClient c : campaigns) {
			if (c.getPropCampaign().getActId() == id) {
				campaignList.add(c);
				if (null != c.getPropCampaignMode()) {
					clearStamina += c.getPropCampaignMode()
							.getUserStaminaCost();
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getClearTimes() {
		return clearTimes;
	}

	public void setClearTimes(int clearTimes) {
		this.clearTimes = clearTimes;
	}

	public int getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(int lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public PropAct getPropAct() {
		return propAct;
	}

	public void setPropAct(PropAct propAct) {
		this.propAct = propAct;
	}

	public List<PropActSpoils> getPropActSpoils() {
		return propActSpoils;
	}

	public void setPropActSpoils(List<PropActSpoils> propActSpoils) {
		this.propActSpoils = propActSpoils;
	}

	public void setCampaignList(ArrayList<CampaignInfoClient> campaignList) {
		this.campaignList = campaignList;
	}

	public int getClearStamina() {
		return clearStamina;
	}

	public void setClearStamina(int clearStamina) {
		this.clearStamina = clearStamina;
	}

	// 获取本章节最后一关
	public CampaignInfoClient getLastCampaignInfoClient() {
		CampaignInfoClient cic = null;
		for (CampaignInfoClient info : campaignList) {
			if (cic == null || cic.getId() < info.getId())
				cic = info;
		}
		return cic;
	}

	/**
	 * 获取通关奖励 所有
	 * 
	 * @param all
	 *            是否查询所有奖励 false表示显示在列表上的奖励
	 * @return
	 */
	public ReturnInfoClient getActSpoils(boolean all) {
		ReturnInfoClient rc = new ReturnInfoClient();
		for (PropActSpoils ap : propActSpoils) {
			// 跳过查询列表时 不是显示项的回报
			if (!all && !ap.isShow())
				continue;
			rc.addCfg(ap.getType(), ap.getSpoilId(), ap.getSpoilAmt());
		}
		return rc;
	}

	/**
	 * 是否领取过通关奖励
	 * 
	 * @return
	 */
	public boolean hasCompleteBonus() {
		return StringUtil.isFlagOn(flag, 1);
	}

	public String getHasBonus() {
		return hasCompleteBonus() ? "已获得" : "未获得";
	}

	/**
	 * 章节完成情况 5/16
	 * 
	 * @return
	 */
	public int[] completePrecent() {
		int[] p = { 0, 0 };
		for (CampaignInfoClient c : campaignList) {
			p[0] += (c.isComplete() ? 1 : 0);
			p[1] += 1;
		}
		return p;
	}

	public boolean isComplete() {
		int[] percent = completePrecent();
		int completed = percent[0];
		int all = percent[1];
		return completed >= all;
	}

	// 获取完成度字符串
	public String getCompleteDegree() {
		int[] percent = completePrecent();
		int completed = percent[0];
		int all = percent[1];

		return completed + "/" + all;
	}

	public List<CampaignInfoClient> getCampaignList() {
		return campaignList == null ? new ArrayList<CampaignInfoClient>()
				: campaignList;
	}

	/**
	 * 是否过期
	 * 
	 * @return
	 */
	public boolean isExpired() {
		Date now = new Date(Config.serverTime());
		if (this.getPropAct().getOpenTime() != null
				&& this.getPropAct().getOpenTime().after(now))
			return true;
		if (this.getPropAct().getCloseTime() != null
				&& this.getPropAct().getCloseTime().before(now))
			return true;
		return false;
	}

	// 章节没有开始
	public boolean isNotOpen() {
		Date now = new Date(Config.serverTime());
		if (this.getPropAct().getOpenTime() != null
				&& this.getPropAct().getOpenTime().after(now))
			return true;
		return false;
	}

	public String getOpenTime() {
		if (isNotOpen() && null != this.getPropAct().getOpenTime()) {
			Date now = new Date(Config.serverTime());
			return DateUtil.formatHourDuring(now, this.getPropAct()
					.getOpenTime())
					+ "后开启";
		}

		return "";
	}

	// 章节已经开始，没有过期
	public boolean isOpen() {
		Date now = new Date(Config.serverTime());
		if (this.getPropAct().getOpenTime() != null
				&& this.getPropAct().getOpenTime().before(now)
				&& this.getPropAct().getCloseTime().after(now))
			return true;
		return false;
	}

	// 距离下次的开启/关闭时间
	public String getLastTime() {
		if (isOpen() && null != this.getPropAct().getCloseTime()) {
			Date now = new Date(Config.serverTime());
			// 如果act中所有campaign开启时间方案相同，act的关闭时间按照campaign的开启时间方案显示
			if (isAllCampaignOpenTimeSame()) {
				CampaignInfoClient campaign = campaignList.get(0);
				if (campaign.isOpen()) {
					int closeSecond = campaign.getNextCloseTime();
					return DateUtil.formatHourOrMinute(closeSecond) + "后关闭";
				} else {
					int openSecond = campaign.getNextOpenTime();
					return DateUtil.formatHourOrMinute(openSecond) + "后开启";
				}
			} else
				return DateUtil.formatHourDuring(now, this.getPropAct()
						.getCloseTime())
						+ "后关闭";
		}

		return "";
	}

	private boolean isAllCampaignOpenTimeSame() {
		if (ListUtil.isNull(campaignList))
			return false;

		int openSolutionId = campaignList.get(0).getOpenSolutionId();
		for (CampaignInfoClient it : campaignList) {
			if (it.getOpenSolutionId() != openSolutionId)
				return false;
		}

		return true;
	}

	public CampaignInfoClient getCampaign(int id) {
		for (CampaignInfoClient c : campaignList) {
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	public void update(ActInfo actInfo, boolean diff) {
		setFlag(actInfo.getFlag());
		if (actInfo.hasInfos()) {
			for (CampaignInfo ci : actInfo.getInfosList()) {
				CampaignInfoClient c = getCampaign(ci.getId());
				if (c != null) {
					c.updateData(ci);
				}
			}
		}
		setClearTimes(actInfo.getClearTimes());
		setLastResetTime(actInfo.getLastResetTime());
	}

	public boolean needResetActClearTimes() {
		return !DateUtil.isToday(lastResetTime * 1000);
	}

	public void delete(ActInfo actInfo) {
		this.flag = 0;
		// for (CampaignInfoClient c : campaignList) {
		// c.deleteData();
		// }
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
		ActInfoClient other = (ActInfoClient) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int[] getCompleted() {
		int[] complete = new int[2];
		if (ListUtil.isNull(campaignList))
			return complete;

		for (CampaignInfoClient it : campaignList) {
			if (it.isComplete())
				complete[0]++;
		}
		complete[1] = campaignList.size();

		return complete;
	}

	public String getCompletedDesc() {
		int[] c = getCompleted();
		return c[0] + "/" + c[1];
	}

	// 是否是新手副本
	public boolean isFreshAct() {
		if (null == propAct)
			return false;

		return 1 == propAct.getType();
	}

	// 是否是战役副本,名将副本和秘籍副本也算战役副本
	public boolean isBattleAct() {
		if (null == propAct)
			return false;

		return 2 == propAct.getType() || 5 == propAct.getType()
				|| 6 == propAct.getType() || 7 == propAct.getType();
	}

	// 是否是活动副本
	public boolean isActivityAct() {
		if (null == propAct)
			return false;

		return 3 == propAct.getType();
	}

	// 是否是普通副本
	public boolean isServerNormalAct() {
		if (null == propAct)
			return false;

		return 1 == propAct.getServerType();
	}

	// 是否是活动副本
	public boolean isServerActivityAct() {
		if (null == propAct)
			return false;

		return 2 == propAct.getServerType();
	}

	// 是否是时空副本
	public boolean isSpecialAct() {
		if (null == propAct)
			return false;

		return 4 == propAct.getType();
	}

	public void setAllPassBonus() {
		flag = (int) StringUtil.setFlagXOR(flag, 1);
	}
}
