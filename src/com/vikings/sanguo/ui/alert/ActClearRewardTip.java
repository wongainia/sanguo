/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-9-26 下午3:41:49
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.alert;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.model.ActInfoClient;
import com.vikings.sanguo.model.CampaignInfoClient;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class ActClearRewardTip extends CustomConfirmDialog {

	public ActClearRewardTip(ActInfoClient act) {
		super("查看扫荡奖励", MEDIUM);
		List<CampaignInfoClient> campaigns = act.getCampaignList();

		ReturnInfoClient fixBonus = null;
		ReturnInfoClient warBonus = null;
		ReturnInfoClient rateBonus = null;

		for (CampaignInfoClient it : campaigns) {
			if (null == fixBonus)
				fixBonus = it.getBonus(CampaignInfoClient.BONUS_FIX);
			else
				fixBonus = fixBonus.mergeBonus(it
						.getBonus(CampaignInfoClient.BONUS_FIX));

			// if (null == warBonus)
			// warBonus = it.getBonus(CampaignInfoClient.BONUS_WAR);
			// else
			// warBonus = warBonus.mergeBonus(it
			// .getBonus(CampaignInfoClient.BONUS_WAR));

			if (null == rateBonus)
				rateBonus = it.getBonus(CampaignInfoClient.BONUS_RATE);
			else
				rateBonus = rateBonus.mergeBonus(it
						.getBonus(CampaignInfoClient.BONUS_RATE));
		}

		ViewGroup reward = (ViewGroup) tip.findViewById(R.id.reward);
		reward.addView(ViewUtil.getBonusView(fixBonus,
				CampaignInfoClient.BONUS_FIX, reward, 0,
				Account.user.getCurVip()));
		// reward.addView(ViewUtil.getBonusView(warBonus,
		// CampaignInfoClient.BONUS_WAR, reward, 0,
		// Account.user.getCurVip()));
		reward.addView(ViewUtil.getBonusView(rateBonus,
				CampaignInfoClient.BONUS_RATE, reward, 0,
				Account.user.getCurVip()));

		setButton(FIRST_BTN, "确    定", closeL);
	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_act_clear_reward, tip, false);
	}
}
