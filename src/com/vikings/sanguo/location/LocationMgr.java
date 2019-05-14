package com.vikings.sanguo.location;

import android.location.Location;

/**
 * 地图定位 位置更新
 * 
 * @author Brad.Chen
 * 
 */
public class LocationMgr  {

	// private static final String TAG = "LocationMgr";

	private static final int minTime = 5000;

	private static final int minDistance = 100;

	// 最大等待gps定位时间 3min 超过时间则切换为网络定位
	private static final long MAX_WAIT_TIME = 3 * 60000;

	private boolean gpsOK = false;

	private Location lastLocation = null;

	private boolean isListening = false;

	private BackupLocPlan backupPlan;

//	private Criteria cri;

	public boolean isListening() {
		return isListening;
	}

	public LocationMgr() {
		backupPlan = new BackupLocPlan(minTime);
	}

	public void startListen() {
		if (isListening)
			return;
		isListening = true;
//		try {
//			lastLocation = lm
//					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		} catch (Exception e) {
//			Log.e("LocationMgr", "unsupport locationProvider..", e);
//		}
//		if (lastLocation != null) {
//			lastLocation.setProvider(Constants.PROVIDER_LAST);
//			gc.setCurLocation(lastLocation);
//		}
//		try {
//			lm.requestLocationUpdates(lm.getBestProvider(cri, true), minTime,minDistance, this);
//		} catch (Exception e) {
//			Log.e("LocationMgr", "unsupport locationProvider..", e);
//			try {
//				lm.requestLocationUpdates(LocationProviderProxy.MapABCNetwork, minTime,minDistance, this);
//			} catch (Exception e1) {
//				Log.e("LocationMgr", "unsupport locationProvider..", e1);
//			}
//		}
		// 启动后备方案
		backupPlan.start();
	}

	public void stopListen() {
		if (!isListening)
			return;
		isListening = false;
//		lm.removeUpdates(this);
		backupPlan.stop();
	}

//	@Override
//	public void onLocationChanged(Location location) {
//		if (location == null)
//			return;
//		// 仅在上次定位gps 本次定位network 并且gps等待时间不超过MAX_WAIT_TIME 丢弃网络定位
//		if (lastLocation != null
//				&& lastLocation.getProvider().equals(
//						LocationManager.GPS_PROVIDER)
//				&& location.getProvider().equals(
//						LocationManager.NETWORK_PROVIDER)
//				&& location.getTime() - lastLocation.getTime() < MAX_WAIT_TIME)
//			return;
//		lastLocation = location;
//		gc.setCurLocation(lastLocation);
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//
//	}

}
