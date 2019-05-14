/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-29 下午2:02:50
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.invoker;

import java.util.List;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.GambleData;
import com.vikings.sanguo.model.ItemWeight;
import com.vikings.sanguo.thread.ImageLoader;
import com.vikings.sanguo.thread.CallBack;

public class LoadGambleImgInvoker extends BaseInvoker {
	private List<GambleData> gambleData;
	private CallBack callBack;

	public LoadGambleImgInvoker(List<GambleData> gambleData, CallBack callBack) {
		this.gambleData = gambleData;
		this.callBack = callBack;
	}

	@Override
	protected String failMsg() {
		return "请稍候重试...";
	}

	@Override
	protected void fire() throws GameException {
		for (GambleData it : gambleData) {
			if (null != it.getItem())
				ImageLoader.getInstance().downloadInCase(
						it.getItem().getImage(), Config.imgUrl);
			else {
				ItemWeight weight = (ItemWeight) CacheMgr.weightCache.get(it
						.getItemId());
				ImageLoader.getInstance().downloadInCase(weight.getIcon(),
						Config.imgUrl);
			}
		}
	}

	@Override
	protected String loadingMsg() {
		return "请稍后...";
	}

	@Override
	protected void onOK() {
		if (null != callBack)
			callBack.onCall();
	}

}
