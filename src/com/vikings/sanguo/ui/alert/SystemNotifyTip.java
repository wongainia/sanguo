package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.IDUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

/**
 * 公告弹出框
 */
public class SystemNotifyTip extends CustomConfirmDialog {
	private ViewGroup body;
	private String urlStr;
	private String defaultTitle = "系统公告";

	public SystemNotifyTip(String urlStr) {
		super(DEFAULT);
		setCloseBtn();
		setTitle(defaultTitle);
		this.urlStr = urlStr;
		body = (ViewGroup) content.findViewById(R.id.body);
	}

	public SystemNotifyTip(String urlStr, String defaultTitle) {
		this(urlStr);
		this.defaultTitle = defaultTitle;
	}

	public void show() {
		body.addView(getWebView());
		super.show();
	}

	private WebView getWebView() {
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
				if (newProgress == 100)
					setTitle(defaultTitle);
				else
					setTitle("加载中...");
			}
		});
		webView.loadUrl(urlStr + "?v=" + IDUtil.getMyId() + "&sid="
				+ Config.serverId);
		return webView;
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_system_notify, contentLayout,
				false);
	}
}
