package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.DungeonClearInvoker;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class ActClearWindow extends CampaignTroopSetWindow {
	private ActInfoClient act;

	public void open(ActInfoClient act) {
		this.act = act;
		this.campaignClient = act.getLastCampaignInfoClient();
		super.open(campaignClient);
	}

	@Override
	protected void startInvoker() {
		List<HeroIdInfoClient> heroIdInfoClients = new ArrayList<HeroIdInfoClient>();
		try {
			heroIdInfoClients = tidyHeroData();
		} catch (GameException e) {
		}
		new DungeonClearInvoker(act.getId(), campaignClient, heroIdInfoClients)
				.start();
	}

	@Override
	protected int getStaminaCost() {
		return act.getClearStamina();
	}

	@Override
	protected boolean checkLeftCount() {
		return true;
	}

	// 设置战役信息
	protected void setCampaignInfo() {
		View view = window.findViewById(R.id.campaignIcon);
		new ViewImgScaleCallBack(campaignClient.getImg(),
				view.findViewById(R.id.icon), Constants.COMMON_ICON_WIDTH,
				Constants.COMMON_ICON_HEIGHT);

		ViewUtil.setText(window, R.id.campaignName, campaignClient
				.getPropCampaign().getName());

		ViewUtil.setImage(window, R.id.difficultIcon,
				campaignClient.getDifficultyImgResId());

		ViewUtil.setRichText(window, R.id.heroName, "重复挑战本章，直至体力耗尽或本章下所有战役次数耗尽");

		ViewUtil.setGone(window, R.id.armCount);
	}
}