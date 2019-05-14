package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class AlertTaskFinishTip extends ResultAnimTip {
		
	public AlertTaskFinishTip() {
		super(true);
	}

	private List<ShowItem> items;

	public void show(List<ShowItem> items, CallBack callBack) {
		this.items = items;
		show(callBack, false);
	}
	
	@Override
	protected int getDrawable() {
		return R.drawable.txt_rwwc;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_task, rewardLayout,
				false);

		ViewGroup contentLayout = (ViewGroup) view.findViewById(R.id.items);
		if (null != items && !items.isEmpty()) {
			ViewGroup vg = (ViewGroup) controller.inflate(
					R.layout.task_award_item, contentLayout, false);

			ViewUtil.setText(view, R.id.title, "你获得了以下奖励");

			ViewGroup content0 = (ViewGroup) vg.findViewById(R.id.content);
			ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);

			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getCount() > 0) {
					if ((i + 1) % 2 == 0) {
						content1.addView(ViewUtil.getShowItemView(items.get(i),
								R.color.color7, false, false));
					} else {
						content0.addView(ViewUtil.getShowItemView(items.get(i),
								R.color.color7, false, false));
					}
				}

			}
			contentLayout.addView(vg);
		} else {
			ViewUtil.setGone(view, R.id.title);
		}
		return view;
	}
}
