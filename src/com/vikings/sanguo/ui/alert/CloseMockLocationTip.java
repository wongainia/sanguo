package com.vikings.sanguo.ui.alert;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;

public class CloseMockLocationTip extends Alert implements OnClickListener ,OnCancelListener{
	private static final int layout = R.layout.alert_close_mock_location;
	private View content;

	private Button go2set;

	private static CloseMockLocationTip instance = null;

	private CloseMockLocationTip() {
		content = controller.inflate(layout);
		go2set = (Button) content.findViewById(R.id.go2set);
		go2set.setOnClickListener(this);
		this.dialog.setOnCancelListener(this);
	}

	private void open() {
		show(content);
	}

	public static void show() {
		if (instance != null)
			return;
		instance = new CloseMockLocationTip();
		instance.open();
	}

	@Override
	public void onClick(View v) {
		try {
			Intent launchSettingsIntent = new Intent(
					"com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
			Config.getController().getMainActivity().startActivity(
					launchSettingsIntent);
		} catch (Exception e) {
			controller.alert("请在[系统设置--应用程序--开发]中找到模拟地点的设置并关闭。 ");
		}
		this.dismiss();
		
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		instance = null;
	}
}
