package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.ArmInfoSelectData;
import com.vikings.sanguo.model.BriefFiefInfoClient;
import com.vikings.sanguo.model.HeroIdInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.TroopAdapter;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaTroopSetWindow extends TroopSetWindow implements CallBack {

	private int total;
	private TroopAdapter troopAdapter = new TroopAdapter(this, true);

	@Override
	protected void init() {
		super.init();
		ViewUtil.setVisible(gradientLayout);
		ViewUtil.setVisible(gradientBelowLayout);
		total = Account.manorInfoClient.getToplimitArmCount();
		if (null != adapter)
			((TroopAdapter) adapter).setTotal(total);
		setBottomButton("保存配置", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getSelectHeroCount() <= 0) {
					controller.alert("请至少配置1名将领");
					return;
				}
				new SaveTroopInvoker().start();
			}
		});
		ViewUtil.setText(gradientLayout.findViewById(R.id.gradientMsg),
				"点击头像更换武将");
	}

	@Override
	protected void updateUI() {
		super.updateUI();
		setLeftTroopView();
	}

	public void open() {
		doOpen();
		this.firstPage();
	}

	@Override
	protected void setinitHeroInfos() {
		super.setinitHeroInfos();
		List<HeroIdInfoClient> arenaHeroInfos = Account.myLordInfo
				.getArenaHeroInfos();
		for (int i = 0; i < arenaHeroInfos.size(); i++) {
			HeroIdInfoClient hiic = arenaHeroInfos.get(i);
			HeroInfoClient heroInfo = Account.heroInfoCache.get(hiic.getId());
			if (heroInfo == null)
				continue;
			if (hiic.isMainHero())
				hics[0].update(heroInfo);
			else {
				if (!hics[1].isValid())
					hics[1].update(heroInfo);
				else
					hics[2].update(heroInfo);
			}
		}
	}

	@Override
	protected List<ArmInfoSelectData> getArmInfoSelectDatas()
			throws GameException {
		List<ArmInfoSelectData> datas = new ArrayList<ArmInfoSelectData>();
		// 已经解锁的士兵id
		List<Integer> armIds = Account.manorInfoClient.getArmids();
		if (Account.myLordInfo.isArenaConfig()) { // 已经配置过兵力
			List<ArmInfoClient> aics = Account.myLordInfo.getArenaTroopInfo();
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

	private void setLeftTroopView() {
		ViewUtil.setText(gradientBelowLayout, R.id.gradientMsg, "已派遣 "
				+ getSelectTroopCount() + "士兵，最多可派" + total + "士兵");
	}

	@Override
	public void onCall() {
		setLeftTroopView();
	}

	private class SaveTroopInvoker extends BaseInvoker {

		@Override
		protected String loadingMsg() {
			return "保存配置";
		}

		@Override
		protected String failMsg() {
			return "保存配置失败";
		}

		@Override
		protected void fire() throws GameException {
			GameBiz.getInstance().arenaConf(tidyHeroData(), tidyArmInfos());
		}

		@Override
		protected void onOK() {
			ctr.alert("配置成功");
			controller.goBack();
		}

	}

	@Override
	protected CallBack getCallBackAfterChooseHero() {
		return null;
	}

}
