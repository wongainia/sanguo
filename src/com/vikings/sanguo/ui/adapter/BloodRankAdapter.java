package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BloodRankInfoClient;
import com.vikings.sanguo.model.BriefUserInfoClient;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.HonorRankRewardTip;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BloodRankAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = convertView.findViewById(R.id.iconLayout);
			ViewUtil.setVisible(holder.rankFrame);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			holder.country = (TextView) convertView.findViewById(R.id.country);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.rewardLayout = (ViewGroup) convertView
					.findViewById(R.id.rewardLayout);
			holder.reward = (ImageView) convertView.findViewById(R.id.reward);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final BloodRankInfoClient bric = (BloodRankInfoClient) getItem(position);
		// 设置玩家信息
		final BriefUserInfoClient fakeUser = new BriefUserInfoClient();
		fakeUser.setId(bric.getUserId());
		fakeUser.setImage(bric.getImage());
		fakeUser.setCountry(bric.getCountry());
		// 设置排名
		if (bric.getRank() == 1) {
			IconUtil.setUserIcon(holder.iconLayout, fakeUser,
					StringUtil.color("NO.1", R.color.k7_color4));
		} else {
			IconUtil.setUserIcon(holder.iconLayout, fakeUser,
					StringUtil.color("NO." + bric.getRank(), R.color.k7_color9));
		}
		if (bric.getUserId() != Account.user.getId())
			holder.iconLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Config.getController().showCastle(fakeUser);
				}
			});
		else
			holder.iconLayout.setOnClickListener(null);

		ViewUtil.setRichText(holder.name, bric.getNick());
		ViewUtil.setRichText(holder.country, "  (" + fakeUser.getCountryName()
				+ ")");

		// 设置家族
		if (bric.hasGuild()) {
			ViewUtil.setRichText(holder.guild, "家族:" + bric.getBgic().getName());
			ViewUtil.setUnderLine(holder.guild);
			holder.guild.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(bric.getGuildId());
				}
			});
		} else {
			ViewUtil.setText(holder.guild, "家族: 无");
			ViewUtil.setTextViewNormal(holder.guild);
			holder.guild.setOnClickListener(null);
		}

		ViewUtil.setRichText(holder.desc, "昨日勇闯血战" + bric.getBestRecord() + "关");
		// 设置奖励
		if (bric.hasReward()) {
			ViewUtil.setVisible(holder.rewardLayout);
			new ViewImgScaleCallBack(bric.getRewardItem().getImage(),
					holder.reward, 70 * Config.SCALE_FROM_HIGH,
					70 * Config.SCALE_FROM_HIGH);
			holder.reward.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new HonorRankRewardTip(bric.getRewardItem()).show();
				}
			});
		} else {
			ViewUtil.setGone(holder.rewardLayout);
			ViewUtil.setImage(holder.reward, null);
			holder.reward.setOnClickListener(null);
		}

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.blood_rank_item;
	}

	private class ViewHolder {
		View iconLayout, rankFrame;
		TextView name, guild, country, desc;
		ViewGroup rewardLayout;
		ImageView reward;
	}
}
