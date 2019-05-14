package com.vikings.sanguo.cache;

import java.util.HashMap;
import java.util.List;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;

abstract public class LazyLoadArrayCache extends ArrayFileCache{

	private boolean isInit = false; 
	
	@Override
	public synchronized void init() throws GameException {
		content = new HashMap();
	}
	
	public void checkLoad(){
		if(!isInit)
			try {
				super.init();
				isInit = true;
			} catch (GameException e) {
				final String msg =  e.getMessage();
				Config.getController().postRunnable(new Runnable() {
					
					@Override
					public void run() {
						Config.getController().alert("", msg, null, false);
					}
				});
			}
	}
	
	public boolean isInit() {
		return isInit;
	}
	
	
	@Override
	public Object search(long key1, long key2) {
		checkLoad();
		return super.search(key1, key2);
	}
	
	@Override
	public List search(long key1) {
		checkLoad();
		return super.search(key1);
	}
}
