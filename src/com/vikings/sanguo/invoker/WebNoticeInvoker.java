package com.vikings.sanguo.invoker;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.ui.alert.EventEntryTip;
import com.vikings.sanguo.ui.alert.WebNoticeTip;

public class WebNoticeInvoker extends BaseInvoker {
	private int notice;

	@Override
	protected String loadingMsg() {
		return "";
	}

	@Override
	protected String failMsg() {
		return "";
	}

	@Override
	protected void fire() throws GameException {
		try {
			notice = HttpConnector.getInstance().getNotice(Config.noticeUrl);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onOK() {
		if (showTip()) {
			new EventEntryTip(
					CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 6), "官方公告",
					null).show();
		}
	}

	private boolean showTip() {
		if (notice == 1)
			return true;
		else
			return false;
	}
}
