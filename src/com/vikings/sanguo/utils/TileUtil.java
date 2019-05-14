package com.vikings.sanguo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.Location;

import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.Point;
import com.vikings.sanguo.ui.map.core.GeoPoint;

/**
 * tile计算公式
 * 
 * @author Brad.Chen
 * 
 */
public class TileUtil {
	//
	// private static final int WORLD_ORIGIN_LEFT = (-180 * 360);
	//
	// private static final int WORLD_ORIGIN_BOTTOM = (-90 * 360);

	public static final int TILE_SPACE = (int) (4); // 单位：秒

	private static final int TILE_SPACE_HALF = (TILE_SPACE >> 1);

	// // tile换算为经纬度 以度为单位
	// private static final double TILE_SIZE = (double) TILE_SPACE / (double)
	// 3600L;


	/**
	 * 转换经纬度为 索引
	 * 
	 * @param x
	 * @return
	 */
	static public int latitude2Index(int x) {
		return x / TILE_SPACE;
	}

	static public int index2Latitude(int x) {
		return x * TILE_SPACE;
	}

	// 换算为服务器端经纬度
	static public int transServer(int n) {
		return (int) (((float) n / 1000000.0f) * 3600.0f);
	}

	// 客户端单位换算度×1000000
	static private int transClient(int n) {
		return (int) (((float) n / 3600.0f) * 1000000);
	}

	// tileid转换为 x索引
	static public int tileId2x(long tileid) {
		return (int) (tileid >> 32);
	}

	// tileid转换为 y索引
	static public int tileId2y(long tileid) {
		return (int) (tileid & 0x00000000ffffffff);
	}

	static public String uniqueMarking(long fiefid) {
		return tileId2x(fiefid) + "," + tileId2y(fiefid);
	}

	/**
	 * 根据tile 得到中心经度
	 * 
	 * @param tileId
	 * @return
	 */
	static public int getTileCenterLon(long tileId) {
		if (tileId == 0)
			return 0;
		return transClient(tileId2x(tileId) * TILE_SPACE + TILE_SPACE_HALF);
	}

	/**
	 * 根据tile 得到中心维度
	 * 
	 * @param tileId
	 * @return
	 */
	static public int getTileCenterLat(long tileId) {
		if (tileId == 0)
			return 0;
		return transClient(tileId2y(tileId) * TILE_SPACE + TILE_SPACE_HALF);
	}

	static public int getTileLon(long tileId) {
		if (tileId == 0)
			return 0;
		return transClient(tileId2x(tileId) * TILE_SPACE);
	}

	static public int getTileLat(long tileId) {
		if (tileId == 0)
			return 0;
		return transClient(tileId2y(tileId) * TILE_SPACE);
	}

	/**
	 * 索引转换为tileid
	 */
	static public long index2TileId(int ix, int iy) {
		long tileId = ix;
		tileId <<= 32;
		tileId |= iy;
		return tileId;
	}

	/**
	 * fief索引转换为tileid
	 */
	static public long fiefIdx2TileId(int fx, int fy) {
		return index2TileId(fx * Config.FIEF_SIZE + Config.FIEF_SIZE / 2, fy * Config.FIEF_SIZE
				+ Config.FIEF_SIZE / 2);
	}

	/**
	 * tileId转fiefId
	 * 
	 * @param tileId
	 * @return
	 */
	static public long tileId2FiefId(long tileId) {
		return index2TileId(tileId2x(tileId) / Config.FIEF_SIZE, tileId2y(tileId)
				/Config.FIEF_SIZE);
	}

	/**
	 * fiefId 转 tileId
	 * 
	 * @param fiefId
	 * @return
	 */
	static public long fiefId2TileId(long fiefId) {
		return fiefIdx2TileId(tileId2x(fiefId), tileId2y(fiefId));
	}

