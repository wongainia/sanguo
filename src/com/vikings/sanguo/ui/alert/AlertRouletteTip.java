package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class AlertRouletteTip extends ResultAnimTip {
	int count7;

	public AlertRouletteTip() {
		super(true);
	}

	private List<ShowItem> items;

	public void show(List<ShowItem> items, CallBack callBack, int count7) {
		this.items = items;
		this.count7 = count7;
		show(callBack, false);
	}

	@Override
	protected int getDrawable() {
		return 0;
	}

	@Override
	protected View getContent() {
		View view = controller.inflate(R.layout.result_task, rewardLayout,
				false);
		View vGroup = content.findViewById(R.id.rouletteLayout);
		if (count7 >= 1) {
			ViewUtil.setVisible(vGroup);
			ViewUtil.setVisible(vGroup.findViewById(R.id.name1));
			vGroup.findViewById(R.id.name1).setBackgroundResource(
					R.drawable.roulette7);
		}
		if (count7 >= 2) {
			ViewUtil.setVisible(vGroup.findViewById(R.id.name2));
			vGroup.findViewById(R.id.name2).setBackgroundResource(
					R.drawable.roulette7);
		}
		if (count7 >= 3) {
			ViewUtil.setVisible(vGroup.findViewById(R.id.name3));
			vGroup.findViewById(R.id.name3).setBackgroundResource(
					R.drawable.roulette7);
		}

		ViewGroup contentLayout = (ViewGroup) view.findViewById(R.id.items);
		if (null != items && !items.isEmpty()) {
			ViewGroup vg = (ViewGroup) controller.inflate(
					R.layout.task_award_item, contentLayout, false);

			ViewUtil.setRichText(view, R.id.title,
					StringUtil.color("获得奖励", R.color.color6));

			ViewGroup content = (ViewGroup) vg.findViewById(R.id.content);
			ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);

			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getCount() > 0) {
					if ((i + 1) % 2 == 0) {
						content1.addView(ViewUtil.getShowItemView(items.get(i),
								R.color.color15, false, false));
					} else {
						content.addView(ViewUtil.getShowItemView(items.get(i),
								R.color.color15, false, false));
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
