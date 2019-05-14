package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout.LayoutParams;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.CheckVersion;
import com.vikings.sanguo.invoker.FinishQuestInvoker;
import com.vikings.sanguo.model.Dict;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.thread.UpdateVersion;
import com.vikings.sanguo.ui.Home;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class UpdateBonusTip extends CustomConfirmDialog {
	private QuestInfoClient qic;
	private View update;
	private Home home;

	// 强制更新时调用
	public UpdateBonusTip(Home home) {
		this.home = home;
	}

	public UpdateBonusTip() {
		super("下载新版本活动");

		update = content.findViewById(R.id.update);
		ViewGroup body = (ViewGroup) findViewById(R.id.body);
		setRightTopCloseBtn();
		body.addView(getWebContent());

		if (Config.isSmallScreen()) {
			View scrollFrame = tip.findViewById(R.id.scrollFrame);
			LayoutParams lp = (LayoutParams) scrollFrame.getLayoutParams();
			lp.height = (int) (270 * Config.SCALE_FROM_HIGH);
		}
	}

	@Override
	public void show() {
		qic = Account.getUpdateQuest();
		if (null == qic)
			return;
		setValue();
		super.show();
	}

	private void setValue() {
		int ver = CacheMgr.dictCache.getDictInt(Dict.TYPE_CLIENT_VERSION, 1);
		if (CheckVersion.isNewer(ver)) {
			ViewUtil.setText(update, "立即领奖");
			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (qic != null)
						new FinishQuestInvoker(qic).start();
				}
			});
		} else {
			ViewUtil.setText(update, "立即更新");
			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if (null != home) {
						new UpdateVersion(home).start();
					} else {
						new UpdateVersion(controller.getCastleWindow()
								.showDownload()).start();
						controller.closeAllPopup();
					}
				}
			});
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_update_bonus, contentLayout,
				false);
	}

	protected WebView getWebContent() {
		WebView webView = ViewUtil.getWebView(controller.getUIContext());

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 当有新连接时，使用当前的 WebView
				view.loadUrl(url);
				return true;
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (null != content) {
					if (newProgress == 100) {
						ViewUtil.setGone(content, R.id.loading);
						ViewUtil.setVisible(content, R.id.scroll);
					} else {
						ViewUtil.setVisible(content, R.id.loading);
						ViewUtil.setGone(content, R.id.scroll);
					}
				}
			}
		});

		String urlStr = CacheMgr.dictCache.getDict(Dict.TYPE_SITE_ADDR, 32);

		webView.loadUrl(urlStr);
		return webView;
	}
}
