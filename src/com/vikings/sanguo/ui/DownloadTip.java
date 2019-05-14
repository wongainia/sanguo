package com.vikings.sanguo.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.alert.UpdateSucTip;
import com.vikings.sanguo.utils.ViewUtil;

import com.vikings.sanguo.thread.UpdateVersion;

public class DownloadTip implements UpdateVersion.DownloadListener,
		OnClickListener {

	private View tip;

	private int p;

	public DownloadTip(ViewGroup vg) {
		View v = vg.findViewById(R.id.downloadFrame);
		ViewUtil.setVisible(v);
		tip = v.findViewById(R.id.percent);
		v.setOnClickListener(this);
	}

	public void setP(int p) {
		if (p == 100) {
			ViewUtil.setText(tip, "请安装");
		} else
			ViewUtil.setText(tip, p + "%");
		this.p = p;
	}

	@Override
	public void setDownloadPercent(int p) {
		setP(p);
	}

	@Override
	public void onClick(View v) {
		if (p != 100)
			return;
		new UpdateSucTip(this).show();
	}

	@Override
	public void cancle() {
	}

}
