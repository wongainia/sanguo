package com.vikings.sanguo.cache;

import java.util.HashMap;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;

abstract public class LazyLoadCache extends FileCache {

	private boolean isInit = false;

	@Override
	public synchronized void init() throws GameException {
		content = new HashMap();
	}

	public void checkLoad() {
		checkLoad(true);
	}

	public void checkLoad(boolean showErrorMsg) {
		if (!isInit)
			try {
				super.init();
				isInit = true;
			} catch (GameException e) {
				if (showErrorMsg) {
					final String msg = e.getMessage();
					Config.getController().postRunnable(new Runnable() {

						@Override
						public void run() {
							Config.getController().alert("", msg, null, false);
						}
					});
				}
			}
	}

	public boolean isInit() {
		return isInit;
	}

	@Override
	public Object get(Object key) throws GameException {
		checkLoad();
		return super.get(key);
	}

	protected HashMap getContent() {
		checkLoad();
		return content;
	}

}
