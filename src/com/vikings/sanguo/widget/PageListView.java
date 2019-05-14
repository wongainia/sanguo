package com.vikings.sanguo.widget;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.window.ListLoading;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public abstract class PageListView extends CustomConfirmDialog implements
		OnScrollListener, OnItemClickListener {
	protected ListView listView;

	protected ObjectAdapter adapter;

	protected ListLoading loading;

	protected ResultPage resultPage;

	protected View emptyShow;

	protected int getTotal() {
		return resultPage.getTotal();
	}

	public PageListView() {
		super(CustomConfirmDialog.DEFAULT);
		setCloseBtn();

		listView = (ListView) tip.findViewById(getListResId());
		listView.setDividerHeight(5);
		this.loading = new ListLoading(tip.findViewById(getLoadingResId()));
		loading.stop();
		this.adapter = getAdapter();
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		emptyShow = tip.findViewById(R.id.empty);
	}

	@Override
	public View getContent() {
		return controller.inflate(R.layout.page_listview);
	}

	protected void addHeader(View header) {
		ViewGroup vg = (ViewGroup) tip.findViewById(R.id.header);
		ViewUtil.setVisible(vg);
		vg.addView(header);
	}

	/**
	 * 声明list的adapter
	 * 
	 * @return
	 */
	abstract protected ObjectAdapter getAdapter();

	/**
	 * 声明如何获取后台动态数据
	 * 
	 * @return
	 */
	// abstract public List getlistData();

	abstract public void getServerData(ResultPage resultPage)
			throws GameException;

	/**
	 * 声明会如何处理list item点击
	 * 
	 * @param o
	 */
	abstract public void handleItem(Object o);

	/**
	 * 声明ListView的 在layout中的id
	 * 
	 * @return
	 */
	protected int getListResId() {
		return R.id.listView;
	}

	/**
	 * 声明loading在 layout中的id
	 * 
	 * @return
	 */
	protected int getLoadingResId() {
		return R.id.loading;
	}

	/**
	 * 声明title在 layout中的id
	 * 
	 * @return
	 */
	protected int getTitleResId() {
		return R.id.listTitle;
	}

	public void setContentTitle(String str) {
		ViewUtil.setRichText(content.findViewById(R.id.content_title),
				R.id.gradientMsg, str);

	}

	public void setContentTitleGone() {
		ViewUtil.setGone(content, R.id.content_title);
	}

	/**
	 * 初始化 显示第一屏
	 */
	protected void firstPage() {
		this.resultPage = new ResultPage();
		this.adapter.clear();
		this.adapter.notifyDataSetChanged();
		this.fetchData();
	}

	protected void fetchData() {
		new FetchInvoker().start();
	}

	public void show() {
		if (this.adapter != null)
			this.adapter.notifyDataSetChanged();
		super.show();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (resultPage == null)
			return;
		// reach the list bottom
		if (firstVisibleItem + visibleItemCount >= totalItemCount) {
			if (loading.isStarted())
				return;
			else if (resultPage.isReachEnd()) {
				return;
			} else {
				fetchData();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// do nothing

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object o = this.adapter.getItem(arg2);
		if (o != null)
			this.handleItem(o);
	}

	@SuppressWarnings("rawtypes")
	protected void updateUI() {
		List ls = resultPage.getResult();
		if (ls != null && ls.size() != 0) {
			ListUtil.deleteRepeat(ls, adapter.getContent());
			adapter.addItems(ls);
		}
		resultPage.addIndex(Math.max(ls.size(), resultPage.getPageSize()));
		resultPage.clearResult();
		adapter.notifyDataSetChanged();
	}

	private class FetchInvoker extends BaseInvoker {

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void beforeFire() {
			loading.start();
		}

		@Override
		protected void fire() throws GameException {
			if (resultPage != null)
				getServerData(resultPage);
		}

		@Override
		protected String loadingMsg() {
			return null;
		}

		@Override
		protected void afterFire() {
			loading.stop();
		}

		@Override
		protected void onOK() {
			if (resultPage == null)
				return;
			updateUI();
			// show();
		}
	}

	protected void dealwithEmptyAdpter() {
		if (adapter.isEmpty()) {
			ViewUtil.setGone(listView);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(emptyShow, getEmptyShowText());
		} else {
			ViewUtil.setVisible(listView);
			ViewUtil.setGone(emptyShow);
		}
	}

	protected String getEmptyShowText() {
		return "";
	}
}
