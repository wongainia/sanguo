package com.vikings.sanguo.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.battle.anim.AnimPool;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.EventRewards;
import com.vikings.sanguo.model.JumpTargetPanel;
import com.vikings.sanguo.model.Quest;
import com.vikings.sanguo.model.QuestInfoClient;
import com.vikings.sanguo.model.UserVip;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.adapter.ObjectAdapter;
import com.vikings.sanguo.ui.window.BonusWebWindow;
import com.vikings.sanguo.ui.window.RouletteWindow;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class EventRewardsUI extends BaseUI {
	public static final byte TYPE_EVENT = 1;
	public static final byte TYPE_REWARDS = 2;

	private static int ICON_WIDTH = 64;
	private static int ICON_HEIGHT = 64;
	private View view;
	private GridView gridView;

	private ImageView arrowIcon;
	private Adapter adapter;

	private byte type;
	private LayoutParams params;

	public EventRewardsUI(View view, ImageView arrowIcon) {
		this.view = view;
		this.arrowIcon = arrowIcon;
		ViewUtil.setImage(arrowIcon, R.drawable.extend_arrow);
		this.adapter = new Adapter();
		this.gridView = (GridView) view.findViewById(R.id.gridView);
		this.gridView.setAdapter(adapter);
		params = (LayoutParams) arrowIcon.getLayoutParams();
	}

	public ObjectAdapter getAdapter() {
		return adapter;
	}

	public GridView getGridView() {
		return gridView;
	}

	public void open(byte type) {
		this.type = type;
		ViewUtil.setVisible(view);
		setArrowIcon(type);
		if (null != adapter) {
			adapter.clear();
			adapter.addItems(getEventList(type));
			adapter.notifyDataSetChanged();
		}
	}

	public void setArrowIcon(byte type) {
		ViewUtil.setImage(arrowIcon,
				ImageUtil.getMirrorBitmapDrawable("extend_arrow"));
		if (type == TYPE_EVENT) {
			params.topMargin = (int) (Config.SCALE_FROM_HIGH * 18);
		} else if (type == TYPE_REWARDS) {
			params.topMargin = (int) (Config.SCALE_FROM_HIGH * 100);
		}
		arrowIcon.postInvalidate();
	}

	public void close() {
		type = 0;
		ViewUtil.setGone(view);
		ViewUtil.setImage(arrowIcon, R.drawable.extend_arrow);
	}

	public boolean isShowEvent() {
		return this.type == TYPE_EVENT;
	}

	public boolean isShowRewards() {
		return this.type == TYPE_REWARDS;
	}

	private List<EventRewards> getEventList(byte type) {
		List<EventRewards> list = new ArrayList<EventRewards>();
		if (ListUtil.isNull(Account.readLog.rewards) == false) {
			for (EventRewards rewards : Account.readLog.rewards) {
				if (rewards.getType() == type
						&& Account.user.getLevel() >= rewards.getMinLevel()) {
					list.add(rewards);
				}
			}
		}

		// 判断是否有升级奖励任务，无则
		if (ListUtil.isNull(list) == false) {
			for (Iterator<EventRewards> iterator = list.iterator(); iterator
					.hasNext();) {
				EventRewards rewards = iterator.next();
				if (rewards.getId() == EventRewards.ID_UPDATE
						&& Account.getUpdateQuest() == null) {
					iterator.remove();
					break;
				}
			}
			Collections.sort(list, new Comparator<EventRewards>() {

				@Override
				public int compare(EventRewards obj1, EventRewards obj2) {
					return obj1.getSequence() - obj2.getSequence();
				}
			});
		}
		return list;
	}

	private void showBonus(EventRewards eventRewards, View v) {
		boolean hasBonus = false;
		if (Account.user.getLevel() >= eventRewards.getMinLevel()
				&& eventRewards.getFirstFlash() == false
				&& eventRewards.getMinLevel() > 1) {
			hasBonus = true;
		}
		if (eventRewards.getId() == 8) {
			List<QuestInfoClient> ls = null;
			ls = Account.getNormalQuest();
			if (Account.hasBonus(ls)) {
				hasBonus = true;
			}
		}
		// 双倍优惠
		else if (eventRewards.getId() == 6
				&& Account.user.hasDoubleRechrgePrivilege()) {
			hasBonus = true;
		}
		// VIP 特权
		else if (eventRewards.getId() == 1) {
			List<QuestInfoClient> ls = null;
			ls = Account.getQuestInfoBySpecialType(Quest.SPECIAL_TYPE_VIP);
			if (null != ls && !ls.isEmpty()) {
				QuestInfoClient quest = ls.get(0);
				List<UserVip> list = CacheMgr.userVipCache.getAll();
				for (UserVip vip : list) {
					if (quest.getQuestId() == vip.getQuestId()
							&& Account.getCurVip().getLevel() >= vip.getLevel()) {
						hasBonus = true;
					}
				}

			}
		}
		// 包月
		else if (eventRewards.getId() == 7) {
			int leftDays = Account.user.getMonthChargeRewardLeftDays();
			if (leftDays > 0 && Account.user.rewardToday() == false) {
				hasBonus = true;
			}

		}
		// 外敌入侵
		else if (eventRewards.getId() == 3 && Account.user.invasionTimes() > 0) {
			hasBonus = true;
		}
		// 铜雀台
		else if (eventRewards.getId() == 4 && Account.user.dongjakTimes() > 0) {
			hasBonus = true;
		}
		// 荣耀榜
		else if (eventRewards.getId() == 9) {
			if (Account.hasHonorRankReward()) {
				hasBonus = true;
			}
		}
		// 每日签到
		else if (eventRewards.getId() == 12) {
			if (Account.user.dailyattendanceTimes() > 0) {
				hasBonus = true;
			}
		}
		// 我要变强
		else if (eventRewards.getId() == 14) {
			if (Account.user.stiffenTimes() > 0) {
				hasBonus = true;
			}

		} else if (eventRewards.getId() == 16) {
			if (Account.user.godWealthLeftTimes() > 0) {
				hasBonus = true;
			}
		} else if (eventRewards.getId() == 15) {
			if (Account.getUpdateQuest() != null) {
				hasBonus = true;
			}
		} else if (eventRewards.getId() == 5) {
			if (Account.user.realRouletteGoodIsFill())
				hasBonus = true;
		}

		if (hasBonus == true) {
			ViewUtil.setVisible(v);
			v.startAnimation(AnimPool.getAwardTip());
		} else {
			v.clearAnimation();
			ViewUtil.setHide(v);
		}
	}

	private class Adapter extends ObjectAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = Config.getController().inflate(getLayoutId());
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.bonus = convertView.findViewById(R.id.rward_tip);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final EventRewards eventRewards = (EventRewards) getItem(position);

			new ViewImgScaleCallBack(eventRewards.getIcon(), holder.icon,
					Config.SCALE_FROM_HIGH * ICON_WIDTH, Config.SCALE_FROM_HIGH
							* ICON_HEIGHT);
			ViewUtil.setText(holder.name, eventRewards.getName());

			ViewUtil.setHide(holder.bonus);
			showBonus(eventRewards, holder.bonus);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					close();
					if (eventRewards.getJumpId() == 1) {
						new BonusWebWindow().open(eventRewards.getName(),
								eventRewards.getUrl());
					} else {
						JumpTargetPanel.doJump(eventRewards.getJumpId());
					}
					if (Account.user.getLevel() >= eventRewards.getMinLevel()) {
						eventRewards.setFirstFlash(true);
					}
				}
			});
			return convertView;
		}

		public void setViewDisplay(View v, Object o, int index) {

		}

		@Override
		public int getLayoutId() {
			return R.layout.event_entry_grid_item;
		}

		private class ViewHolder {
			ImageView icon;
			TextView name;
			View bonus; // 右上角显示 new或者ok
		}

	}
}
