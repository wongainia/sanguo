package com.vikings.sanguo.ui.alert;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.biz.GameBiz;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.exception.GameException;
import com.vikings.sanguo.invoker.BaseInvoker;
import com.vikings.sanguo.model.CheckInRewards;
import com.vikings.sanguo.model.Item;
import com.vikings.sanguo.model.ReturnInfoClient;
import com.vikings.sanguo.protos.RES_DATA_TYPE;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.PressedZoomButton;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ImageUtil;
import com.vikings.sanguo.utils.ViewUtil;
import com.vikings.sanguo.widget.CustomConfirmDialog;

public class DailyRewardTip extends CustomConfirmDialog implements
		OnClickListener {
	private View checkinRewards, checkInContent;
	private int itemDays[] = { R.id.checkin_one, R.id.checkin_two,
			R.id.checkin_three, R.id.checkin_four, R.id.checkin_five,
			R.id.checkin_six, R.id.checkin_seven };

	private int itemDaysBg[] = { R.drawable.checkin_oneday,
			R.drawable.checkin_twoday, R.drawable.checkin_threeday,
			R.drawable.checkin_fourday, R.drawable.checkin_fiveday,
			R.drawable.checkin_sixday, R.drawable.checkin_sevenday };

	public DailyRewardTip() {
		super(CustomConfirmDialog.DEFAULT);
		checkInContent = content.findViewById(R.id.checkin_content);
		ViewUtil.setImage(checkInContent, R.drawable.checkin_bottom);
		refreshInterval = 500;
	}

	@Override
	public void show() {
		setValue();
		super.show();
		ViewUtil.setGone(tip.findViewById(R.id.title_layout));
	}

	private void setValue() {
		checkinRewards = tip.findViewById(R.id.checkinRewards);
		checkinRewards.setOnClickListener(this);
		tip.findViewById(R.id.bg).setBackgroundDrawable(null);
		ViewUtil.setImage(tip.findViewById(R.id.checkin_content),
				"checkin_bottom.png");

		((View) tip.findViewById(R.id.checkin_close))
				.setOnClickListener(closeL);
		int cnt = Account.user.getCheckinCount();
		long timeLong = Account.user.getLastCheckinTime() * 1000L;

		if (!DateUtil.isYesterday(timeLong) && !DateUtil.isToday(timeLong)) {
			cnt = 0;
		}

		if (DateUtil.isToday(timeLong)) {
			canRewords(false);
		} else {
			canRewords(true);
		}

		int curVip = Account.getCurVip().getLevel();
		int maxDay = CacheMgr.checkInRewardsCache.maxDay(curVip);

		for (int i = 0; i < maxDay; i++) {
			// 获取每个Item View
			ViewGroup vGroup = (ViewGroup) tip.findViewById(itemDays[i]);
			// 设置days背景
			vGroup.findViewById(R.id.days).setBackgroundResource(itemDaysBg[i]);
			// 奖品图片
			ImageView numBg = (ImageView) vGroup.findViewById(R.id.numBg);
			// 奖品个数
			TextView num = (TextView) vGroup.findViewById(R.id.num);
			// 是否已经领奖背景
			View isCheckView = vGroup.findViewById(R.id.check);

			ReturnInfoClient ri = CacheMgr.checkInRewardsCache.day(curVip,
					i + 1);

			num.setText("x" + ri.getCheckInRewards().getAmount());

			vGroup.setTag(ri);
			vGroup.setOnClickListener(this);

			setNumBg(ri.getCheckInRewards(), numBg);

			if (i < cnt) {
				isCheckView.setBackgroundResource(R.drawable.everyday_checked);
				vGroup.findViewById(R.id.days).setBackgroundResource(
						R.drawable.award_yet);
				if (cnt >= maxDay && i == maxDay - 1) {
					isCheckView.setBackgroundDrawable(null);
					vGroup.findViewById(R.id.days).setBackgroundResource(
							itemDaysBg[i]);
				}
			} else {
				isCheckView.setBackgroundDrawable(null);
				vGroup.findViewById(R.id.days).setBackgroundResource(
						itemDaysBg[i]);
			}
		}
	}

	private void setNumBg(CheckInRewards checkinRewards, ImageView numBg) {
		switch (RES_DATA_TYPE.valueOf(checkinRewards.getType())) {
		case RES_DATA_TYPE_ATTR:
			new ViewImgScaleCallBack(
					ReturnInfoClient.getAttrTypeBigIconName(checkinRewards
							.getThingId()), numBg,
					Constants.CHECKIN_ICON_WIDTH, Constants.CHECKIN_ICON_WIDTH);
			break;
		case RES_DATA_TYPE_ITEM:
			Item item = null;
			try {
				item = (Item) CacheMgr.itemCache.get(checkinRewards
						.getThingId());
			} catch (GameException e) {
				e.printStackTrace();
			}
			if (item != null) {
				new ViewImgScaleCallBack(item.getImage(), numBg,
						Constants.CHECKIN_ICON_WIDTH,
						Constants.CHECKIN_ICON_HEIGHT);
			}
			break;
		case RES_DATA_TYPE_ARM:
		default:
			break;
		}

	}

	private void canRewords(boolean isReword) {
		if (isReword == false) {
			ImageUtil.setBgGray(checkinRewards);
		} else {
			ImageUtil.setBgNormal(checkinRewards);
		}
	}

	@Override
	protected void updateDynView() {
		if (!DateUtil.isToday(Account.user.getLastCheckinTime() * 1000L)) {
			PressedZoomButton checkin_title = (PressedZoomButton) tip
					.findViewById(R.id.checkinRewards);
			if (checkin_title != null) {
				if (checkin_title.getTag() == null) {
					checkin_title.setTag(false);
				}
				if ((Boolean) checkin_title.getTag() == false) {
					checkin_title.handZoomSmall((Boolean) checkin_title
							.getTag());
					checkin_title.setTag(true);
				} else if ((Boolean) checkin_title.getTag() == true) {
					checkin_title.handZoomSmall((Boolean) checkin_title
							.getTag());
					checkin_title.setTag(false);
				}
			}
		}

	}

	@Override
	protected View getContent() {
		return controller.inflate(R.layout.alert_daily_reward, tip, false);
	}

	@Override
	public void onClick(View v) {
		if (v == checkinRewards) {
			new CheckInInvoker().start();
		} else {
			for (int i = 0; i < itemDays.length; i++) {
				if (itemDays[i] == v.getId()) {
					new RewardTip("第" + (i + 1) + "天奖励详情",
							((ReturnInfoClient) v.getTag()).showReturn(true),
							false, false).show();
					return;
				}
			}
		}

	}

	class CheckInInvoker extends BaseInvoker {
		private ReturnInfoClient ric;

		@Override
		protected String loadingMsg() {
			return "申请奖励中...";
		}

		@Override
		protected String failMsg() {
			return "获取每日登录礼包失败";
		}

		@Override
		protected void fire() throws GameException {
			ric = GameBiz.getInstance().checkIn();
		}

		@Override
		protected void onOK() {
			dismiss();
			Account.readLog.LAST_CHECKIN_TIME = Config.serverTime();
			new RewardTip("领奖成功", ric.showReturn(true), false, false).show();
		}

	}
}
