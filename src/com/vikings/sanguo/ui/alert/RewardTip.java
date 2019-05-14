package com.vikings.sanguo.ui.alert;

import java.util.List;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.ViewGroup;
import com.vikings.sanguo.R;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class RewardTip extends CustomConfirmDialog {

	public RewardTip(String title, List<ShowItem> reward) {
		this(title, reward, false, false);
	}

	public RewardTip(String title, List<ShowItem> reward, boolean heroColor,
			boolean soulQulity, final CallBack callBack) {
		super("", reward.size() > 8 ? MEDIUM : DEFAULT, true);
		ViewUtil.setGone(tip, R.id.closeDesc);
		ViewUtil.setRichText(content, R.id.rewardTitle, title);
		fillRewards(reward, heroColor, soulQulity);
		ViewUtil.setGone(tip, R.id.rtClose);

		if (callBack == null)
			this.dialog.setOnCancelListener(null);
		else
			this.dialog.setOnCancelListener(new CancleListener(callBack));
	}

	public RewardTip(String title, List<ShowItem> reward, boolean heroColor,
			boolean soulQulity) {
		super("", reward.size() > 8 ? MEDIUM : DEFAULT, true);
		ViewUtil.setGone(tip, R.id.closeDesc);
		ViewUtil.setRichText(content, R.id.rewardTitle, title);
		fillRewards(reward, heroColor, soulQulity);
		ViewUtil.setGone(tip, R.id.rtClose);
	}

	private void fillRewards(List<ShowItem> reward, boolean heroColor,
			boolean soulQulity) {
		ViewGroup items = (ViewGroup) content.findViewById(R.id.items);
		items.setOnClickListener(closeL);
		ViewGroup vg = (ViewGroup) controller
				.inflate(R.layout.gamble_award_item);
		ViewUtil.setGone(vg.findViewById(R.id.separateLine1));

		ViewGroup content = (ViewGroup) vg.findViewById(R.id.content);
		ViewGroup content1 = (ViewGroup) vg.findViewById(R.id.content1);

		for (int i = 0; i < reward.size(); i++) {
			if (reward.get(i).getCount() > 0) {
				if ((i + 1) % 2 == 0) {
					content1.addView(ViewUtil.getShowItemView(reward.get(i),
							R.color.color15, heroColor, soulQulity));
				} else {
					content.addView(ViewUtil.getShowItemView(reward.get(i),
							R.color.color15, heroColor, soulQulity));
				}
			}
		}
		items.addView(vg);
	}

	@Override
	protected View getContent() {
		tip.findViewById(R.id.bg).setBackgroundDrawable(null);
		return controller.inflate(R.layout.alert_reward_tip);
	}

	private class CancleListener implements OnCancelListener {

		CallBack call;

		public CancleListener(CallBack call) {
			this.call = call;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if (null != call)
				call.onCall();
			RewardTip.this.dialog.setOnCancelListener(null);
		}

	}
}
