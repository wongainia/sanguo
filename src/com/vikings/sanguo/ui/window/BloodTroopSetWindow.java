package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BloodAttackInvoker;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.TroopAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class BloodTroopSetWindow extends TroopSetWindow implements CallBack {

	protected byte minTalent;// 最低将领品质
	private int total;// 士兵总量
	private TroopAdapter troopAdapter = new TroopAdapter(this, true);
	private int record;// 关卡

	public void open(int record) {
		this.record = record;
		doOpen();
		this.firstPage();
	}

	@Override
	protected void init() {
		super.init();
		ViewUtil.setVisible(gradientLayout);
		ViewUtil.setVisible(gradientBelowLayout);
		total = Account.manorInfoClient.getToplimitArmCount();
		if (null != adapter)
			((TroopAdapter) adapter).setTotal(total);
		setBottomButton("出   战", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new BloodConfAttackInvoker(record).start();
			}
		});
		setBottomPadding();
		ViewUtil.setText(gradientLayout.findViewById(R.id.gradientMsg),
				"点击头像更换武将");
	}

	@Override
	protected void setinitHeroInfos() {
		List<HeroInfoClient> list = Account.getBloodHeros();
		hics[0] = list.get(0);
		hics[1] = list.get(1);
		hics[2] = list.get(2);
	}

	private void setTroopDescView() {
		ViewUtil.setText(gradientBelowLayout, R.id.gradientMsg, "可派遣" + total
				+ "兵力， 已派" + getSelectTroopCount() + "兵力");
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		setTroopDescView();
	}

	private class BloodConfAttackInvoker extends BloodAttackInvoker {
		public BloodConfAttackInvoker(int num) {
			super(num);
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().bloodConf(tidyHeroData(), tidyArmInfos());
			super.fire();
		}

		@Override
		protected void onOK() {
			Config.getController().goBack();
			Config.getController().goBack();
			new BloodRewardWindow().open();
			super.onOK();

		}
	}

	@Override
	public void onCall() {
		setTroopDescView();
	}

	@Override
	protected List<ArmInfoSelectData> getArmInfoSelectDatas()
			throws GameException {
		List<ArmInfoSelectData> datas = new ArrayList<ArmInfoSelectData>();
		// 已经解锁的士兵id
		List<Integer> armIds = Account.manorInfoClient.getArmids();
		if (Account.myLordInfo.isBloodConfig()) { // 已经配置过兵力
			List<ArmInfoClient> aics = Account.myLordInfo.getLbic()
					.getBloodTroopInfo();
			for (Integer armId : armIds) {
				boolean has = false;
				for (ArmInfoClient aic : aics) {
					if (aic.getId() == armId) {
						has = true;
						ArmInfoSelectData data = new ArmInfoSelectData(
								aic.getId(), total, aic.getCount(), true, true);
						datas.add(data);
						break;
					}
				}
				if (!has) {
					ArmInfoSelectData data = new ArmInfoSelectData(armId,
							total, 0, true, true);
					datas.add(data);
				}
			}
		} else { // 未配置过兵力
			int count = 0;
			if (armIds.size() > 0)
				count = total / armIds.size();
			for (int i = 0; i < armIds.size(); i++) {
				int armId = armIds.get(i);
				ArmInfoSelectData data = new ArmInfoSelectData(armId, total,
						(i != armIds.size() - 1 ? count : total - count
								* (armIds.size() - 1)), true, true);
				datas.add(data);
			}
		}
		return datas;
	}

	@Override
	protected BriefFiefInfoClient getHeroChooseBriefFief() {
		return null;
	}

	@Override
	public void handleItem(Object o) {
	}

	@Override
	protected ObjectAdapter getAdapter() {
		troopAdapter.setTroopEffectInfo(Account.getUserTroopEffectInfo());
		return troopAdapter;
	}

	@Override
	protected boolean needSaveBloodHero() {
		return true;
	}

	@Override
	protected CallBack getCallBackAfterChooseHero() {
		return null;
	}
}
