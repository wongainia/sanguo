package com.vikings.sanguo.message;

import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.vikings.sanguo.protos.ChatTimeInfo;
import com.vikings.sanguo.protos.MsgRspHeartbeat;

/**
 * 
 * @author Brad.Chen
 * 
 */
public class HeartBeatResp extends BaseResp {

	// 服务器端富用户信息版本
	private int userVer;

	// 服务器公告版本
	private int notifyVer;

	// 服务器当前log信息的最大id，根据此id判断是否有新的信息
	private long maxIdRealLog;

	// 服务器当前message信息的最大id，根据此id判断是否有新的信息
	private long maxIdMessage;

	private int currentTime;

	private List<ChatTimeInfo> chatTimeInfos;

	private long maxIdWantedPlayer; // 本国最大追杀令id

	@Override
	protected void fromBytes(byte[] buf, int index) throws Exception {
		MsgRspHeartbeat resp = new MsgRspHeartbeat();
		ProtobufIOUtil.mergeFrom(buf, resp, resp);
		userVer = resp.getUserVer();
		notifyVer = resp.getNotifyVer();
		maxIdRealLog = resp.getMaxIdRealLog();
		maxIdMessage = resp.getMaxIdMessage();
		currentTime = resp.getCurrentTime();
		chatTimeInfos = new ArrayList<ChatTimeInfo>();
		if (resp.hasChatTimeInfos()) {
			chatTimeInfos.addAll(resp.getChatTimeInfosList());
		}
		maxIdWantedPlayer = resp.getMaxIdWantedPlayer();
	}

	public int getUserVer() {
		return userVer;
	}

	public int getNotifyVer() {
		return notifyVer;
	}

	public long getMaxIdRealLog() {
		return maxIdRealLog;
	}

	public long getMaxIdMessage() {
		return maxIdMessage;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public List<ChatTimeInfo> getChatTimeInfos() {
		return chatTimeInfos;
	}

	public long getMaxIdWantedPlayer() {
		return maxIdWantedPlayer;
	}

}
