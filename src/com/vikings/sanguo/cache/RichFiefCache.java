package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.FiefDataSynResp;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.FiefInfoClient;
import com.vikings.sanguo.model.FiefProp;
import com.vikings.sanguo.model.LordFiefInfoClient;
import com.vikings.sanguo.model.ManorInfoClient;
import com.vikings.sanguo.model.RichFiefInfoClient;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.protos.BaseFiefInfo;
import com.vikings.sanguo.protos.FiefInfo;
import com.vikings.sanguo.protos.RichFiefInfo;
import com.vikings.sanguo.ui.window.CastleWindow;
import com.vikings.sanguo.ui.window.PopupUI;

public class RichFiefCache {

	private Map<Long, RichFiefInfoClient> content = new HashMap<Long, RichFiefInfoClient>();

	private Map<Long, LordFiefInfoClient> lordFiefInfo = new HashMap<Long, LordFiefInfoClient>();

	/**
	 * 同步数据调用
	 * 
	 * @param datas
	 */
	synchronized public void merge(SyncData<LordFiefInfoClient>[] datas) {
		if (datas == null)
			return;
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < datas.length; i++) {
			byte ctrOP = datas[i].getCtrlOP();
			LordFiefInfoClient info = datas[i].getData();
			switch (ctrOP) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				lordFiefInfo.put(info.getFiefId(), info);
				ids.add(info.getFiefId());
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				lordFiefInfo.remove(info.getFiefId());
				content.remove(info.getFiefId());
				break;
			default:
				break;
			}
		}
		if (ids.size() > 0) {
			new Thread(new FetchFief(ids)).start();
		}
	}

	/**
	 * 适配主城没落地时 没lordfief 和richfief
	 */
	synchronized public void fixEmptyMaonor() {
		try {
			if (Account.manorInfoClient.getPos() == ManorInfoClient.POS_EMPTY
					&& lordFiefInfo.size() == 0) {
				LordFiefInfoClient lord = new LordFiefInfoClient(
						ManorInfoClient.POS_EMPTY);
				lordFiefInfo.put(lord.getFiefId(), lord);
				FiefInfo f = new FiefInfo().setBi(new BaseFiefInfo()
						.setUserid(Account.user.getId())
						.setPropid(FiefProp.PROP_CASTLE)
						.setId(lord.getFiefId()));
				FiefInfoClient fief = FiefInfoClient.convert(f);
				content.put(lord.getFiefId(), new RichFiefInfoClient(fief));
			}
		} catch (GameException e) {
			e.printStackTrace();
		}

	}

	synchronized public ArrayList<RichFiefInfoClient> getAll() {
		return new ArrayList<RichFiefInfoClient>(content.values());
	}

	synchronized public void deleteFief(long id) {
		lordFiefInfo.remove(id);
		content.remove(id);
		Config.getController().getBattleMap().deleteFief(id);
		updateUI();
	}

	synchronized public RichFiefInfoClient getInfo(long id) {
		return content.get(id);
	}

	synchronized public int countResSite() {
		int count = 0;
		for (RichFiefInfoClient r : content.values()) {
			if (r.getFiefInfo().getProp().isResource())
				count++;
		}
		return count;
	}

	public RichFiefInfoClient getManorFief() {
		return getInfo(Account.manorInfoClient.getPos());
	}

	public List<Long> getFiefids() {
		return new ArrayList<Long>(lordFiefInfo.keySet());
	}

	/**
	 * 检查是否有处于战斗状态的领地 包括打扫战场状态的。 以通知心跳加速
	 * 
	 * @return
	 */
	synchronized public boolean hasDitryFief() {
		for (RichFiefInfoClient rf : content.values()) {
			if (rf.getFiefInfo() != null
					&& !BattleStatus.isSafe(rf.getFiefInfo().getBattleState()))
				return true;
		}
		return false;
	}

	/**
	 * 领地数量
	 * 
	 * @return
	 */
	public int getFiefCountExceptManor() {
		return lordFiefInfo.size() - 1;
	}

	public int getResourceFiefCount() {
		int count = 0;
		for (RichFiefInfoClient rf : content.values()) {
			if (rf.isResource())
				count++;

		}
		return count;
	}

	public boolean isMyFief(long id) {
		return lordFiefInfo.containsKey(id);
	}

	/**
	 * 兵力移到lordfief下， 以lordfief下为准， 所以richfief的取兵力 建筑 全部在这里代理
	 * 
	 * @param myFiefId
	 * @return
	 */
	public List<ArmInfoClient> getTroopInfo(long myFiefId) {
		if (!lordFiefInfo.containsKey(myFiefId))
			return new ArrayList<ArmInfoClient>();
		return lordFiefInfo.get(myFiefId).getTroopInfo();
	}

	/**
	 * 兵力移到lordfief下， 以lordfief下为准， 所以richfief的取兵力 建筑 全部在这里代理
	 * 
	 * @param myFiefId
	 * @return
	 */
	public void setTroopInfo(long myFiefId, List<ArmInfoClient> troopInfo) {
		if (!lordFiefInfo.containsKey(myFiefId))
			return;
		lordFiefInfo.get(myFiefId).setTroopInfo(troopInfo);
	}

	/**
	 * 兵力移到lordfief下， 以lordfief下为准， 所以richfief的取兵力 建筑 全部在这里代理
	 * 
	 * @param myFiefId
	 * @return
	 */
	public BuildingInfoClient getBuildingInfo(long myFiefId) {
		if (!lordFiefInfo.containsKey(myFiefId))
			return null;
		return lordFiefInfo.get(myFiefId).getBuildingInfo();
	}

	public void updateManor() {
		if (null != getManorFief())
			Config.getController().getBattleMap()
					.updateFief(getManorFief().brief());
		updateUI();
	}

	/**
	 * 取所有的资源点建筑
	 * 
	 * @return
	 */
	public List<BuildingInfoClient> getResourceBuildings() {
		List<BuildingInfoClient> list = new ArrayList<BuildingInfoClient>();
		for (long fiefId : getFiefids()) {
			RichFiefInfoClient rfic = getInfo(fiefId);
			if (rfic.getFiefInfo().getProp().getType() == FiefProp.TYPE_RESOURCE) {
				BuildingInfoClient bic = rfic.getFiefInfo().getBuilding();
				if (null != bic)
					list.add(bic);
			}
		}
		return list;
	}

	/**
	 * 界面主动操作更新数据 例如调兵 开战后
	 * 
	 * @param lord
	 * @param fief
	 */
	synchronized public void update(LordFiefInfoClient lord, FiefInfoClient fief) {
		// 保证不更新错误领地信息，检查领主是否是自己
		if (fief != null && fief.getUserid() != Account.user.getId()) {
			return;
		}
		long id = 0;
		if (lord != null) {
			lordFiefInfo.put(lord.getFiefId(), lord);
			id = lord.getFiefId();
		}
		if (fief != null) {
			content.put(fief.getId(), new RichFiefInfoClient(fief));
			id = fief.getId();
			// 更新英雄所在领地信息
			Account.heroInfoCache.updateHeroFief(fief);
		}
		// 更新地图缓存领地数据
		RichFiefInfoClient rf = content.get(id);
		if (rf == null)
			return;
		Config.getController().getBattleMap().updateFief(rf.brief());
		updateUI();
	}

	synchronized private void updateData(FiefDataSynResp rsp)
			throws GameException {
		for (RichFiefInfo ri : rsp.getInfos()) {
			RichFiefInfoClient r = RichFiefInfoClient.convert(ri);
			if (r.getFiefInfo() != null)
				content.put(ri.getFiefid(), r);
			// 更新地图缓存领地数据
			if (Config.getController().getFiefMap() != null)
				Config.getController().getBattleMap().updateFief(r.brief());
		}
		for (SyncData<LordFiefInfoClient> s : rsp.getDatas()) {
			switch (s.getCtrlOP()) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				lordFiefInfo.put(s.getData().getFiefId(), s.getData());
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				lordFiefInfo.remove(s.getData().getFiefId());
				content.remove(s.getData().getFiefId());
				break;
			default:
				break;
			}
		}
	}

	public void updateUI() {
		Config.getController().postRunnable(new UpdateUI());
	}

	// 判断领地数据是否取完
	public boolean isFetchOver() {
		return content.size() == lordFiefInfo.size();
	}

	private class UpdateUI implements Runnable {

		@Override
		public void run() {
			if (Config.getController().getFiefMap() != null)
				Config.getController().getFiefMap().updateMyFief();
			if (Config.getController().getCurPopupUI() != null) {
				PopupUI popupUI = Config.getController().getCurPopupUI();
				if (popupUI instanceof CastleWindow) {
					((CastleWindow) popupUI).setCastleInfo();
				}
			}
		}

	}

	private class FetchFief implements Runnable {

		private ArrayList<Long> ids;

		public FetchFief(ArrayList<Long> ids) {
			this.ids = ids;
		}

		@Override
		public void run() {
			List<Long> alllist = new ArrayList<Long>();
			List<Long> diffList = new ArrayList<Long>();
			for (Long id : ids) {
				if (content.containsKey(id)) {
					diffList.add(id);
				} else {
					alllist.add(id);
				}
			}
			// 重试3次
			for (int i = 0; i < 3; i++) {
				try {
					if (!alllist.isEmpty() || !diffList.isEmpty()) {
						FiefDataSynResp rsp = GameBiz.getInstance()
								.fiefDataSyn(alllist, diffList);
						updateData(rsp);
						updateUI();
						return;
					}
				} catch (Exception e) {
					Log.e("richfiefcache", "fetch fief data fail", e);
				}
			}

		}

	}

}
