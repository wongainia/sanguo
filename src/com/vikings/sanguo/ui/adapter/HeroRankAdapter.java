/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-10-11 下午3:27:34
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.invoker.ReviewOtherHeroInvoker;
import com.vikings.sanguo.model.HeroInit;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.model.HeroQuality;
import com.vikings.sanguo.model.HeroRankInfoClient;
import com.vikings.sanguo.model.RankProp;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HeroRankAdapter extends ObjectAdapter {
	private RankProp rankProp;

	public HeroRankAdapter(RankProp rankProp) {
		this.rankProp = rankProp;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.heroIcon = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			// holder.rankFrame = convertView.findViewById(R.id.rankFrame);
			// ViewUtil.setVisible(holder.rankFrame);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.prof = (TextView) convertView.findViewById(R.id.prof);
			holder.lord = (TextView) convertView.findViewById(R.id.lord);
			holder.guild = (TextView) convertView.findViewById(R.id.guild);
			ViewUtil.setGone(convertView, R.id.reward);
			convertView.setTag(holder);
		}
		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder holder = (ViewHolder) v.getTag();
		final HeroRankInfoClient hric = (HeroRankInfoClient) o;

		setHeroInfo(holder, hric);
		setLordInfo(holder, hric);

		holder.heroIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ReviewOtherHeroInvoker(hric.getLord(), hric.getId())
						.start();
			}
		});

		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.getController().showCastle(hric.getLord());
			}
		});
	}

	private void setHeroInfo(ViewHolder holder, HeroRankInfoClient hric) {
		IconUtil.setHeroIcon(holder.heroIcon, hric.getHeroProp(),
				hric.getHeroQuality(), hric.getStar(), true);

		ViewUtil.setRichText(
				holder.name,
				StringUtil.getHeroTypeName(hric.getHeroQuality())
						+ "  "
						+ StringUtil.getHeroName(hric.getHeroProp(),
								hric.getHeroQuality()));
		ViewUtil.setRichText(holder.prof, "总统率:" + hric.getMaxArmValue(), true);
	}

	private void setLordInfo(ViewHolder holder, HeroRankInfoClient hric) {
		ViewUtil.setText(holder.lord, "领主:" + hric.getLordName());
		if (null != hric.getGuild()) {
			ViewUtil.setText(holder.guild, "家族:" + hric.getGuild().getName());
		} else
			ViewUtil.setText(holder.guild, "家族:无");
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_rank_item;
	}

	class ViewHolder {
		ViewGroup heroIcon;
		TextView name;
		TextView prof;
		TextView guild;
		TextView lord;
		// View rankFrame;
	}
}
