/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Author : danping peng
 *
 *  Comment : 
 */
package com.vikings.sanguo.utils;

import android.util.Log;

public class LogDebug {
	private static Boolean isOpen = true;
	private static final String TAG = "handan";

	public static final int e = 1;
	public static final int i = 2;
	public static final int v = 3;
	public static final int d = 4;
	public static final int w = 5;

	public static void Log(int LogLevel, String tag, String msg) {
		if (isOpen) {
			switch (LogLevel) {
			case e:
				Log.e(tag == null ? TAG : tag, msg);
				break;
			case i:
				Log.i(tag == null ? TAG : tag, msg);
				break;

			case v:
				Log.v(tag == null ? TAG : tag, msg);
				break;

			case d:
				Log.d(tag == null ? TAG : tag, msg);
				break;

			case w:
				Log.w(tag == null ? TAG : tag, msg);
				break;

			}
		}

	}

	public static void LogE(String msg) {
		if (isOpen) {
			Log.e(TAG, msg);
		}

	}

}