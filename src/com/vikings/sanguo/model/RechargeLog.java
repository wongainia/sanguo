/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-6-27
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.model;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.StringUtil;

public class RechargeLog {
	private int rmb; // 充值金额,单位：分
	private long time; // 充值时间
	private BriefUserInfoClient rechargeUser; // 充值账户
	private int channel; // 充值渠道
	private int cash; // 元宝

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public void setRechargeUser(BriefUserInfoClient rechargeUser) {
		this.rechargeUser = rechargeUser;
	}

	public void setRmb(int rmb) {
		this.rmb = rmb;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getChannel() {
		return channel;
	}

	public BriefUserInfoClient getRechargeUser() {
		return rechargeUser;
	}

	public int getRmb() {
		return rmb;
	}

	public long getTime() {
		return time;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public int getCash() {
		return cash;
	}

	public String getRechargeInfo() {
		String nickName = "";
		if (null == rechargeUser)
			nickName = Config.getController().getString(
					R.string.RechargeLog_getRechargeInfo_1);
		else if (Account.user.getId() == rechargeUser.getId())
			nickName = Config.getController().getString(R.string.myself);
		else
			nickName = rechargeUser.getNickName() + "(" + rechargeUser.getId()
					+ ")";

		String log = StringUtil.repParams(
				Config.getController().getString(
						R.string.RechargeLog_getRechargeInfo_2),
				StringUtil.color(nickName, R.color.color16));

		return log;
	}

	// 1 支付宝
	// 2 易宝
	// 3 汇付天下 网银
	// 5 V币电话充值 已停用
	// 7 快钱
	// 8 鸿联1元短信 已停用
	// 9 银联 网银
	// 10 IOS 正版appstore
	// 101 支付宝web 官网充值
	// 102 易宝web 官网充值
	// 1001 UC联运 uc支付
	//
	// CHANNEL_CMCC = 1002; 南京移动短信
	// CHANNEL_CMCC_MM = 1003; 广东移动短信计费
	// CHANNEL_TEL189 = 1004; 电信短信

	public String getChannelInfo() {
		String channelName = CacheMgr.dictCache.getDict(
				Dict.TYPE_RECHARGE_CHANNEL, channel);
		if (StringUtil.isNull(channelName))
			channelName = "未知渠道";
		return channelName;
	}

	public String getTimeInfo() {
		return DateUtil.db2MinuteFormat.format(time);
	}
}
