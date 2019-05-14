/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午10:08:16
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import android.view.View;
import android.view.View.OnClickListener;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.RankProp;
import com.vikings.sanguo.ui.alert.HonorRuleTip;
import com.vikings.sanguo.widget.CustomBaseListWindow;

public abstract class RankWindow extends CustomBaseListWindow{
	protected RankProp rankProp;
	
	public void open(RankProp rankProp) {
		this.rankProp = rankProp;
		doOpen();
	}
	
	@Override
	protected void init() {
		init(rankProp.getTitle());
		setLeftBtn("规则说明", new OnClickListener() {
			@Override
			public void onClick(View v) {
				new HonorRuleTip(rankProp.getTitle(), getHonorRule()).show();
			}
		});
		firstPage();
	}
	
	// 框体左按钮背景
	@Override
	protected byte getLeftBtnBgType() {
		return WINDOW_BTN_BG_TYPE_CLICK;
	}

	public static RankWindow create(RankProp rankProp) {
		switch (rankProp.getId()) {
		case 1:
		case 3:
		case 4:
			return new UserRankWindow();
		case 2:
			return new HeroRankWindow();
		default:
			return null;
		}
	}
	
	protected String getHonorRule() {
		return CacheMgr.uiTextCache.getTxt(rankProp.getUITextId());
	};
}

