package com.vikings.sanguo.config;

import java.util.Arrays;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.vikings.sanguo.cache.CacheMgr;

public class Verify {

	public static void check() {
		try {
			byte[] data = Config
					.getController()
					.getMainActivity()
					.getPackageManager()
					.getPackageInfo(
							Config.getController().getMainActivity()
									.getPackageName(),
							PackageManager.GET_SIGNATURES).signatures[0]
					.toByteArray();
			if (!Arrays.equals(data, Config.check)) {
				Config.getController().alert(
						CacheMgr.dictCache.getDict(1103, 1));
			}
		} catch (NameNotFoundException e) {
		}
	}
}
