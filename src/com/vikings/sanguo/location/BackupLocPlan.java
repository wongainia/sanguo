package com.vikings.sanguo.location;

import android.location.Location;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.vikings.sanguo.config.Config;

public class BackupLocPlan {

	private int firstWaitTime;

	private int updateWaitTime;

	private Handler handler;

	private LocationClient baiduLoc;

	private Location customLocation = null;

	private Runnable setLocation;

	private BaiduLocListener baiduLocListener = new BaiduLocListener();

	public BackupLocPlan(int time) {
		// 初次定位等待时间
		this.firstWaitTime = time;
		// 定时定位等待时间
		this.updateWaitTime = time * 2;

		this.handler = new Handler();

		this.baiduLoc = new LocationClient(Config.getController()
				.getUIContext());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setScanSpan(updateWaitTime);
		option.disableCache(true);
		option.setPoiNumber(0);
		option.setPoiDistance(0);
		option.setPoiExtraInfo(false);
		baiduLoc.setLocOption(option);
		this.baiduLoc.registerLocationListener(baiduLocListener);
		setLocation = new Set();
	}

	public void start() {
		baiduLoc.start();
//		new Thread(new WorstCase()).start();
	}

	public void stop() {
		baiduLoc.stop();
	}

	private boolean validate(BDLocation loc) {
		if (loc != null && loc.getLatitude() > 1d && loc.getLongitude() > 1d)
			return true;
		else
			return false;
	}

	private class BaiduLocListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation loc) {
			Location pre = Config.getController().getCurLocation();
			// 当前没有定位
			// 定位信息是手动
			// 定位信息是last
			// 定位信息是server
			// 都再次进行手工定位。 仅当定位信息来自android系统时，手工定位退出控制
			if (pre == null
					|| Constants.PROVIDER_CUSTOM.equals(pre.getProvider())
					|| Constants.PROVIDER_LAST.equals(pre.getProvider())
					|| Constants.PROVIDER_SERVER.equals(pre.getProvider())) {
				if (validate(loc)) {
					customLocation = new Location(Constants.PROVIDER_CUSTOM);
					customLocation.setLatitude(loc.getLatitude());
					customLocation.setLongitude(loc.getLongitude());
					customLocation.setAltitude(loc.getAltitude());
					handler.post(setLocation);
				}
			}
			// 系统定位已经生效,退出后备方案
			else {
				stop();
			}
		}

		@Override
		public void onReceivePoi(BDLocation loc) {

		}

	}
//
//	private class WorstCase extends SingletonRunnable {
//
//		@Override
//		public String getThreadName() {
//			return "WorstCase";
//		}
//
//		@Override
//		public void doRun() {
//			try {
//				Thread.sleep(firstWaitTime);
//			} catch (Exception e) {
//			}
//			while (true) {
//				try {
//					Thread.sleep(firstWaitTime);
//				} catch (Exception e) {
//				}
//				Location pre = Config.getController().getCurLocation();
//				if (pre == null) {
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							if (Account.user.isValidUser()
//									&& Config.getController()
//											.getCurLocation() == null)
//								ChooseAddrsTip.getInstance().show();
//						}
//					});
//				} else
//					break;
//			}
//
//		}
//	}

	private class Set implements Runnable {

		@Override
		public void run() {
			Config.getController().setCurLocation(customLocation);
		}

	}
}
