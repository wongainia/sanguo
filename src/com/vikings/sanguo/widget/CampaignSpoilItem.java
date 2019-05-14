/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-4-27 下午2:41:57
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.BaseUI;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class CampaignSpoilItem extends BaseUI{
	private ViewGroup widget;
	
	public CampaignSpoilItem(ShowItem showItem) {
		widget = (ViewGroup) controller.inflate(R.layout.campaign_spoit_item);
		
		new ViewImgScaleCallBack(showItem.getImg(),
				widget.findViewById(R.id.icon), Constants.SMALL_ICON_WIDTH,
				Constants.SMALL_ICON_HEIGHT);
		ViewUtil.setText(widget, R.id.name, showItem.getName());
		if (hasUserExpBonus(showItem)) {
			int cnt = (int) (showItem.getCount() * CacheMgr.dictCache.getUserExpBonusRate() / 100f);
			ViewUtil.setRichText(widget, R.id.amount, StringUtil.color("×" + cnt, R.color.k7_color12));
		} else if (hasHeroExpBonus(showItem)) {
			int cnt = (int) (showItem.getCount() * CacheMgr.dictCache.getHeroExpBonusRate() / 100f);
			ViewUtil.setRichText(widget, R.id.amount, StringUtil.color("×" + cnt, R.color.k7_color12));
		} else 
			ViewUtil.setText(widget, R.id.amount, "×" + showItem.getCount());
	}

	private boolean hasUserExpBonus(ShowItem showItem) {
		return showItem.getName().equals(Constants.USER_EXP) && CacheMgr.dictCache.getUserExpBonusRate() > 100;
	}

	private boolean hasHeroExpBonus(ShowItem showItem) {
		return showItem.getName().equals(Constants.HERO_EXP) && CacheMgr.dictCache.getHeroExpBonusRate() > 100;
	}
	
	public ViewGroup getWidget() {
		return widget;
	}
}
