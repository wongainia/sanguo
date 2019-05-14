package com.vikings.sanguo.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroArmPropInfoClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.ui.window.HeroFavourWindow;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroFavourAdapter extends ObjectAdapter {

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.iconLayout = (ViewGroup) convertView
					.findViewById(R.id.iconLayout);
			holder.qualityName = (TextView) convertView
					.findViewById(R.id.qualityName);
			holder.heroName = (TextView) convertView
					.findViewById(R.id.heroName);
			holder.heroTime = (TextView) convertView
					.findViewById(R.id.herotime);
			holder.heroSkill = (TextView) convertView
					.findViewById(R.id.heroskill);

			holder.heroLayout = (ViewGroup) convertView
					.findViewById(R.id.heroLayout);
			holder.heroAttack = (TextView) convertView
					.findViewById(R.id.heroAttack);
			holder.heroDefent = (TextView) convertView
					.findViewById(R.id.heroDefent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ViewUtil.setGone(holder.heroLayout);
		HeroInfoClient heroInfo = (HeroInfoClient) getItem(position);

		IconUtil.setHeroIconScale(holder.iconLayout, heroInfo);
		holder.iconLayout
				.setOnClickListener(new OwnHeroClickListerner(heroInfo));
		ViewUtil.setRichText(holder.qualityName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(
				holder.heroName,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		// 参考将领属性列表HeroProp取值
		StringBuilder buf = new StringBuilder("将领熟练: ");
		List<HeroArmPropInfoClient> list = heroInfo.getArmPropInfos();
		for (HeroArmPropInfoClient hapic : list) {
			buf.append("#").append(hapic.getHeroTroopName().getSmallIcon())
					.append("# ").append(" ");
		}

		{
			int level = 0;
			if (heroInfo.getFavourInfoClient() != null) {
				level = heroInfo.getFavourInfoClient().getLevel();
			}
			ViewUtil.setText(holder.heroTime, "兴奋值LV" + level);
			// ViewUtil.setText(holder.heroTime, "LV" + heroInfo.getLevel());
			ViewUtil.setRichText(holder.heroSkill, buf.toString());
			ViewUtil.setVisible(holder.heroLayout);
			ViewUtil.setText(holder.heroAttack, R.id.heroAttack, "武力: "
					+ heroInfo.getRealAttack());// 攻击力
			ViewUtil.setText(holder.heroDefent, R.id.heroDefent, "防护: "
					+ heroInfo.getRealDefend());// 防御力
		}

		convertView.setOnClickListener(new ClickListener(heroInfo));
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_patronize_list_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout, heroLayout;
		TextView qualityName, heroName, heroTime, heroSkill, heroAttack,
				heroDefent;
	}

	private class ClickListener implements OnClickListener {
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic) {
			this.hic = hic;
		}

		@Override
		public void onClick(View v) {
			new HeroFavourWindow(hic).open();
		}

	}

}
