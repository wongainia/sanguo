/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-29 下午4:02:13
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.widget;

import com.vikings.sanguo.R;
import com.vikings.sanguo.ui.BaseUI;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;

import android.view.View;
import android.widget.ListView;

public class CommonFrameListView extends BaseUI {
	private ListView widget;

	public CommonFrameListView() {
		widget = (ListView) controller.inflate(R.layout.common_frame_list);
	}
	
	public CommonFrameListView(ObjectAdapter adapter) {
		widget = (ListView) controller.inflate(R.layout.common_frame_list);
		setAdapter(adapter);
	}

	public ListView getWidget() {
		return widget;
	}

	public void setAdapter(ObjectAdapter adapter) {
		if (null != adapter) {
			widget.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void setHeadView(View headerView) {
		if (widget.getHeaderViewsCount() == 0)
			widget.addHeaderView(headerView, null, false);
	}

	public void setFooterView(View footerView) {
		if (widget.getFooterViewsCount() == 0)
			widget.addFooterView(footerView, null, false);
	}

//	// 计算ListView中某个item需要移动的距离
//	public void smoothMoveItem(View v) {
//		int offset = 0;
//
//		int[] list = { -1, -1 };
//		widget.getLocationOnScreen(list);
//
//		int idx = widget.indexOfChild(v);
//		int upIdx = idx - 1;
//		while (upIdx >= 0) {
//			View lastItem = widget.getChildAt(upIdx);
//			int[] item = { -1, -1 };
//			lastItem.getLocationOnScreen(item);
//
//			if (item[1] <= list[1]) {
//				offset = (idx - upIdx) * v.getMeasuredHeight()
//						- (list[1] - item[1]);
//				break;
//			} else
//				upIdx--;
//		}
//
//		int step = 20;
//		int step_time = 20; // 每步移动时间
//		int duration = (offset / step + 1) * step_time;
////		widget.smoothScrollBy(offset, duration);
//		
//		try {
//			widget.smoothScrollBy(offset, duration);
//		} catch (Exception e) {
//			// 如果sdk版本不支持smoothScrollBy则不滚动
//		}
//	}
}
