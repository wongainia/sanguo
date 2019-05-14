package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BuildingType;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.ui.adapter.BuildingTypeAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class BuildingTypeListWindow extends CustomListViewWindow {

	@Override
	protected void init() {
		adapter = new BuildingTypeAdapter();
		super.init("建造建筑");
		setContentAboveTitle(R.layout.user_info_head);
	}

	public void open() {
		this.doOpen();
	}

	@Override
	public void showUI() {
		super.showUI();
		if (null != adapter) {
			adapter.clear();
			adapter.addItems(getBuildingType());
			adapter.notifyDataSetChanged();
		}
		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);
	}

	private List<BuildingType> getBuildingType() {
		List<BuildingType> list = new ArrayList<BuildingType>();
		List<BuildingType> all = CacheMgr.buildingTypeCache.getAll();
		for (BuildingType type : all) {
			if (Account.user.getLevel() >= type.getOpenLv()) {
				list.add(type);
			}
		}
		if (!list.isEmpty())
			Collections.sort(list, new Comparator<BuildingType>() {
				@Override
				public int compare(BuildingType type1, BuildingType type2) {
					return type1.getSeq() - type2.getSeq();
				}
			});
		return list;
	}

	private List<UserInfoHeadData> getUserInfoHeadDatas() {
		List<UserInfoHeadData> datas = new ArrayList<UserInfoHeadData>();
		UserInfoHeadData data0 = new UserInfoHeadData();
		data0.setType(UserInfoHeadData.DATA_TYPE_MONEY);
		data0.setValue(Account.user.getMoney());
		datas.add(data0);

		UserInfoHeadData data1 = new UserInfoHeadData();
		data1.setType(UserInfoHeadData.DATA_TYPE_CURRENCY);
		data1.setValue(Account.user.getCurrency());
		datas.add(data1);

		UserInfoHeadData data2 = new UserInfoHeadData();
		data2.setType(UserInfoHeadData.DATA_TYPE_FOOD);
		data2.setValue(Account.user.getFood());
		datas.add(data2);

		UserInfoHeadData data3 = new UserInfoHeadData();
		data3.setType(UserInfoHeadData.DATA_TYPE_ARMY);
		data3.setValue(Account.myLordInfo.getArmCount());
		datas.add(data3);
		return datas;
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}
}
