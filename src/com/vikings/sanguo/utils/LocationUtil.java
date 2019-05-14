package com.vikings.sanguo.utils;

import android.location.Location;

import com.vikings.sanguo.ui.map.core.GeoPoint;

public class LocationUtil {

	private static final int max_bonus = 8000;
	private static final int min_bonus = 3000;
	private static final int per_bonus = 50;

	public static GeoPoint getGeoByLocation(Location location) {
		GeoPoint gp = null;
		try {
			/* 当Location存在 */
			if (location != null) {
				double geoLatitude = location.getLatitude() * 1E6;
				double geoLongitude = location.getLongitude() * 1E6;
				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gp;
	}

	public static boolean equals(Location loc1, Location loc2) {
		if (loc1 == null && loc2 == null)
			return true;
		if (loc1 == null || loc2 == null)
			return false;
		int lat1 = (int) (loc1.getLatitude() * 1E6);
		int lon1 = (int) (loc1.getLongitude() * 1E6);
		int lat2 = (int) (loc2.getLatitude() * 1E6);
		int lon2 = (int) (loc2.getLongitude() * 1E6);
		if (lat1 == lat2 && lon1 == lon2)
			return true;
		return false;
	}

	public static int distance(Location l1, GeoPoint p2) {
		float[] results = new float[1];
		Location.distanceBetween(l1.getLatitude(), l1.getLongitude(),
				(double) p2.getLatitudeE6() / 1E6D, (double) p2
						.getLongitudeE6() / 1E6D, results);
		return (int) results[0];
	}

	public static int distance(Location l1, int destLat, int destLon) {
		float[] results = new float[1];
		Location.distanceBetween(l1.getLatitude(), l1.getLongitude(),
				(double) destLat / 1E6D, (double) destLon / 1E6D, results);
		return (int) results[0];
	}

	public static int distance(int lat, int lon, int destLat, int destLon) {
		float[] results = new float[1];
		Location.distanceBetween((double) lat / 1E6D, (double) lon / 1E6D,
				(double) destLat / 1E6D, (double) destLon / 1E6D, results);
		return (int) results[0];
	}

	public static int distance(Location l1, Location l2) {
		return (int) l1.distanceTo(l2);
	}

	public static String descDistance(long tile1, long tile2) {
		int m = distance(TileUtil.getTileCenterLat(tile1), TileUtil
				.getTileCenterLon(tile1), TileUtil.getTileCenterLat(tile2),
				TileUtil.getTileCenterLon(tile2));
		if (m < 100)
			return "100m";
		if (m < 200)
			return "200m";
		if (m < 500)
			return "500m";
		if (m < 1000)
			return "1000m";
		if (m < 5000)
			return "5000m";
		return "5000m";
	}

	public static String descLongDistance(long tile1, long tile2) {
		if (tile1 == 0 || tile2 == 0) {
			return ">1000km";
		}
		int m = distance(TileUtil.getTileCenterLat(tile1), TileUtil
				.getTileCenterLon(tile1), TileUtil.getTileCenterLat(tile2),
				TileUtil.getTileCenterLon(tile2));
		if (m == 0) {
			return "10m";
		}
		if (m < 1000)
			return m + "m";
		if (m < 1000000)
			return m
					/ 1000
					+ ((m / 100) % 10 == 0 ? "" : "."
							+ String.valueOf((m / 100) % 10)) + "km";
		return ">1000km";
	}


	public static long location2TileId(Location loc) {
		GeoPoint gp = getGeoByLocation(loc);
		return TileUtil.toTileId(gp.getLongitudeE6(), gp.getLatitudeE6());
	}
}
