package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.CacheMgr;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.HeroLevelUp;
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.ui.window.HeroDevourWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroDevourAdapter extends ObjectAdapter {

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
			holder.armProps = (TextView) convertView
					.findViewById(R.id.armProps);
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
		holder.iconLayout
				.setOnClickListener(new OwnHeroClickListerner(heroInfo));
		ViewUtil.setRichText(holder.qualityName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(
				holder.heroName,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		ViewUtil.setText(holder.heroLevel, "LV" + heroInfo.getLevel());

		HeroLevelUp levelUp = (HeroLevelUp) CacheMgr.heroLevelUpCache.getExp(
				heroInfo.getHeroType().getType(), heroInfo.getStar(),
				heroInfo.getLevel());
		if (null != levelUp) {
			holder.progressBar.set(heroInfo.getExp(), levelUp.getNeedExp());
			ViewUtil.setRichText(holder.progressDesc, heroInfo.getExp() + "/"
					+ levelUp.getNeedExp());
		}

		StringBuilder buf = new StringBuilder("擅长兵种：");
		List<HeroArmPropInfoClient> list = heroInfo.getArmPropInfos();
		for (HeroArmPropInfoClient hapic : list) {
			buf.append("#").append(hapic.getHeroTroopName().getSmallIcon())
					.append("#&nbsp;&nbsp;");
		}
		ViewUtil.setRichText(holder.armProps, buf.toString(), true);
		convertView.setOnClickListener(new ClickListener(heroInfo));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_devour_list_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout;
		TextView qualityName, heroName, heroLevel, progressDesc, armProps;
		ProgressBar progressBar;
	}

	private class ClickListener implements OnClickListener {
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic) {
			this.hic = hic;
		}

		@Override
		public void onClick(View v) {
			new HeroDevourWindow(hic).open();
		}

	}

}
