/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-9 上午10:19:35
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.cache;

import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.UITextProp;
import com.vikings.sanguo.utils.StringUtil;

public class UITextPropCache extends FileCache {
	private static final String NAME = "prop_ui_text.csv";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Object getKey(Object obj) {
		return ((UITextProp) obj).getId();
	}

	@Override
	public Object fromString(String line) {
		return UITextProp.fromString(line);
	}

	public String getTxt(int id) {
		try {
			return ((UITextProp) get(id)).getTxt();
		} catch (GameException e) {
			return "";
		}
	}

	public int getInt(int id) {
		try {
			String v = ((UITextProp) get(id)).getTxt();
			if (StringUtil.isNull(v))
				return 0;
			return Integer.valueOf(v);
		} catch (Exception e) {
			return 0;
		}
	}
}
