package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.PropAct;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.protos.ActInfo;
import com.vikings.sanguo.protos.ActInfos;
import com.vikings.sanguo.protos.CampaignInfo;
import com.vikings.sanguo.protos.ExActInfo;

public class ActInfoCache {

	private ArrayList<ActInfoClient> all;

	public ActInfoCache() throws GameException {
		all = new ArrayList<ActInfoClient>();

		// 先初始化campaign
		ArrayList<CampaignInfoClient> campaignList = new ArrayList<CampaignInfoClient>();
		for (Object o : CacheMgr.campaignCache.getSortedKey()) {
			int cid = (Integer) o;
			campaignList.add(new CampaignInfoClient(cid));
		}

		// act
		for (Object o : CacheMgr.actCache.getSortedKey()) {
			int cid = (Integer) o;
			all.add(new ActInfoClient(cid, campaignList));
		}
	}

	/**
	 * 根据副本类型获取章节 （这里要排除点当前不能查看的副本,或者已经过期的）
	 * 
	 * @param type
	 * @return
	 */
	public List<ActInfoClient> getActsByType(int type, byte difficult) {
		ArrayList<ActInfoClient> rs = new ArrayList<ActInfoClient>();
		for (ActInfoClient actInfoClient : all) {
			PropAct propAct = actInfoClient.getPropAct();
			// 需要排除过期的act
			if (null != propAct && propAct.getType() == type
					&& propAct.getDifficult() == difficult
					&& !actInfoClient.isExpired()) {
				if (propAct.hasNoPreAct()) { // 没有前置act
					rs.add(actInfoClient);
				} else {
					ActInfoClient preActInfoClient = getAct(propAct
							.getPreActId());
					if (null != preActInfoClient
							&& preActInfoClient.isComplete())
						rs.add(actInfoClient);
				}
			}
		}
		return rs;
	}

	public ActInfoClient getAct(int actId) {
		for (ActInfoClient ac : all) {
			if (ac.getId() == actId && !ac.isExpired())
				return ac;
		}
		return null;
	}

	/**
	 * 更新服务器数据
	 * 
	 * @param syncDataSet
	 */
	public void updateDate(SyncDataSet syncDataSet) {
		updateData(syncDataSet.actInfos);
		updateData(syncDataSet.dynamicActInfos);
	}

	/**
	 * 客户端操作副本后的主动数据更新
	 * 
	 * @param actId
	 * @param ci
	 */
	public void updateData(int actId, CampaignInfo ci) {
		ActInfoClient ac = getAct(actId);
		if (ac == null)
			return;
		CampaignInfoClient c = ac.getCampaign(ci.getId());
		if (c == null)
			return;
		c.updateData(ci);
	}

	/**
	 * 差量更新
	 * 
	 * @param actId
	 * @param infos
	 */
	public void updateData(int actId, List<CampaignInfo> infos) {
		ActInfoClient ac = getAct(actId);
		if (ac == null)
			return;
		for (CampaignInfo info : infos) {
			CampaignInfoClient c = ac.getCampaign(info.getId().intValue());
			if (c == null)
				continue;
			c.updateData(info);
		}
	}

	// 副本扫荡，差量更新数据
	public void updateDiffData(ActInfo actInfo) {
		for (ActInfoClient it : all) {
			if (it.getId() == actInfo.getId()) {
				it.update(actInfo, true);
				break;
			}
		}
	}

	private void updateData(ActInfos actInfos) {
		if (actInfos == null)
			return;
		for (ExActInfo ea : actInfos.getInfosList()) {
			ActInfoClient ac = getAct(ea.getInfo().getId());
			if (ac == null)
				continue;
			switch (ea.getCtrl().getOp().byteValue()) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				ac.update(ea.getInfo(), true);
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				ac.delete(ea.getInfo());
				break;
			default:
				break;
			}
		}
	}
}
