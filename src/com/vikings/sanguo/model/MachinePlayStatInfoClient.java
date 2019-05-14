/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-25
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.model;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.MachinePlayStatInfo;
import com.vikings.sanguo.protos.MachinePlayType;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.StringUtil;

public class MachinePlayStatInfoClient {
	private MachinePlayStatInfo statInfo;
	private BriefUserInfoClient user;
	private ReturnInfoClient ric;

	public void setStatInfo(MachinePlayStatInfo statInfo) {
		this.statInfo = statInfo;
	}

	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}

	public MachinePlayStatInfo getStatInfo() {
		return statInfo;
	}

	public BriefUserInfoClient getUser() {
		return user;
	}

	public String getLuckyDesc() {
		return getLuckyTitle() + getMachineReward(statInfo); // buf.toString();
	}

	public String getLuckyTitle() {
		StringBuilder buf = new StringBuilder("幸运儿")
				.append(StringUtil.color(user.getHtmlNickName(),
						R.color.color19))
				.append("让美人  ")
				.append(StringUtil.color(getGirlName(statInfo), R.color.color8))
				.append("  芳心蠢动， 获得了");
		return buf.toString();
	}

	private String getGirlName(MachinePlayStatInfo info) {
		if (MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == info.getMachineType())
			return "大乔";
		// return Config.getController().getString(
		// R.string.MachinePlayStatInfoClient_getMachineName_1);
		else if (MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE == info
				.getMachineType())
			return "小乔";
		return "";
	}

	private String getMachineName(MachinePlayStatInfo info) {
		if (MachinePlayType.MACHINE_PLAY_TYPE_JUNIOR == info.getMachineType())
			return CacheMgr.gambleCache.getJuniorName();
		// return Config.getController().getString(
		// R.string.MachinePlayStatInfoClient_getMachineName_1);
		else if (MachinePlayType.MACHINE_PLAY_TYPE_MIDDLE == info
				.getMachineType())
			return CacheMgr.gambleCache.getMiddleName();
		// return Config.getController().getString(
		// R.string.MachinePlayStatInfoClient_getMachineName_2);
		else if (MachinePlayType.MACHINE_PLAY_TYPE_SENIOR == info
				.getMachineType())
			return CacheMgr.gambleCache.getAdvancedName();
		// return Config.getController().getString(
		// R.string.MachinePlayStatInfoClient_getMachineName_3);
		else
			return Config.getController().getString(
					R.string.MachinePlayStatInfoClient_getMachineName_4);
	}

	private String getMachineReward(MachinePlayStatInfo info) {
		return ric.toTextDesc(true);
	}

	public ReturnInfoClient getRic() {
		return ric;
	}

	public static MachinePlayStatInfoClient convert(MachinePlayStatInfo info)
			throws GameException {
		if (null == info)
			return null;
		MachinePlayStatInfoClient mpsic = new MachinePlayStatInfoClient();

		mpsic.statInfo = info;
		mpsic.user = CacheMgr.userCache.get(info.getUserid());
		mpsic.ric = ReturnInfoClient.convert2Client(info.getRi(), false);

		return mpsic;
	}

	public static List<MachinePlayStatInfoClient> convert2List(
			List<MachinePlayStatInfo> infos) throws GameException {
		List<MachinePlayStatInfoClient> list = new ArrayList<MachinePlayStatInfoClient>();
		if (!ListUtil.isNull(infos)) {
			for (MachinePlayStatInfo info : infos)
				list.add(convert(info));
		}
		return list;
	}
}
