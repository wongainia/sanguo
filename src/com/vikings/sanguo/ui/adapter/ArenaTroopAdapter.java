package com.vikings.sanguo.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ArenaTroopAdapter extends ObjectAdapter {
	private UserTroopEffectInfo effectInfo;

	public ArenaTroopAdapter(UserTroopEffectInfo effectInfo) {
		this.effectInfo = effectInfo;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			ViewHolder holder = new ViewHolder();
			holder.icon = convertView.findViewById(R.id.icon);
			holder.name = convertView.findViewById(R.id.troopName);
			holder.count = convertView.findViewById(R.id.troopSum);
			convertView.setTag(holder);
		}

		setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ArmInfoClient ai = (ArmInfoClient) getItem(index);
		ViewHolder holder = (ViewHolder) v.getTag();
		IconUtil.setArmIcon(holder.icon, ai, effectInfo);
		ViewUtil.setText(holder.name, "" + ai.getProp().getName());
		ViewUtil.setText(holder.count, "×" + ai.getCount());
	}

	@Override
	public int getLayoutId() {
		return R.layout.war_info_list;
	}

	class ViewHolder {
		View icon, name, count;
	}
}
