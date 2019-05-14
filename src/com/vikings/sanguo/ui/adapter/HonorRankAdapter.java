/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-8 下午5:33:54
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HonorRankData;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.HonorRankRewardTip;
import com.vikings.sanguo.ui.window.GuildInfoWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public abstract class HonorRankAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.iconLayout = convertView.findViewById(R.id.iconLayout);
			ViewUtil.setVisible(holder.rankFrame);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.reward = (ImageView) convertView.findViewById(R.id.reward);
			holder.honor_rank_layout = convertView
					.findViewById(R.id.honor_rank_layout);
			holder.reward_item = convertView.findViewById(R.id.reward_item);
			convertView.setTag(holder);
		}
		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		HonorRankData hrd = (HonorRankData) o;
		setItemBg(holder, index);
		setUserInfo(holder, hrd, index);
		setGuildInfo(holder, hrd, index);
		setDesc(holder, hrd, index);
		setRewardInfo(holder, hrd);
	}

	private void setItemBg(ViewHolder holder, int position) {
		if (position == 0) {
			holder.honor_rank_layout
					.setBackgroundResource(R.drawable.common_item_bg);
			holder.reward_item.setBackgroundResource(R.drawable.honor_box_btm1);
		} else {
			holder.honor_rank_layout
					.setBackgroundResource(R.drawable.transcript_list_bg);
			holder.reward_item
					.setBackgroundResource(R.drawable.honor_box_btm_other);
		}
	}

	protected void setUserInfo(ViewHolder holder, final HonorRankData hrd,
			int position) {
		if (null != hrd.getUser()) {
			if (0 == indexOf(hrd)) {
				IconUtil.setUserIcon(holder.iconLayout, hrd.getUser(),
						StringUtil.color("NO.1", R.color.color13));
			} else {
				IconUtil.setUserIcon(holder.iconLayout, hrd.getUser(),
						StringUtil.color("NO." + (indexOf(hrd) + 1),
								R.color.color13));
			}

			holder.iconLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Config.getController().showCastle(hrd.getUser());
				}
			});

			ViewUtil.setRichText(holder.name, StringUtil.color(hrd.getUser()
					.getNickName()
					+ "  ("
					+ hrd.getUser().getCountryName()
					+ ")", position == 0 ? R.color.color22 : R.color.color16));
		}
	}

	protected void setGuildInfo(ViewHolder holder, final HonorRankData hrd,
			int position) {
		if (null != hrd.getGuild()) {
			ViewUtil.setRichText(holder.guild, StringUtil.color("家族:"
					+ hrd.getGuild().getName(), position == 0 ? R.color.color9
					: R.color.color14));
			ViewUtil.setUnderLine(holder.guild);
			holder.guild.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new GuildInfoWindow().open(hrd.getGuild().getId());
				}
			});
		} else {
			ViewUtil.setRichText(holder.guild, StringUtil.color("家族: 无",
					position == 0 ? R.color.color9 : R.color.color14));
			ViewUtil.setTextViewNormal(holder.guild);
			holder.guild.setOnClickListener(null);
		}
	}

	protected void setDesc(ViewHolder holder, final HonorRankData hrd,
			int position) {
		ViewUtil.setRichText(holder.desc, StringUtil.color(getDesc(hrd),
				position == 0 ? R.color.color9 : R.color.color14));
	}

	protected void setRewardInfo(ViewHolder holder, final HonorRankData hrd) {
		if (null != hrd.getItem()) {
			new ViewImgScaleCallBack(hrd.getItem().getImage(), holder.reward,
					70 * Config.SCALE_FROM_HIGH, 70 * Config.SCALE_FROM_HIGH);
			holder.reward.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new HonorRankRewardTip(hrd.getItem()).show();
				}
			});
		} else {
			ViewUtil.setImage(holder.reward, null);
			holder.reward.setOnClickListener(null);
		}

	}

	protected abstract String getDesc(HonorRankData hrd);

	@Override
	public int getLayoutId() {
		return R.layout.honor_rank_item;
	}

	class ViewHolder {
		View reward_item;
		View honor_rank_layout;
		View iconLayout;
		View rankFrame;
		TextView name;
		TextView guild;
		TextView desc;
		ImageView reward;
	}
}
