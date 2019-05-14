package com.vikings.sanguo.ui.alert;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class UpdateTips extends CustomConfirmDialog implements OnClickListener {
	private static final int layout = R.layout.alert_update;
	private Button download;
	private Intent it;
	private TextView error_msg;

	public UpdateTips(String msg) {
		super("升级", DEFAULT);
		download = (Button) findViewById(R.id.download);
		download.setOnClickListener(this);
		error_msg = (TextView) findViewById(R.id.error_msg);
		ViewUtil.setRichText(error_msg, msg);
	}

	@Override
	public void onClick(View v) {
		if (v == download) {
			dismiss();
			it = new Intent(Intent.ACTION_VIEW, Uri.parse(CacheMgr.dictCache
					.getDict(Dict.TYPE_SITE_ADDR, 1)));
			Config.getController().getMainActivity().startActivity(it);
		}
	}

	@Override
	protected View getContent() {
		return controller.inflate(layout, contentLayout, false);
	}
}
