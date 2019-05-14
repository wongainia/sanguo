package com.vikings.sanguo.cache;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.vikings.sanguo.BattleStatus;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.model.BriefBattleInfoClient;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.model.SyncDataSet;
import com.vikings.sanguo.ui.NotifyMsg;
import com.vikings.sanguo.ui.window.WarInfoWindow;
import com.vikings.sanguo.utils.TroopUtil;

/**
 * 
 * 
 * @author susong
 * 
 */
public class BriefBattleInfoCache implements Serializable {

	private static final long serialVersionUID = -301633916777778119L;

	static final private String file = "battleInfoCache_v1_";

	private Map<Long, BriefBattleInfoClient> content = new HashMap<Long, BriefBattleInfoClient>();

	// 自己的战场id
	public List<Long> battleIds = new ArrayList<Long>();

	public int battleVer = 0;

	private BriefBattleInfoCache() {
	}

	public List<Long> getBattleIds() {
		return battleIds;
	}

	public int countCurBattle() {
		return battleIds.size();
	}

	synchronized private void deleteContent(long id) {
		content.remove(id);
		battleIds.remove(id);
	}

	synchronized private void updateContent(BriefBattleInfoClient bbic) {
		if (!battleIds.contains(bbic.getBattleid())) {
			battleIds.add(bbic.getBattleid());
		}
		content.put(bbic.getBattleid(), bbic);
	}

	synchronized private void updateContent(List<BriefBattleInfoClient> ls) {
		for (BriefBattleInfoClient b : ls) {
			updateContent(b);
		}
	}

	synchronized public BriefBattleInfoClient getBriefBattleInCache(
			long battleId) {
		return content.get(battleId);
	}

	synchronized public List<BriefBattleInfoClient> getAll() {
		for (Iterator<Long> iter = battleIds.iterator(); iter.hasNext();) {
			long battleid = iter.next();
			BriefBattleInfoClient bf = content.get(battleid);
			if (battleid == 0 || bf == null
					|| !BattleStatus.isInBattle(bf.getState())) {
				iter.remove();
				if (content.containsKey(battleid))
					content.remove(battleid);
			}
		}
		return new ArrayList<BriefBattleInfoClient>(content.values());
	}

	public Map<Long, BriefBattleInfoClient> getContent() {
		return content;
	}

	public void mergeAll(SyncData<Long>[] datas) {
		// 如果全量同步到的数据未空，说明没有战争，清空数据
		if (datas == null || datas.length <= 0) {
			battleIds.clear();
			content.clear();
			updateUI();
			return;
		}
		for (int i = 0; i < datas.length; i++) {
			long id = datas[i].getData();
			if (!battleIds.contains(id))
				battleIds.add(id);
		}
		if (!battleIds.isEmpty() && content.isEmpty()) {
			new FetchAllDataInvoker(battleIds).start();
		}
	}

	public void mergeDiff(int ver, SyncData<Long>[] datas, boolean notice) {
		int last = battleVer;
		battleVer = ver;
		// 战斗id版本变化，并且无id同步到， 是在心跳周期内战斗发生并结束了，直接通知用户去查看日志
		if (datas == null || datas.length == 0) {
			return;
		}

		List<Long> addIds = new ArrayList<Long>();
		List<Long> repIds = new ArrayList<Long>();
		List<BriefBattleInfoClient> delBattles = new ArrayList<BriefBattleInfoClient>();
		for (int i = 0; i < datas.length; i++) {
			byte ctrOP = datas[i].getCtrlOP();
			long id = datas[i].getData();
			switch (ctrOP) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
				if (!battleIds.contains(id)) {
					battleIds.add(id);
					addIds.add(id);
				}
				break;
			case SyncCtrl.DATA_CTRL_OP_REP:
				if (!battleIds.contains(id)) {
					battleIds.add(id);
					repIds.add(id);
				}
				break;
			case SyncCtrl.DATA_CTRL_OP_DEL:
				BriefBattleInfoClient b = getBriefBattleInCache(id);
				if (b != null)
					delBattles.add(b);
				deleteContent(id);
				break;
			default:
				break;
			}
		}

