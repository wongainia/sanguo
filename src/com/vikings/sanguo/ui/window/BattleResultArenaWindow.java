package com.vikings.sanguo.ui.window;

import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.BattleResultArenaTab;

public class BattleResultArenaWindow extends BattleResultWindow {
	private int oldRank = -1;
	private int newRank = -1;

	public void open(BattleLogInfoClient blic, ReturnInfoClient ric,
			boolean isLog, String bgName, int oldRank, int newRank) {
		this.oldRank = oldRank;
		this.newRank = newRank;
		super.open(blic, ric, isLog, bgName);
	}

	@Override
	protected void initTabsLayoutGroups() {
		tabsLayoutGroups = new ViewGroup[3];
		tabsLayoutGroups[0] = (ViewGroup) window.findViewById(R.id.arenaLayout);
		tabsLayoutGroups[1] = (ViewGroup) window.findViewById(R.id.lossLayout);
		tabsLayoutGroups[2] = (ViewGroup) window.findViewById(R.id.logLayout);
		ViewUtil.setGone(window, R.id.awardLayout);
	}

	protected void setBonus() {
		if (!tabIsInit[0]) {
			new BattleResultArenaTab(blic, tabsLayoutGroups[0], oldRank,
					newRank);
			tabIsInit[0] = true;
		}
	}
}
