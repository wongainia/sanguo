/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-6-26 下午4:43:55
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.model;

import android.util.Log;
import com.vikings.sanguo.R;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.protos.ReturnInfo;
import com.vikings.sanguo.utils.StringUtil;

public class BattleLogReturnInfoClient {
	private int type;
	private ReturnInfoClient returnInfoClient;
	private BriefUserInfoClient user;
	
	public void setReturnInfoClient(ReturnInfoClient returnInfoClient) {
		this.returnInfoClient = returnInfoClient;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public ReturnInfoClient getReturnInfoClient() {
		return returnInfoClient;
	}
	
	public int getType() {
		return type;
	}
	
	public int getUserId() {
		return returnInfoClient.getUserId();
	}
	
	public int getTypeImg() {
		switch (type) {
		case 1:
			return R.drawable.pass_award;//"通关奖励";
		case 2:
			return R.drawable.record_award;//"战绩奖励";
		case 3:
			return R.drawable.extra_award;//"意外奖励";
		case 4:
			return R.drawable.drop_award;//"掉落奖励";
		case 5:
			return R.drawable.plunder_award;//"掠夺奖励";
		default:
			return 0;//"奖励";
		}
	}
	
	public String getTypeName() {
		switch (type) {
		case 1:
			return "通关奖励";
		case 2:
			return "战绩奖励";
		case 3:
			return "意外奖励";
		case 4:
			return "掉落奖励";
		case 5:
			return "掠夺奖励";
		default:
			return "奖励";
		}
	}
	
	public boolean isPassAward() {
		return 1 == type;
	}
	
	public boolean isRecordAward() {
		return type == 2;
	}
	
	public boolean isRateAward() {
		return 3 == type;
	}
	
	public boolean isDrop() {
		return type == 4;
	}
	
	public boolean isPlunder() {
		return type == 5;
	}
	
	public ReturnInfo getReturnInfo() {
		return returnInfoClient.getReturnInfo();
	}
	
	public void setUser(BriefUserInfoClient user) {
		this.user = user;
	}
	
	public BriefUserInfoClient getUser() {
		return user;
	}
	
	public boolean isDropNothing() {
		return isDrop()
				&& StringUtil.isNull(getReturnInfoClient().toTextDesc(
						true));
	}
	
	static public BattleLogReturnInfoClient createBattleLogReturnInfoClient(
			ReturnInfo ri, int type) {
		BattleLogReturnInfoClient blric = new BattleLogReturnInfoClient();
		
		try {
			ReturnInfoClient ric = ReturnInfoClient.convert2Client(ri, false);
			blric.setReturnInfoClient(ric);
			blric.setType(type);
			blric.setUser(null);
		} catch (GameException e) {
			Log.e("BattleResultWindow", "merge loss failed");
		}
		
		return blric;
	}
}
