package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class CampaignTroopAdapter extends ObjectAdapter {
	private UserTroopEffectInfo troopEffectInfo;
	private List<HeroArmPropClient> armProps = new ArrayList<HeroArmPropClient>();

	public void setTroopEffectInfo(UserTroopEffectInfo userTroopEffectInfo) {
		this.troopEffectInfo = userTroopEffectInfo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ArmInfoClient aic = (ArmInfoClient) getItem(position);
		TroopProp prop = aic.getProp();
		IconUtil.setArmIcon(holder.icon, aic, troopEffectInfo, null);
		ViewUtil.setText(holder.name, prop.getName());
		ViewUtil.setText(holder.count, ":" + aic.getCount());
		ViewUtil.setText(holder.desc, prop.getDesc());
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {

	}

	@Override
	public int getLayoutId() {
		return R.layout.campaign_troop_item;
	}

	private class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView count;
		public TextView desc;
	}
}
