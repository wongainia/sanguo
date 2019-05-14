/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-25 下午6:06:27
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.window;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.Dict;

public class ActClearDirectWindow extends DirectWindow{

	@Override
	protected void init() {
		super.init("扫荡副本说明");
	}
	
	@Override
	public String getUrl() {
		return CacheMgr.dictCache.getDict(Dict.TYPE_CLEAR_ACT, (byte) 2);
	}
}