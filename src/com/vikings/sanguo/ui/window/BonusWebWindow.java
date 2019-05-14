package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.IDUtil;

public class BonusWebWindow extends DirectWindow {
	private String title;
	private String url;

	public void open(String title, String url) {
		this.title = title;
		this.url = url;
		doOpen();
	}

	@Override
	protected void init() {
		super.init(title);
	}

	@Override
	public String getUrl() {
		return url + "?v=" + IDUtil.getMyId() + "&sid=" + Config.serverId;
	}

}
