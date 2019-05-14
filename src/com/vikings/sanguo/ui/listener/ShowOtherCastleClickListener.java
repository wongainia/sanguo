/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-23 上午10:47:20
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.listener;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import android.view.View;
import android.view.View.OnClickListener;

public class ShowOtherCastleClickListener implements OnClickListener {
	private BriefUserInfoClient brief;

	public ShowOtherCastleClickListener(BriefUserInfoClient brief) {
		this.brief = brief;
	}

	@Override
	public void onClick(View v) {
		if (null != brief && brief.isOtherUser())
			Config.getController().showCastle(brief);
	}
}
