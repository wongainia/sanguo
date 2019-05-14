package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.model.BuildingType;
import com.vikings.sanguo.model.UserInfoHeadData;
import com.vikings.sanguo.ui.adapter.BuildingAdapter;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class BuildingListWindow extends CustomListViewWindow {

	private BuildingType type;

	@Override
	protected void init() {
		adapter = new BuildingAdapter();
		super.init("建筑列表");
		setContentAboveTitle(R.layout.user_info_head);
	}

	public void open(BuildingType type) {
		this.type = type;
		this.doOpen();

	}

	@Override
	public void showUI() {
		super.showUI();
		if (null != adapter) {
			adapter.clear();
			adapter.addItems(getBuilding(type.getType()));
			adapter.notifyDataSetChanged();
		}
		ViewUtil.setUserInfoHeadAttrs(window,
				UserInfoHeadData.getSpecialDatas1(), true, Account.user);
	}

	public List<BuildingProp> getBuilding(short type) {
		List<BuildingProp> list = new ArrayList<BuildingProp>();
		// 初始建筑
		List<BuildingProp> initialBuildings = new ArrayList<BuildingProp>();
		initialBuildings = CacheMgr.buildingPropCache
				.getInitialBuildingsByMainType(type);
		// 自己拥有的建筑
		List<BuildingInfoClient> ownBuildings = new ArrayList<BuildingInfoClient>();
		if (null != Account.manorInfoClient
				&& !Account.manorInfoClient.getBuildingInfos().isEmpty()) {
			ownBuildings.addAll(Account.manorInfoClient.getBuildingInfos());
		}

		// 将自己已有的建筑替换初始建筑,且已经有的建筑在列表中显示的是下一个等级的建筑（如果有）
		for (BuildingProp prop : initialBuildings) {
			// 所有不需要显示的建筑的排序会配置为0
			if (prop.getSequence() == 0) {
				continue;
			}
			boolean has = false;
			for (int i = 0; i < ownBuildings.size(); i++) {
				if (prop.isSameSeries(ownBuildings.get(i).getProp())) {
					list.add(ownBuildings.get(i).getProp());
					has = true;
					break;
				}
			}
			if (!has) {
				list.add(prop);
			}

		}
		sort(list);
		return list;
	}

	private void sort(List<BuildingProp> list) {
		Collections.sort(list, new Comparator<BuildingProp>() {

			@Override
			public int compare(BuildingProp prop1, BuildingProp prop2) {

				boolean topLevel1 = topLevel(prop1);
				boolean topLevel2 = topLevel(prop2);
				if (!topLevel1 && topLevel2)
					return -1;
				else if (topLevel1 && !topLevel2)
					return 1;
				else {
					boolean unlock1 = unlock(prop1);
					boolean unlock2 = unlock(prop2);
					if (unlock1 && !unlock2)
						return -1;
					else if (!unlock1 && unlock2)
						return 1;
					else
						return prop1.getSequence() - prop2.getSequence();
				}

			}

		});
	}

	// private boolean canBuild(BuildingProp prop) {
	// return unlock(prop) && topLevel(prop);
	// }

	// 是否解锁
	private boolean unlock(BuildingProp prop) {
		BuildingInfoClient bic = Account.manorInfoClient.getBuilding(prop);
		if (null != bic) {
			BuildingProp next = bic.getProp().getNextLevel();
			if (null != next) {
				return BuildingRequirementCache.unlock(next.getId(),
						next.isCheckLv());
			} else {
				return false;
			}
		} else {
			return BuildingRequirementCache.unlock(prop.getId(),
					prop.isCheckLv());
		}
	}

	private boolean topLevel(BuildingProp prop) {
		BuildingInfoClient bic = Account.manorInfoClient.getBuilding(prop);
		if (null != bic && bic.getProp().getNextLevel() == null)
			return true;
		else
			return false;
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

	@Override
	protected int refreshInterval() {
		return 10000;
	}

}
