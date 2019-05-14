package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.HeroProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.utils.ViewUtil;

public class HeroStatisticsAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.hero_statistics_grid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.starLayout = convertView.findViewById(R.id.starLayout);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.talent = (ImageView) convertView.findViewById(R.id.talent);
			holder.rank = (TextView) convertView.findViewById(R.id.rank);
			holder.grid_item = convertView.findViewById(R.id.grid_item);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HeroProp prop = (HeroProp) getItem(position);

		ViewUtil.setGone(holder.starLayout);
		ViewUtil.setGone(holder.talent);
		new ViewImgScaleCallBack(prop.getIcon(), holder.icon,
				Config.SCALE_FROM_HIGH * Constants.HERO_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.HERO_ICON_HEIGHT);
		// try {
		// HeroQuality heroQuality = (HeroQuality) CacheMgr.heroQualityCache
		// .get(prop.getType());
		// if (null != heroQuality) {
		// ViewUtil.setVisible(holder.rank);
		// convertView.setOnClickListener(new ClickListener(prop,
		// heroQuality));
		// } else {
		// convertView.setOnClickListener(null);
		// }
		// } catch (GameException e) {
		//
		// }
		holder.grid_item.setTag(prop);
		holder.grid_item.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Config.getController().openHeroDetailHDWindow(
						(HeroProp) v.getTag());
			}
		});
		ViewUtil.setText(holder.name, prop.getName());

		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
	}

	private static class ViewHolder {
		View starLayout;
		ImageView icon, talent;
		TextView rank;
		TextView name;
		View grid_item;
	}

}