	/**
	 * 
	 * @param tileid
	 *            庄园所在地
	 * @param r
	 *            半径（fief个数）
	 * @return
	 */
	static public List<Long> roundFiefids(long tileid, int r) {
		List<Long> fiefids = new ArrayList<Long>();
		if (r == 0)
			return fiefids;

		List<Point> list = new ArrayList<Point>();
		for (int i = -r; i <= r; i++) {
			for (int j = -r; j <= r; j++) {
				if (i != 0 || j != 0) {
					Point point = new Point();
					point.setX(i);
					point.setY(j);
					list.add(point);
				}

			}
		}
		Collections.sort(list, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				int tmp1 = Math.max(Math.abs(p1.getX()), Math.abs(p1.getY()));
				int tmp2 = Math.max(Math.abs(p2.getX()), Math.abs(p2.getY()));
				if (tmp1 == tmp2) {
					if (p1.getX() == p2.getX()) {
						return p1.getY() - p2.getY();
					} else {
						return p1.getX() - p2.getX();
					}
				} else {
					return tmp1 - tmp2;
				}
			}
		});

		int x = tileId2x(tileid) / Config.FIEF_SIZE;
		int y = tileId2y(tileid) / Config.FIEF_SIZE;

		for (Point point : list) {
			long fiefid = index2TileId(x + point.getX(), y + point.getY());
			fiefids.add(fiefid);
		}
		return fiefids;
	}

	/**
	 * 计算从dstFiefId领地到tarFiefId领地的tile距离
	 * 
	 * @param dstFiefId
	 * @param tarFiefId
	 * @return
	 */
	static public double fiefDistance(long dstFiefId, long tarFiefId) {
		double dstX = tileId2x(dstFiefId);
		double dstY = tileId2y(dstFiefId);
		double tarX = tileId2x(tarFiefId);
		double tarY = tileId2y(tarFiefId);
		double distance = Math.sqrt((tarX - dstX) * (tarX - dstX)
				+ (tarY - dstY) * (tarY - dstY));
		if (CacheMgr.holyPropCache.isHoly(tarFiefId)) {
			int maxDistance = CacheMgr.dictCache.getDictInt(
					Dict.TYPE_BATTLE_COST, 11);
			distance = Math.min(distance, maxDistance);
		}
		return distance;
	}

	/**
	 * 取上下左右的fiefid，返回的顺序也是上下左右
	 * 
	 * @param fiefId
	 * @return
	 */
	static public long[] getNeighbourFiefId(long fiefId) {
		long[] fiefids = new long[4];
		int x = tileId2x(fiefId);
		int y = tileId2y(fiefId);
		fiefids[0] = index2TileId(x, y + 1);
		fiefids[1] = index2TileId(x, y - 1);
		fiefids[2] = index2TileId(x - 1, y);
		fiefids[3] = index2TileId(x + 1, y);
		return fiefids;
	}

	/**
	 * 得到一片区域内的所有的tile id
	 * 
	 * @param leftBottomLon
	 * @param leftBottomLat
	 * @param rightTopLon
	 * @param rightTopLat
	 * @return
	 */
	static public ArrayList<Long> regionFiefs(int leftBottomLon,
			int leftBottomLat, int rightTopLon, int rightTopLat) {
		int x1 = latitude2Index(transServer(leftBottomLon)) / Config.FIEF_SIZE;
		int x2 = latitude2Index(transServer(rightTopLon)) / Config.FIEF_SIZE;
		int y1 = latitude2Index(transServer(leftBottomLat)) / Config.FIEF_SIZE;
		int y2 = latitude2Index(transServer(rightTopLat)) / Config.FIEF_SIZE;
		ArrayList<Long> rs = new ArrayList<Long>((x2 - x1 + 1) * (y2 - y1 + 1));
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				rs.add(index2TileId(i, j));
			}
		}
		return rs;
	}

	static public long toTileId(int lon, int lat) {
		return index2TileId(latitude2Index(transServer(lon)),
				latitude2Index(transServer(lat)));
	}

	static public long toTileId(Location loc) {
		if (loc == null)
			return 0;
		return TileUtil.toTileId((int) (loc.getLongitude() * 1E6),
				(int) (loc.getLatitude() * 1E6));
	}

	static public GeoPoint toGeoPoint(long tileId) {
		return new GeoPoint(getTileCenterLat(tileId), getTileCenterLon(tileId));
	}

	static public GeoPoint getFiefCenterGeo(long fiefId) {
		long p = index2TileId(tileId2x(fiefId) * Config.FIEF_SIZE + Config.FIEF_SIZE / 2,
				tileId2y(fiefId) * Config.FIEF_SIZE + Config.FIEF_SIZE / 2);
		return new GeoPoint(getTileLat(p), getTileLon(p));
	}

	// GeoPoint转fiefId
	static public long geoPoint2FiefId(GeoPoint p) {
		return tileId2FiefId(toTileId(p.getLongitudeE6(), p.getLatitudeE6()));
	}
}