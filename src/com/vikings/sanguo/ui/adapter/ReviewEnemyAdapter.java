/**
 *  CopyRight 2012 Shenzhen Viking NetWork Tech Co., Ltd
 *  
 *  All right reserved.
 *
 *  Time : 2013-5-9 下午8:18:36
 *
 *  Author : Kircheis Chen
 *
 *  Comment : 
 */
package com.vikings.sanguo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikings.sanguo.R;
import com.vikings.sanguo.config.Config;
import com.vikings.sanguo.model.ArmInfoClient;
import com.vikings.sanguo.model.HeroArmPropClient;
import com.vikings.sanguo.model.HeroInfoClient;
import com.vikings.sanguo.model.OtherHeroInfoClient;
import com.vikings.sanguo.model.TroopProp;
import com.vikings.sanguo.protos.UserTroopEffectInfo;
import com.vikings.sanguo.ui.alert.TroopDetailTip;
import com.vikings.sanguo.utils.IconUtil;
import com.vikings.sanguo.utils.ListUtil;
import com.vikings.sanguo.utils.ViewUtil;

public class ReviewEnemyAdapter extends ObjectAdapter implements
		OnClickListener {
	private UserTroopEffectInfo troopEffectInfo;
	private List<HeroInfoClient> hic;
	private List<OtherHeroInfoClient> ohic;
	private List<HeroArmPropClient> armProps = new ArrayList<HeroArmPropClient>();

	public void setUserTroopEffectInfo(UserTroopEffectInfo troopEffectInfo) {
		this.troopEffectInfo = troopEffectInfo;
	}

	public void setHic(List<HeroInfoClient> hic) {
		this.hic = hic;
		armProps.clear();
		if (!ListUtil.isNull(hic)) {
			for (HeroInfoClient heroInfoClient : hic) {
				if (heroInfoClient.isValid())
					armProps.addAll(heroInfoClient.getArmPropClient());
			}
		}
	}

	public void setOhic(List<OtherHeroInfoClient> ohic) {
		this.ohic = ohic;
		armProps.clear();
		if (!ListUtil.isNull(ohic)) {
			for (OtherHeroInfoClient oheroInfoClient : ohic) {
				if (oheroInfoClient.isValid())
					armProps.addAll(oheroInfoClient.getArmPropClient());
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ArmInfoClient ai = (ArmInfoClient) getItem(position);

		if (convertView == null) {
			convertView = Config.getController().inflate(getLayoutId());
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.count = (TextView) convertView.findViewById(R.id.count);
			viewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
			viewHolder.armType = convertView.findViewById(R.id.armType);
			convertView.setTag(viewHolder);
			convertView.setOnClickListener(this);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.ai = ai;

		this.setViewDisplay(convertView, getItem(position), position);
		return convertView;
	}

	@Override
	public void setViewDisplay(View v, Object o, int index) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		ArmInfoClient ai = viewHolder.ai;
		TroopProp tp = ai.getProp();
		if (null != hic)
			IconUtil.setArmIcon(viewHolder.icon, ai, troopEffectInfo, hic);
		else if (null != ohic)
			IconUtil.setOtherArmIcon(viewHolder.icon, ai, troopEffectInfo, ohic);
		else
			IconUtil.setArmIcon(viewHolder.icon, ai, troopEffectInfo);
		ViewUtil.setText(viewHolder.name, tp.getName());
		ViewUtil.setText(viewHolder.count, "× " + ai.getCount());
		ViewUtil.setText(viewHolder.desc, tp.getDesc());

		if (HeroArmPropClient.isStrength(armProps, tp)) {
			ViewUtil.setVisible(viewHolder.armType);
			ViewUtil.setImage(viewHolder.armType, tp.getSmallIcon());
		} else
			ViewUtil.setGone(viewHolder.armType);
	}

	@Override
	public int getLayoutId() {
		return R.layout.review_enemy_item;
	}

	class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView count;
		public TextView desc;
		public ArmInfoClient ai;
		public View armType;
	}

	@Override
	public void onClick(View v) {
		if (v instanceof ViewGroup) {
			ArmInfoClient ai = ((ViewHolder) v.getTag()).ai;
			if (null != hic)
				new TroopDetailTip()
						.show(ai.getProp(), troopEffectInfo, hic, 0);
			else if (null != ohic)
				new TroopDetailTip().show(ai.getProp(), troopEffectInfo, ohic,
						0, null);
			else
				new TroopDetailTip().show(ai.getProp(), troopEffectInfo);
		}
	}
}
