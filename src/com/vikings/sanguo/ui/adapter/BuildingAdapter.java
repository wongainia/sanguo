package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.Constants;
import com.vikings.sanguo.R;
import com.vikings.sanguo.cache.Account;
import com.vikings.sanguo.cache.BuildingRequirementCache;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.BuildingInfoClient;
import com.vikings.sanguo.model.BuildingProp;
import com.vikings.sanguo.thread.ViewImgScaleCallBack;
import com.vikings.sanguo.ui.alert.BuildingBuyTip;
import com.vikings.sanguo.utils.StringUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class BuildingAdapter extends ObjectAdapter {

	@Override
	public int getLayoutId() {
		return R.layout.building_line;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BuildingProp buildingProp = (BuildingProp) getItem(position);
		BuildingInfoClient bic = Account.manorInfoClient
				.getBuilding(buildingProp);

		BuildingProp nextBuilding = buildingProp.getNextLevel();

		if (buildingProp.getLevel() <= 1
				&& (null == bic || null == nextBuilding)) {
			ViewUtil.setText(holder.name, buildingProp.getBuildingName());
		} else {
			ViewUtil.setText(holder.name, buildingProp.getBuildingName()
					+ " LV" + buildingProp.getLevel());
		}

		new ViewImgScaleCallBack(buildingProp.getImage(), holder.icon,
				Config.SCALE_FROM_HIGH * Constants.ITEM_ICON_WIDTH,
				Config.SCALE_FROM_HIGH * Constants.ITEM_ICON_HEIGHT);

		ViewUtil.setRichText(holder.desc, buildingProp.getDesc());

		if (null == bic) {
			ViewUtil.setRichText(holder.state,
					StringUtil.color("未解锁", R.color.color17));
		} else {
			if (nextBuilding == null) {
				ViewUtil.setRichText(holder.state,
						StringUtil.color("已满级", R.color.color6));
			} else {
				if (!BuildingRequirementCache.unlock(nextBuilding.getId(),
						nextBuilding.isCheckLv())) {
					ViewUtil.setRichText(holder.state,
							StringUtil.color("条件不足", R.color.color24));
				} else {
					ViewUtil.setRichText(holder.state,
							StringUtil.color("可升级", R.color.color19));
				}
			}
		}

		if (null == bic) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Config.getController().alert("该建筑未解锁");
				}
			});
		} else if (nextBuilding == null) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Config.getController().alert("该建筑已经满级");
				}
			});
		} else {
			convertView.setOnClickListener(new ClickListener(nextBuilding));
		}
		return convertView;
	}

	private class ClickListener implements OnClickListener {
		private BuildingProp prop;

		public ClickListener(BuildingProp prop) {
			this.prop = prop;
		}

		@Override
		public void onClick(View v) {
			new BuildingBuyTip(prop).show();
		}
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	static class ViewHolder {
		ImageView icon;
		TextView name, desc, state;
	}
}