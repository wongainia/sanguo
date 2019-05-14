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
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.ui.adapter.ArmEnhanceAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class ArmEnhanceListWindow extends CustomListViewWindow {

	@Override
	protected void init() {
		adapter = new ArmEnhanceAdapter();
		List<TroopProp> ls = getEnhanceArm();
		sort(ls);
		adapter.addItems(ls);
		super.init("部队强化");
		setContentAboveTitle(R.layout.user_info_head);
		setContentBelowTitle(R.layout.gradient_msg);
		TextView textView = (TextView) window.findViewById(R.id.gradientMsg);
		textView.setTextSize(14);
		ViewUtil.setText(textView, "选择对应的兵种，提升攻防属性");
	}

	// 未解锁的在最下面
	private void sort(List<TroopProp> lt) {
		Collections.sort(lt, new Comparator<TroopProp>() {
			@Override
			public int compare(TroopProp object1, TroopProp object2) {
				if (ArmEnhanceAdapter.armUnLock(object1.getId())
						&& !ArmEnhanceAdapter.armUnLock(object2.getId())) {
					return -1;
				} else if (!ArmEnhanceAdapter.armUnLock(object1.getId())
						&& ArmEnhanceAdapter.armUnLock(object2.getId())) {
					return 1;
				}
				return object1.getId() - object2.getId();
			}
		});
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);
	}

	private List<TroopProp> getEnhanceArm() {
		List<Integer> armIds = Account.armEnhanceCache.getEnhanceArmId();
		List<TroopProp> list = new ArrayList<TroopProp>();
		for (Integer armId : armIds) {
			try {
				TroopProp troopProp = (TroopProp) CacheMgr.troopPropCache
						.get(armId);
				list.add(troopProp);
			} catch (GameException e) {
			}
		}
		return list;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected int refreshInterval() {
		return 10000;
	}
}
