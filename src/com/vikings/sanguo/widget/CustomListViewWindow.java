package com.vikings.sanguo.widget;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.ListView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;

abstract public class CustomListViewWindow extends CustomPopupWindow {
	protected ListView listView;
	protected ObjectAdapter adapter;
	protected View footerView = null;
	protected View headerView = null;
	protected View emptyShow;
	protected boolean hasListAnim = true;
	// 权宜之计，定义listview中item的高度
	final static private int ITEM_HEIGHT = 123;

	protected void init(String title, boolean fullScreen, boolean hasListAnim) {
		this.hasListAnim = hasListAnim;
		this.fullScreen = fullScreen;
		init(title);
	}

	@Override
	protected void init(String title) {
		super.init(title, fullScreen);
		int layout = hasListAnim ? R.layout.common_frame_list
				: R.layout.common_frame_list_no_anim;
		setContent(layout);
		listView = (ListView) window.findViewById(R.id.listView);
		this.adapter = getAdapter();

		headerView = initHeaderView();
		footerView = initFooterView();

		if (listView.getHeaderViewsCount() == 0 && null != headerView)
			listView.addHeaderView(headerView, null, false);

		if (listView.getFooterViewsCount() == 0 && null != footerView)
			listView.addFooterView(footerView, null, false);

		if (null != adapter) {
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		if (getEmptyShowLayoutId() > 0) {
			emptyShow = controller.inflate(getEmptyShowLayoutId(), content,
					false);
			content.addView(emptyShow);
		}

	}

	protected View initHeaderView() {
		return null;
	}

	protected View initFooterView() {
		return null;
	}

	public View getHeader() {
		return headerView;
	}

	public View getFooterView() {
		return footerView;
	}

	protected int getEmptyShowLayoutId() {
		return R.layout.empty_show;
	}

	protected String getEmptyShowText() {
		return "";
	}

	abstract protected ObjectAdapter getAdapter();

	protected void dealwithEmptyAdpter() {
		if (emptyShow == null)
			return;
		if (adapter.isEmpty()) {
			ViewUtil.setGone(listView);
			ViewUtil.setVisible(emptyShow);
			ViewUtil.setRichText(emptyShow, R.id.emptyDesc, getEmptyShowText());
		} else {
			ViewUtil.setVisible(listView);
			ViewUtil.setGone(emptyShow);
		}
	}

	// 计算ListView中某个item需要移动的距离
	public void smoothMoveItem(View v) {
		int offset = 0;

		int[] list = { -1, -1 };
		int[] item = { -1, -1 };
		listView.getLocationOnScreen(list);

		int idx = listView.indexOfChild(v);
		int upIdx = idx - 1;
		if (idx == 0) {
			v.getLocationOnScreen(item);
			offset = item[1] - list[1];
		} else {
			while (upIdx >= 0) {
				View lastItem = listView.getChildAt(upIdx);
				// int[] item = { -1, -1 };
				lastItem.getLocationOnScreen(item);

				if (item[1] <= list[1]) {
					offset = (idx - upIdx)
							* (int) (ITEM_HEIGHT * Config.SCALE_FROM_HIGH)// v.getMeasuredHeight()
							- (list[1] - item[1]);
					break;
				} else
					upIdx--;
			}
		}

		int step = 40;
		int step_time = 10; // 每步移动时间
		int duration = (Math.abs(offset) / step + 1) * step_time;

		try {
			Class c = listView.getClass();
			Method smoothScrollBy = c.getMethod("smoothScrollBy", new Class[] {
					int.class, int.class });
			Object result = smoothScrollBy.invoke(listView, new Object[] {
					new Integer(offset), new Integer(duration) });
		} catch (Exception e) {
			// 如果sdk版本不支持smoothScrollBy则不滚动
		}
	}

	public boolean needMove(View v) {
		int[] list = { -1, -1 };
		listView.getLocationOnScreen(list);

		int idx = listView.indexOfChild(v);
		if (idx >= 0) {
			View lastItem = listView.getChildAt(idx);
			int[] item = { -1, -1 };
			lastItem.getLocationOnScreen(item);

			// 允许10以内的误差
			return (Math.abs(list[1] - item[1]) > 10);
		}

		return false;
	}

	public ListView getListView() {
		return listView;
	}
}
