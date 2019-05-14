/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-8-6 下午2:48:37
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import java.util.List;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.AbsListView.OnScrollListener;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.window.ListLoading;
import com.vikings.sanguo.utils.ViewUtil;

public abstract class CustomListView implements OnScrollListener{
	protected ExpandableListView listView;
	
	protected BaseExpandableListAdapter adapter;

	protected ListLoading loading;

	protected ResultPage resultPage;
	
	protected View emptyShow;
	
	protected int getTotal() {
		return resultPage.getTotal();
	}

	public CustomListView(ExpandableListView listView, View loadView, View emptyShow) {
		this.listView = listView;
		this.listView.setDividerHeight(0);
		this.loading = new ListLoading(loadView);
		loading.stop();
		this.emptyShow = emptyShow;
	}

	protected void setAdapterToListView() {
		this.adapter = getAdapter();
		this.listView.setAdapter(adapter);
		this.listView.setOnScrollListener(this);
	}
	
	/**
	 * 声明list的adapter
	 * 
	 * @return
	 */
	abstract protected BaseExpandableListAdapter getAdapter();

	/**
	 * 声明如何获取后台动态数据
	 * 
	 * @return
	 */

	abstract public void getServerData(ResultPage resultPage)
			throws GameException;

	/**
	 * 声明会如何处理list item点击
	 * 
	 * @param o
	 */
	abstract public void handleItem(Object o);

	/**
	 * 初始化 显示第一屏
	 */
	public void firstPage() {
		this.resultPage = new ResultPage();
		this.adapter.notifyDataSetChanged();
		this.fetchData();
	}

	protected void fetchData() {
		new FetchInvoker().start();
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (resultPage == null)
			return;
		//此处配套使用的是ExpandableListView，resultPage.total就是group的个数
			if (loading.isStarted())
				return;
			else if (resultPage.isReachEnd()) {
				return;
			} else
				fetchData();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}


	protected void updateUI() {
		List ls = resultPage.getResult();
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
		}
	}
	
	protected void dealwithEmptyAdpter() {
		if (adapter.isEmpty() && null != emptyShow) {
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
