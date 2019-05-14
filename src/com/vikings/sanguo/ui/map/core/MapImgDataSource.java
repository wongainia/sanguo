package com.vikings.sanguo.ui.map.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.vikings.sanguo.access.FileAccess;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.network.HttpConnector;
import com.vikings.sanguo.utils.ImageUtil;

public class MapImgDataSource {

	private int zoom;

	private int width;

	private int height;

	// private String url =
	// "http://st.map.soso.com/api?size=#width#*#height#&center=#lon#,#lat#&zoom=#zoom#&format=jpg";

	private String url = "http://restapi.amap.com/gss/simple?sid=3027&ia=1&content=MAP&traffic=off&key=1e82ee91c67e6187b7c2d5275bcca586&cenY=#lat#&cenX=#lon#&width=#width#&height=#height#&maplevel=#zoom#&showLogo=false";

	// private String url =
	// "http://maps.googleapis.com/maps/api/staticmap?center=#lat#,#lon#&zoom=#zoom#&size=#width#x#height#&maptype=terrain&sensor=true&language=zh_cn&format=jpg";

	private int cutHeight = 0;

	private MapView mapView;

	FileAccess fa = Config.getController().getFileAccess();

	private Bitmap stub;

	public MapImgDataSource(int zoom, int width, int height, MapView mapView) {
		this.zoom = zoom;
		this.width = width;
		this.height = height;
		int map_h = this.height + cutHeight;
		this.mapView = mapView;
		url = url.replace("#width#", this.width + "");
		url = url.replace("#height#", map_h + "");
		// url = url.replace("#zoom#", (this.zoom) + "");
		url = url.replace("#zoom#", (17 - this.zoom) + "");
		createStub();
		new Thread(new Worker(), "map").start();
	}

	private Bitmap getBitmapNoScale(String name) {
		Bitmap bmp = null;
		// 先在缓存找
		bmp = Config.getController().getBitmapCache().get(name);
		if (bmp != null)
			return bmp;
		int resId = Config.getController().findResId(name, "drawable");
		// 在打包drawable内
		if (resId > 0) {
			InputStream is = Config.getController().getResources()
					.openRawResource(resId);
			bmp = BitmapFactory.decodeStream(is, null,
					ImageUtil.getOptimizedOptions(name, true));
		}
		// 从sdCard读取
		else {
			bmp = ImageUtil.getGenerateImage(name);
		}
		if (bmp == null)
			return null;
		Config.getController().getBitmapCache().save(name, bmp);
		return bmp;

	}

	private void createStub() {
		stub = getBitmapNoScale("nomap.jpg");
		stub = Bitmap.createScaledBitmap(stub, width, height, true);
	}

	private String generateName(GeoPoint geo) {
		return (geo.getLatitudeE6() > 0 ? geo.getLatitudeE6() : "_"
				+ (-geo.getLatitudeE6()))
				+ ""
				+ (geo.getLongitudeE6() > 0 ? geo.getLongitudeE6() : "_"
						+ (-geo.getLongitudeE6())) + "_"+ zoom +"_map.jpg";
	}

	private String getUrl(GeoPoint geo) {
		return url.replace("#lat#", geo.getLatitudeE6() / 1E6f + "").replace(
				"#lon#", geo.getLongitudeE6() / 1E6f + "");
	}

	public Bitmap getMapImage(GeoPoint geo) {
		String name = generateName(geo);
		Bitmap b = getBitmapNoScale(name);
		if (b != null)
			return b;
		addTask(geo);
		return stub;
	}

	public int getCutHeight() {
		return cutHeight;
	}

	public Bitmap getStub() {
		return stub;
	}

	private LinkedList<GeoPoint> taskList = new LinkedList<GeoPoint>();

	public void addTask(GeoPoint geo) {
		synchronized (taskList) {
			if (taskList.contains(geo))
				return;
			taskList.addFirst(geo);
			taskList.notifyAll();
		}
	}

	private void downloadImage(GeoPoint geo) throws GameException {

		for (int i = 0; i < 3; i++) {
			try {
				byte[] buf = HttpConnector.getInstance().httpGetBytes(
						getUrl(geo));
				if (buf != null && buf.length != 0) {

					// 去水印处理 已知 中心点 高度 水印高度比例 调整高度取图后，按比例截取，保存
					if (cutHeight > 0) {
						fa.saveImage(buf, "big_" + generateName(geo));
						Bitmap b = BitmapFactory.decodeByteArray(buf, 0,
								buf.length, ImageUtil.opts_rgb565_generate);
						Bitmap cut = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
								b.getHeight() - cutHeight, null, true);
						try {
							FileOutputStream out = new FileOutputStream(
									fa.readImage(generateName(geo)));
							cut.compress(CompressFormat.JPEG, 100, out);
							out.close();
						} catch (IOException e) {
						}
						b.recycle();
						cut.recycle();
						b = null;
						cut = null;
					} else {
						fa.saveImage(buf, generateName(geo));
					}
					return;
				}
			} catch (Exception e) {
			}
		}
		throw new GameException("can not download img:" + geo);
	}

	private class Worker implements Runnable {

		@Override
		public void run() {
			while (true) {
				GeoPoint geo = null;
				synchronized (taskList) {
					if (taskList.size() != 0) {
						geo = taskList.getFirst();
					} else {
						try {
							taskList.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (geo == null)
					continue;
				try {
					downloadImage(geo);
				} catch (GameException e) {
					Log.e("MapImgDataSource", e.getMessage());
				}
				synchronized (taskList) {
					for (Iterator<GeoPoint> iterator = taskList.iterator(); iterator
							.hasNext();) {
						GeoPoint ic = (GeoPoint) iterator.next();
						if (ic.equals(geo)) {
							iterator.remove();
							break;
						}
					}
				}
				mapView.postInvalidate();
			}
		}
	}

}
