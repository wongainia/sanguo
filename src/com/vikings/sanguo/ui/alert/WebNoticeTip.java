package com.vikings.sanguo.ui.alert;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.utils.ViewUtil;

public class WebNoticeTip extends Alert {
	private static final int layout = R.layout.alert_web_notice;
	private View content;
	private ViewGroup body;
	private TextView title;
	private String urlStr, titleStr;
	private Button confirmBtn;

	public WebNoticeTip(String urlStr, String titleStr) {
		this.urlStr = urlStr + "?sid=" + Config.serverId;
		this.titleStr = titleStr;
		this.content = controller.inflate(layout);
		title = (TextView) content.findViewById(R.id.title);
		body = (ViewGroup) content.findViewById(R.id.body);
		confirmBtn = (Button) content.findViewById(R.id.confirmBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});

	}

	public void show() {
		setValue();
		super.show(content);
	}

	private void setValue() {
		body.addView(getContent());
	}

	private WebView getContent() {
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
					title.setText(titleStr);
				else
					title.setText("加载中...");
			}
		});
		webView.loadUrl(urlStr);

		webView.setDownloadListener(new MyWebViewDownLoadListener());
		return webView;
	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			controller.getUIContext().startActivity(intent);
		}

	}
}
