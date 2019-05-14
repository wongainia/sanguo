package com.vikings.sanguo.cache;

import java.util.ArrayList;
import java.util.List;
import com.vikings.sanguo.ResultCode;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BackgroundInvoker;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.message.RichGuildVersionQueryResp;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.GuildJoinInfoClient;
import com.vikings.sanguo.model.RichGuildInfoClient;
import com.vikings.sanguo.model.SyncCtrl;
import com.vikings.sanguo.model.SyncData;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.protos.BattleIdInfo;

public class RichGuildInfoClientCache {
	private final int UPDATE_TIME = 2 * 60 * 1000;

	private int guildid;
	private RichGuildInfoClient richInfo;
	private long lastUpdataTime = 0;

	private long attackBattleId;
	private long defendBattleId;

	public int getGuildid() {
		return guildid;
	}

	public void setGuildid(int guildid) {
		this.guildid = guildid;
	}

	public void setRichInfo(RichGuildInfoClient richInfo) {
		this.richInfo = richInfo;
	}

	public RichGuildInfoClient getRichInfoInCache() {
		return this.richInfo;
	}

	public void updata(boolean forceUpdate) throws GameException {
		if (Account.user.hasGuild()) {
			if (forceUpdate) {
				if (null == richInfo || guildid != richInfo.getGic().getId()) {
					update(getDateFromSer());
					CacheMgr.bgicCache.updateCache(richInfo.getGic());
				} else {
					checkVersionAndUpdate();
				}
			} else {
				if (null == richInfo || guildid != richInfo.getGic().getId()) {
					update(getDateFromSer());
					CacheMgr.bgicCache.updateCache(richInfo.getGic());
				} else {
					if (Config.serverTime() - lastUpdataTime > UPDATE_TIME) {
						checkVersionAndUpdate();
					}
				}
			}

			if (null != richInfo && richInfo.getGic().hasAltar()) {
				List<Long> ids = new ArrayList<Long>();
				ids.add(richInfo.getGic().getAltarId());
				List<BriefFiefInfoClient> fiefs = GameBiz.getInstance()
						.briefFiefInfoQuery(ids);
				if (!ListUtil.isNull(fiefs))
					Account.guildAltar = fiefs.get(0);
			}
		}
	}

	private void checkVersionAndUpdate() throws GameException {
		RichGuildVersionQueryResp resp = GameBiz.getInstance()
				.richGuildVersionQuery(guildid);
		if (resp.getVersion() > richInfo.getVersion()) {
			update(getDateFromSer());
			CacheMgr.bgicCache.updateCache(richInfo.getGic());
		} else {
			lastUpdataTime = Config.serverTime();
		}
		// 处理家族战争(家族这边存的briefbattleInfo，服务器给的方案是只要有就查)
		// doAttackBattle(resp.getAttackInfo());
		// doDefendBattle(resp.getDefendInfo());
	}

	private void doAttackBattle(BattleIdInfo info) {
		if (attackBattleId > 0 && info == null) {
			// 战斗结束
		} else if (attackBattleId > 0 && info != null
				&& attackBattleId != info.getBi().getBattleid()) {
			// 该场战斗结束，又有新战斗
		} else if (attackBattleId == 0 && info != null) {
			// 有新的战斗了
		}
	}

	private void doDefendBattle(BattleIdInfo info) {
		if (defendBattleId > 0 && info == null) {
			// 战斗结束
		} else if (defendBattleId == 0 && info != null) {
			// 有新的战斗了
		}
	}

	private RichGuildInfoClient getDateFromSer() throws GameException {
		RichGuildInfoClient richGuildInfoClient = GameBiz.getInstance()
				.richGuildInfoQuery(guildid);
		lastUpdataTime = Config.serverTime();
		return richGuildInfoClient;

	}

	public RichGuildInfoClient getRichGuildInfoClient() throws GameException {
		if (Account.user.hasGuild()) {
			if (null == richInfo || guildid != richInfo.getGic().getId()) {
				richInfo = getDateFromSer();
				CacheMgr.bgicCache.updateCache(richInfo.getGic());
			}
			return richInfo;
		} else {
			throw new GameException(
					CacheMgr.errorCodeCache
							.getMsg(ResultCode.RESULT_FAILED_GUILD_INVALID));
		}

	}

	public void update(RichGuildInfoClient rgic) {
		if (richInfo == null) {
			richInfo = rgic;
		} else {
			checkNotic(richInfo, rgic);
			richInfo.update(rgic);
		}
	}

	// 有成员申请时提示
	private void checkNotic(RichGuildInfoClient oldRgic,
			RichGuildInfoClient newRgic) {
		if (null == oldRgic || null == newRgic)
			return;
		List<GuildJoinInfoClient> oldList = new ArrayList<GuildJoinInfoClient>();
		oldList.addAll(oldRgic.getGjics());
		List<GuildJoinInfoClient> newList = new ArrayList<GuildJoinInfoClient>();
		newList.addAll(newRgic.getGjics());
		// 如果自己是族长，通知有人申请加入家族
		if (Account.user.getId() == newRgic.getGic().getLeader())
			new CompareInvoker(oldList, newList, newRgic.getGic().getId())
					.start();
	}

	public void merge(SyncData<Integer> guildId) {
		switch (guildId.getCtrlOP()) {
		case SyncCtrl.DATA_CTRL_OP_NONE:
		case SyncCtrl.DATA_CTRL_OP_ADD:
		case SyncCtrl.DATA_CTRL_OP_REP:
			guildid = guildId.getData();
			break;
		case SyncCtrl.DATA_CTRL_OP_DEL:
			guildid = 0;
			break;
		default:
			break;
		}
	}

	public boolean hasGuide() {
		return guildid > 0;
	}

	public boolean isMyGuild(long guildId) {
		return this.guildid == guildId;
	}

	private class CompareInvoker extends BackgroundInvoker {
		private int id;
		private List<GuildJoinInfoClient> oldList;
		private List<GuildJoinInfoClient> newList;
		private List<GuildJoinInfoClient> difList = new ArrayList<GuildJoinInfoClient>();

		public CompareInvoker(List<GuildJoinInfoClient> oldList,
				List<GuildJoinInfoClient> newList, int id) {
			this.oldList = oldList;
			this.newList = newList;
			this.id = id;
		}

		@Override
		protected void fire() throws GameException {
			for (GuildJoinInfoClient info : newList) {
				if (!oldList.contains(info)) {
					difList.add(info);
				}
			}
			if (!difList.isEmpty()) {
				List<Integer> ids = new ArrayList<Integer>();
				for (GuildJoinInfoClient info : difList) {
					if (!ids.contains(info.getUserId()))
						ids.add(info.getUserId());
				}
				List<BriefUserInfoClient> users = UserCache.sequenceByIds(ids,
						CacheMgr.getUser(ids));
				for (GuildJoinInfoClient info : difList) {
					info.setBriefUser(CacheMgr.getUserById(info.getUserId(),
							users));
				}
			}
		}

		@Override
		protected void onOK() {
			if (!difList.isEmpty()) {
				for (final GuildJoinInfoClient info : difList) {
					Config.getController().getHandler().post(new Runnable() {

						@Override
						public void run() {
							Config.getController().getNotifyMsg()
									.addMsg(info, id);
						}
					});
				}
			}
		}

	}

	public boolean hasAltar() {
		return null != richInfo && richInfo.getGic().hasAltar();
	}
}