		if (!addIds.isEmpty() || !repIds.isEmpty() || !delBattles.isEmpty()) {
			new FetchDiffDataInvoker(addIds, repIds, delBattles, notice)
					.start();
		}
	}

	// 差量同步战争id
	public void syncBattleIdFromSer() throws GameException {
		SyncDataSet dataSet = GameBiz.getInstance().refreshBattle();
		if (dataSet.battleIds == null)
			return;
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < dataSet.battleIds.length; i++) {
			byte ctrOP = dataSet.battleIds[i].getCtrlOP();
			long id = dataSet.battleIds[i].getData();
			switch (ctrOP) {
			case SyncCtrl.DATA_CTRL_OP_NONE:
			case SyncCtrl.DATA_CTRL_OP_ADD:
			case SyncCtrl.DATA_CTRL_OP_REP:
				if (!battleIds.contains(id)) {
					battleIds.add(id);
				}
				ids.add(id);
			case SyncCtrl.DATA_CTRL_OP_DEL:
				deleteContent(id);
				break;
			default:
				break;
			}
		}
		// 马上去取新的battle数据
		new FetchAllDataInvoker(ids).fire();
	}

	// 取当前自己所有领地上的战争信息，包括攻击，防守，援助
	public List<BriefBattleInfoClient> getBriefBattleInfos(
			List<Long> battleIds, boolean needUpdateCache) throws GameException {
		List<BriefBattleInfoClient> infos = GameBiz.getInstance()
				.briefBattleInfoQuery(battleIds);
		if (needUpdateCache) {
			updateContent(infos);
		}
		return infos;
	}

	public List<BriefBattleInfoClient> getBriefBattleInfos(List<Long> battleIds)
			throws GameException {
		return getBriefBattleInfos(battleIds, true);
	}

	/**
	 * 差量同步更新数据后，进行消息提示
	 * 
	 * @param addIds
	 * @param repIds
	 * @param delBattles
	 * @param fetchBattles
	 * @throws GameException
	 */
	private void compare(List<Long> addIds, List<Long> repIds,
			List<BriefBattleInfoClient> delBattles,
			List<BriefBattleInfoClient> fetchBattles) throws GameException {
		// 结束的战争 查询最新的领地数据刷新缓存和提示
		List<Long> ids = new ArrayList<Long>();
		for (BriefBattleInfoClient b : delBattles) {
			ids.add(b.getDefendFiefid());
		}
		if (ids.size() > 0) {
			List<BriefFiefInfoClient> fiefs = GameBiz.getInstance()
					.briefFiefInfoQuery(ids);
			for (BriefBattleInfoClient b : delBattles) {
				for (BriefFiefInfoClient f : fiefs) {
					if (b.getDefendFiefid() == f.getId()) {
						b.setBfic(f);
						Config.getController().getNotifyMsg()
								.addMsg(NotifyMsg.TYPE_FIEF_BATTLE_END, b);
						break;
					}
				}
			}
		}
		// 战斗状态发生变化
		for (BriefBattleInfoClient b : fetchBattles) {
			BriefBattleInfoClient old = getBriefBattleInCache(b.getBattleid());
			if (addIds.contains(b.getBattleid())) {
				// 如果已经有数据了 是客户端主动添加，不需要消息通知
				if (old != null)
					continue;
				Config.getController().getNotifyMsg()
						.addMsg(NotifyMsg.TYPE_FIEF_ATTACK, b);
			}
			if (repIds.contains(b.getBattleid())) {
				if (old == null)
					continue;
				if (b.getState() == BattleStatus.BATTLE_STATE_SURROUND_END
						&& old.getState() == BattleStatus.BATTLE_STATE_SURROUND) {
					// 援助的战斗不用通知围城结束
					if (b.getAttacker() == Account.user.getId()
							|| b.getDefender() == Account.user.getId()) {
						Config.getController().getNotifyMsg()
								.addMsg(NotifyMsg.TYPE_FIEF_SURROUND_END, b);
					}
				}
			}
		}
	}

	// 清除缓存的相应BriefBattleInfo信息
	public void delete(final long battleId) {
		deleteContent(battleId);
		updateUI();
		Config.getController().postRunnable(new Runnable() {

			@Override
			public void run() {
				Config.getController().getNotifyMsg().clearBattle(battleId);
			}
		});
	}

	// 添加缓存
	public void update(BriefBattleInfoClient bbic) {
		if (null == bbic)
			return;
		updateContent(bbic);
		updateUI();
	}

	public void save() {
		try {
			for (Iterator<Long> iter = battleIds.iterator(); iter.hasNext();) {
				long battleid = iter.next();
				BriefBattleInfoClient bf = content.get(battleid);
				bf.setStateWhenSave(TroopUtil.getCurBattleState(bf.getState(),
						bf.getTime()));
			}
			ObjectOutputStream out = new ObjectOutputStream(Config
					.getController()
					.getUIContext()
					.openFileOutput(file + Account.user.getSaveID(),
							Context.MODE_PRIVATE));
			out.writeObject(this);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BriefBattleInfoCache getInstance(boolean newContent,
			Context context) {
		BriefBattleInfoCache cache = null;
		if (newContent) {
			cache = new BriefBattleInfoCache();
		} else {
			try {
				ObjectInputStream in = new ObjectInputStream(
						context.openFileInput(file + Account.user.getSaveID()));
				cache = (BriefBattleInfoCache) in.readObject();
				in.close();
			} catch (Exception e) {
				cache = new BriefBattleInfoCache();
			}
		}

		return cache;
	}

	public static BriefBattleInfoCache getInstance(boolean newContent) {
		return getInstance(newContent, Config.getController().getUIContext());
	}

	private class FetchAllDataInvoker extends BackgroundInvoker {

		private List<Long> battleIds;

		public FetchAllDataInvoker(List<Long> battleIds) {
			this.battleIds = battleIds;
		}

		@Override
		protected void fire() throws GameException {
			int index = 0;
			while (index < battleIds.size()) {
				int temp = index + 10;
				if (temp > battleIds.size())
					temp = battleIds.size();
				getBriefBattleInfos(battleIds.subList(index, temp));
				index = temp;
			}
		}

		@Override
		protected void onOK() {
			updateUI();
		}

	}

	private class FetchDiffDataInvoker extends BackgroundInvoker {

		private List<Long> addIds;
		private List<Long> repIds;
		private List<BriefBattleInfoClient> delBattles;
		private boolean notice;

		private List<BriefBattleInfoClient> fetchBattles = new ArrayList<BriefBattleInfoClient>();
		private List<Long> allIds = new ArrayList<Long>();

		public FetchDiffDataInvoker(List<Long> addIds, List<Long> repIds,
				List<BriefBattleInfoClient> delBattles, boolean notice) {
			this.addIds = addIds;
			this.repIds = repIds;
			this.delBattles = delBattles;
			this.notice = notice;
		}

		@Override
		protected void fire() throws GameException {
			if (!addIds.isEmpty())
				allIds.addAll(addIds);
			if (!repIds.isEmpty())
				allIds.addAll(repIds);
			// allIds添加 battleIds中有id，content中无数据的id，重新取数据
			List<Long> ids = getBattleIdNotInCache();
			if (!ids.isEmpty()) {
				for (Long id : ids) {
					if (!allIds.contains(id))
						allIds.add(id);
				}
			}
			if (!allIds.isEmpty()) {
				int index = 0;
				while (index < allIds.size()) {
					int temp = index + 10;
					if (temp > allIds.size())
						temp = allIds.size();
					fetchBattles.addAll(getBriefBattleInfos(
							allIds.subList(index, temp), false));
					index = temp;
				}
			}
			if (notice)
				compare(addIds, repIds, delBattles, fetchBattles);
		}

		@Override
		protected void onOK() {
			if (!fetchBattles.isEmpty()) {
				updateContent(fetchBattles);
				// 刷新战场界面
				WarInfoWindow.inform(delBattles, fetchBattles);
				updateUI();
			}

		}

	}

	// 获取已经有battleId，但是有可能因为同步失败，没有取到briefBattle的数据
	public List<Long> getBattleIdNotInCache() {
		List<Long> ids = new ArrayList<Long>();
		for (Long battleId : battleIds) {
			if (!content.containsKey(battleId))
				ids.add(battleId);
		}
		return ids;
	}

	public void checkData() throws GameException {

		for (Long id : battleIds) {
			BriefBattleInfoClient bbic = getBriefBattleInCache(id);
			if (bbic == null)
				continue;
			int curState = bbic.getState();
			// 行军 、围城中 需要计算最新状态
			if (bbic.getState() == BattleStatus.BATTLE_STATE_SURROUND)
				curState = TroopUtil.getCurBattleState(bbic.getState(),
						bbic.getTime());
			if (curState == BattleStatus.BATTLE_STATE_SURROUND_END
					&& bbic.getState() == BattleStatus.BATTLE_STATE_SURROUND) {
				bbic.setState(BattleStatus.BATTLE_STATE_SURROUND_END);
				bbic.setTime(0);
				// 援助的战斗不用通知围城结束
				if (bbic.getAttacker() == Account.user.getId()
						|| bbic.getDefender() == Account.user.getId()) {
					Config.getController().getNotifyMsg()
							.addMsg(NotifyMsg.TYPE_FIEF_SURROUND_END, bbic);
				}
			}
		}
	}

	public void updateUI() {
		Config.getController().postRunnable(new Runnable() {
			@Override
			public void run() {
				if (Config.getController().getFiefMap() != null) {
					Config.getController().getFiefMap().updateMyFief();
				}
			}
		});
	}

}
