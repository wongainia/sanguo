package com.vikings.sanguo.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class BitmapCache {

	private HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

	public Bitmap get(String name) {
		if (cache.containsKey(name)) {
			SoftReference<Bitmap> ref = cache.get(name);
			// 引用被回收 或者bitmap被回收 去掉key
			if (ref == null || ref.get() == null || ref.get().isRecycled()) {
				cache.remove(name);
			} else {
				// 返回缓存的bitmap
				return ref.get();
			}
		}
		return null;
	}

	public void remove(String name) {
		// Log.e("BitmapCache", name);
		SoftReference<Bitmap> ref = cache.remove(name);
		if (ref != null) {
			Bitmap b = ref.get();
			if (b != null && !b.isRecycled()) {
				b.recycle();
			}
			b = null;
			ref.clear();
			ref = null;
		}
	}

	public void save(String name, Bitmap b) {
		cache.put(name, new SoftReference<Bitmap>(b));
	}
}
