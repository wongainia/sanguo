package com.vikings.sanguo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.vikings.sanguo.battle.anim.DrawableAnimationBasis;
import com.vikings.sanguo.cache.BitmapCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.SaveBitmapInvoker;
import com.vikings.sanguo.model.BattleAnimEffects;

/**
 * 各种图片读取配置，读取方法
 * 
 * @author chenqing
 * 
 */
public class ImageUtil {

	public static BitmapFactory.Options opts_rgb565;
	public static BitmapFactory.Options opts_argb4444;
	public static BitmapFactory.Options opts_rgb565_generate;
	public static BitmapFactory.Options opts_argb4444_generate;

	public static BitmapFactory.Options opts_argb8888;

	public static BitmapFactory.Options opts_argb8888_generate;

	public static ColorMatrixColorFilter garyFilter = null;
	public static ColorMatrixColorFilter textHighLightFilter = null;
	public static ColorMatrixColorFilter wishFilter = null;
	public static ColorMatrixColorFilter reviveFilter = null;
	
	public final static int ARGB4444 = 2;       
	public final static int RGB565 = 3;  

	static {
		opts_rgb565 = new BitmapFactory.Options();
		opts_rgb565.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts_rgb565.inTargetDensity = Config.densityDpi;
		opts_rgb565.inPreferredConfig = Bitmap.Config.RGB_565;
		opts_rgb565.inPurgeable = true;
		opts_rgb565.inInputShareable = true;

		opts_argb4444 = new BitmapFactory.Options();
		opts_argb4444.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts_argb4444.inTargetDensity = Config.densityDpi;
		opts_argb4444.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opts_argb4444.inPurgeable = true;
		opts_argb4444.inInputShareable = true;

		opts_argb8888 = new BitmapFactory.Options();
		opts_argb8888.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts_argb8888.inTargetDensity = Config.densityDpi;
		opts_argb8888.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts_argb8888.inPurgeable = true;
		opts_argb8888.inInputShareable = true;

		opts_rgb565_generate = new BitmapFactory.Options();
		opts_rgb565_generate.inDensity = Config.densityDpi;
		opts_rgb565_generate.inTargetDensity = Config.densityDpi;
		opts_rgb565_generate.inPreferredConfig = Bitmap.Config.RGB_565;
		opts_rgb565_generate.inPurgeable = true;
		opts_rgb565_generate.inInputShareable = true;

		opts_argb4444_generate = new BitmapFactory.Options();
		opts_argb4444_generate.inDensity = Config.densityDpi;
		opts_argb4444_generate.inTargetDensity = Config.densityDpi;
		opts_argb4444_generate.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opts_argb4444_generate.inPurgeable = true;
		opts_argb4444_generate.inInputShareable = true;

		opts_argb8888_generate = new BitmapFactory.Options();
		opts_argb8888_generate.inDensity = Config.densityDpi;
		opts_argb8888_generate.inTargetDensity = Config.densityDpi;
		opts_argb8888_generate.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts_argb8888_generate.inPurgeable = true;
		opts_argb8888_generate.inInputShareable = true;

		ColorMatrix cm = new ColorMatrix();
		cm.set(new float[] { 0.308f, 0.609f, 0.082f, 0, 0, 0.308f, 0.609f,
				0.082f, 0, 0, 0.308f, 0.609f, 0.082f, 0, 0, 0, 0, 0, 1, 0 });
		garyFilter = new ColorMatrixColorFilter(cm);
		cm = new ColorMatrix();
		cm.set(new float[] { 1, 0, 0, 0, 100, 0, 1, 0, 0, 70, 0, 0, 1, 0, -16,
				0, 0, 0, 1, 0 });
		textHighLightFilter = new ColorMatrixColorFilter(cm);
		cm = new ColorMatrix();
		cm.set(new float[] { 1, 0, 0, 0, 100, 0, 1, 0, 0, -10, 0, 0, 1, 0, -90,
				0, 0, 0, 1, 0 });
		wishFilter = new ColorMatrixColorFilter(cm);

		cm = new ColorMatrix();
		cm.set(new float[] { 0.308f, 0.609f, 0.082f, 0, -24, 0.308f, 0.609f,
				0.082f, 0, -3, 0.308f, 0.609f, 0.082f, 0, 18, 0, 0, 0, 1, 0 });
		// cm.set(new float[] { 1, 0, 0, 0, -27, 0, 1, 0, 0, -13, 0, 0, 1, 0,
		// -7,
		// 0, 0, 0, 1, 0 });
		reviveFilter = new ColorMatrixColorFilter(cm);
	}

