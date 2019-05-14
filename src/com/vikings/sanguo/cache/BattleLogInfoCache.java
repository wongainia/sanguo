package com.vikings.sanguo.cache;

import java.io.File;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.BattleLogInfo;

/**
 * 缓存BattleLogInfo
 * 
 * @author susong
 * 
 */
public class BattleLogInfoCache {

	public BattleLogInfo getBattleLogInfo(long battleLogId)
			throws GameException {

		BattleLogInfo info = Config.getController().getFileAccess()
				.getBattleLogInfo(battleLogId);
		if (null == info) {
			info = GameBiz.getInstance().battleLogInfoQuery(battleLogId);
			Config.getController().getFileAccess().saveBattleLogInfo(info);
		}
		return info;
	}

	public void updateCache(BattleLogInfo info) {
		File file = Config.getController().getFileAccess()
				.getBattleLogInfoFile(info.getId());
		if (null == file)
			Config.getController().getFileAccess().saveBattleLogInfo(info);
	}
}
