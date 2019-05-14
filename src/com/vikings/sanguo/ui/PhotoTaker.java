package com.vikings.sanguo.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.thread.CallBack;

public class PhotoTaker {

	private static final int DICT_TYPE = 99999;
	private static final int DICT_VALUE = 1;

	public static PhotoTaker getInstance() {
		if (Config.screenWidth < 480)
			return new MotoPhotoTaker();
		String[] types = CacheMgr.dictCache.getDict(DICT_TYPE, DICT_VALUE)
				.split("\\|");
		for (String s : types) {
			if (Build.MANUFACTURER.equalsIgnoreCase(s))
				return new MotoPhotoTaker();
		}
		return new PhotoTaker();
	}

	protected int CAMERA = 1000;

	protected int PICKED = 1001;

	protected final static String IMG_FILE_NAME = "tmp_icon.png";
	protected final static String CAMERA_FILE_NAME = "tmp_camera.png";

	protected int CAMERA_ICON_PX = 480;

	protected int PICKED_ICON_PX = 480;

	protected int CAMERA_COMPRESS = 80;

	protected int PICKED_COMPRESS = 80;

	protected File cameraTmp;

	protected File icon;

	protected Handler handler = new Handler();

	protected CallBack callBack;

	public void camera() {
		FileAccess fa = Config.getController().getFileAccess();
		if (fa.getSdCardDir() == null) {
			Config.getController().alert(
					Config.getController()
							.getString(R.string.PhotoTaker_camera));
			return;
		}
		cameraTmp = new File(fa.getSdCardDir(), CAMERA_FILE_NAME);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraTmp));
		Config.getController().getMainActivity()
				.startActivityForResult(intent, CAMERA);
	}

	public void pickFromGallery() {
		try {
			icon = Config.getController().getFileAccess()
					.readImage(IMG_FILE_NAME);
			if (!icon.exists())
				icon.createNewFile();

		} catch (Exception e) {
			Config.getController().alert(
					Config.getController().getString(
							R.string.PhotoTaker_pickFromGallery));
			return;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", PICKED_ICON_PX);
		intent.putExtra("outputY", PICKED_ICON_PX);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("output", Uri.fromFile(icon));
		Config.getController().getMainActivity()
				.startActivityForResult(intent, PICKED);
	}

	private void compress() {
		try {
			FileAccess fa = Config.getController().getFileAccess();
			icon = fa.readImage(IMG_FILE_NAME);
			if (!icon.exists())
				icon.createNewFile();
		} catch (Exception e) {
			Config.getController().alert(
					Config.getController().getString(
							R.string.PhotoTaker_pickFromGallery));
			return;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts.inTargetDensity = DisplayMetrics.DENSITY_HIGH;
		Bitmap bmp = BitmapFactory.decodeFile(icon.getAbsolutePath(), opts);
		try {
			FileOutputStream out = new FileOutputStream(icon);
			bmp.compress(CompressFormat.JPEG, PICKED_COMPRESS, out);
			out.close();
		} catch (Exception e) {
			Config.getController().alert(
					Config.getController().getString(
							R.string.PhotoTaker_pickFromGallery));
		}
	}

	// 居中裁剪
	public boolean cut() {
		try {
			FileAccess fa = Config.getController().getFileAccess();
			cameraTmp = new File(fa.getSdCardDir(), CAMERA_FILE_NAME);
			icon = fa.readImage(IMG_FILE_NAME);
			if (!icon.exists())
				icon.createNewFile();

		} catch (Exception e) {
			Config.getController().alert(
					Config.getController().getString(
							R.string.PhotoTaker_pickFromGallery));
			return false;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts.inTargetDensity = DisplayMetrics.DENSITY_HIGH;
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(cameraTmp.getAbsolutePath(),
				opts);
		int srcW = opts.outWidth;
		int srcH = opts.outHeight;
		int inSampleSize = (srcW > srcH ? srcH : srcW) / CAMERA_ICON_PX;
		if (inSampleSize < 1)
			inSampleSize = 1;
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = inSampleSize;
		bitmap = BitmapFactory.decodeFile(cameraTmp.getAbsolutePath(), opts);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		bitmap = Bitmap.createBitmap(bitmap, (w - CAMERA_ICON_PX) / 2,
				(h - CAMERA_ICON_PX) / 2, CAMERA_ICON_PX, CAMERA_ICON_PX);
		try {
			FileOutputStream out = new FileOutputStream(icon);
			bitmap.compress(CompressFormat.JPEG, CAMERA_COMPRESS, out);
			out.close();
		} catch (IOException e) {
			Config.getController().alert(
					Config.getController().getString(
							R.string.PhotoTaker_pickFromGallery));
			return false;
		}
		return true;
	}

	public void onReturn(final int requestCode, final int resultCode,
			final Intent data) {
		handler.post(new Runnable() {
			@Override
			public void run() {

				// Toast.makeText(Config.getController().getMainActivity(),
				// resultCode+"", Toast.LENGTH_LONG).show();

				if (resultCode != Activity.RESULT_OK) {
					// 点击返回时，不提示错误
					if (resultCode == Activity.RESULT_CANCELED) {
						return;
					}
					Config.getController().alert(
							Config.getController().getString(
									R.string.PhotoTaker_onReturn));
					return;
				}
				if (requestCode == CAMERA) {
					if (!cut()) {
						return;
					}
				}
				if (requestCode == PICKED) {
					compress();
				}
				if (callBack != null)
					callBack.onCall();
			}
		});
	}

	public File getFile() {
		try {
			return Config.getController().getFileAccess()
					.readImage(IMG_FILE_NAME);
		} catch (Exception e) {
			return null;
		}
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

}
