package com.vikings.sanguo.service;

import android.content.Context;
import android.content.Intent;

public class Notification {

	public static void startService(Context context) {
		context.startService(new Intent(context, SystemNotification.class));
	}

	// public static void startService(Context context, int clientCode) {
	// if (Constants.VALUEOPEN.equals(Setting.notice)
	// || Constants.TIMING.equals(Setting.notice)) {
	// Intent intent = new Intent(context, SystemNotification.class);
	// Bundle bundle = new Bundle();
	// bundle.putInt("ClientCode", clientCode);
	// intent.putExtra("tag", bundle);
	// context.startService(intent);
	// }
	// }

	public static void stopService(Context context) {
		context.stopService(new Intent(context, SystemNotification.class));
	}

}
