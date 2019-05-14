package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.thread.AddrLoader;
import com.vikings.sanguo.ui.listener.OwnHeroClickListerner;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.TileUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class AbandonHeroAdapter extends ObjectAdapter {
	// 分解将领 adapter
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
			holder.heroTick = (ImageView) convertView
					.findViewById(R.id.heroTick);
			holder.chooseLayout = (FrameLayout) convertView
					.findViewById(R.id.chooseLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final HeroInfoClient heroInfo = (HeroInfoClient) getItem(position);

		IconUtil.setHeroIconScale(holder.iconLayout, heroInfo);
		ViewUtil.setRichText(holder.qualityName, heroInfo.getColorTypeName());
		ViewUtil.setRichText(
				holder.heroName,
				StringUtil.getHeroName(heroInfo.getHeroProp(),
						heroInfo.getHeroQuality()));

		ViewUtil.setText(holder.heroLevel, "Lv" + heroInfo.getLevel());
		ViewUtil.setRichText(holder.state, heroInfo.getHeroState());

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
		holder.iconLayout.setOnClickListener(new OwnHeroClickListerner(
				heroInfo, true));

		if (heroInfo.getChooseAbandon()) {
			holder.heroTick.setVisibility(View.VISIBLE);
		} else {
			holder.heroTick.setVisibility(View.GONE);
		}
		final ImageView heroTick = holder.heroTick;
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ViewUtil.isVisible(heroTick)) {
					ViewUtil.setGone(heroTick);
				} else {
					ViewUtil.setVisible(heroTick);
				}
				boolean isChooseAbandon = heroInfo.getChooseAbandon();
				heroInfo.setChooseAbandon(!isChooseAbandon);
			}
		});
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.abandon_hero_item;
	}

	private static class ViewHolder {
		TextView qualityName, heroName, heroLevel, address, heroAttack,
				heroDefent, state;
		ViewGroup iconLayout;
		ImageView heroTick;
		FrameLayout chooseLayout;
	}
}
