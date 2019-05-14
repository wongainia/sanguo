/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-12-17 下午3:13:19
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ReturnInfoClient;

public class ManorReviveInvoker extends BaseInvoker {
	private int target;
	private int buildingId;
	private List<Integer> armIds;
	private int bossArmId;
	private int bossCount;
	private ReturnInfoClient ric;

	public ManorReviveInvoker(int target, int buildingId, List<Integer> armIds,
			int bossArmId, int bossCount) {
		this.target = target;
		this.buildingId = buildingId;
		this.armIds = armIds;
		this.bossArmId = bossArmId;
		this.bossCount = bossCount;
	}

	@Override
	protected String loadingMsg() {
		return "英魂复活中";
	}

	@Override
	protected String failMsg() {
		return "英魂复活失败";
	}

	@Override
	protected void fire() throws GameException {
		ric = GameBiz.getInstance().manorRevive(target, buildingId, armIds,
				bossArmId, bossCount);
	}

	@Override
	protected void onOK() {
		ctr.alert("英魂复活成功！");
		ctr.updateUI(ric, false);
	}
}
