package com.vikings.sanguo.ui.window;

import java.util.ArrayList;
import java.util.List;

import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.adapter.VipAdapter;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.widget.CustomListViewWindow;

public class VipListWindow extends CustomListViewWindow implements CallBack {
	private boolean first = true;

	@Override
	protected void init() {
		adapter = new VipAdapter(this);
		super.init("至尊VIP");
		listView.setDividerHeight((int) (8 * Config.SCALE_FROM_HIGH));
	}

	public void open() {
		doOpen();
	}

	@Override
	protected ObjectAdapter getAdapter() {
		return adapter;
	}

	private void update() {
		if (null != adapter) {
			adapter.clear();
			int postion = setVipAdapter((VipAdapter) adapter);
			adapter.notifyDataSetChanged();
			if (first) { // 首次进来，滚动到指定的位置
				listView.setSelection(postion);
				first = false;
			}
		}
	}

	@Override
	public void showUI() {
		super.showUI();
		update();
	}

	@SuppressWarnings("unchecked")
	// 返回值为列表需要滚动到的位置
	private int setVipAdapter(VipAdapter vipAdapter) {
		int index = -1;
		List<UserVip> vips = new ArrayList<UserVip>();
		int curVipLevel = Account.getCurVip().getLevel();
		int charge = Account.user.getCharge();

		List<UserVip> list = CacheMgr.userVipCache.getAll();

		QuestInfoClient quest = getVipQuest();
		// 将要领奖的vip等级
		int questVipLevel = 0;

		for (UserVip vip : list) {
			if (vip.getLevel() > 0 && vip.getLevel() <= curVipLevel + 5)
				vips.add(vip);

			if (index == -1 && vip.getLevel() == curVipLevel + 1) { // 取当前等级的下一个位置
				index = vips.size() - 1;
			}

			if (null != quest && quest.getQuestId() == vip.getQuestId()) { // 取可以领奖的位置
				questVipLevel = vip.getLevel();
				index = vips.size() - 1;
			}

		}

		vipAdapter.addItems(vips);
		vipAdapter.setInfos(curVipLevel, questVipLevel, charge, quest);
		return index;
	}

	// 获取未完成领奖任务的vip等级
	private QuestInfoClient getVipQuest() {
		List<QuestInfoClient> quest = Account
				.getQuestInfoBySpecialType(Quest.SPECIAL_TYPE_VIP);
		if (!ListUtil.isNull(quest))
			return quest.get(0);
		return null;
	}

	@Override
	public void onCall() {
		update();
	}
}
