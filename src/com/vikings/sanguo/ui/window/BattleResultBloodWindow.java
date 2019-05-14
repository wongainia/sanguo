package com.vikings.sanguo.ui.window;

import android.view.View;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.ViewUtil;

public class BattleResultBloodWindow extends BattleResultWindow {
	private int index = 1;	
	public void open(BattleLogInfoClient blic, ReturnInfoClient ric,
			boolean isLog, String bgName) {		
		super.open(blic, ric, isLog, bgName);		
	}
	
	@Override
	protected void init()
	{
		super.init();		
	}
	
	public int getIndex()
	{
		return index;
	}
	
	@Override
	public void showUI()
	{
		// TODO Auto-generated method stub
		super.showUI();
		//ViewUtil.setGone(tabs[0]);
		ViewUtil.setGone(window, R.id.tab_award_layout);
	}
	
	protected void selectTab(int index) {
		this.index = index;
		for (int i = 0; i < tabs.length; i++) {
			if (i == index) {
				tabs[i].setBackgroundResource(tabBgIds[1]);
				tabs[i].setTextColor(Config.getController().getResources()
						.getColor(tabColorId[1]));// tabColorId[1]);
				tabsLayoutGroups[i].setVisibility(View.VISIBLE);

			} else {
				if(ViewUtil.isVisible(tabs[i]))
				{
					tabs[i].setBackgroundResource(tabBgIds[0]);
					tabs[i].setTextColor(Config.getController().getResources()
							.getColor(tabColorId[0]));
				}
				tabsLayoutGroups[i].setVisibility(View.GONE);
			}
		}
	}
}
