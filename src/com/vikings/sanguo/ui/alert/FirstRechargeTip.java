package com.vikings.sanguo.ui.alert;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.FirstRechargeReward;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.thread.ViewImgCallBack;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class FirstRechargeTip extends CustomConfirmDialog{

	public FirstRechargeTip() {
		super("首次充值活动", CustomConfirmDialog.HUGE);
		
		ViewGroup content = (ViewGroup) findViewById(R.id.content);
		ArrayList<FirstRechargeReward> ls = CacheMgr.firstRechargeRewardCache.getAll();
		for (FirstRechargeReward it : ls) {
			ViewGroup vg = (ViewGroup) controller.inflate(R.layout.first_recharge_item);
			
			try {
				switch (it.getType()) {
				case 1:
					HeroProp hp = (HeroProp) CacheMgr.heroPropCache.get(it
							.getRewardId());
					new ViewImgCallBack(hp.getIcon(), vg.findViewById(R.id.icon));
					ViewUtil.setText(vg, R.id.name, hp.getName());
					ViewUtil.setText(vg, R.id.title, "充满" + it.getMoney() + "元," + hp.getName() + "帮你平天下");
					ViewUtil.setText(vg, R.id.desc, it.getRewardDesc());
					ViewUtil.setText(vg, R.id.bonus, it.getRechargeDesc());
					break;
				case 2:
					break;
				}
			} catch (GameException e) {
				e.printStackTrace();
			}
			
			content.addView(vg);
		}
		
		ViewUtil.setRichText(findViewById(R.id.doubleRecharge),
				"1.你充值得到的元宝数量翻倍,获得更多划算<br>2.每个帐号只有一次翻倍机会,千万别错过!");
	}
	
	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_first_recharge);
	}

}
