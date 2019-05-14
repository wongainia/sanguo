package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.model.BattleLogInfoClient;
import com.vikings.sanguo.model.BattleLogReturnInfoClient;
import com.vikings.sanguo.model.BattleLossDetail;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ActClearWinTip extends CustomConfirmDialog {

	public ActClearWinTip(BattleLogInfoClient blic, final ReturnInfoClient ric) {
		super("扫荡副本成功", MEDIUM);

		ViewUtil.setImage(content.findViewById(R.id.pattenMirror),
				ImageUtil.getMirrorBitmapDrawable("pattern_line.png"));

		List<BattleLogReturnInfoClient> ownReward = blic.getOwnReward();
		if (ListUtil.isNull(ownReward))
			ViewUtil.setGone(tip, R.id.rewardFrame);
		else {
			ViewGroup reward = (ViewGroup) tip.findViewById(R.id.reward);
			for (BattleLogReturnInfoClient it : ownReward) {
				if (null != it.getReturnInfoClient()) {
					switch (it.getType()) {
					case 1:
						reward.addView(ViewUtil.getBonusView(
								it.getReturnInfoClient(),
								CampaignInfoClient.BONUS_FIX, reward, 0, null));
						break;
					// case 2:
					// reward.addView(ViewUtil.getBonusView(it
					// .getReturnInfoClient(),
					// CampaignInfoClient.BONUS_WAR, reward, blic
					// .getDetail().getRecord(), null));
					// break;
					case 3:
						reward.addView(ViewUtil.getBonusView(
								it.getReturnInfoClient(),
								CampaignInfoClient.BONUS_RATE, reward, 0, null));
						break;
					default:
						break;
					}
				}
			}
		}

		BattleLossDetail bld = blic.getMyTotalLoss();
		if (null == bld)
			ViewUtil.setGone(tip, R.id.lossFrame);
		else {
			ViewGroup loss = (ViewGroup) tip.findViewById(R.id.loss);
			ViewUtil.setText(tip, R.id.lossDesc, "您攻击损失" + bld.getTotalLoss()
					+ "名士兵");
			View v = ViewUtil.getLossView(blic.getMyTotalLoss(), loss);
			if (null != v)
				loss.addView(v);
		}

		setButton(FIRST_BTN, "确    定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();

				if (null != ric && ric.getLevel() > 0)
					new UserLevelUpTip(ric).show();
			}
		});
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_act_clear_win, contentLayout,
				false);
	}
}
