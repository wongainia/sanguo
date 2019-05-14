package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.model.PlayerWantedInfoClient;
import com.vikings.sanguo.thread.CallBack;
import com.vikings.sanguo.thread.UserIconCallBack;
import com.vikings.sanguo.ui.alert.CommonCustomAlert;
import com.vikings.sanguo.ui.alert.FavorFiefSearchTip;
import com.vikings.sanguo.utils.DateUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class PlayerWantedInfoAdapter extends ObjectAdapter {
	private CallBack cb;

	public PlayerWantedInfoAdapter(CallBack cb) {
		this.cb = cb;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.wantedMeLayout = (ViewGroup) convertView
					.findViewById(R.id.wantedMeLayout);
			holder.wantedOtherLayout = (ViewGroup) convertView
					.findViewById(R.id.wantedOtherLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PlayerWantedInfoClient data = (PlayerWantedInfoClient) getItem(position);
		BriefUserInfoClient targetUser = data.getTargetUser();
		if (null != targetUser) {
			int leftTime = data.getInfo().getEndTime() - Config.serverTimeSS();
			if (leftTime < 0)
				leftTime = 0;
			if (data.getInfo().getTarget() == Account.user.getId()) {
				ViewUtil.setVisible(holder.wantedMeLayout);
				ViewUtil.setGone(holder.wantedOtherLayout);
				ViewUtil.setText(holder.wantedMeLayout, R.id.desc, "你正处于被追杀的状态");
				ViewUtil.setText(holder.wantedMeLayout, R.id.time,
						getLeftTime(leftTime));
			} else {
				ViewUtil.setGone(holder.wantedMeLayout);
				ViewUtil.setVisible(holder.wantedOtherLayout);
				ViewUtil.setText(holder.wantedOtherLayout, R.id.name,
						targetUser.getNickName());
				ViewUtil.setText(holder.wantedOtherLayout, R.id.level, "Lv:"
						+ targetUser.getLevel());
				ViewUtil.setText(holder.wantedOtherLayout, R.id.country, "国家:"
						+ targetUser.getCountryName());
				ViewUtil.setText(holder.wantedOtherLayout, R.id.time,
						getLeftTime(leftTime));

			}
			convertView.setOnClickListener(new ClickListener(data));

			new UserIconCallBack(targetUser, holder.iconLayout,
					Config.SCALE_FROM_HIGH * Constants.ICON_WIDTH,
					Config.SCALE_FROM_HIGH * Constants.ICON_HEIGHT);
		}
		return convertView;
	}

	private String getLeftTime(int leftTime) {
		if (leftTime <= 0) {
			return "已过期";
		} else {
			return "追杀令剩余时间:" + DateUtil.formatHour(leftTime);
		}
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.search_player_wanted_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout, wantedMeLayout, wantedOtherLayout;
	}

	private class ClickListener implements OnClickListener {
		private PlayerWantedInfoClient data;

		public ClickListener(PlayerWantedInfoClient data) {
			this.data = data;
		}

		@Override
		public void onClick(View v) {

			if (data.getInfo().getTarget().intValue() == Account.user.getId()) {
				new CommonCustomAlert("追杀令通知", CommonCustomAlert.DEFAULT, true,
						data.getWantedMeDesc(), "", null, "", null, "", false)
						.show();
			} else {
				if (null != cb)
					cb.onCall();
				new FavorFiefSearchTip(Constants.USER, data.getTargetUser())
						.show();
			}
		}
	}
}
