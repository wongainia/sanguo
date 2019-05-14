/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2012-7-25
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */

package com.vikings.sanguo.ui.window;

import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomPopupWindow;

public abstract class DirectWindow extends CustomPopupWindow {
	protected ViewGroup body;
	protected TextView title;

	protected void init(String str) {
		super.init(str);
		setContent(R.layout.recharge_direct);
		title = (TextView) window.findViewById(R.id.title);
		body = (ViewGroup) window.findViewById(R.id.body);
		body.addView(getContent());
	}

	protected WebView getContent() {
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
				if (null != window) {
					if (newProgress == 100) {
						ViewUtil.setGone(window, R.id.loading);
						ViewUtil.setVisible(window, R.id.scroll);
					} else {
						ViewUtil.setVisible(window, R.id.loading);
						ViewUtil.setGone(window, R.id.scroll);
					}
				}
			}

		});

		String urlStr = getUrl();
		webView.loadUrl(urlStr);
		return webView;
	}

	abstract public String getUrl();

}
