/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-3 下午4:27:02
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.BuildingInfoClient;

public class DevourToEvoluteWindow extends HeroEvolveWindow {

	@Override
	public boolean goBack() {
		controller.closeAllPopup();

		BuildingInfoClient bar = Account.manorInfoClient.getBar();
		if (null != bar)
			new BarWindow().open();
		return true;
	}
}
