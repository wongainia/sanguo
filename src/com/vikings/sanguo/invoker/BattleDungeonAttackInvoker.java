package com.vikings.sanguo.invoker;

import java.util.List;

import com.vikings.sanguo.battle.anim.BattleDriver;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.message.DungeonAttackResp;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.ui.adapter.CampaignAdapter;
import com.vikings.sanguo.ui.guide.BaseStep;
import com.vikings.sanguo.ui.window.BattleWindow;

//副本开战 
public class BattleDungeonAttackInvoker extends BaseInvoker {
	private CampaignInfoClient campaignClient;
	private List<HeroIdInfoClient> hiics;
	private BattleLogInfoClient blic;
	private BattleDriver battleDriver;
	private boolean help;

	/**
	 * @param campaignClient
	 * @param hics
	 * @param help
	 *            是否使用系统将领
	 */
	public BattleDungeonAttackInvoker(CampaignInfoClient campaignClient,
			List<HeroIdInfoClient> hiics, boolean help) {
		this.campaignClient = campaignClient;
		this.hiics = hiics;
		this.help = help;

	}

	@Override
	protected String loadingMsg() {
		return "数据加载中...";
	}

	@Override
	protected String failMsg() {
		return "获取数据失败";
	}

	@Override
	protected void onFail(GameException exception) {
		super.onFail(exception);
		// 失败后刷新界面 避免兵力不对
		Config.getController().getCurPopupUI().showUI();
	}

	@Override
	protected void fire() throws GameException {
		// 是否打过第四关副本战役
		if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4
				&& !BaseStep.isStep501Enter()) {
			new EndGuider(BaseStep.INDEX_STEP501_ENTER).start();
		}

		boolean first = (campaignClient.getTimes() == 0); // 是否未胜利过

		DungeonAttackResp resp = GameBiz.getInstance().dungeonAttack(
				campaignClient.getPropCampaign().getActId(),
				campaignClient.getId(), hiics, help);
		blic = new BattleLogInfoClient();
		blic.init(resp.getLog(), campaignClient.getBgName(),
				campaignClient.getWall());
		battleDriver = new BattleDriver(blic, resp.getRi());
		// TODO 下载战斗图片
		CacheMgr.downloadBattleImgAndSound(blic);
		if (blic.isMeWin()) {
			if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4
					&& Config.firstComplement1_4_win == 0)
				Config.firstComplement1_4_win = 3;
			Config.latestDungeonResult = 1;
			CampaignAdapter.isFromBattleResult = true;
			if (first)
				CampaignAdapter.isFirstWin = true;
		} else if (blic.isMeLose()) {
			if (campaignClient.getId() == CampaignInfoClient.CAMPAIGN_1_4)
				Config.firstComplement1_4 = 1;
			Config.latestDungeonResult = -1;
			CampaignAdapter.isFromBattleResult = false;
		}
		// 记录章节ID 和 成败
		Config.CAMPAIGN_ID = campaignClient.getId();
		Config.CAMPAIGN_IS_FAIL = blic.isMeLose() ? true : false;
	}

	@Override
	protected void onOK() {
		Config.getController().goBack();
		{
			ctr.goBack();
			new BattleWindow().open(battleDriver, null);
		}

	}
}
