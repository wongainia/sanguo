package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.ManorDraft;
import com.vikings.sanguo.model.ShowItem;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.ui.adapter.ArmTrainingAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.guide.Step601;
import com.vikings.sanguo.ui.guide.StepMgr;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.TroopUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ArmTrainingListWindow extends CustomListViewWindow {
	private List<TroopProp> trainingList;

	@Override
	protected void init() {
		super.init("募兵营");
		setContentAboveTitle(R.layout.user_info_head);
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "选择对应的兵种，进行解锁和招募");
		initTrainingList();
	}

	@SuppressWarnings("unchecked")
	private void initTrainingList() {
		trainingList = new ArrayList<TroopProp>();
		List<TroopProp> ls = CacheMgr.troopPropCache.getTrainingTroops();
		for (TroopProp it : ls) {
			List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(it
					.getId());
			if (!drafts.isEmpty())
				trainingList.add(it);
		}
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		setAdapter();
		// 标题栏右侧描述
		setRightTxt("兵种：" + getRecruitedCount());
		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);

		step603Trigger();
	}

	private void step603Trigger() {
		if (!Step601.isFinish() && adapter != null
				&& adapter.getItem(0) != null) {
			// 盾兵数量
			TroopProp tProp = (TroopProp) adapter.getItem(0);
			int count = Account.myLordInfo.getArmCountByType(tProp.getId());
			List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(tProp
					.getId());
			if (!ListUtil.isNull(drafts)) {
				ManorDraft manorDraft = drafts.get(0);
				try {
					BuildingProp prop = (BuildingProp) CacheMgr.buildingPropCache
							.get(manorDraft.getBuildingId());
					// 单价
					int prices = TroopUtil.getPrices(tProp.getId(),
							prop.getId());

					int totalprices = prices * count;

					float resTrainingRate = manorDraft.getResourceDraftRate() / 100f; // 资源招募百分比
					int realCountByResource = Account.manorInfoClient
							.getDraftCountByRate(resTrainingRate);
					List<ShowItem> showItems = CacheMgr.manorDraftResourceCache
							.checkResourceEnough(tProp, prop,
									realCountByResource);

					if (totalprices != 0
							&& Account.user.getCurrency() >= totalprices
							&& !ListUtil.isNull(showItems)) {
						StepMgr.startStep603();
					}

				} catch (GameException e) {
				}

			}
		}
	}

	// 框体右按钮背景
	@Override
	protected byte getRightBtnBgType() {
		return WINDOW_BTN_BG_TYPE_DESC;
	}

	// 可招募兵种显示值
	private String getRecruitedCount() {
		int cnt = 0;
		for (TroopProp it : trainingList) {
			if (unlock(it))
				cnt++;
		}
		return cnt + "/" + trainingList.size();
	}

	// 设置列表显示可招募的士兵
	private void setAdapter() {
		if (adapter != null) {
			adapter.clear();
			sort(trainingList);
			adapter.addItems(trainingList);
			adapter.notifyDataSetChanged();
		}
	}

	// 士兵属性的排列
	private void sort(List<TroopProp> lt) {
		Collections.sort(lt, new Comparator<TroopProp>() {

			@Override
			public int compare(TroopProp object1, TroopProp object2) {
				if (unlock(object1) && !unlock(object2)) {
					return -1;
				} else if (!unlock(object1) && unlock(object2)) {
					return 1;
				}
				return object1.getId() - object2.getId();
			}
		});
	}

	// 是否是可招募的士兵
	private boolean unlock(TroopProp prop) {
		return Account.manorInfoClient.getArmids().contains(prop.getId());

	}

	// 检查兵种对应建筑是否存在
	// public boolean canRecruit(TroopProp prop) {
	// List<ManorDraft> drafts = CacheMgr.manorDraftCache.search(prop.getId());
	// BuildingProp buildingProp = null;
	// BuildingInfoClient bic = null;
	// if (!drafts.isEmpty()) {
	// ManorDraft draft = drafts.get(0);
	// buildingProp = CacheMgr.getBuildingByID(draft.getBuildingId());
	// bic = Account.manorInfoClient.getBuilding(buildingProp);
	// }
	//
	// return null != bic;
	// }

	@Override
	protected ObjectAdapter getAdapter() {
		adapter = new ArmTrainingAdapter();
		return adapter;
	}

}
