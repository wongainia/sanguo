/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 上午11:32:10
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HolyBattleStateClient;
import com.vikings.sanguo.model.RankProp;
import com.vikings.sanguo.model.UserRankInfoClient;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class UserRankAdapter extends ObjectAdapter implements OnClickListener {
	private RankProp rankProp;

	public UserRankAdapter(RankProp rankProp) {
		this.rankProp = rankProp;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.viewGroup = (ViewGroup) convertView
					.findViewById(R.id.honor_rank_layout);
			holder.iconLayout = convertView.findViewById(R.id.iconLayout);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			ViewUtil.setGone(convertView, R.id.reward_item);
			convertView.setTag(holder);
		}

		convertView.setOnClickListener(this);
		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		holder.viewGroup.setBackgroundResource(R.drawable.hero_add_bg);
		UserRankInfoClient uric = (UserRankInfoClient) o;
		holder.rankInfo = uric;

		setUserInfo(holder, uric);
		setRankNum(holder, uric);
		setGuildInfo(holder, uric);
		setDesc(holder, uric);
	}

	protected void setUserInfo(ViewHolder holder, UserRankInfoClient uric) {
		IconUtil.setUserIcon(holder.iconLayout, uric.getUser(),
				rankProp.getFirstName());
		ViewUtil.setText(holder.name, uric.getTitle());
	}

	protected void setRankNum(ViewHolder holder, UserRankInfoClient uric) {
		if (0 == indexOf(uric)) {
			IconUtil.setRankNum(holder.iconLayout, uric.getUser(),
					StringUtil.color(rankProp.getFirstName(), R.color.color13));
		} else {
			IconUtil.setUserIcon(holder.iconLayout, uric.getUser(), "NO."
					+ (indexOf(uric) + 1));
		}
	}

	protected void setGuildInfo(ViewHolder holder, UserRankInfoClient uric) {
		if (null != uric.getBgic()) {
			ViewUtil.setText(holder.guild, "家族:" + uric.getBgic().getName());
		} else
			ViewUtil.setText(holder.guild, "家族:无");
	}

	protected void setDesc(ViewHolder holder, UserRankInfoClient uric) {
		ViewUtil.setRichText(holder.desc, uric.getDesc(rankProp), true);
	}

	@Override
	public int getLayoutId() {
		return R.layout.honor_rank_item;
	}

	class ViewHolder {
		ViewGroup viewGroup;
		View iconLayout;
		TextView name;
		TextView guild;
		TextView desc;
		UserRankInfoClient rankInfo;
	}

	@Override
	public void onClick(View v) {
		UserRankInfoClient uric = ((ViewHolder) v.getTag()).rankInfo;
		Config.getController().showCastle(uric.getUser());
	}
}
