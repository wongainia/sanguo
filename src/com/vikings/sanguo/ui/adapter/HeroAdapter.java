package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroAdapter extends ObjectAdapter {

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.qualityName = (TextView) convertView
					.findViewById(R.id.qualityName);
			holder.heroName = (TextView) convertView
					.findViewById(R.id.heroName);
			holder.heroLevel = (TextView) convertView
					.findViewById(R.id.heroLevel);
			holder.heroAttack = (TextView) convertView
					.findViewById(R.id.heroAttack);
			holder.heroDefent = (TextView) convertView
					.findViewById(R.id.heroDefent);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			holder.progressDesc = (TextView) convertView
					.findViewById(R.id.progressDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HeroInfoClient heroInfo = (HeroInfoClient) getItem(position);

		IconUtil.setHeroIconScale(holder.iconLayout, heroInfo);
		ViewUtil.setRichText(holder.qualityName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(
				holder.heroName,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		ViewUtil.setText(holder.heroLevel, "Lv" + heroInfo.getLevel());
		ViewUtil.setRichText(holder.state, heroInfo.getHeroState());

		holder.progressBar.set(heroInfo.getStamina(),
				CacheMgr.heroCommonConfigCache.getMaxStamina());
		ViewUtil.setText(holder.progressDesc, heroInfo.getStamina() + "/"
				+ CacheMgr.heroCommonConfigCache.getMaxStamina());

		ViewUtil.setText(holder.heroAttack, R.id.heroAttack,
				"武力: " + heroInfo.getRealAttack());// 攻击力
		ViewUtil.setText(holder.heroDefent, R.id.heroDefent,
				"防护: " + heroInfo.getRealDefend());// 防御力

		if (heroInfo.getFiefid() != Account.manorInfoClient.getPos()) {
			new AddrLoader(holder.address, TileUtil.fiefId2TileId(heroInfo
					.getFiefid()), AddrLoader.TYPE_SUB);
		} else {
			ViewUtil.setText(holder.address, "主城");
		}
		convertView
				.setOnClickListener(new OwnHeroClickListerner(heroInfo, true));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_list_item;
	}

	private static class ViewHolder {
		TextView qualityName, heroName, heroLevel, address, progressDesc,
				heroAttack, heroDefent, state;
		ProgressBar progressBar;
		ViewGroup iconLayout;
	}
}
