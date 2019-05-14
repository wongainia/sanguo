package com.vikings.sanguo.cache;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.ErrorCode;

public class ErrorCodeCache extends LazyLoadCache {

	private static final String FILE_NAME = "errcode.csv";

	private static final String UNKNOW_ERROR = Config.getController()
			.getString(R.string.unkown_error);

	@Override
	public Object fromString(String line) {
		return ErrorCode.fromString(line);
	}

	@Override
	public Object getKey(Object obj) {
		return ((ErrorCode) obj).getId();
	}

	@Override
	public String getName() {
		return FILE_NAME;
	}

	public String getMsg(short id) {
		checkLoad();
		try {
			if (!this.content.containsKey(id)) {
				return UNKNOW_ERROR+"("+id+")";
			} else {
				return ((ErrorCode) get(id)).getMsg();
			}
		} catch (GameException e) {
			return "";
		}
	}

}
