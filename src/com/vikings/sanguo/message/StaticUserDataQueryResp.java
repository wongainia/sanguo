package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.model.LogInfoClient;
import com.vikings.sanguo.protos.ArenaLogInfo;
import com.vikings.sanguo.protos.BriefBattleLogInfo;
import com.vikings.sanguo.protos.LogInfo;
import com.vikings.sanguo.protos.MessageInfo;
import com.vikings.sanguo.protos.MsgRspStaticUserDataQuery;
import com.vikings.sanguo.protos.TrayNotifyInfo;
import com.vikings.sanguo.protos.TroopLogInfo;
import com.vikings.sanguo.protos.UserNotifyInfo;

/**
 * 
 * @author susong
 * 
 */
public class StaticUserDataQueryResp extends BaseResp {

	private MsgRspStaticUserDataQuery resp;

	private List<LogInfoClient> logInfos;
	private List<MessageInfo> messageInfos;
	private List<TrayNotifyInfo> trayInfos;
	private List<UserNotifyInfo> userNotifyInfos;
	private List<BriefBattleLogInfo> briefBattleLogs;
	private List<TroopLogInfo> troopLogInfos;
	private List<ArenaLogInfo> arenaLogs;

	@Override
	public void fromBytes(byte[] buf, int index) throws Exception {
		resp = new MsgRspStaticUserDataQuery();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);

		if (resp.hasLogInfos()) {
			logInfos = new ArrayList<LogInfoClient>();
			for (LogInfo li : resp.getLogInfosList()) {
				LogInfoClient lic = new LogInfoClient();
				lic.setLogInfo(li);
				logInfos.add(lic);
			}
		} else if (resp.hasMessageInfos()) {
			messageInfos = new ArrayList<MessageInfo>();
			messageInfos.addAll(resp.getMessageInfosList());
		} else if (resp.hasTrayInfos()) {
			trayInfos = new ArrayList<TrayNotifyInfo>();
			trayInfos.addAll(resp.getTrayInfosList());
		} else if (resp.hasNotifyInfos()) {
			userNotifyInfos = new ArrayList<UserNotifyInfo>();
			userNotifyInfos.addAll(resp.getNotifyInfosList());
		} else if (resp.hasBriefBattleLogs()) {
			briefBattleLogs = new ArrayList<BriefBattleLogInfo>();
			briefBattleLogs.addAll(resp.getBriefBattleLogsList());
		} else if (resp.hasTroopLogs()) {
			troopLogInfos = new ArrayList<TroopLogInfo>();
			troopLogInfos.addAll(resp.getTroopLogsList());
		} else if (resp.hasArenaLogs()) {
			arenaLogs = new ArrayList<ArenaLogInfo>();
			arenaLogs.addAll(resp.getArenaLogsList());
		}
	}

	public MsgRspStaticUserDataQuery getResp() {
		return resp;
	}

	public List<LogInfoClient> getLogInfos() {
		return logInfos == null ? new ArrayList<LogInfoClient>() : logInfos;
	}

	public List<MessageInfo> getMessageInfos() {
		return messageInfos == null ? new ArrayList<MessageInfo>()
				: messageInfos;
	}

	public List<TrayNotifyInfo> getTrayInfos() {
		return trayInfos == null ? new ArrayList<TrayNotifyInfo>() : trayInfos;
	}

	public List<UserNotifyInfo> getUserNotifyInfos() {
		return userNotifyInfos == null ? new ArrayList<UserNotifyInfo>()
				: userNotifyInfos;
	}

	public List<BriefBattleLogInfo> getBriefBattleLogs() {
		return briefBattleLogs == null ? new ArrayList<BriefBattleLogInfo>()
				: briefBattleLogs;
	}

	public List<TroopLogInfo> getTroopLogInfos() {
		return troopLogInfos == null ? new ArrayList<TroopLogInfo>()
				: troopLogInfos;
	}

	public List<ArenaLogInfo> getArenaLogs() {
		return arenaLogs == null ? new ArrayList<ArenaLogInfo>() : arenaLogs;
	}
}
