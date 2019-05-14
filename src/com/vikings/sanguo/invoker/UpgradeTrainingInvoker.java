/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-3-5 下午7:31:06
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;

public class UpgradeTrainingInvoker extends BaseInvoker {

	private int type;
	private ManorDraft manorDraft;
	private BuildingProp buildingProp;
	private BuildingBuyResp buildingResp;
	private CallBack cb;
	
	public UpgradeTrainingInvoker(int type, ManorDraft manorDraft,
			BuildingProp buildingProp, 
			com.vikings.sanguo.thread.CallBack cb) {
		super();
		this.type = type;
		this.manorDraft = manorDraft;
		this.buildingProp = buildingProp;
		this.cb = cb;
	}
	
	
	
	
	
	
	@Override
	protected String loadingMsg() {
		return "升级中...";
	}


	@Override
	protected String failMsg() {
		return "升级失败";
	}

	@Override
	protected void fire() throws GameException {
//		BuildingProp prop = (BuildingProp) CacheMgr.buildingPropCache.get(manorDraft.getBuildingId());
//		if (BuildingRequirementCache.unlock(prop.getId(), prop.isCheckLv()))
//			
//		if (null == Account.manorInfoClient.getBuilding(prop) && CacheMgr.buildingBuyCostCache.isEnough(prop.getId()))
			buildingResp=GameBiz.getInstance().buildingBuy(type, buildingProp.getId());
		
		
	}

	@Override
	protected void onOK() {
		SoundMgr.play(R.raw.sfx_resume);
		
		ctr.updateUI(buildingResp.getRi(), true, true, true);
		
		ctr.alert("升级成功", buildingProp.getDesc(), null, false);
		
		Account.manorInfoClient.update(buildingResp.getMic());
		
		if (null != cb)
			cb.onCall();
		
		
	}
	
}
