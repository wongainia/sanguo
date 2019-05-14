package com.vikings.sanguo.ui.map.core;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;

public class MapView extends View {

	private GeoPoint center = new GeoPoint(22545468, 113941160);

	private boolean showMap = false;

	private InternalProjection internalProjection;

	private MapImgDataSource mapImgDataSource;

	private GeoPoint base = new GeoPoint(12211180, 148886719);// 要插入标记的经纬度位置

	private Bitmap fakeMap = null, drawMap = null; // 标记使用的图标

	private int tileWidth;
	private int tileHeight;
	private Rect bitmapRect = new Rect();
	private Rect screenRect = new Rect();

	public static final int MIN_MOVE_PX = (int) (10 * Config.SCALE_FROM_HIGH);

	protected float touchX;

	protected float touchY;

	protected float moveX;

	protected float moveY;

	protected float diff;

	protected float diffX;

	protected float diffY;

	private ArrayList<Overlay> overlays = new ArrayList<Overlay>();

	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MapView(Context context, String apiKey) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		internalProjection = new InternalProjection();
		this.fakeMap = Config.getController().getBitmap(R.drawable.map);
		mapImgDataSource = new MapImgDataSource(Config.getZoomLevel() - 1, 240,
				240, this);
	}

	public void setMapCenter(GeoPoint center) {
		this.center = center;
		invalidate();
	}

	public void animateTo(GeoPoint center) {
		setMapCenter(center);
	}

	public GeoPoint getMapCenter() {
		return center;
	}

	public Projection getProjection() {
		return internalProjection;
	}

	public void setShowMap(boolean showMap) {
		this.showMap = showMap;
	}

	public boolean isShowMap() {
		return showMap;
	}

	public void addOverlay(Overlay o) {
		this.overlays.add(o);
	}

	private boolean isMove(float src, float dest) {
		diff = src - dest;
		return diff > MIN_MOVE_PX || diff < -MIN_MOVE_PX;
	}

	public void addOverlay() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			// 按下时候 可能是点击 可能是移动 所以先行记录2个起始坐标
			// 记录点击坐标
			// 记录移动开始坐标
			touchX = event.getX();
			touchY = event.getY();
			moveX = touchX;
			moveY = touchY;
			break;
		case MotionEvent.ACTION_UP:
			// up的时候，可能是点击确认，可能是移动结束
			// 如果up的偏移够小，认为是一次点击事件
			if (!isMove(touchX, event.getX()) && !isMove(touchY, event.getY())) {
				onClick(touchX, touchY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isMove(moveX, event.getX()) || isMove(moveY, event.getY())) {
				diffX = event.getX() - moveX;
				diffY = event.getY() - moveY;
				onMove(diffX, diffY);
				moveX = event.getX();
				moveY = event.getY();
			}
			break;
		default:
			break;
		}
		return true;
	}

	private void onClick(float x, float y) {
		GeoPoint geo = internalProjection.fromPixels((int) x, (int) y);
		for (Overlay o : overlays) {
			if (o.onTap(geo, this))
				return;
		}
	}

	private void onMove(float x, float y) {
		setMapCenter(internalProjection.fromPixels(
				(int) (Config.screenWidth / 2 - x),
				(int) (Config.screenHeight / 2 - y)));
	}

	@Override
	protected void onDraw(Canvas canvas) {

		//

		if (!showMap) {
			drawMap = fakeMap;
			tileWidth = drawMap.getWidth();
			tileHeight = drawMap.getHeight();
		} else {
			drawMap = mapImgDataSource.getStub();
			tileWidth = drawMap.getWidth() * 2;
			tileHeight = drawMap.getHeight() * 2;
		}

		Point pos = internalProjection.toPixels(base, null);
		int x = pos.x % tileWidth == 0 ? 0 : -(tileWidth - pos.x % tileWidth);
		while (x < Config.screenWidth) {
			int y = pos.y % tileHeight == 0 ? 0 : -(tileHeight - pos.y
					% tileHeight);
			while (y < Config.screenHeight) {
				bitmapRect.left = 0;
				bitmapRect.right = drawMap.getWidth();
				bitmapRect.top = 0;
				bitmapRect.bottom = drawMap.getHeight();
				screenRect.left = x;
				screenRect.right = x + tileWidth;
				screenRect.top = y;
				screenRect.bottom = y + tileHeight;

				if (showMap) {
					drawMap = mapImgDataSource.getMapImage(internalProjection
							.fromPixels(x + tileWidth / 2, y + tileHeight / 2
									+ mapImgDataSource.getCutHeight() / 2));
				}

				if (x < 0) {
					screenRect.left = 0;
					bitmapRect.left = (0 - x) * drawMap.getWidth() / tileWidth;
				}
				if (x + tileWidth > Config.screenWidth) {
					screenRect.right = Config.screenWidth;
					bitmapRect.right = drawMap.getWidth()
							- (x + tileWidth - Config.screenWidth)
							* drawMap.getWidth() / tileWidth;
				}
				if (y < 0) {
					screenRect.top = 0;
					bitmapRect.top = (0 - y) * drawMap.getHeight() / tileHeight;
				}
				if (y + tileHeight > Config.screenHeight) {
					screenRect.bottom = Config.screenHeight;
					bitmapRect.bottom = drawMap.getHeight()
							- (y + tileHeight - Config.screenHeight)
							* drawMap.getHeight() / tileHeight;
				}
				canvas.drawBitmap(drawMap, bitmapRect, screenRect, null);
				// canvas.drawRect(screenRect, paint);
				y = y + tileHeight;
			}
			x = x + tileWidth;
		}
		for (Overlay i : overlays) {
			i.draw(canvas, this);
		}
	}

	private class InternalProjection implements Projection {

		private static final double CIRCUMFERENCE_IN_METERS = 40075160.0;
		private int tiles = 1 << Config.getZoomLevel();
		private final int tileSize = 256;
		private double circumference = tileSize * tiles;
		private double radius = circumference / (2.0 * Math.PI);

		Point geoPointToPoint(GeoPoint gp) {
			Point ret = new Point();
			double longitude = ((double) gp.getLongitudeE6()) / 1000000.0
					* Math.PI / 180.0;
			ret.x = (int) (radius * longitude + (circumference / 2.0));
			double latitude = ((double) gp.getLatitudeE6()) / 1000000.0
					* Math.PI / 180.0;
			ret.y = (int) ((circumference / 2.0) - (radius / 2.0 * Math
					.log((1.0 + Math.sin(latitude))
							/ (1.0 - Math.sin(latitude)))));
			return ret;
		}

		GeoPoint pointToGeoPoint(Point p) {
			double longRadians = (p.x - (circumference / 2.0)) / radius;
			double longDegrees = longRadians * 180.0 / Math.PI;
			int long1E6 = (int) (longDegrees * 1000000.0);

			double latitude = (Math.PI / 2.0)
					- (2.0 * Math.atan(Math
							.exp(/* -1.0 * */(p.y - (circumference / 2.0))
									/ radius)));
			int lat1E6 = (int) (latitude * 180.0 / Math.PI * 1000000.0);

			return new GeoPoint(lat1E6, long1E6);
		}

		public GeoPoint fromPixels(int x, int y) {
			Point c = geoPointToPoint(center);
			c.x = c.x - (getWidth() / 2) + x;
			c.y = c.y - (getHeight() / 2) + y;
			return pointToGeoPoint(c);
		}

		public float metersToEquatorPixels(float meters) {
			return (float) (meters * circumference / CIRCUMFERENCE_IN_METERS);
		}

		public Point toPixels(GeoPoint in, Point out) {
			if (out == null)
				out = new Point();
			Point p = geoPointToPoint(in);
			Point c = geoPointToPoint(center);
			out.set(p.x - c.x + (getWidth() / 2), p.y - c.y + (getHeight() / 2));
			return out;
		}

		// /**
		// * Get the coordinates of the upper-left corner of the tile containing
		// a
		// * Geopoint
		// */
		// GeoPoint getTileGeoPoint(GeoPoint gp) {
		// return tileCoordsToGeoPoint(getTileCoords(gp));
		// }
		//
		// TileCoords getTileCoords(final double lat, final double lon) {
		// int xtile = (int)Math.floor((lon + 180) / 360 * (1 << zoomLevel));
		// int ytile = (int)Math.floor((1 - Math.log(Math.tan(lat * Math.PI /
		// 180) + 1
		// / Math.cos(lat * Math.PI / 180))
		// / Math.PI)
		// / 2 * (1 << zoomLevel));
		// return new TileCoords(xtile, ytile, zoomLevel);
		// }

	}

	private class Anim implements Runnable {

		float lat_s, lon_s;
		float lat_d, lon_d;
		float lati, loni;

		int count = 10;
		int time = 1000;

		int times = 1;

		public Anim(GeoPoint dst) {
			lat_s = center.getLatitudeE6();
			lat_d = dst.getLatitudeE6();
			lon_s = center.getLongitudeE6();
			lon_d = dst.getLongitudeE6();

			lati = (lat_d - lat_s) / count;
			loni = (lon_d - lon_s) / count;
		}

		@Override
		public void run() {
			if (lat_s == lat_d && lon_s == lon_d)
				return;

			times++;

			if (lat_s != lat_d) {
				lat_s = lat_s + lati;
			}
			if (lon_s != lon_d) {
				lon_s = lon_s + loni;
			}
			center.setLatitudeE6((int) lat_s);
			center.setLongitudeE6((int) lon_s);
			invalidate();
			if (times >= count)
				return;
			postDelayed(this, time / count);
		}

	}

}