	public static Options getOptimizedOptions(String name, boolean generatedRes) {
		// 数字开头(用户照片) 或者 jpg的图片沒有透明 用rgb565
		if (name.toLowerCase().endsWith(".jpg")
				|| Character.isDigit(name.charAt(0)))
			return generatedRes ? opts_rgb565_generate : opts_rgb565;
		else {
			if (name.startsWith("mask")) {
				return generatedRes ? opts_argb8888_generate : opts_argb8888;
			} else {
				return generatedRes ? opts_argb4444_generate : opts_argb8888;
			}
		}
	}

	// 取带后缀的图片名
	private static String getImgNameWithSuffix(String name, String suffix) {
		String newName = "";
		int index = name.indexOf(".");
		if (index == -1)
			newName = name + suffix;
		else
			newName = name.substring(0, index) + suffix
					+ name.substring(index, name.length());
		return newName;
	}

	// 不带后缀的图片名
	public static String getImgNameWithoutSuffix(String imgName) {
		int index = imgName.indexOf('.');
		if (index >= 0)
			return imgName.substring(0, index);
		return imgName;
	}

	public static String imageMirrorName(String name) {
		return getImgNameWithSuffix(name, "_m");
	}

	public static Bitmap imageMirror(Bitmap bitmap, String name) {
		if (bitmap == null)
			return null;
		Matrix matrix = new Matrix();
		matrix.setScale(-1, 1);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		try {
			FileOutputStream out = new FileOutputStream(Config.getController()
					.getFileAccess().readImage(imageMirrorName(name)));
			b.compress(CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
		}
		return b;
	}

	public static Bitmap imageMirrorRotate(Bitmap bitmap, String name,
			int horiMirror, int veriMirror, int degree, float xScale,
			float yScale, boolean isSave) {
		if (bitmap == null)
			return null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		if (horiMirror == 1) {
			matrix.postScale(-1, 1,width/2,height/2);
		}
		if (veriMirror == 1) {
			matrix.postScale(1, -1,width/2,height/2);
		}

		if (degree > 0) {
			matrix.postRotate(degree, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);
		}

		if (xScale != 100f && yScale != 100f) {
			matrix.postScale(xScale / 100, yScale / 100);
		}

		Bitmap b = null;
		try {
			b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			if (b == null) {
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

		if (isSave) {
			String fileName = imageMirrorName(name) + "_" + degree;
			new SaveBitmapInvoker(b, fileName).start();

			// try
			// {
			// FileOutputStream out = new
			// FileOutputStream(Config.getController()
			// .getFileAccess()
			// .readImage(imageMirrorName(name) + "_" + degree));
			// b.compress(CompressFormat.PNG, 100, out);
			// out.close();
			// }
			// catch (IOException e)
			// {
			// e.printStackTrace();
			// }
		}
		return b;
	}

	public static String imageRotateName(String name, float degrees) {
		return getImgNameWithSuffix(name, "_r" + ((int) degrees));
	}

	public static Bitmap imageRotate(Bitmap bitmap, String name, float degrees) {
		if (bitmap == null)
			return null;
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees, (float) bitmap.getWidth() / 2,
				(float) bitmap.getHeight() / 2);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		try {
			FileOutputStream out = new FileOutputStream(Config.getController()
					.getFileAccess().readImage(imageRotateName(name, degrees)));
			b.compress(CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
		}
		return b;
	}

	public static String imageScaleName(String name, int xScale, int yScale) {
		String newName;
		int index = name.indexOf(".");
		if (index == -1)
			newName = name + "_" + xScale + "_" + yScale;
		else
			newName = name.substring(0, index) + "_" + xScale + "_" + yScale
					+ name.substring(index, name.length());
		return newName;
	}

	public static String imageScaleName(String name, String suffix) {
		return getImgNameWithSuffix(name, "_" + suffix);
	}

	// TODO 启线程保存
	public static Bitmap imageScale(Bitmap bitmap, String name, float xScale,
			float yScale) {
		if (bitmap == null)
			return null;
		Bitmap b = null;
		try {
			b = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()
					* xScale / 100), (int) (bitmap.getHeight() * yScale / 100),
					true);
			if (b == null) {
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			if (bitmap.isRecycled() == false) {
				bitmap.recycle();
				bitmap = null;
			}
			return null;
		}
		// String fileName = imageScaleName(name, (int) xScale,(int) yScale);
		// new SaveBitmapInvoker(b, fileName).start();
		try {
			FileOutputStream out = new FileOutputStream(
					Config.getController()
							.getFileAccess()
							.readImage(
									imageScaleName(name, (int) xScale,
											(int) yScale)));
			b.compress(CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
		}
		return b;
	}

	public static Bitmap imageScale(Bitmap bitmap, String name, float xScale,
			float yScale, String post) {
		if (bitmap == null)
			return null;
		Bitmap b = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()
				* xScale / 100), (int) (bitmap.getHeight() * yScale / 100),
				true);
		try {
			FileOutputStream out = new FileOutputStream(Config.getController()
					.getFileAccess().readImage(imageScaleName(name, post)));
			b.compress(CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
		}
		return b;
	}

	public static Bitmap getResImage(String name) {
		return getImage(name, ImageUtil.getOptimizedOptions(name, false));
	}

	public static Bitmap getGenerateImage(String name) {
		return getImage(name, ImageUtil.getOptimizedOptions(name, true));
	}
	
	public static Bitmap getGenerateImage(String name,Options options) {
		return getImage(name, options);
	}

	static private boolean isConfigEqual(android.graphics.Bitmap.Config c1,
			android.graphics.Bitmap.Config c2) {
		if (c1 == null && c2 == null)
			return true;
		if (c1 == null && c2 != null)
			return false;
		if (c1 != null && c2 == null)
			return false;
		return c1.equals(c2);
	}

	static private Bitmap getImage(String name, BitmapFactory.Options options) {
		try {
			File f = Config.getController().getFileAccess().readImage(name);
			if (f.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
						options);
				if (bitmap != null
						&& !isConfigEqual(bitmap.getConfig(),
								options.inPreferredConfig)) {
					Bitmap tmp = bitmap;
					bitmap = tmp.copy(options.inPreferredConfig, false);
					tmp.recycle();
				}
				return bitmap;
			}
		} catch (Exception e) {
			Log.e("ImageUtil", e.getMessage(), e);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static Drawable center(Drawable src) {
		int h = src.getIntrinsicHeight();
		int w = src.getIntrinsicWidth();
		src.setBounds(-w / 2, -h / 2, w / 2, h / 2);
		return src;
	}

	public static Drawable centerBottom(Drawable src) {
		int h = src.getIntrinsicHeight();
		int w = src.getIntrinsicWidth();
		src.setBounds(-w / 2, -h, w / 2, 0);
		return src;
	}

	public static void setBgGray(View v) {
		if (v == null)
			return;
		Drawable d = v.getBackground();
		if (d == null)
			return;
		if (d instanceof NinePatchDrawable) {
			d.setColorFilter(garyFilter);
			v.setBackgroundDrawable(d);
		} else if (d instanceof BitmapDrawable) {
			Drawable gary = new BitmapDrawable(Config.getController()
					.getResources(), ((BitmapDrawable) d).getBitmap());
			gary.setColorFilter(garyFilter);
			v.setBackgroundDrawable(gary);
		}
	}

	public static void setBgNormal(View v) {
		if (v == null)
			return;
		Drawable d = v.getBackground();
		if (d == null)
			return;
		if (d instanceof NinePatchDrawable || d instanceof BitmapDrawable) {
			d.clearColorFilter();
			v.setBackgroundDrawable(d);
			v.invalidate();
		}
	}

	public static void setReviveBg(View v) {
		Drawable d = v.getBackground();
		if (d != null && d instanceof NinePatchDrawable) {
			d.setColorFilter(reviveFilter);
			v.setBackgroundDrawable(d);
			return;
		}

		if (d == null || !(d instanceof BitmapDrawable))
			return;
		Drawable gary = new BitmapDrawable(Config.getController()
				.getResources(), ((BitmapDrawable) d).getBitmap());
		gary.setColorFilter(reviveFilter);

		v.setBackgroundDrawable(gary);
	}

	public static void setAlpha(View v, int alphaCount) {
		Drawable d = v.getBackground();
		if (d == null || !(d instanceof BitmapDrawable))
			return;
		Drawable alpha = new BitmapDrawable(Config.getController()
				.getResources(), ((BitmapDrawable) d).getBitmap());
		alpha.setAlpha(alphaCount);
		v.setBackgroundDrawable(alpha);
	}
	/* 重叠两张图片 */
	public static Bitmap combinePictures(Bitmap bmpBottom, Bitmap bmpTop,
			float x, float y) {// 参数依次为：下层图像，上层图像，返回图像宽度，返回图像高度，上层图像 x 坐标，上层图像 y
								// 坐标，像素密度
		Canvas canvas = new Canvas(bmpBottom);
		canvas.drawBitmap(bmpTop, x * Config.SCALE_FROM_HIGH, y
				* Config.SCALE_FROM_HIGH, null);
		return bmpBottom;
	}

	public static BitmapDrawable getMirrorBitmapDrawable(String name) {
		Bitmap bmp = Config.getController().getMirrorBitmap(name);
		BitmapDrawable d = new BitmapDrawable(Config.getController()
				.getUIContext().getResources(), bmp);
		return d;
	}

	// 镜像 同时旋转
	public static BitmapDrawable getMirrorRotateBitmapDrawable(String name,
			int horiMirrorType, int vericalMirror, int degree, int xScale,
			int yScale, boolean isSave) {
		BitmapDrawable d = null;
		Bitmap bmp = Config.getController().getMirrorRotateBitmap(name,
				horiMirrorType, vericalMirror, degree, xScale, yScale, isSave);
		// Config.getController().getDrawable(suiff+id,xScale,yScale);
		if (bmp != null) {
			d = new BitmapDrawable(Config.getController().getUIContext()
					.getResources(), bmp);
		}
		return d;
	}
	
	public static DrawableAnimationBasis createAnimationDrawable(
			BattleAnimEffects battleAnimEffects) {
		if (battleAnimEffects == null) {
			return null;
		}
		String seq = battleAnimEffects.getSeqFrame();
		if (seq == null || seq.length() == 0) {
			return null;
		}
		DrawableAnimationBasis animationDrawable = new DrawableAnimationBasis(battleAnimEffects);
		
		int size = seq.length();
	
		for (int id = 0; id < size; id++) {
			Drawable drawable = null;
			char index = seq.charAt(id);
			String imgName = battleAnimEffects.getIcon();
			int during = 0;
			if (id == 0) {
				during = battleAnimEffects.getDuring() / 4;
			} else {
				during = battleAnimEffects.getDuring();
			}
			System.gc();
					
			drawable = Config.getController().getDrawable(
					imgName + "_" + (index) + ".png");
			
			
			if (drawable != null) 
			{
				animationDrawable.addFrame(drawable, during);
			}
			else {
				System.gc();
				int animCount = animationDrawable.getNumberOfFrames();
				for (int dex = 0; dex < animCount; dex++) {
					Drawable d = animationDrawable.getFrame(dex);
					if (d != null) {
						if (d instanceof BitmapDrawable) {
							BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
							Bitmap bmp = bitmapDrawable.getBitmap();
							if (bmp != null && bmp.isRecycled() == false) {
								bmp.recycle();
								bmp = null;
							}
						}
					}
					BitmapCache bitmapCache = Config.getController().getBitmapCache();
					String img = "";
					if (battleAnimEffects.getHoriMirror() != 0
							|| battleAnimEffects.getHoriMirror() != 0
							|| battleAnimEffects.getVerticalMirror() > 0) {
						img = battleAnimEffects.getIcon() + "_" + index
								+ ".png" + "_"
								+ battleAnimEffects.getRotateDegress();
					} else {
						String icon = battleAnimEffects.getIcon() + "_" + index
								+ ".png";
						img = imageScaleName(icon,
								(int) battleAnimEffects.getXScale(),
								(int) battleAnimEffects.getYScale());
					}
					Bitmap bmp = bitmapCache.get(img);
					if (bmp != null && bmp.isRecycled() == false) {
						bitmapCache.remove(img);
					}
				}
				return null;
			}
		}
	
		animationDrawable.setOneShot(true);
		return animationDrawable;
	}
	public static BitmapDrawable getRotateBitmapDrawable(String name,
			float degrees) {
		Bitmap bmp = Config.getController().getRotateBitmap(name, degrees);
		BitmapDrawable d = new BitmapDrawable(Config.getController()
				.getUIContext().getResources(), bmp);
		return d;
	}

	public static String getNumStr(ArrayList<Integer> list, String pre) {
		StringBuilder builder = new StringBuilder();
		if (null != list && list.size() > 0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				switch (list.get(i)) {
				case 0:
					builder.append("#" + pre + "0#");
					break;
				case 1:
					builder.append("#" + pre + "1#");
					break;
				case 2:
					builder.append("#" + pre + "2#");
					break;
				case 3:
					builder.append("#" + pre + "3#");
					break;
				case 4:
					builder.append("#" + pre + "4#");
					break;
				case 5:
					builder.append("#" + pre + "5#");
					break;
				case 6:
					builder.append("#" + pre + "6#");
					break;
				case 7:
					builder.append("#" + pre + "7#");
					break;
				case 8:
					builder.append("#" + pre + "8#");
					break;
				case 9:
					builder.append("#" + pre + "9#");
					break;
				case -1: // 万
					builder.append("#" + pre + "w#");
					break;

				case -2:// 小数点
					builder.append("#" + pre + "dot#");
					break;
				}
			}
		}

		return builder.toString();
	}

	public static String getMaskName(String srcName) {
		return "mask_" + srcName;
	}

	/**
	 * 将图src中非透明的地方全部变成黑色，然后生成一张同大小的图
	 * 
	 * @param src
	 *            原图 jpg
	 * @return 遮罩中黑色部位保留原图 其他地方透明
	 */
	public static Bitmap getMaskBitmap(String srcName) {
		String name = getMaskName(srcName);
		Bitmap b = getGenerateImage(name);
		if (b != null) {
			Config.getController().getBitmapCache().save(name, b);
			return b;
		}

		Bitmap bitmap = getImage(srcName, opts_argb8888);

		if (null == bitmap) {
			int srcID = Config.getController().findResId(srcName, "drawable");

			InputStream is = Config.getController().getResources()
					.openRawResource(srcID);
			bitmap = BitmapFactory.decodeStream(is, null, opts_argb8888);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] picPixels = new int[w * h];

		bitmap.getPixels(picPixels, 0, w, 0, 0, w, h);

		for (int i = 0; i < picPixels.length; i++) {
			if (picPixels[i] != 0x00000000)
				picPixels[i] = 0xff000000;

		}
		Bitmap resultBitmap = Bitmap.createBitmap(picPixels, w, h,
				Bitmap.Config.ARGB_8888);
		resultBitmap.setDensity(bitmap.getDensity());
		Config.getController().getBitmapCache().save(name, resultBitmap);
		bitmap.recycle();
		bitmap.recycle();
		Config.getController().getFileAccess().saveImage(resultBitmap, name);
		return resultBitmap;
	}

	/**
	 * 根据2张jpg 生成带透明的png图， 遮罩中黑色部位保留原图 其他地方透明 2个图片必要要一样大
	 * 
	 * @param src
	 *            原图 jpg
	 * @param mask
	 *            黑白遮罩 jpg
	 * @return 遮罩中黑色部位保留原图 其他地方透明
	 */
	public static Bitmap getMaskBitmap(String srcName, String maskName) {
		String name = "mask_" + srcName + ".png";
		Bitmap b = getGenerateImage(name);
		if (b != null) {
			Config.getController().getBitmapCache().save(name, b);
			return b;
		}
		int srcID = Config.getController().findResId(srcName, "drawable");

		int maskId = Config.getController().findResId(maskName, "drawable");

		InputStream is = Config.getController().getResources()
				.openRawResource(srcID);
		Bitmap bg = BitmapFactory.decodeStream(is, null, opts_argb8888);
		is = Config.getController().getResources().openRawResource(maskId);
		Bitmap mask = BitmapFactory.decodeStream(is, null, opts_argb8888);

		int w = mask.getWidth();
		int h = mask.getHeight();

		int[] picPixels = new int[w * h];
		int[] maskPixels = new int[w * h];

		bg.getPixels(picPixels, 0, w, 0, 0, w, h);
		mask.getPixels(maskPixels, 0, w, 0, 0, w, h);

		for (int i = 0; i < maskPixels.length; i++) {
			// if(maskPixels[i] == 0xff000000){
			// picPixels[i] = 0;
			// }else if(maskPixels[i] == 0){
			// //donothing
			// }else{
			// //把mask的a通道应用与picBitmap
			// maskPixels[i] &= 0xff000000;
			// maskPixels[i] = 0xff000000 - maskPixels[i];
			// picPixels[i] &= 0x00ffffff;
			// picPixels[i] |= maskPixels[i];
			// }

			if (maskPixels[i] != 0xff000000) {
				picPixels[i] = 0x00000000;
			}
		}
		Bitmap resultBitmap = Bitmap.createBitmap(picPixels, w, h,
				Bitmap.Config.ARGB_8888);
		resultBitmap.setDensity(bg.getDensity());
		Config.getController().getBitmapCache().save(name, resultBitmap);
		bg.recycle();
		mask.recycle();
		Config.getController().getFileAccess().saveImage(resultBitmap, name);
		return resultBitmap;
	}

	// public static Drawable getGambleBg() {
	// Bitmap bmp = Config.getController().getBitmap(R.drawable.gamble_tile);
	// BitmapDrawable d = new BitmapDrawable(Config.getController()
	// .getResources(), bmp);
	// d.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	// d.setDither(true);
	// return d;
	// }

	// degree mirrror
	public static Drawable imageDegreeMirror(int resId, int degree, int xScale,
			int yScale) {
		Bitmap bitmap = Config.getController().getBitmap(resId);
		Matrix matrix = new Matrix();
		if (bitmap == null) {
			return null;
		}
		matrix.setScale(-1, 1);
		matrix.postRotate(degree);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		BitmapDrawable d = null;
		if (xScale != 1 && yScale != 1) {
			Bitmap b = Bitmap.createScaledBitmap(bmp,
					(int) (bitmap.getWidth() * xScale),
					(int) (bitmap.getHeight() * yScale), true);

			d = new BitmapDrawable(Config.getController().getUIContext()
					.getResources(), b);
		} else {
			d = new BitmapDrawable(Config.getController().getUIContext()
					.getResources(), bmp);
		}
		return d;
	}
	
	public static BitmapFactory.Options getBitmapOption(int type)
	{
		if(type == ARGB4444)
		{
			return opts_argb4444_generate;
		}
		else if(type == RGB565)
		{
			return opts_rgb565_generate;
		}
		return opts_argb8888_generate;
	}

}
