package com.vikings.sanguo.ui;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.controller.GameController;

/**
 * 设置可回收的图片资源 调用setBg(view,img),回收时调用recycle()
 * 
 * 仅当本窗口单独使用的资源使用ImageHolder来绑定图片，共用图片直接写在xml
 * 
 * @author Brad.Chen
 * 
 */
public class ImageHolder {

	private LinkedList<View> holder;

	public void setBg(View v, int resId) {
		v.setBackgroundDrawable(getDrawable(resId));
		saveRef(v);
	}

	public void setBg(View v, Drawable drawable) {
		v.setBackgroundDrawable(drawable);
		saveRef(v);
	}

	public BitmapDrawable getDrawable(int resId) {
		GameController gc = Config.getController();
		Bitmap bitmap = BitmapFactory.decodeResource(gc.getResources(), resId);
		return new BitmapDrawable(gc.getResources(), bitmap);
	}

	public void saveRef(View v) {
		if (this.holder == null)
			holder = new LinkedList<View>();
		holder.add(v);
	}

	public void recycle() {
		if (holder == null)
			return;
		for (View v : holder) {
			Drawable d = v.getBackground();
			if (d == null)
				return;
			v.setBackgroundDrawable(null);
			if (d instanceof BitmapDrawable) {
				recycleBitmapDrawable((BitmapDrawable) d);
			} else if (d instanceof AnimationDrawable) {
				((AnimationDrawable) d).stop();
				int frames = ((AnimationDrawable) d).getNumberOfFrames();
				for (int i = 0; i < frames; i++) {
					Drawable frameDrawable = ((AnimationDrawable) d)
							.getFrame(i);
					if (frameDrawable instanceof BitmapDrawable)
						recycleBitmapDrawable((BitmapDrawable) frameDrawable);
				}
			}
		}
		holder.clear();
		holder = null;
	}

	private void recycleBitmapDrawable(BitmapDrawable drawable) {
		Bitmap b = drawable.getBitmap();
		if (b != null && !b.isRecycled())
			b.recycle();
	}
}
