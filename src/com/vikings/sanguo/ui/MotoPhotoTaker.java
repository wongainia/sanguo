package com.vikings.sanguo.ui;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.vikings.sanguo.R;
import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;

public class MotoPhotoTaker extends PhotoTaker {

	public MotoPhotoTaker() {
		CAMERA_ICON_PX = 480;
		PICKED_ICON_PX = 300;
		CAMERA_COMPRESS = 80;
		PICKED_COMPRESS = 90;
	}

	@Override
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
		intent.putExtra("return-data", true);
		// intent.putExtra("outputFormat", "JPEG");
		// intent.putExtra("output", Uri.fromFile(icon));
		intent.putExtra("setWallpaper", false);
		Config.getController().getMainActivity()
				.startActivityForResult(intent, PICKED);
	}

	private void compress(Intent rs) {
		try {
			FileAccess fa = Config.getController().getFileAccess();
			icon = fa.readImage(IMG_FILE_NAME);
			if (!icon.exists())
				icon.createNewFile();
			Bitmap bitmap = rs.getParcelableExtra("data");
			FileOutputStream out = new FileOutputStream(icon);
			bitmap.compress(CompressFormat.JPEG, PICKED_COMPRESS, out);
			out.close();
			bitmap.recycle();
		} catch (Exception e) {
			Log.e(Config.getController().getString(
					R.string.MotoPhotoTaker_compress), e.getMessage(), e);
			Config.getController().alert(
					Config.getController().getString(
							R.string.MotoPhotoTaker_compress));
		}
	}

	@Override
	public void onReturn(final int requestCode, final int resultCode,
			final Intent data) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (resultCode != Activity.RESULT_OK) {
					// 点击返回时，不提示错误
					if (resultCode == Activity.RESULT_CANCELED) {
						return;
					}
					Config.getController().alert(
							Config.getController().getString(
									R.string.MotoPhotoTaker_onReturn));
					return;
				}
				if (requestCode == CAMERA) {
					if (!cut()) {
						return;
					}
				}
				if (requestCode == PICKED) {
					compress(data);
				}
				if (callBack != null)
					callBack.onCall();
			}
		});
	}

}
