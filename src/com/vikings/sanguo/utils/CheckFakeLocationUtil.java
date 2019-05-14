package com.vikings.sanguo.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;

import com.vikings.sanguo.config.Config;

public class CheckFakeLocationUtil {
	public static String forbidProcesses(List<String> forbidProcessesNameList) {
		ActivityManager manager = (ActivityManager) Config.getController()
				.getUIContext().getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfos = manager
				.getRunningAppProcesses();
		if (processInfos != null && !processInfos.isEmpty()) {
			for (RunningAppProcessInfo info : processInfos) {
				for (String name : forbidProcessesNameList) {
					if (name.equals(info.processName)) {
						return info.processName;
					}
				}
			}
		}
		List<RunningServiceInfo> serviceInfos = manager.getRunningServices(100);
		if (null != serviceInfos && !serviceInfos.isEmpty()) {
			for (RunningServiceInfo info : serviceInfos) {
				for (String name : forbidProcessesNameList) {
					if (null != info.service
							&& name.equals(info.service.getPackageName())) {
						return info.process;
					}
				}
			}
		}
		return null;
	}
}
