package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.DungeonClearResp;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.ui.alert.ActClearLoseTip;
import com.vikings.sanguo.ui.alert.ActClearWinTip;
import com.vikings.sanguo.utils.ViewUtil;

public class DungeonClearInvoker extends BaseInvoker {
	private int actId;
	private List<HeroIdInfoClient> hics;
	private DungeonClearResp resp;
	private BattleLogInfoClient blic;
	private CampaignInfoClient campaignClient;

	public DungeonClearInvoker(int actId, CampaignInfoClient campaignClient,
			List<HeroIdInfoClient> hics) {
		this.actId = actId;
		this.campaignClient = campaignClient;
		this.hics = hics;
	}

	@Override
	protected String loadingMsg() {
		return "扫荡战场中";
	}

	@Override
	protected String failMsg() {
		return "扫荡失败";
	}

	@Override
	protected void fire() throws GameException {
		resp = GameBiz.getInstance().dungeonClear(actId, hics);
		blic = new BattleLogInfoClient();
		blic.init(resp.getBattleLogInfo());
	}

	@Override
	protected void onOK() {
		ctr.updateUI(resp.getRi(), true, false, false);
		if (blic.isMeWin()) {
			new ActClearWinTip(blic, resp.getRi()).show();
		} else {
			new ActClearLoseTip().show();
		}

		ViewUtil.setHeroReward(blic);
	}
}
