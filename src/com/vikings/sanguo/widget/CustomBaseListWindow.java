/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-30 下午7:30:29
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.vikings.sanguo.R;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.window.ListLoading;
import com.vikings.sanguo.utils.ListUtil;

abstract public class CustomBaseListWindow extends CustomListViewWindow
		implements OnScrollListener, OnItemClickListener {

	protected ListLoading loading;
	protected ResultPage resultPage;

	protected int getTotal() {
		return resultPage.getTotal();
	}

	protected void init(String title) {
		super.init(title);
		View load = controller.inflate(getLoadingLayoutId());
		content.addView(load);
		this.loading = new ListLoading(load);

		loading.stop();
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void destory() {
		super.destory();
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
	 * 声明title在 layout中的id
	 * 
	 * @return
	 */
	// protected int getTitleResId() {
	// return R.id.listTitle;
	// }

	/**
	 * 初始化 显示第一屏
	 */
	public void firstPage() {
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
		// if (totalItemCount <= 0) {
		// return;
		// }
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

	protected int getLoadingLayoutId() {
		return R.layout.loading;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object o = this.adapter.getItem(arg2);
		if (o != null)
			this.handleItem(o);
	}

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
		protected void onFail(GameException exception) {
			super.onFail(exception);
			if (resultPage != null)
				resultPage.setTotal(0);
		}

		@Override
		protected void onOK() {
			if (resultPage == null)
				return;
			updateUI();
		}
	}
}
