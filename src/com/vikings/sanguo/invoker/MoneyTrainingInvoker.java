/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2014-1-9 下午2:56:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.BuildingBuyResp;
import com.vikings.sanguo.message.ManorDraftResp;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.sound.SoundMgr;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;

public class MoneyTrainingInvoker extends BaseInvoker {
	private int count;
	private ManorDraft manorDraft;
	private TroopProp troopProp;
	private ManorDraftResp resp;
	private CallBack cb;
	private BuildingBuyResp buildingResp;

	public MoneyTrainingInvoker(ManorDraft manorDraft, TroopProp troopProp,
			int count, CallBack cb) {
		this.count = count;
		this.manorDraft = manorDraft;
		this.troopProp = troopProp;
		this.cb = cb;
	}

	@Override
	protected String loadingMsg() {
		return "训练中…";
	}

	@Override
	protected String failMsg() {
		return "训练失败";
	}

	@Override
	protected void fire() throws GameException {
		BuildingProp prop = (BuildingProp) CacheMgr.buildingPropCache
				.get(manorDraft.getBuildingId());

		if (BuildingRequirementCache.unlockByCurrency(prop.getId(),
				prop.isCheckLv()))

			if (null == Account.manorInfoClient.getBuilding(prop)
					&& CacheMgr.buildingBuyCostCache.isEnough(prop.getId()))
				buildingResp = GameBiz.getInstance().buildingBuy(
						Constants.TYPE_MATERIAL, prop.getId());

		resp = GameBiz.getInstance().manorDraft(manorDraft.getBuildingId(),
				troopProp.getId(), Constants.MANOR_DRAFT_BY_MONEY, count);
	}

	@Override
	protected void onOK() {
		if (null != buildingResp)
			ctr.updateUI(buildingResp.getRi(), true, true, true);

		SoundMgr.play(R.raw.sfx_recruit);
		ctr.updateUI(resp.getRi(), true);
		ctr.alert("招募成功",
				"恭喜你招募了" + StringUtil.color("" + count, R.color.k7_color5)
						+ "名" + troopProp.getName(), null, false);
		Account.manorInfoClient.update(resp.getMic());
		if (null != cb)
			cb.onCall();
	}
}