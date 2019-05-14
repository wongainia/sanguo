package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.WorldLevelInfoClient;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.ui.window.HeroEvolveListWindow;
import com.vikings.sanguo.ui.window.HeroEvolveWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroEvolveAdapter extends ObjectAdapter {

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
			holder.talentName = (TextView) convertView
					.findViewById(R.id.talentName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.level = (TextView) convertView.findViewById(R.id.level);
			holder.armProps = (TextView) convertView
					.findViewById(R.id.armProps);
			holder.evolveDesc = (TextView) convertView
					.findViewById(R.id.evolveDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 引导模块
		if (HeroEvolveListWindow.mStep404CallBack != null && position == 0) {
			HeroEvolveListWindow.mStep404CallBack.onCall();
			HeroEvolveListWindow.mStep404CallBack = null;
		}

		HeroInfoClient heroInfo = (HeroInfoClient) getItem(position);
		IconUtil.setHeroIconScale(holder.iconLayout, heroInfo);
		holder.iconLayout
				.setOnClickListener(new OwnHeroClickListerner(heroInfo));
		ViewUtil.setRichText(holder.talentName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(
				holder.name,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		ViewUtil.setText(holder.level, "LV" + heroInfo.getLevel());
		if (heroInfo.isInBattle()) {
			ViewUtil.setRichText(holder.evolveDesc,
					StringUtil.color(heroInfo.getHeroState(), R.color.color11));
		} else if (heroInfo.isMaxStar()) {
			if (heroInfo.isMaxTalent()) {
				ViewUtil.setRichText(holder.evolveDesc,
						StringUtil.color("MAX", R.color.color11));
			} else if (null != WorldLevelInfoClient.getWorldLevelProp()
					&& heroInfo.getTalent() == WorldLevelInfoClient
							.getWorldLevelProp().getMaxHeroTalent()) {
				ViewUtil.setRichText(holder.evolveDesc,
						StringUtil.color("世界等级限制", R.color.color11));
			} else {
				ViewUtil.setRichText(
						holder.evolveDesc,
						"可进化为:"
								+ heroInfo.getEvolveTalentName()
								+ " "
								+ StringUtil
										.starStr(heroInfo.getStar() < Constants.HERO_MAX_STAR ? (heroInfo
												.getStar() + 1) : 1));
			}
		} else if (heroInfo.getLevel() < heroInfo.getEvolveLevel()) {
			ViewUtil.setRichText(holder.evolveDesc,
					StringUtil.color("等级不足", R.color.color11));
		} else {
			ViewUtil.setRichText(
					holder.evolveDesc,
					"可进化为:"
							+ heroInfo.getEvolveTalentName()
							+ " "
							+ StringUtil
									.starStr(heroInfo.getStar() < Constants.HERO_MAX_STAR ? (heroInfo
											.getStar() + 1) : 1));
		}

		StringBuilder buf = new StringBuilder("擅长兵种:");
		List<HeroArmPropInfoClient> list = heroInfo.getArmPropInfos();
		for (HeroArmPropInfoClient hapic : list) {
			buf.append("#").append(hapic.getHeroTroopName().getSmallIcon())
					.append("# ");
		}
		ViewUtil.setRichText(holder.armProps, buf.toString(), true);

		convertView.setOnClickListener(new ClickListener(heroInfo));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_evolve_list_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout;
		TextView talentName, name, level, evolveDesc, armProps;
	}

	private class ClickListener implements OnClickListener {
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic) {
			this.hic = hic;
		}

		@Override
		public void onClick(View v) {
			if (hic.isMaxStar() && hic.isMaxTalent()) {
				Config.getController().alert("该将领已进化至最高星级");
			} else if (hic.isInBattle()) {
				Config.getController().alert("该将领正在战场中，不能进化");
			} else if (hic.isMaxStar()
					&& null != WorldLevelInfoClient.getWorldLevelProp()
					&& hic.getTalent() == WorldLevelInfoClient
							.getWorldLevelProp().getMaxHeroTalent()) {
				Config.getController().alert("该将领已经进化到当前世界等级下的最高级");
			} else {
				new HeroEvolveWindow().open(hic);
			}
		}
	}

}
