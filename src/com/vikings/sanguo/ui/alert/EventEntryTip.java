package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EventEntry;
import com.vikings.sanguo.model.JumpTargetPanel;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.ui.window.BonusWebWindow;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.IDUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class EventEntryTip extends CustomConfirmDialog implements
		OnClickListener {
	private ScrollView scrollView;
	private ViewGroup webLayout, eventLayout, layout;
	private String urlStr, titleStr;
	private List<EventEntry> events;
	private ViewGroup extView; // 展开的子view
	private ViewGroup selectView;// 选中的view
	private List<View> views;

	public EventEntryTip(String urlStr, String titleStr, List<EventEntry> events) {
		super(titleStr);
		this.urlStr = urlStr + "?v=" + IDUtil.getMyId() + "&sid="
				+ Config.serverId;
		this.titleStr = titleStr;
		this.events = events;
		scrollView = (ScrollView) content.findViewById(R.id.scrollView);
		webLayout = (ViewGroup) content.findViewById(R.id.webLayout);
		eventLayout = (ViewGroup) content.findViewById(R.id.eventLayout);
		layout = (ViewGroup) content.findViewById(R.id.layout);
		// setRightTopCloseBtn();
		setCloseBtn("关闭", closeL);
	}

	public void show() {
		setValue();
		super.show();
	}

	private void setValue() {
		webLayout.addView(getWebContent());
		if (null != events) {
			for (EventEntry it : events) {
				ViewGroup viewGroup = (ViewGroup) controller.inflate(
						R.layout.event_entry_item, eventLayout, false);
				setEventEntryItem(it, viewGroup);
				viewGroup.setTag(it);
				viewGroup.setOnClickListener(this);
				eventLayout.addView(viewGroup);
			}
		} else {
			android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) webLayout
					.getLayoutParams();
			params.height = (int) (440 * Config.SCALE_FROM_HIGH);
			webLayout.setLayoutParams(params);
		}
	}

	private void setEventEntryItem(EventEntry it, View view) {
		new ViewImgCallBack(it.getImg(), view.findViewById(R.id.img));
		ViewUtil.setGone(view, R.id.detailLayout);
	}

	private void setExtView(ViewGroup view, EventEntry event) {
		setCountDown(event, view);
		setContent(event, view);
		setReward(event, view);
		setGo(event, view);
	}

	private void setCountDown(EventEntry entry, View view) {
		if (entry.isNeedCountDown()) {
			refreshInterval = 1000;
			if (entry.getRelatedQuestid() > 0) {
				QuestInfoClient qic = Account.getQuestInfoById(entry
						.getRelatedQuestid());
				boolean isShow = null != qic;
				String time = isShow ? DateUtil.formatTime(qic
						.getCountDownSecond()) : "";
				setCountDown(isShow, view, time);
			} else {
				int countDown = CacheMgr.timeTypeConditionCache
						.getCountDownSecond(entry.getPropTimeId());
				setCountDown(-1 != countDown, view,
						DateUtil.formatTime(countDown));
			}
		} else {
			boolean isShow = !StringUtil.isNull(entry.getCountDownDesc());
			String time = isShow ? entry.getCountDownDesc() : "";
			setCountDown(isShow, view, time);
		}
	}

	private void setCountDown(boolean isShow, View view, String time) {
		if (null == view)
			return;

		if (isShow) {
			ViewUtil.setVisible(view, R.id.timeTitle);
			ViewUtil.setRichText(view, R.id.time, time);
		} else {
			ViewUtil.setGone(view, R.id.timeTitle);
			ViewUtil.setGone(view, R.id.time);
		}
	}

	private void setContent(EventEntry it, View item) {
		if (!StringUtil.isNull(it.getDesc()))
			ViewUtil.setRichText(item, R.id.content, it.getDesc());
		else {
			ViewUtil.setGone(item, R.id.contentTitle);
			ViewUtil.setGone(item, R.id.content);
		}
	}

	private void setReward(EventEntry it, View item) {
		if (!StringUtil.isNull(it.getReward()))
			ViewUtil.setRichText(item, R.id.reward, it.getReward());
		else {
			ViewUtil.setGone(item, R.id.rewardTitle);
			ViewUtil.setGone(item, R.id.reward);
		}
	}

	private void setGo(final EventEntry event, View item) {
		View go = item.findViewById(R.id.go);
		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (event.getToWindow() == 1) {
					new BonusWebWindow().open("", event.getUrl());
				} else {
					JumpTargetPanel.doJump(event.getToWindow());
				}
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_event_entry, contentLayout,
				false);
	}

	private WebView getWebContent() {
		WebView webView = ViewUtil.getScrollWebView(controller.getUIContext());

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
					setTitle(titleStr);
				else
					setTitle("加载中...");
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

	@Override
	public void onClick(View v) {
		Object obj = v.getTag();
		if (null != obj) {
			EventEntry event = (EventEntry) obj;
			ViewGroup detailLayout = (ViewGroup) v
					.findViewById(R.id.detailLayout);
			if ((ViewGroup) v == selectView) {
				detailLayout.removeView(extView);
				ViewUtil.setGone(v, R.id.detailLayout);
				selectView = null;
			} else {
				if (null != selectView) {
					((ViewGroup) selectView.findViewById(R.id.detailLayout))
							.removeView(extView);
					ViewUtil.setGone(selectView.findViewById(R.id.detailLayout));
				}

				detailLayout.setPadding(0, (int) (v.findViewById(R.id.img)
						.getHeight() + 8 * Config.SCALE_FROM_HIGH), 0, 0);
				LayoutParams params = (LayoutParams) detailLayout
						.getLayoutParams();
				params.width = (int) (v.getWidth() - 4 * Config.SCALE_FROM_HIGH);
				params.height = LayoutParams.WRAP_CONTENT;
				if (null == extView)
					extView = (ViewGroup) controller.inflate(
							R.layout.event_entry_detail, detailLayout, false);
				ViewUtil.setVisible(detailLayout);
				(detailLayout).addView(extView);
				setExtView(extView, event);
				selectView = (ViewGroup) v;
				scrollView.post(scrollBottom);

			}
		}
	}

	Runnable scrollBottom = new Runnable() {

		@Override
		public void run() {
			int off = layout.getMeasuredHeight() - scrollView.getHeight();
			if (off > 0) {
				scrollView.scrollTo(0, off);
			}
		}
	};
}
