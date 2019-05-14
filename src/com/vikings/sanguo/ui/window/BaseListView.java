package com.vikings.sanguo.ui.window;

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
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

abstract public class BaseListView extends PopupWindow implements
		OnScrollListener, OnItemClickListener {

	protected ListView listView;

	protected ObjectAdapter adapter;

	protected ListLoading loading;

	protected ResultPage resultPage;

	protected View footerView;

	protected View headerView;

	protected int getTotal() {
		return resultPage.getTotal();
	}

	@Override
	protected void init() {
		listView = (ListView) getPopupView().findViewById(getListResId());
		listView.setDividerHeight(0);
		this.loading = new ListLoading(getPopupView().findViewById(
				getLoadingResId()));
		loading.stop();
		this.adapter = getAdapter();

		initAdapterHeader();

		footerView = controller.inflate(R.layout.footer_view);
		ViewUtil.setRichText(footerView.findViewById(R.id.text),
				getFooterViewText());
		if (listView.getFooterViewsCount() == 0) {
			listView.addFooterView(footerView, null, false);
		}
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	protected void initAdapterHeader() {

	}

	@Override
	protected void destory() {
		this.resultPage = null;
		this.adapter.clear();
		this.adapter.notifyDataSetChanged();
		this.listView.setAdapter(null);
	}

	protected void setHeaderView(View view) {
		if (null == headerView)
			return;
		ViewGroup header = (ViewGroup) headerView;
		if (header.getChildCount() > 0) {
			header.removeAllViews();
		}
		if (view != null)
			header.addView(view);
	}

	/**
	 * 当子类需要在列表的末尾添加文字时重写该方法
	 * 
	 * @return
	 */
	protected String getFooterViewText() {
		return "";
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

	@Override
	public void showUI() {
		if (this.adapter != null)
			this.adapter.notifyDataSetChanged();
		super.showUI();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (resultPage == null)
			return;
//		if (totalItemCount <= 0) {
//			return;
//		}
		// reach the list bottom
		if (firstVisibleItem + visibleItemCount >= totalItemCount) {
			if (loading.isStarted())
				return;
			else if (resultPage.isReachEnd()) {
				return;
			} else
				fetchData();
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

	protected void setTitle(int total) {
		ViewUtil.setText(getPopupView().findViewById(getTitleResId()), total);
	}

	protected void updateUI() {
		List ls = resultPage.getResult();
		setTitle(resultPage.getTotal());
		// int size = 0;
		if (ls != null && ls.size() != 0) {
			// size = ls.size();
			ListUtil.deleteRepeat(ls, adapter.getContent());
			adapter.addItems(ls);
		}
		resultPage.addIndex(Math.max(ls.size(), resultPage.getPageSize()));
		resultPage.clearResult();
		adapter.notifyDataSetChanged();
		// // 设置total = cureindex 之后不再获取数据
		// if (size == 0) {
		// resultPage.setCurIndex(resultPage.getTotal());
		// }
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
		}
	}

	public View getHeader() {
		return headerView;
	}
}
