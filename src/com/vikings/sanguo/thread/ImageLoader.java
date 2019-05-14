package com.vikings.sanguo.thread;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.utils.ImageUtil;

/**
 * 网络加载图片
 * 
 * @author Brad.Chen
 * 
 */
public class ImageLoader {

	private static final ImageLoader instance = new ImageLoader();

	FileAccess fa = Config.getController().getFileAccess();

	private Handler handler = new Handler();

	private ImageLoader() {
		new Thread(new Worker(), "loadImage").start();
	}

	public static ImageLoader getInstance() {
		if (instance == null)
			return new ImageLoader();
		else
			return instance;
	}

	public void downloadInCase(String name, String url) throws GameException {
		Drawable d = Config.getController().getDrawable(name);
		if (d != null)
			return;
		downloadImage(name, url);
	}

	private LinkedList<ImageCallBack> taskList = new LinkedList<ImageCallBack>();

	public void setImage(ImageCallBack obj) {
		Drawable d = Config.getController().getDrawable(obj.getImgName());
		if (d != null) {
			obj.setImage(d);
			return;
		}
		if (obj.getFailCallBack() != null) {

		} else {
			obj.setImage(obj.getStub());
		}
		synchronized (taskList) {
			taskList.addLast(obj);
			taskList.notifyAll();
		}
	}

	private void downloadImage(String imageName, String url)
			throws GameException {
		for (int i = 0; i < 3; i++) {
			try {
				byte[] buf = HttpConnector.getInstance().httpGetBytes(
						url + imageName);
				if (buf != null && buf.length != 0) {
					fa.saveImage(buf, imageName);
					return;
				}
			} catch (IOException e) {
			}
		}
		throw new GameException("can not download img:" + imageName);
	}

	private class Worker implements Runnable {

		@Override
		public void run() {
			while (true) {
				ImageCallBack task = null;
				synchronized (taskList) {
					if (taskList.size() != 0) {
						task = taskList.getFirst();
					} else {
						try {
							taskList.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (task == null)
					continue;
				boolean rs = false;
				try {
					downloadImage(task.getImgName(), task.getUrl());
					rs = true;
				} catch (GameException e) {
				}
				Drawable drawable = Config.getController().getDrawable(
						task.getImgName());
				synchronized (taskList) {
					for (Iterator<ImageCallBack> iterator = taskList.iterator(); iterator
							.hasNext();) {
						ImageCallBack ic = (ImageCallBack) iterator.next();
						if (ic.equals(task)) {
							iterator.remove();
							if (rs && drawable != null) {
								Drawable d = drawable;
								handler.post(new SetImage(d, ic));
							} else {
								if (ic.getFailCallBack() != null) {
									handler.post(new FailCallBack(ic
											.getFailCallBack()));

								}
							}
						}
					}
				}
			}
		}
	}

	private class FailCallBack implements Runnable {
		CallBack callBack;

		public FailCallBack(CallBack callBack) {
			this.callBack = callBack;
		}

		@Override
		public void run() {
			if (null != callBack)
				callBack.onCall();

		}

	}

	private class SetImage implements Runnable {

		Drawable d;

		ImageCallBack ic;

		public SetImage(Drawable d, ImageCallBack ic) {
			this.d = d;
			this.ic = ic;
		}

		@Override
		public void run() {
			ic.setImage(d);
		}

	}
	
	//16位bitmap
	public void setImageLowQuality(ImageCallBack obj) {
		Drawable d = Config.getController().getDrawableHdInBattle(obj.getImgName(),ImageUtil.ARGB4444);
		if (d != null) {
			obj.setImage(d);
			return;
		}
		if (obj.getFailCallBack() != null) {

		} else {
			obj.setImage(obj.getStub());
		}
		synchronized (taskList) {
			taskList.addLast(obj);
			taskList.notifyAll();
		}
	}
}
