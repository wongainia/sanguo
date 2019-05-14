package com.vikings.sanguo.ui.window;

import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ResultPage;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.widget.CustomPopupWindow;

abstract public class BaseGridView extends CustomPopupWindow implements  //PopupWindow 
		OnScrollListener, OnItemClickListener {

	protected ResultPage resultPage;
	protected ObjectAdapter adapter;
	protected GridView gridView;
	private boolean isLoading = true;
	private String loadingMsg;

	protected void init(String title, int resId) {
		super.init(title);
		setContent(resId);
		gridView = (GridView) getPopupView().findViewById(getGridview());
		gridView.setOnScrollListener(this);
		adapter = getAdapter();
		gridView.setAdapter(adapter);
		gridView.setPadding(0, 0, 0, 0);
		gridView.setVerticalSpacing((int) (2 * Config.SCALE_FROM_HIGH));
//		gridView.setHorizontalSpacing((int) (4 * Config.SCALE_FROM_HIGH));

		this.gridView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return MotionEvent.ACTION_MOVE == event.getAction() ? true
						: false;
			}
		});
	}

	public void setLoadingMsg(String msg) {
		this.loadingMsg = msg;
	}

	/**
	 * 初始化 显示第一屏
	 */
	protected void firstPage() {
		this.resultPage = new ResultPage();
		adapter = getAdapter();
		gridView.setAdapter(adapter);
		this.adapter.clear();
		this.adapter.notifyDataSetChanged();
		this.fetchData();
	}

	protected void fetchData() {
		isLoading = false;
		new FetchInvoker().start();
	}

	protected int getGridview() {
		return R.id.gridView;
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

	protected void updateUI() {
		List ls = resultPage.getResult();
		if (ls != null && ls.size() != 0) {
			adapter.addItems(ls);
			adapter.fillEmpty();
		}
		resultPage.addIndex(Math.max(ls.size(), resultPage.getPageSize()));
		resultPage.clearResult();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (resultPage == null)
			return;
		// reach the list bottom
		if (firstVisibleItem + visibleItemCount >= totalItemCount) {
			if (resultPage.isReachEnd()) {
				return;
			} else {
				if (isLoading) {
					isLoading = false;
					fetchData();
				}

			}

		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	protected void destory() {
		this.resultPage = null;
		this.adapter.clear();
		this.adapter.notifyDataSetChanged();
		this.gridView.setAdapter(null);

	}

	private class FetchInvoker extends BaseInvoker {

		@Override
		protected void beforeFire() {
			if (loadingMsg != null)
				Config.getController().showLoading(loadingMsg());
		}

		@Override
		protected String failMsg() {
			return "获取数据失败";
		}

		@Override
		protected void onFail(GameException exception) {
			super.onFail(exception);
			isLoading = true;
		}

		@Override
		protected void fire() throws GameException {
			if (resultPage != null)
				getServerData(resultPage);
		}

		@Override
		protected String loadingMsg() {
			return loadingMsg;
		}

		@Override
		protected void onOK() {
			if (resultPage == null)
				return;
			updateUI();
			isLoading = true;
		}
	}

}
