package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;

public class SkillDirectWindow extends DirectWindow {

	@Override
	protected void init() {
		super.init("效果说明");
	}

	@Override
	public String getUrl() {
		return CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 40);
	}
}
