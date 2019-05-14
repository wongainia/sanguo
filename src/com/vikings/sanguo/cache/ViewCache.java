package com.vikings.sanguo.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.view.View;

public class ViewCache {

	private HashMap<Integer, WeakReference<View>> cache = new HashMap<Integer, WeakReference<View>>();

	public View get(int resId) {
		if (cache.containsKey(resId)) {
			WeakReference<View> ref = cache.get(resId);
			// 引用被回收 或者bitmap被回收 去掉key
			if (ref == null || ref.get() == null) {
				cache.remove(resId);
			} else {
				return ref.get();
			}
		}
		return null;
	}

	public void remove(int resId) {
		WeakReference<View> ref = cache.remove(resId);
		if (ref != null) {
			ref.clear();
			ref = null;
		}
	}

	public void save(int resId, View v) {
		cache.put(resId, new WeakReference<View>(v));
	}

}
