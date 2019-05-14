package com.vikings.sanguo.config;

import android.util.DisplayMetrics;

public class DipUtil {

	private static final int base_w = 480;

	/**
	 * 根据屏幕像素 强制调整缩放比
	 * 
	 * @param dm
	 */
	public static void adjustDisplayMetrics(DisplayMetrics dm) {
		int w = dm.widthPixels;
		// 和480分辨率的比例
		dm.densityDpi = DisplayMetrics.DENSITY_HIGH * w / base_w;

		dm.density = dm.densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT;

		dm.scaledDensity = dm.density;

		// // test
		// Display display = activity.getWindowManager().getDefaultDisplay();
		// try {
		// Field f = display.getClass().getDeclaredField("mDensity");
		// f.setAccessible(true);
		// f.set(display, dm.density);
		// } catch (SecurityException e) {
		// e.printStackTrace();
		// } catch (NoSuchFieldException e) {
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * dip转px
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		return (int) (dipValue * Config.SCALE_FROM_HIGH + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int px2dip(float pxValue) {
		return (int) (pxValue / Config.SCALE_FROM_HIGH + 0.5f);
	}

}
