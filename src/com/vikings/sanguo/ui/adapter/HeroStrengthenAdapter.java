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
import com.vikings.sanguo.ui.ProgressBar;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroStrengthenAdapter extends ObjectAdapter {

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
			holder.armPropLayout = (ViewGroup) convertView
					.findViewById(R.id.armPropLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HeroInfoClient hic = (HeroInfoClient) getItem(position);
		IconUtil.setHeroIconScale(holder.iconLayout, hic);
		holder.iconLayout.setOnClickListener(new OwnHeroClickListerner(hic));
		ViewUtil.setRichText(holder.talentName, hic.getColorTypeName());
		ViewUtil.setRichText(holder.name,
				StringUtil.getHeroName(hic.getHeroProp(), hic.getHeroQuality()));

		ViewUtil.setText(holder.level, "Lv:" + hic.getLevel());

		int viewCount = holder.armPropLayout.getChildCount();
		List<HeroArmPropInfoClient> list = hic.getArmPropInfos();
		if (viewCount >= list.size()) {
			for (int i = 0; i < viewCount; i++) {
				View view = holder.armPropLayout.getChildAt(i);
				if (i < list.size()) {
					ViewUtil.setVisible(view);
					setArmProp(view, list.get(i));
				} else {
					ViewUtil.setGone(view);
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (i < viewCount) {
					View view = holder.armPropLayout.getChildAt(i);
					ViewUtil.setVisible(view);
					setArmProp(view, list.get(i));
				} else {
					View view = Config.getController().inflate(
							R.layout.hero_strengthen_item_armprop);
					holder.armPropLayout.addView(view);
					setArmProp(view, list.get(i));
				}
			}

		}

		convertView.setOnClickListener(new ClickListener(hic));
		return convertView;
	}

	private void setArmProp(View view, HeroArmPropInfoClient hapic) {
		ViewUtil.setRichText(view.findViewById(R.id.desc),
				hapic.getArmPropSlug(false) + "统率:");
		ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);
		bar.set(hapic.getValue(), hapic.getMaxValue());
		ViewUtil.setText(view, R.id.progressDesc, hapic.getValue() + "/"
				+ hapic.getMaxValue());
	}

	@Override
	public int getLayoutId() {
		return R.layout.hero_strengthen_item;
	}

	private static class ViewHolder {
		ViewGroup iconLayout, armPropLayout;
		TextView talentName, name, level;
	}

	private class ClickListener implements OnClickListener {
		private HeroInfoClient hic;

		public ClickListener(HeroInfoClient hic) {
			this.hic = hic;
		}

		@Override
		public void onClick(View v) {
			Config.getController().openStrengthenWindow(hic);
		}

	}
}
