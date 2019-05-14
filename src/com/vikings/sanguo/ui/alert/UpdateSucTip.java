package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.thread.UpdateVersion;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class UpdateSucTip extends CustomConfirmDialog {

	private UpdateVersion.DownloadListener dll;

	public UpdateSucTip(UpdateVersion.DownloadListener dl) {
		super("下载完毕", CustomConfirmDialog.DEFAULT);
		this.dll = dl;
		ViewUtil.setText(findViewById(R.id.msg), "新版本已下载完成,是否立即安装?");
		setButton(SECOND_BTN, "开始安装", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				UpdateVersion.install();
			}
		});
		setButton(CustomConfirmDialog.THIRD_BTN, "取        消",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
						dll.cancle();
					}
				});
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_confirm, contentLayout, false);
	}

}
